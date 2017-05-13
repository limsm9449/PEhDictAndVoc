package com.sleepingbear.pehdictandvoc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = (ActionBar) getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();
        StringBuffer allSb = new StringBuffer();
        StringBuffer CurrentSb = new StringBuffer();
        StringBuffer tempSb = new StringBuffer();

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 단어장" + CommConstants.sqlCR);
        tempSb.append("- 내가 등록한 단어를 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .하단의 + 버튼을 클릭해서 신규 단어장을 추가할 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .기존 단어장을 길게 클릭하시면 수정, 추가, 삭제,  내보내기, 가져오기를 하실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .단어장을 클릭하시면 등록된 단어를 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "VOCABULARY".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 단어장 - 나의 예문" + CommConstants.sqlCR);
        tempSb.append("- 내가 체크한 예문을 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "MY_SAMPLE".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 단어장 - 카테고리별" + CommConstants.sqlCR);
        tempSb.append("- 단어장 및 회화를 카테고리로 분리 해서 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("- 단어장에 등록할 카테고리를 선택해서 길게 클릭하면 단어장에 등록하실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("- 상단 Refresh 버튼을 클릭시 최신 정보로 갱신이 됩니다.(데이타 사용)" + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "DIC_CATEGORY".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 단어장 - 카테고리별 상세" + CommConstants.sqlCR);
        tempSb.append("- 선택한 카테고리별 등록 단어를 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("- 단어를 롱클릭후 단어장에 등록하실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("- 상단 Refresh 버튼을 클릭시 최신 정보로 갱신이 됩니다.(데이타 사용)" + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "DIC_CATEGORY_VIEW".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 단어장 - 단어 학습" + CommConstants.sqlCR);
        tempSb.append("- 등록한 단어를 5가지 방법으로 공부할 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .단어장 선택, 학습 종류 선택, 시간 선택을 하신후 학습시작을 클릭하세요." + CommConstants.sqlCR);
        tempSb.append(" .Default는 현재부터 60일전에 등록한 단어입니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "STUDY".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 단답 학습" + CommConstants.sqlCR);
        tempSb.append(" .단어를 클릭하시면 뜻을 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .별표를 클릭해서 암기여부를 표시합니다." + CommConstants.sqlCR);
        tempSb.append(" .단어를 길게 클릭하시면 단어 보기/전체 정답 보기를 선택하실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "STUDY1".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 4지선다 학습" + CommConstants.sqlCR);
        tempSb.append(" .별표를 클릭해서 암기여부를 표시합니다." + CommConstants.sqlCR);
        tempSb.append(" .단어를 길게 클릭하시면 정답 보기/ 단어 보기/전체 정답 보기를 선택하실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "STUDY2".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 카드형 학습" + CommConstants.sqlCR);
        tempSb.append("- 카드형 학습입니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "STUDY3".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 카드형 OX 학습" + CommConstants.sqlCR);
        tempSb.append("- 카드형 OX 학습입니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "STUDY4".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 카드형 4지선다 학습" + CommConstants.sqlCR);
        tempSb.append("- 카드형 4지선다 학습입니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "STUDY5".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 사전" + CommConstants.sqlCR);
        tempSb.append("- 영한 사전, 한영 사전을 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .단어를 클릭하시면 단어 상세를 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .예문을 클릭하시면 문장 상세를 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "DICTIONARY".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 오늘의 단어" + CommConstants.sqlCR);
        tempSb.append("- 오늘의 단어를 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .데이타 삭제 버튼을 클릭하면 오늘의 단어를 전체 삭제하실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "TODAY".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 단어 상세" + CommConstants.sqlCR);
        tempSb.append("- 상단 콤보 메뉴를 선택하시면 네이버 사전, 다음 사전, 예제를 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "WORDVIEW".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 문장 상세" + CommConstants.sqlCR);
        tempSb.append("- 문장의 발음 및 관련 단어를 조회하실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .연필 버튼을 클릭해서 문장을 입력하시면 관련 단어와 해석을 조회하실 수 있습니다.(해석은 정확도가 떨어지니 참고만 하세요)" + CommConstants.sqlCR);
        tempSb.append(" .단어를 클릭하시면 단어 보기 및 등록할 단어장을 선택 하실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .별표를 클릭하시면 Default 단어장에 추가 됩니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "SENTENCEVIEW".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 기타" + CommConstants.sqlCR);
        tempSb.append("- 영어 사이트, 영어 번역, E-Mail, 어플 백업 및 복구 기능을 실행 할 수 있습니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "OTHER".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        tempSb.delete(0, tempSb.length());
        tempSb.append("* 영어 사이트 " + CommConstants.sqlCR);
        tempSb.append(" .해석할 문장을 선택하여 클립보드에 복사를 하세요." + CommConstants.sqlCR);
        tempSb.append(" .오른쪽 하단에 있는 i 버튼을 클릭하세요." + CommConstants.sqlCR);
        tempSb.append(" .선택한 문장을 기준으로 관련 단어들을 보실 수 있습니다." + CommConstants.sqlCR);
        tempSb.append(" .문장을 선택 안하고 i 버튼을 클릭할 경우 클립보드에 들어있는 문장을 기준으로 단어가 조회 됩니다." + CommConstants.sqlCR);
        tempSb.append("" + CommConstants.sqlCR);
        if ( "WEB_VIEW".equals(b.getString("SCREEN")) ) {
            CurrentSb.append(tempSb.toString());
        } else {
            allSb.append(tempSb.toString());
        }

        if ( "ALL".equals(b.getString("SCREEN")) ) {
            ((TextView) this.findViewById(R.id.my_c_help_tv1)).setText(allSb.toString());
        } else {
            ((TextView) this.findViewById(R.id.my_c_help_tv1)).setText(CurrentSb.toString() + CommConstants.sqlCR + CommConstants.sqlCR + allSb.toString());
        }
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
