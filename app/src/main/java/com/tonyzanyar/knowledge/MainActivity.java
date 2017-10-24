package com.tonyzanyar.knowledge;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tonyzanyar.knowledge.data.MyAdapter;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_CONTENT;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.KEY_WORD;
import static com.tonyzanyar.knowledge.data.Contract.MAX_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.MAX_NUM;
import static com.tonyzanyar.knowledge.data.Contract.MIN_LIMIT;
import static com.tonyzanyar.knowledge.data.Contract.MIN_NUM;
/**
 * @author zhangxin
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "MainActivity1";
    private MyAdapter myAdapter;
    private Button randomTopic;
    private Button randomQuestion;
    private Button keySearch;
    private EditText firstTopic;
    private EditText lastTopic;
    private EditText keyWords;
    private ListView list;
    private FloatingActionButton fab;
    private int firstNum;
    private int lastNum;
    private String key;
    private static final int LOAD=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){
        randomTopic= (Button) findViewById(R.id.random_topic);
        randomQuestion= (Button) findViewById(R.id.random_question);
        keySearch=(Button)findViewById(R.id.key_search);
        firstTopic=(EditText)findViewById(R.id.first_topic);
        lastTopic=(EditText)findViewById(R.id.last_topic);
        keyWords=(EditText)findViewById(R.id.key_words);
        fab= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        list=(ListView)findViewById(R.id.list);
        myAdapter=new MyAdapter(this,null);
        list.setAdapter(myAdapter);
        getLoaderManager().initLoader(LOAD,null,MainActivity.this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,EditTopicActivity.class);
                intent.setData(Uri.withAppendedPath(CONTENT_URI,String.valueOf(l)));
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("您是否要删除这条话题？");
                final long id=l;
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri=Uri.withAppendedPath(CONTENT_URI,String.valueOf(id));
                        int d=getContentResolver().delete(uri,null,null);
                        if (d==-1){
                            Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog a=builder.create();
                a.show();
                return true;
            }
        });
        randomTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();
                if (firstNum>=MIN_LIMIT&&lastNum>=MIN_LIMIT&&lastNum<=MAX_LIMIT&&firstNum<=lastNum) {
                    Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                    intent.putExtra(MIN_NUM,firstNum);
                    intent.putExtra(MAX_NUM,lastNum);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "请输入合理的序号范围", Toast.LENGTH_SHORT).show();
                }
            }
        });
        randomQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();
                if (firstNum>=MIN_LIMIT&&lastNum>=MIN_LIMIT&&lastNum<=MAX_LIMIT&&firstNum<=lastNum) {
                    Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                    intent.putExtra(MIN_NUM,firstNum);
                    intent.putExtra(MAX_NUM,lastNum);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "请输入合理的序号范围", Toast.LENGTH_SHORT).show();
                }
            }
        });
        keySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();
                if (TextUtils.isEmpty(key)){
                    Toast.makeText(MainActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra(KEY_WORD,key);
                    startActivity(intent);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,EditTopicActivity.class);
                    startActivity(intent);
            }
        });
    }

    private void getText(){
        String f=firstTopic.getText().toString();
        if (!TextUtils.isEmpty(f)){
            firstNum=Integer.parseInt(f);
        }else {
            firstNum=-1;
        }
        String l=lastTopic.getText().toString();
        if (!TextUtils.isEmpty(l)){
            lastNum=Integer.parseInt(l);
        }else {
            lastNum=-1;
        }
        key=keyWords.getText().toString();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String []columns={COLUMN_NUM,COLUMN_TOPIC};
        return new CursorLoader(this,CONTENT_URI,columns,null,null,COLUMN_NUM);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished: cursor num "+cursor.getCount());
        myAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);
    }
}
