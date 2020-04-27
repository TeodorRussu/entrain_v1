package entraining_feed.feed.env;

public class StaticData {

    private static String emailTo;

    private StaticData() {
    }

    public static String getEmailTo() {
        return emailTo;
    }

    public static void setEmailTo(String email) {
        emailTo = email;
    }
}
