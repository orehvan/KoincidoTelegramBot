package models;

import models.Constants.Enums.*;

public class User
{
    public Question question;

    private long chatId;
    private UserState userState;
    private String name;
    private String description;

    public User(long chatId)
    {
        this.chatId = chatId;
        this.question = new Question();
        this.userState = new UserState();
        this.userState.setRegState(RegState.UNREGISTERED);
        this.userState.setQuestState(QuestState.UNAVAILABLE);
    }

    public long getChatId()
    {
        return chatId;
    }

    public void setChatId(long chatId)
    {
        this.chatId = chatId;
    }

    public UserState getUserState()
    {
        return userState;
    }

    public void setLastQuestionChatId(long chatId)
    {
        this.userState.setLastQuestionChatId(chatId);
    }

    public void setUserQuestState(QuestState readyState)
    {
        this.userState.setQuestState(readyState);
    }

    public void setUserRegState(RegState regState)
    {
        this.userState.setRegState(regState);
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


}
