package rupik.com.bengali_calendar;

/**
 * Created by macmin5 on 01/02/17.
 */

public class DateInfo {
    int date;
    String occasionName;
    String occasionDetails;
    boolean isSpecial;

    public int getDate() {
        return date;
    }

    public String getOccasionDetails() {
        return occasionDetails;
    }

    public String getOccasionName() {
        return occasionName;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setOccasionDetails(String occasionDetails) {
        this.occasionDetails = occasionDetails;
    }

    public void setOccasionName(String occasionName) {
        this.occasionName = occasionName;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }
}
