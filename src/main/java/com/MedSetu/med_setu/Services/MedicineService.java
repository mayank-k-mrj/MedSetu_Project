package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.ValidationStatus;
import com.MedSetu.med_setu.Model.MedicineEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface MedicineService {
    MedicineEntity saveMedicine(MultipartFile file, String username);
    String extractMedicineName(String text);
    String extractBatchNumber(String text);
    String extractExpiryDate(String text);
    LocalDate convertToLocalDate(String expiry);
    Boolean editDetails(MedicineEntity medicineEntity, Long id);
    List<MedicineEntity> fetchAllMeds(String username);
    void updateValidation(Long id, ValidationStatus validationStatus);
}
