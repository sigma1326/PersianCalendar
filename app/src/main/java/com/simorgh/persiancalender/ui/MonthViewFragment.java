package com.simorgh.persiancalender.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.simorgh.persiancalender.R;
import com.simorgh.persiancalender.adapter.GridRecyclerAdapter;
import com.simorgh.persiancalender.model.CalendarMonthItem;
import com.simorgh.persiancalender.utils.CalendarTool;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MonthViewFragment extends Fragment {
    private CalendarTool calendarTool;
    private int position;
    private RecyclerView recyclerView;
    private ArrayList<CalendarMonthItem> items;
    private GridRecyclerAdapter gridRecyclerAdapter;
    private PersianCalender.OnDayClickListener listener;

    public void updateCalendar(CalendarTool ct, ArrayList<CalendarMonthItem> s_monthItems) {
        calendarTool = ct;
        items = s_monthItems;
        initRecyclerView();
    }

    private OnFragmentInteractionListener mListener;

    public MonthViewFragment() {
    }

    public static MonthViewFragment newInstance(CalendarTool calendarTool, int i, ArrayList<CalendarMonthItem> items, PersianCalender.OnDayClickListener listener) {
        MonthViewFragment fragment = new MonthViewFragment();
        fragment.calendarTool = calendarTool;
        fragment.position = i;
        fragment.items = items;
        fragment.listener = listener;
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        recyclerView = view.findViewById(R.id.rv_days);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), 7);
        recyclerView.setLayoutManager(gridLayoutManager);

        initRecyclerView();


        ImageButton btnPrevMonth = view.findViewById(R.id.img_btn_left);
        ImageButton btnNextMonth = view.findViewById(R.id.img_btn_right);

        btnPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.previousMonth(position);
                }
            }
        });

        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.NextMonth(position);
                }
            }
        });

        return view;
    }

    private void initRecyclerView() {
        gridRecyclerAdapter = new GridRecyclerAdapter(calendarTool, items, listener, PersianCalender.isCurrentMonth(calendarTool));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(gridRecyclerAdapter.getItemCount());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        // disable the scrollview
        recyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        recyclerView.setAdapter(gridRecyclerAdapter);
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.previousMonth(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateViews(int position, int lastClickedCell) {
        gridRecyclerAdapter.updateItems(position, lastClickedCell);
    }

    public interface OnFragmentInteractionListener {
        void previousMonth(int position);

        void NextMonth(int position);
    }
}
