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
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;

public class DicCategoryActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    public DicCategoryCursorAdapter adapter;
    public Spinner s_dicgroup;
    public boolean isChange = false;
    private int mOrder = -1;
    private String mOrderName = "";
    private String dicGroup = "";
    private String vocName = "";
    private String kind = "";
    private int mSelect = 0;

    DicCategoryTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dic_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        ActionBar ab = (ActionBar) getSupportActionBar();
        ab.setTitle("카테고리");
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(DicQuery.getMainCategoryCount(), null);
        String[] from = new String[]{"KIND_NAME"};
        int[] to = new int[]{android.R.id.text1};
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, from, to);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_dicgroup = (Spinner) this.findViewById(R.id.my_c_dc_s_dicgroup);
        s_dicgroup.setAdapter(mAdapter);
        s_dicgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dicGroup = ((Cursor) s_dicgroup.getSelectedItem()).getString(1);

                //메뉴 갱신
                invalidateOptionsMenu();

                changeListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //정렬
        Spinner spinner = (Spinner) this.findViewById(R.id.my_c_dc_s_ord);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dicCategoryOrderValue, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mOrder = parent.getSelectedItemPosition();
                mOrderName = getResources().getStringArray(R.array.dicCategoryOrderValue)[mOrder];

                changeListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinner.setSelection(1);

        changeListView();
    }

    public void changeListView() {
        //정렬 보여주기
        String dicGroup = ((Cursor) s_dicgroup.getSelectedItem()).getString(1);
        if ("W".equals(dicGroup.substring(0, 1)) && !"W00".equals(dicGroup)) {
            Spinner spinner = (Spinner) this.findViewById(R.id.my_c_dc_s_ord);
            spinner.setVisibility(View.VISIBLE);
        } else {
            Spinner spinner = (Spinner) this.findViewById(R.id.my_c_dc_s_ord);
            spinner.setVisibility(View.GONE);
        }

        Cursor cursor = db.rawQuery(DicQuery.getSubCategoryCount(((Cursor) s_dicgroup.getSelectedItem()).getString(1), mOrder), null);

        ListView listView = (ListView) this.findViewById(R.id.my_c_dc_lv_dickind);
        adapter = new DicCategoryCursorAdapter(getApplicationContext(), cursor, this);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Cursor cur = (Cursor) adapter.getItem(position);
                kind = cur.getString(cur.getColumnIndexOrThrow("KIND"));

                if ("W".equals(kind.substring(0, 1))) {
                    //layout 구성
                    //메뉴 선택 다이얼로그 생성
                    Cursor cursor = db.rawQuery(DicQuery.getVocabularyKindContextMenu(), null);
                    final String[] kindCodes = new String[cursor.getCount()];
                    final String[] kindCodeNames = new String[cursor.getCount()];

                    int idx = 0;
                    while (cursor.moveToNext()) {
                        kindCodes[idx] = cursor.getString(cursor.getColumnIndexOrThrow("KIND"));
                        kindCodeNames[idx] = cursor.getString(cursor.getColumnIndexOrThrow("KIND_NAME"));
                        idx++;
                    }
                    cursor.close();

                    final android.support.v7.app.AlertDialog.Builder dlg = new android.support.v7.app.AlertDialog.Builder(DicCategoryActivity.this);
                    dlg.setTitle("단어장 선택");
                    dlg.setSingleChoiceItems(kindCodeNames, mSelect, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            mSelect = arg1;
                        }
                    });
                    dlg.setNeutralButton("신규 단어장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final View dialog_layout = getLayoutInflater().inflate(R.layout.dialog_dic_category, null);

                            //dialog 생성..
                            AlertDialog.Builder builder = new AlertDialog.Builder(DicCategoryActivity.this);
                            builder.setView(dialog_layout);
                            final AlertDialog alertDialog = builder.create();

                            final EditText et_voc_name = ((EditText) dialog_layout.findViewById(R.id.my_dc_et_voc_name));
                            et_voc_name.setText(cur.getString(cur.getColumnIndexOrThrow("KIND_NAME")));

                            ((Button) dialog_layout.findViewById(R.id.my_dc_b_save)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if ("".equals(et_voc_name.getText().toString())) {
                                        Toast.makeText(DicCategoryActivity.this, "단어장 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        alertDialog.dismiss();

                                        vocName = et_voc_name.getText().toString();

                                        String insCategoryCode = DicQuery.getInsCategoryCode(db);
                                        db.execSQL(DicQuery.getInsNewCategory("MY", insCategoryCode, vocName));

                                        Cursor wordCursor = db.rawQuery(DicQuery.getCategoryWord(kind), null);
                                        while ( wordCursor.moveToNext() ) {
                                            String entryId = wordCursor.getString(wordCursor.getColumnIndexOrThrow("ENTRY_ID"));
                                            DicDb.insDicVoc(db, entryId, insCategoryCode);
                                            // DicUtils. writeInfoToFile(getApplicationContext(), "MYWORD_INSERT" + ":" + insCategoryCode + ":" + DicUtils.getDelimiterDate(DicUtils.getCurrentDate(), ".") + ":" + entryId);
                                        }

                                        DicUtils. writeNewInfoToFile(getApplicationContext(), db);

                                        isChange = true;

                                        Toast.makeText(getApplicationContext(), "단어장에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            ((Button) dialog_layout.findViewById(R.id.my_dc_b_close)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                            isChange = true;
                        }
                    });
                    dlg.setNegativeButton("취소", null);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Cursor wordCursor = db.rawQuery(DicQuery.getCategoryWord(kind), null);
                            while ( wordCursor.moveToNext() ) {
                                String entryId = wordCursor.getString(wordCursor.getColumnIndexOrThrow("ENTRY_ID"));
                                DicDb.insDicVoc(db, entryId, kindCodes[mSelect]);
                            }

                            isChange = true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_refresh) {
            if ( DicUtils.isNetWork(this) ) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("최근 수정일자를 기준으로 카테고리 변경사항을 갱신합니다.\n변경 정보를 가져오기 위해서 데이타를 사용합니다.\n연결하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                task = new DicCategoryTask();
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
            bundle.putString("SCREEN", "DIC_CATEGORY");

            Intent intent = new Intent(getApplication(), HelpActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this.getApplication(), DicCategoryActivity.class);
        intent.putExtra("isChange", isChange);
        setResult(RESULT_OK, intent);

        finish();
    }

    private class DicCategoryTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(DicCategoryActivity.this);
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
            HashMap hm = new HashMap();
            hm.put("W01","1");
            hm.put("W02","2");
            hm.put("W03","3");
            hm.put("W04","4");
            hm.put("W05","5");
            hm.put("W06","12");
            hm.put("W07","9");
            hm.put("W08","13");

            if ( hm.containsKey(dicGroup) ) {
                ArrayList wordAl = DicUtils.gatherCategory(db, "http://wordbook.daum.net/open/wordbook/list.do?theme=" + hm.get(dicGroup) + "&order=recent&dic_type=endic", dicGroup);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
            task = null;

            changeListView();

            super.onPostExecute(result);
        }
    }
}

class DicCategoryCursorAdapter extends CursorAdapter {
    private String entryId = "";
    private String seq = "";
    private Cursor mCursor;
    private Activity mActivity;

    private Context mContext;

    static class ViewHolder {
        protected String kind;
        protected String kindName;
        protected String codeGroup;
    }

    public DicCategoryCursorAdapter(Context context, Cursor cursor, Activity activity) {
        super(context, cursor, 0);
        mContext = context;
        mCursor = cursor;
        mActivity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_dic_category_item, parent, false);

        ViewHolder viewHolder = new ViewHolder();
        view.setTag(viewHolder);

        //Item 선택
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder vViewHolder = (ViewHolder) v.getTag();

                Intent intent = new Intent(mActivity.getApplication(), DicCategoryViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("kind", vViewHolder.kind);
                bundle.putString("kindName", vViewHolder.kindName);
                bundle.putString("codeGroup", vViewHolder.codeGroup);

                intent.putExtras(bundle);

                mActivity.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.kind = cursor.getString(cursor.getColumnIndexOrThrow("KIND"));
        viewHolder.kindName = cursor.getString(cursor.getColumnIndexOrThrow("KIND_NAME"));
        viewHolder.codeGroup = cursor.getString(cursor.getColumnIndexOrThrow("CODE_GROUP"));

        String category = cursor.getString(cursor.getColumnIndexOrThrow("KIND_NAME"));
        if ( "W".equals(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("KIND"))).substring(0,1)) ) {
            String updDate = cursor.getString(cursor.getColumnIndexOrThrow("UPD_DATE"));
            if ( !"".equals(DicUtils.getString(updDate)) ) {
                category += " [" + updDate + ", " + cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARK_CNT")) + "]";
            }
            ((TextView) view.findViewById(R.id.my_c_dci_tv_category)).setText(category);
            ((TextView) view.findViewById(R.id.my_c_dci_tv_cnt)).setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("W_CNT"))));
        } else {
            ((TextView) view.findViewById(R.id.my_c_dci_tv_category)).setText(category);
            ((TextView) view.findViewById(R.id.my_c_dci_tv_cnt)).setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("S_CNT"))));
        }
    }
}