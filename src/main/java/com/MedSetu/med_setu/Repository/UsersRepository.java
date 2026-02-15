package com.MedSetu.med_setu.Repository;

import com.MedSetu.med_setu.Model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity,String> {
    Optional<UsersEntity> findByUsername(String email);
}
