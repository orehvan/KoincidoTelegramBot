package models;

public interface UserRepository
{
    public void initialize();

    public User getByChatId(long chatId);
    public User getByChatIdOrNew(long chatId);
    public User getRandom();

    public void put(User user);
}
