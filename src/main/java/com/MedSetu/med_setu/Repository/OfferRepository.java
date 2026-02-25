package com.MedSetu.med_setu.Repository;

import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Model.OffersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<OffersEntity, Long> {
    List<OffersEntity> findAllByMedicineId(Long medicine);
    List<OffersEntity> findAllByNgo_Id(Long ngoId);
}
