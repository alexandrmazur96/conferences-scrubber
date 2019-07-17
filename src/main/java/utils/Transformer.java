package utils;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import entities.Conference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Transformer {

    public static HashMap<Integer, Conference> transformListConferenceToMap(ArrayList<Conference> conferenceList) {
        HashMap<Integer, Conference> map = new HashMap<>(conferenceList.size());

        for (Conference conference : conferenceList) {
            map.put(conference.hashCode(), conference);
        }

        return map;
    }

    public static HashMap<Integer, Conference> transformListQueryDocumentSnapshotToConferenceMap(List<QueryDocumentSnapshot> documents) {
        HashMap<Integer, Conference> map = new HashMap<>(documents.size());

        for (QueryDocumentSnapshot document : documents) {
            Conference conference = Maker.buildConferenceFromDocument(document);
            map.put(conference.hashCode(), conference);
        }

        return map;
    }
}
