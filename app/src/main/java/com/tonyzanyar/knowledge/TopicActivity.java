package com.tonyzanyar.knowledge;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_CONTENT;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.MAX_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.MAX_NUM;
import static com.tonyzanyar.knowledge.data.Contract.MIN_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.MIN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.randomLines;

/**
 * @author zhangxin
 */
public class TopicActivity extends AppCompatActivity {

    private int firstNum;
    private int lastNum;
    private TextView topic;
    private TextView content;
    long idText;
    private int[] position;
    private int mark = 0;
    private Cursor cursor;
    private int cursorCount;
    private Button topicToQuestion;
    private Button anotherTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        topic = (TextView) findViewById(R.id.topic_text);
        content = (TextView) findViewById(R.id.content_text);
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        topicToQuestion=(Button)findViewById(R.id.test);
        anotherTopic=(Button)findViewById(R.id.another_topic);
        final Intent intent = getIntent();
        firstNum = intent.getIntExtra(MIN_NUM, MIN_LIMIT);
        lastNum = intent.getIntExtra(MAX_NUM,MAX_LIMIT);

        //下面是基于cursor的一切
        if (firstNum > lastNum) {
            return;
        }
        String selection = COLUMN_NUM + ">=" + firstNum + " AND " + COLUMN_NUM + "<=" + lastNum;
        String[] columns = {COLUMN_NUM, COLUMN_TOPIC, COLUMN_CONTENT};
        cursor = getContentResolver().query(CONTENT_URI, columns, selection, null, null);
        cursorCount = cursor.getCount();
        if (cursorCount < 1) {
            return;
        }
        position = randomLines(cursorCount);
        nextViews();
        anotherTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextViews();
            }
        });
        topicToQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(TopicActivity.this,QuestionActivity.class);
                int id=cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUM));
                intent1.putExtra(MIN_NUM,id);
                intent1.putExtra(MAX_NUM,id);
                startActivity(intent1);
            }
        });

    }

    private void nextViews(){
        cursor.moveToPosition(position[mark++]);
        if (mark >= cursorCount) {
            mark = 0;
        }
        idText = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NUM));
        String topicText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
        String contentText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
        topic.setText(idText + ". " + topicText);
        content.setText(contentText);
    }
}
