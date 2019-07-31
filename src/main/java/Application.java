import entities.Conference;
import utils.http.ConferenceGrabber;
import utils.json.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Application {

    private int year;
    private boolean isNeedHandleAllYears;
    private Map<String, String> configuration;

    private static String[] urls = {
            "https://raw.githubusercontent.com/tech-conferences/conference-data/master/conferences"
    };

    private static String[] conferenceTypes = {
            "android",
            "clojure",
            "cpp",
            "css",
            "data",
            "devops",
            "dotnet",
            "elixir",
            "elm",
            "general",
            "golang",
            "graphql",
            "groovy",
            "ios",
            "java",
            "javascript",
            "networking",
            "php",
            "python",
            "ruby",
            "rust",
            "scala",
            "security",
            "tech-comm",
            "typescript",
            "ux"
    };

    public Application(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    public void executeAsync() throws ExecutionException, InterruptedException, IOException {
        ConferenceGrabber conferenceGrabber = new ConferenceGrabber();
        JsonParser jsonParser = new JsonParser();
        String credentialsPath = configuration.get("FIREBASE_CREDENTIALS");
        String databaseUrl = configuration.get("DATABASE_URL");
        String collectionName = configuration.get("COLLECTION_NAME");

        for (String url : urls) {
            conferenceGrabber.setUrl(url);
            for (String conferenceType : conferenceTypes) {
                Future<String> future = conferenceGrabber.grabConferencesAsync(year, conferenceType);
                String conferencesJson = future.get();

                if (conferencesJson.equals("")) {
                    continue;
                }

                conferencesJson = conferencesJson.replaceAll("–", "-");
                ArrayList<Conference> conferenceList = jsonParser.makeConferencesList(conferencesJson, year);
                utils.firebase.Conference firebaseConference = new utils.firebase.Conference(credentialsPath, databaseUrl, collectionName);
                firebaseConference.processConferences(year, conferenceType, conferenceList);
            }
        }
    }

    public void execute() throws Exception {
        ConferenceGrabber conferenceGrabber = new ConferenceGrabber();
        JsonParser jsonParser = new JsonParser();
        String credentialsPath = configuration.get("FIREBASE_CREDENTIALS");
        String databaseUrl = configuration.get("DATABASE_URL");
        String collectionName = configuration.get("COLLECTION_NAME");

        for (String url : urls) {
            conferenceGrabber.setUrl(url);
            for (String conferenceType : conferenceTypes) {
                String conferencesJson = conferenceGrabber.grabConferences(year, conferenceType);

                if (conferencesJson.equals("")) {
                    continue;
                }

                conferencesJson = conferencesJson.replaceAll("–", "-");
                ArrayList<Conference> conferenceList = jsonParser.makeConferencesList(conferencesJson, year);
                utils.firebase.Conference firebaseConference = new utils.firebase.Conference(credentialsPath, databaseUrl, collectionName);
                firebaseConference.processConferences(year, conferenceType, conferenceList);
            }
        }
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setIsNeedHandleAllYears(boolean flag) {
        this.isNeedHandleAllYears = flag;
    }
}