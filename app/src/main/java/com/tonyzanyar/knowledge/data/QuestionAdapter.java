package com.tonyzanyar.knowledge.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_QUESTION;

/**
 * @author zhangxin
 */

public class QuestionAdapter extends CursorAdapter {


    public QuestionAdapter(Context context, Cursor c) {
        super(context, c, 2);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView t=view.findViewById(android.R.id.text1);
        if (!cursor.isAfterLast()){
            String topic=cursor.getString(cursor.getColumnIndexOrThrow(TWO_COLUMN_QUESTION));
            t.setText(topic);
            cursor.moveToNext();
        }
    }
}
