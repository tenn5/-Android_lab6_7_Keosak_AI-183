package ua.opu.contactlist;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String email;
    private String phone;
    private String uri;

    @Ignore
    public Contact(String name, String email, String phone, String uri) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uri = uri;
    }

    @Ignore
    public Contact() {
    }

    public Contact(int id, String name, String email, String phone, String uri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
