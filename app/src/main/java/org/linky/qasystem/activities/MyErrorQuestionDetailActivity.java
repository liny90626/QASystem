package org.linky.qasystem.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.linky.qasystem.R;
import org.linky.qasystem.util.ConstantUtil;
import org.linky.qasystem.util.Tools;

public class MyErrorQuestionDetailActivity extends Activity {

    private ImageView mLeft;
    private TextView mTitle;

    private ImageView mQuestionImgIV;
    private TextView mQuestionTypeTV;
    private TextView mQuestionNameTV;
    private LinearLayout mLayoutA;
    private LinearLayout mLayoutB;
    private LinearLayout mLayoutC;
    private LinearLayout mLayoutD;
    private LinearLayout mLayoutE;
    private ImageView mIvA;
    private ImageView mIvB;
    private ImageView mIvC;
    private ImageView mIvD;
    private ImageView mIvE;
    private TextView mTvA;
    private TextView mTvB;
    private TextView mTvC;
    private TextView mTvD;
    private TextView mTvE;
    private ImageView mIvA_;
    private ImageView mIvB_;
    private ImageView mIvC_;
    private ImageView mIvD_;
    private ImageView mIvE_;
    private LinearLayout mWrongLayout;
    private TextView mExplainDetailTv;

    private String mQueestionImg = "";
    private String mQuestionName = "";
    private String mQuestionType = "";
    private String mQuestionAnswer = "";
    private String mQuestionSelect = "";
    private String mIsRight = "";
    private String mAnalysis = "";
    private String mOptionA = "";
    private String mOptionB = "";
    private String mOptionC = "";
    private String mOptionD = "";
    private String mOptionE = "";
    private String mOptionType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_error_question_detail);
        initView();
    }

    private void initView() {
        mQueestionImg = getIntent().getStringExtra("questionImg");
        mQuestionName = getIntent().getStringExtra("questionName");
        mQuestionType = getIntent().getStringExtra("questionType");
        mQuestionAnswer = getIntent().getStringExtra("questionAnswer");
        mQuestionSelect = getIntent().getStringExtra("questionSelect");
        mIsRight = getIntent().getStringExtra("isRight");
        mAnalysis = getIntent().getStringExtra("Analysis");
        mOptionA = getIntent().getStringExtra("optionA");
        mOptionB = getIntent().getStringExtra("optionB");
        mOptionC = getIntent().getStringExtra("optionC");
        mOptionD = getIntent().getStringExtra("optionD");
        mOptionE = getIntent().getStringExtra("optionE");
        mOptionType = getIntent().getStringExtra("optionType");
        mLeft = (ImageView) findViewById(R.id.left);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.my_errors);
        mLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        mQuestionImgIV = (ImageView) findViewById(R.id.activity_prepare_test_img);
        mQuestionTypeTV = (TextView) findViewById(R.id.activity_prepare_test_no);
        mQuestionNameTV = (TextView) findViewById(R.id.activity_prepare_test_question);
        mLayoutA = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_a);
        mLayoutB = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_b);
        mLayoutC = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_c);
        mLayoutD = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_d);
        mLayoutE = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_e);
        mIvA = (ImageView) findViewById(R.id.vote_submit_select_image_a);
        mIvB = (ImageView) findViewById(R.id.vote_submit_select_image_b);
        mIvC = (ImageView) findViewById(R.id.vote_submit_select_image_c);
        mIvD = (ImageView) findViewById(R.id.vote_submit_select_image_d);
        mIvE = (ImageView) findViewById(R.id.vote_submit_select_image_e);
        mTvA = (TextView) findViewById(R.id.vote_submit_select_text_a);
        mTvB = (TextView) findViewById(R.id.vote_submit_select_text_b);
        mTvC = (TextView) findViewById(R.id.vote_submit_select_text_c);
        mTvD = (TextView) findViewById(R.id.vote_submit_select_text_d);
        mTvE = (TextView) findViewById(R.id.vote_submit_select_text_e);
        mIvA_ = (ImageView) findViewById(R.id.vote_submit_select_image_a_);
        mIvB_ = (ImageView) findViewById(R.id.vote_submit_select_image_b_);
        mIvC_ = (ImageView) findViewById(R.id.vote_submit_select_image_c_);
        mIvD_ = (ImageView) findViewById(R.id.vote_submit_select_image_d_);
        mIvE_ = (ImageView) findViewById(R.id.vote_submit_select_image_e_);
        mWrongLayout = (LinearLayout) findViewById(R.id.activity_prepare_test_wrongLayout);
        mExplainDetailTv = (TextView) findViewById(R.id.activity_prepare_test_explain_detail);

        mQuestionNameTV.setText("" + mQuestionName);

        if (mOptionA.equals("")) {
            mLayoutA.setVisibility(View.GONE);
        }
        if (mOptionB.equals("")) {
            mLayoutB.setVisibility(View.GONE);
        }
        if (mOptionC.equals("")) {
            mLayoutC.setVisibility(View.GONE);
        }
        if (mOptionD.equals("")) {
            mLayoutD.setVisibility(View.GONE);
        }
        if (mOptionE.equals("")) {
            mLayoutE.setVisibility(View.GONE);
        }

        //文字题目
        mIvA_.setVisibility(View.GONE);
        mIvB_.setVisibility(View.GONE);
        mIvC_.setVisibility(View.GONE);
        mIvD_.setVisibility(View.GONE);
        mIvE_.setVisibility(View.GONE);
        mTvA.setVisibility(View.VISIBLE);
        mTvB.setVisibility(View.VISIBLE);
        mTvC.setVisibility(View.VISIBLE);
        mTvD.setVisibility(View.VISIBLE);
        mTvE.setVisibility(View.VISIBLE);
        mTvA.setText("A." + mOptionA);
        mTvB.setText("B." + mOptionB);
        mTvC.setText("C." + mOptionC);
        mTvD.setText("D." + mOptionD);
        mTvE.setText("E." + mOptionE);

        if (mQuestionType.equals("0")) {
            mQuestionImgIV.setVisibility(View.GONE);
            mQuestionTypeTV.setText(R.string.single_choice);
            //显示正确选项
            if (mQuestionAnswer.contains("A")) {
                mIvA.setImageResource(R.drawable.ic_practice_test_right);
                mTvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("B")) {
                mIvB.setImageResource(R.drawable.ic_practice_test_right);
                mTvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("C")) {
                mIvC.setImageResource(R.drawable.ic_practice_test_right);
                mTvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("D")) {
                mIvD.setImageResource(R.drawable.ic_practice_test_right);
                mTvD.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("E")) {
                mIvE.setImageResource(R.drawable.ic_practice_test_right);
                mTvE.setTextColor(Color.parseColor("#61bc31"));
            }

            if (mQuestionSelect.contains("A")) {
                mIvA.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvA.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("B")) {
                mIvB.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvB.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("C")) {
                mIvC.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvC.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("D")) {
                mIvD.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvD.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("E")) {
                mIvE.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvE.setTextColor(Color.parseColor("#d53235"));
            }

        } else if (mQuestionType.equals("1")) {
            mQuestionImgIV.setVisibility(View.GONE);
            mQuestionTypeTV.setText(R.string.multiple_choice);
            //显示正确选项
            if (mQuestionAnswer.contains("A")) {
                mIvA.setImageResource(R.drawable.ic_practice_test_right);
                mTvA.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mQuestionAnswer.contains("B")) {
                mIvB.setImageResource(R.drawable.ic_practice_test_right);
                mTvB.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mQuestionAnswer.contains("C")) {
                mIvC.setImageResource(R.drawable.ic_practice_test_right);
                mTvC.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mQuestionAnswer.contains("D")) {
                mIvD.setImageResource(R.drawable.ic_practice_test_right);
                mTvD.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mQuestionAnswer.contains("E")) {
                mIvE.setImageResource(R.drawable.ic_practice_test_right);
                mTvE.setTextColor(Color.parseColor("#61bc31"));
            }

            if (mQuestionSelect.contains("A")) {
                mIvA.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvA.setTextColor(Color.parseColor("#d53235"));
            }
            if (mQuestionSelect.contains("B")) {
                mIvB.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvB.setTextColor(Color.parseColor("#d53235"));
            }
            if (mQuestionSelect.contains("C")) {
                mIvC.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvC.setTextColor(Color.parseColor("#d53235"));
            }
            if (mQuestionSelect.contains("D")) {
                mIvD.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvD.setTextColor(Color.parseColor("#d53235"));
            }
            if (mQuestionSelect.contains("E")) {
                mIvE.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvE.setTextColor(Color.parseColor("#d53235"));
            }
        } else if (mQuestionType.equals("2")) {
            mQuestionImgIV.setVisibility(View.GONE);
            mQuestionTypeTV.setText(R.string.true_or_false);
            //显示正确选项
            if (mQuestionAnswer.contains("A")) {
                mIvA.setImageResource(R.drawable.ic_practice_test_right);
                mTvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("B")) {
                mIvB.setImageResource(R.drawable.ic_practice_test_right);
                mTvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("C")) {
                mIvC.setImageResource(R.drawable.ic_practice_test_right);
                mTvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("D")) {
                mIvD.setImageResource(R.drawable.ic_practice_test_right);
                mTvD.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("E")) {
                mIvE.setImageResource(R.drawable.ic_practice_test_right);
                mTvE.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mQuestionSelect.contains("A")) {
                mIvA.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvA.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("B")) {
                mIvB.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvB.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("C")) {
                mIvC.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvC.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("D")) {
                mIvD.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvD.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("E")) {
                mIvE.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvE.setTextColor(Color.parseColor("#d53235"));
            }

            mLayoutC.setVisibility(View.GONE);
            mLayoutD.setVisibility(View.GONE);
        } else if (mQuestionType.equals("3")) {
            mQuestionImgIV.setVisibility(View.VISIBLE);
            Bitmap imgBitmap = Tools.getImageFromAssetsFile(MyErrorQuestionDetailActivity.this, mQueestionImg);
            if (null != imgBitmap) {
                mQuestionImgIV.setImageBitmap(imgBitmap);
            }
            mQuestionTypeTV.setText(R.string.single_choice);
            //显示正确选项
            if (mQuestionAnswer.contains("A")) {
                mIvA.setImageResource(R.drawable.ic_practice_test_right);
                mTvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("B")) {
                mIvB.setImageResource(R.drawable.ic_practice_test_right);
                mTvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("C")) {
                mIvC.setImageResource(R.drawable.ic_practice_test_right);
                mTvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("D")) {
                mIvD.setImageResource(R.drawable.ic_practice_test_right);
                mTvD.setTextColor(Color.parseColor("#61bc31"));
            } else if (mQuestionAnswer.contains("E")) {
                mIvE.setImageResource(R.drawable.ic_practice_test_right);
                mTvE.setTextColor(Color.parseColor("#61bc31"));
            }

            if (mQuestionSelect.contains("A")) {
                mIvA.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvA.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("B")) {
                mIvB.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvB.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("C")) {
                mIvC.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvC.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("D")) {
                mIvD.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvD.setTextColor(Color.parseColor("#d53235"));
            } else if (mQuestionSelect.contains("E")) {
                mIvE.setImageResource(R.drawable.ic_practice_test_wrong);
                mTvE.setTextColor(Color.parseColor("#d53235"));
            }
        }

        if (mIsRight.equals(ConstantUtil.isError)) {
            mWrongLayout.setVisibility(View.VISIBLE);
            mExplainDetailTv.setText("" + mAnalysis);
        } else {
            mWrongLayout.setVisibility(View.GONE);
        }
    }

}
