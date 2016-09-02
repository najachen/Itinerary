package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.model.Task;
import silver.reminder.itinerary.util.TableSchemaSetSpec;

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
        id
     */
    private int taskId;
    private int scheduleId;

    /*
        Bo
     */
    private ItineraryBo itineraryBo;
    private SoundDingDongBo soundDingDongBo;

    /**
     * 所選的音效檔id fileName
     */
    private int soundFileId;
    private String soundFileName;

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

        this.itineraryBo = ItineraryBoImpl.getInstance(this);
        this.soundDingDongBo = SoundDingDongBoImpl.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(this.taskId == 0){
            siteDingDong.setText(GlobalNaming.SPACE);
        }else{
            Task task = this.itineraryBo.findTaskById(this.taskId);
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
     * @param view
     */
    private void browseSoundFileSchedule(View view) {
        Intent intent = new Intent(this, ListSoundFileActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SOUND_FILE);
    }

    /**
     * 儲存提醒
     * @param view
     */
    private void saveSchedule(View view) {

//        put("tm", TableSchemaSetSpec.TEXT | TableSchemaSetSpec.NOT_NULL);
//        put("taskId", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.NOT_NULL);
//        put("soundFileId", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.NOT_NULL);

        //檢查欄位是否都有填
        if(){

        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SELECT_SOUND_FILE:
                if(resultCode == RESULT_OK){
                    this.soundFileId = data.getIntExtra(GlobalNaming.SOUND_FILE_ID, GlobalNaming.ERROR_CODE);
                    this.soundFileName = data.getStringExtra(GlobalNaming.SOUND_FILE_NAME);
                    this.soundFilePathSchedule.setText(this.soundFileName);
                }
                break;
        }
    }
}
