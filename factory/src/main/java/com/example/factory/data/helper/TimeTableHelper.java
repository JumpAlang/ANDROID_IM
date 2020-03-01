package com.example.factory.data.helper;
//import com.zhuangfei.timetable.model.ScheduleEnable;

public class TimeTableHelper {
//    public static List<Subject> parse(String parseString) {
//        List<Subject> courses = new ArrayList<>();
//        try {
//            JSONArray array = new JSONArray(parseString);
//            for(int i=0;i<array.length();i++){
//                JSONArray array2=array.getJSONArray(i);
//                String term=array2.getString(0);
//                String name=array2.getString(3);
//                String teacher=array2.getString(8);
//                if(array2.length()<=10){
//                    courses.add(new MySubject(term,name,null, teacher, null, -1, -1, -1, -1,null));
//                    continue;
//                }
//                String string=array2.getString(17);
//                if(string!=null){
//                    string=string.replaceAll("\\(.*?\\)", "");
//                }
//                String room=array2.getString(16)+string;
//                String weeks=array2.getString(11);
//                int day,start,step;
//                try {
//                    day=Integer.parseInt(array2.getString(12));
//                    start=Integer.parseInt(array2.getString(13));
//                    step=Integer.parseInt(array2.getString(14));
//                } catch (Exception e) {
//                    day=-1;
//                    start=-1;
//                    step=-1;
//                }
//                courses.add(new MySubject(term,name, room, teacher, getWeekList(weeks), start, step, day, -1,null));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return courses;
//    }
}
