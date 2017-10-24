package com.tonyzanyar.knowledge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_CONTENT;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.ONE_TABLE;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_QUESTION;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_TOPIC_ID;
import static com.tonyzanyar.knowledge.data.Contract.TWO_TABLE;

/**
 * @author zhangxin
 */

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="knowledge.db";
    private static final int DB_VERSION=1;
    private static final String UN_ADD="未添加";

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String c="CREATE TABLE IF NOT EXISTS "+ONE_TABLE+"(" +
                COLUMN_NUM+" INTEGER PRIMARY KEY,"+
                COLUMN_TOPIC+" TEXT NOT NULL DEFAULT '" +UN_ADD +"',"+
                COLUMN_CONTENT+" TEXT NOT NULL DEFAULT '" +UN_ADD +"') ;"
                ;
        sqLiteDatabase.execSQL(c);

        String d="CREATE TABLE IF NOT EXISTS "+TWO_TABLE+"(" +
                TWO_COLUMN_NUM+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TWO_COLUMN_QUESTION+" TEXT NOT NULL DEFAULT '" +UN_ADD +"',"+
                TWO_COLUMN_TOPIC_ID+" INTEGER NOT NULL);"
                ;
        sqLiteDatabase.execSQL(d);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
