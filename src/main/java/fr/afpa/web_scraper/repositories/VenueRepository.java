package fr.afpa.web_scraper.repositories;

import fr.afpa.web_scraper.entities.Venue;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends CrudRepository<Venue, UUID>{
}
