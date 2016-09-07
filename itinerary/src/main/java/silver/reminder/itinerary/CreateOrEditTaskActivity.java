package silver.reminder.itinerary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.model.Schedule;
import silver.reminder.itinerary.model.Task;
import silver.reminder.itinerary.util.GlobalNaming;

/**
 * Created by Administrator on 2016/8/22.
 */
public class CreateOrEditTaskActivity extends AppCompatActivity {

    private EditText taskName;
    private EditText taskDate;
    private EditText taskTime;
    private EditText taskSite;
    private Button backChoiceOutdoorAllOperation;

    private Button createSchedule;
    private Button resetFields;
    private Button saveTask;

    /*
        請求碼
     */
    private static final int REQUEST_CODE_CREATE_DING_DONG = 0x0001;

    /**
     * 頁面物件
     */
    private Task task;
    private Schedule schedule;

    /**
     *
     */
    Schedule scheduleOnActivityResult;

    /*
        Bo
     */
    private ItineraryBo itineraryBo;
    private SoundDingDongBo soundDingDongBo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_edit_task);

        findViews();

        itineraryBo = ItineraryBoImpl.getInstance(this);
        soundDingDongBo = SoundDingDongBoImpl.getInstance(this);

        scheduleOnActivityResult = new Schedule();
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

        /*
            顯示內容
         */
        if (task.get_id() == null || task.get_id() == 0) { //若為新增狀態 清空所有欄位
            taskName.setText(GlobalNaming.SPACE);
            taskDate.setText(GlobalNaming.SPACE);
            taskTime.setText(GlobalNaming.SPACE);
            taskSite.setText(GlobalNaming.SPACE);
        } else { //若為編輯狀態 帶出所有的值並放進欄位

            Calendar taskCal = Calendar.getInstance();
            taskCal.setTimeInMillis(task.getTime());

            taskName.setText(task.getName());
            taskDate.setText(GlobalNaming.getDateString(taskCal));
            taskTime.setText(GlobalNaming.getTimeString(taskCal));
            taskSite.setText(task.getSite());
        }

        /*
            設定音效按鍵的字樣
            若無設定 為"新增提醒"
            若有設定 為"編輯提醒"
         */
        if (schedule.get_id() != null && schedule.get_id() > 0) {
            createSchedule.setText(R.string.editOrDeleteReminder);
        } else if (schedule.get_id() == null || schedule.get_id() == 0) {
            createSchedule.setText(R.string.createReminder);
        }
    }

    private void findViews() {

        backChoiceOutdoorAllOperation = (Button) findViewById(R.id.backChoiceOutdoorAllOperation);
        backChoiceOutdoorAllOperation.setOnClickListener(this::backChoiceOutdoorAllOperation);
        createSchedule = (Button) findViewById(R.id.createSchedule);
        createSchedule.setOnClickListener(this::createSchedule);
        resetFields = (Button) findViewById(R.id.resetFields);
        resetFields.setOnClickListener(this::resetFields);
        saveTask = (Button) findViewById(R.id.saveTask);
        saveTask.setOnClickListener(this::saveTask);

        taskName = (EditText) findViewById(R.id.taskName);
        taskSite = (EditText) findViewById(R.id.taskSite);
        taskDate = (EditText) findViewById(R.id.taskDate);
        taskTime = (EditText) findViewById(R.id.taskTime);
    }

    /**
     * 存檔 這裡設定時間的方式很不優 待後續改進(如果有時間的話)
     *
     * @param view
     */
    private void saveTask(View view) {

        //時間資料格式處理
        String tm = taskDate.getText().toString() + taskTime.getText().toString();
        tm = GlobalNaming.cleanTimeString(tm);

        if (tm.length() != 14) {
            Snackbar.make(view, getString(R.string.timeFormatError) + GlobalNaming.SPACE + tm, Snackbar.LENGTH_SHORT).show();
            return;
        }

        Task taskSave = new Task();
        taskSave.setName(taskName.getText().toString());
        taskSave.setTime(GlobalNaming.getCalendar(tm).getTimeInMillis());
        taskSave.setSite(taskSite.getText().toString());

        long taskRowId = 0;
        if (task.get_id() == null || task.get_id() == 0) {
            taskRowId = itineraryBo.createTask(taskSave);
            taskSave.set_id((int) taskRowId);
        } else {
            taskSave.set_id(task.get_id());
            itineraryBo.modifyTask(taskSave);
        }

        //test-
        Log.d("新增行程id", String.valueOf(taskRowId));

        //如果有設定提醒 要存檔
        boolean isHaveNoTaskId = task.get_id() == null || task.get_id() == 0;
        boolean isHaveScheduleTm =
                scheduleOnActivityResult.getTime() != null &&
                scheduleOnActivityResult.getTime() > 0 &&
                taskSave.getTime() != null &&
                taskSave.getTime() > 0;
        boolean isHaveScheduleSoundFileId =
                scheduleOnActivityResult.getSoundFileId() != null &&
                scheduleOnActivityResult.getSoundFileId() > 0;

        if (isHaveNoTaskId && isHaveScheduleTm && isHaveScheduleSoundFileId) {

            Schedule scheduleSave = new Schedule();
            scheduleSave.setTime(scheduleOnActivityResult.getTime());
            scheduleSave.setTaskId(taskSave.get_id());
            scheduleSave.setSoundFileId(scheduleOnActivityResult.getSoundFileId());

            long scheduleRowId = soundDingDongBo.createSchedule(scheduleSave);
            scheduleSave.set_id((int) scheduleRowId);

            /*
                新增鬧鐘
             */
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            //manifest.xml 設定的 action name 是
            //silver.reminder.itinerary.util.GlobalNaming.ALARM_RECEIVER_INTENT_ACTION_NAME

            //test-
            Log.d("GlobalNaming action - ", GlobalNaming.INTENT_ACTION_NAME_ALARM_RECEIVER);

            Intent intent = new Intent();
            intent.setAction(GlobalNaming.INTENT_ACTION_NAME_ALARM_RECEIVER);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) scheduleRowId, intent, PendingIntent.FLAG_ONE_SHOT);

            Calendar tmCal = Calendar.getInstance();
            tmCal.setTimeInMillis(scheduleSave.getTime());
            alarmManager.set(AlarmManager.RTC_WAKEUP, tmCal.getTimeInMillis(), pendingIntent);

            //test-
            Log.d("新增提醒設定id", String.valueOf(scheduleRowId));
        }

        //清空欄位並返回
        resetFields(view);
        backChoiceOutdoorAllOperation(view);
    }

    /**
     * 清空欄位
     *
     * @param view
     */
    private void resetFields(View view) {
        this.taskName.setText(GlobalNaming.SPACE);
        this.taskDate.setText(GlobalNaming.SPACE);
        this.taskTime.setText(GlobalNaming.SPACE);
        this.taskSite.setText(GlobalNaming.SPACE);
    }

    /**
     * 新增或編輯提醒
     *
     * @param view
     */
    private void createSchedule(View view) {

        if (schedule.get_id() == null || schedule.get_id() == 0) {
            Intent intent = new Intent(this, CreateDingDongActivity.class);
            intent.putExtra(GlobalNaming.TASK_ID, task.get_id());
            startActivityForResult(intent, REQUEST_CODE_CREATE_DING_DONG);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.queEditOrDelete))
                    .setPositiveButton(getString(R.string.edit), CreateOrEditTaskActivity.this::editSchedule)
                    .setNegativeButton(getString(R.string.delete), CreateOrEditTaskActivity.this::deleteSchedule)
                    .setNeutralButton(getString(R.string.cancel), null)
                    .show();
        }
    }

    /**
     * 編輯提醒
     *
     * @param dialogInterface
     * @param which
     */
    private void editSchedule(DialogInterface dialogInterface, int which) {
        Intent intent = new Intent(this, CreateDingDongActivity.class);
        intent.putExtra(GlobalNaming.TASK_ID, task.get_id());
        startActivity(intent);
    }

    /**
     * 刪除提醒
     *
     * @param dialogInterface
     * @param which
     */
    private void deleteSchedule(DialogInterface dialogInterface, int which) {
        soundDingDongBo.removeSchedule(schedule.get_id());

        //刪除鬧鐘
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = GlobalNaming.getAlarmPendingIntent(this, schedule.get_id());
        alarmManager.cancel(pendingIntent);
    }

    /**
     * 回上一頁
     *
     * @param view
     */
    private void backChoiceOutdoorAllOperation(View view) {
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_CREATE_DING_DONG:
                if (resultCode == RESULT_OK) {
                    Long time = data.getLongExtra(GlobalNaming.SCHEDULE_FIELD_TO_SAVE_TM, GlobalNaming.ERROR_CODE);
                    Integer soundFileId = data.getIntExtra(GlobalNaming.SCHEDULE_FIELD_TO_SAVE_SOUND_FILE_ID, GlobalNaming.ERROR_CODE);

                    if (time != null && time > 0 && soundFileId != null && soundFileId > 0) {
                        scheduleOnActivityResult.setTime(time);
                        scheduleOnActivityResult.setSoundFileId(soundFileId);
                    }
                }
                break;
        }
    }
}
