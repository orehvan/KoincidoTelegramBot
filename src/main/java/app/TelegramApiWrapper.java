package app;

import models.Constants.Enums.UpdateType;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
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

                response = bot.formResponse(UpdateType.MESSAGE, currentChatId, message.getText());
                bot.updateUsername(currentChatId, currentUsername);
            }
            else if (update.hasCallbackQuery())
            {
                var callbackQuery = update.getCallbackQuery();
                currentChatId = callbackQuery.getMessage().getChatId();
                response = bot.formResponse(UpdateType.CALLBACK, currentChatId, callbackQuery.getData());
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

    private List<List<InlineKeyboardButton>> setInlineKeyboardMarkup(HashMap<Long, String> keys)
    {
        var keyboardArray = new ArrayList<List<InlineKeyboardButton>>();

        if (keys.containsKey(0L))
        {
            var row = new ArrayList<InlineKeyboardButton>();
            var button = new InlineKeyboardButton();

            button.setText("Открыть профиль пользователя в Telegram");
            button.setUrl(keys.get(0L));

            row.add(button);
            keyboardArray.add(row);
        }
        else
        {
            for (var key :
                    keys.keySet())
            {
                var row = new ArrayList<InlineKeyboardButton>();
                var button = new InlineKeyboardButton();

                button.setText(keys.get(key));
                button.setCallbackData(key.toString());

                row.add(button);
                keyboardArray.add(row);
            }

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
