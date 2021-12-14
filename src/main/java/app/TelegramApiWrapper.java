package app;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TelegramApiWrapper extends TelegramLongPollingBot
{
    private final BotLogic bot;
    private final String telegramBotName;
    private final String telegramBotToken;

    public TelegramApiWrapper(BotLogic bot, String telegramBotName, String telegramBotToken)
    {
        this.telegramBotName = telegramBotName;
        this.telegramBotToken = telegramBotToken;
        this.bot = bot;
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        try
        {
            long currentChatId;
            BotResponse response;


            if (update.hasMessage())
            {
                var message = update.getMessage();
                currentChatId = message.getChatId();
                var currentUsername = message.getFrom().getUserName();

                response = bot.respondToMessage(currentChatId, message.getText());
                bot.updateUsername(currentChatId, currentUsername);
            }
            else if (update.hasCallbackQuery())
            {
                var callbackQuery = update.getCallbackQuery();
                currentChatId = callbackQuery.getMessage().getChatId();
                response = bot.respondToCallback(currentChatId, callbackQuery.getData());
            }
            else
            {
                throw new Exception("No data found");
            }

            InlineKeyboardMarkup inlineKeyboard = null;
            if (response.getKeyboardMarkup() != null)
            {
                inlineKeyboard = new InlineKeyboardMarkup();
                inlineKeyboard.setKeyboard(setInlineKeyboardMarkup(response.getKeyboardMarkup()));
            }

            sendResponse(currentChatId, response.getMessageText(), inlineKeyboard);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendResponse(Long chatId, String msgText,
                              InlineKeyboardMarkup inlineKeyboard)
    {
        var sender = new SendMessage();
        sender.setChatId(chatId.toString());
        sender.setText(msgText);

        if (inlineKeyboard != null)
        {
            sender.setReplyMarkup(inlineKeyboard);
        }

        try
        {
            execute(sender);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    private List<List<InlineKeyboardButton>> setInlineKeyboardMarkup(LinkedList<KeyboardButton> keyboardButtons)
    {
        var keyboardArray = new ArrayList<List<InlineKeyboardButton>>();

        for (var button :
                keyboardButtons)
        {
            var row = new ArrayList<InlineKeyboardButton>();
            var inlineKeyboardButton = new InlineKeyboardButton();

            inlineKeyboardButton.setText(button.getText());
            inlineKeyboardButton.setCallbackData(button.getCallbackData());
            inlineKeyboardButton.setUrl(button.getUrl());

            row.add(inlineKeyboardButton);
            keyboardArray.add(row);
        }

        return keyboardArray;
    }

    @Override
    public String getBotUsername()
    {
        return telegramBotName;
    }

    @Override
    public String getBotToken()
    {
        return telegramBotToken;
    }
}
