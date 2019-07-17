import entities.Conference;
import utils.http.RequestWrapper;
import utils.json.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        if (args.length > 0) {
            year = Integer.parseInt(args[0]);
        }

        RequestWrapper confRequest = new RequestWrapper();
        JsonParser jsonParser = new JsonParser();

        try {
            String android = confRequest.makeRequest(year, "android");

            ArrayList<Conference> conferenceList = jsonParser.getConferenceList(android, year);

            String credPath = "/Users/alexandrmazur/IdeaProjects/conferences-scrubber/src/main/java/credentials/firebase_credentials.json";
            String dbUrl = "https://disco-abacus-219713.firebaseio.com";

            utils.firebase.Conference firebaseConference = new utils.firebase.Conference(credPath, dbUrl);
            firebaseConference.processConferences(year, conferenceList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}