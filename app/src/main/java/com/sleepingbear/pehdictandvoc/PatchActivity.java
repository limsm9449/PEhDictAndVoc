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

        patch.append("* 신규 패치" + CommConstants.sqlCR);
        patch.append("- 단어장 상세 부분을 네이버 검색, 다움 검색으로 변경하였습니다." + CommConstants.sqlCR);
        patch.append("- 영어사전, 영어회화, 영어신문을 하나로 통합하여 사용하면 좋을듯해서 '최고의 영어학습' 어플을 새로 만들었습니다. 한개의 어플로 계속 기능개선을 할 예정입니다." + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("- 사전 단어 검색시 여러 단어로 검색을 하도록 수정을 했습니다." + CommConstants.sqlCR);
        patch.append("  .'food love'을 입력하시면 food,love을 검색 합니다." + CommConstants.sqlCR);
        patch.append("  .'fo'을 입력하시면 fo로 시작하는 단어를 검색합니다." + CommConstants.sqlCR);
        patch.append("- 검색된 사전에서 롱클릭을 하면 바로 단어장으로 등록을 할 수 있습니다." + CommConstants.sqlCR);
        patch.append("- 검색단어 입력후 X 버튼 클릭시 재조회로 불편함이 있어 입력을 받도록 수정" + CommConstants.sqlCR);
        patch.append("- 단어장에서 TTS로 단어, 뜻을 듣는 기능 추가 - 상단 Context Menu에서 TTS 듣기 선택" + CommConstants.sqlCR);
        patch.append("- 단어학습에서 '카드형 4지선다 TTS 학습' 기능 추가" + CommConstants.sqlCR);
        patch.append("- 카테고리에서 단어장에 추가시 기존 단어장을 선택해서 넣거나, 신규 단어장으로 넣을 수 있도록 수정." + CommConstants.sqlCR);
        patch.append("- 단어장에서 선택을 해서 삭제하거나, 다른 단어장으로 복사, 이동하는 기능 추가" + CommConstants.sqlCR);
        patch.append("- 단어장에서 'MY 단어장'을 삭제한 후에 다른 단어를 추가할 경우 오류가 발생하여 삭제 못하도록 수정." + CommConstants.sqlCR);
        patch.append("- 2016.10.1 : 영한 사전 및 단어장 개발" + CommConstants.sqlCR);

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
