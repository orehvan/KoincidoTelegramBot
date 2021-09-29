package app;

import models.User;
import java.util.List;

public class BotLogic
{
    private List<User> users;

    public String formResponse(String text)
    {
        if (text.equals("/start"))
        {
            return "Привет! Я бот для знакомств по интересам.";
        }

        return "";
    }

}