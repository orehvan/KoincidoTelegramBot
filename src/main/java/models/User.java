package models;

public class User
{
    public Question question;

    private String chatId;
    private UserState userState;
    private String name;
    private String description;

    public User()
    {
        this.userState = new UserState();
        this.userState.setRegState(UserState.regState.UNREGISTERED);
    }

    public String getChatId()
    {
        return chatId;
    }

    public void setChatId(String chatId)
    {
        this.chatId = chatId;
    }

    public UserState getUserState()
    {
        return userState;
    }

    public void setUserState(UserState.regState state)
    {
        userState.setRegState(state);
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
