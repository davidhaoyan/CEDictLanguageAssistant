package com.david.cedict.util;

import com.david.cedict.struct.PostingListMap;
import com.david.cedict.struct.PostingListSerial;
import org.tartarus.snowball.ext.PorterStemmer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utility {
    private static void updateMaxTf(PostingListMap idToFreq, Map<Integer,Integer> idToMaxTf) {
        for (int docId : idToFreq.keySet()) {
            if (idToMaxTf.get(docId) == null) {
                idToMaxTf.put(docId, idToFreq.get(docId));
            } else {
                idToMaxTf.put(docId, Math.max(idToMaxTf.get(docId), idToFreq.get(docId)));
            }
        }
    }

    public static void deserializeInvertedIndex(Statement dbStatement, Map<String, PostingListMap> invertedIndex, Map<Integer,Integer> idToMaxTf) {
        try {
            ResultSet resultSet = dbStatement.executeQuery("SELECT * FROM InvertedIndex");
            while (resultSet.next()) {
                String word = resultSet.getString("word");
                PostingListSerial postingList = new PostingListSerial(resultSet.getString("postingList"));
                PostingListMap idToFreq = postingList.deserialize(new MapVisitor());
                invertedIndex.put(word, idToFreq);
                updateMaxTf(idToFreq, idToMaxTf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> processWords(List<String> words) {
        Stopword ss = new Stopword("src/main/resources/stopwords.txt");
        PorterStemmer ps = new PorterStemmer();
        List<String> processedWords = new ArrayList<>();
        for (String word : words) {
            if (!ss.isStopWord(word) && !word.equals("")) {
            ps.setCurrent(word);
            ps.stem();
            processedWords.add(ps.getCurrent().toLowerCase());
            }
        }
        return processedWords;
    }

    public static List<String> splitStringRegex(String string) {
        List<String> words = new ArrayList<>();
        for (String word : string.split("[\\p{Punct}\\s]+")) {
            words.add(word);
        }
        return words;
    }

    // Need to escape single quote ' in SQL query
    public static String escapeSingleQuotes(String input) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\'') {
                builder.append("''");
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
