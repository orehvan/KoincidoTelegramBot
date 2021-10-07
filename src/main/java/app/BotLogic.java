package app;

import models.Enums.regState;
import models.User;
import models.UserRepository;

public class BotLogic
{

    private final UserRepository userRepo = new UserRepository();

    public String formResponse(long chatId, String text)
    {

        if (!userRepo.containsKey(chatId) ||
                userRepo.get(chatId).getUserState().getRegState() != regState.REGISTERED)
        {
            return registerNewUser(chatId, text);
        }

        return "Smth went wrong";
    }

    public String registerNewUser(long chatId, String text)
    {
        var user = userRepo.getOrDefault(chatId, new User());

        switch (user.getUserState().getRegState())
        {
            case UNREGISTERED -> {
                userRepo.put(chatId, user);
                user.setUserRegState(regState.NAME_REQUESTED);

                return "Приветствую! Я - бот для знакомств по интересам. \r\n" +
                        "Давайте знакомиться! Как к Вам можно обращаться?";
            }
            case NAME_REQUESTED -> {
                user.setName(text);
                user.setUserRegState(regState.DESCRIPTION_REQUESTED);

                return String.format("Очень рад, %s!", user.getName()) +
                        "\r\nРасскажите что-нибудь о себе, буквально пару слов.";
            }
            case DESCRIPTION_REQUESTED -> {
                user.setDescription(text);
                user.setUserRegState(regState.QUESTION_REQUESTED);

                return String.format("Вы явно интересная личность, %s!", user.getName()) +
                        "\r\nПредлагаю Вам придумать вопрос. Другие пользователи будут отвечать на него, а вы сможете выбрать самые интересные ответы и пообщаться с респондентами!";
            }
            case QUESTION_REQUESTED -> {
                user.question.setQuestion(text);
                user.setUserRegState(regState.REGISTERED);

                return "Вам тоже уже интересно, что Вам ответят?\r\n" +
                        "Пока Вы ждёте, предлагаю поотвечать на чужие вопросы. Используйте команду /next, чтобы получить первый вопрос!";
            }
            case REGISTERED -> {
                return "Error: registration done already.";
            }
        }

        return "Something went wrong during the registration.";
    }
}