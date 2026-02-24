package com.MedSetu.med_setu.Repository;

import com.MedSetu.med_setu.Model.DonationEntity;
import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
    List<DonationEntity> findAllByNgoId(Long id);
    Optional<DonationEntity> findByMedicine(MedicineEntity medicine);
    List<DonationEntity> findAllByDonorUsername(String username);
}
