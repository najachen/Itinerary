package silver.reminder.itinerary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import silver.reminder.itinerary.AlarmActivity;
import silver.reminder.itinerary.util.GlobalNaming;

/**
 * 收到鬧鐘intent要做的事
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (GlobalNaming.INTENT_ACTION_NAME_ALARM_RECEIVER.equals(intent.getAction())) {

            String taskName = intent.getStringExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_TASK_NAME);
            String taskSite = intent.getStringExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_TASK_SITE);
            int soundFileId = intent.getIntExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_SOUND_FILE_ID, GlobalNaming.ERROR_CODE);
            int scheduleId = intent.getIntExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_SCHEDULE_ID, GlobalNaming.ERROR_CODE);

            Intent intentAlarm = new Intent(context, AlarmActivity.class);
            intentAlarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intentAlarm.putExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_TASK_NAME, taskName);
            intentAlarm.putExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_TASK_SITE, taskSite);
            intentAlarm.putExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_SOUND_FILE_ID, soundFileId);
            intentAlarm.putExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_SCHEDULE_ID, scheduleId);

            context.startActivity(intentAlarm);
        }
    }
}
