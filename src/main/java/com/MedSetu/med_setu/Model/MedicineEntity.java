package com.MedSetu.med_setu.Model;

import com.MedSetu.med_setu.DTO.ValidationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicine")
public class MedicineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "name", nullable = false)
    private String name;

//    @Column(name = "expiryDate", nullable = false)
    private LocalDate expiryDate;

//    @Column(name = "batchNumber", nullable = false)
    private String batchNumber;

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "validationStatus", nullable = false)
    private ValidationStatus validationStatus;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private UsersEntity uploadedby;

    public MedicineEntity() {
    }

    public MedicineEntity(String name, LocalDate expiryDate, String batchNumber, String imageUrl, ValidationStatus validationStatus, LocalDateTime createdAt) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.batchNumber = batchNumber;
        this.imageUrl = imageUrl;
        this.validationStatus = validationStatus;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsersEntity getUploadedby() {
        return uploadedby;
    }

    public void setUploadedby(UsersEntity uploadedby) {
        this.uploadedby = uploadedby;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
