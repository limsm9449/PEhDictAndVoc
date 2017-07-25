package com.sleepingbear.pehdictandvoc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PatchActivity extends AppCompatActivity {
    private int fontSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        StringBuffer patch = new StringBuffer();

        patch.append("* 패치 내역" + CommConstants.sqlCR);
        patch.append("- 사전검색 History에서 선택없이 백할 경우 발생하는 오류 수정."  + CommConstants.sqlCR);
        patch.append(""  + CommConstants.sqlCR);
        patch.append("- 2017.07.20"  + CommConstants.sqlCR);
        patch.append("영한사전에 여러 기능을 추가하다보니 어플의 특색이 없어졌습니다."  + CommConstants.sqlCR);
        patch.append("그래서 어플 이름처럼 영한사전 기능으로 변경하였습니다."  + CommConstants.sqlCR);
        patch.append("사전 이외의 기능을 많이 사용하셨다면 '최고의 영어학습' 어플을 이용해보세요."  + CommConstants.sqlCR);
        patch.append("이용에 불편을 드려서 죄송합니다."  + CommConstants.sqlCR);

        ((TextView) this.findViewById(R.id.my_c_patch_tv1)).setText(patch.toString());

        fontSize = Integer.parseInt( DicUtils.getPreferencesValue( this, CommConstants.preferences_font ) );
        ((TextView) this.findViewById(R.id.my_c_patch_tv1)).setTextSize(fontSize);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
