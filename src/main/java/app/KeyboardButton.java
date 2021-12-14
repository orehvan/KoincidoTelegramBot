package app;

public class KeyboardButton
{
    private String text;
    private String url;
    private String callbackData;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getCallbackData()
    {
        return callbackData;
    }

    public void setCallbackData(String callbackData)
    {
        this.callbackData = callbackData;
    }
}
