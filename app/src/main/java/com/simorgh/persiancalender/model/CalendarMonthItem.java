package com.simorgh.persiancalender.model;

public class CalendarMonthItem {
    // 0: day 1: day name
    private int type;
    private Object item;
    private boolean isSelected = false;

    public CalendarMonthItem(int type, Object item) {
        this.type = type;
        this.item = item;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}
