package models;

import models.Constants.Enums.*;

public class UserState
{
    private long lastQuestionChatId;
    private ChatState chatState;
    private QuestState questState;

    public long getLastQuestionChatId()
    {
        return lastQuestionChatId;
    }

    public void setLastQuestionChatId(long lastQuestionChatId)
    {
        this.lastQuestionChatId = lastQuestionChatId;
    }

    public ChatState getRegState()
    {
        return chatState;
    }

    public void setRegState(ChatState chatState)
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
}
