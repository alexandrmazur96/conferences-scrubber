package utils.json;

import entities.Conference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    public ArrayList<Conference> getConferenceList(String json, int year) {
        JSONArray conferenceList = new JSONArray(json);
        ArrayList<Conference> list = new ArrayList<>();

        for (Object conference : conferenceList) {
            list.add(this.getConference(conference, year));
        }

        return list;
    }

    private Conference getConference(Object json, int year) {
        JSONObject jsonObject = new JSONObject(json.toString());
        
        try {
            String name = jsonObject.isNull("name") ? "" : jsonObject.getString("name");
            String url = jsonObject.isNull("url") ? "" : jsonObject.getString("url");
            String startDate = jsonObject.isNull("startDate") ? "" : jsonObject.getString("startDate");
            String endDate = jsonObject.isNull("endDate") ? "" : jsonObject.getString("endDate");
            String city = jsonObject.isNull("city") ? "" : jsonObject.getString("city");
            String country = jsonObject.isNull("country") ? "" : jsonObject.getString("country");
            String twitter = jsonObject.isNull("twitter") ? "" : jsonObject.getString("twitter");
            String cfpUrl = jsonObject.isNull("cfpUrl") ? "" : jsonObject.getString("cfpUrl");
            String cfpStartDate = jsonObject.isNull("cfpStartDate") ? "" : jsonObject.getString("cfpStartDate");
            String cfpEndDate = jsonObject.isNull("cfpEndDate") ? "" : jsonObject.getString("cfpEndDate");

            return new Conference(name, url, startDate, endDate, city, country, twitter, cfpUrl, cfpStartDate, cfpEndDate, year);
        } catch (JSONException exception) {
            System.out.println(exception.getMessage());
        }

        return null;
    }
}