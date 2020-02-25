package com.example.factory.model.card;

import android.app.Service;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程卡片，用于从网络获取课程信息
 */
public class SubjectCard implements ScheduleEnable {
    /**
     * 课程名
     */
    private String name;

    /**
     * 教室
     */
    private String room;

    /**
     * 教师
     */
    private String teacher;

    /**
     * 起始周
     */
    private int startWeek;

    /**
     * 上课周数
     */
    private int stepWeek;
    /**
     * 开始上课的节次
     */
    private int start;

    /**
     * 上课节数
     */
    private int step;

    /**
     * 周几上
     */
    private int day;

    /**
     *  一个随机数，用于对应课程的颜色
     */
    private int colorRandom;

    @Override
    public Schedule getSchedule() {
        Schedule schedule = new Schedule();
        schedule.setColorRandom(colorRandom);
        schedule.setDay(day);
        schedule.setName(name);
        schedule.setRoom(room);
        schedule.setStep(step);
        schedule.setStart(start);
        schedule.setTeacher(teacher);
        List<Integer> weekList=new ArrayList<>();
        for (int i = startWeek; i < stepWeek; i++) {
            weekList.add(i);
        }
        schedule.setWeekList(weekList);
        return new Schedule();
    }
}
