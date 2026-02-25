package com.MedSetu.med_setu.Repository;

import com.MedSetu.med_setu.Model.UsersAddressEntity;
import com.MedSetu.med_setu.Model.UsersEntity;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersAddressRepository extends JpaRepository<UsersAddressEntity, Long> {
    Optional<UsersAddressEntity> findByUser(UsersEntity users);

    Optional<UsersAddressEntity> findByUserId(Long id);
}
