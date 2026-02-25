package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.OfferParams;
import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Model.NGOEntity;
import com.MedSetu.med_setu.Model.OffersEntity;

public interface OfferService {
    //offer cerate karne ke liye
    OffersEntity createOffer(OfferParams offerParams);
}
