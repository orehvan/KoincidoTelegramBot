package models;

public class Question
{
    private String text;
    private Boolean answer;

    public Question(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public void setAnswer(Boolean answer)
    {
        this.answer = answer;
    }

    public Boolean getAnswer()
    {
        return answer;
    }
}
