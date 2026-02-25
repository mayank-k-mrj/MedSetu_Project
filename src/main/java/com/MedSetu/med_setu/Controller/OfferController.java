package com.MedSetu.med_setu.Controller;

import com.MedSetu.med_setu.DTO.OfferParams;
import com.MedSetu.med_setu.Model.OffersEntity;
import com.MedSetu.med_setu.Services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Parser;
import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {
    @Autowired
    private OfferService offerService;

    @PostMapping("/create")
    public OffersEntity createOffers(@RequestBody OfferParams offerParams){
        OffersEntity data = offerService.createOffer(offerParams);
        return data;
    }

    @GetMapping("/{medid}/alloffers")
    public List<OffersEntity> findAllOffers(@PathVariable Long medid){
        return offerService.getAllOffers(medid);
    }
}
