package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ListTaskActivity extends AppCompatActivity {

    private ListView tasklist;
    private Button backChoiceOutdoorAllOperation;
    private FloatingActionButton prePage;
    private FloatingActionButton addTask;
    private FloatingActionButton nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_activity);

        findViews();
    }

    private void findViews() {
        backChoiceOutdoorAllOperation = (Button) findViewById(R.id.backChoiceOutdoorAllOperation);
        backChoiceOutdoorAllOperation.setOnClickListener(this::backChoiceOutdoorAllOperation);
        tasklist = (ListView) findViewById(R.id.taskList);
        tasklist.setOnItemClickListener(this::onTaskItemClick);
        prePage = (FloatingActionButton) findViewById(R.id.prePage);
        prePage.setOnClickListener(this::prePage);
        addTask = (FloatingActionButton) findViewById(R.id.addTask);
        addTask.setOnClickListener(this::addTask);
        nextPage = (FloatingActionButton) findViewById(R.id.nextPage);
        nextPage.setOnClickListener(this::nextPage);
    }

    /**
     * 上一頁
     *
     * @param view
     */
    private void prePage(View view) {


    }

    /**
     * 下一頁
     *
     * @param view
     */
    private void nextPage(View view) {

    }

    /**
     * 新增行程
     *
     * @param view
     */
    private void addTask(View view) {
        Intent intent = new Intent(this, CreateOrEditTaskActivity.class);
        startActivity(intent);
    }

    /**
     * 進入單一行程明細頁面
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    private void onTaskItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
     * 返回
     *
     * @param view
     */
    private void backChoiceOutdoorAllOperation(View view) {
        this.finish();
    }
}
