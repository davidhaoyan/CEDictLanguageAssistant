package com.david.cedict;

import com.david.cedict.struct.*;
import com.david.cedict.util.MapVisitor;
import com.david.cedict.util.Utility;

import java.sql.*;
import java.util.*;

public class Retriever {

    public class ScoreEntry {
        private float score;
        private Entry entry;
        public ScoreEntry(float score, Entry entry) {
            this.score = score;
            this.entry = entry;
        }

        public float getScore() {
            return score;
        }

        public Entry getEntry() {
            return entry;
        }
    }


    public class ScoreEntryComparator implements Comparator<ScoreEntry> {
        @Override
        public int compare(ScoreEntry e1, ScoreEntry e2) {
            if (e1.getScore() < e2.getScore()) {
                return -1;
            } else if (e1.getScore() > e2.getScore()) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    private Statement dbStatement = null;
    private Statement vsmStatement = null;
    private Map<String, PostingListMap> invertedIndex = new HashMap<>();
    private Map<Integer, Integer> idToMaxTf = new HashMap<>();

    public Retriever() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:./data/cedictdb", "sa", "");
            dbStatement = connection.createStatement();
            connection = DriverManager.getConnection("jdbc:h2:./data/vsm", "sa", "");
            vsmStatement = connection.createStatement();
            Utility.deserializeInvertedIndex(dbStatement, invertedIndex, idToMaxTf);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> createQueryVector(String query) {
        query = Utility.escapeSingleQuotes(query);
        List<String> words = new ArrayList<>();
        for (String word : query.toLowerCase().split("[\\p{Punct}\\s]+")) {
            words.add(word);
        }
        List<String> processedWords = Utility.processWords(words);
        System.out.println(processedWords);
        return processedWords;
    }

    public float calculateCosineSimilarity(List<String> queryVector, DocumentWeightMap docVector, int id) {
        float score = 0;
        for (String word: queryVector) {
            if (docVector.get(word) != null) {
                score += docVector.get(word);
            }
        }
        score = score / queryVector.size() / docVector.size();
        return score;
    }

    private Set<Integer> retrieveRelevantIds(List<String> queryVector) {
        Set<Integer> relevantIds = new HashSet<>();
        for (String word: queryVector) {
            if (invertedIndex.containsKey(word)) {
                PostingListMap postingList = invertedIndex.get(word);
                for (Integer docId: postingList.keySet()) {
                    relevantIds.add(docId);
                }
            }
        }
        return relevantIds;
    }

    public PriorityQueue<ScoreEntry> outputRanking(String query) {
        PriorityQueue<ScoreEntry> entries = new PriorityQueue<>(new ScoreEntryComparator());
        try {
            List<String> queryVector = createQueryVector(query);
            Set<Integer> relevantIds = retrieveRelevantIds(queryVector);

            for (Integer id: relevantIds) {
                ResultSet vectorSet = vsmStatement.executeQuery("SELECT * FROM vsm WHERE id = " + id);
                vectorSet.next();
                DocumentWeightSerial docSerial = new DocumentWeightSerial(vectorSet.getString("weights"));
                DBMap docVector = docSerial.deserialize(new MapVisitor());

                ResultSet entrySet = dbStatement.executeQuery("SELECT * FROM entries WHERE id = " + id);
                entrySet.next();
                Entry entry = new Entry(entrySet.getString("Traditional"), entrySet.getString("Simplified"),
                        entrySet.getString("Pinyin"), entrySet.getString("English"), id);
                entries.add(new ScoreEntry(-calculateCosineSimilarity(queryVector, (DocumentWeightMap) docVector, id), entry));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
}
