package com.sleepingbear.pehdictandvoc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DicDb {

    public static void insDicVoc(SQLiteDatabase db, String entryId, String kind) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_VOC " + CommConstants.sqlCR);
        sql.append(" WHERE KIND = '" + kind + "'" + CommConstants.sqlCR);
        sql.append("   AND ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.setLength(0);
        sql.append("INSERT INTO DIC_VOC (KIND, ENTRY_ID, MEMORIZATION,RANDOM_SEQ, INS_DATE) " + CommConstants.sqlCR);
        sql.append("SELECT '" + kind + "', ENTRY_ID, 'N', RANDOM(), '" + DicUtils.getDelimiterDate(DicUtils.getCurrentDate(), ".")  + "' " + CommConstants.sqlCR);
        sql.append("  FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void insDicVocForWord(SQLiteDatabase db, String word, String kind) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_VOC " + CommConstants.sqlCR);
        sql.append(" WHERE KIND = '" + kind + "'" + CommConstants.sqlCR);
        sql.append("   AND ENTRY_ID = (SELECT ENTRY_ID FROM DIC WHERE WORD = '" + word + "')" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.setLength(0);
        sql.append("INSERT INTO DIC_VOC (KIND, ENTRY_ID, MEMORIZATION,RANDOM_SEQ, INS_DATE) " + CommConstants.sqlCR);
        sql.append("SELECT '" + kind + "', ENTRY_ID, 'N', RANDOM(), '" + DicUtils.getDelimiterDate(DicUtils.getCurrentDate(), ".")  + "' " + CommConstants.sqlCR);
        sql.append("  FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = (SELECT ENTRY_ID FROM DIC WHERE WORD = '" + word + "')" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void insDicVoc(SQLiteDatabase db, String entryId, String kind, String insDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_VOC " + CommConstants.sqlCR);
        sql.append(" WHERE KIND = '" + kind + "'" + CommConstants.sqlCR);
        sql.append("   AND ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.setLength(0);
        sql.append("INSERT INTO DIC_VOC (KIND, ENTRY_ID, MEMORIZATION,RANDOM_SEQ, INS_DATE) " + CommConstants.sqlCR);
        sql.append("SELECT '" + kind + "', ENTRY_ID, 'N', RANDOM(), '" + insDate + "' " + CommConstants.sqlCR);
        sql.append("  FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void delDicVoc(SQLiteDatabase db, String entryId, String kind) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_VOC " + CommConstants.sqlCR);
        sql.append(" WHERE KIND = '" + kind + "'" + CommConstants.sqlCR);
        sql.append("   AND ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void delDicVocAll(SQLiteDatabase db, String entryId) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_VOC " + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void insToday(SQLiteDatabase db, String entryId, String today) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO DIC_TODAY(TODAY, ENTRY_ID) " + CommConstants.sqlCR);
        sql.append("SELECT '" + today + "', ENTRY_ID " + CommConstants.sqlCR);
        sql.append("  FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void updMemory(SQLiteDatabase db, String entryId, String memoryYn) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE DIC_VOC " + CommConstants.sqlCR);
        sql.append("   SET MEMORIZATION = '" + memoryYn + "'" + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "' " + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    /**
     * 오늘이 단어 초기화
     * @param db
     */
    public static void initToday(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_TODAY" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    /**
     * 단어장 초기화
     * @param db
     */
    public static void initVocabulary(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_VOC" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.delete(0, sql.length());
        sql.append("DELETE FROM DIC_CODE" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = 'MY'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.delete(0, sql.length());
        sql.append("INSERT INTO DIC_CODE(CODE_GROUP, CODE, CODE_NAME)" + CommConstants.sqlCR);
        sql.append("VALUES('MY', 'MY0000', 'MY 단어장')" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    /*
    public static boolean isExistWord(SQLiteDatabase db, String word) {
        boolean rtn = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) CNT FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE WORD = '" + word + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());

        Cursor cursor = db.rawQuery(sql.toString(), null);
        if ( cursor.moveToNext() ) {
            if ( cursor.getInt(cursor.getColumnIndexOrThrow("CNT")) > 0 ) {
                rtn = true;
            }
        }
        cursor.close();

        return rtn;
    }
    */

    public static String getEntryIdForWord(SQLiteDatabase db, String word) {
        String rtn = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ENTRY_ID  " + CommConstants.sqlCR);
        sql.append("  FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE WORD = '" + word + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());

        Cursor cursor = db.rawQuery(sql.toString(), null);
        if ( cursor.moveToNext() ) {
            rtn = cursor.getString(cursor.getColumnIndexOrThrow("ENTRY_ID"));
        }
        cursor.close();

        return rtn;
    }

    public static boolean isExistMySample(SQLiteDatabase db, String sentence) {
        boolean rtn = false;

        if ( sentence != null ) {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(*) CNT  " + CommConstants.sqlCR);
            sql.append("  FROM DIC_MY_SAMPLE " + CommConstants.sqlCR);
            sql.append(" WHERE SENTENCE1 = '" + sentence.replaceAll("'", "''") + "'" + CommConstants.sqlCR);
            DicUtils.dicSqlLog(sql.toString());

            Cursor cursor = db.rawQuery(sql.toString(), null);
            if (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("CNT")) > 0) {
                    rtn = true;
                }
            }
            cursor.close();
        }

        return rtn;
    }

    public static void initSample(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_MY_SAMPLE" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void delDicMySample(SQLiteDatabase db, String sentence) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_MY_SAMPLE " + CommConstants.sqlCR);
        sql.append(" WHERE SENTENCE1 = '" + sentence.replaceAll("'" ,"''") + "'" + CommConstants.sqlCR);
        DicUtils.dicLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void insDicMySample(SQLiteDatabase db, String sentence1, String sentence2, String today) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO DIC_MY_SAMPLE (TODAY, SENTENCE1, SENTENCE2)" + CommConstants.sqlCR);
        sql.append("VALUES( '" + today + "', '" + sentence1.replaceAll("'" ,"''") + "', '" + sentence2.replaceAll("'" ,"''") + "')" +  CommConstants.sqlCR);
        DicUtils.dicLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void delDicCategoryWord(SQLiteDatabase db, String code) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_CATEGORY_WORD " + CommConstants.sqlCR);
        sql.append(" WHERE CODE = '" + code + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void insDicCategoryWord(SQLiteDatabase db, String code, ArrayList wordAl) {
        StringBuffer sql = new StringBuffer();

        String words = "";
        for ( int i = 0; i < wordAl.size(); i++ ) {
            if ( i == 0 ) {
                words += ((String)((HashMap)wordAl.get(i)).get("WORD")).replaceAll("'","");
            } else {
                words += "," + ((String)((HashMap)wordAl.get(i)).get("WORD")).replaceAll("'","");
            }
        }

        sql.delete(0, sql.length());
        sql.append("INSERT INTO DIC_CATEGORY_WORD (CODE, ENTRY_ID) " + CommConstants.sqlCR);
        sql.append("SELECT '" + code + "' CODE, ENTRY_ID" + CommConstants.sqlCR);
        sql.append("FROM   DIC " + CommConstants.sqlCR);
        sql.append("WHERE  KIND = 'F'" + CommConstants.sqlCR);
        sql.append("AND    WORD IN ('" + words.replaceAll(",","','") + "')" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void updDicCategoryInfo(SQLiteDatabase db, String code) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE DIC_CODE " + CommConstants.sqlCR);
        sql.append("SET    W_CNT = (SELECT COUNT(*) FROM DIC_CATEGORY_WORD WHERE CODE = '" + code + "')" + CommConstants.sqlCR);
        sql.append("WHERE  CODE = '" + code + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void updDicCategoryInfo(SQLiteDatabase db, String code, String codeName, String updDate, String bookmarkCnt) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE DIC_CODE " + CommConstants.sqlCR);
        sql.append("SET    CODE_NAME = '" + codeName + "'" + CommConstants.sqlCR);
        sql.append("       ,UPD_DATE = '" + updDate + "'" + CommConstants.sqlCR);
        sql.append("       ,BOOKMARK_CNT = '" + bookmarkCnt + "'" + CommConstants.sqlCR);
        sql.append("WHERE  CODE = '" + code + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void insDicCategoryInfo(SQLiteDatabase db, String codeGroup, String code, String codeName, String updDate, String wCnt, String bookmarkCnt) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO DIC_CODE(CODE_GROUP, CODE, CODE_NAME, UPD_DATE, W_CNT, S_CNT, BOOKMARK_CNT) " + CommConstants.sqlCR);
        sql.append("VALUES ('" + codeGroup + "','" + code + "','" + codeName + "','" + updDate + "'," + wCnt + ",0," + bookmarkCnt + ")" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void patch1(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE DIC_CODE" + CommConstants.sqlCR);
        sql.append("SET    CODE = 'MY0000' " + CommConstants.sqlCR);
        sql.append("WHERE  CODE = 'MY000' " + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.delete(0, sql.length());
        sql.append("UPDATE DIC_VOC" + CommConstants.sqlCR);
        sql.append("SET    KIND = 'MY0000' " + CommConstants.sqlCR);
        sql.append("WHERE  KIND = 'MY000' " + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }
}
