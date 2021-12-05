package app;

import java.util.HashMap;

public class BotResponse
{
    private String messageText;
    private HashMap<Long, String> keyboardMarkup;

    public BotResponse(String messageText, HashMap<Long, String> keyboardMarkup)
    {
        this.messageText = messageText;
        this.keyboardMarkup = keyboardMarkup;
    }


    public String getMessageText()
    {
        return messageText;
    }

    public void setMessageText(String messageText)
    {
        this.messageText = messageText;
    }

    public HashMap<Long, String> getKeyboardMarkup()
    {
        return keyboardMarkup;
    }

    public void setKeyboardMarkup(HashMap<Long, String> keyboardMarkup)
    {
        this.keyboardMarkup = keyboardMarkup;
    }
}
