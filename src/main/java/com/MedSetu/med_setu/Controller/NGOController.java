package com.MedSetu.med_setu.Controller;

import com.MedSetu.med_setu.DTO.NGOResponseDTO;
import com.MedSetu.med_setu.Model.NGOEntity;
import com.MedSetu.med_setu.Services.NGOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/ngo")
public class NGOController {

    @Autowired
    private NGOService ngoService;

    @GetMapping("/{userid}/getngo")
    public NGOResponseDTO fetchNgo(@PathVariable Long userid){
        return ngoService.fetchNGO(userid);
    }

    @PostMapping("/ngoregister")
    public ResponseEntity<String> createNgo(@RequestBody NGOEntity ngoEntity, Principal principal){
        String username = principal.getName();
        Boolean created = ngoService.createNGO(ngoEntity, username);
        if(created){
            return ResponseEntity.ok("NGO registered with name : "+ngoEntity.getNgoname() + " License No : "+ngoEntity.getLicenseNo());

        }
        else{
            return ResponseEntity.status(500).body("Error while registering.");
        }
    }

    @PutMapping("/{userid}/editngo")
    public ResponseEntity<String> updateNgo(@PathVariable Long userid, @RequestBody NGOEntity ngoEntity){
        Boolean updated = ngoService.updateNGO(userid, ngoEntity);
        if(updated){
            return ResponseEntity.ok("Data updated : NGOName : "+ngoEntity.getNgoname() + " Address : "+ngoEntity.getAddress() + " City : "+ngoEntity.getCity() + " Pincode : "+ngoEntity.getCity() + " License NO. : "+ngoEntity.getLicenseNo());
        }
        return ResponseEntity.status(500).body("NGO data not updated");
    }
}
