package com.MedSetu.med_setu.Controller;

import com.MedSetu.med_setu.DTO.ValidationStatus;
import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Services.MedicineService;
import org.junit.jupiter.params.ParameterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/fetchall")
    public List<MedicineEntity> fetchAllMedicines(Principal principal){
        return medicineService.fetchAllMeds(principal.getName());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMedicine(@RequestParam("file") MultipartFile file, Principal principal){
        String username = principal.getName();

        MedicineEntity saved =  medicineService.saveMedicine(file, username);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/validation")
    public String updateValidation(@PathVariable Long id, @RequestParam ValidationStatus validationStatus){
        medicineService.updateValidation(id, validationStatus);

        return "Validation updated successfully";
    }

    @PutMapping("/{id}/updatemed")
    public ResponseEntity<String> editmedicine(@RequestBody MedicineEntity medicineEntity, @PathVariable Long id){
        Boolean edited = medicineService.editDetails(medicineEntity, id);
        if(edited){
            return ResponseEntity.ok("Medicine details edited successfully.");
        }
        else{
            return ResponseEntity.status(500).body("Medicine details not updated");
        }
    }

}
