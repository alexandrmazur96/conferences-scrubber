import entities.Conference;
import utils.EnvConfig;
import utils.http.ConferenceGrabber;
import utils.json.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class Main {

    /**
     * Project - https://github.com/tech-conferences/conference-data
     */
    private static String url = "https://raw.githubusercontent.com/tech-conferences/conference-data/master/conferences/";

    public static void main(String[] args) throws Exception {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        if (args.length > 0) {
            year = Integer.parseInt(args[0]);
        }

        InputStream stream = Main.class.getResourceAsStream("/.env");
        EnvConfig envConfigUtil = new EnvConfig(stream);
        Map<String, String> config = envConfigUtil.getConfig();
        ConferenceGrabber confRequest = new ConferenceGrabber(url);
        JsonParser jsonParser = new JsonParser();

        String android = confRequest.grabConferences(year, "android");
        ArrayList<Conference> conferenceList = jsonParser.makeConferencesList(android, year);

        String credentialsPath = config.get("FIREBASE_CREDENTIALS");
        String databaseUrl = config.get("DATABASE_URL");
        String collectionName = config.get("COLLECTION_NAME");

        utils.firebase.Conference firebaseConference = new utils.firebase.Conference(credentialsPath, databaseUrl, collectionName);

        firebaseConference.processConferences(year, conferenceList);
    }
}