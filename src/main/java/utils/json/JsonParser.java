package utils.json;

import entities.Conference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    /**
     * Go over JSON array of conferences and create conferences object list
     * @param json JSON-array with conference object representation
     * @param year the year when the conference will or will be
     * @return collection of conference objects.
     */
    public ArrayList<Conference> makeConferencesList(String json, int year, String conferenceType) {
        JSONArray conferenceList = new JSONArray(json);
        ArrayList<Conference> list = new ArrayList<>();

        for (Object conference : conferenceList) {
            list.add(this.getConference(conference, year, conferenceType));
        }

        return list;
    }

    /**
     * Make conference object from JSON.
     * @param json conference representation in JSON format
     * @param year the year when the conference will or will be
     * @return Conference object
     */
    private Conference getConference(Object json, int year, String conferenceType) {
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
            List<String> conferenceTypes = new ArrayList<String>();
            conferenceTypes.add(conferenceType);

            return new Conference(name, url, startDate, endDate, city, country, twitter, cfpUrl, cfpStartDate, cfpEndDate, year, conferenceTypes);
        } catch (JSONException exception) {
            System.out.println(exception.getMessage());
        }

        return null;
    }
}