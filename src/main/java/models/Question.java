package models;

import java.util.HashMap;

public class Question
{
    private String questionText;
    private HashMap<Long, String> answers;

    public Question()
    {
        this.answers = new HashMap<>();
    }

    public void addAnswer(long chatId, String answer)
    {
        this.answers.put(chatId, answer);
    }

    public String getAnswerByChatId(String chatId)
    {
        return this.answers.getOrDefault(chatId, "Missing ChatId");
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
