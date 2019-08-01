package utils;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import entities.Conference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transformer {

    /**
     * Transform list of queried documents from Firebase to conferences objects represented as Map<conference hash, conference object>
     * @param documents objects from Firebase
     * @return collection of conferences.
     */
    public static HashMap<Integer, Conference> transformListQueryDocumentSnapshotToConferenceMap(List<QueryDocumentSnapshot> documents) {
        HashMap<Integer, Conference> map = new HashMap<>(documents.size());

        for (QueryDocumentSnapshot document : documents) {
            Conference conference = Maker.buildConferenceFromDocument(document);
            map.put(conference.hashCode(), conference);
        }

        return map;
    }

    /**
     * Transform conference object to it HashMap representation.
     * Use for insert or update Firebase database
     * @param conf Conference object
     * @return conference object as map representation
     */
    public static Map<String, Object> transformConferenceToMap(entities.Conference conf) {
        Map<String, Object> confMap = new HashMap<>();

        if (conf.getFirebaseId() != null) {
            confMap.put("id", conf.getFirebaseId());
        }

        confMap.put("name", conf.getName());
        confMap.put("url", conf.getUrl());
        confMap.put("city", conf.getCity());
        confMap.put("country", conf.getCountry());
        confMap.put("twitter", conf.getTwitter());
        confMap.put("startDate", conf.getStartDate());
        confMap.put("endDate", conf.getEndDate());
        confMap.put("cfpUrl", conf.getCfpUrl());
        confMap.put("cfpStartDate", conf.getCfpStartDate());
        confMap.put("cfpEndDate", conf.getCfpEndDate());
        confMap.put("year", conf.getYear());
        confMap.put("digest", conf.getConferenceDigest());

        return confMap;
    }
}
