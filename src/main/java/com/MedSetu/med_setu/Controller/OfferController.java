package com.MedSetu.med_setu.Controller;

import com.MedSetu.med_setu.DTO.CounterOfferDTO;
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

    @PostMapping("/{medid}/{ngoid}create")
    public OffersEntity createOffers(@PathVariable Long medid, @PathVariable Long ngoid,@RequestBody OfferParams offerParams){
        OffersEntity data = offerService.createOffer(medid, ngoid, offerParams);
        return data;
    }

    @GetMapping("/{medid}/alloffers")
    public List<OffersEntity> findAllOffers(@PathVariable Long medid){
        return offerService.getAllOffers(medid);
    }

    @GetMapping("/{ngoid}/allbyngo")
    public List<OffersEntity> findByNgoId(@PathVariable Long ngoid){
        return offerService.getOffersByNgo(ngoid);
    }

    @GetMapping("/{offerid}/accept")
    public OffersEntity acceptOffer(@PathVariable Long offerid){
        return offerService.acceptOffer(offerid);
    }

    @GetMapping("/{offerid}/reject")
    public OffersEntity rejectOffer(@PathVariable Long offerid){
        return offerService.rejectOffer(offerid);
    }

    @PutMapping("/{offerid}/counter")
    public OffersEntity counterOffer(@PathVariable Long offerid, @RequestBody CounterOfferDTO counterOfferDTO){
        return offerService.counterOffer(offerid, counterOfferDTO.counterPrice());
    }

    @PutMapping("/{offerId}/ngo-accept")
    public OffersEntity ngoAcceptCounter(@PathVariable Long offerId) {
        return offerService.ngoAcceptCounter(offerId);
    }
}
