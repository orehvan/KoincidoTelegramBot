package models;

import models.Enums.regState;

public class User
{
    public Question question;

    private long chatId;
    private UserState userState;
    private String name;
    private String description;

    public User()
    {
        this.question = new Question();
        this.userState = new UserState();
        this.userState.setRegState(regState.UNREGISTERED);
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

    public void setUserRegState(regState regState)
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
