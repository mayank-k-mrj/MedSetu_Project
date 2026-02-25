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
    public Boolean createRow(MedicineEntity medicine, String username) {
        try {
            // 1. Get the Donor (User)
            UsersEntity user = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username : " + username));

            NGOEntity assignedNgo = null;

            try {
                // 2. Try to get user address to find NGO in the same city
                Optional<UsersAddressEntity> addressOpt = usersAddressRepository.findByUserId(user.getId());

                if (addressOpt.isPresent()) {
                    String userCity = addressOpt.get().getCity();

                    // Case-insensitive city matching
                    List<NGOEntity> allNgos = ngoRepository.findAll();
                    for (NGOEntity ngo : allNgos) {
                        if (ngo.getCity() != null && ngo.getCity().equalsIgnoreCase(userCity)) {
                            assignedNgo = ngo;
                            break; // City match ho gayi
                        }
                    }
                }
            } catch (Exception addrEx) {
                System.out.println("Address Warning: " + addrEx.getMessage());
            }

            // 3. Fallback: Agar User ki city me NGO nahi hai, toh DB ka Pehla NGO assign kar do
            if (assignedNgo == null) {
                List<NGOEntity> allNgos = ngoRepository.findAll();
                if (!allNgos.isEmpty()) {
                    assignedNgo = allNgos.get(0);
                } else {
                    throw new RuntimeException("CRITICAL: Koi bhi NGO database me registered nahi hai!");
                }
            }

            // 4. Create and Save the Donation Row
            DonationEntity donationEntity = new DonationEntity();
            donationEntity.setDonor(user);
            donationEntity.setMedicine(medicine);
            donationEntity.setStatus(Status.PENDING);
            donationEntity.setNgo(assignedNgo);

            donationRepository.save(donationEntity);
            System.out.println("SUCCESS: Donation linked to NGO -> " + assignedNgo.getNgoname());

            return true;

        } catch (Exception e) {
            System.err.println("ERROR while assigning NGO / creating donation: " + e.getMessage());
            e.printStackTrace(); // Exact error console me dikhayega
        }

        return false;
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
