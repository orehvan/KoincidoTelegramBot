package models;

import models.Constants.Enums.*;

public class UserState
{
    private long lastQuestionChatId;
    private RegState regState;
    private QuestState questState;

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
