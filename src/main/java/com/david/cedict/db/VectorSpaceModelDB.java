package com.david.cedict.db;

import com.david.cedict.struct.DocumentWeightMap;
import com.david.cedict.struct.DocumentWeightSerial;
import com.david.cedict.util.MapVisitor;
import com.david.cedict.struct.PostingListMap;
import com.david.cedict.util.Utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class VectorSpaceModelDB {
    private Statement vsmStatement;
    private Statement dbStatement;
    private int i = 1;
    private Map<String, PostingListMap> invertedIndex = new HashMap<>();
    private Map<Integer,Integer> idToMaxTf = new HashMap<>();

    public VectorSpaceModelDB(Statement vsmStatement, Statement dbStatement) {
        this.vsmStatement = vsmStatement;
        this.dbStatement = dbStatement;
        Utility.deserializeInvertedIndex(dbStatement, invertedIndex, idToMaxTf);
        try {
            vsmStatement.execute(wipeVSM());
            vsmStatement.execute(initVSM());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String wipeVSM() {
        return "DROP TABLE IF EXISTS vsm";
    }

    private String initVSM() {
        return "CREATE TABLE IF NOT EXISTS vsm (" +
                "id INT PRIMARY KEY," +
                "weights VARCHAR(10000))";
    }

    private String writeVSM(int id, String weights) {
        return "INSERT INTO vsm (id, weights)" +
                "VALUES (" + id + ",'" + weights + "')";
    }

    private DocumentWeightMap createVector(int docId, String docEnglish) {
        DocumentWeightMap weights = new DocumentWeightMap();
        List<String> words = Utility.splitStringRegex(docEnglish);
        Set<String> processedWords = new HashSet<>(Utility.processWords(words));
        for (String word : processedWords) {
            float weight = calculateWeight(word, docId);
            if (weight > 0) {
                weights.put(word, weight);
            }
        }
        return weights;
    }

    private int calculateTf(String word, int docId) {
        PostingListMap idToFreq = invertedIndex.get(word);
        if (idToFreq.get(docId) == null ) {
            return 0;
        } else {
            return invertedIndex.get(word).get(docId);
        }
    }

    private float calculateIdf(String word) {
        return (float)1/invertedIndex.get(word).size();
    }

    private float calculateWeight(String word, int docId) {
        float tf = (float) calculateTf(word, docId);
        float idf = calculateIdf(word);
        float maxTf;
        if (idToMaxTf.get(docId) == null) {
            maxTf = 1;
        } else {
            maxTf = idToMaxTf.get(docId);
        }
        return tf * idf / maxTf;
    }



    // InvertedIndex : Map(word -> Map(docId -> freq))
    // Tf(word, docId) = freq of word in docId (invertedIndex[word][docId])
    // Df(word) = how many docs does word appear in (invertedIndex[word].size)
    // Idf = 1 / Df
    // MaxTf(docId) = max Tf for all words in docId
    public void runVSM() throws SQLException {
        try {
            ResultSet resultSet = dbStatement.executeQuery("SELECT * FROM entries");
            while (resultSet.next()) {
                int docId = resultSet.getInt("id");
                System.out.println("Creating vector for #" + docId);
                DocumentWeightSerial vector = createVector(docId, resultSet.getString("English")).serialize(new MapVisitor());
                vsmStatement.execute(writeVSM(docId, vector.getData()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}