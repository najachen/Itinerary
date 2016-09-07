package silver.reminder.itinerary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.model.SoundFile;
import silver.reminder.itinerary.util.GlobalNaming;

public class AlarmActivity extends AppCompatActivity {

    /**
     * 音樂播放器
     */
    private MediaPlayer mediaPlayer;

    /**
     * 正在提醒的id
     */
    private int scheduleId;
    private SoundDingDongBo soundDingDongBo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundDingDongBo = SoundDingDongBoImpl.getInstance(this);

        Intent intent = getIntent();

        String taskName = intent.getStringExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_TASK_NAME);
        String taskSite = intent.getStringExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_TASK_SITE);
        int soundFileId = intent.getIntExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_SOUND_FILE_ID, GlobalNaming.ERROR_CODE);
        scheduleId = intent.getIntExtra(GlobalNaming.ALARM_INTENT_EXTRA_KEY_SCHEDULE_ID, GlobalNaming.ERROR_CODE);

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.timeFormat));
        String nowFormatted = simpleDateFormat.format(now);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.alertTaskTitle01) + taskName + getString(R.string.alertTaskTitle02))
                .setMessage(getString(R.string.siteLabel) + taskSite + "\n" + nowFormatted)
                .setNeutralButton(getString(R.string.sure), this::stopAlarm)
                .show();

        /*
            播放音樂
        */
        if (soundFileId != 0) {
            //音樂檔檔名

            SoundFile soundFile = soundDingDongBo.findSoundFileById(soundFileId);
            String soundFileName = soundFile != null ? soundFile.getFileName() : "";

            String musicFilePathInPublicDir = CreateSoundFileActivity.DIR_SILVER_REMINDER
                    + CreateSoundFileActivity.DIR_SILVER_REMINDER_STORE
                    + soundFileName;

            //Uri
            File publicMusicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File musicFile = new File(publicMusicDir, musicFilePathInPublicDir);
            Uri musicFileUri = Uri.fromFile(musicFile);

            mediaPlayer = MediaPlayer.create(this, musicFileUri);
            mediaPlayer.start();
        }
    }

    /**
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
//        soundDingDongBo.removeSchedule(this.scheduleId);
        //鬧鐘
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = GlobalNaming.getAlarmPendingIntent(this, this.scheduleId);
        alarmManager.cancel(pendingIntent);

        finish();
    }
}
