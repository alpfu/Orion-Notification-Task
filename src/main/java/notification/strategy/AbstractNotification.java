package notification.strategy;

import java.util.Date;

/**
 * Abstract class keeps common instances of notifications
 * that are language preference and notificationMessage
 * */
public abstract class AbstractNotification {

    private String notificationMessage;
    private String lang; // TODO: get this from option panel...

    private int num;
    private Date timestamp;

    public AbstractNotification(String lang, int num, Date timestamp) {
        this.lang = lang;
        this.num = num;
        this.timestamp = timestamp;
    }

    public abstract String createNotificationMessage();

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getLang() {
        return lang;
    }

    public int getNum() {
        return num;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
