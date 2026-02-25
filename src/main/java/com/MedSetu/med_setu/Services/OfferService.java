package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.OfferParams;
import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Model.NGOEntity;
import com.MedSetu.med_setu.Model.OffersEntity;

import java.util.List;

public interface OfferService {
    //offer cerate karne ke liye
    OffersEntity createOffer(OfferParams offerParams);
    //for fetching all offers
    List<OffersEntity> getAllOffers(Long medicine);
//    //for getting offers by ngo
//    List<OffersEntity> getOffersByNgo(Long ngoId);
//    //for accepting rejecting by user
//    OffersEntity acceptOffer(Long offerId);
//    //for counter offer
//    OffersEntity counterOffer(Long offerId, Double counterPrice);
//    //ngo accept order
//    OffersEntity ngoAcceptCounter(Long offerId);
}
