package models;

import java.util.HashMap;

public class Question
{
    private String questionText;
    private HashMap<String, String> answers;

    public Question()
    {
        this.answers = new HashMap<>();
    }

    public HashMap<String, String> getAnswers()
    {
        return answers;
    }

    public void setAnswers(HashMap<String, String> answers)
    {
        this.answers = answers;
    }

    public void addAnswer(String username, String answer)
    {
        this.answers.put(username, answer);
    }

    public String getAnswerByUsername(String username)
    {
        return this.answers.getOrDefault(username, "Missing Username");
    }

    public String getQuestionText()
    {
        return questionText;
    }

    public void setQuestionText(String questionText)
    {
        this.questionText = questionText;
    }
}
