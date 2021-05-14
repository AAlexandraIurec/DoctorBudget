package com.example.doctorBudget.DAOs;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.doctorBudget.Entities.User;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {
    //Insert query
    @Insert(onConflict = REPLACE)
    void inserUser(User user);

    //Delete query
    @Delete
    void deleteUser(User user);

    //Update query
    @Query("UPDATE User SET lastName= :sLastName, firstName =:sFirstName, email=:sEmail, occupation=:sOccupation WHERE userID = :sUserID ")
    void updateUser(int sUserID, String sLastName, String sFirstName, String sEmail, String sOccupation);

    // Get all data query
    @Query("SELECT userID, lastName, firstName, email, occupation, date_of_birth, profilePicture, country FROM User")
    List<User> getAllUsers();
}
