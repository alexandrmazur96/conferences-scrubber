package utils.firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
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

    private static boolean dbInitiated = false;

    private String credentialsPath;
    private String dbUrl;

    private CollectionReference conferenceCollection;

    /**
     * @param credentialsPath path to Google Firebase credentials, stored in JSON usually.
     * @param dbUrl           Google Firebase URL.
     * @param collectionName  Name of collection where stored conferences.
     * @throws IOException when credentialsPath not found, see init().
     */
    public Conference(String credentialsPath, String dbUrl, String collectionName) throws IOException {
        this.credentialsPath = credentialsPath;
        this.dbUrl = dbUrl;
        this.init(collectionName);
    }

    /**
     * Insert of update conferences records.
     *
     * @param year           processing year
     * @param conferenceList list of conferences, which should be processed.
     */
    public void processConferences(int year, String conferenceType, ArrayList<entities.Conference> conferenceList) throws ExecutionException, InterruptedException {
        Pair<ArrayList<Map<String, Object>>, ArrayList<Map<String, Object>>> pairForProcess = this.getForProcess(year, conferenceType, conferenceList);

        for (Map<String, Object> confMap : pairForProcess.getFirst()) {
            this.conferenceCollection.add(confMap);
        }

        for (Map<String, Object> confMap : pairForProcess.getSecond()) {
            DocumentReference documentReference = this.conferenceCollection.document((String) confMap.get("id"));
            documentReference.update(confMap);
        }
    }

    /**
     * Make pair of collection, where stored conferences for update & for insert.
     * <p>
     * One method because we can make pair in one pass of cycle and avoid O(2N) computing.
     *
     * @param year            need for querying documents.
     * @param conferencesList conferences collection, created from JSON.
     * @return pair of collection - first for insert new conferences and second for update existing conferences.
     */
    private Pair<ArrayList<Map<String, Object>>, ArrayList<Map<String, Object>>> getForProcess(int year, String conferenceType, List<entities.Conference> conferencesList)
            throws ExecutionException, InterruptedException {

        HashMap<Integer, entities.Conference> dbConferenceMap = Transformer.transformListQueryDocumentSnapshotToConferenceMap(this.queryDocumentsSnapshot(year, conferenceType));
        ArrayList<Map<String, Object>> forInsert = new ArrayList<>();
        ArrayList<Map<String, Object>> forUpdate = new ArrayList<>();

        for (entities.Conference conference : conferencesList) {
            if (dbConferenceMap.containsKey(conference.hashCode())) {
                entities.Conference comparable = dbConferenceMap.get(conference.hashCode());
                if (comparable.equals(conference)) {
                    continue;
                }
                conference.setId(comparable.getFirebaseId());
                forUpdate.add(Transformer.transformConferenceToMap(conference));
            } else {
                forInsert.add(Transformer.transformConferenceToMap(conference));
            }
        }

        return new Pair<>(forInsert, forUpdate);
    }

    /**
     * Go to Firebase and get documents from conference collection.
     *
     * @param year from what we start querying conferences from Firebase.
     * @return collection of conference documents from Firebase.
     */
    private List<QueryDocumentSnapshot> queryDocumentsSnapshot(int year, String conferenceType) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = conferenceCollection.whereEqualTo("year", year).whereArrayContains("conferenceTypes", conferenceType).get();
        QuerySnapshot querySnapshot = query.get();

        return querySnapshot.getDocuments();
    }

    /**
     * Prepare objects for work with Firebase.
     *
     * @throws IOException when FileInputStream can't find credentials JSON.
     */
    private void init(String collectionName) throws IOException {
        try {
            if (!dbInitiated) {
                FileInputStream serviceAccount = new FileInputStream(this.credentialsPath);

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(this.dbUrl)
                        .build();
                FirebaseApp.initializeApp(options);

                dbInitiated = true;
            }

            Firestore db = FirestoreClient.getFirestore();
            this.conferenceCollection = db.collection(collectionName);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
