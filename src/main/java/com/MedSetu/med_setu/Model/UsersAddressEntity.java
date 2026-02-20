package com.MedSetu.med_setu.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_details")
public class UsersAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Long pincode;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UsersEntity user;

    public UsersAddressEntity() {
    }

    public UsersAddressEntity(Long pincode, String city) {
        this.pincode = pincode;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }
}
