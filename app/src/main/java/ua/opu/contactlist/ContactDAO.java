package ua.opu.contactlist;

import android.graphics.Color;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);

    @Query("DELETE FROM contacts WHERE id=:id")
    void deleteContactById(int id);

    @Query("SELECT * FROM contacts WHERE id=:id")
    Contact getContactById(int id);

    @Query("DELETE FROM contacts")
    void deleteAll();

    @Query("SELECT * FROM contacts")
    List<Contact> getAll();
}
