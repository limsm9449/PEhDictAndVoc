package com.sleepingbear.pehdictandvoc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DaumVocabularyViewActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private DaumVocabularyViewCursorAdapter adapter;
    public String categoryId;
    public String kind;
    public int mSelect = 0;

    DaumVocabularyViewTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_vocabulary_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        Bundle b = this.getIntent().getExtras();
        categoryId = b.getString("CATEGORY_ID");
        kind = b.getString("KIND");

        ActionBar ab = (ActionBar) getSupportActionBar();
        ab.setTitle(b.getString("CATEGORY_NAME"));
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        getListView();
    }

    public void getListView() {
        Cursor cursor = db.rawQuery(DicQuery.getDaumVocabulary(categoryId), null);
        if ( cursor.getCount() == 0 && "R1,R2,R3".indexOf(DicUtils.getString(kind)) < 0 ) {
            if ( DicUtils.isNetWork(this) ) {
                task = new DaumVocabularyViewTask();
                task.execute();
            } else {
                Toast.makeText(getApplicationContext(), "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        ListView listView = (ListView) this.findViewById(R.id.my_lv);
        adapter = new DaumVocabularyViewCursorAdapter(getApplicationContext(), cursor, 0);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListener);
        listView.setOnItemLongClickListener(itemLongClickListener);
        listView.setSelection(0);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cur = (Cursor) adapter.getItem(position);

            Intent intent = new Intent(getApplication(), WordViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("entryId", cur.getString(cur.getColumnIndexOrThrow("ENTRY_ID")));
            bundle.putString("seq", cur.getString(cur.getColumnIndexOrThrow("_id")));
            intent.putExtras(bundle);

            startActivity(intent);
        }
    };

    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cur = (Cursor) adapter.getItem(position);

            final String entryId = cur.getString(cur.getColumnIndexOrThrow("ENTRY_ID"));
            final String word = cur.getString(cur.getColumnIndexOrThrow("WORD"));
            final String seq = cur.getString(cur.getColumnIndexOrThrow("_id"));

            //메뉴 선택 다이얼로그 생성
            Cursor cursor = db.rawQuery(DicQuery.getSentenceViewContextMenu(), null);
            final String[] kindCodes = new String[cursor.getCount()];
            final String[] kindCodeNames = new String[cursor.getCount()];

            int idx = 0;
            while (cursor.moveToNext()) {
                kindCodes[idx] = cursor.getString(cursor.getColumnIndexOrThrow("KIND"));
                kindCodeNames[idx] = cursor.getString(cursor.getColumnIndexOrThrow("KIND_NAME"));
                idx++;
            }
            cursor.close();

            final android.support.v7.app.AlertDialog.Builder dlg = new android.support.v7.app.AlertDialog.Builder(DaumVocabularyViewActivity.this);
            dlg.setTitle("단어장 선택");
            dlg.setSingleChoiceItems(kindCodeNames, mSelect, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    mSelect = arg1;
                }
            });
            dlg.setNegativeButton("취소", null);
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DicDb.insDicVoc(db, entryId, kindCodes[mSelect]);
                    adapter.dataChange();

                    DicUtils.setDbChange(getApplicationContext()); //변경여부 체크
                }
            });
            dlg.show();

            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_refresh) {
            if ( DicUtils.isNetWork(this) ) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("단어 정보를 동기화 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                task = new DaumVocabularyViewTask();
                                task.execute();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            } else {
                Toast.makeText(getApplicationContext(), "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_help) {
            Bundle bundle = new Bundle();
            bundle.putString("SCREEN", CommConstants.screen_daumVocabularyView);

            Intent intent = new Intent(getApplication(), HelpActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // 상단 메뉴 구성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);

        return true;
    }

    @Override
    // 메뉴 상태 변경
    public boolean onPrepareOptionsMenu(Menu menu) {
        if ( "TOEIC,TOEFL,TEPS,수능영어,NEAT/NEPT,초중고영어,회화,기타".indexOf(kind) > -1 ) {
            ((MenuItem)menu.findItem(R.id.action_refresh)).setVisible(true);
        } else {
            ((MenuItem) menu.findItem(R.id.action_refresh)).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private class DaumVocabularyViewTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(DaumVocabularyViewActivity.this);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
            pd.setContentView(R.layout.custom_progress);

            pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pd.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ArrayList wordAl = DicUtils.gatherCategoryWord("http://wordbook.daum.net/open/wordbook.do?id=" + categoryId);
            DicDb.delDaumVocabulary(db, categoryId);
            DicDb.insDaumVocabulary(db, categoryId, wordAl);
            DicDb.updDaumCategoryWordCount(db, categoryId);

            DicUtils.setDbChange(getApplicationContext()); //변경여부 체크

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
            task = null;

            getListView();

            super.onPostExecute(result);
        }
    }
}

class DaumVocabularyViewCursorAdapter extends CursorAdapter {
    int fontSize = 0;

    public DaumVocabularyViewCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);

        fontSize = Integer.parseInt( DicUtils.getPreferencesValue( context, CommConstants.preferences_font ) );
    }

    public void dataChange() {
        mCursor.requery();
        mCursor.move(mCursor.getPosition());

        //변경사항을 반영한다.
        notifyDataSetChanged();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.content_daum_vocabulary_view_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.my_tv_word)).setText(cursor.getString(cursor.getColumnIndexOrThrow("WORD")));
        ((TextView) view.findViewById(R.id.my_tv_spelling)).setText(cursor.getString(cursor.getColumnIndexOrThrow("SPELLING")));
        ((TextView) view.findViewById(R.id.my_tv_mean)).setText(cursor.getString(cursor.getColumnIndexOrThrow("MEAN")));

        //사이즈 설정
        ((TextView) view.findViewById(R.id.my_tv_word)).setTextSize(fontSize);
        ((TextView) view.findViewById(R.id.my_tv_spelling)).setTextSize(fontSize);
        ((TextView) view.findViewById(R.id.my_tv_mean)).setTextSize(fontSize);
    }
}