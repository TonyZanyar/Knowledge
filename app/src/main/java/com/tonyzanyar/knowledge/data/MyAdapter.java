package com.tonyzanyar.knowledge.data;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;

/**
 * @author zhangxin
 */

public class MyAdapter extends CursorAdapter {


    public MyAdapter(Context context, Cursor c) {
        super(context, c, 1);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView t=view.findViewById(android.R.id.text1);
        if (!cursor.isAfterLast()){
            long id=cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NUM));
            String topic=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
            t.setText(id+". "+topic);
            cursor.moveToNext();
        }

    }
}
