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
    private static final int REQUEST_CODE_EDIT_DING_DONG = 0x0010;

    /**
     * 欲編輯行程的id
     */
    private int taskId;

    /**
     * 相關scheduleId
     */
    private int scheduleId;

    /*
        Bo
     */
    private ItineraryBo itineraryBo;
    private SoundDingDongBo soundDingDongBo;

    /**
     * 任務要設定的音效id(存關聯檔時使用)
     */
    private Schedule scheduleSetupFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_edit_task);

        findViews();

        //新增狀態 什麼也沒帶過來
        //編輯 帶來 taskId
        this.taskId = getIntent().getIntExtra(GlobalNaming.TASK_ID, GlobalNaming.ERROR_CODE);

        this.itineraryBo = ItineraryBoImpl.getInstance(this);
        this.soundDingDongBo = SoundDingDongBoImpl.getInstance(this);

        if (this.taskId != 0) {
            Schedule keySchedule = new Schedule();
            keySchedule.setTaskId(this.taskId);
            Cursor cursorSchedule = soundDingDongBo.findScheduleList(keySchedule);

            if (cursorSchedule.getCount() == 1 && cursorSchedule.moveToFirst()) {
                this.scheduleId = cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("id"));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
            顯示內容
         */
        if (this.taskId == 0) { //若為新增狀態 清空所有欄位
            this.taskName.setText(GlobalNaming.SPACE);
            this.taskDate.setText(GlobalNaming.SPACE);
            this.taskTime.setText(GlobalNaming.SPACE);
            this.taskSite.setText(GlobalNaming.SPACE);
        } else { //若為編輯狀態 帶出所有的值並放進欄位
            Task task = itineraryBo.findTaskById(this.taskId);
            String dateAndTimeString = GlobalNaming.getDateFormat(task.getTm());
            String[] dateAndTimeArray = dateAndTimeString.split(GlobalNaming.SPACE);

            this.taskName.setText(task.getName());
            this.taskDate.setText(dateAndTimeArray[0]);
            this.taskTime.setText(dateAndTimeArray[1]);
            this.taskSite.setText(task.getSite());
        }

        /*
            設定音效按鍵的字樣
            若無設定 為"新增提醒"
            若有設定 為"編輯提醒"
         */
        if (this.scheduleId != 0) {
            createSchedule.setText("編輯或刪除提醒");
        } else if (this.scheduleId == 0) {
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
     * 存檔 xxx 這裡設定時間的方式很不優 待後續改進(如果有時間的話)
     *
     * @param view
     */
    private void saveTask(View view) {

        //時間資料格式處理
        String tm = taskDate.getText().toString() + taskTime.getText().toString();
        tm = tm.replace("-", "").replace(":", "");

        Task task = new Task();
        task.setName(taskName.getText().toString());
        task.setTm(tm);
        task.setSite(taskSite.getText().toString());

        long taskRowId = 0;
        if (this.taskId == 0) {
            taskRowId = itineraryBo.createTask(task);
        } else {
            task.setId(this.taskId);
            itineraryBo.modifyTask(task);
        }

        //test
        Log.d("新增行程id", String.valueOf(taskRowId));






        //如果有設定音效 要加入關係檔 xxx 這裡設計有誤
        if (scheduleSetupFeedback != null) {

            //內容已經有音效檔的id跟發射時間的設定
            scheduleSetupFeedback.setTaskId((int) taskRowId);
            long scheduleRowId = soundDingDongBo.createSchedule(scheduleSetupFeedback);

            //test
            Log.d("新增提醒設定id", String.valueOf(scheduleRowId));

            //清空暫存資料
            scheduleSetupFeedback = null;
        }

        //清空欄位並返回
        this.resetFields(view);
        this.backChoiceOutdoorAllOperation(view);
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
     * 新增行程與音效關聯檔 xxx
     *
     * @param view
     */
    private void createSchedule(View view) {

        if(this.scheduleId == 0){
            Intent intent = new Intent(this, CreateDingDongActivity.class);
            intent.putExtra(GlobalNaming.TASK_ID, this.taskId);
            startActivityForResult(intent, REQUEST_CODE_CREATE_DING_DONG);
        }else{
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
     * @param dialogInterface
     * @param which
     */
    private void editSchedule(DialogInterface dialogInterface, int which){
        Intent intent = new Intent(this, CreateDingDongActivity.class);
        intent.putExtra(GlobalNaming.SCHEDULE_ID, this.scheduleId);
        startActivityForResult(intent, REQUEST_CODE_EDIT_DING_DONG);
    }

    /**
     * 刪除提醒
     * @param dialogInterface
     * @param which
     */
    private void deleteSchedule(DialogInterface dialogInterface, int which){

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
                    Bundle bundle = data.getExtras();
                    scheduleSetupFeedback = (Schedule) bundle.get(GlobalNaming.SCHEDULE_OBJECT);
                }
                break;
            case REQUEST_CODE_EDIT_DING_DONG:
                if(resultCode == RESULT_OK){




                    //XXX
                }
                break;
        }
    }
}
