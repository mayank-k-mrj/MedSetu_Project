package com.MedSetu.med_setu.Controller;

import com.MedSetu.med_setu.DTO.NGOResponseDTO;
import com.MedSetu.med_setu.DTO.Status;
import com.MedSetu.med_setu.Model.DonationEntity;
import com.MedSetu.med_setu.Model.NGOEntity;
import com.MedSetu.med_setu.Services.DonationService;
import com.MedSetu.med_setu.Services.DonationServiceImp;
import com.MedSetu.med_setu.Services.NGOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ngo")
public class NGOController {

    @Autowired
    private NGOService ngoService;

    @Autowired
    private DonationService donationService;

    @GetMapping("/{userid}/getngo")
    public NGOResponseDTO fetchNgo(@PathVariable Long userid){
        return ngoService.fetchNGO(userid);
    }

    //Fetch all donations
    @GetMapping("/alldonations")
    public List<DonationEntity> fetchAllDons(Principal principal){
        return ngoService.fetchAllDonations(principal.getName());
    }

    //Registers NGO
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

    //Updates ngo data
    @PutMapping("/{userid}/editngo")
    public ResponseEntity<String> updateNgo(@PathVariable Long userid, @RequestBody NGOEntity ngoEntity){
        Boolean updated = ngoService.updateNGO(userid, ngoEntity);
        if(updated){
            return ResponseEntity.ok("Data updated : NGOName : "+ngoEntity.getNgoname() + " Address : "+ngoEntity.getAddress() + " City : "+ngoEntity.getCity() + " Pincode : "+ngoEntity.getCity() + " License NO. : "+ngoEntity.getLicenseNo());
        }
        return ResponseEntity.status(500).body("NGO data not updated");
    }

    //Updates status like PENDING -> APPROVED.
    @PutMapping("/{id}/statupdate")
    public Status updatingStatus(@PathVariable Long id, @RequestParam Status status, Principal principal){
        String username = principal.getName();
        Status stat = donationService.updateRequest(id, status, username);
        return stat;
    }
}
