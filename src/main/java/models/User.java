package models;

import models.Constants.Enums.QuestState;
import models.Constants.Enums.ChatState;

public class User
{
    public Question question;

    private long chatId;
    private String name;
    private String description;
    private long lastQuestionChatId;
    private ChatState chatState;
    private QuestState questState;

    public User()
    {
    }

    public User(long chatId)
    {
        this.chatId = chatId;
        this.question = new Question();
        this.chatState = ChatState.UNREGISTERED;
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

    public ChatState getChatState()
    {
        return chatState;
    }

    public void setChatState(ChatState chatState)
    {
        this.chatState = chatState;
    }

    public QuestState getQuestState()
    {
        return questState;
    }

    public void setQuestState(QuestState questState)
    {
        this.questState = questState;
    }

    public String printUserProfile()
    {
        return name + "\r\n" + description;
    }
}
