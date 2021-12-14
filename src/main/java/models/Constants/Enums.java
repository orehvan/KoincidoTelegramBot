package models.Constants;

public class Enums
{
    public enum ChatState
    {
        UNREGISTERED,
        NAME_REQUESTED,
        DESCRIPTION_REQUESTED,
        QUESTION_REQUESTED,
        REGISTERED
    }

    public enum QuestState
    {
        UNAVAILABLE,
        IDLE,
        ANSWER_REQUESTED
    }
}
