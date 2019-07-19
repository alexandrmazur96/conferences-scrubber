import entities.Conference;
import org.jetbrains.annotations.NotNull;
import utils.EnvConfig;
import utils.http.RequestWrapper;
import utils.json.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class Main {

    public static void main(@NotNull String[] args) throws Exception {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        if (args.length > 0) {
            year = Integer.parseInt(args[0]);
        }

        InputStream stream = Main.class.getResourceAsStream("/.env");
        EnvConfig envConfigUtil = new EnvConfig(stream);
        Map<String, String> config = envConfigUtil.getConfig();
        RequestWrapper confRequest = new RequestWrapper();
        JsonParser jsonParser = new JsonParser();

        String android = confRequest.makeRequest(year, "android");
        ArrayList<Conference> conferenceList = jsonParser.getConferenceList(android, year);

        String credentialsPath = config.get("FIREBASE_CREDENTIALS");
        String databaseUrl = config.get("DATABASE_URL");
        String collectionName = "conference";

        utils.firebase.Conference firebaseConference = new utils.firebase.Conference(credentialsPath, databaseUrl, collectionName);

        firebaseConference.processConferences(year, conferenceList);
    }
}