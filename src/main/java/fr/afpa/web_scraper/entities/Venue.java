package fr.afpa.web_scraper.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "venue")
public class Venue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String name;

    private String address;

    private int cityId;

    private String phone;

    private boolean isFestival;

    public Venue() {    
    }

    public Venue(UUID id, String name, String address, int cityId, String phone, boolean isFestival) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cityId = cityId;
        this.phone = phone;
        this.isFestival = isFestival;
    }

    public UUID getId() {
        return id;
    }

    public Venue setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Venue setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Venue setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getCityId() {
        return cityId;
    }

    public Venue setCityId(int cityId) {
        this.cityId = cityId;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Venue setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public boolean isFestival() {
        return isFestival;
    }

    public Venue setFestival(boolean isFestival) {
        this.isFestival = isFestival;
        return this;
    }

    @Override
    public String toString() {
        return "Venue [id=" + id + ", name=" + name + ", address=" + address + ", cityId=" + cityId + ", phone=" + phone
                + ", isFestival=" + isFestival + "]";
    }

    
    
}
