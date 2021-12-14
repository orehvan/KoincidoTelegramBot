package models;

import models.Constants.Enums.ChatState;
import models.Constants.Enums.QuestState;

public class User
{
    public Question question;

    private long chatId;
    private String username;
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

    public String printProfileWithAnswer(String answer)
    {
        return String.format("Имя: %s\r\n" +
                            "Описание: %s\r\n" +
                            "Ответ на твой вопрос: \"%s\"", name, description, answer);
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
