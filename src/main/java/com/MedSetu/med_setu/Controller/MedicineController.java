package com.MedSetu.med_setu.Controller;

import com.MedSetu.med_setu.DTO.ValidationStatus;
import com.MedSetu.med_setu.Model.DonationEntity;
import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Services.DonationService;
import com.MedSetu.med_setu.Services.MedicineService;
import org.junit.jupiter.params.ParameterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private DonationService donationService;
    
    @GetMapping("/{id}/fetchone")
    public Optional<DonationEntity> fetchOneStatus(@PathVariable Long id){
        return donationService.fetchStatus(id);
    }

    @GetMapping("/fetchall")
    public List<MedicineEntity> fetchAllMedicines(Principal principal){
        return medicineService.fetchAllMeds(principal.getName());
    }

    //Uploads image for OCR(Image reading).
    @PostMapping("/upload")
    public ResponseEntity<MedicineEntity> uploadMedicine(@RequestParam("file") MultipartFile file, Principal principal){
        String username = principal.getName();

        MedicineEntity saved =  medicineService.saveMedicine(file, username);

        return ResponseEntity.ok(saved);
    }

    //Updating validation
    @PutMapping("/{id}/validation")
    public String updateValidation(@PathVariable Long id, @RequestParam ValidationStatus validationStatus){
        medicineService.updateValidation(id, validationStatus);

        return "Validation updated successfully";
    }

    //Updated Medicine data after uploading.
    @PutMapping("/{id}/updatemed")
    public ResponseEntity<String> editmedicine(@RequestBody MedicineEntity medicineEntity, @PathVariable Long id, Principal principal){
        MedicineEntity updateMed = medicineService.editDetails(medicineEntity, id);
        if(updateMed != null){
            donationService.createRow(updateMed, principal.getName());
            return ResponseEntity.ok("Medicine details edited successfully.");

        }
        else{
            return ResponseEntity.status(500).body("Medicine details not edited");
        }
    }

}
