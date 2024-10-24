package fr.afpa.web_scraper.repositories;

import fr.afpa.web_scraper.entities.Event;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, UUID>{
}
