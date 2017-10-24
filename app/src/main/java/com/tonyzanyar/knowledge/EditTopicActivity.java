package com.tonyzanyar.knowledge;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_CONTENT;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.MAX_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.MIN_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_TOPIC_ID;

/**
 * @author zhangxin
 */
public class EditTopicActivity extends AppCompatActivity {
    private static final String TAG = "EditTopicActivity1";

    private boolean isEdit;
    private EditText num;
    private EditText topic;
    private EditText content;
    private FloatingActionButton fab3;
    private Button editQuestion;

    long idGot;
    long idFromView;
    String topicFromView;
    String contentFromView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent=getIntent();
        num=(EditText)findViewById(R.id.edit_num);
        topic=(EditText)findViewById(R.id.edit_topic);
        content=(EditText)findViewById(R.id.edit_content);
        editQuestion=(Button)findViewById(R.id.edit_questions_of_topic);
        fab3=(FloatingActionButton)findViewById(R.id.floatingActionButton3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();

            }
        });
        Uri uri=intent.getData();
        isEdit=uri!=null;
        if (!isEdit){
            editQuestion.setVisibility(View.GONE);
            num.setFocusable(false);
            num.setFocusableInTouchMode(false);
        }else {
            initViews(uri);
        }

    }

    private void saveData() {
        if (!isEdit){
            String id=num.getText().toString();
            int intId=Integer.parseInt(id);
            if (intId<MIN_LIMIT||intId>MAX_LIMIT){
                Toast.makeText(this, "请输入1-999之间的序号", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri=Uri.withAppendedPath(CONTENT_URI,String.valueOf(intId));
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            if (cursor.getCount()==0){
                insertData();
            }
        }
        updateData();
        finish();
    }

    private void insertData() {
        ContentValues value=initValue();
        getContentResolver().insert(CONTENT_URI,value);
    }

    private void updateData(){
        ContentValues value=initValue();
        getContentResolver().update(Uri.withAppendedPath(CONTENT_URI,String.valueOf(idFromView)),value,null,null);
    }

    private ContentValues initValue(){
        ContentValues value=new ContentValues();
        String i=num.getText().toString();
        idFromView=Integer.valueOf(i);
        topicFromView=topic.getText().toString();
        contentFromView=content.getText().toString();
        value.put(COLUMN_NUM,idFromView);
        value.put(COLUMN_TOPIC,topicFromView);
        value.put(COLUMN_CONTENT,contentFromView);
        return value;
    }

    private void initViews(Uri uri){
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);
        cursor.moveToNext();
        idGot=cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NUM));

        String topicGot=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
        String contentGot=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
        num.setText(String.valueOf(idGot));
        topic.setText(topicGot);
        content.setText(contentGot);
        cursor.close();
        editQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditTopicActivity.this,EditQuestionActivity.class);
                intent.putExtra(TWO_COLUMN_TOPIC_ID,idGot);
                Log.d(TAG, "initViews: id"+idGot);
                startActivity(intent);
            }
        });
    }
}
