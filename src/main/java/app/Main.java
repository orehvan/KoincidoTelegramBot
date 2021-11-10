package app;

import models.MongoDBUserRepository;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main
{
    public static void main(String[] args)
    {
        var userRepo = new MongoDBUserRepository();

        var bot = new BotLogic(userRepo);
        var telegramWrapper = new TelegramApiWrapper(bot);

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