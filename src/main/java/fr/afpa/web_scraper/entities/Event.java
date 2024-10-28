package fr.afpa.web_scraper.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.UUID;
import java.time.*;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne(targetEntity = Venue.class)
    @JoinColumn(name = "venue_id")
    private UUID venueId;

    private int musicalStyleId;

    public Event() {
    }

    public Event(UUID id, String name, LocalDateTime dateTime, UUID venueId, int musicalStyleId) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime; 
        this.venueId = venueId;
        this.musicalStyleId = musicalStyleId;
    }

    public UUID getId() {
        return id;
    }

    public Event setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Event setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public UUID getVenueId() {
        return venueId;
    }

    public Event setVenueId(UUID venueId) {
        this.venueId = venueId;
        return this;
    }

    public int getMusicalStyleId() {
        return musicalStyleId;
    }

    public Event setMusicalStyleId(int musicalStyleId) {
        this.musicalStyleId = musicalStyleId;
        return this;
    }

    

}
