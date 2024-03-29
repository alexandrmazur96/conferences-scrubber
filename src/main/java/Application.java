import entities.Conference;
import utils.http.ConferenceGrabber;
import utils.json.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class Application {

    private int handleYear;
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

    Application(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    void executeAsync() throws ExecutionException, InterruptedException, IOException {
        ConferenceGrabber conferenceGrabber = new ConferenceGrabber();
        JsonParser jsonParser = new JsonParser();
        String credentialsPath = configuration.get("FIREBASE_CREDENTIALS");
        String databaseUrl = configuration.get("DATABASE_URL");
        String collectionName = configuration.get("COLLECTION_NAME");
        int[] years = this.getYears();

        for (int year : years) {
            for (String url : urls) {
                conferenceGrabber.setUrl(url);
                for (String conferenceType : conferenceTypes) {
                    Future<String> future = conferenceGrabber.grabConferencesAsync(year, conferenceType);
                    String conferencesJson = future.get();

                    if (conferencesJson.equals("")) {
                        continue;
                    }

                    ArrayList<Conference> conferenceList = jsonParser.makeConferencesList(conferencesJson, year, conferenceType);
                    utils.firebase.Conference firebaseConference = new utils.firebase.Conference(credentialsPath, databaseUrl, collectionName);
                    firebaseConference.processConferences(year, conferenceList);
                }
            }
        }
    }

    void setHandleYear(int handleYear) {
        this.handleYear = handleYear;
    }

    private int[] getYears() {
        ArrayList<Integer> years = new ArrayList<>();
        if (isNeedHandleAllYears) {
            for (int i = 2014; i <= Calendar.getInstance().get(Calendar.YEAR) + 2; i++) {
                years.add(i);
            }

            return years.stream().filter(Objects::nonNull).mapToInt(t -> t).toArray();
        } else {
            return new int[]{handleYear};
        }
    }

    void setIsNeedHandleAllYears() {
        this.isNeedHandleAllYears = true;
    }
}