package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ChoiceOutdoorAllOperationActivity extends AppCompatActivity {

    /**
     * 選擇列出行程的搜尋方式
     */
    private static final int REQUEST_CODE_SELECT_TASK_PERIOD = 0x0001;

    private Button backChoiceHome;
    private Button todayTask;
    private Button allTask;
    private Button createTask;
    private Button createSoucdFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_outdoor_all_operation);

        findViews();
    }

    private void findViews() {
        backChoiceHome = (Button) findViewById(R.id.backChoiceHome);
        backChoiceHome.setOnClickListener(this::backChoiceHome);

        todayTask = (Button) findViewById(R.id.todayTask);
        todayTask.setOnClickListener(this::todayTask);

        allTask = (Button) findViewById(R.id.allTask);
        allTask.setOnClickListener(this::allTask);

        createTask = (Button) findViewById(R.id.createTask);
        createTask.setOnClickListener(this::createTask);

        createSoucdFile = (Button) findViewById(R.id.createSoucdFile);
        createSoucdFile.setOnClickListener(this::createSoundFile);
    }

    /**
     * 新增音樂檔
     * @param view
     */
    private void createSoundFile(View view) {
        Intent intent = new Intent(this, CreateSoundFileActivity.class);
        startActivity(intent);

        //xxx 這裡等錄音功能熟悉了再繼續做
    }

    /**
     * 新增行程
     * @param view
     */
    private void createTask(View view) {
        Intent intent = new Intent(this, CreateOrEditTaskActivity.class);
        startActivity(intent);
    }

    /**
     * 所有行程(進入選擇時間範圍的頁面)
     * @param view
     */
    private void allTask(View view) {
        Intent intent = new Intent(this, ChoiceTaskListPeriodActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_TASK_PERIOD);
    }

    /**
     * 今天行程
     * @param view
     */
    private void todayTask(View view) {
        Intent intent = new Intent(this, ListTaskActivity.class);
        intent.putExtra(GlobalNaming.TASK_SEARCH_MODE, GlobalNaming.TASK_SEARCH_MODE_TODAY);
        startActivity(intent);
    }

    /**
     * 回首頁
     * @param view
     */
    private void backChoiceHome(View view) {
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_SELECT_TASK_PERIOD && resultCode == RESULT_OK){
            int searchMode = data.getIntExtra(GlobalNaming.TASK_SEARCH_MODE, GlobalNaming.ERROR_CODE);

            Intent intent = new Intent(this, ListTaskActivity.class);
            intent.putExtra(GlobalNaming.TASK_SEARCH_MODE, searchMode);
            startActivity(intent);
        }
    }
}
