package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.Status;
import com.MedSetu.med_setu.Model.DonationEntity;
import com.MedSetu.med_setu.Model.MedicineEntity;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.Optional;

public interface DonationService {
    Boolean createRow(MedicineEntity medicine, String username);
    Status updateRequest(Long donationId, Status status, String username);
    Optional<DonationEntity> fetchStatus(Long id);
}
