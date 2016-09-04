package silver.reminder.itinerary.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import silver.reminder.itinerary.CreateSoundFileActivity;
import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.model.SoundFile;
import silver.reminder.itinerary.util.GlobalNaming;

/**
 * 收到鬧鐘intent要做的事
 */
public class AlarmReceiver extends BroadcastReceiver {

    /**
     * 音樂播放器
     */
    private MediaPlayer mediaPlayer;

    /**
     * 正在提醒的id
     */
    private int scheduleId;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (GlobalNaming.INTENT_ACTION_NAME_ALARM_RECEIVER.equals(intent.getAction())) {

            String taskName = intent.getStringExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_TASK_NAME);
            String taskSite = intent.getStringExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_TASK_SITE);
            int soundFileId = intent.getIntExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_SOUND_FILE_ID, GlobalNaming.ERROR_CODE);
            scheduleId = intent.getIntExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_SCHEDULE_ID, GlobalNaming.ERROR_CODE);

            Date now = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("現在時間 hh 點 mm 分 ss 秒");
            String nowFormatted = simpleDateFormat.format(now);

            new AlertDialog.Builder(context)
                    .setTitle("行程 - " + taskName + " 提醒訊息!!")
                    .setMessage("地點 : " + taskSite + "\n" + nowFormatted)
                    .setNeutralButton("確定", AlarmReceiver.this::stopAlarm)
                    .show();

            /*
                播放音樂
             */
            if (soundFileId != 0) {
                //音樂檔檔名
                SoundDingDongBo soundDingDongBo = SoundDingDongBoImpl.getInstance(context);
                SoundFile soundFile = soundDingDongBo.findSoundFileById(soundFileId);
                String soundFileName = soundFile != null ? soundFile.getFileName() : "";

                String musicFilePathInPublicDir = CreateSoundFileActivity.DIR_SILVER_REMINDER
                        + CreateSoundFileActivity.DIR_SILVER_REMINDER_STORE
                        + soundFileName;
                //Uri
                File publicMusicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                File musicFile = new File(publicMusicDir, musicFilePathInPublicDir);
                Uri musicFileUri = Uri.fromFile(musicFile);

                mediaPlayer = MediaPlayer.create(context, musicFileUri);
                mediaPlayer.start();
            }
        }
    }

    /**
     * 確認已獲知提醒
     *
     * @param dialogInterface
     * @param which
     */
    private void stopAlarm(DialogInterface dialogInterface, int which) {

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

        /**
         * 刪除提醒與取消鬧鐘
         */
        //db
        SoundDingDongBo soundDingDongBo = SoundDingDongBoImpl.getInstance(this.context);
        soundDingDongBo.removeSchedule(this.scheduleId);
        //鬧鐘
        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = GlobalNaming.getAlarmPendingIntent(this.context, this.scheduleId);
        alarmManager.cancel(pendingIntent);
    }
}
