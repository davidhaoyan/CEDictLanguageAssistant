package com.david.cedict.db;

import com.david.cedict.struct.Entry;
import com.david.cedict.util.MapVisitor;
import com.david.cedict.struct.PostingListMap;
import com.david.cedict.util.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CEDictDB {
    private Map<String, PostingListMap> invertedIndex = new HashMap<>(); // word => (id => freq)
    private static int i = 1;
    private Statement dbStatement;

    public CEDictDB(Statement statement) {
        this.dbStatement = statement;
        try {
            statement.execute(wipeDB());
            statement.execute(initEntries());
            statement.execute(initInvertedIndex());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String wipeDB() {
        return "DROP TABLE IF EXISTS entries, invertedIndex";
    }

    public String initEntries() {
        return "CREATE TABLE IF NOT EXISTS entries (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "Traditional VARCHAR(50)," +
                "Simplified VARCHAR(50)," +
                "Pinyin VARCHAR(255)," +
                "English VARCHAR(1000))";
    }

    public String initInvertedIndex() {
        return "CREATE TABLE IF NOT EXISTS invertedIndex (" +
                "word VARCHAR(50) PRIMARY KEY," +
                "postingList VARCHAR(75000))"; // #id:freq#id:freq...
    }

    public void parse() {
        try {
            File myObj = new File("src/main/resources/cedict_ts.u8");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Entry entry = parseLine(data);
                runDB(entry);
            }
            myReader.close();
            writePostingListToDB();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static Entry parseLine(String line) {
        String[] split = line.split("/");
        String[] chinese = split[0].split("\\[");
        String[] hanyu = chinese[0].split(" ");

        String english = "";
        for (int i = 1; i < split.length; i++) {
            english += split[i] + "/";
        }

        String trad = hanyu[0];
        String simp = hanyu[1];
        String pinyin = chinese[1].substring(0, chinese[1].length()-2);
        english = english.substring(0, english.length()-1);
        english = Utility.escapeSingleQuotes(english);
        Entry entry = new Entry(trad, simp, pinyin, english, i++);
        return entry;
    }



    private void writeEntriesToDB(Entry entry) {
        try {
            dbStatement.execute("INSERT INTO entries (Traditional, Simplified, Pinyin, English) VALUES ('" +
                    entry.getTrad() + "','" + entry.getSimp() + "','" + entry.getPinyin() + "','" + entry.getEnglish() +
                    "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private Map<String, Integer> getWordFreq(Entry entry) {
        List<String> words = Utility.splitStringRegex(entry.getEnglish());
        List<String> processedWords = Utility.processWords(words);

        Map<String, Integer> wordFreq = new HashMap<>();
        for (String word: processedWords) {
            wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
        }
        return wordFreq;
    }

    private void writePostingListToDB() {
        for (String word : invertedIndex.keySet()) {
            try {
                dbStatement.execute("INSERT INTO invertedIndex (word, postingList) VALUES ('" + word + "','" +
                        invertedIndex.get(word).serialize(new MapVisitor()).getData() + "')");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeInvertedIndex(Entry entry) {
        Map<String, Integer> wordFreq = getWordFreq(entry);

        for (String word : wordFreq.keySet()) {
            if (!invertedIndex.containsKey(word)) {
                invertedIndex.put(word, new PostingListMap(Map.of(entry.getId(), wordFreq.get(word))));
            } else {
                int id = entry.getId();
                PostingListMap existing = invertedIndex.get(word);
                existing.put(id, wordFreq.get(word));
                invertedIndex.put(word, existing);
            }
        }
    }

    public void runDB(Entry entry) {
        System.out.println("Inserting" + i);
        writeEntriesToDB(entry);
        writeInvertedIndex(entry);
    }
}
