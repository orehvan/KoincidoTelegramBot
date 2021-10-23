package models;

import models.Constants.Enums.QuestState;
import models.Constants.Enums.RegState;

public class User
{
    public Question question;

    private long chatId;
    private UserState userState;
    private String name;
    private String description;
    private long lastQuestionChatId;
    private RegState regState;
    private QuestState questState;

    public User()
    {
    }

    public User(long chatId)
    {
        this.chatId = chatId;
        this.question = new Question();
        this.userState = new UserState();
        this.regState = RegState.UNREGISTERED;
        this.questState = QuestState.UNAVAILABLE;
    }

    public long getChatId()
    {
        return chatId;
    }

    public void setChatId(long chatId)
    {
        this.chatId = chatId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getLastQuestionChatId()
    {
        return lastQuestionChatId;
    }

    public void setLastQuestionChatId(long lastQuestionChatId)
    {
        this.lastQuestionChatId = lastQuestionChatId;
    }

    public RegState getRegState()
    {
        return regState;
    }

    public void setRegState(RegState regState)
    {
        this.regState = regState;
    }

    public QuestState getQuestState()
    {
        return questState;
    }

    public void setQuestState(QuestState questState)
    {
        this.questState = questState;
    }
}
