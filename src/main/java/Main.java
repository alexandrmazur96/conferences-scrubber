import entities.Conference;
import utils.EnvConfig;
import utils.http.RequestWrapper;
import utils.json.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        if (args.length > 0) {
            year = Integer.parseInt(args[0]);
        }

        RequestWrapper confRequest = new RequestWrapper();
        JsonParser jsonParser = new JsonParser();

        InputStream stream = Main.class.getResourceAsStream("/.env");
        EnvConfig envConfigUtil = new EnvConfig(stream);
        Map<String, String> config = envConfigUtil.getConfig();

        String android = confRequest.makeRequest(year, "android");

        ArrayList<Conference> conferenceList = jsonParser.getConferenceList(android, year);

        String credentialsPath = config.get("FIREBASE_CREDENTIALS");
        String databaseUrl = config.get("DATABASE_URL");

        utils.firebase.Conference firebaseConference = new utils.firebase.Conference(credentialsPath, databaseUrl);

        firebaseConference.processConferences(year, conferenceList);
    }
}