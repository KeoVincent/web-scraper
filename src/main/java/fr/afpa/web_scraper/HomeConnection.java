package fr.afpa.web_scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public final class HomeConnection {
    
    private static Connection connection;

    private static String url = "http://www.tyzicos.com/";

    public static Connection getInstance() {
        if (connection == null ) {
            try {
                connection = Jsoup.connect(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
