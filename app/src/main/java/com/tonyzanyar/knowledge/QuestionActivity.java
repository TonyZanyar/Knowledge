package com.tonyzanyar.knowledge;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_CONTENT;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.MAX_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.MAX_NUM;
import static com.tonyzanyar.knowledge.data.Contract.MIN_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.MIN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_QUESTION;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_TOPIC_ID;
import static com.tonyzanyar.knowledge.data.Contract.TWO_CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.randomLines;

/**
 * @author zhangxin
 */
public class QuestionActivity extends AppCompatActivity {

    private Button anotherQuestion;
    private Button questionToTopic;
    private int firstNum;
    private int lastNum;
    private int[] position;
    private int mark = 0;
    long idText;
    private Cursor cursor;
    private int cursorCount;
    private TextView question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        question= (TextView) findViewById(R.id.question_text);
        anotherQuestion= (Button) findViewById(R.id.another_question);
        questionToTopic= (Button) findViewById(R.id.answer);
        final Intent intent = getIntent();
        firstNum = intent.getIntExtra(MIN_NUM, MIN_LIMIT);
        lastNum = intent.getIntExtra(MAX_NUM,MAX_LIMIT);

        //下面是基于cursor的一切
        if (firstNum > lastNum) {
            return;
        }
        String selection =TWO_COLUMN_TOPIC_ID + ">=" + firstNum + " AND " + TWO_COLUMN_TOPIC_ID + "<=" + lastNum;
        String[] columns = {TWO_COLUMN_NUM, TWO_COLUMN_QUESTION, TWO_COLUMN_TOPIC_ID};
        cursor = getContentResolver().query(TWO_CONTENT_URI, columns, selection, null, null);
        cursorCount = cursor.getCount();
        if (cursorCount < 1) {
            return;
        }
        position = randomLines(cursorCount);
        nextViews();
        anotherQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextViews();
            }
        });
        questionToTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(QuestionActivity.this,TopicActivity.class);
                int id=cursor.getInt(cursor.getColumnIndexOrThrow(TWO_COLUMN_TOPIC_ID));
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
        String contentText = cursor.getString(cursor.getColumnIndexOrThrow(TWO_COLUMN_QUESTION));
        question.setText(contentText);
    }
}
