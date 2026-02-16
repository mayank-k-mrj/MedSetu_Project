package com.MedSetu.med_setu.Repository;

import com.MedSetu.med_setu.Model.NGOEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NGORepository extends JpaRepository<NGOEntity, Long> {
    Optional<NGOEntity> findByLicenseno(String licenceno);
    Optional<NGOEntity> findByUserId(Long id);
}
