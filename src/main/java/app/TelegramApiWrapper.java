package app;

import models.Constants.Emojis;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class TelegramApiWrapper extends TelegramLongPollingBot
{
    private final BotLogic bot;
    private final ReplyKeyboardMarkup replyKeyboardMarkup;

    public TelegramApiWrapper(BotLogic bot)
    {
        this.bot = bot;
        this.replyKeyboardMarkup = new ReplyKeyboardMarkup();
        setKeyboardMarkup();
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        try
        {
            if (!update.hasMessage())
            {
                return;
            }

            var message = update.getMessage();
            var currentChatId = message.getChatId();
            var response = bot.formResponse(currentChatId, message.getText());
            sendResponse(currentChatId, response);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendResponse(Long chatId, String msgText)
    {
        var sender = new SendMessage();
        sender.setChatId(chatId.toString());
        sender.setText(msgText);
        sender.setReplyMarkup(replyKeyboardMarkup);

        try
        {
            execute(sender);
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    private void setKeyboardMarkup()
    {
        var keyboard = new ArrayList<KeyboardRow>();
        var keyboardRow = new KeyboardRow();

        keyboardRow.add(String.format("Регистрация %s", Emojis.MEMO));
        keyboardRow.add(String.format("Следующий вопрос %s", Emojis.ARROW_RIGHT));
        keyboardRow.add(String.format("Список ответов %s", Emojis.SPEECH_BUBBLE));
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
    }

    @Override
    public String getBotUsername()
    {
        return System.getenv("TelegramBotName");
    }

    @Override
    public String getBotToken()
    {
        return System.getenv("TelegramBotToken");
    }
}
