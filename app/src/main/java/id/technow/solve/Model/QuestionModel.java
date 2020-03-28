package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionModel {
    @SerializedName("id")
    @Expose
    private int id_soal;

    @SerializedName("question")
    @Expose
    private String question;

    @SerializedName("review")
    @Expose
    private String review;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("pic_question")
    @Expose
    private String pic_question;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("trueAnswer")
    @Expose
    private String trueAnswer;

    @SerializedName("trueAnswerContent")
    @Expose
    private String trueAnswerContent;

    @SerializedName("trueAnswerPic")
    @Expose
    private String trueAnswerPic;

    @SerializedName("user_answer")
    @Expose
    private String user_answer;

    @SerializedName("user_answer_content")
    @Expose
    private String user_answer_content;

    @SerializedName("option")
    @Expose
    private List<OptionModel> option = null;

    public QuestionModel(int id_soal, String question, String review, String type, String pic_question, String duration, String trueAnswer, String trueAnswerContent, String trueAnswerPic, String user_answer, String user_answer_content, List<OptionModel> option) {
        this.id_soal = id_soal;
        this.question = question;
        this.review = review;
        this.type = type;
        this.pic_question = pic_question;
        this.duration = duration;
        this.trueAnswer = trueAnswer;
        this.trueAnswerContent = trueAnswerContent;
        this.trueAnswerPic = trueAnswerPic;
        this.user_answer = user_answer;
        this.user_answer_content = user_answer_content;
        this.option = option;
    }

    public int getId_soal() {
        return id_soal;
    }

    public String getQuestion() {
        return question;
    }

    public String getReview() {
        return review;
    }

    public String getType() {
        return type;
    }

    public String getPic_question() {
        return pic_question;
    }

    public String getDuration() {
        return duration;
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

    public String getUser_answer() {
        return user_answer;
    }

    public String getUser_answer_content() {
        return user_answer_content;
    }

    public List<OptionModel> getOption() {
        return option;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public void setUser_answer_content(String user_answer_content) {
        this.user_answer_content = user_answer_content;
    }
}
