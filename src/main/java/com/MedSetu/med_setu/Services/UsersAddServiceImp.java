package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.Model.UsersAddressEntity;
import com.MedSetu.med_setu.Model.UsersEntity;
import com.MedSetu.med_setu.Repository.UsersAddressRepository;
import com.MedSetu.med_setu.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersAddServiceImp implements UsersAddressService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersAddressRepository usersAddressRepository;

    @Override
    public void addDetails(UsersAddressEntity usersAddress, String username){
        UsersEntity users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username : "+username));

        UsersAddressEntity usersAddress1 = new UsersAddressEntity();

        usersAddress1.setUser(users);
        usersAddress1.setCity(usersAddress.getCity());
        usersAddress1.setPincode(usersAddress.getPincode());
        usersAddress1.setAddress(usersAddress.getAddress());

        usersAddressRepository.save(usersAddress1);
        users.setProfilecomplete(true);
        usersRepository.save(users);
    }
}
