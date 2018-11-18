package com.simorgh.persiancalender.model;

import android.util.Log;

import com.simorgh.persiancalender.management.App;
import com.simorgh.persiancalender.utils.CalendarTool;

import java.util.ArrayList;
import java.util.Calendar;

public class CalenderManager {
    private Calendar calendar;
    private CalendarTool calendarTool;
    private CalendarTool currentPersianDateFirst;
    private int currentPersianMonth;
    private int currentPersianYear;
    private int currentPersianDay;
    private Calendar currentGeDate;


    public CalenderManager(Calendar calendar) {
        this.calendar = calendar;
        this.currentGeDate = calendar;

        this.calendarTool = new CalendarTool(getGrYear(), getGrMonthForCalenderTool(), getGrDayOfMonth());
        this.currentPersianYear = getPersianYear();
        this.currentPersianMonth = getPersianMonth();
        this.currentPersianDay = getPersianDayOfMonth();


        currentPersianDateFirst = new CalendarTool(getGrYear(), getGrMonthForCalenderTool(), getGrDayOfMonth());
        currentPersianDateFirst.setIranianDate(calendarTool.getIranianYear(), calendarTool.getIranianMonth(), 1);
    }

    public CalendarTool getCurrentPersianDateFirst() {
        return currentPersianDateFirst;
    }

    public void setCurrentPersianDateFirst(CalendarTool currentPersianDateFirst) {
        this.currentPersianDateFirst = currentPersianDateFirst;
    }

    public CalendarTool getCalendarTool() {
        return calendarTool;
    }

    public void setCalendarTool(CalendarTool calendarTool) {
        this.calendarTool = calendarTool;
    }


    public ArrayList<CalendarMonthItem> initDays() {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        items.addAll(getDayNames());
        items.addAll(getEmptyDays());
        items.addAll(getDays());

        return items;
    }

    public ArrayList<CalendarMonthItem> initDays(CalendarTool ct) {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        items.addAll(getDayNames());
        items.addAll(getEmptyDays(ct));
        items.addAll(getDays());

        return items;
    }


    private ArrayList<CalendarMonthItem> getDayNames() {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        // init the day names
        CalendarMonthItem sat = new CalendarMonthItem(1, new CalendarDayName("ش"));
        CalendarMonthItem sun = new CalendarMonthItem(1, new CalendarDayName("ی"));
        CalendarMonthItem mon = new CalendarMonthItem(1, new CalendarDayName("د"));
        CalendarMonthItem tue = new CalendarMonthItem(1, new CalendarDayName("س"));
        CalendarMonthItem wed = new CalendarMonthItem(1, new CalendarDayName("چ"));
        CalendarMonthItem thu = new CalendarMonthItem(1, new CalendarDayName("پ"));
        CalendarMonthItem fri = new CalendarMonthItem(1, new CalendarDayName("ج"));

        items.add(sat);
        items.add(sun);
        items.add(mon);
        items.add(tue);
        items.add(wed);
        items.add(thu);
        items.add(fri);

        return items;
    }


    private ArrayList<CalendarMonthItem> getEmptyDays() {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        //days : 0 to 6 , starts with saturday
        int dayOfWeekFarsi = (currentPersianDateFirst.getDayOfWeek() + 2) % 7;
//        Log.d(App.TAG, "# day " + dayOfWeekFarsi);


        for (int i = 0; i < dayOfWeekFarsi; i++) {
            CalendarMonthItem s_monthItem = new CalendarMonthItem(1, new CalendarDayName(""));
            items.add(s_monthItem);
        }
        return items;
    }

    private ArrayList<CalendarMonthItem> getEmptyDays(CalendarTool ct) {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        //days : 0 to 6 , starts with saturday
        int dayOfWeekFarsi = (ct.getDayOfWeek() + 2) % 7;
        Log.d(App.TAG, "# day " + dayOfWeekFarsi);


        for (int i = 0; i < dayOfWeekFarsi; i++) {
            CalendarMonthItem s_monthItem = new CalendarMonthItem(1, new CalendarDayName(""));
            items.add(s_monthItem);
        }
        return items;
    }

    private ArrayList<CalendarMonthItem> getDays() {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        int numOfDays = 31;
        if (calendarTool.getIranianMonth() > 7) {
            numOfDays = 30;
            if (calendarTool.getIranianMonth() == 12) {
                numOfDays = calendarTool.IsLeap(calendarTool.getIranianYear()) ? 30 : 29;
            }
        }

        for (int i = 0; i < numOfDays; i++) {
            CalendarMonthItem s_monthItem = new CalendarMonthItem(0, new CalendarDay(i + 1));
            items.add(s_monthItem);
        }
        return items;
    }


    public int getGrYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getGrMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getGrMonthForCalenderTool() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getGrDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getPersianYear() {
        return calendarTool.getIranianYear();
    }

    public int getPersianMonth() {
        return calendarTool.getIranianMonth();
    }

    public int getPersianDayOfMonth() {
        return calendarTool.getIranianDay();
    }

    public void rollUpMonth() {
        currentPersianMonth++;
        if (currentPersianMonth == 13) {
            currentPersianMonth = 1;
            currentPersianYear++;
        }
        currentPersianDateFirst.setIranianDate(currentPersianYear, currentPersianMonth, 1);
        int dayOfWeekFarsi = (currentPersianDateFirst.getDayOfWeek() + 2) % 7;
        Log.d(App.TAG, "# day + " + dayOfWeekFarsi);
    }

    public CalendarTool rollUpMonth(CalendarTool ct) {
        int m = ct.getIranianMonth() + 1;
        int y = ct.getIranianYear();
        if (m == 13) {
            m = 1;
            y++;
        }
        ct.setIranianDate(y, m, 1);
        return ct;
    }

    public CalendarTool rollDownMonth(CalendarTool ct) {
        int m = ct.getIranianMonth() - 1;
        int y = ct.getIranianYear();
        if (m == 0) {
            m = 12;
            y--;
        }
        ct.setIranianDate(y, m, 1);
        return ct;
    }

    public void rollDownMonth() {
        currentPersianMonth--;
        if (currentPersianMonth == 0) {
            currentPersianMonth = 12;
            currentPersianYear--;
        }
        currentPersianDateFirst.setIranianDate(currentPersianYear, currentPersianMonth, 1);
        int dayOfWeekFarsi = (currentPersianDateFirst.getDayOfWeek() + 2) % 7;
        Log.d(App.TAG, "# day - " + dayOfWeekFarsi);
    }

}
