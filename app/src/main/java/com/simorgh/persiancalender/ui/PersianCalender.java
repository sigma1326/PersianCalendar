package com.simorgh.persiancalender.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.simorgh.persiancalender.R;
import com.simorgh.persiancalender.management.App;
import com.simorgh.persiancalender.model.CalendarDay;
import com.simorgh.persiancalender.model.CalendarDayName;
import com.simorgh.persiancalender.model.CalendarMonthItem;
import com.simorgh.persiancalender.utils.CalendarTool;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class PersianCalender extends ConstraintLayout {
    private FragmentManager fragmentManager;
    private ViewPager viewPager;
    private AppCompatTextView tv_year;
    private AppCompatTextView tv_month;
    private AppCompatTextView tv_day_full_name;
    private AppCompatTextView tvPersian_date;
    private AppCompatTextView tvGregorian_date;
    private AppCompatTextView tvArabic_date;
    Calendar realGregorianDate;
    CalendarTool persianMonthPointer;
    public static CalendarTool realPersianDate;
    private LoopingViewPagerAdapter pagerAdapter;
    private int currentMonthPosition;
    public static int currentPage = 0;
    public static int lastPage = 0;
    public static CalendarTool selectedDate;

    private OnDayClickListener listener = new OnDayClickListener() {
        int lastClickedCell = -1;


        @Override
        public void onClick(int position) {
            MonthViewFragment fragment = (MonthViewFragment) pagerAdapter.getItem(currentMonthPosition);
            fragment.updateViews(position, lastClickedCell);
            lastClickedCell = position;
            Log.d(App.TAG, "selected persian date: " + selectedDate.getIranianDate());
            Log.d(App.TAG, "selected miladi date: " + selectedDate.getGregorianDate());
            tvPersian_date.setText(selectedDate.getIranianFormalDate());
            tvGregorian_date.setText(selectedDate.getGregorianFormalDate());
            tvArabic_date.setText(selectedDate.getArabicFormalDate());
            tv_day_full_name.setText(PersianCalender.getDayOfWeekPersianName(1 + (selectedDate.getDayOfWeek() + 2) % 7));
        }
    };


    public static String getDayOfWeekPersianName(int day) {
        String name = "";
        switch (day) {
            case 1:
                name = "شنبه";
                break;
            case 2:
                name = "یکشنبه";
                break;
            case 3:
                name = "دوشنبه";
                break;
            case 4:
                name = "سە‌شنبه";
                break;
            case 5:
                name = "چهارشنبه";
                break;
            case 6:
                name = "پنج‌شنبه";
                break;
            case 7:
                name = "جمعه";
                break;
        }
        return name;
    }

    class CalenderManager {


        public CalenderManager() {

        }


    }

    public int getGregorianYear() {
        return realGregorianDate.get(Calendar.YEAR);
    }

    public int getGregorianMonthForPersianCal() {
        return realGregorianDate.get(Calendar.MONTH) + 1;
    }

    public int getGregorianDayOfMonth() {
        return realGregorianDate.get(Calendar.DAY_OF_MONTH);
    }

    public PersianCalender(Context context) {
        super(context);
        initViews();
    }

    public PersianCalender(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PersianCalender(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public CalendarTool getMonthPointer() {
        return persianMonthPointer;
    }

    public void setMonthPointer(CalendarTool monthPointer) {
        this.persianMonthPointer = monthPointer;
    }

    @SuppressLint("SetTextI18n")
    public void updateCalenderViews(int position) {
        tv_month.setText(persianMonthPointer.getIranianMonthName());
        tv_year.setText(persianMonthPointer.getIranianYear() + "");
        MonthViewFragment fragment = (MonthViewFragment) pagerAdapter.getItem(position);
        fragment.updateCalendar(persianMonthPointer, initDays());
        currentMonthPosition = position;
        lastPage = currentPage;
        currentPage = position;
    }

    public static boolean isCurrentMonth(CalendarTool ct) {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        CalendarTool realPersianDate = new CalendarTool(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));

        return ((ct.getIranianYear() == realPersianDate.getIranianYear())
                && (ct.getIranianMonth() == realPersianDate.getIranianMonth()));
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.persian_calender, this, true);

        tv_year = findViewById(R.id.tv_year);
        tv_month = findViewById(R.id.tv_month);
        tvPersian_date = findViewById(R.id.tv_persian_date);
        tvGregorian_date = findViewById(R.id.tv_gregorian_date);
        tvArabic_date = findViewById(R.id.tv_arabic_date);
        tv_day_full_name = findViewById(R.id.tv_full_day_name);

        viewPager = findViewById(R.id.month_view_container);

        realGregorianDate = Calendar.getInstance(TimeZone.getDefault());
        persianMonthPointer = new CalendarTool(getGregorianYear(), getGregorianMonthForPersianCal(), getGregorianDayOfMonth());
        selectedDate = new CalendarTool(getGregorianYear(), getGregorianMonthForPersianCal(), getGregorianDayOfMonth());
        realPersianDate = new CalendarTool(getGregorianYear(), getGregorianMonthForPersianCal(), getGregorianDayOfMonth());
    }

    private void init() {
        pagerAdapter = new LoopingViewPagerAdapter(fragmentManager);
        MonthViewFragment monthFragment;
        MonthViewModel model;
        for (int i = 0; i < 12; i++) {
            monthFragment = MonthViewFragment.newInstance(persianMonthPointer, i, initDays(), listener);
            model = new MonthViewModel(monthFragment, persianMonthPointer);
            pagerAdapter.addItem(model);
        }


        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0, true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPosition = -1;

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > lastPosition) {
                    persianMonthPointer.rollUpMonth();
                } else if (position < lastPosition) {
                    persianMonthPointer.rollDownMonth();
                }
                updateCalenderViews(position);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tvPersian_date.setText(realPersianDate.getIranianFormalDate());
        tvGregorian_date.setText(realPersianDate.getGregorianFormalDate());
        tvArabic_date.setText(realPersianDate.getArabicFormalDate());
        tv_day_full_name.setText(PersianCalender.getDayOfWeekPersianName(1 + (realPersianDate.getDayOfWeek() + 2) % 7));

    }

    public ArrayList<CalendarMonthItem> initDays() {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        items.addAll(getDayNames());
        items.addAll(getEmptyDays());
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

        persianMonthPointer.setIranianDate(persianMonthPointer.getIranianYear(), persianMonthPointer.getIranianMonth(), 1);
        //days : 0 to 6 , starts with saturday
        int dayOfWeekFarsi = (persianMonthPointer.getDayOfWeek() + 2) % 7;

        for (int i = 0; i < dayOfWeekFarsi; i++) {
            CalendarMonthItem s_monthItem = new CalendarMonthItem(1, new CalendarDayName(""));
            items.add(s_monthItem);
        }
        return items;
    }

    public static ArrayList<CalendarMonthItem> getEmptyDays(CalendarTool ct) {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        ct.setIranianDate(ct.getIranianYear(), ct.getIranianMonth(), 1);
        //days : 0 to 6 , starts with saturday
        int dayOfWeekFarsi = (ct.getDayOfWeek() + 2) % 7;

        for (int i = 0; i < dayOfWeekFarsi; i++) {
            CalendarMonthItem s_monthItem = new CalendarMonthItem(1, new CalendarDayName(""));
            items.add(s_monthItem);
        }
        return items;
    }

    private ArrayList<CalendarMonthItem> getDays() {
        ArrayList<CalendarMonthItem> items = new ArrayList<>();

        int numOfDays = 31;
        if (persianMonthPointer.getIranianMonth() > 7) {
            numOfDays = 30;
            if (persianMonthPointer.getIranianMonth() == 12) {
                numOfDays = persianMonthPointer.IsLeap(persianMonthPointer.getIranianYear()) ? 30 : 29;
            }
        }

        for (int i = 0; i < numOfDays; i++) {
            CalendarMonthItem s_monthItem = new CalendarMonthItem(0, new CalendarDay(i + 1));
            items.add(s_monthItem);
        }
        return items;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false; //will not prevent child touch events
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        if (gainFocus) {
            performClick();
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        init();
    }

    public class LoopingViewPagerAdapter extends FragmentPagerAdapter {

        private List<MonthViewModel> items;

        public LoopingViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.items = new ArrayList<>();
        }

        public LoopingViewPagerAdapter(FragmentManager fm, List<MonthViewModel> items) {
            super(fm);
            this.items = items;
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position % items.size()).getFragment();
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            int virtualPosition = position % items.size();
            return super.instantiateItem(container, virtualPosition);
        }

        @Override
        public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            int virtualPosition = position % items.size();
            super.destroyItem(container, virtualPosition, object);
        }

        public void addItem(MonthViewModel monthViewModel) {
            items.add(monthViewModel);
        }

    }


    class MonthViewModel {
        private Fragment fragment;
        private CalendarTool calendarTool;
        private boolean isCurrentMonth;
        private int currentDay;

        public MonthViewModel(Fragment fragment, CalendarTool calendarTool, boolean isCurrentMonth, int currentDay) {
            this.fragment = fragment;
            this.calendarTool = calendarTool;
            this.isCurrentMonth = isCurrentMonth;
            this.currentDay = currentDay;
        }

        public MonthViewModel(Fragment fragment, CalendarTool calendarTool) {
            this.fragment = fragment;
            this.calendarTool = calendarTool;
            this.isCurrentMonth = false;
            this.currentDay = 1;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public CalendarTool getCalendarTool() {
            return calendarTool;
        }

        public void setCalendarTool(CalendarTool calendarTool) {
            this.calendarTool = calendarTool;
        }

        public boolean isCurrentMonth() {
            return isCurrentMonth;
        }

        public void setCurrentMonth(boolean currentMonth) {
            isCurrentMonth = currentMonth;
        }

        public int getCurrentDay() {
            return currentDay;
        }

        public void setCurrentDay(int currentDay) {
            this.currentDay = currentDay;
        }
    }

    public interface OnDayClickListener {
        void onClick(int position);
    }
}
