package com.example.goalfitness.Model;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;

public class UserLocation {

    private GeoFire geoFire ;
    private Customer_Register customer_register;

    public UserLocation(GeoLocation geoLocation, Customer_Register customer_register) {
        this.geoFire = geoFire;
        this.customer_register = customer_register;
    }

    public UserLocation(){

    }

    public GeoFire getGeoFire() {
        return geoFire;
    }

    public void setGeoFire(GeoFire geoFire) {
        this.geoFire = geoFire;
    }

    public Customer_Register getCustomer_register() {
        return customer_register;
    }

    public void setCustomer_register(Customer_Register customer_register) {
        this.customer_register = customer_register;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "geoFire=" + geoFire +
                ", customer_register=" + customer_register +
                '}';
    }
}
