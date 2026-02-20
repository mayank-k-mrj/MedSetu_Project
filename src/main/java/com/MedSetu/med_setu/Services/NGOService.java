package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.NGOResponseDTO;
import com.MedSetu.med_setu.Model.DonationEntity;
import com.MedSetu.med_setu.Model.NGOEntity;
import org.hibernate.bytecode.enhance.internal.tracker.NoopCollectionTracker;

import java.util.List;

public interface NGOService {

    Boolean createNGO(NGOEntity ngoEntity, String username);
    NGOResponseDTO fetchNGO(Long userId);
    Boolean updateNGO(Long userid, NGOEntity ngoEntity);
    List<DonationEntity> fetchAllDonations(String username);
}
