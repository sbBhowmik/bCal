package rupik.com.bengali_calendar;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by macmin5 on 01/02/17.
 */

public class MonthInfo {
    Date startDate;
    Date endDate;
    String monthName;
    int numberOfDaysInMonth;

    ArrayList<DateInfo> specialDates;

    public int getNumberOfDaysInMonth() {
        return numberOfDaysInMonth;
    }

    public void setNumberOfDaysInMonth(int numberOfDaysInMonth) {
        this.numberOfDaysInMonth = numberOfDaysInMonth;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ArrayList<DateInfo> getSpecialDates() {
        return specialDates;
    }

    public void setSpecialDates(ArrayList<DateInfo> specialDates) {
        this.specialDates = specialDates;
    }
}
