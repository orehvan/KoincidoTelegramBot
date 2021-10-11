package app;

import models.Constants.Emojis;
import models.Constants.Enums.*;
import models.UserRepository;

public class BotLogic
{
    private final UserRepository userRepo = new UserRepository();

    public String formResponse(long chatId, String text)
    {
        if (text.equals("/start") || userRepo.getByChatIdOrNew(chatId).getUserState().getRegState() != RegState.REGISTERED)
        {
            return registerNewUser(chatId, text);
        }
        else if(text.equals("/next") || userRepo.getByChatId(chatId).getUserState().getQuestState() != QuestState.UNAVAILABLE)
        {
            return recordNewAnswer(chatId, text);
        }

        return "Smth went wrong";
    }

    private String recordNewAnswer(long chatId, String text)
    {
        var user = userRepo.getByChatId(chatId);
        var questioner = userRepo.getRandomUser();

        if (user.getUserState().getQuestState() == QuestState.IDLE)
        {
            user.setUserQuestState(QuestState.ANSWER_REQUESTED);

            return String.format("%s %s спрашивает:\r\n", Emojis.hmm, questioner.getName()) +
                    questioner.question.getQuestionText();
        }
        else
        {
            user.setUserQuestState(QuestState.IDLE);
            questioner.question.addAnswer(chatId, text);

            return String.format("Ответ записан. %s /next?", Emojis.whiteCheckMark);
        }
    }

    public String registerNewUser(long chatId, String text)
    {
        var user = userRepo.getByChatIdOrNew(chatId);

        switch (user.getUserState().getRegState())
        {
            case UNREGISTERED:
                userRepo.put(user);
                user.setUserRegState(RegState.NAME_REQUESTED);

                return String.format("Приветствую! %s Я - бот для знакомств по интересам. \r\n", Emojis.wave) +
                        "Давайте знакомиться! Как к Вам можно обращаться?";

            case NAME_REQUESTED:
                user.setName(text);
                user.setUserRegState(RegState.DESCRIPTION_REQUESTED);

                return String.format("Очень рад, %s!", user.getName()) +
                        "\r\nРасскажите что-нибудь о себе, буквально пару слов.";

            case DESCRIPTION_REQUESTED:
                user.setDescription(text);
                user.setUserRegState(RegState.QUESTION_REQUESTED);

                return String.format("Вы явно интересная личность, %s!", user.getName()) +
                        "\r\nПредлагаю Вам придумать вопрос. Другие пользователи будут отвечать на него, а вы сможете выбрать самые интересные ответы и пообщаться с респондентами!";

            case QUESTION_REQUESTED:
                user.question.setQuestionText(text);
                user.setUserRegState(RegState.REGISTERED);
                user.setUserQuestState(QuestState.IDLE);

                return "Вам тоже уже интересно, что Вам ответят?\r\n" +
                        "Пока Вы ждёте, предлагаю поотвечать на чужие вопросы. Используйте команду /next, чтобы получить первый вопрос!";

            case REGISTERED:
                user.setUserRegState(RegState.NAME_REQUESTED);
                user.setUserQuestState(QuestState.UNAVAILABLE);

                return String.format("Окей, я сделаю вид, что вижу Вас впервые. %s", Emojis.sweatGrim) +
                        "Так, как говорите, вас зовут?";
        }

        return "Something went wrong during the registration.";
    }
}