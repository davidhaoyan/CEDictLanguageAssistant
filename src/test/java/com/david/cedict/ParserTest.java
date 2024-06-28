package com.david.cedict;

import com.david.cedict.db.CEDictDB;
import com.david.cedict.struct.Entry;
import junit.framework.TestCase;

public class ParserTest extends TestCase {
    public void writeParserTest(String line, String trad, String simp, String pinyin, String english) {
        Entry e = CEDictDB.parseLine(line);
        if (!e.getTrad().equals(trad)) { fail("Traditional field mismatch. Expected: " + trad + ", Actual: " + e.getTrad()); }
        if (!e.getSimp().equals(simp)) { fail("Simplified field mismatch. Expected: " + simp + ", Actual: " + e.getSimp()); }
        if (!e.getPinyin().equals(pinyin)) { fail("Pinyin field mismatch. Expected: " + pinyin + ", Actual: " + e.getPinyin()); }
        if (!e.getEnglish().equals(english)) { fail("English field mismatch. Expected: " + english + ", Actual: " + e.getEnglish()); }
    }
    public void testParser() {
        writeParserTest("龜縮 龟缩 [gui1 suo1] /to withdraw/to hole up/",
                "龜縮",
                "龟缩",
                "gui1 suo1",
                "to withdraw/to hole up");

        writeParserTest("龜船 龟船 [gui1 chuan2] /\"turtle ship\", armored warship used by Koreans in fighting the Japanese during the Imjin war of 1592-1598 壬辰倭亂|壬辰倭乱[ren2 chen2 wo1 luan4]/",
                "龜船",
                "龟船",
                "gui1 chuan2",
                "\"turtle ship\", armored warship used by Koreans in fighting the Japanese during the Imjin war of 1592-1598 壬辰倭亂|壬辰倭乱[ren2 chen2 wo1 luan4]");
    }
}
