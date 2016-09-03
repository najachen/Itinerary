package silver.reminder.itinerary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.model.Schedule;
import silver.reminder.itinerary.model.Task;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
            準備頁面物件
         */
        int taskId =  getIntent().getIntExtra(GlobalNaming.TASK_ID, GlobalNaming.ERROR_CODE);

        //
        task = itineraryBo.findTaskById(taskId);
        task = task == null ? new Task() : task;

        //
        schedule = new Schedule();

        Schedule keySchedule = new Schedule();
        keySchedule.setTaskId(taskId);
        Cursor cursorSchedule = soundDingDongBo.findScheduleList(keySchedule);

        if (cursorSchedule.getCount() == 1 && cursorSchedule.moveToFirst()) {
            schedule.setId(cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("id")));
            schedule.setTaskId(cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("taskId")));
            schedule.setSoundFileId(cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("soundFileId")));
            schedule.setTm(cursorSchedule.getString(cursorSchedule.getColumnIndexOrThrow("tm")));
        }

        /*
            顯示內容
         */
        if (task.getId() == 0) { //若為新增狀態 清空所有欄位
            taskName.setText(GlobalNaming.SPACE);
            taskDate.setText(GlobalNaming.SPACE);
            taskTime.setText(GlobalNaming.SPACE);
            taskSite.setText(GlobalNaming.SPACE);
        } else { //若為編輯狀態 帶出所有的值並放進欄位

            String dateAndTimeString = GlobalNaming.getDateFormat(task.getTm());
            String[] dateAndTimeArray = dateAndTimeString.split(GlobalNaming.SPACE);

            taskName.setText(task.getName());
            taskDate.setText(dateAndTimeArray[0]);
            taskTime.setText(dateAndTimeArray[1]);
            taskSite.setText(task.getSite());
        }

        /*
            設定音效按鍵的字樣
            若無設定 為"新增提醒"
            若有設定 為"編輯提醒"
         */
        if (schedule.getId() != 0) {
            createSchedule.setText("編輯或刪除提醒");
        } else if (schedule.getId() == 0) {
            createSchedule.setText("新增提醒");
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
        tm = GlobalNaming.cleanSpecCharTo14DigiCode(tm);

        Task taskSave = new Task();
        taskSave.setName(taskName.getText().toString());
        taskSave.setTm(tm);
        taskSave.setSite(taskSite.getText().toString());

        long taskRowId = 0;
        if (task.getId() == 0) {
            taskRowId = itineraryBo.createTask(taskSave);
        } else {
            taskSave.setId(task.getId());
            itineraryBo.modifyTask(taskSave);
        }

        //test
        Log.d("新增行程id", String.valueOf(taskRowId));

        //如果有設定提醒 要存檔
        boolean isHaveNoTaskId = task.getId() == null || task.getId() == 0;
        boolean isHaveScheduleTm = schedule.getTm() != null && task.getTm().length() > 0;
        boolean isHaveScheduleSoundFileId = schedule.getSoundFileId() != null && schedule.getSoundFileId() > 0;
        if (isHaveNoTaskId && isHaveScheduleTm && isHaveScheduleSoundFileId) {

            //內容已經有音效檔的id跟發射時間的設定
            schedule.setTaskId((int) taskRowId);
            long scheduleRowId = soundDingDongBo.createSchedule(schedule);

            //test
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

        if (this.schedule.getId() == 0) {
            Intent intent = new Intent(this, CreateDingDongActivity.class);
            intent.putExtra(GlobalNaming.TASK_ID, task.getId());
            startActivityForResult(intent, REQUEST_CODE_CREATE_DING_DONG);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("編輯或刪除提醒??")
                    .setPositiveButton("編輯", CreateOrEditTaskActivity.this::editSchedule)
                    .setNegativeButton("刪除", CreateOrEditTaskActivity.this::deleteSchedule)
                    .setNegativeButton("取消", null)
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
        intent.putExtra(GlobalNaming.TASK_ID, task.getId());
        startActivity(intent);
    }

    /**
     * 刪除提醒
     *
     * @param dialogInterface
     * @param which
     */
    private void deleteSchedule(DialogInterface dialogInterface, int which) {
        soundDingDongBo.removeSchedule(schedule.getId());
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
                    String tm = data.getStringExtra(GlobalNaming.SCHEDULE_FIELD_TO_SAVE_TM);
                    Integer soundFileId = data.getIntExtra(GlobalNaming.SCHEDULE_FIELD_TO_SAVE_SOUND_FILE_ID, GlobalNaming.ERROR_CODE);

                    if (tm != null && tm.length() > 0 && soundFileId != null && soundFileId > 0) {
                        schedule.setTm(tm);
                        schedule.setSoundFileId(soundFileId);
                    }
                }
                break;
        }
    }
}
