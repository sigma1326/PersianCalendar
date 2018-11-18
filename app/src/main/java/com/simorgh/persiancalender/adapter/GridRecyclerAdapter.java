package com.simorgh.persiancalender.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.persiancalender.R;
import com.simorgh.persiancalender.management.App;
import com.simorgh.persiancalender.model.CalendarDay;
import com.simorgh.persiancalender.model.CalendarDayName;
import com.simorgh.persiancalender.model.CalendarMonthItem;
import com.simorgh.persiancalender.ui.PersianCalender;
import com.simorgh.persiancalender.utils.CalendarTool;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> {
    private ArrayList<CalendarMonthItem> items;
    private PersianCalender.OnDayClickListener listener;
    private boolean isCurrentMonth;
    private CalendarTool calendarTool;

    public ArrayList<CalendarMonthItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CalendarMonthItem> items) {
        this.items = items;
    }

    public GridRecyclerAdapter(CalendarTool calendarTool, ArrayList<CalendarMonthItem> items, PersianCalender.OnDayClickListener listener, boolean isCurrentMonth) {
        this.calendarTool = calendarTool;
        this.items = items;
        this.listener = listener;
        this.isCurrentMonth = isCurrentMonth;
        Log.d(App.TAG, "current month: " + isCurrentMonth);
        Log.d(App.TAG, "current date: " + calendarTool.getIranianDate());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_name_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(int position, int lastClickedCell) {
//        Log.d(App.TAG, "unselected: " + lastClickedCell);
//        Log.d(App.TAG, "selected: " + position);
        if (lastClickedCell == -1) {
            items.get(position).setSelected(true);
            notifyItemChanged(position);
        } else if (position != lastClickedCell) {
            items.get(position).setSelected(true);
            items.get(lastClickedCell).setSelected(false);
            notifyItemChanged(position);
            notifyItemChanged(lastClickedCell);
        } else {
            if (!items.get(position).isSelected()) {
                items.get(position).setSelected(true);
                notifyItemChanged(position);
            }
        }
        // update the selected day
        int empty_days = PersianCalender.getEmptyDays(calendarTool).size();
        int day_names = 7;
        int offset = empty_days + day_names;
        PersianCalender.selectedDate.setIranianDate(calendarTool.getIranianYear(), calendarTool.getIranianMonth(), (position + 1) - offset);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView tv_day_name;
        private int position = -1;

        void init() {
            tv_day_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != -1) {
                        listener.onClick(position);
                    }
                }
            });
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_day_name = view.findViewById(R.id.tv_day_name);
            init();
        }

        public void bind(CalendarMonthItem item, final int position) {
            this.position = position;
            if (item.getType() == 0) {
                // item is day
                tv_day_name.setTextAppearance(view.getContext(), R.style.day_number_style);
                tv_day_name.setText(((CalendarDay) item.getItem()).getDay() + "");
                if (item.isSelected()) {
                    if (isCurrentMonth) {
                        if (((CalendarDay) item.getItem()).getDay() == calendarTool.getIranianDay()) {
                            tv_day_name.setBackgroundResource(R.drawable.fill_circle_orange);
                            tv_day_name.setTextAppearance(tv_day_name.getContext(), R.style.day_number_selected_style);
                        } else {
                            tv_day_name.setBackgroundResource(R.drawable.fill_circle);
                            tv_day_name.setTextAppearance(tv_day_name.getContext(), R.style.day_number_selected_style);
                        }
                    } else {
                        tv_day_name.setBackgroundResource(R.drawable.fill_circle);
                        tv_day_name.setTextAppearance(tv_day_name.getContext(), R.style.day_number_selected_style);
                    }

                } else {
                    if (isCurrentMonth) {
                        if (((CalendarDay) item.getItem()).getDay() == PersianCalender.realPersianDate.getIranianDay()) {
                            tv_day_name.setBackgroundResource(R.drawable.border_circle_orange);
                            tv_day_name.setTextAppearance(tv_day_name.getContext(), R.style.day_number_style);
                        } else {
                            tv_day_name.setBackground(null);
                            tv_day_name.setTextAppearance(tv_day_name.getContext(), R.style.day_number_style);
                        }
                    } else {
                        tv_day_name.setBackground(null);
                        tv_day_name.setTextAppearance(tv_day_name.getContext(), R.style.day_number_style);
                    }
                }
            } else if (item.getType() == 1) {
                // item is day name
                tv_day_name.setTextAppearance(view.getContext(), R.style.day_name_style);
                tv_day_name.setText(((CalendarDayName) item.getItem()).getName());
                tv_day_name.setBackground(null);
            }
        }
    }
}
