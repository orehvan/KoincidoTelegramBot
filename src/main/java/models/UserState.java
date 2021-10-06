package models;

public class UserState
{
    public enum regState
    { UNREGISTERED, NAME_REQUESTED, DESCRIPTION_REQUESTED, QUESTION_REQUESTED, REGISTERED }

    private regState regState;

    public UserState.regState getRegState()
    {
        return regState;
    }

    public void setRegState(UserState.regState regState)
    {
        this.regState = regState;
    }
}
