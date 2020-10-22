package com.puo.fireman_app;

import java.util.ArrayList;
import java.util.List;

public class Question {

  private String question;
  private int answerIndex, questionImage = 0;
  private List<Object> answers;

  public Question(String question, int answerIndex, List<Object> answers) {
    this.question = question;
    this.answerIndex = answerIndex;
    this.answers = new ArrayList<>(answers);
  }

  public Question(String question, int answerIndex, List<Object> answers, int questionImage) {
    this.question = question;
    this.answerIndex = answerIndex;
    this.answers = new ArrayList<>(answers);
    this.questionImage = questionImage;
  }

  public int getQuestionImage() {
    return questionImage;
  }

  public String getQuestion() {
    return question;
  }

  public int getAnswerIndex() {
    return answerIndex;
  }

  public List<Object> getAnswers() {
    return answers;
  }
}
