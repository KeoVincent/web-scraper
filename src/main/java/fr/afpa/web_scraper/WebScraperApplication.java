package fr.afpa.web_scraper;

import fr.afpa.web_scraper.entities.Event;
import fr.afpa.web_scraper.repositories.EventRepository;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.mediatype.alps.Doc;

import fr.afpa.web_scraper.services.ScraperServiceOld;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class WebScraperApplication {

	// @Autowired
	// private ScraperService scraper;

	@PostConstruct
	public void init() {

	}

	public static void main(String[] args) {
		
		SpringApplication.run(WebScraperApplication.class, args);

		
		
	}

}
