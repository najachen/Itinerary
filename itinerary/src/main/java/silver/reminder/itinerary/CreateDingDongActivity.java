package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;

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
        id
     */
    private int taskId;
    private int scheduleId;

    /*
        Bo
     */
    private SoundDingDongBo soundDingDongBo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.create_ding_dong);

        findViews();

        //新增 帶了taskId 但不保證有值 (新增行程就沒有值)
        //編輯 帶了scheduleId
        Intent intent = getIntent();
        this.taskId = intent.getIntExtra(GlobalNaming.TASK_ID, GlobalNaming.ERROR_CODE);
        this.scheduleId = intent.getIntExtra(GlobalNaming.SCHEDULE_ID, GlobalNaming.ERROR_CODE);

        soundDingDongBo = SoundDingDongBoImpl.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();



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
     * @param view
     */
    private void browseSoundFileSchedule(View view) {

    }

    /**
     * 儲存提醒
     * @param view
     */
    private void saveSchedule(View view) {

    }

    /**
     * 新增音效檔
     * @param view
     */
    private void createSoundFile(View view) {

    }

    /**
     * 回上一頁
     * @param view
     */
    private void backCreateOrEditTask(View view) {
        finish();
    }
}
