package fr.afpa.web_scraper.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

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
        // for (String date : getDateString()) {
        // System.out.println(date);
        // }
        // System.out.println(getDateElements().getFirst());
        // getDateTime(getDateElements().getFirst());
        createEvent();

    }

    public Document scrapAll() {
        Connection con = HomeConnection.getInstance();
        try {
            return con.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Selects from whole scrapped doc all the event titles, compares them to the
     * current DB state and adds a new title if not exists ; clunky but working
     * 
     */
    public void getNames() {

        List<Event> allEvents = (List) eventRepository.findAll();

        Elements names = doc.select("span[class='titre']");
        // eventRepository.save(new Event().setName(names.getFirst().text()));
        if (allEvents.size() > 0) {
            for (Element name : names) {
                boolean exists = false;
                for (Event event : allEvents) {
                    if (name.text().equals(event.getName())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    eventRepository.save(new Event().setName(name.text()));
                } else {
                    System.out.println(name.text() + " exists already");

                }
            }
        } else {
            for (Element name : names) {
                eventRepository.save(new Event().setName(name.text()));
            }

        }

    }

    /**
     * Gets date row and children elements
     * 
     * @return Elements
     */
    public Elements getDateElements() {
        return doc.select("div.date-row");
    }

    public List<String> getDateString() {

        ArrayList<String> datesList = new ArrayList<>();

        Elements dateElements = getDateElements();

        for (Element element : dateElements) {
            StringBuilder dateString = new StringBuilder();
            dateString.append(element.select("span.year").text() + "-");
            switch (element.select("span.month").text()) {
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
            if (element.select("span.day-num").text().length() == 1) {
                dateString.append("0" + element.select("span.day-num").text());
            } else {
                dateString.append(element.select("span.day-num").text());
            }
            datesList.add(dateString.toString());
        }
        return datesList;
    }

    // TODO implement getName
    // TODO implement getVenuId
    public boolean createEvent() {
        Elements dateRows = doc.select("div.date-row");
        // Elements eventRows = dateRows.select("div.concert-ctn");
        for (Element dateRow : dateRows) {
            Elements eventRows = dateRow.select("div.concert-ctn");
            for (Element eventRow : eventRows) {
                // eventRepository.save(new
                // Event().setDateTime(getDateTime(dateRow)).setName(getName(eventRow))
                // .setVenueId(getVenueId(venue)));
                System.out.println(getDateTime(dateRow, eventRow) + eventRow.text());
            }
        }
        return true;

    }

    // TODO implement getDateTime
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
        
        System.out.println((time.text().split("h"))[0]);

        String[] splitTime = time.text().split("h");
        // System.out.println(time.text());
        dateTimeString.append(" " + splitTime[0] + ":" + splitTime[1]);

        // System.out.println(dateTimeString);
        return LocalDateTime.parse(dateString + " 01:01", formatter);

    }
}

// doc.select("span[class='titre']").forEach(System.out::println);
// System.out.println(doc.select("span[class='titre']").getFirst());
// Elements dates = doc.select("div[class='row date-row']");
// Elements concerts = dates.select("div.lieu");
// System.out.println(concerts.text());
