package com.MedSetu.med_setu.Repository;

import com.MedSetu.med_setu.Model.MedicineEntity;
import com.MedSetu.med_setu.Model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<MedicineEntity, Long> {
    @Override
    Optional<MedicineEntity> findById(Long id);
    List<MedicineEntity> findByUploadedbyId(Long id);
}
