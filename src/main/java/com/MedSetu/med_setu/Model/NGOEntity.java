package com.MedSetu.med_setu.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "ngos")
public class NGOEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngoname", nullable = false)
    private String ngoname;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "pincode", nullable = false)
    private Long pincode;

    @Column(name = "licenseno", nullable = false)
    private String licenseno;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private UsersEntity user;

    public NGOEntity(String ngoname, String address, String city, Long pincode, String licenseno) {
        this.ngoname = ngoname;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.licenseno = licenseno;
    }

    public NGOEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNgoname() {
        return ngoname;
    }

    public void setNgoname(String ngoname) {
        this.ngoname = ngoname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getLicenseNo() {
        return licenseno;
    }

    public void setLicenseNo(String licenseno) {
        this.licenseno = licenseno;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

}
