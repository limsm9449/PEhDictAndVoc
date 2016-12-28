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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = (ActionBar) getSupportActionBar();
        ab.setTitle("패치 내용");
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        StringBuffer patch = new StringBuffer();

        patch.append("* 2016.12.28" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("- 카테고리에서 단어장에 추가시 기존 단어장을 선택해서 넣거나, 신규 단어장으로 넣을 수 있도록 수정." + CommConstants.sqlCR);
        patch.append("- 단어장에서 선택을 해서 삭제하거나, 다른 단어장으로 복사, 이동하는 기능 추가" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);

        patch.append("* 2016.11.5" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("- 단어장에서 'MY 단어장'을 삭제한 후에 다른 단어를 추가할 경우 오류가 발생하여 삭제 못하도록 수정." + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);

        patch.append("* 2016.10.1" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("- 영한 사전 및 단어장 개발" + CommConstants.sqlCR);

        ((TextView) this.findViewById(R.id.my_c_patch_tv1)).setText(patch.toString());
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
