package com.example.yutathinkpad.esc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.object.AttendanceRateObject;

import java.util.List;

/**
 * Created by Yuta on 2016/03/29.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AttendanceRateViewHolder> {
    private List<AttendanceRateObject> items;


    public RecyclerViewAdapter (List<AttendanceRateObject> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AttendanceRateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shuseki_rate_view, viewGroup, false);
        return new AttendanceRateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AttendanceRateViewHolder viewHolder, int i) {

        viewHolder.subjectName.setText(items.get(i).getSubjectName());
        viewHolder.unitNum.setText(items.get(i).getUnit());
        viewHolder.attendanceRate.setText(items.get(i).getAttendanceRate());
        viewHolder.attendanceNum.setText(items.get(i).getAttendanceNumber() );
        viewHolder.abcentNum.setText(items.get(i).getAbsentNumber());
        viewHolder.lateNum.setText(items.get(i).getLateNumber());
        viewHolder.kouketsuNum1.setText(items.get(i).getPublicAbsentNumber1());
        viewHolder.kouketsuNum2.setText(items.get(i).getPublicAbsentNumber2());
    }

    public static class AttendanceRateViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView subjectName;
        public TextView unitNum;
        public TextView attendanceRate;
        public TextView attendanceNum;
        public TextView abcentNum;
        public TextView lateNum;
        public TextView kouketsuNum1;
        public TextView kouketsuNum2;

        public AttendanceRateViewHolder(View v) {
            super(v);
            subjectName = (TextView)v.findViewById(R.id.subject_text_view);
            unitNum = (TextView)v.findViewById(R.id.unit_num);
            attendanceRate = (TextView)v.findViewById(R.id.attendance_rate);
            attendanceNum = (TextView)v.findViewById(R.id.attendance_num);
            abcentNum = (TextView)v.findViewById(R.id.abcent_num);
            lateNum = (TextView)v.findViewById(R.id.late_num);
            kouketsuNum1 = (TextView)v.findViewById(R.id.kouketsu_num1);
            kouketsuNum2 = (TextView)v.findViewById(R.id.kouketsu_num2);
        }
    }
}