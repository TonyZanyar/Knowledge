package com.tonyzanyar.knowledge;

import android.app.LoaderManager;
import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tonyzanyar.knowledge.data.QuestionAdapter;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_QUESTION;
import static com.tonyzanyar.knowledge.data.Contract.TWO_COLUMN_TOPIC_ID;
import static com.tonyzanyar.knowledge.data.Contract.TWO_CONTENT_URI;

/**
 * @author zhangxin
 */
public class EditQuestionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "EditQuestionActivity1";
    private int idGot;
    private QuestionAdapter myAdapter;
    private ListView list;
    private static final int LOAD=3;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        Intent intent=getIntent();
        idGot= (int) intent.getLongExtra(TWO_COLUMN_TOPIC_ID,1);
        Log.d(TAG, "onCreate: id"+idGot);
        list= (ListView) findViewById(R.id.question_list);
        fab= (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        myAdapter=new QuestionAdapter(this,null);
        list.setAdapter(myAdapter);
        getLoaderManager().initLoader(LOAD,null,EditQuestionActivity.this);
        initViews();
    }

    private void initViews() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long id=l;
                AlertDialog.Builder builder=new AlertDialog.Builder(EditQuestionActivity.this);
                builder.setTitle("要删除这条题目么?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri=Uri.withAppendedPath(TWO_CONTENT_URI,String.valueOf(id));
                        int d=getContentResolver().delete(uri,null,null);
                        if (d==-1){
                            Toast.makeText(EditQuestionActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(EditQuestionActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert=builder.create();
                alert.show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText text=new EditText(EditQuestionActivity.this);
                AlertDialog.Builder builder=new AlertDialog.Builder(EditQuestionActivity.this);
                builder.setTitle("新建题目");
                builder.setView(text);
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String q=text.getText().toString();
                        if (!TextUtils.isEmpty(q)){
                            ContentValues v=new ContentValues();
                            v.put(TWO_COLUMN_QUESTION,q);
                            v.put(TWO_COLUMN_TOPIC_ID,idGot);
                            getContentResolver().insert(TWO_CONTENT_URI,v);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog al=builder.create();
                al.show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String []columns={TWO_COLUMN_NUM,TWO_COLUMN_QUESTION,TWO_COLUMN_TOPIC_ID};
        String selection=TWO_COLUMN_TOPIC_ID+"=?";
        String []selectionArgs={String.valueOf(idGot)};
        return new CursorLoader(this,TWO_CONTENT_URI,columns,selection,selectionArgs,TWO_COLUMN_NUM);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        myAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);
    }
}
