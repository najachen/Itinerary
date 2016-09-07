package silver.reminder.itinerary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.model.Schedule;
import silver.reminder.itinerary.model.SoundFile;
import silver.reminder.itinerary.model.Task;
import silver.reminder.itinerary.util.GlobalNaming;

/**
 * Created by hsuan on 2016/8/27.
 */
public class CreateDingDongActivity extends AppCompatActivity {

    private Button backCreateOrEditTask;
    private Button createSoundFile;
    private Button saveSchedule;
    private Button browseSoundFileSchedule;
    private EditText dateSchedule;
    private EditText timeSchedule;
    private EditText soundFilePathSchedule;
    private TextView siteDingDong;

    /*
       請求碼
     */
    private static final int REQUEST_CODE_SELECT_SOUND_FILE = 0x0001;

    /*
        頁面物件
     */
    private Task task;
    private Schedule schedule;
    private SoundFile soundFile;
    private SoundFile soundFileOnActivityResult;

    /*
        Bo
     */
    private ItineraryBo itineraryBo;
    private SoundDingDongBo soundDingDongBo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_ding_dong);

        findViews();

        itineraryBo = ItineraryBoImpl.getInstance(this);
        soundDingDongBo = SoundDingDongBoImpl.getInstance(this);

        soundFileOnActivityResult = new SoundFile();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
            準備頁面物件
         */
        int taskId = getIntent().getIntExtra(GlobalNaming.TASK_ID, GlobalNaming.ERROR_CODE);

        //
        task = itineraryBo.findTaskById(taskId);
        task = task == null ? new Task() : task;

        //
        schedule = new Schedule();

        Schedule keySchedule = new Schedule();
        keySchedule.setTaskId(taskId);
        Cursor cursorSchedule = soundDingDongBo.findScheduleList(keySchedule);

        if (cursorSchedule.getCount() == 1 && cursorSchedule.moveToFirst()) {
            schedule.set_id(cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("_id")));
            schedule.setTaskId(cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("taskId")));
            schedule.setSoundFileId(cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("soundFileId")));
            schedule.setTime(cursorSchedule.getLong(cursorSchedule.getColumnIndexOrThrow("time")));
        }

        //
        Integer soundFileId = schedule.getSoundFileId() == null ? new Integer(0) : schedule.getSoundFileId();
        soundFile = soundDingDongBo.findSoundFileById(soundFileId);
        soundFile = soundFile == null ? new SoundFile() : soundFile;

        /*
            顯示內容
         */
        Long scheduleTime = schedule.getTime();
        if (scheduleTime != null && scheduleTime > 0) {

            Calendar calendarSchedule = Calendar.getInstance();
            calendarSchedule.setTimeInMillis(schedule.getTime());

            dateSchedule.setText(GlobalNaming.getDateString(calendarSchedule));
            timeSchedule.setText(GlobalNaming.getTimeString(calendarSchedule));
        }

        String soundFileName = soundFile.getFileName();
        if (soundFileName != null && soundFileName.length() > 0) {
            soundFilePathSchedule.setText(soundFileName);
        }

        String taskSite = task.getSite();
        if (taskSite != null && taskSite.length() > 0) {
            siteDingDong.setText(task.getSite());
        }
    }

    private void findViews() {
        backCreateOrEditTask = (Button) findViewById(R.id.backCreateOrEditTask);
        backCreateOrEditTask.setOnClickListener(this::backCreateOrEditTask);
        createSoundFile = (Button) findViewById(R.id.createSoundFile);
        createSoundFile.setOnClickListener(this::createSoundFile);
        saveSchedule = (Button) findViewById(R.id.saveSchedule);
        saveSchedule.setOnClickListener(this::saveSchedule);
        browseSoundFileSchedule = (Button) findViewById(R.id.browseSoundFileSchedule);
        browseSoundFileSchedule.setOnClickListener(this::browseSoundFileSchedule);

        dateSchedule = (EditText) findViewById(R.id.dateSchedule);
        timeSchedule = (EditText) findViewById(R.id.timeSchedule);
        soundFilePathSchedule = (EditText) findViewById(R.id.soundFilePathSchedule);

        siteDingDong = (TextView) findViewById(R.id.siteDingDong);
    }

    /**
     * 瀏覽音效檔清單
     *
     * @param view
     */
    private void browseSoundFileSchedule(View view) {
        Intent intent = new Intent(this, ListSoundFileActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SOUND_FILE);
    }

    /**
     * 儲存提醒
     *
     * @param view
     */
    private void saveSchedule(View view) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Schedule scheduleSave = new Schedule();

        //檢查欄位是否都有填
        String dateSchedule = this.dateSchedule.getText().toString();
        if (dateSchedule == null || dateSchedule.length() == 0) {
            Snackbar.make(view, R.string.dateNotFill, Snackbar.LENGTH_SHORT).show();
            return;
        }
        String timeSchedule = this.timeSchedule.getText().toString();
        if (timeSchedule == null || timeSchedule.length() == 0) {
            Snackbar.make(view, R.string.timeNotFill, Snackbar.LENGTH_SHORT).show();
            return;
        }
        String clean14TimeString = GlobalNaming.cleanTimeString(dateSchedule + timeSchedule);
        Calendar timeCal = GlobalNaming.getCalendar(clean14TimeString);
        scheduleSave.setTime(timeCal.getTimeInMillis());


        Integer soundFileId = soundFile.get_id();
        soundFileId = soundFileId != null && soundFileId > 0 ? soundFileId : 0;
        Integer soundFileIdOnActivityResult = soundFileOnActivityResult.get_id();
        soundFileIdOnActivityResult = soundFileIdOnActivityResult != null && soundFileIdOnActivityResult > 0 ? soundFileIdOnActivityResult : 0;

        if (soundFileId == 0 && soundFileIdOnActivityResult == 0) {
            Snackbar.make(view, R.string.soundFileNotChoice, Snackbar.LENGTH_SHORT).show();
            return;
        }

        //soundFileIdOnActivityResult higher 優先
        if (soundFileIdOnActivityResult > 0) {
            scheduleSave.setSoundFileId(soundFileIdOnActivityResult);
        } else {
            scheduleSave.setSoundFileId(soundFileId);
        }

        //test-
        Log.d("time", String.valueOf(timeCal.getTimeInMillis()));
        Log.d("soundFileIdOnResult", String.valueOf(soundFileIdOnActivityResult));
        Log.d("soundFileId", String.valueOf(soundFileId));

        /*
            先看 schedule本身有無id
                有 則一定有taskid -> 更新schedule
                無 看有無 task id
                    有 -> 新增schedule
                    無 -> 帶回上頁 與task一起儲存
         */
        Integer scheduleId = schedule.get_id();
        if (scheduleId != null && scheduleId > 0) {

            //test-
            Log.d("scheduleId", String.valueOf(scheduleId));

            scheduleSave.set_id(scheduleId);
            scheduleSave.setTaskId(task.get_id());

            int modRowCount = soundDingDongBo.modifySchedule(scheduleSave);

            /*
                更新鬧鐘
             */
            //取消原有的
            PendingIntent pendingIntent = GlobalNaming.getAlarmPendingIntent(this, scheduleId);
            alarmManager.cancel(pendingIntent);
            //建立新的
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(scheduleSave.getTime());
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        } else {

            Integer taskId = task.get_id();
            if (taskId != null && taskId > 0) {

                scheduleSave.setTaskId(taskId);
                long rowId = soundDingDongBo.createSchedule(scheduleSave);

                /*
                    新增鬧鐘
                 */
                PendingIntent pendingIntent = GlobalNaming.getAlarmPendingIntent(this, (int) rowId);
                Calendar tmCal = Calendar.getInstance();
                tmCal.setTimeInMillis(scheduleSave.getTime());
                alarmManager.set(AlarmManager.RTC_WAKEUP, tmCal.getTimeInMillis(), pendingIntent);

            } else {
                Intent intent = new Intent();
                intent.putExtra(GlobalNaming.SCHEDULE_FIELD_TO_SAVE_TM, scheduleSave.getTime());
                intent.putExtra(GlobalNaming.SCHEDULE_FIELD_TO_SAVE_SOUND_FILE_ID, scheduleSave.getSoundFileId());
                setResult(RESULT_OK, intent);
            }
        }

        backCreateOrEditTask(view);
    }

    /**
     * 新增音效檔
     *
     * @param view
     */
    private void createSoundFile(View view) {
        Intent intent = new Intent(this, CreateSoundFileActivity.class);
        startActivity(intent);
    }

    /**
     * 回上一頁
     *
     * @param view
     */
    private void backCreateOrEditTask(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SELECT_SOUND_FILE:
                if (resultCode == RESULT_OK) {

                    int soundFileId = data.getIntExtra(GlobalNaming.SOUND_FILE_ID, GlobalNaming.ERROR_CODE);
                    soundFileOnActivityResult.set_id(soundFileId);
//                    soundFile.set_id(soundFileId);

                    String soundFileName = data.getStringExtra(GlobalNaming.SOUND_FILE_NAME);
                    soundFileOnActivityResult.setFileName(soundFileName);
//                    soundFile.setFileName(soundFileName);

                    //test-
                    Log.d("soundFileId", String.valueOf(soundFileId));

                    //update view
                    soundFilePathSchedule.setText(soundFileName);
                }
                break;
            default:
        }
    }
}
