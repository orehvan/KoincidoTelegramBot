package models;

import java.util.List;

public class User
{
    private int chatId;
    private String name;
    private String description;
    private List<Survey> surveys;

    public User(int chatId, String name, String description)
    {
        this.chatId = chatId;
        this.name = name;
        this.description = description;
    }

    public int getChatId()
    {
        return chatId;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }
}
