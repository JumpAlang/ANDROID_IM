package com.example.imapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.common.common.app.PresenterToolbarActivity;
import com.example.common.common.app.ToolbarActivity;
import com.example.factory.data.helper.TimeTableHelper;
import com.example.factory.model.card.SubjectCard;
import com.example.factory.presenter.timetable.TimeTableContract;
import com.example.factory.presenter.timetable.TimeTablePresent;
import com.example.imapp.R;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.listener.OnItemBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;

import java.util.ArrayList;
import java.util.List;

public class TimeTableActivity extends PresenterToolbarActivity<TimeTableContract.Presenter>
        implements TimeTableContract.View {
    public static final String TAG="TimeTableActivity";

    @BindView(R.id.id_weekview)
    WeekView mWeekView;

    @BindView(R.id.id_timetableView)
    TimetableView mTimetableView;

    @BindView(R.id.id_title)
    TextView titleTextView;

    private int target=-1;

    public static void show(Context context) {
        context.startActivity(new Intent(context,TimeTableActivity.class));
    }
    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化周选择控件
        initTime();
        setTitle("");
    }
    public void initTime(){
        mWeekView.curWeek(1)
                .callback(new IWeekView.OnWeekItemClickedListener() {
                    @Override
                    public void onWeekClicked(int week) {
                        int cur = mTimetableView.curWeek();
                        //更新切换后的日期，从当前周cur->切换的周week
                        mTimetableView.onDateBuildListener()
                                .onUpdateDate(cur, week);
                        mTimetableView.changeWeekOnly(week);
                    }
                })
                .callback(new IWeekView.OnWeekLeftClickedListener() {
                    @Override
                    public void onWeekLeftClicked() {
                        onWeekLeftLayoutClicked();
                    }
                })
                .isShow(false)//设置隐藏，默认显示
                .showView();

        mTimetableView.curWeek(1)
                .curTerm("大三下学期")
                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, List<Schedule> scheduleList) {
                        display(scheduleList);
                    }
                })
                .isShowNotCurWeek(false)
                .callback(new ISchedule.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View v, int day, int start) {
                        Toast.makeText(TimeTableActivity.this,
                                "长按:周" + day  + ",第" + start + "节",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .callback(new ISchedule.OnWeekChangedListener() {
                    @Override
                    public void onWeekChanged(int curWeek) {
                        titleTextView.setText("第" + curWeek + "周");
                    }
                })
                .showView();
    }
    protected void display(List<Schedule> beans) {
        String str = "";
        for (Schedule bean : beans) {
            str += bean.getName() + ","+bean.getWeekList().toString()+","+bean.getStart()+","+bean.getStep()+"\n";
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void initData() {
        super.initData();
        mPresenter.pullData();
    }

    @OnClick(R.id.title)
    public void swtchWeek(){
        Log.d(TAG, "swtchWeek: ");
        if (mWeekView.isShowing())
            hideWeekView();
        else
            showWeekView();
    }
    /**
     * 周次选择布局的左侧被点击时回调<br/>
     * 对话框修改当前周次
     */
    protected void onWeekLeftLayoutClicked() {
        final String items[] = new String[20];
        int itemCount = mWeekView.itemCount();
        for (int i = 0; i < itemCount; i++) {
            items[i] = "第" + (i + 1) + "周";
        }
//        target = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置当前周");
        builder.setSingleChoiceItems(items, mTimetableView.curWeek() - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        target = i;
                    }
                });
        builder.setPositiveButton("设置为当前周", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (target != -1) {
                    mWeekView.curWeek(target + 1).updateView();
                    mTimetableView.changeWeekForce(target + 1);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    public void hideWeekView(){
        mWeekView.isShow(false);
        titleTextView.setTextColor(getResources().getColor(R.color.app_course_textcolor_blue));
        int cur = mTimetableView.curWeek();
        mTimetableView.onDateBuildListener()
                .onUpdateDate(cur, cur);
        mTimetableView.changeWeekOnly(cur);
    }

    public void showWeekView(){
        mWeekView.isShow(true);
        titleTextView.setTextColor(getResources().getColor(R.color.app_red));
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
    public void updateSucceed(List<SubjectCard> subjectCards) {
        Log.d(TAG, "updateSucceed: ");
        mWeekView.source(subjectCards).showView();
        mTimetableView.source(subjectCards).updateView();
    }
}
