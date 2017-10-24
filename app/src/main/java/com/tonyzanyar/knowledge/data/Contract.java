package com.tonyzanyar.knowledge.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Random;

/**
 * @author zhangxin
 */

public class Contract implements BaseColumns {
    public static final String MIN_NUM="min_num";
    public static final String MAX_NUM="max_num";
    public static final String KEY_WORD="key_word";
    public static final int MAX_LIMIT=999;
    public static final int MIN_LIMIT=1;

    private  Contract(){
    }

    public static final String AUTHORITY="com.tonyzanyar.knowledge";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);

    public static final String ONE_PATH="topics";
    public static final String ONE_TABLE=ONE_PATH;
    public static final String COLUMN_NUM=_ID;
    public static final String COLUMN_TOPIC="topic";
    public static final String COLUMN_CONTENT="content";
    public static final Uri CONTENT_URI= Uri.withAppendedPath(BASE_CONTENT_URI,ONE_PATH);

    public static final String TWO_PATH="questions";
    public static final String TWO_TABLE=TWO_PATH;
    public static final String TWO_COLUMN_NUM=_ID;
    public static final String TWO_COLUMN_QUESTION="question";
    public static final String TWO_COLUMN_TOPIC_ID="topic_id";
    public static final Uri TWO_CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,TWO_PATH);

    public static int[] randomLines(int max){
        if (max<1){
            return null;
        }
        int[]a=new int[max];
        for (int i = 0; i < max; i++) {
            a[i]=i;
        }
        Random random= new Random();
        for (int i = 0; i < max; i++) {
            int num1=a[i];
            int position=i+random.nextInt(max-i);
            a[i]=a[position];
            a[position]=num1;
        }
        return a;
    }

}
