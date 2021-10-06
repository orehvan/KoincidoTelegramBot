package models;

import java.util.HashMap;

public class Question
{
    private String question;
    private HashMap<String, String> answers;

    public Question(String text)
    {
        this.question = text;
        this.answers = new HashMap<>();
    }

    public void addAnswer(String chatId, String answer)
    {
        this.answers.put(chatId, answer);
    }

    public String getAnswerByChatId(String chatId)
    {
        return this.answers.getOrDefault(chatId, "Missing ChatId");
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }
}
