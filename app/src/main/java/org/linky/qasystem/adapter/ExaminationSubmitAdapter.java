package org.linky.qasystem.adapter;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.linky.qasystem.R;
import org.linky.qasystem.activities.AnalogyExaminationActivity;
import org.linky.qasystem.activities.MyErrorQuestionActivity;
import org.linky.qasystem.bean.AnswerInfo;
import org.linky.qasystem.bean.ErrorQuestionInfo;
import org.linky.qasystem.bean.SaveQuestionInfo;
import org.linky.qasystem.database.DBManager;
import org.linky.qasystem.util.ConstantUtil;
import org.linky.qasystem.util.Tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExaminationSubmitAdapter extends PagerAdapter {

    private AnalogyExaminationActivity mContext;
    // 传递过来的页面view的集合
    private List<View> mViewItems;
    // 每个item的页面view
    private View mConvertView;
    // 传递过来的所有数据
    private List<AnswerInfo> mDataItems;

    private String mImgServerUrl = "";

    private Map<Integer, Boolean> mMap = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> mMapClick = new HashMap<Integer, Boolean>();
    private Map<Integer, String> mMapMultiSelect = new HashMap<Integer, String>();

    private boolean mIsClick = false;
    private boolean mIsNext = false;

    private StringBuffer mAnswer = new StringBuffer();
    private StringBuffer mAnswerLast = new StringBuffer();
    private StringBuffer mAnswer1 = new StringBuffer();

    private DBManager mDbManager;

    private String mIsCorrect = ConstantUtil.isCorrect;//1对，0错

    private int mErrorTopicNum = 0;

    private String mResultA = "";
    private String mResultB = "";
    private String mResultC = "";
    private String mResultD = "";
    private String mResultE = "";

    public ExaminationSubmitAdapter(AnalogyExaminationActivity context, List<View> viewItems, List<AnswerInfo> dataItems, String imgServerUrl) {
        mContext = context;
        mViewItems = viewItems;
        mDataItems = dataItems;
        mImgServerUrl = imgServerUrl;
        mDbManager = new DBManager(context);
        mDbManager.openDB();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewItems.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ViewHolder holder = new ViewHolder();
        mConvertView = mViewItems.get(position);
        holder.questionImg = (ImageView) mConvertView.findViewById(R.id.activity_prepare_test_img);
        holder.questionType = (TextView) mConvertView.findViewById(R.id.activity_prepare_test_no);
        holder.question = (TextView) mConvertView.findViewById(R.id.activity_prepare_test_question);
        holder.previousBtn = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_upLayout);
        holder.nextBtn = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_nextLayout);
        holder.nextText = (TextView) mConvertView.findViewById(R.id.menu_bottom_nextTV);
        holder.errorBtn = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_errorLayout);
        holder.totalText = (TextView) mConvertView.findViewById(R.id.activity_prepare_test_totalTv);
        holder.nextImage = (ImageView) mConvertView.findViewById(R.id.menu_bottom_nextIV);
        holder.wrongLayout = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_wrongLayout);
        holder.explainDetailTv = (TextView) mConvertView.findViewById(R.id.activity_prepare_test_explain_detail);
        holder.layoutA = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_layout_a);
        holder.layoutB = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_layout_b);
        holder.layoutC = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_layout_c);
        holder.layoutD = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_layout_d);
        holder.layoutE = (LinearLayout) mConvertView.findViewById(R.id.activity_prepare_test_layout_e);
        holder.ivA = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_a);
        holder.ivB = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_b);
        holder.ivC = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_c);
        holder.ivD = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_d);
        holder.ivE = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_e);
        holder.tvA = (TextView) mConvertView.findViewById(R.id.vote_submit_select_text_a);
        holder.tvB = (TextView) mConvertView.findViewById(R.id.vote_submit_select_text_b);
        holder.tvC = (TextView) mConvertView.findViewById(R.id.vote_submit_select_text_c);
        holder.tvD = (TextView) mConvertView.findViewById(R.id.vote_submit_select_text_d);
        holder.tvE = (TextView) mConvertView.findViewById(R.id.vote_submit_select_text_e);
        holder.ivA_ = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_a_);
        holder.ivB_ = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_b_);
        holder.ivC_ = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_c_);
        holder.ivD_ = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_d_);
        holder.ivE_ = (ImageView) mConvertView.findViewById(R.id.vote_submit_select_image_e_);

        holder.totalText.setText(position + 1 + "/" + mDataItems.size());

        holder.errorBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, MyErrorQuestionActivity.class);
                mContext.startActivity(intent);
            }
        });

        if (mDataItems.get(position).getOptionA().equals("")) {
            holder.layoutA.setVisibility(View.GONE);
        }
        if (mDataItems.get(position).getOptionB().equals("")) {
            holder.layoutB.setVisibility(View.GONE);
        }
        if (mDataItems.get(position).getOptionC().equals("")) {
            holder.layoutC.setVisibility(View.GONE);
        }
        if (mDataItems.get(position).getOptionD().equals("")) {
            holder.layoutD.setVisibility(View.GONE);
        }
        if (mDataItems.get(position).getOptionE().equals("")) {
            holder.layoutE.setVisibility(View.GONE);
        }

        //判断是否文字图片题目
        //文字题目
        holder.ivA_.setVisibility(View.GONE);
        holder.ivB_.setVisibility(View.GONE);
        holder.ivC_.setVisibility(View.GONE);
        holder.ivD_.setVisibility(View.GONE);
        holder.ivE_.setVisibility(View.GONE);
        holder.tvA.setVisibility(View.VISIBLE);
        holder.tvB.setVisibility(View.VISIBLE);
        holder.tvC.setVisibility(View.VISIBLE);
        holder.tvD.setVisibility(View.VISIBLE);
        holder.tvE.setVisibility(View.VISIBLE);
        holder.tvA.setText("A." + mDataItems.get(position).getOptionA());
        holder.tvB.setText("B." + mDataItems.get(position).getOptionB());
        holder.tvC.setText("C." + mDataItems.get(position).getOptionC());
        holder.tvD.setText("D." + mDataItems.get(position).getOptionD());
        holder.tvE.setText("E." + mDataItems.get(position).getOptionE());

        //判断题型
        if (mDataItems.get(position).getQuestionType().equals("0")) {
            //单选题
            holder.questionImg.setVisibility(View.GONE);
            holder.questionType.setText(mContext.getString(R.string.single_choice));
            holder.question.setText((position + 1) + ". "
                    + mDataItems.get(position).getQuestionName());

            holder.layoutA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'A');
                }
            });

            holder.layoutB.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'B');
                }
            });

            holder.layoutC.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'C');
                }
            });

            holder.layoutD.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'D');
                }
            });

            holder.layoutE.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'E');
                }
            });

        } else if (mDataItems.get(position).getQuestionType().equals("1")) {
            //多选题
            holder.questionImg.setVisibility(View.GONE);
            holder.questionType.setText(mContext.getString(R.string.multiple_choice));
            holder.question.setText((position + 1) + ". "
                    + mDataItems.get(position).getQuestionName());

            holder.layoutA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onMultipleChoiceClick(holder, position, 'A');
                    mResultA = "A";
                }
            });

            holder.layoutB.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onMultipleChoiceClick(holder, position, 'B');
                    mResultB = "B";
                }
            });

            holder.layoutC.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onMultipleChoiceClick(holder, position, 'C');
                    mResultC = "C";
                }
            });

            holder.layoutD.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onMultipleChoiceClick(holder, position, 'D');
                    mResultD = "D";
                }
            });

            holder.layoutE.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onMultipleChoiceClick(holder, position, 'E');
                    mResultE = "E";
                }
            });
        } else if (mDataItems.get(position).getQuestionType().equals("2")) {
            //判断题
            holder.questionImg.setVisibility(View.GONE);
            holder.questionType.setText(mContext.getString(R.string.true_or_false));
            holder.question.setText((position + 1) + ". "
                    + mDataItems.get(position).getQuestionName());
            holder.layoutA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onJudgementClick(holder, position, 'A');
                }
            });

            holder.layoutB.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onJudgementClick(holder, position, 'B');
                }
            });

            holder.layoutC.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onJudgementClick(holder, position, 'C');
                }
            });

            holder.layoutD.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onJudgementClick(holder, position, 'D');
                }
            });

            holder.layoutE.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onJudgementClick(holder, position, 'E');
                }
            });
        } else if ((mDataItems.get(position).getQuestionType().equals("3"))) {
            //看图单选题
            holder.questionImg.setVisibility(View.VISIBLE);
            String imgPath = mDataItems.get(position).getQuestionImg();
            Bitmap imgBitmap = Tools.getImageFromAssetsFile(mContext, imgPath);
            if (null != imgBitmap) {
                holder.questionImg.setImageBitmap(imgBitmap);
            }
            holder.questionType.setText(mContext.getString(R.string.single_choice));
            holder.question.setText((position + 1) + ". "
                    + mDataItems.get(position).getQuestionName());

            holder.layoutA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'A');
                }
            });

            holder.layoutB.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'B');
                }
            });

            holder.layoutC.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'C');
                }
            });

            holder.layoutD.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'D');
                }
            });

            holder.layoutE.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onSingleChoiceClick(holder, position, 'E');
                }
            });
        }

        // 最后一页修改"下一步"按钮文字
        if (position == mViewItems.size() - 1) {
            holder.nextText.setText(R.string.commit);
            holder.nextImage.setImageResource(R.drawable.vote_submit_finish);
        }

        holder.previousBtn.setOnClickListener(new LinearOnClickListener(position - 1, false, position, holder));
        holder.nextBtn.setOnClickListener(new LinearOnClickListener(position + 1, true, position, holder));
        container.addView(mViewItems.get(position));
        return mViewItems.get(position);
    }

    /**
     * @author 设置上一步和下一步按钮监听
     */
    private class LinearOnClickListener implements OnClickListener {

        private int mPosition;
        private int mPosition1;
        private boolean mIsNext;
        private ViewHolder mViewHolder;

        public LinearOnClickListener(int position, boolean isNext, int position1, ViewHolder viewHolder) {
            mPosition = position;
            mPosition1 = position1;
            mViewHolder = viewHolder;
            mIsNext = isNext;
        }

        @Override
        public void onClick(View v) {

            if (mPosition == mViewItems.size()) {
                //单选
                if (mDataItems.get(mPosition1).getQuestionType().equals("0")) {
                    if (!mMap.containsKey(mPosition1)) {
                        Toast.makeText(mContext, R.string.please_make_a_choice, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mContext.uploadExamination(mErrorTopicNum);
                } else if (mDataItems.get(mPosition1).getQuestionType().equals("1")) {
                    //判断多选时的点击
                    if (!mMap.containsKey(mPosition1)) {
                        if (!mMapClick.containsKey(mPosition1)) {
                            Toast.makeText(mContext, R.string.please_make_a_choice, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    mMap.put(mPosition1, true);

                    if (mMapMultiSelect.containsKey(mPosition1)) {
                        //提交答题
                        mContext.uploadExamination(mErrorTopicNum);
                    } else {
                        String ssStr = mDataItems.get(mPosition1).getCorrectAnswer();
                        ssStr = ssStr.replace("|", "");

                        if (mPosition == mViewItems.size()) {
                            if (mAnswerLast.toString().contains("A")) {
                                mAnswer1.append("A");
                            }
                            if (mAnswerLast.toString().contains("B")) {
                                mAnswer1.append("B");
                            }
                            if (mAnswerLast.toString().contains("C")) {
                                mAnswer1.append("C");
                            }
                            if (mAnswerLast.toString().contains("D")) {
                                mAnswer1.append("D");
                            }
                            if (mAnswerLast.toString().contains("E")) {
                                mAnswer1.append("E");
                            }
                        } else {
                            if (mAnswer.toString().contains("A")) {
                                mAnswer1.append("A");
                            }
                            if (mAnswer.toString().contains("B")) {
                                mAnswer1.append("B");
                            }
                            if (mAnswer.toString().contains("C")) {
                                mAnswer1.append("C");
                            }
                            if (mAnswer.toString().contains("D")) {
                                mAnswer1.append("D");
                            }
                            if (mAnswer.toString().contains("E")) {
                                mAnswer1.append("E");
                            }
                        }

                        if (mAnswer1.toString().equals(ssStr)) {
                            //清除答案
                            mAnswer.delete(0, mAnswer.length());
                            mAnswer1.delete(0, mAnswer1.length());
                            mIsCorrect = ConstantUtil.isCorrect;
                            mMapMultiSelect.put(mPosition1, ConstantUtil.isCorrect);
                            //保存数据
                            SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                            questionInfo.setQuestionId(mDataItems.get(mPosition1).getQuestionId());
                            questionInfo.setQuestionType(mDataItems.get(mPosition1).getQuestionType());
                            questionInfo.setRealAnswer(mDataItems.get(mPosition1).getCorrectAnswer());
                            questionInfo.setScore(mDataItems.get(mPosition1).getScore());
                            questionInfo.setIs_correct(mIsCorrect);
                            mContext.mQuestionInfos.add(questionInfo);
                            mDataItems.get(mPosition1).setIsSelect("0");
                            //提交答题
                            mContext.uploadExamination(mErrorTopicNum);
                        } else {
                            //清除答案
                            mAnswer.delete(0, mAnswer.length());
                            mAnswer1.delete(0, mAnswer1.length());
                            mErrorTopicNum += 1;
                            mIsCorrect = ConstantUtil.isError;
                            //自动添加错误题目
                            ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                            errorQuestionInfo.setQuestionName(mDataItems.get(mPosition1).getQuestionName());
                            errorQuestionInfo.setQuestionType(mDataItems.get(mPosition1).getQuestionType());
                            errorQuestionInfo.setQuestionAnswer(mDataItems.get(mPosition1).getCorrectAnswer());
                            errorQuestionInfo.setIsRight(mIsCorrect);
                            errorQuestionInfo.setQuestionSelect(mAnswer.toString());
                            errorQuestionInfo.setAnalysis(mDataItems.get(mPosition1).getAnalysis());
                            errorQuestionInfo.setOptionType(mDataItems.get(mPosition1).getOption_type());
                            if (mDataItems.get(mPosition1).getOption_type().equals("0")) {
                                errorQuestionInfo.setOptionA(mDataItems.get(mPosition1).getOptionA());
                                errorQuestionInfo.setOptionB(mDataItems.get(mPosition1).getOptionB());
                                errorQuestionInfo.setOptionC(mDataItems.get(mPosition1).getOptionC());
                                errorQuestionInfo.setOptionD(mDataItems.get(mPosition1).getOptionD());
                                errorQuestionInfo.setOptionE(mDataItems.get(mPosition1).getOptionE());
                            } else {
                                errorQuestionInfo.setOptionA(mDataItems.get(mPosition1).getOptionA().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionA());
                                errorQuestionInfo.setOptionB(mDataItems.get(mPosition1).getOptionB().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionB());
                                errorQuestionInfo.setOptionC(mDataItems.get(mPosition1).getOptionC().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionC());
                                errorQuestionInfo.setOptionD(mDataItems.get(mPosition1).getOptionD().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionD());
                                errorQuestionInfo.setOptionE(mDataItems.get(mPosition1).getOptionE().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionE());
                            }

                            long column = mDbManager.insertErrorQuestion(errorQuestionInfo);
                            if (column == -1) {
                                Toast.makeText(mContext, R.string.add_failed, Toast.LENGTH_SHORT).show();
                            }
                            mIsCorrect = ConstantUtil.isError;
                            mMapMultiSelect.put(mPosition1, ConstantUtil.isError);

                            //提示
                            mViewHolder.wrongLayout.setVisibility(View.VISIBLE);
                            mViewHolder.explainDetailTv.setText("" + mDataItems.get(mPosition1).getAnalysis());
                            //保存数据
                            SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                            questionInfo.setQuestionId(mDataItems.get(mPosition1).getQuestionId());
                            questionInfo.setQuestionType(mDataItems.get(mPosition1).getQuestionType());
                            questionInfo.setRealAnswer(mDataItems.get(mPosition1).getCorrectAnswer());
                            questionInfo.setScore(mDataItems.get(mPosition1).getScore());
                            questionInfo.setIs_correct(mIsCorrect);
                            mContext.mQuestionInfos.add(questionInfo);
                            mDataItems.get(mPosition1).setIsSelect("0");
                            //显示正确选项
                            if (mDataItems.get(mPosition1).getCorrectAnswer().contains("A")) {
                                mViewHolder.ivA.setImageResource(R.drawable.ic_practice_test_right);
                                mViewHolder.tvA.setTextColor(Color.parseColor("#61bc31"));
                            }
                            if (mDataItems.get(mPosition1).getCorrectAnswer().contains("B")) {
                                mViewHolder.ivB.setImageResource(R.drawable.ic_practice_test_right);
                                mViewHolder.tvB.setTextColor(Color.parseColor("#61bc31"));
                            }
                            if (mDataItems.get(mPosition1).getCorrectAnswer().contains("C")) {
                                mViewHolder.ivC.setImageResource(R.drawable.ic_practice_test_right);
                                mViewHolder.tvC.setTextColor(Color.parseColor("#61bc31"));
                            }
                            if (mDataItems.get(mPosition1).getCorrectAnswer().contains("D")) {
                                mViewHolder.ivD.setImageResource(R.drawable.ic_practice_test_right);
                                mViewHolder.tvD.setTextColor(Color.parseColor("#61bc31"));
                            }
                            if (mDataItems.get(mPosition1).getCorrectAnswer().contains("E")) {
                                mViewHolder.ivE.setImageResource(R.drawable.ic_practice_test_right);
                                mViewHolder.tvE.setTextColor(Color.parseColor("#61bc31"));
                            }
                        }

                    }
                } else {
                    if (!mMap.containsKey(mPosition1)) {
                        Toast.makeText(mContext, R.string.please_make_a_choice, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mContext.uploadExamination(mErrorTopicNum);
                }
            } else {
                if (mPosition == -1) {
                    Toast.makeText(mContext, R.string.already_in_first_page, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //单选
                    if (mDataItems.get(mPosition1).getQuestionType().equals("0")) {
                        if (mIsNext) {
                            if (!mMap.containsKey(mPosition1)) {
                                Toast.makeText(mContext, R.string.please_make_a_choice, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        ExaminationSubmitAdapter.this.mIsNext = mIsNext;
                        mContext.setCurrentView(mPosition);
                    } else if (mDataItems.get(mPosition1).getQuestionType().equals("1")) {
                        if (mIsNext) {
                            //判断多选时的点击
                            if (!mMap.containsKey(mPosition1)) {
                                if (!mMapClick.containsKey(mPosition1)) {
                                    Toast.makeText(mContext, R.string.please_make_a_choice, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            mMap.put(mPosition1, true);

                            if (mMapMultiSelect.containsKey(mPosition1)) {
                                //清除答案
                                mAnswer.delete(0, mAnswer.length());
                                //选过的，直接跳转下一题
                                ExaminationSubmitAdapter.this.mIsNext = mIsNext;
                                mContext.setCurrentView(mPosition);
                            } else {
                                String ssStr = mDataItems.get(mPosition1).getCorrectAnswer();
                                ssStr = ssStr.replace("|", "");
                                if (mAnswer.toString().contains("A")) {
                                    mAnswer1.append("A");
                                }
                                if (mAnswer.toString().contains("B")) {
                                    mAnswer1.append("B");
                                }
                                if (mAnswer.toString().contains("C")) {
                                    mAnswer1.append("C");
                                }
                                if (mAnswer.toString().contains("D")) {
                                    mAnswer1.append("D");
                                }
                                if (mAnswer.toString().contains("E")) {
                                    mAnswer1.append("E");
                                }
                                if (mAnswer1.toString().equals(ssStr)) {
                                    //清除答案
                                    mAnswer.delete(0, mAnswer.length());
                                    mAnswer1.delete(0, mAnswer1.length());
                                    mIsCorrect = ConstantUtil.isCorrect;
                                    mMapMultiSelect.put(mPosition1, ConstantUtil.isCorrect);
                                    //保存数据
                                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                                    questionInfo.setQuestionId(mDataItems.get(mPosition1).getQuestionId());
                                    questionInfo.setQuestionType(mDataItems.get(mPosition1).getQuestionType());
                                    questionInfo.setRealAnswer(mDataItems.get(mPosition1).getCorrectAnswer());
                                    questionInfo.setScore(mDataItems.get(mPosition1).getScore());
                                    questionInfo.setIs_correct(mIsCorrect);
                                    mContext.mQuestionInfos.add(questionInfo);
                                    mDataItems.get(mPosition1).setIsSelect("0");
                                    ExaminationSubmitAdapter.this.mIsNext = mIsNext;
                                    mContext.setCurrentView(mPosition);
                                } else {
                                    //清除答案
                                    mAnswer.delete(0, mAnswer.length());
                                    mAnswer1.delete(0, mAnswer1.length());
                                    mErrorTopicNum += 1;
                                    mIsCorrect = ConstantUtil.isError;
                                    //自动添加错误题目
                                    ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                                    errorQuestionInfo.setQuestionName(mDataItems.get(mPosition1).getQuestionName());
                                    errorQuestionInfo.setQuestionType(mDataItems.get(mPosition1).getQuestionType());
                                    errorQuestionInfo.setQuestionAnswer(mDataItems.get(mPosition1).getCorrectAnswer());
                                    errorQuestionInfo.setIsRight(mIsCorrect);
                                    errorQuestionInfo.setQuestionSelect(mAnswer.toString());
                                    errorQuestionInfo.setAnalysis(mDataItems.get(mPosition1).getAnalysis());
                                    errorQuestionInfo.setOptionType(mDataItems.get(mPosition1).getOption_type());
                                    if (mDataItems.get(mPosition1).getOption_type().equals("0")) {
                                        errorQuestionInfo.setOptionA(mDataItems.get(mPosition1).getOptionA());
                                        errorQuestionInfo.setOptionB(mDataItems.get(mPosition1).getOptionB());
                                        errorQuestionInfo.setOptionC(mDataItems.get(mPosition1).getOptionC());
                                        errorQuestionInfo.setOptionD(mDataItems.get(mPosition1).getOptionD());
                                        errorQuestionInfo.setOptionE(mDataItems.get(mPosition1).getOptionE());
                                    } else {
                                        errorQuestionInfo.setOptionA(mDataItems.get(mPosition1).getOptionA().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionA());
                                        errorQuestionInfo.setOptionB(mDataItems.get(mPosition1).getOptionB().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionB());
                                        errorQuestionInfo.setOptionC(mDataItems.get(mPosition1).getOptionC().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionC());
                                        errorQuestionInfo.setOptionD(mDataItems.get(mPosition1).getOptionD().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionD());
                                        errorQuestionInfo.setOptionE(mDataItems.get(mPosition1).getOptionE().equals("") ? "" : mImgServerUrl + mDataItems.get(mPosition1).getOptionE());
                                    }
                                    long colunm = mDbManager.insertErrorQuestion(errorQuestionInfo);

                                    if (colunm == -1) {
                                        Toast.makeText(mContext, R.string.add_failed, Toast.LENGTH_SHORT).show();
                                    }
                                    mIsCorrect = ConstantUtil.isError;
                                    mMapMultiSelect.put(mPosition1, ConstantUtil.isError);

                                    //提示
                                    mViewHolder.wrongLayout.setVisibility(View.VISIBLE);
                                    mViewHolder.explainDetailTv.setText("" + mDataItems.get(mPosition1).getAnalysis());
                                    //保存数据
                                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                                    questionInfo.setQuestionId(mDataItems.get(mPosition1).getQuestionId());
                                    questionInfo.setQuestionType(mDataItems.get(mPosition1).getQuestionType());
                                    questionInfo.setRealAnswer(mDataItems.get(mPosition1).getCorrectAnswer());
                                    questionInfo.setScore(mDataItems.get(mPosition1).getScore());
                                    questionInfo.setIs_correct(mIsCorrect);
                                    mContext.mQuestionInfos.add(questionInfo);
                                    mDataItems.get(mPosition1).setIsSelect("0");
                                    //显示正确选项
                                    if (mDataItems.get(mPosition1).getCorrectAnswer().contains("A")) {
                                        mViewHolder.ivA.setImageResource(R.drawable.ic_practice_test_right);
                                        mViewHolder.tvA.setTextColor(Color.parseColor("#61bc31"));
                                    }
                                    if (mDataItems.get(mPosition1).getCorrectAnswer().contains("B")) {
                                        mViewHolder.ivB.setImageResource(R.drawable.ic_practice_test_right);
                                        mViewHolder.tvB.setTextColor(Color.parseColor("#61bc31"));
                                    }
                                    if (mDataItems.get(mPosition1).getCorrectAnswer().contains("C")) {
                                        mViewHolder.ivC.setImageResource(R.drawable.ic_practice_test_right);
                                        mViewHolder.tvC.setTextColor(Color.parseColor("#61bc31"));
                                    }
                                    if (mDataItems.get(mPosition1).getCorrectAnswer().contains("D")) {
                                        mViewHolder.ivD.setImageResource(R.drawable.ic_practice_test_right);
                                        mViewHolder.tvD.setTextColor(Color.parseColor("#61bc31"));
                                    }
                                    if (mDataItems.get(mPosition1).getCorrectAnswer().contains("E")) {
                                        mViewHolder.ivE.setImageResource(R.drawable.ic_practice_test_right);
                                        mViewHolder.tvE.setTextColor(Color.parseColor("#61bc31"));
                                    }
                                }
                            }
                        } else {
                            mContext.setCurrentView(mPosition);
                        }

                    } else {
                        if (mIsNext) {
                            if (!mMap.containsKey(mPosition1)) {
                                Toast.makeText(mContext, R.string.please_make_a_choice, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        ExaminationSubmitAdapter.this.mIsNext = mIsNext;
                        mContext.setCurrentView(mPosition);
                    }
                }
            }

        }

    }

    @Override
    public int getCount() {
        if (mViewItems == null)
            return 0;
        return mViewItems.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //错题数
    public int errorTopicNum() {
        if (mErrorTopicNum != 0) {
            return mErrorTopicNum;
        }
        return 0;
    }

    private class ViewHolder {
        public ImageView questionImg;
        public TextView questionType;
        public TextView question;
        public LinearLayout previousBtn, nextBtn, errorBtn;
        public TextView nextText;
        public TextView totalText;
        public ImageView nextImage;
        public LinearLayout wrongLayout;
        public TextView explainDetailTv;
        public LinearLayout layoutA;
        public LinearLayout layoutB;
        public LinearLayout layoutC;
        public LinearLayout layoutD;
        public LinearLayout layoutE;
        public ImageView ivA;
        public ImageView ivB;
        public ImageView ivC;
        public ImageView ivD;
        public ImageView ivE;
        public TextView tvA;
        public TextView tvB;
        public TextView tvC;
        public TextView tvD;
        public TextView tvE;
        public ImageView ivA_;
        public ImageView ivB_;
        public ImageView ivC_;
        public ImageView ivD_;
        public ImageView ivE_;
    }

    private void onSingleChoiceClick(final ViewHolder holder, final int position, final char choice) {
        if (null == holder) {
            return;
        }

        String choiceName = "";
        ImageView ivChoice = null;
        TextView tvChoice = null;
        switch (choice) {
        case 'A':
            choiceName = "A";
            ivChoice = holder.ivA;
            tvChoice = holder.tvA;
            break;

        case 'B':
            choiceName = "B";
            ivChoice = holder.ivB;
            tvChoice = holder.tvB;
            break;

        case 'C':
            choiceName = "C";
            ivChoice = holder.ivC;
            tvChoice = holder.tvC;
            break;

        case 'D':
            choiceName = "D";
            ivChoice = holder.ivD;
            tvChoice = holder.tvD;
            break;

        case 'E':
            choiceName = "E";
            ivChoice = holder.ivE;
            tvChoice = holder.tvE;
            break;

        default:
            return;
        }

        if (mMap.containsKey(position)) {
            return;
        }
        mMap.put(position, true);

        if (mDataItems.get(position).getCorrectAnswer().contains(choiceName)) {
            mContext.setCurrentView(position + 1);
            ivChoice.setImageResource(R.drawable.ic_practice_test_right);
            tvChoice.setTextColor(Color.parseColor("#61bc31"));
            mIsCorrect = ConstantUtil.isCorrect;
        } else {
            mIsCorrect = ConstantUtil.isError;
            mErrorTopicNum += 1;
            //自动添加错误题目
            ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
            errorQuestionInfo.setQuestionName(mDataItems.get(position).getQuestionName());
            errorQuestionInfo.setQuestionType(mDataItems.get(position).getQuestionType());
            errorQuestionInfo.setQuestionAnswer(mDataItems.get(position).getCorrectAnswer());
            errorQuestionInfo.setIsRight(mIsCorrect);
            errorQuestionInfo.setQuestionSelect(choiceName);
            errorQuestionInfo.setAnalysis(mDataItems.get(position).getAnalysis());
            errorQuestionInfo.setOptionType(mDataItems.get(position).getOption_type());
            if (mDataItems.get(position).getOption_type().equals("0")) {
                errorQuestionInfo.setOptionA(mDataItems.get(position).getOptionA());
                errorQuestionInfo.setOptionB(mDataItems.get(position).getOptionB());
                errorQuestionInfo.setOptionC(mDataItems.get(position).getOptionC());
                errorQuestionInfo.setOptionD(mDataItems.get(position).getOptionD());
                errorQuestionInfo.setOptionE(mDataItems.get(position).getOptionE());
            } else {
                errorQuestionInfo.setOptionA(mDataItems.get(position).getOptionA().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionA());
                errorQuestionInfo.setOptionB(mDataItems.get(position).getOptionB().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionB());
                errorQuestionInfo.setOptionC(mDataItems.get(position).getOptionC().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionC());
                errorQuestionInfo.setOptionD(mDataItems.get(position).getOptionD().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionD());
                errorQuestionInfo.setOptionE(mDataItems.get(position).getOptionE().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionE());
            }

            long column = mDbManager.insertErrorQuestion(errorQuestionInfo);
            if (column == -1) {
                Toast.makeText(mContext, R.string.add_failed, Toast.LENGTH_SHORT).show();
            }

            ivChoice.setImageResource(R.drawable.ic_practice_test_wrong);
            tvChoice.setTextColor(Color.parseColor("#d53235"));
            //提示
            holder.wrongLayout.setVisibility(View.VISIBLE);
            holder.explainDetailTv.setText("" + mDataItems.get(position).getAnalysis());
            //显示正确选项
            if (mDataItems.get(position).getCorrectAnswer().contains("A")) {
                holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (mDataItems.get(position).getCorrectAnswer().contains("B")) {
                holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (mDataItems.get(position).getCorrectAnswer().contains("C")) {
                holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (mDataItems.get(position).getCorrectAnswer().contains("D")) {
                holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvD.setTextColor(Color.parseColor("#61bc31"));
            } else if (mDataItems.get(position).getCorrectAnswer().contains("E")) {
                holder.ivE.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvE.setTextColor(Color.parseColor("#61bc31"));
            }
        }

        //保存数据
        SaveQuestionInfo questionInfo = new SaveQuestionInfo();
        questionInfo.setQuestionId(mDataItems.get(position).getQuestionId());
        questionInfo.setQuestionType(mDataItems.get(position).getQuestionType());
        questionInfo.setRealAnswer(mDataItems.get(position).getCorrectAnswer());
        questionInfo.setScore(mDataItems.get(position).getScore());
        questionInfo.setIs_correct(mIsCorrect);
        mContext.mQuestionInfos.add(questionInfo);
        mDataItems.get(position).setIsSelect("0");
    }

    private void onMultipleChoiceClick(final ViewHolder holder, final int position, final char choice) {
        if (null == holder) {
            return;
        }

        String choiceName = "";
        ImageView ivChoice = null;
        TextView tvChoice = null;

        switch (choice) {
        case 'A':
            choiceName = "A";
            ivChoice = holder.ivA;
            tvChoice = holder.tvA;
            break;

        case 'B':
            choiceName = "B";
            ivChoice = holder.ivB;
            tvChoice = holder.tvB;
            break;

        case 'C':
            choiceName = "C";
            ivChoice = holder.ivC;
            tvChoice = holder.tvC;
            break;

        case 'D':
            choiceName = "D";
            ivChoice = holder.ivD;
            tvChoice = holder.tvD;
            break;

        case 'E':
            choiceName = "E";
            ivChoice = holder.ivE;
            tvChoice = holder.tvE;
            break;

        default:
            return;
        }

        mMapClick.put(position, true);
        if (mMap.containsKey(position)) {
            return;
        }
        if (mDataItems.get(position).getCorrectAnswer().contains(choiceName)) {
            ivChoice.setImageResource(R.drawable.ic_practice_test_right);
            tvChoice.setTextColor(Color.parseColor("#61bc31"));
            mIsCorrect = ConstantUtil.isCorrect;
            if (position == mViewItems.size() - 1) {
                mAnswerLast.append(choiceName);
            } else {
                mAnswer.append(choiceName);
            }
        } else {
            mIsCorrect = ConstantUtil.isError;
            mMapMultiSelect.put(position, mIsCorrect);
            mErrorTopicNum += 1;
            //自动添加错误题目
            ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
            errorQuestionInfo.setQuestionName(mDataItems.get(position).getQuestionName());
            errorQuestionInfo.setQuestionType(mDataItems.get(position).getQuestionType());
            errorQuestionInfo.setQuestionAnswer(mDataItems.get(position).getCorrectAnswer());
            errorQuestionInfo.setIsRight(mIsCorrect);
            errorQuestionInfo.setQuestionSelect(choiceName);
            errorQuestionInfo.setAnalysis(mDataItems.get(position).getAnalysis());
            errorQuestionInfo.setOptionType(mDataItems.get(position).getOption_type());
            if (mDataItems.get(position).getOption_type().equals("0")) {
                errorQuestionInfo.setOptionA(mDataItems.get(position).getOptionA());
                errorQuestionInfo.setOptionB(mDataItems.get(position).getOptionB());
                errorQuestionInfo.setOptionC(mDataItems.get(position).getOptionC());
                errorQuestionInfo.setOptionD(mDataItems.get(position).getOptionD());
                errorQuestionInfo.setOptionE(mDataItems.get(position).getOptionE());
            } else {
                errorQuestionInfo.setOptionA(mDataItems.get(position).getOptionA().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionA());
                errorQuestionInfo.setOptionB(mDataItems.get(position).getOptionB().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionB());
                errorQuestionInfo.setOptionC(mDataItems.get(position).getOptionC().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionC());
                errorQuestionInfo.setOptionD(mDataItems.get(position).getOptionD().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionD());
                errorQuestionInfo.setOptionE(mDataItems.get(position).getOptionE().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionE());
            }

            long column = mDbManager.insertErrorQuestion(errorQuestionInfo);
            if (column == -1) {
                Toast.makeText(mContext, R.string.add_failed, Toast.LENGTH_SHORT).show();
            }

            mMap.put(position, true);
            ivChoice.setImageResource(R.drawable.ic_practice_test_wrong);
            tvChoice.setTextColor(Color.parseColor("#d53235"));
            //提示
            holder.wrongLayout.setVisibility(View.VISIBLE);
            holder.explainDetailTv.setText("" + mDataItems.get(position).getAnalysis());
            //显示正确选项
            if (mDataItems.get(position).getCorrectAnswer().contains("A")) {
                holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvA.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mDataItems.get(position).getCorrectAnswer().contains("B")) {
                holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvB.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mDataItems.get(position).getCorrectAnswer().contains("C")) {
                holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvC.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mDataItems.get(position).getCorrectAnswer().contains("D")) {
                holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvD.setTextColor(Color.parseColor("#61bc31"));
            }
            if (mDataItems.get(position).getCorrectAnswer().contains("E")) {
                holder.ivE.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvE.setTextColor(Color.parseColor("#61bc31"));
            }

            //保存数据
            SaveQuestionInfo questionInfo = new SaveQuestionInfo();
            questionInfo.setQuestionId(mDataItems.get(position).getQuestionId());
            questionInfo.setQuestionType(mDataItems.get(position).getQuestionType());
            questionInfo.setRealAnswer(mDataItems.get(position).getCorrectAnswer());
            questionInfo.setScore(mDataItems.get(position).getScore());
            questionInfo.setIs_correct(mIsCorrect);
            mContext.mQuestionInfos.add(questionInfo);
            mDataItems.get(position).setIsSelect("0");
        }
    }

    private void onJudgementClick(final ViewHolder holder, final int position, final char choice) {
        if (null == holder) {
            return;
        }

        String choiceName = "";
        ImageView ivChoice = null;
        TextView tvChoice = null;

        switch (choice) {
        case 'A':
            choiceName = "A";
            ivChoice = holder.ivA;
            tvChoice = holder.tvA;
            break;

        case 'B':
            choiceName = "B";
            ivChoice = holder.ivB;
            tvChoice = holder.tvB;
            break;

        case 'C':
            choiceName = "C";
            ivChoice = holder.ivC;
            tvChoice = holder.tvC;
            break;

        case 'D':
            choiceName = "D";
            ivChoice = holder.ivD;
            tvChoice = holder.tvD;
            break;

        case 'E':
            choiceName = "E";
            ivChoice = holder.ivE;
            tvChoice = holder.tvE;
            break;

        default:
            return;
        }
        if (mMap.containsKey(position)) {
            return;
        }
        mMap.put(position, true);
        if (mDataItems.get(position).getCorrectAnswer().contains(choiceName)) {
            mContext.setCurrentView(position + 1);
            ivChoice.setImageResource(R.drawable.ic_practice_test_right);
            tvChoice.setTextColor(Color.parseColor("#61bc31"));
            mIsCorrect = ConstantUtil.isCorrect;
        } else {
            mIsCorrect = ConstantUtil.isError;
            mErrorTopicNum += 1;
            //自动添加错误题目
            ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
            errorQuestionInfo.setQuestionName(mDataItems.get(position).getQuestionName());
            errorQuestionInfo.setQuestionType(mDataItems.get(position).getQuestionType());
            errorQuestionInfo.setQuestionAnswer(mDataItems.get(position).getCorrectAnswer());
            errorQuestionInfo.setIsRight(mIsCorrect);
            errorQuestionInfo.setQuestionSelect(choiceName);
            errorQuestionInfo.setAnalysis(mDataItems.get(position).getAnalysis());
            errorQuestionInfo.setOptionType(mDataItems.get(position).getOption_type());
            if (mDataItems.get(position).getOption_type().equals("0")) {
                errorQuestionInfo.setOptionA(mDataItems.get(position).getOptionA());
                errorQuestionInfo.setOptionB(mDataItems.get(position).getOptionB());
                errorQuestionInfo.setOptionC(mDataItems.get(position).getOptionC());
                errorQuestionInfo.setOptionD(mDataItems.get(position).getOptionD());
                errorQuestionInfo.setOptionE(mDataItems.get(position).getOptionE());
            } else {
                errorQuestionInfo.setOptionA(mDataItems.get(position).getOptionA().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionA());
                errorQuestionInfo.setOptionB(mDataItems.get(position).getOptionB().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionB());
                errorQuestionInfo.setOptionC(mDataItems.get(position).getOptionC().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionC());
                errorQuestionInfo.setOptionD(mDataItems.get(position).getOptionD().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionD());
                errorQuestionInfo.setOptionE(mDataItems.get(position).getOptionE().equals("") ? "" : mImgServerUrl + mDataItems.get(position).getOptionE());
            }

            long column = mDbManager.insertErrorQuestion(errorQuestionInfo);
            if (column == -1) {
                Toast.makeText(mContext, R.string.add_failed, Toast.LENGTH_SHORT).show();
            }

            ivChoice.setImageResource(R.drawable.ic_practice_test_wrong);
            tvChoice.setTextColor(Color.parseColor("#d53235"));
            //提示
            holder.wrongLayout.setVisibility(View.VISIBLE);
            holder.explainDetailTv.setText("" + mDataItems.get(position).getAnalysis());
            //显示正确选项
            if (mDataItems.get(position).getCorrectAnswer().contains("A")) {
                holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (mDataItems.get(position).getCorrectAnswer().contains("B")) {
                holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (mDataItems.get(position).getCorrectAnswer().contains("C")) {
                holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (mDataItems.get(position).getCorrectAnswer().contains("D")) {
                holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvD.setTextColor(Color.parseColor("#61bc31"));
            } else if (mDataItems.get(position).getCorrectAnswer().contains("E")) {
                holder.ivE.setImageResource(R.drawable.ic_practice_test_right);
                holder.tvE.setTextColor(Color.parseColor("#61bc31"));
            }
        }

        //保存数据
        SaveQuestionInfo questionInfo = new SaveQuestionInfo();
        questionInfo.setQuestionId(mDataItems.get(position).getQuestionId());
        questionInfo.setQuestionType(mDataItems.get(position).getQuestionType());
        questionInfo.setRealAnswer(mDataItems.get(position).getCorrectAnswer());
        questionInfo.setScore(mDataItems.get(position).getScore());
        questionInfo.setIs_correct(mIsCorrect);
        mContext.mQuestionInfos.add(questionInfo);
        mDataItems.get(position).setIsSelect("0");
    }

}
