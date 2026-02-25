package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.Status;
import com.MedSetu.med_setu.Model.DonationEntity;
import com.MedSetu.med_setu.Model.MedicineEntity;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.List;
import java.util.Optional;

public interface DonationService {
    Boolean createRow(MedicineEntity medicine, String username);
    Status updateRequest(Long donationId, Status status, String username);
    DonationEntity fetchStatus(Long id);
    List<DonationEntity> fetchAllStatus(String username);
}
