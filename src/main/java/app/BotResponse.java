package app;

import java.util.ArrayList;
import java.util.LinkedList;

public class BotResponse
{
    private String messageText;
    private LinkedList<KeyboardButton> keyboardMarkup;

    public BotResponse(String messageText, LinkedList<KeyboardButton> keyboardMarkup)
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

    public LinkedList<KeyboardButton> getKeyboardMarkup()
    {
        return keyboardMarkup;
    }

    public void setKeyboardMarkup(LinkedList<KeyboardButton> keyboardMarkup)
    {
        this.keyboardMarkup = keyboardMarkup;
    }
}
