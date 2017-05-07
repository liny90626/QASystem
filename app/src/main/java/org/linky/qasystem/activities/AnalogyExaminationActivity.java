package org.linky.qasystem.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.linky.qasystem.R;
import org.linky.qasystem.adapter.ExaminationSubmitAdapter;
import org.linky.qasystem.bean.AnswerInfo;
import org.linky.qasystem.bean.ErrorQuestionInfo;
import org.linky.qasystem.bean.SaveQuestionInfo;
import org.linky.qasystem.database.DBManager;
import org.linky.qasystem.util.ConstantUtil;
import org.linky.qasystem.util.ViewPagerScroller;
import org.linky.qasystem.view.VoteSubmitViewPager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AnalogyExaminationActivity extends Activity {

    private ImageView mLeftIv;
    private TextView mTitleTv;
    private TextView mRight;

    protected VoteSubmitViewPager mViewPager;
    protected ExaminationSubmitAdapter mPagerAdapter;
    protected List<View> mViewItems = new ArrayList<View>();
    protected List<AnswerInfo> mDataItems = new ArrayList<AnswerInfo>();
    private ProgressDialog mProgressDialog;

    private String mPageCode;
    private int mPageScore;
    private int mErrorTopicNums;// 错题数
    private int mErrorTopicNums1;// 错题数
    private String mIsPerfectData = "1";// 是否完善资料0完成 1未完成
    private String mType = "0";// 0模拟 1竞赛
    private String mErrorMsg = "";

    private Dialog mBuilderSubmit;

    private List<Map<String, SaveQuestionInfo>> mList = new ArrayList<Map<String, SaveQuestionInfo>>();
    private Map<String, SaveQuestionInfo> mMap2 = new HashMap<String, SaveQuestionInfo>();
    public List<SaveQuestionInfo> mQuestionInfos = new ArrayList<SaveQuestionInfo>();

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mMinute = 2;
    private int mSecond = 0;

    private boolean mIsPause = false;
    private int mIsFirst;

    private DBManager mDbManager;

    private String mDateStr = "";
    protected String mImgServerUrl = "";

    private boolean isUpload = false;

    private Handler handlerSubmit = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
            case 1:
                showSubmitDialog();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mBuilderSubmit.dismiss();
                        finish();
                    }
                }, 3000);
                break;
            default:
                break;
            }

        }
    };


    Handler handlerTime = new Handler() {
        public void handleMessage(Message msg) {
            // 判断时间快到前2分钟字体颜色改变
            if (mMinute < 1) {
                mRight.setTextColor(Color.RED);
            } else {
                mRight.setTextColor(Color.WHITE);
            }
            if (mMinute == 0) {
                if (mSecond == 0) {
                    mIsFirst += 1;
                    // 时间到
                    if (mIsFirst == 1) {
                        showTimeOutDialog(true, "0");
                    }
                    mRight.setText("00:00");
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                    if (mTimerTask != null) {
                        mTimerTask = null;
                    }
                } else {
                    mSecond--;
                    if (mSecond >= 10) {
                        mRight.setText("0" + mMinute + ":" + mSecond);
                    } else {
                        mRight.setText("0" + mMinute + ":0" + mSecond);
                    }
                }
            } else {
                if (mSecond == 0) {
                    mSecond = 59;
                    mMinute--;
                    if (mMinute >= 10) {
                        mRight.setText(mMinute + ":" + mSecond);
                    } else {
                        mRight.setText("0" + mMinute + ":" + mSecond);
                    }
                } else {
                    mSecond--;
                    if (mSecond >= 10) {
                        if (mMinute >= 10) {
                            mRight.setText(mMinute + ":" + mSecond);
                        } else {
                            mRight.setText("0" + mMinute + ":" + mSecond);
                        }
                    } else {
                        if (mMinute >= 10) {
                            mRight.setText(mMinute + ":0" + mSecond);
                        } else {
                            mRight.setText("0" + mMinute + ":0" + mSecond);
                        }
                    }
                }
            }
        }

        ;
    };

    private Handler handlerStopTime = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
                stopTime();
                break;
            case 1:
                startTime();
                break;
            default:
                break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_practice_test);
        mDbManager = new DBManager(AnalogyExaminationActivity.this);
        mDbManager.openDB();
        initView();
        loadData();

        for (int i = 0; i < mDataItems.size(); i++) {
            mViewItems.add(getLayoutInflater().inflate(
                    R.layout.vote_submit_viewpager_item, null));
        }
        mPagerAdapter = new ExaminationSubmitAdapter(
                this, mViewItems,
                mDataItems, mImgServerUrl);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.getParent()
                .requestDisallowInterceptTouchEvent(false);

        ErrorQuestionInfo[] errorQuestionInfos = mDbManager.queryAllData();
        if (errorQuestionInfos != null) {
            // 删除上次保存的我的错题
            mDbManager.deleteAllData();
        }
    }

    public void initView() {
        mLeftIv = (ImageView) findViewById(R.id.left);
        mTitleTv = (TextView) findViewById(R.id.title);
        mRight = (TextView) findViewById(R.id.right);
        mTitleTv.setText(R.string.examining);
        Drawable drawable1 = getBaseContext().getResources().getDrawable(
                R.drawable.ic_practice_time);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
                drawable1.getMinimumHeight());
        mRight.setCompoundDrawables(drawable1, null, null, null);
        mRight.setText(getResources().getString(R.string.default_exam_time));
        mViewPager = (VoteSubmitViewPager) findViewById(R.id.vote_submit_viewpager);
        mLeftIv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // finish();
                mIsPause = true;
                showTimeOutDialog(true, "1");
                Message msg = new Message();
                msg.what = 0;
                handlerStopTime.sendMessage(msg);
            }
        });

        initViewPagerScroll();

    }

    protected abstract void loadData();

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(mViewPager.getContext());
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    /**
     * @param index 根据索引值切换页面
     */
    public void setCurrentView(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (null != mDbManager) {
            mDbManager.closeDB();
        }
        stopTime();
        mMinute = -1;
        mSecond = -1;
        super.onDestroy();
    }

    // 提交试卷
    public void uploadExamination(int errortopicNum) {
        // TODO Auto-generated method stub
        String resultlist = "[";
        mErrorTopicNums = errortopicNum;

        if (mQuestionInfos.size() > 0) {
            //选择过题目
            //全部选中
            if (mQuestionInfos.size() == mDataItems.size()) {
                for (int i = 0; i < mQuestionInfos.size(); i++) {
                    if (i == mQuestionInfos.size() - 1) {
                        resultlist += mQuestionInfos.get(i).toString() + "]";
                    } else {
                        resultlist += mQuestionInfos.get(i).toString() + ",";
                    }
                    if (mQuestionInfos.size() == 0) {
                        resultlist += "]";
                    }
                    if (mQuestionInfos.get(i).getIs_correct()
                            .equals(ConstantUtil.isCorrect)) {
                        int score = Integer.parseInt(mQuestionInfos.get(i).getScore());
                        mPageScore += score;
                    }
                }
            } else {
                //部分选中
                for (int i = 0; i < mDataItems.size(); i++) {
                    if (mDataItems.get(i).getIsSelect() == null) {
                        mErrorTopicNums1 += 1;
                        //保存数据
                        SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                        questionInfo.setQuestionId(mDataItems.get(i).getQuestionId());
                        questionInfo.setQuestionType(mDataItems.get(i).getQuestionType());
                        questionInfo.setRealAnswer(mDataItems.get(i).getCorrectAnswer());
                        questionInfo.setScore(mDataItems.get(i).getScore());
                        questionInfo.setIs_correct(ConstantUtil.isError);
                        mQuestionInfos.add(questionInfo);
                    }
                }

                for (int i = 0; i < mDataItems.size(); i++) {
                    if (i == mDataItems.size() - 1) {
                        resultlist += mQuestionInfos.get(i).toString() + "]";
                    } else {
                        resultlist += mQuestionInfos.get(i).toString() + ",";
                    }
                    if (mDataItems.size() == 0) {
                        resultlist += "]";
                    }
                    if (mQuestionInfos.get(i).getIs_correct()
                            .equals(ConstantUtil.isCorrect)) {
                        int score = Integer.parseInt(mQuestionInfos.get(i).getScore());
                        mPageScore += score;
                    }
                }
            }
        } else {
            //没有选择题目
            for (int i = 0; i < mDataItems.size(); i++) {
                if (mDataItems.get(i).getIsSelect() == null) {
                    mErrorTopicNums1 += 1;
                    //保存数据
                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(mDataItems.get(i).getQuestionId());
                    questionInfo.setQuestionType(mDataItems.get(i).getQuestionType());
                    questionInfo.setRealAnswer(mDataItems.get(i).getCorrectAnswer());
                    questionInfo.setScore(mDataItems.get(i).getScore());
                    questionInfo.setIs_correct(ConstantUtil.isError);
                    mQuestionInfos.add(questionInfo);
                }
            }

            for (int i = 0; i < mDataItems.size(); i++) {
                if (i == mDataItems.size() - 1) {
                    resultlist += mQuestionInfos.get(i).toString() + "]";
                } else {
                    resultlist += mQuestionInfos.get(i).toString() + ",";
                }
                if (mDataItems.size() == 0) {
                    resultlist += "]";
                }
                if (mQuestionInfos.get(i).getIs_correct()
                        .equals(ConstantUtil.isCorrect)) {
                    int score = Integer.parseInt(mQuestionInfos.get(i).getScore());
                    mPageScore += score;
                }
            }
        }

        Message msg = handlerSubmit.obtainMessage();
        msg.what = 1;
        handlerSubmit.sendMessage(msg);

    }

    // 弹出对话框通知用户答题时间到
    protected void showTimeOutDialog(final boolean flag, final String backType) {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.my_dialog);
        builder.getWindow().setGravity(Gravity.CENTER);
        TextView title = (TextView) builder.findViewById(R.id.dialog_title);
        TextView content = (TextView) builder.findViewById(R.id.dialog_content);
        if (backType.equals("0")) {
            content.setText(R.string.exam_time_out);
        } else if (backType.equals("1")) {
            content.setText(R.string.exam_manual_end);
        } else {
            content.setText(mErrorMsg + "");
        }
        final Button confirm_btn = (Button) builder
                .findViewById(R.id.dialog_sure);
        Button cancel_btn = (Button) builder.findViewById(R.id.dialog_cancle);
        if (backType.equals("0")) {
            confirm_btn.setText(R.string.commit);
            cancel_btn.setText(R.string.cancel);
        } else if (backType.equals("1")) {
            confirm_btn.setText(R.string.cancel);
            cancel_btn.setText(R.string.continue_test);
        } else {
            confirm_btn.setText(R.string.confirm);
            cancel_btn.setVisibility(View.GONE);
        }
        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backType.equals("0")) {
                    builder.dismiss();
                    uploadExamination(mPagerAdapter.errorTopicNum());
                } else {
                    builder.dismiss();
                    finish();
                }
            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backType.equals("0")) {
                    finish();
                    builder.dismiss();
                } else {
                    mIsPause = false;
                    builder.dismiss();
                    Message msg = new Message();
                    msg.what = 1;
                    handlerStopTime.sendMessage(msg);
                }
            }
        });
        builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
        builder.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                return flag;
            }
        });
        builder.show();
    }

    // 弹出对话框通知用户提交成功
    protected void showSubmitDialog() {
        mBuilderSubmit = new Dialog(this, R.style.dialog);
        mBuilderSubmit.setContentView(R.layout.my_dialog);
        mBuilderSubmit.getWindow().setGravity(Gravity.CENTER);
        TextView title = (TextView) mBuilderSubmit.findViewById(R.id.dialog_title);
        TextView content = (TextView) mBuilderSubmit.findViewById(R.id.dialog_content);
        content.setText(R.string.commit_success);
        final Button confirm_btn = (Button) mBuilderSubmit
                .findViewById(R.id.dialog_sure);
        confirm_btn.setVisibility(View.GONE);
        Button cancel_btn = (Button) mBuilderSubmit.findViewById(R.id.dialog_cancle);
        cancel_btn.setVisibility(View.GONE);
        mBuilderSubmit.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
        mBuilderSubmit.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        mBuilderSubmit.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            mIsPause = true;
            showTimeOutDialog(true, "1");
            Message msg = new Message();
            msg.what = 0;
            handlerStopTime.sendMessage(msg);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        Message msg = new Message();
        msg.what = 0;
        handlerStopTime.sendMessage(msg);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Message msg = new Message();
        msg.what = 1;
        handlerStopTime.sendMessage(msg);
        super.onResume();
    }

    private void startTime() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {

                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    handlerTime.sendMessage(msg);
                }
            };
        }
        if (mTimer != null && mTimerTask != null) {
            mTimer.schedule(mTimerTask, 0, 1000);
        }
    }

    private void stopTime() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    protected int loadQuestionFromAssets(String file) {
        InputStream is = null;
        try {
            is = getAssets().open(file);
            if (0 != parseQuestion(is)) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e2) {
            }
        }

        return 0;
    }

    private int parseQuestion(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;

        int i = 0;
        AnswerInfo curLineInfo = null;
        try {
            while ((str = bufferedReader.readLine()) != null) {
                if (str.isEmpty() || str.startsWith("#")) {
                    continue;
                }

                // 解析Question时负责创建AnswerInfo
                curLineInfo = createAnswerInfo(++i);
                curLineInfo.setQuestionName(str);
                mDataItems.add(curLineInfo);
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e2) {
            }
        }
        return 0;
    }

    protected abstract AnswerInfo createAnswerInfo(int questionId);

    protected int loadAnalysisFromAssets(String file) {
        InputStream is = null;
        try {
            is = getAssets().open(file);
            if (0 != parseAnalysis(is)) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e2) {
            }
        }

        return 0;
    }

    private int parseAnalysis(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;

        int i = 0;
        AnswerInfo curLineInfo = null;
        try {
            while ((str = bufferedReader.readLine()) != null) {
                // 解析Question时负责创建AnswerInfo
                if (i < mDataItems.size()) {
                    curLineInfo = mDataItems.get(i++);
                    curLineInfo.setAnalysis(str);
                }
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e2) {
            }
        }
        return 0;
    }

    protected int loadCorrectAnswerFromAssets(String file) {
        InputStream is = null;
        try {
            is = getAssets().open(file);
            if (0 != parseCorrectAnswer(is)) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e2) {
            }
        }

        return 0;
    }

    private int parseCorrectAnswer(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;

        int i = 0;
        AnswerInfo curLineInfo = null;
        try {
            while ((str = bufferedReader.readLine()) != null) {
                // 解析Question时负责创建AnswerInfo
                if (i < mDataItems.size()) {
                    curLineInfo = mDataItems.get(i++);
                    curLineInfo.setCorrectAnswer(str);
                }
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e2) {
            }
        }
        return 0;
    }

    protected int loadOptionsFromAssets(String file) {
        InputStream is = null;
        try {
            is = getAssets().open(file);
            if (0 != parseOptions(is)) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e2) {
            }
        }

        return 0;
    }

    private int parseOptions(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;

        int i = 0;
        AnswerInfo curLineInfo = null;
        try {
            while ((str = bufferedReader.readLine()) != null) {
                // 解析Question时负责创建AnswerInfo
                if (i < mDataItems.size()) {
                    curLineInfo = mDataItems.get(i++);
                    int j = 0;
                    for (String option : str.split(";")) {
                        switch (j++) {
                        case 0:
                            curLineInfo.setOptionA(option);
                            break;
                        case 1:
                            curLineInfo.setOptionB(option);
                            break;
                        case 2:
                            curLineInfo.setOptionC(option);
                            break;
                        case 3:
                            curLineInfo.setOptionD(option);
                            break;
                        case 4:
                            curLineInfo.setOptionE(option);
                            break;
                        default:
                            // 暂时只支持5个选项
                            break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e2) {
            }
        }
        return 0;
    }
}
