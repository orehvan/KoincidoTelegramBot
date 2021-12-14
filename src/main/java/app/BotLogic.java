package app;

import models.Constants.Emojis;
import models.Constants.Enums.ChatState;
import models.Constants.Enums.QuestState;
import models.repos.UserRepository;

import java.util.LinkedList;

public record BotLogic(UserRepository userRepo)
{
    public void updateUsername(long chatId, String username)
    {
        var user = userRepo.getByChatId(chatId);
        user.setUsername(username);
        userRepo.put(user);
    }

    public BotResponse respondToCallback(long chatId, String callback)
    {
        var user = userRepo.getByChatId(chatId);
        var respondent = userRepo.getByUsername(callback);
        var answer = user.question.getAnswerByUsername(respondent.getUsername());

        var userLink = new LinkedList<KeyboardButton>();
        var button = new KeyboardButton();

        button.setUrl(String.format("t.me/%s", respondent.getUsername()));
        button.setText("Открыть профиль в Telegram");
        userLink.add(button);

        return new BotResponse(respondent.printProfileWithAnswer(answer), userLink);
    }

    public BotResponse respondToMessage(long chatId, String text)
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
                        return new BotResponse("Неверный ввод. Используй команды для работы с ботом.", null);
                    }
                }
            }
        }

        return new BotResponse("Incorrect input", null);
    }

    private BotResponse showAnswers(long chatId)
    {
        var user = userRepo.getByChatId(chatId);

        var answers = new LinkedList<KeyboardButton>();

        for (var username :
                user.question.getAnswers().keySet())
        {
            var respondent = userRepo.getByUsername(username);

            var button = new KeyboardButton();
            button.setText(String.format("%s: %s", respondent.getName(), user.question.getAnswerByUsername(username)));
            button.setCallbackData(username);

            answers.add(button);
        }

        return new BotResponse(String.format("На твой вопрос \"%s\" ответили:\r\n", user.question.getQuestionText()),
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
            questioner.question.addAnswer(user.getUsername(), text);
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
                        "Давай знакомиться! Как к тебе можно обращаться?";
            }
            case NAME_REQUESTED -> {
                user.setName(text);
                user.setChatState(ChatState.DESCRIPTION_REQUESTED);
                userRepo.put(user);
                return String.format("Очень рад, %s!", user.getName()) +
                        "\r\nРасскажи что-нибудь о себе, буквально пару слов.";
            }
            case DESCRIPTION_REQUESTED -> {
                user.setDescription(text);
                user.setChatState(ChatState.QUESTION_REQUESTED);
                userRepo.put(user);
                return String.format("Ты явно интересная личность, %s!", user.getName()) +
                        "\r\nПредлагаю придумать вопрос. Другие пользователи будут отвечать на него, а ты сможешь" +
                        " выбрать самые интересные ответы и пообщаться с респондентами!";
            }
            case QUESTION_REQUESTED -> {
                user.question.setQuestionText(text);
                user.setChatState(ChatState.REGISTERED);
                user.setQuestState(QuestState.IDLE);
                userRepo.put(user);
                return "Тоже уже интересно, что тебе ответят?\r\n" +
                        "Пока ждёшь, предлагаю поотвечать на чужие вопросы. Используй команду /next, чтобы " +
                        "получить первый вопрос!";
            }
        }

        return "Something went wrong during the registration.";
    }
}