package com.MedSetu.med_setu.Services;

import com.MedSetu.med_setu.DTO.Role;
import com.MedSetu.med_setu.DTO.Status;
import com.MedSetu.med_setu.DTO.ValidationStatus;
import com.MedSetu.med_setu.Model.*;
import com.MedSetu.med_setu.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DonationServiceImp implements DonationService{

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private NGORepository ngoRepository;

    @Autowired
    private UsersAddressRepository usersAddressRepository;

    @Override
    public DonationEntity fetchStatus(Long id){
        MedicineEntity medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Medicine not found with id: " + id)
                );

        return donationRepository.findByMedicine(medicine)
                .orElseThrow(() ->
                        new RuntimeException("Donation not found for this medicine")
                );
    }

    @Override
    public List<DonationEntity> fetchAllStatus(String username){
        UsersEntity users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username : "+username));

        List<DonationEntity> donation = donationRepository.findAllByDonorUsername(username);

        return donation;
    }

    @Override
    public Boolean createRow(MedicineEntity medicine, String username){

        if (medicine.getValidationStatus() != ValidationStatus.VALID) {
            return false;
        }
        UsersEntity user = usersRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found with username : " + username));

        UsersAddressEntity address = usersAddressRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("User address not found"));

        Long userPincode = address.getPincode();
        String userCity = address.getCity();

        NGOEntity assignNGO = ngoRepository
                .findFirstByPincode(userPincode)
                .orElseGet(() ->
                        ngoRepository.findFirstByCity(userCity)
                                .orElseGet(() ->
                                        ngoRepository.findAll()
                                                .stream()
                                                .findFirst()
                                                .orElseThrow(() ->
                                                        new RuntimeException("No NGO available"))
                                )
                );

        DonationEntity donation = new DonationEntity();
        donation.setDonor(user);
        donation.setMedicine(medicine);
        donation.setNgo(assignNGO);
        donation.setStatus(Status.PENDING);

        donationRepository.save(donation);

        return true;
    }

    @Override
    public Status updateRequest(Long donationId, Status status, String username){
        UsersEntity user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().equals(Role.NGO)) {
            throw new RuntimeException("Access denied. Only NGOs can update donation status.");
        }

        NGOEntity ngo = ngoRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        DonationEntity donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found with id: " + donationId));

        if (!donation.getNgo().getId().equals(ngo.getId())) {
            throw new RuntimeException("You are not authorized to update this donation");
        }

        if (donation.getStatus() == Status.COMPLETED) {
            throw new RuntimeException("Completed donation cannot be updated");
        }

        if (!isValidTransition(donation.getStatus(), status)) {
            throw new RuntimeException("Invalid status transition");
        }

        donation.setStatus(status);

        donationRepository.save(donation);

        return status;
    }

    private boolean isValidTransition(Status current, Status next) {

        if (current == next) return false;

        // PENDING se
        if (current == Status.PENDING &&
                (next == Status.ACCEPTED || next == Status.REJECTED)) {
            return true;
        }

        // ACCEPTED se
        if (current == Status.ACCEPTED &&
                next == Status.COMPLETED) {
            return true;
        }

        return false;
    }
}
