package com.tonyzanyar.knowledge;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tonyzanyar.knowledge.data.MyAdapter;

import static com.tonyzanyar.knowledge.data.Contract.COLUMN_CONTENT;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_NUM;
import static com.tonyzanyar.knowledge.data.Contract.COLUMN_TOPIC;
import static com.tonyzanyar.knowledge.data.Contract.CONTENT_URI;
import static com.tonyzanyar.knowledge.data.Contract.KEY_WORD;
import static com.tonyzanyar.knowledge.data.Contract.MAX_NUM;
import static com.tonyzanyar.knowledge.data.Contract.MIN_NUM;

/**
 * @author zhangxin
 */
public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView list;
    private MyAdapter myAdapter;
    private static final int LOAD=2;
    private String keyGot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent1=getIntent();
        keyGot=intent1.getStringExtra(KEY_WORD);
        if (TextUtils.isEmpty(keyGot)){
            keyGot="商用车分类";
        }
        list= (ListView) findViewById(R.id.list_search);
        myAdapter=new MyAdapter(this,null);
        list.setAdapter(myAdapter);
        getLoaderManager().initLoader(LOAD,null,SearchActivity.this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(SearchActivity.this,TopicActivity.class);
                intent.putExtra(MIN_NUM,(int)l);
                intent.putExtra(MAX_NUM,(int)l);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String []columns={COLUMN_NUM,COLUMN_TOPIC};
        String like="'%"+keyGot+"%'";
        String selection=COLUMN_TOPIC+" lIKE "+like+" OR "+COLUMN_CONTENT+" lIKE "+like;
        return new CursorLoader(this,CONTENT_URI,columns,selection,null,COLUMN_NUM);
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
