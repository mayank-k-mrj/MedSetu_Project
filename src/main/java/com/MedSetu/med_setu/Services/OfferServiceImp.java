package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.OfferParams;
import com.MedSetu.med_setu.DTO.Status;
import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Model.NGOEntity;
import com.MedSetu.med_setu.Model.OffersEntity;
import com.MedSetu.med_setu.Repository.MedicineRepository;
import com.MedSetu.med_setu.Repository.NGORepository;
import com.MedSetu.med_setu.Repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImp implements OfferService{

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private NGORepository ngoRepository;

    @Override
    public OffersEntity createOffer(OfferParams offerParams) {

        MedicineEntity med = medicineRepository.findById(offerParams.medid())
                .orElseThrow(() -> new RuntimeException(
                        "Medicine not found with id : " + offerParams.medid()));

        NGOEntity ngo_row = ngoRepository.findById(offerParams.ngoid())
                .orElseThrow(() -> new RuntimeException(
                        "Ngo not found with id : " + offerParams.ngoid()));

        OffersEntity offers = new OffersEntity();

        offers.setName(med.getName());
        offers.setOffered_price(offerParams.price());
        offers.setStatus(Status.PENDING);

        offers.setMedicine(med);
        offers.setDonor(med.getUploadedby());
        offers.setNgo(ngo_row);

        return offerRepository.save(offers);
    }

    @Override
    public List<OffersEntity> getAllOffers(Long medicine){

        return offerRepository.findAllByMedicineId(medicine);
    }
}
