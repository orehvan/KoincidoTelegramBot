package models;

public interface UserRepository
{
    User getByChatId(long chatId);
    User getByChatIdOrNew(long chatId);
    User getOtherRandom(User requested);

    void put(User user);
}
