package models.repos;

import models.User;

public interface UserRepository
{
    User getByChatId(long chatId);
    User getByChatIdOrNew(long chatId);
    User getByUsername(String username);
    User getOtherRandom(User requested);

    long count();

    void put(User user);
    void remove(long chatId);
}
