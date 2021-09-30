package models;

import java.util.List;

public class User
{
    private int chatId;
    private Constants.regStates regState;
    private String name;
    private String description;
    private List<Survey> surveys;

    public User()
    {
        this.regState = Constants.regStates.UNREGISTERED;
    }

    public int getChatId()
    {
        return chatId;
    }

    public void setChatId(int chatId)
    {
        this.chatId = chatId;
    }

    public Constants.regStates getRegState()
    {
        return regState;
    }

    public void setRegState(Constants.regStates state)
    {
        regState = state;
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
