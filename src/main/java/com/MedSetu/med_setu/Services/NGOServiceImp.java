package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.NGOResponseDTO;
import com.MedSetu.med_setu.Model.DonationEntity;
import com.MedSetu.med_setu.Model.NGOEntity;
import com.MedSetu.med_setu.Model.UsersEntity;
import com.MedSetu.med_setu.Repository.DonationRepository;
import com.MedSetu.med_setu.Repository.NGORepository;
import com.MedSetu.med_setu.Repository.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NGOServiceImp implements NGOService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private NGORepository ngoRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Override
    public Boolean createNGO(NGOEntity ngoEntity, String username) {
        try{
            if(ngoRepository.findByLicenseno(ngoEntity.getLicenseNo()).isPresent()){
                throw new RuntimeException("NGO already registered with this licence no : "+ngoEntity.getLicenseNo());
            }
            UsersEntity user = usersRepository.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));

            ngoEntity.setUser(user);

            ngoRepository.save(ngoEntity);
            return true;
        }
        catch (Exception e){
            System.out.println("Error with NGO : "+e.getMessage());
        }
        return false;
    }

    public NGOResponseDTO fetchNGO(Long id){
        NGOEntity present = ngoRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("NGO not found wiht userid : "+id));
        return new NGOResponseDTO(
                present.getId(),
                present.getNgoname(),
                present.getAddress(),
                present.getCity(),
                present.getPincode(),
                present.getLicenseNo(),
                present.getUser().getId()
        );
    }

    public Boolean updateNGO(Long userid, NGOEntity ngoEntity){
        NGOEntity existingNgo = ngoRepository.findByUserId(userid)
                .orElseThrow(() -> new RuntimeException("NGO not found with UserId : "+userid));

        existingNgo.setNgoname(ngoEntity.getNgoname());
        existingNgo.setAddress(ngoEntity.getAddress());
        existingNgo.setCity(ngoEntity.getCity());
        existingNgo.setPincode(ngoEntity.getPincode());
        existingNgo.setLicenseNo(ngoEntity.getLicenseNo());

        ngoRepository.save(existingNgo);
        return true;
    }

    @Override
    public List<DonationEntity> fetchAllDonations(String username){
        UsersEntity user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username : "+username));

        NGOEntity ngo = ngoRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("NGO not found with user_id :"+user.getId()));

        return donationRepository.findAllByNgoId(ngo.getId());
    }
}
