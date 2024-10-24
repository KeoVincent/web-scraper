package fr.afpa.web_scraper.services;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import fr.afpa.web_scraper.HomeConnection;
import fr.afpa.web_scraper.entities.Event;
import fr.afpa.web_scraper.repositories.EventRepository;

@Service
public class ScraperService {

    private final EventRepository eventRepository;

    private Document doc = scrapAll();

    public ScraperService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        getNames();
    }

    // public ScraperService() {
    //     // scrapAll();
    //     // System.out.println(doc.text());

    // }

    public Document scrapAll() {
        Connection con = HomeConnection.getInstance();
        try {
            Document docTry = con.get();
            return docTry;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public void getNames() {
        Elements names = doc.select("span[class='titre']");
        // eventRepository.save(new Event().setName(names.getFirst().text()));
        for (Element name : names) {
            eventRepository.save(new Event().setName(name.text()));
        }
    }

    // doc.select("span[class='titre']").forEach(System.out::println);
    // System.out.println(doc.select("span[class='titre']").getFirst());
    // Elements dates = doc.select("div[class='row date-row']");
    // Elements concerts = dates.select("div.lieu");
    // System.out.println(concerts.text());

}
