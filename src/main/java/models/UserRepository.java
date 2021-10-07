package models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserRepository implements Map<Long, User>
{
    private HashMap<Long, User> userRepo = new HashMap<>();

    @Override
    public int size()
    {
        return userRepo.size();
    }

    @Override
    public boolean isEmpty()
    {
        return userRepo.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return userRepo.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return userRepo.containsValue(value);
    }

    @Override
    public User get(Object key)
    {
        return userRepo.get(key);
    }

    @Override
    public User put(Long key, User value)
    {
        return userRepo.put(key, value);
    }

    @Override
    public User remove(Object key)
    {
        return userRepo.remove(key);
    }

    @Override
    public void putAll(Map<? extends Long, ? extends User> m)
    {
        userRepo.putAll(m);
    }

    @Override
    public void clear()
    {
        userRepo.clear();
    }

    @Override
    public Set<Long> keySet()
    {
        return userRepo.keySet();
    }

    @Override
    public Collection<User> values()
    {
        return userRepo.values();
    }

    @Override
    public Set<Entry<Long, User>> entrySet()
    {
        return userRepo.entrySet();
    }
}
