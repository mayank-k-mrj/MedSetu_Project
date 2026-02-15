package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.Model.UsersEntity;
import org.springframework.stereotype.Service;

public interface UsersService {
    Boolean createUser(UsersEntity usersEntity);
    UsersEntity fetchUser(String email);
}
