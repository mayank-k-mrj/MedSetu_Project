package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.Model.UsersEntity;
import com.MedSetu.med_setu.Repository.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService{

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Boolean createUser(UsersEntity usersEntity){
        try {
            if(usersRepository.findByUsername(usersEntity.getUsername()).isPresent()){
                throw new RuntimeException("User already exists with thi email : "+usersEntity.getUsername());
            }

            usersRepository.save(usersEntity);

            return true;
        }
        catch(Exception e){
            System.out.println("Error Caught : "+e.getMessage());
        }
        return false;
    }

    public UsersEntity fetchUser(String username){
        UsersEntity usersEntity = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with email : "+username));

        return usersEntity;
    }
}
