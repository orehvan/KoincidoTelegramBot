package app;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import models.Constants;

public class TelegramApiWrapper extends TelegramLongPollingBot
{
    private BotLogic bot;

    public TelegramApiWrapper(BotLogic bot)
    {
        this.bot = bot;
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        try
        {
            if(!update.hasMessage())
                return;

            var message = update.getMessage();
            var currentChatId = message.getChatId().toString();
            var response = bot.formResponse(currentChatId, message.getText());
            sendResponse(currentChatId, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendResponse(String chatId, String msgText)
    {
        var sender = new SendMessage();
        sender.setChatId(chatId);
        sender.setText(msgText);

        try
        {
            execute(sender);
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername()
    {
        return Constants.getBOT_NAME();
    }

    @Override
    public String getBotToken()
    {
        return Constants.getBOT_TOKEN();
    }
}
