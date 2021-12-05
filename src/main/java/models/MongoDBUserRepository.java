package models;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

public class MongoDBUserRepository implements UserRepository
{
    MongoCollection<User> userRepo;

    public MongoDBUserRepository(String mongoConnectionString)
    {
        var codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder()
                        .register(
                                ClassModel.builder(User.class).enableDiscriminator(true).build(),
                                ClassModel.builder(Question.class).enableDiscriminator(true).build()
                        ).automatic(true)
                        .build()));

        userRepo = MongoClients.create(mongoConnectionString).getDatabase("BotData")
                .withCodecRegistry(codecRegistry).getCollection("UserRepository", User.class);
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
    public User getOtherRandom(User requested)
    {
        var user = requested;

        while (user.getChatId() == requested.getChatId())
        {
            var result = userRepo.aggregate(List.of(Aggregates.sample(1)));
            var iter = result.iterator();
            user = iter.next();
        }

        return user;
    }

    @Override
    public void put(User user)
    {
        userRepo.updateOne(Filters.eq("chatId", user.getChatId()), new Document("$set", user),
                new UpdateOptions().upsert(true));
    }

    @Override
    public void remove(long chatId)
    {
        userRepo.deleteOne(Filters.eq("chatId", chatId));
    }


}

