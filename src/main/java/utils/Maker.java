package utils;

import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.*;

public class Maker {

    /**
     * @param conf Conference object
     * @return conference object as map representation
     */
    public static Map<String, Object> buildConferenceMapObject(entities.Conference conf) {
        Map<String, Object> confMap = new HashMap<>();

        if (conf.getFirebaseId() != null) {
            confMap.put("id", conf.getFirebaseId());
        }

        confMap.put("name", conf.getName());
        confMap.put("url", conf.getUrl());
        confMap.put("city", conf.getCity());
        confMap.put("country", conf.getCountry());
        confMap.put("twitter", conf.getTwitter());
        confMap.put("startDate", conf.getStartDate());
        confMap.put("endDate", conf.getEndDate());
        confMap.put("cfpUrl", conf.getCfpUrl());
        confMap.put("cfpStartDate", conf.getCfpStartDate());
        confMap.put("cfpEndDate", conf.getCfpEndDate());
        confMap.put("year", conf.getYear());

        return confMap;
    }

    /**
     * Create conference object for use in Transformer class.
     * @param document object from Firebase Database
     * @return Conference object
     */
    static entities.Conference buildConferenceFromDocument(QueryDocumentSnapshot document) {
        int year = Objects.requireNonNull(document.getLong("year")).intValue();
        String name = document.getString("name") == null ? "" : document.getString("name");
        String url = document.getString("url") == null ? "" : document.getString("url");
        String startDate = document.getString("startDate") == null ? "" : document.getString("startDate");
        String endDate = document.getString("endDate") == null ? "" : document.getString("endDate");
        String city = document.getString("city") == null ? "" : document.getString("city");
        String country = document.getString("country") == null ? "" : document.getString("country");
        String twitter = document.getString("twitter") == null ? "" : document.getString("twitter");
        String cfpUrl = document.getString("cfpUrl") == null ? "" : document.getString("cfpUrl");
        String cfpStartDate = document.getString("cfpStartDate") == null ? "" : document.getString("cfpStartDate");
        String cfpEndDate = document.getString("cfpEndDate") == null ? "" : document.getString("cfpEndDate");
        List<String> conferenceTypes = (ArrayList<String>) document.get("conferenceTypes");

        return new entities.Conference(name, url, startDate, endDate, city, country, twitter, cfpUrl, cfpStartDate, cfpEndDate, year, document.getId(), conferenceTypes);
    }
}
