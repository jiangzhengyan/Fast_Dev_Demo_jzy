package com.tencent.customdialog_up_to_down;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout ll_pop_data;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TextView tv_pop_date_sure;
    private TextView tv_pop_date_cancel;
    private TextView qq_1;
    private TextView qq_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qq_1 = (TextView) findViewById(R.id.qq_1);
        qq_2 = (TextView) findViewById(R.id.qq_2);

        qq_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(1);
            }
        });
        qq_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(2);
            }
        });


    }


    private String which = "start_time";
    private long startTime = 0;
    private long endTime = 0;

    private void showDateDialog(int type) {
        if (type == 1)
            which = "start_time";
        else
            which = "end_time";
        final CustomDialog customDialog = new CustomDialog(this, R.style.Theme_Light_Dialog_Down_To_Up);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_pop_date_sure:
                        String dateAndTime = UserUtils.getDateAndTime();
                        long selectTimeMillis_ = UserUtils.formatDateToTimeMillis(dateAndTime, "yyyy-MM-dd HH:mm");
                        long todayTimeMillis_ = UserUtils.getTodayTimeMillis();
                        Log.e(TAG, " todayTimeMillis_: "+todayTimeMillis_ );
                        Log.e(TAG, "selectTimeMillis_: "+selectTimeMillis_ );
                        //确定
                        if ("start_time".equals(which)) {
                            if (selectTimeMillis_ < todayTimeMillis_) {
                                startTime = selectTimeMillis_;
                                Toast.makeText(MainActivity.this, "选择的开始时间已过去", Toast.LENGTH_SHORT).show();
                                qq_1.setText(UserUtils.getDateAndTime());
                                customDialog.dismiss();
                            }
                            if (endTime != 0) {
                                if (selectTimeMillis_ > endTime) {
                                    Toast.makeText(MainActivity.this, "选择的开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
                                } else {
                                    startTime = selectTimeMillis_;
                                    qq_1.setText(UserUtils.getDateAndTime());
                                    customDialog.dismiss();
                                }
                            } else {
                                startTime = selectTimeMillis_;
                                qq_1.setText(UserUtils.getDateAndTime());
                                customDialog.dismiss();
                            }
                        } else {
                            if (startTime != 0) {
                                if (selectTimeMillis_ < startTime) {
                                    Toast.makeText(MainActivity.this, "选择的结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
                                }else {
                                    endTime = selectTimeMillis_;
                                    qq_2.setText(UserUtils.getDateAndTime());
                                    customDialog.dismiss();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "请先选择开始时间", Toast.LENGTH_SHORT).show();
                            }

                        }
                        break;
                    case R.id.tv_pop_date_cancel:
                        //取消
                        customDialog.dismiss();

                        break;
                }
            }
        };

        View v = View.inflate(this, R.layout.dialog_date_picker, null);
        ll_pop_data = (LinearLayout) v.findViewById(R.id.ll_pop_data_time);
        datePicker = (DatePicker) v.findViewById(R.id.datepicker_time);//DatePicker
        timePicker = (TimePicker) v.findViewById(R.id.timepicker_time);//TimePicker
        tv_pop_date_sure = (TextView) v.findViewById(R.id.tv_pop_date_sure);//确定
        tv_pop_date_cancel = (TextView) v.findViewById(R.id.tv_pop_date_cancel);//取消
        timePicker.setIs24HourView(true);
        //设置监听
        ll_pop_data.setOnClickListener(clickListener);
        tv_pop_date_cancel.setOnClickListener(clickListener);
        tv_pop_date_sure.setOnClickListener(clickListener);
        UserUtils.setDateAndTimeListen(datePicker, timePicker);
        customDialog.setContentView(v);
        customDialog.show();
    }


}
