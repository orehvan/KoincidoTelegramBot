package app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        String connectionString = System.getProperty("mongodb.uri");
        try(MongoClient mongoClient = MongoClients.create(connectionString))
        {
            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            databases.forEach(db -> System.out.println(db.toJson()));
        }
        catch (Exception connection)
        {
            connection.printStackTrace();
        }

        var bot = new BotLogic();
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