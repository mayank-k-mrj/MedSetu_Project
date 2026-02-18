package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.ValidationStatus;
import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Model.UsersEntity;
import com.MedSetu.med_setu.Repository.MedicineRepository;
import com.MedSetu.med_setu.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MedicineServiceImp implements MedicineService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private OCRServiceImp ocrServiceImp;

    @Override
    public Boolean editDetails(MedicineEntity medicineEntity, Long id){
        MedicineEntity updateMedicine = medicineRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Row with id "+id + " doesn't exists."));

        updateMedicine.setImageUrl(medicineEntity.getImageUrl());
        updateMedicine.setName(medicineEntity.getName());
        updateMedicine.setBatchNumber(medicineEntity.getBatchNumber());
        updateMedicine.setExpiryDate(medicineEntity.getExpiryDate());
        updateMedicine.setValidationStatus(ValidationStatus.PENDING);

        medicineRepository.save(updateMedicine);
        return true;
    }

    @Override
    public List<MedicineEntity> fetchAllMeds(String username){
        UsersEntity users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username : "+username));

        return medicineRepository.findByUploadedbyId(users.getId());
    }

    @Override
    public MedicineEntity saveMedicine(MultipartFile file, String username){
        try{
            UsersEntity user = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username : "+username));

            //upload folder create
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
            File directory = new File(uploadDir);
            if(!directory.exists()){
                directory.mkdir();
            }

            //creating unique file name
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + filename;

            //saving the file
            file.transferTo(new File(filePath));

            //creating medicine entity
            MedicineEntity medicine = new MedicineEntity();
            medicine.setImageUrl(filePath);
            medicine.setUploadedby(user);

            String ocrText = ocrServiceImp.extractText(filePath);
            System.out.println("===== OCR RESULT =====");
            System.out.println(ocrText);

            //extraction logic
            String name = extractMedicineName(ocrText);
            String batch = extractBatchNumber(ocrText);
            String expiry = extractExpiryDate(ocrText);

            LocalDate expiryDate = convertToLocalDate(expiry);

            medicine.setName(name);
            medicine.setBatchNumber(batch);
            medicine.setExpiryDate(expiryDate);
            medicine.setValidationStatus(ValidationStatus.PENDING);


            MedicineEntity saved =  medicineRepository.save(medicine);
            return saved;

        }
        catch(Exception e){
            System.out.println("Error occured while file handling : "+e.getMessage());
            throw new RuntimeException("Data not saved.");
        }
    }

    @Override
    public void updateValidation(Long id, ValidationStatus validationStatus){
        MedicineEntity validateMed = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Row with id "+id + " doesn't exists."));

            validateMed.setValidationStatus(validationStatus);
            medicineRepository.save(validateMed);
    }

    @Override
    public String extractMedicineName(String text) {

        String[] lines = text.split("\n");

        for(String line : lines) {

            String cleaned = line.trim();

            if(cleaned.length() > 5 &&
                    !cleaned.toLowerCase().contains("batch") &&
                    !cleaned.toLowerCase().contains("exp") &&
                    !cleaned.toLowerCase().contains("mfg") &&
                    !cleaned.toLowerCase().contains("dosage")) {

                return cleaned;
            }
        }

        return "UNKNOWN";
    }

    @Override
    public String extractBatchNumber(String text) {

        Pattern pattern = Pattern.compile(
                "(BATCH\\s?NO\\.?|B\\.NO\\.?|BN\\:?)[^A-Z0-9]*([A-Z0-9]+)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if(matcher.find()) {
            return matcher.group(2);
        }

        return null;
    }

    @Override
    public String extractExpiryDate(String text) {

        // 1️⃣ Numeric format (12/2027 or 12-27)
        Pattern numericPattern = Pattern.compile("(\\d{2}[/-]\\d{2,4})");
        Matcher numericMatcher = numericPattern.matcher(text);

        if (numericMatcher.find()) {
            return numericMatcher.group(1);
        }

        // 2️⃣ Word month format (JAN 2027)
        Pattern wordPattern = Pattern.compile(
                "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\s+\\d{4}",
                Pattern.CASE_INSENSITIVE
        );

        Matcher wordMatcher = wordPattern.matcher(text);

        if (wordMatcher.find()) {
            return wordMatcher.group();
        }

        return null;
    }

    @Override
    public LocalDate convertToLocalDate(String expiry) {

        if(expiry == null)
            return null;

        try {

            expiry = expiry.trim(); // 🔥 IMPORTANT FIX

            // Handle JAN 2027 format
            if(expiry.matches("(?i)(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\s+\\d{4}")) {

                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("MMM yyyy")
                        .toFormatter(Locale.ENGLISH);

                YearMonth yearMonth = YearMonth.parse(expiry.trim(), formatter);

                return yearMonth.atEndOfMonth();
            }


            // Handle 12/27 format
            if(expiry.matches("\\d{2}/\\d{2}")) {

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("MM/yy");

                YearMonth yearMonth = YearMonth.parse(expiry, formatter);

                return yearMonth.atEndOfMonth();
            }

            // Handle 12/2027 format
            if(expiry.matches("\\d{2}/\\d{4}")) {

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("MM/yyyy");

                YearMonth yearMonth = YearMonth.parse(expiry, formatter);

                return yearMonth.atEndOfMonth();
            }

        } catch (Exception e) {
            System.out.println("Expiry conversion failed: " + e.getMessage());
        }

        return null;
    }


}
