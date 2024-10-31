package fr.afpa.web_scraper.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import fr.afpa.web_scraper.entities.Event;
import fr.afpa.web_scraper.entities.Venue;
import fr.afpa.web_scraper.repositories.EventRepository;
import fr.afpa.web_scraper.repositories.VenueRepository;

@Service
public class ScraperService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private Connection con;
    // dependency injection in constructor
    public ScraperService(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        
        List<Document> hallDocuments = getHallDocuments();
        Venue currentVenue = new Venue();
        for (Document document : hallDocuments) {
            currentVenue = createVenue(document, false);
            createEvent(document, currentVenue); 
        }
    }
    
    public Document getDocument(String url) {
        con = Jsoup.connect(url);
        try {
            return con.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Document> getHallDocuments() {
        Document hallPage = getDocument("http://www.tyzicos.com/concerts-salles-bars/bretagne");
        Elements hallElements = hallPage.select("a.un-salle-bar");
        List<Document> hallDocuments = new ArrayList<>();
        for (Element hallElement : hallElements) {
            String hallLink = hallElement.attr("href");
            Document hallDocument = getDocument("http://www.tyzicos.com" + hallLink);
            hallDocuments.add(hallDocument);
        }
        return hallDocuments;
    }

     /**
     * Creates a Venue object from a Document object.
     * 
     */
    public Venue createVenue(Document venuePage, boolean isFestival) {

        // getting venue name
        String venueName = venuePage.select("div.title").text();

        // getting venue address span (contains phone too)
        String venueAddressPhone = venuePage.select("div.adress > span").text();

        // cutting phone from string
        String venueAddress = venueAddressPhone.split("\\d{2}( \\d{2}){4}")[0];

        // getting venue phone if present ; else set phone to null
        String venuePhone = null;
        Pattern phonePattern = Pattern.compile("\\d{2}( \\d{2}){4}");
        Matcher phoneMatcher = phonePattern.matcher(venueAddressPhone);
        if (phoneMatcher.find()) {
            venuePhone = phoneMatcher.group(0);
        }

        // creating venue with respective attributes
        Venue venue = new Venue().setName(venueName).setAddress(venueAddress).setPhone(venuePhone)
                .setFestival(isFestival);
        List<Venue> dbVenues = (List) venueRepository.findAll();
        if (!dbVenues.isEmpty()) {
            boolean exists = false;
            for (Venue dbVenue : dbVenues) {
                if (venue.getName().equals(dbVenue.getName())) {
                    exists = true;
                    System.out.println("ALREADY EXISTS Venue " + venue.getName() + "  ; id = " + dbVenue.getId());
                    return dbVenue;
                }
            }
            if (!exists) {
                venueRepository.save(venue);
                System.out.println("SAVING Venue " + venue.getName() + "  ; id = " + venue.getId());
                return venue;
            }
        } else {
            System.out.println("here");
            venueRepository.save(venue);
        }

        return venue;
    }

    public Event createEvent(Document document, Venue venue) {
        // System.out.println(document);
        Elements dateRows = document.select(".date-row");
        // System.out.println("ici" + dateRows);
        List<Event> dbEvents = (List) eventRepository.findAll();
        // date creation
        Event event = new Event();
        for (Element dateRow : dateRows) {
            Elements concertRows = dateRow.select(".one-concert");
            for (Element concertRow : concertRows) {
                LocalDateTime date = getDateTime(dateRow, concertRow);
                String name = concertRow.select(".titre").text();
                event.setName(name).setDateTime(date).setVenue(venue);

                if (!dbEvents.isEmpty()) {
                    boolean exists = false;
                    for (Event dbEvent : dbEvents) { 
                        if (event.getName().equals(dbEvent.getName())) {
                            exists = true;
                            System.out.println("ALREADY EXISTS Event " + event.getName() + "  ; id = " + dbEvent.getId());
                            return dbEvent;
                        }
                    }
                    if (!exists) {
                        eventRepository.save(event);
                        System.out.println("SAVING Event " + event.getName() + "  ; id = " + event.getId());
                        return event;
                    }
                } else {
                    eventRepository.save(event);
                }
            }
        }

        return event;
    }

    public LocalDateTime getDateTime(Element dateRow, Element concertRow) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        StringBuilder dateString = new StringBuilder();

        StringBuilder dateTimeString = new StringBuilder();

        // Building dateString
        dateString.append(dateRow.select("span.year").text() + "-");
        switch (dateRow.select("span.month").text()) {
            case "janvier":
                dateString.append("01-");
                break;
            case "février":
                dateString.append("02-");
                break;
            case "mars":
                dateString.append("03-");
                break;
            case "avril":
                dateString.append("04-");
                break;
            case "mai":
                dateString.append("05-");
                break;
            case "juin":
                dateString.append("06-");
                break;
            case "juillet":
                dateString.append("07-");
                break;
            case "août":
                dateString.append("08-");
                break;
            case "septembre":
                dateString.append("09-");
                break;
            case "octobre":
                dateString.append("10-");
                break;
            case "novembre":
                dateString.append("11-");
                break;
            case "décembre":
                dateString.append("12-");
                break;

            default:
                break;
        }

        if (dateRow.select("span.day-num").text().length() == 1) {
            dateString.append("0" + dateRow.select("span.day-num").text());
        } else {
            dateString.append(dateRow.select("span.day-num").text());
        }
        // dateString done : YYYY-MM-DD

        dateTimeString.append(dateString);
        Element time = concertRow.select("div.heure").getFirst();

        // System.out.println(time.text().substring(0, 5));
        String[] splitTime = {};
        if (time.text().length() < 5) {
            splitTime = time.text().substring(0, 4).split("h");
            dateTimeString.append(" 0" + splitTime[0] + ":" + splitTime[1]);
            
        } else {
            splitTime = time.text().substring(0, 5).split("h");
            dateTimeString.append(" " + splitTime[0] + ":" + splitTime[1]);
        }
        // System.out.println(time.text());

        // System.out.println(dateTimeString.toString());
        return LocalDateTime.parse(dateTimeString, formatter);

    }


}
