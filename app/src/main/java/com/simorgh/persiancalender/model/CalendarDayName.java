package com.simorgh.persiancalender.model;

public class CalendarDayName {
    private String name;
    private boolean selectable;

    public CalendarDayName(String name) {
        this.name = name;
        selectable = !name.equals("");
    }


    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.selectable = !name.equals("");
    }
}
