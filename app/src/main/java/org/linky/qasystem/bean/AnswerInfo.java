package org.linky.qasystem.bean;

public class AnswerInfo {

    private String questionId; // 试题主键
    private String questionImg; //试题图片
    private String questionName; // 试题题目
    private String QuestionFor; // （0模拟试题，1竞赛试题）
    private String QuestionType; // 试题类型
    private String analysis; // 试题分析
    private String correctAnswer; // 正确答案
    private String optionA; // 正确答案A
    private String optionB; // 正确答案B
    private String optionC; // 正确答案C
    private String optionD; // 正确答案D
    private String optionE; // 正确答案E
    private String score; // 分值
    private String option_type; // 是否是图片题0是1否
    private String isSelect; // 是否选择0是1否

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setQuestionImg(String questionImg) {
        this.questionImg = questionImg;
    }

    public String getQuestionImg() {
        return questionImg;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestionFor() {
        return QuestionFor;
    }

    public void setQuestionFor(String questionFor) {
        QuestionFor = questionFor;
    }

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setOption_type(String option_type) {
        this.option_type = option_type;
    }


}
