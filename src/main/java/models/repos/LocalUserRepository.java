package models.repos;

import models.User;

import java.util.HashMap;
import java.util.Random;

public class LocalUserRepository implements UserRepository
{
    private HashMap<Long, User> userRepo;

    public LocalUserRepository()
    {
        userRepo = new HashMap<>();
    }

    public User getByChatId(long chatId)
    {
        return userRepo.get(chatId);
    }

    public User getByChatIdOrNew(long chatId)
    {
        return userRepo.getOrDefault(chatId, new User(chatId));
    }

    @Override
    public User getByUsername(String username)
    {
        return null;
    }

    public User getOtherRandom(User requested)
    {
        var generator = new Random();
        var users = userRepo.values().toArray();

        return (User) users[generator.nextInt(users.length)];
    }

    @Override
    public long count()
    {
        return userRepo.size();
    }

    public void put(User user)
    {
        this.userRepo.put(user.getChatId(), user);
    }

    @Override
    public void remove(long chatId)
    {
        this.userRepo.remove(chatId);
    }
}
