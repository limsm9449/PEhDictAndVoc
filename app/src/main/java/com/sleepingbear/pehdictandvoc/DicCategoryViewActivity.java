package com.sleepingbear.pehdictandvoc;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class DicCategoryViewActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private DicCategoryViewCursorAdapter adapter;
    public String kind;
    public String dicGroup;
    public int mSelect = 0;

    DicCategoryViewTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dic_category_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        Bundle b = this.getIntent().getExtras();
        kind = b.getString("kind");
        dicGroup = b.getString("codeGroup");

        ActionBar ab = (ActionBar) getSupportActionBar();
        ab.setTitle(b.getString("kindName"));
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        getListView();
    }

    public void getListView() {
        StringBuffer sql = new StringBuffer();
        if ( "W".equals(kind.substring(0,1)) ) {
            sql.append("SELECT B.SEQ _id," + CommConstants.sqlCR);
            sql.append("       B.SEQ," + CommConstants.sqlCR);
            sql.append("       B.WORD," + CommConstants.sqlCR);
            sql.append("       B.MEAN," + CommConstants.sqlCR);
            sql.append("       B.ENTRY_ID," + CommConstants.sqlCR);
            sql.append("       B.SPELLING," + CommConstants.sqlCR);
            sql.append("       (SELECT COUNT(*) FROM DIC_VOC WHERE ENTRY_ID = A.ENTRY_ID) MY_VOC" + CommConstants.sqlCR);
            sql.append("  FROM DIC_CATEGORY_WORD A, DIC B" + CommConstants.sqlCR);
            sql.append(" WHERE A.ENTRY_ID = B.ENTRY_ID" + CommConstants.sqlCR);
            sql.append("   AND A.CODE = '" + kind + "'" + CommConstants.sqlCR);
            sql.append(" ORDER BY B.WORD" + CommConstants.sqlCR);
        } else {
            sql.append("SELECT SEQ _id," + CommConstants.sqlCR);
            sql.append("       SEQ," + CommConstants.sqlCR);
            sql.append("       SENTENCE1," + CommConstants.sqlCR);
            sql.append("       SENTENCE2" + CommConstants.sqlCR);
            sql.append("  FROM DIC_CATEGORY_SENT " + CommConstants.sqlCR);
            sql.append(" WHERE CODE = '" + kind + "'" + CommConstants.sqlCR);
            sql.append(" ORDER BY SEQ" + CommConstants.sqlCR);
        }
        DicUtils.dicSqlLog(sql.toString());

        Cursor cursor = db.rawQuery(sql.toString(), null);

        ListView listView = (ListView) this.findViewById(R.id.my_c_dk_lv1);
        adapter = new DicCategoryViewCursorAdapter(getApplicationContext(), cursor, this, db);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if ( "W".equals(kind.substring(0,1)) ) {
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

                    final android.support.v7.app.AlertDialog.Builder dlg = new android.support.v7.app.AlertDialog.Builder(DicCategoryViewActivity.this);
                    dlg.setTitle("메뉴 선택");
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
                            DicUtils.writeInfoToFile(getApplicationContext(), "MYWORD_INSERT" + ":" + kindCodes[mSelect] + ":" + DicUtils.getDelimiterDate(DicUtils.getCurrentDate(), ".") + ":" + entryId);
                        }
                    });
                    dlg.show();
                }

                return true;
            };
        });
        listView.setSelection(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_refresh) {
            if ( DicUtils.isNetWork(this) ) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("단어 정보를 가져오기 위해서 데이타를 사용합니다.\n연결하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                task = new DicCategoryViewTask();
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
            bundle.putString("SCREEN", "DIC_CATEGORY_VIEW");

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
        if ( "W01,W02,W03,W04,W05,W06,W07,W08".indexOf(dicGroup) > -1 ) {
            ((MenuItem)menu.findItem(R.id.action_refresh)).setVisible(true);
        } else {
            ((MenuItem) menu.findItem(R.id.action_refresh)).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private class DicCategoryViewTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(DicCategoryViewActivity.this);
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
            ArrayList wordAl = DicUtils.gatherCategoryWord("http://wordbook.daum.net/open/wordbook.do?id=" + kind.substring(1,kind.length()));
            DicDb.delDicCategoryWord(db, kind);
            DicDb.insDicCategoryWord(db, kind, wordAl);
            DicDb.updDicCategoryInfo(db, kind);

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

class DicCategoryViewCursorAdapter extends CursorAdapter {
    private String entryId = "";
    private String seq = "";
    private Activity mActivity;
    private Cursor mCursor;
    private SQLiteDatabase mDb;

    static class ViewHolder {
        protected String entryId;
        protected String seq;
        protected ImageButton myvoc;
        protected boolean isMyVoc;
        protected String sentence1;
        protected String sentence2;
    }

    public DicCategoryViewCursorAdapter(Context context, Cursor cursor, Activity activity, SQLiteDatabase db) {
        super(context, cursor, 0);
        mCursor = cursor;
        mActivity = activity;
        mDb = db;
    }

    public void dataChange() {
        mCursor.requery();
        mCursor.move(mCursor.getPosition());

        //변경사항을 반영한다.
        notifyDataSetChanged();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = null;

        ViewHolder viewHolder = new ViewHolder();

        if ( "W".equals(((DicCategoryViewActivity)mActivity).kind.substring(0,1)) ) {
            view = LayoutInflater.from(context).inflate(R.layout.content_dic_category_view_item_w, parent, false);

            //Item 선택
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder vViewHolder = (ViewHolder) v.getTag();
                    Intent intent = new Intent(mActivity.getApplication(), WordViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("entryId", vViewHolder.entryId);
                    bundle.putString("seq", vViewHolder.seq);
                    intent.putExtras(bundle);

                    mActivity.startActivity(intent);
                }
            });

            viewHolder.myvoc = (ImageButton) view.findViewById(R.id.my_c_dcviw_ib_myvoc);
            viewHolder.myvoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder viewHolder = (ViewHolder)v.getTag();

                    if ( viewHolder.isMyVoc ) {
                        DicDb.delDicVocAll(mDb, viewHolder.entryId);
                        DicUtils.writeInfoToFile(mActivity, "MYWORD_DELETE_ALL" + ":" + viewHolder.entryId);
                    } else {
                        DicDb.insDicVoc(mDb, viewHolder.entryId, "MY0000");
                        DicUtils.writeInfoToFile(mActivity, "MYWORD_INSERT" + ":" + "MY0000" + ":" + DicUtils.getDelimiterDate(DicUtils.getCurrentDate(), ".") + ":" + viewHolder.entryId);
                    }

                    dataChange();
                }
            });
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.content_dic_category_view_item_s, parent, false);

            //Item 선택
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder vViewHolder = (ViewHolder) v.getTag();

                    Cursor cursor = mDb.rawQuery(DicQuery.getDicForWord(vViewHolder.sentence1), null);
                    if (cursor.moveToNext()) {
                        Intent intent = new Intent(mActivity.getApplication(), WordViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("entryId", cursor.getString(cursor.getColumnIndexOrThrow("ENTRY_ID")));
                        bundle.putString("seq", cursor.getString(cursor.getColumnIndexOrThrow("SEQ")));
                        intent.putExtras(bundle);

                        mActivity.startActivity(intent);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("foreign", vViewHolder.sentence1);
                        bundle.putString("han", vViewHolder.sentence2);

                        Intent intent = new Intent(mActivity.getApplication(), SentenceViewActivity.class);
                        intent.putExtras(bundle);

                        mActivity.startActivity(intent);
                    }
                }
            });
        }

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if ( "W".equals(((DicCategoryViewActivity)mActivity).kind.substring(0,1)) ) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.entryId = cursor.getString(cursor.getColumnIndexOrThrow("ENTRY_ID"));
            viewHolder.seq = cursor.getString(cursor.getColumnIndexOrThrow("SEQ"));
            viewHolder.myvoc.setTag(viewHolder);

            ((TextView) view.findViewById(R.id.my_c_dcviw_tv_viet)).setText(cursor.getString(cursor.getColumnIndexOrThrow("WORD")));
            ((TextView) view.findViewById(R.id.my_c_dcviw_tv_spelling)).setText(cursor.getString(cursor.getColumnIndexOrThrow("SPELLING")));
            ((TextView) view.findViewById(R.id.my_c_dcviw_tv_mean)).setText(cursor.getString(cursor.getColumnIndexOrThrow("MEAN")));

            ImageButton ib_myvoc = (ImageButton)view.findViewById(R.id.my_c_dcviw_ib_myvoc);
            if ( cursor.getInt(cursor.getColumnIndexOrThrow("MY_VOC")) > 0 ) {
                ib_myvoc.setImageResource(android.R.drawable.star_on);
                viewHolder.isMyVoc = true;
            } else {
                ib_myvoc.setImageResource(android.R.drawable.star_off);
                viewHolder.isMyVoc = false;
            }
        } else {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.sentence1 = cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE1"));
            viewHolder.sentence2 = cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE2"));

            ((TextView) view.findViewById(R.id.my_c_dcis_tv_sentence1)).setText(cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE1")));
            ((TextView) view.findViewById(R.id.my_c_dcis_tv_sentence2)).setText(cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE2")));
        }
    }
}