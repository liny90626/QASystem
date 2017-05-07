package org.linky.qasystem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.linky.qasystem.R;
import org.linky.qasystem.adapter.MyErrorQuestionListAdapter;
import org.linky.qasystem.bean.ErrorQuestion;
import org.linky.qasystem.bean.ErrorQuestionInfo;
import org.linky.qasystem.database.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyErrorQuestionActivity extends Activity {

    private ImageView mLeft;
    private TextView mTitle;

    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();// 列表数据
    private ListView mListView;

    private List<ErrorQuestion> mList = new ArrayList<ErrorQuestion>();

    private MyErrorQuestionListAdapter mAdapter;

    private ErrorQuestion mQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_error_question);

        initView();
    }

    private void initView() {
        mLeft = (ImageView) findViewById(R.id.left);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.my_errors);
        mListView = (ListView) findViewById(R.id.listview);

        mLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        mAdapter = new MyErrorQuestionListAdapter(this, mData, mListView);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MyErrorQuestionActivity.this, MyErrorQuestionDetailActivity.class);
                mQuestion = mList.get(position);
                intent.putExtra("questionName", mQuestion.getQuestionName());
                intent.putExtra("questionType", mQuestion.getQuestionType());
                intent.putExtra("questionAnswer", mQuestion.getQuestionAnswer());
                intent.putExtra("questionSelect", mQuestion.getQuestionSelect());
                intent.putExtra("isRight", mQuestion.getIsRight());
                intent.putExtra("Analysis", mQuestion.getAnalysis());
                intent.putExtra("optionA", mQuestion.getOptionA());
                intent.putExtra("optionB", mQuestion.getOptionB());
                intent.putExtra("optionC", mQuestion.getOptionC());
                intent.putExtra("optionD", mQuestion.getOptionD());
                intent.putExtra("optionE", mQuestion.getOptionE());
                intent.putExtra("optionType", mQuestion.getOptionType());
                startActivity(intent);
            }
        });

        DBManager dbManager = new DBManager(MyErrorQuestionActivity.this);
        dbManager.openDB();

        ErrorQuestionInfo[] errorQuestionInfos = dbManager.queryAllData();
        if (errorQuestionInfos == null) {
            Toast.makeText(MyErrorQuestionActivity.this, R.string.no_data,
                    Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> map = null;
            for (int i = 0; i < errorQuestionInfos.length; i++) {
                ErrorQuestion errorQuestion = new ErrorQuestion();
                map = new HashMap<String, Object>();
                map.put("title", errorQuestionInfos[i].questionName);// 标题
                map.put("type", errorQuestionInfos[i].questionType);// 标题
                map.put("answer", errorQuestionInfos[i].questionAnswer);// 标题
                map.put("isright", errorQuestionInfos[i].isRight);//
                map.put("selected", errorQuestionInfos[i].questionSelect);//
                map.put("analysis", errorQuestionInfos[i].Analysis);//
                mData.add(map);

                errorQuestion.setQuestionName(errorQuestionInfos[i].questionName);
                errorQuestion.setQuestionType(errorQuestionInfos[i].questionType);
                errorQuestion.setQuestionAnswer(errorQuestionInfos[i].questionAnswer);
                errorQuestion.setQuestionSelect(errorQuestionInfos[i].questionSelect);
                errorQuestion.setIsRight(errorQuestionInfos[i].isRight);
                errorQuestion.setAnalysis(errorQuestionInfos[i].Analysis);
                errorQuestion.setOptionA(errorQuestionInfos[i].optionA);
                errorQuestion.setOptionB(errorQuestionInfos[i].optionB);
                errorQuestion.setOptionC(errorQuestionInfos[i].optionC);
                errorQuestion.setOptionD(errorQuestionInfos[i].optionD);
                errorQuestion.setOptionE(errorQuestionInfos[i].optionE);
                errorQuestion.setOptionType(errorQuestionInfos[i].optionType);
                mList.add(errorQuestion);
            }
        }
    }
}
