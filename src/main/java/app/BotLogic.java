package app;

import models.Constants.Emojis;
import models.Constants.Enums.*;
import models.LocalUserRepository;
import models.MongoDBUserRepository;

public class BotLogic
{
    //private final LocalUserRepository userRepo = new LocalUserRepository();
    private MongoDBUserRepository userRepo;

    public String formResponse(long chatId, String text)
    {
        userRepo = new MongoDBUserRepository();
        userRepo.initialize();

        if (text.equals("/start") || userRepo.getByChatIdOrNew(chatId).getRegState() != RegState.REGISTERED)
        {
            return registerNewUser(chatId, text);
        }
        else if(text.equals("/next") || userRepo.getByChatId(chatId).getQuestState() == QuestState.ANSWER_REQUESTED)
        {
            return recordNewAnswer(chatId, text);
        }
        else if(text.equals("/answers") && userRepo.getByChatId(chatId).getQuestState() == QuestState.IDLE)
        {
            return showAnswers(chatId);
        }

        return "Incorrect input";
    }

    private String showAnswers(long chatId)
    {
        var user = userRepo.getByChatId(chatId);
        
        var output = new StringBuilder();
        output.append(String.format("На Ваш вопрос \"%s\" ответили:\r\n", user.question.getQuestionText()));

        for (var respondentId :
                user.question.getAllRespondentsChatIds())
        {
            var answer = user.question.getAnswerByChatId(respondentId);
            output.append(String.format("%s: %s\r\n", userRepo.getByChatId(respondentId).getName(), answer));
        }

        return output.toString();
    }

    private String recordNewAnswer(long chatId, String text)
    {
        var user = userRepo.getByChatId(chatId);

        if (user.getQuestState() == QuestState.IDLE)
        {

            user.setQuestState(QuestState.ANSWER_REQUESTED);
            var questioner = userRepo.getRandom();
            user.setLastQuestionChatId(questioner.getChatId());

            return String.format("%s %s спрашивает:\r\n", Emojis.hmm, questioner.getName()) +
                    questioner.question.getQuestionText();
        }
        else
        {
            var questioner = userRepo.getByChatId(user.getLastQuestionChatId());
            user.setQuestState(QuestState.IDLE);
            questioner.question.addAnswer(chatId, text);

            return String.format("Ответ записан. %s /next?", Emojis.whiteCheckMark);
        }
    }

    public String registerNewUser(long chatId, String text)
    {
        var user = userRepo.getByChatIdOrNew(chatId);

        switch (user.getRegState())
        {
            case UNREGISTERED -> {
                user.setRegState(RegState.NAME_REQUESTED);
                userRepo.put(user);
                return String.format("Приветствую! %s Я - бот для знакомств по интересам. \r\n", Emojis.wave) +
                        "Давайте знакомиться! Как к Вам можно обращаться?";
            }
            case NAME_REQUESTED -> {
                user.setName(text);
                user.setRegState(RegState.DESCRIPTION_REQUESTED);
                userRepo.put(user);
                return String.format("Очень рад, %s!", user.getName()) +
                        "\r\nРасскажите что-нибудь о себе, буквально пару слов.";
            }
            case DESCRIPTION_REQUESTED -> {
                user.setDescription(text);
                user.setRegState(RegState.QUESTION_REQUESTED);
                userRepo.put(user);
                return String.format("Вы явно интересная личность, %s!", user.getName()) +
                        "\r\nПредлагаю Вам придумать вопрос. Другие пользователи будут отвечать на него, а вы сможете выбрать самые интересные ответы и пообщаться с респондентами!";
            }
            case QUESTION_REQUESTED -> {
                user.question.setQuestionText(text);
                user.setRegState(RegState.REGISTERED);
                user.setQuestState(QuestState.IDLE);
                userRepo.put(user);
                return "Вам тоже уже интересно, что Вам ответят?\r\n" +
                        "Пока Вы ждёте, предлагаю поотвечать на чужие вопросы. Используйте команду /next, чтобы получить первый вопрос!";
            }
            case REGISTERED -> {
                user.setRegState(RegState.NAME_REQUESTED);
                user.setQuestState(QuestState.UNAVAILABLE);
                userRepo.put(user);
                return String.format("Окей, я сделаю вид, что вижу Вас впервые. %s", Emojis.sweatGrim) +
                        "Так, как говорите, вас зовут?";
            }
        }

        return "Something went wrong during the registration.";
    }
}