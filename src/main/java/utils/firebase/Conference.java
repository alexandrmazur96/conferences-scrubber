package utils.firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import utils.Maker;
import utils.Pair;
import utils.Transformer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Conference {

    private String credentialsPath;
    private String dbUrl;

    private CollectionReference conferenceCollection;

    public Conference(String credentialsPath, String dbUrl) {
        this.credentialsPath = credentialsPath;
        this.dbUrl = dbUrl;
        this.init();
    }

    public void processConferences(int year, ArrayList<entities.Conference> conferenceList) throws ExecutionException, InterruptedException {
        Pair<ArrayList<Map<String, Object>>, ArrayList<Map<String, Object>>> pairForProcess = this.getForProcess(year, conferenceList);

        System.out.println("Start insert into firebase collection...");

        for (Map<String, Object> confMap : pairForProcess.getFirst()) {
            System.out.println("Insert: " + confMap.toString());
            this.conferenceCollection.add(confMap);
        }

        System.out.println("End insert into firebase collection");

        System.out.println("Start update firebase collection...");

        for (Map<String, Object> confMap : pairForProcess.getSecond()) {
            System.out.println("Update: " + confMap.toString());
            DocumentReference documentReference = this.conferenceCollection.document((String) confMap.get("id"));
            documentReference.update(confMap);
        }

        System.out.println("End update firebase collection...");
    }

    private Pair<ArrayList<Map<String, Object>>, ArrayList<Map<String, Object>>> getForProcess(int year, List<entities.Conference> conferencesList)
            throws ExecutionException, InterruptedException {

        HashMap<Integer, entities.Conference> dbConferenceMap = Transformer.transformListQueryDocumentSnapshotToConferenceMap(this.queryDocumentsSnapshot(year));
        ArrayList<Map<String, Object>> forInsert = new ArrayList<>();
        ArrayList<Map<String, Object>> forUpdate = new ArrayList<>();

        for (entities.Conference conference : conferencesList) {
            if (dbConferenceMap.containsKey(conference.hashCode())) {
                entities.Conference comparable = dbConferenceMap.get(conference.hashCode());
                if (!comparable.equals(conference)) {
                    conference.setId(comparable.getFirebaseId());
                    forUpdate.add(Maker.buildConferenceMapObject(conference));
                }
            } else {
                forInsert.add(Maker.buildConferenceMapObject(conference));
            }
        }

        return new Pair<>(forInsert, forUpdate);
    }

    private List<QueryDocumentSnapshot> queryDocumentsSnapshot(int year) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = conferenceCollection.whereEqualTo("year", year).get();
        QuerySnapshot querySnapshot = query.get();

        return querySnapshot.getDocuments();
    }

    private void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream(this.credentialsPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(this.dbUrl)
                    .build();
            FirebaseApp.initializeApp(options);

            Firestore db = FirestoreClient.getFirestore();
            this.conferenceCollection = db.collection("conference");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
