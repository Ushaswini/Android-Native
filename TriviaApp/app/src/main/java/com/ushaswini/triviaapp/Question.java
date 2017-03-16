package com.ushaswini.triviaapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ushas on 08/02/2017.
 */

public class Question implements Parcelable{
    String urlToImage,questionText;
    ArrayList<String> choices = new ArrayList<String>();
    int answerIndex, userAnswerIndex,id;
    boolean isCorrectAnswer = false;

    public Question() {
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public int getUserAnswerIndex() {
        return userAnswerIndex;
    }

    public void setUserAnswerIndex(int userAnswerIndex) {
        this.userAnswerIndex = userAnswerIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionText);
        dest.writeString(urlToImage);
        dest.writeInt(answerIndex);
        dest.writeInt(userAnswerIndex);
        dest.writeInt(id);
        dest.writeByte((byte) (isCorrectAnswer ? 1 : 0));
        dest.writeSerializable(choices);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
    private Question(Parcel in){
        this.questionText = in.readString();
        this.urlToImage = in.readString();
        this.answerIndex = in.readInt();
        this.userAnswerIndex = in.readInt();
        this.id = in.readInt();
        this.isCorrectAnswer= in.readByte() != 0;
        this.choices = (ArrayList<String>) in.readSerializable();
    }
    }
