package org.linky.qasystem.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ErrorQuestion implements Parcelable {

    private int questionId;
    private String questionImg;
    private String questionName;
    private String questionType;
    private String questionAnswer;
    private String questionSelect;
    private String isRight;
    private String Analysis;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String optionType;

    public static final Parcelable.Creator<ErrorQuestion> CREATOR = new Parcelable.Creator<ErrorQuestion>() {
        public ErrorQuestion createFromParcel(Parcel in) {
            return new ErrorQuestion(in);
        }

        public ErrorQuestion[] newArray(int size) {
            return new ErrorQuestion[size];
        }
    };

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(String questionImg) {
        this.questionImg = questionImg;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getQuestionSelect() {
        return questionSelect;
    }

    public void setQuestionSelect(String questionSelect) {
        this.questionSelect = questionSelect;
    }

    public String getIsRight() {
        return isRight;
    }

    public void setIsRight(String isRight) {
        this.isRight = isRight;
    }

    public String getAnalysis() {
        return Analysis;
    }

    public void setAnalysis(String analysis) {
        Analysis = analysis;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeString(questionName);
        dest.writeString(questionType);
        dest.writeString(questionAnswer);
        dest.writeString(questionSelect);
        dest.writeString(isRight);
        dest.writeString(Analysis);
        dest.writeString(optionA);
        dest.writeString(optionB);
        dest.writeString(optionC);
        dest.writeString(optionD);
        dest.writeString(optionE);
        dest.writeString(optionType);
    }

    public ErrorQuestion() {
    }

    private ErrorQuestion(Parcel in) {
        questionId = in.readInt();
        questionImg = in.readString();
        questionName = in.readString();
        questionType = in.readString();
        questionAnswer = in.readString();
        questionSelect = in.readString();
        isRight = in.readString();
        Analysis = in.readString();
        optionA = in.readString();
        optionB = in.readString();
        optionC = in.readString();
        optionD = in.readString();
        optionE = in.readString();
        optionType = in.readString();
    }

}