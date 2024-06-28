package com.david.cedict.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinyinDecoder {
    private static final Map<Integer, String> PinyinToneMark = new HashMap<>();
    static {
        PinyinToneMark.put(0, "aoeiuv\u00fc");
        PinyinToneMark.put(1, "\u0101\u014d\u0113\u012b\u016b\u01d6\u01d6");
        PinyinToneMark.put(2, "\u00e1\u00f3\u00e9\u00ed\u00fa\u01d8\u01d8");
        PinyinToneMark.put(3, "\u01ce\u01d2\u011b\u01d0\u01d4\u01da\u01da");
        PinyinToneMark.put(4, "\u00e0\u00f2\u00e8\u00ec\u00f9\u01dc\u01dc");
    }

    public static String decodePinyin(String s) {
        StringBuilder r = new StringBuilder();
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z' || c == ' ' || c >= 'A' && c <= 'Z') {
                t.append(c);
            } else if (c == ':') {
                assert t.charAt(t.length() - 1) == 'u';
                t.setCharAt(t.length() - 1, '\u00fc');
            } else {
                if (c >= '0' && c <= '5') {
                    int tone = (int) c - '1';
                    if (tone != 0) {
                        Pattern pattern = Pattern.compile("[aoeiuv\u00fc]+");
                        Matcher matcher = pattern.matcher(t.toString());
                        if (!matcher.find()) {
                            t.append(c);
                        } else if (matcher.group().length() == 1) {
                            int index = PinyinToneMark.get(0).indexOf(matcher.group());
                            t.replace(matcher.start(), matcher.end(), PinyinToneMark.get(tone).substring(index, index + 1));
                        } else {
                            if (t.toString().contains("a")) {
                                t.replace(t.indexOf("a"), t.indexOf("a") + 1, PinyinToneMark.get(tone).substring(0, 1));
                            } else if (t.toString().contains("o")) {
                                t.replace(t.indexOf("o"), t.indexOf("o") + 1, PinyinToneMark.get(tone).substring(1, 2));
                            } else if (t.toString().contains("e")) {
                                t.replace(t.indexOf("e"), t.indexOf("e") + 1, PinyinToneMark.get(tone).substring(2, 3));
                            } else if (t.toString().endsWith("ui")) {
                                t.replace(t.length() - 1, t.length(), PinyinToneMark.get(tone).substring(3, 4));
                            } else if (t.toString().endsWith("iu")) {
                                t.replace(t.length() - 1, t.length(), PinyinToneMark.get(tone).substring(4, 5));
                            } else {
                                t.append("!");
                            }
                        }
                    }
                }
                r.append(t);
                t.setLength(0);
            }
        }
        r.append(t);
        return r.toString();
    }
}