package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.Status;
import com.MedSetu.med_setu.Model.MedicineEntity;
import org.springframework.scheduling.support.SimpleTriggerContext;

public interface DonationService {
    Boolean createRow(MedicineEntity medicine, String username);
    Status updateRequest(Long donationId, Status status, String username);
}
