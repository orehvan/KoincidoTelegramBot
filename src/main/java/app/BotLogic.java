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

    public Tuple<String, HashMap<Long, String>> formResponse(UpdateType updateType, long chatId, String text)
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
                return new Tuple<>("Incorrect input", null);
            }
        }
    }

    private Tuple<String, HashMap<Long, String>> respondToCallback(String text)
    {
        var user = userRepo.getByChatId(Long.parseLong(text));

        if (user == null)
        {
            return new Tuple<>("User not found", null);
        }

        var userLink = new HashMap<Long, String>();
        userLink.put(0L, String.format("t.me/%s", user.getUsername()));

        return new Tuple<>(user.printUserProfile(), userLink);
    }

    private Tuple<String, HashMap<Long, String>> respondToMessage(long chatId, String text)
    {
        if (text.equals("/start") || text.equals(String.format("Регистрация %s", Emojis.MEMO)) ||
                userRepo.getByChatIdOrNew(chatId).getChatState() != ChatState.REGISTERED)
        {
            return new Tuple<>(registerNewUser(chatId, text), null);
        }
        else if (text.equals("/next") || text.equals(String.format("Следующий вопрос %s", Emojis.ARROW_RIGHT)) ||
                userRepo.getByChatId(chatId).getQuestState() == QuestState.ANSWER_REQUESTED)
        {
            var skip = text.equals("/next") || text.equals(String.format("Следующий вопрос %s", Emojis.ARROW_RIGHT));

            return new Tuple<>(recordNewAnswer(chatId, text, skip), null);
        }
        else if (text.equals("/answers") || text.equals(String.format("Список ответов %s", Emojis.SPEECH_BUBBLE)) &&
                userRepo.getByChatId(chatId).getQuestState() == QuestState.IDLE)
        {
            return showAnswers(chatId);
        }

        return new Tuple<>("Incorrect input", null);
    }

    private Tuple<String, HashMap<Long, String>> showAnswers(long chatId)
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

        return new Tuple<>(String.format("На Ваш вопрос \"%s\" ответили:\r\n", user.question.getQuestionText()),
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

    public String registerNewUser(long chatId, String text)
    {
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
            case REGISTERED -> {
                user.setChatState(ChatState.NAME_REQUESTED);
                user.setQuestState(QuestState.UNAVAILABLE);
                userRepo.put(user);
                return String.format("Окей, я сделаю вид, что вижу Вас впервые. %s", Emojis.SWEAT_GRIM) +
                        "Так, как говорите, вас зовут?";
            }
        }

        return "Something went wrong during the registration.";
    }
}