package org.linky.qasystem.activities;

import android.widget.Toast;

import org.linky.qasystem.R;
import org.linky.qasystem.bean.AnswerInfo;

public class MultipleChoiceActivity extends AnalogyExaminationActivity {
    private final static String QUESTION_FILE = "MultipleChoice/Question.txt";
    private final static String ANALYSIS_FILE = "MultipleChoice/Analysis.txt";
    private final static String CORRECT_ANSWER_FILE = "MultipleChoice/CorrectAnswer.txt";
    private final static String OPTIONS_FILE = "MultipleChoice/Options.txt";

    @Override
    protected void loadData() {
        // Clear old data
        mDataItems.clear();
        if (0 != loadQuestionFromAssets(QUESTION_FILE)
                || 0 != loadAnalysisFromAssets(ANALYSIS_FILE)
                || 0 != loadCorrectAnswerFromAssets(CORRECT_ANSWER_FILE)
                || 0 != loadOptionsFromAssets(OPTIONS_FILE)) {
            Toast.makeText(MultipleChoiceActivity.this, R.string.no_data,
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected AnswerInfo createAnswerInfo(int questionId) {
        String noData = getString(R.string.no_data);

        AnswerInfo info = new AnswerInfo();
        info.setQuestionId(String.valueOf(questionId));         // 试题主键
        info.setQuestionName(noData);                           // 试题题目
        info.setQuestionType("1");                              // 试题类型, 0单选, 1多选, 2判断
        info.setQuestionFor("0");                               // 0模拟试题，1竞赛试题
        info.setAnalysis(noData);                               // 试题分析
        info.setCorrectAnswer(noData);                          // 正确答案
        info.setOptionA("");                                    // 试题选项A
        info.setOptionB("");                                    // 试题选项B
        info.setOptionC("");                                    // 试题选项C
        info.setOptionD("");                                    // 试题选项D
        info.setOptionE("");                                    // 试题选项E
        info.setScore("1");                                     // 分值
        info.setOption_type("0");
        return info;
    }

}
