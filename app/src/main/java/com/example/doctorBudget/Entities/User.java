package com.example.doctorBudget.Entities;


import android.graphics.Bitmap;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import androidx.room.TypeConverters;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;


@Entity (foreignKeys = @ForeignKey(entity = Country.class,
        parentColumns = "countryName",
        childColumns = "country",
        onDelete = CASCADE))
public class User {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int userID;
    private String lastName;
    private String firstName;
    private String email;
    @TypeConverters({Converters.class})
    public Date date_of_birth;
    private String country;
    private String occupation;
    @TypeConverters({Converters.class})
    private Bitmap profilePicture;

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @NonNull

    public int getUserID() {
        return userID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }
}