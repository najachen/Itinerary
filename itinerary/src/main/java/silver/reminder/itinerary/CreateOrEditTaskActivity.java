package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.javabean.Schedule;
import silver.reminder.itinerary.javabean.Task;

/**
 * Created by Administrator on 2016/8/22.
 */
public class CreateOrEditTaskActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATE_DING_DONG = 0x0001;

    private EditText taskName;
    private EditText taskDate;
    private EditText taskTime;
    private EditText taskSite;

    private Button backChoiceOutdoorAllOperation;
    private Button createDingDong;
    private Button resetFields;
    private Button saveTask;

    /**
     * 任務要設定的音效id(存關聯檔時使用)
     */
    private Schedule scheduleSetupFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_edit_task);

        findViews();
    }

    private void findViews() {

        backChoiceOutdoorAllOperation = (Button) findViewById(R.id.backChoiceOutdoorAllOperation);
        backChoiceOutdoorAllOperation.setOnClickListener(this::backChoiceOutdoorAllOperation);
        createDingDong = (Button) findViewById(R.id.createDingDong);
        createDingDong.setOnClickListener(this::createDingDong);
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
     * 存檔
     *
     * @param view
     */
    private void saveTask(View view) {

        Task task = new Task();
        task.setName(taskName.getText().toString());
        task.setTm(taskDate.getText().toString() + taskTime.getText().toString());
        task.setSite(taskSite.getText().toString());

        ItineraryBo itineraryBo = ItineraryBoImpl.getInstance(this);
        long taskRowId = itineraryBo.createTask(task);

        //test
        Log.d("新增行程id", String.valueOf(taskRowId));

        //如果有設定音效 要加入關係檔
        if (scheduleSetupFeedback != null) {

            //內容已經有音效檔的id跟發射時間的設定
            scheduleSetupFeedback.setTaskId((int) taskRowId);

            SoundDingDongBo soundDingDongBo = SoundDingDongBoImpl.getInstance(this);
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
     * 新增行程與音效關聯檔
     *
     * @param view
     */
    private void createDingDong(View view) {
        Intent intent = new Intent(this, CreateDingDongActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE_DING_DONG);
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
                    scheduleSetupFeedback = (Schedule) bundle.get(GlobalNaming.SCHEDULE_SETUP_FEEDBACK);
                }
                break;
        }
    }
}
