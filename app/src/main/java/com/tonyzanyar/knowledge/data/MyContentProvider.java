package com.tonyzanyar.knowledge.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import static com.tonyzanyar.knowledge.data.Contract.AUTHORITY;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_CONTENT;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.MAX_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.MIN_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.ONE_PATH;
import static com.tonyzanyar.knowledge.data.Contract.ONE_TABLE;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_QUESTION;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_TOPIC_ID;
import static com.tonyzanyar.knowledge.data.Contract.TWO_CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.TWO_PATH;
import static com.tonyzanyar.knowledge.data.Contract.TWO_TABLE;
/**
 * @author zhangxin
 */
public class MyContentProvider extends ContentProvider {

    private MyDbHelper helper;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int ONE = 100;
    private static final int ONE_ID = 101;
    private static final int TWO = 200;
    private static final int TWO_ID = 201;

    static {
        MATCHER.addURI(AUTHORITY, ONE_PATH, ONE);
        MATCHER.addURI(AUTHORITY, ONE_PATH + "/#", ONE_ID);
        MATCHER.addURI(AUTHORITY, TWO_PATH, TWO);
        MATCHER.addURI(AUTHORITY, TWO_PATH + "/#", TWO_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int match = MATCHER.match(uri);
        int forRe;
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (match) {
            case ONE_ID:
                selection = COLUMN_NUM + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                forRe=db.delete(ONE_TABLE,selection,selectionArgs);
                break;
            case TWO_ID:
                selection = TWO_COLUMN_NUM + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                forRe=db.delete(TWO_TABLE,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("uri is wrong:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return forRe;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        int match = MATCHER.match(uri);
        Uri forRe;
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (match) {
            case ONE:
                if (values.containsKey(COLUMN_NUM)) {
                    int num = values.getAsInteger(COLUMN_NUM);
                    if (num < MIN_LIMIT || num > MAX_LIMIT) {
                        throw new IllegalArgumentException("num is wrong.");
                    }
                }
                long id = db.insert(ONE_TABLE, null, values);
                if (id != -1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                forRe=ContentUris.withAppendedId(CONTENT_URI, id);
                break;
            case TWO:
                if (values.containsKey(TWO_COLUMN_NUM)) {
                    int num = values.getAsInteger(TWO_COLUMN_NUM);
                    if (num < MIN_LIMIT || num > MAX_LIMIT) {
                        throw new IllegalArgumentException("num is wrong.");
                    }
                }
                long id2 = db.insert(TWO_TABLE, null, values);
                if (id2 != -1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                forRe=ContentUris.withAppendedId(TWO_CONTENT_URI, id2);
                break;
            default:
                throw new IllegalArgumentException("uri is wrong:" + uri);
        }
        return forRe;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        helper = new MyDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        int match = MATCHER.match(uri);
        Cursor cursor;
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns1 = {COLUMN_NUM, COLUMN_TOPIC, COLUMN_CONTENT};
        String[] columns2 = {TWO_COLUMN_NUM, TWO_COLUMN_QUESTION, TWO_COLUMN_TOPIC_ID};
        switch (match) {
            case ONE:
                cursor = db.query(ONE_TABLE, columns1, selection, selectionArgs, null, null, sortOrder);
                break;
            case ONE_ID:
                selection = COLUMN_NUM + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ONE_TABLE, columns1, selection, selectionArgs, null, null, sortOrder);
                break;
            case TWO:
                cursor = db.query(TWO_TABLE, columns2, selection, selectionArgs, null, null, sortOrder);
                break;
            case TWO_ID:
                selection = TWO_COLUMN_NUM + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TWO_TABLE, columns2, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("uri is wrong:" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int match = MATCHER.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        int lines;
        switch (match) {
            case ONE_ID:
                if (values.containsKey(COLUMN_NUM)) {
                    int num = values.getAsInteger(COLUMN_NUM);
                    if (num < MIN_LIMIT || num > MAX_LIMIT) {
                        throw new IllegalArgumentException("num is wrong.");
                    }
                }
                selection = COLUMN_NUM + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                lines = db.update(ONE_TABLE, values, selection, selectionArgs);
                break;
            case TWO_ID:
                if (values.containsKey(TWO_COLUMN_TOPIC_ID)) {
                    int num = values.getAsInteger(TWO_COLUMN_TOPIC_ID);
                    if (num < MIN_LIMIT || num > MAX_LIMIT) {
                        throw new IllegalArgumentException("num is wrong.");
                    }
                }
                selection = TWO_COLUMN_NUM + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                lines = db.update(TWO_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("uri is wrong:" + uri);
        }
        if (lines != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return lines;
    }
}
