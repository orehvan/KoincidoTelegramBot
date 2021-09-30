package models;

public class Constants
{
    public enum regStates {UNREGISTERED, NAME_REQUESTED, DESCRIPTION_REQUESTED, TEST_STARTED, TEST_COMPLETE}

    private static final String BOT_NAME = "Koincido";
    private static final String BOT_TOKEN = "2042577245:AAGGqgU3f2iolEOD_19A364EI4B6erVoMBA";

    public static String getBOT_NAME()
    {
        return BOT_NAME;
    }

    public static String getBOT_TOKEN()
    {
        return BOT_TOKEN;
    }
}
