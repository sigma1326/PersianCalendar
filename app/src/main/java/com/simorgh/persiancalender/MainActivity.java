package com.simorgh.persiancalender;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.simorgh.persiancalender.management.App;
import com.simorgh.persiancalender.ui.MonthViewFragment;
import com.simorgh.persiancalender.ui.PersianCalender;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MonthViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "debug13";
    private TextView tv_year;
    private TextView tv_month;
    private PersianCalender persianCalender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "# thisYear : " + App.cm.getGrYear());
        Log.d(TAG, "@ thisMonth : " + App.cm.getGrMonth());
        Log.d(TAG, "$ thisDay : " + App.cm.getGrDayOfMonth());

        Log.d(TAG, "$ persian year: " + App.cm.getPersianYear());
        Log.d(TAG, "$ persian month : " + App.cm.getPersianMonth());
        Log.d(TAG, "$ persian day : " + App.cm.getPersianDayOfMonth());

        persianCalender = findViewById(R.id.persian_calender);
        persianCalender.setFragmentManager(getSupportFragmentManager());
        persianCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(App.TAG, "clicked: ");
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void previousMonth(int position) {
        if (persianCalender == null) {
            return;
        }

        persianCalender.getMonthPointer().rollDownMonth();
        persianCalender.updateCalenderViews(position);
    }


    public void NextMonth(int position) {
        if (persianCalender == null) {
            return;
        }
        persianCalender.getMonthPointer().rollUpMonth();
        persianCalender.updateCalenderViews(position);
    }

}
