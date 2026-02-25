package com.MedSetu.med_setu.Model;

import com.MedSetu.med_setu.DTO.OfferStatus;
import com.MedSetu.med_setu.DTO.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
public class OffersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double offered_price;

    private Double counter_price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status;

    private String medStat = "AVAILABLE";

    @CreationTimestamp
    private LocalDateTime created_at;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private MedicineEntity medicine;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private UsersEntity donor;

    @ManyToOne
    @JoinColumn(name = "ngo_id")
    private NGOEntity ngo;

    public OffersEntity() {
    }

    public OffersEntity(String name, Double offered_price, Double counter_price, OfferStatus status) {
        this.name = name;
        this.offered_price = offered_price;
        this.counter_price = counter_price;
        this.status = status;
    }

    public Long getNgoId() {
        return (ngo != null) ? ngo.getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getOffered_price() {
        return offered_price;
    }

    public void setOffered_price(Double offered_price) {
        this.offered_price = offered_price;
    }

    public Double getCounter_price() {
        return counter_price;
    }

    public void setCounter_price(Double counter_price) {
        this.counter_price = counter_price;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
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

    public String getMedStat() {
        return medStat;
    }

    public void setMedStat(String medStat) {
        this.medStat = medStat;
    }
}
