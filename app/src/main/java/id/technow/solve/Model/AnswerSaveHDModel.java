package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerSaveHDModel {
    @SerializedName("question_id")
    @Expose
    private String question_id;

    @SerializedName("question")
    @Expose
    private String question;

    @SerializedName("pic_question")
    @Expose
    private String pic_question;

    @SerializedName("review")
    @Expose
    private String review;

    @SerializedName("trueAnswer")
    @Expose
    private String trueAnswer;

    @SerializedName("trueAnswerContent")
    @Expose
    private String trueAnswerContent;

    @SerializedName("trueAnswerPic")
    @Expose
    private String trueAnswerPic;

    @SerializedName("user_true")
    @Expose
    private String user_true;

    @SerializedName("user_answer")
    @Expose
    private String user_answer;

    @SerializedName("user_answer_content")
    @Expose
    private String user_answer_content;

    @SerializedName("user_answer_pic")
    @Expose
    private String user_answer_pic;

    public AnswerSaveHDModel(String question_id, String question, String pic_question, String review, String trueAnswer, String trueAnswerContent, String trueAnswerPic, String user_true, String user_answer, String user_answer_content, String user_answer_pic) {
        this.question_id = question_id;
        this.question = question;
        this.pic_question = pic_question;
        this.review = review;
        this.trueAnswer = trueAnswer;
        this.trueAnswerContent = trueAnswerContent;
        this.trueAnswerPic = trueAnswerPic;
        this.user_true = user_true;
        this.user_answer = user_answer;
        this.user_answer_content = user_answer_content;
        this.user_answer_pic = user_answer_pic;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuestion() {
        return question;
    }

    public String getPic_question() {
        return pic_question;
    }

    public String getReview() {
        return review;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public String getTrueAnswerContent() {
        return trueAnswerContent;
    }

    public String getTrueAnswerPic() {
        return trueAnswerPic;
    }

    public String getUser_true() {
        return user_true;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public String getUser_answer_content() {
        return user_answer_content;
    }

    public String getUser_answer_pic() {
        return user_answer_pic;
    }
}
