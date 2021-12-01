package app;

import models.MongoDBUserRepository;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main
{
    public static void main(String[] args)
    {
        var botName = System.getenv("TelegramBotName");
        var botToken = System.getenv("TelegramBotToken");
        var mongoConnectionString = System.getenv("MongoDBConnectionString");
        var userRepo = new MongoDBUserRepository(mongoConnectionString);

        var bot = new BotLogic(userRepo);
        var telegramWrapper = new TelegramApiWrapper(bot, botName, botToken);

        try
        {
            var botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramWrapper);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }
}