package notification.strategy;

import java.util.Date;

public class AvailabilityNotification extends AbstractNotification {

    public AvailabilityNotification(String lang, int num, Date timestamp) {
        super(lang, num, timestamp);
    }

    @Override
    public String createNotificationMessage() {

        String lang = super.getLang();

        if(lang.equals("TR")) {
            super.setNotificationMessage(super.getTimestamp() + " tarihinde Aradiginiz " + super.getNum() + " simdi cevrimici");
        } else if (lang.equals("ENG")) {
            super.setNotificationMessage("The number you called: " + super.getNum() + " at " + super.getTimestamp() + " is now available.");
        }

        return super.getNotificationMessage();

    }
}
