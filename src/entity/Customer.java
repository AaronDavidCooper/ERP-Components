/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author AARON
 */

@Entity
@Table(name="OCST")
public class Customer {
    @Id
    @Column(name="acc")
    private String accRef;
    
    @Column(name="name")
    private String name;
    
    @Column(name="addressLine1")
    private String addressLine1;
    
    @Column(name="addressLine2")
    private String addressLine2;
    
    @Column(name="addressLine3")
    private String addressLine3;
    
    @Column(name="country")
    private String country;
    
    @Column(name="postcode")
    private String postcode;
    
    @Column(name="telephone")
    private String telephone;
    
    @Column(name="vatstatus")
    private boolean vatApplicable;
    
    public Customer(){
        
    }

    public Customer(String accRef, String name, String addressLine1, String addressLine2, String addressLine3, String country, String postcode, String telephone, boolean vatApplicable) {
        this.accRef = accRef;
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.country = country;
        this.postcode = postcode;
        this.telephone = telephone;
        this.vatApplicable = vatApplicable;
    }

    public String getAccRef() {
        return accRef;
    }

    public void setAccRef(String accRef) {
        this.accRef = accRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isVatApplicable() {
        return vatApplicable;
    }

    public void setVatApplicable(boolean vatApplicable) {
        this.vatApplicable = vatApplicable;
    }

    @Override
    public String toString() {
        return "Customer{" + "accRef=" + accRef + ", name=" + name + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", addressLine3=" + addressLine3 + ", country=" + country + ", postcode=" + postcode + ", telephone=" + telephone + ", vatApplicable=" + vatApplicable + '}';
    }
    
    
    
    public String getAddress() {
        return this.addressLine1 + ", " +this.addressLine2 + ", " + this.addressLine3 + ", " + this.country + ", " + this.postcode;
    }
    
    
    
}
