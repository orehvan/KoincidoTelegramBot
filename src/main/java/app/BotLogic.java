package app;

import models.Constants;
import models.User;

import java.util.Map;

public class BotLogic
{

    private Map<String, User> usersRepo;

    public String formResponse(String chatId, String text)
    {

        if ((!usersRepo.containsKey(chatId) && text == "/start") ||
                usersRepo.get(chatId).getRegState() != Constants.regStates.TEST_COMPLETE)
        {
            return registerNewUser(chatId, text);
        }

        return "Smth went wrong";
    }

    public String registerNewUser(String chatId, String text)
    {
        var user = usersRepo.getOrDefault(chatId, new User());
        switch (user.getRegState())
        {
            case UNREGISTERED -> {
                usersRepo.put(chatId, user);
                user.setRegState(Constants.regStates.NAME_REQUESTED);
                return "Приветствую! Я - бот для знакомств по интересам. \r\n" +
                        "Давайте знакомиться! Как к Вам можно обращаться?";
            }
            case NAME_REQUESTED -> {
                user.setName(text);
                user.setRegState(Constants.regStates.DESCRIPTION_REQUESTED);
                return String.format("Очень рад, %name!", user.getName()) +
                        "\r\n Расскажите что-нибудь о себе, буквально пару слов.";
            }
            case DESCRIPTION_REQUESTED -> {
                user.setDescription(text);
                user.setRegState(Constants.regStates.TEST_STARTED);
                return String.format("Вы явно интересная личность, %name!", user.getName() +
                        "\r\n Чтобы точнее подобрать собеседников для Вас, предлагаю пройти короткий тест.");
            }
            case TEST_COMPLETE -> {

            }
        }
        return "Something went wrong during the registration.";
    }
}