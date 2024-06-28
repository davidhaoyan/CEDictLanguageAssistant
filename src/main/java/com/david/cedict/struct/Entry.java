package com.david.cedict.struct;

public class Entry {
    private int id;
    private String trad;
    private String simp;
    private String pinyin;
    private String english;

    public int getId() { return id; }
    public String getTrad() { return trad; }
    public String getSimp() { return simp; }
    public String getPinyin() { return pinyin; }
    public String getEnglish() { return english; }

    public Entry(String trad, String simp, String pinyin, String english, int id) {
        this.trad = trad;
        this.simp = simp;
        this.pinyin = pinyin;
        this.english = english;
        this.id = id;
    }

    @Override
    public String toString() {
        return "trad:" + trad + "\nsimp:" + simp + "\npinyin:" + pinyin + "\nenglish:" + english;
    }
}
