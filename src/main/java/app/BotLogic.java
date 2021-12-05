package app;

import models.Constants.Emojis;
import models.Constants.Enums.ChatState;
import models.Constants.Enums.QuestState;
import models.Constants.Enums.UpdateType;
import models.UserRepository;

import java.util.HashMap;

public record BotLogic(UserRepository userRepo)
{
    public void updateUsername(long chatId, String username)
    {
        var user = userRepo.getByChatId(chatId);
        user.setUsername(username);
        userRepo.put(user);
    }

    public BotResponse formResponse(UpdateType updateType, long chatId, String text)
    {
        switch (updateType)
        {

            case MESSAGE -> {
                return respondToMessage(chatId, text);
            }
            case CALLBACK -> {
                return respondToCallback(text);
            }
            default -> {
                return new BotResponse("Incorrect input", null);
            }
        }
    }

    private BotResponse respondToCallback(String text)
    {
        var user = userRepo.getByChatId(Long.parseLong(text));

        if (user == null)
        {
            return new BotResponse("User not found", null);
        }

        var userLink = new HashMap<Long, String>();
        userLink.put(0L, String.format("t.me/%s", user.getUsername()));

        return new BotResponse(user.printUserProfile(), userLink);
    }

    private BotResponse respondToMessage(long chatId, String text)
    {
        switch (text)
        {
            case "/start" -> {
                return new BotResponse(registerNewUser(chatId, text, true), null);
            }
            case "/next" -> {
                return new BotResponse(recordNewAnswer(chatId, text, true), null);
            }
            case "/answers" -> {
                return showAnswers(chatId);
            }
            default -> {
                switch (userRepo.getByChatIdOrNew(chatId).getQuestState())
                {

                    case UNAVAILABLE -> {
                        return new BotResponse(registerNewUser(chatId, text, false), null);
                    }
                    case ANSWER_REQUESTED -> {
                        return new BotResponse(recordNewAnswer(chatId, text, false), null);
                    }
                    case IDLE -> {
                        return new BotResponse("Неверный ввод. Используйте команды для работы с ботом.", null);
                    }
                }
            }
        }

        return new BotResponse("Incorrect input", null);
    }

    private BotResponse showAnswers(long chatId)
    {
        var user = userRepo.getByChatId(chatId);

        var answers = new HashMap<Long, String>();

        for (var id :
                user.question.getAnswers().keySet())
        {
            var respondent = userRepo.getByChatId(Long.parseLong(id));

            answers.put(respondent.getChatId(), String.format("%s: %s", respondent.getName(),
                    user.question.getAnswerByChatId(id)));
        }

        return new BotResponse(String.format("На Ваш вопрос \"%s\" ответили:\r\n", user.question.getQuestionText()),
                answers);
    }

    private String recordNewAnswer(long chatId, String text, boolean skip)
    {
        var user = userRepo.getByChatId(chatId);

        if (skip)
        {
            user.setQuestState(QuestState.IDLE);
        }

        if (user.getQuestState() == QuestState.IDLE)
        {

            user.setQuestState(QuestState.ANSWER_REQUESTED);
            var questioner = userRepo.getOtherRandom(user);
            user.setLastQuestionChatId(questioner.getChatId());
            userRepo.put(user);

            return String.format("%s %s спрашивает:\r\n", Emojis.HMM, questioner.getName()) +
                    questioner.question.getQuestionText();
        }
        else
        {
            var questioner = userRepo.getByChatId(user.getLastQuestionChatId());
            questioner.question.addAnswer(chatId, text);
            userRepo.put(questioner);

            user.setQuestState(QuestState.IDLE);
            userRepo.put(user);

            return String.format("Ответ записан. %s /next?", Emojis.WHITE_CHECK_MARK);
        }
    }

    public String registerNewUser(long chatId, String text, boolean reset)
    {
        if (reset)
        {
            userRepo.remove(chatId);
        }

        var user = userRepo.getByChatIdOrNew(chatId);

        switch (user.getChatState())
        {
            case UNREGISTERED -> {
                user.setChatState(ChatState.NAME_REQUESTED);
                userRepo.put(user);
                return String.format("Приветствую! %s Я - бот для знакомств по интересам. \r\n", Emojis.WAVE) +
                        "Давайте знакомиться! Как к Вам можно обращаться?";
            }
            case NAME_REQUESTED -> {
                user.setName(text);
                user.setChatState(ChatState.DESCRIPTION_REQUESTED);
                userRepo.put(user);
                return String.format("Очень рад, %s!", user.getName()) +
                        "\r\nРасскажите что-нибудь о себе, буквально пару слов.";
            }
            case DESCRIPTION_REQUESTED -> {
                user.setDescription(text);
                user.setChatState(ChatState.QUESTION_REQUESTED);
                userRepo.put(user);
                return String.format("Вы явно интересная личность, %s!", user.getName()) +
                        "\r\nПредлагаю Вам придумать вопрос. Другие пользователи будут отвечать на него, а вы сможете" +
                        " выбрать самые интересные ответы и пообщаться с респондентами!";
            }
            case QUESTION_REQUESTED -> {
                user.question.setQuestionText(text);
                user.setChatState(ChatState.REGISTERED);
                user.setQuestState(QuestState.IDLE);
                userRepo.put(user);
                return "Вам тоже уже интересно, что Вам ответят?\r\n" +
                        "Пока Вы ждёте, предлагаю поотвечать на чужие вопросы. Используйте команду /next, чтобы " +
                        "получить первый вопрос!";
            }
        }

        return "Something went wrong during the registration.";
    }
}