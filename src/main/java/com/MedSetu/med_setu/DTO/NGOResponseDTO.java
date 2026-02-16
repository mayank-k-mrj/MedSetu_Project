package com.MedSetu.med_setu.DTO;

public record NGOResponseDTO(
        Long id,
        String ngoname,
        String address,
        String city,
        Long pincode,
        String licenseNo,
        Long userid
) {
}
