package com.crime.cout.Models;

import android.support.annotation.NonNull;

public class CrimeModel {
   String crimeName;
   String crimeType;
   String zipCode;
   String latitude;
   String longitude;

   //CAN DELETE DEFAULT CONST
    public CrimeModel() {
    }

    public CrimeModel(String crimeName, String crimeType, String zipCode, String latitude, String longitude) {
        this.crimeName = crimeName;
        this.crimeType = crimeType;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    //Added 8/28/22 - ToString();
    @NonNull
    @Override
    public String toString() {

        StringBuilder string = new StringBuilder();
        string.append("\n-- Crime Model Object --\n");
        string.append("Crime Name: " + crimeName + "\n");
        string.append("Crime Type: " + crimeType + "\n");
        string.append("Crime Zip: " + zipCode + "\n");
        string.append("Latitude: " + latitude + "\n");
        string.append("Longitude: " + longitude + "\n");
        string.append("--------------------------");

        return string.toString();
    }

    public String getCrimeName() {
        return crimeName;
    }

    public void setCrimeName(String crimeName) {
        this.crimeName = crimeName;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
