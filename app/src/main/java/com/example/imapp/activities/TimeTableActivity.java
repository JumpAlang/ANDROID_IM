package com.example.imapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.common.common.app.PresenterToolbarActivity;
import com.example.common.common.app.ToolbarActivity;
import com.example.factory.presenter.timetable.TimeTableContract;
import com.example.factory.presenter.timetable.TimeTablePresent;
import com.example.imapp.R;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;

import java.util.ArrayList;
import java.util.List;

public class TimeTableActivity extends PresenterToolbarActivity<TimeTableContract.Presenter>
        implements TimeTableContract.View {

    @BindView(R.id.id_weekview)
    WeekView mWeekView;

    @BindView(R.id.id_timetableView)
    TimetableView mTimetableView;

    public static void show(Context context) {
        context.startActivity(new Intent(context,TimeTableActivity.class));
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化周选择控件
        mWeekView.curWeek(1)
                .showView();
        //初始化主视图
        mTimetableView.curWeek(1)
                .curTerm("大三下学期")
                .maxSlideItem(10)
                .monthWidthDp(30)
                .showView();
        setTitle("课程表");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_time_table;
    }

    @Override
    protected TimeTablePresent initPresenter() {
        return new TimeTablePresent(this);
    }

    @Override
    public void setPresenter(TimeTableContract.Presenter presenter) {
        this.mPresenter=presenter;
    }

    @Override
    public void showError(int str) {
        super.showError(str);
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void updateSucceed() {

    }
}
