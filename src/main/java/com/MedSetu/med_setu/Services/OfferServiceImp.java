package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.OfferParams;
import com.MedSetu.med_setu.DTO.OfferStatus;
import com.MedSetu.med_setu.DTO.ProductStatus;
import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Model.NGOEntity;
import com.MedSetu.med_setu.Model.OffersEntity;
import com.MedSetu.med_setu.Repository.MedicineRepository;
import com.MedSetu.med_setu.Repository.NGORepository;
import com.MedSetu.med_setu.Repository.OfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OfferServiceImp implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private NGORepository ngoRepository;

    // ================= CREATE OFFER =================

    @Override
    public OffersEntity createOffer(Long medid, Long ngoid, OfferParams offerParams) {

        MedicineEntity med = medicineRepository.findById(medid)
                .orElseThrow(() ->
                        new RuntimeException("Medicine not found"));

        if ("SOLD".equals(med.getStatus())) {
            throw new RuntimeException("Medicine already sold");
        }

        NGOEntity ngo = ngoRepository.findById(ngoid)
                .orElseThrow(() ->
                        new RuntimeException("NGO not found"));

        OffersEntity offer = new OffersEntity();

        offer.setName(med.getName());
        offer.setOffered_price(offerParams.price());
        offer.setStatus(OfferStatus.PENDING);
        offer.setMedicine(med);
        offer.setDonor(med.getUploadedby());
        offer.setNgo(ngo);

        return offerRepository.save(offer);
    }

    // ================= GET OFFERS =================

    @Override
    public List<OffersEntity> getAllOffers(Long medicineId) {
        return offerRepository.findAllByMedicineId(medicineId);
    }

    @Override
    public List<OffersEntity> getOffersByNgo(Long ngoId) {
        return offerRepository.findAllByNgo_Id(ngoId);
    }

    // ================= ACCEPT OFFER =================

    @Override
    public OffersEntity acceptOffer(Long offerid) {
        OffersEntity offer = offerRepository.findById(offerid)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        MedicineEntity med = offer.getMedicine();
        offer.setStatus(OfferStatus.ACCEPTED);
        med.setStatus(ProductStatus.SOLD); // Medicine ko sold mark karo

        medicineRepository.save(med);
        return offerRepository.save(offer);
    }

    // ================= REJECT OFFER =================
    @Override
    public OffersEntity rejectOffer(Long offerId) {
        // 1. Offer dhundo
        OffersEntity offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        // 2. Check karo ki kahin ye pehle se accepted to nahi hai
        if (offer.getStatus() == OfferStatus.ACCEPTED) {
            throw new RuntimeException("Accepted offer cannot be rejected");
        }

        // 3. Status badlo
        offer.setStatus(OfferStatus.REJECTED);

        // 4. Sabse zaroori: DB mein save karo taaki change reflect ho
        return offerRepository.save(offer);
    }

    // ================= COUNTER OFFER =================

    @Override
    public OffersEntity counterOffer(Long offerId, Double counterPrice) {
        OffersEntity offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        // PENDING ya COUNTERED dono cases mein allow karo
        if (offer.getStatus() != OfferStatus.PENDING && offer.getStatus() != OfferStatus.COUNTERED) {
            throw new RuntimeException("Counter not allowed for current status: " + offer.getStatus());
        }

        if (counterPrice == null || counterPrice <= 0) {
            throw new RuntimeException("Invalid counter price");
        }

        offer.setCounter_price(counterPrice);
        offer.setStatus(OfferStatus.COUNTERED);

        return offerRepository.save(offer);
    }

    // ================= NGO ACCEPT COUNTER =================
    @Override
    public OffersEntity ngoAcceptCounter(Long offerId) {
        OffersEntity offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        if (offer.getStatus() != OfferStatus.COUNTERED) {
            throw new RuntimeException("Only COUNTERED offer allowed");
        }

        MedicineEntity medicine = offer.getMedicine();

        // Data Update logic
        offer.setOffered_price(offer.getCounter_price());
        offer.setCounter_price(null);
        offer.setStatus(OfferStatus.ACCEPTED);
        medicine.setStatus(ProductStatus.SOLD);

        // IMPORTANT: Dono repositories ko save karo
        medicineRepository.save(medicine);
        offerRepository.save(offer);

        List<OffersEntity> all = offerRepository.findAllByMedicineId(medicine.getId());
        for (OffersEntity o : all) {
            if (!o.getId().equals(offerId)) {
                o.setStatus(OfferStatus.REJECTED);
            }
        }
        offerRepository.saveAll(all);
        return offer;
    }
}