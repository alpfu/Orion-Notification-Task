package notification.strategy;

import java.util.Date;

public class MissingCallNotification extends AbstractNotification {

    private int count;

    public MissingCallNotification(String lang, int num, Date timestamp, int count) {
        super(lang, num, timestamp);
        this.count = count;
    }

    @Override
    public String createNotificationMessage() {

        String lang = super.getLang();

        if(lang.equals("TR")) {
            super.setNotificationMessage("Sizi arayan numaralar: " + super.getNum() + " " + super.getTimestamp() + " " + this.count);
        } else if (lang.equals("ENG")) {
            super.setNotificationMessage("Missed calls: " + super.getNum() + " " + super.getTimestamp() + " " + this.count);
        }

        return super.getNotificationMessage();

    }
}
