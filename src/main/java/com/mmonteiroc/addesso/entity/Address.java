package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 15/06/2020
 * Package:com.mmonteiroc.addesso.entity
 * Project: addesso
 */
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaddress")
    private Long idAddress;

    @Column(name = "name")
    private String name;

    @Column(name = "street")
    private String street;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;   // Autonomic comunity....

    @Column(name = "country")
    private String country;

    @Column(name = "primary_address")
    private Boolean primary;

    @ManyToOne
    @JoinColumn(name = "user_iduser")
    @JsonBackReference
    private User userAddress;

    public Address() {
    }

    public Long getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(Long idAddress) {
        this.idAddress = idAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public User getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(User userAddress) {
        this.userAddress = userAddress;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
}
