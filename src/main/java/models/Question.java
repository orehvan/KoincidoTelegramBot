package models;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

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

    public void addAnswer(long chatId, String answer)
    {
        this.answers.put(Objects.toString(chatId), answer);
    }

    public Collection<String> getAllRespondentsChatIds()
    {
        return answers.keySet();
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
