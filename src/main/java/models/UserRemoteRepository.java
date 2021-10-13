package models;

import app.DataBaseManager;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import java.util.ArrayList;

public class UserRemoteRepository
{
    public void pullData()
    {
        try (var mongoClient = MongoClients.create(System.getenv("MongoDBConnectionString")))
        {
            var dataBase = mongoClient.listDatabases().into(new ArrayList<>());
        }

    }
}

