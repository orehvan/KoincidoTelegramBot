package app;

import models.User;
import models.UserRepository;
import models.UserState;

public class BotLogic
{

    private UserRepository userRepo = new UserRepository();

    public String formResponse(String chatId, String text)
    {

        if (!userRepo.containsChatId(chatId) || userRepo.getUserByChatId(chatId).getUserState().getRegState() != UserState.regState.REGISTERED)
        {
            return registerNewUser(chatId, text);
        }

        return "Smth went wrong";
    }

    public String registerNewUser(String chatId, String text)
    {
        User user;
        if (userRepo.containsChatId(chatId))
        {
            user = userRepo.getUserByChatId(chatId);
        }
        else {
            user = new User();
        }
        switch (user.getUserState().getRegState())
        {
            case UNREGISTERED -> {
                userRepo.addUser(user);
                user.setUserState(UserState.regState.NAME_REQUESTED);
                return "Приветствую! Я - бот для знакомств по интересам. \r\n" +
                        "Давайте знакомиться! Как к Вам можно обращаться?";
            }
            case NAME_REQUESTED -> {
                user.setName(text);
                user.setUserState(UserState.regState.DESCRIPTION_REQUESTED);
                return String.format("Очень рад, %name!", user.getName()) +
                        "\r\n Расскажите что-нибудь о себе, буквально пару слов.";
            }
            case DESCRIPTION_REQUESTED -> {
                user.setDescription(text);
                user.setUserState(UserState.regState.QUESTION_REQUESTED);
                return String.format("Вы явно интересная личность, %name!", user.getName()) +
                        "\r\n Чтобы точнее подобрать собеседников для Вас, предлагаю пройти короткий тест.";
            }
        }
        return "Something went wrong during the registration.";
    }
}