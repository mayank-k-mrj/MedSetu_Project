package com.MedSetu.med_setu.Model;

import com.MedSetu.med_setu.DTO.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation")
public class DonationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // Ye puraane wale hata dena (jisme WRITE_ONLY laga hai) aur inko laga dena:

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"uploadedby"})
    private MedicineEntity medicine;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"password", "donations", "role"})
    private UsersEntity donor;

    @ManyToOne
    @JoinColumn(name = "ngo_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"user"})
    private NGOEntity ngo;

    @CreationTimestamp
    private LocalDateTime created_at;

    public DonationEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MedicineEntity getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineEntity medicine) {
        this.medicine = medicine;
    }

    public UsersEntity getDonor() {
        return donor;
    }

    public void setDonor(UsersEntity donor) {
        this.donor = donor;
    }

    public NGOEntity getNgo() {
        return ngo;
    }

    public void setNgo(NGOEntity ngo) {
        this.ngo = ngo;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
