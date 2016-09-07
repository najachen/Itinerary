package silver.reminder.itinerary.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.model.Schedule;
import silver.reminder.itinerary.util.GlobalNaming;

/**
 * 系統重新開機時
 * 重新註冊所有鬧鐘
 * 資料從schedule資料表抓取
 */
public class ReConfigReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        SoundDingDongBo soundDingDongBo = SoundDingDongBoImpl.getInstance(context);
        Cursor cursorSchedule = soundDingDongBo.findScheduleList(new Schedule());

        while(cursorSchedule.moveToNext()){
            int _id = cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("_id"));
            long time = cursorSchedule.getLong(cursorSchedule.getColumnIndexOrThrow("time"));

            PendingIntent pendingIntent = GlobalNaming.getAlarmPendingIntent(context, _id);
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }
}
