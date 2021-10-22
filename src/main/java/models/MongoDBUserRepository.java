package models;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDBUserRepository implements UserRepository
{
    MongoCollection<User> userRepo;

    @Override
    public void initialize()
    {
        try (var mongoClient = MongoClients.create(System.getenv("MongoDBConnectionString")))
        {
            var database = mongoClient.getDatabase("BotData");
            var codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder()
                            .register(ClassModel.builder(User.class).enableDiscriminator(true).build()).automatic(true)
                            .build()));

            userRepo = database.withCodecRegistry(codecRegistry).getCollection("UserRepository", User.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public User getByChatId(long chatId)
    {
        return userRepo.find(Filters.eq("chatId", chatId)).first();
    }

    @Override
    public User getByChatIdOrNew(long chatId)
    {
        var user = userRepo.find(Filters.eq("chatId", chatId)).first();

        return user == null ? new User(chatId) : user;
    }

    @Override
    public User getRandom()
    {
        return null;
    }

    @Override
    public void put(User user)
    {
        userRepo.replaceOne(Filters.eq("chatId", user.getChatId()), user);
    }
}

