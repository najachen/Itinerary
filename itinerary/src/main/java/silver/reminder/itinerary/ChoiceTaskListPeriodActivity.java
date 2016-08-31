package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ChoiceTaskListPeriodActivity extends AppCompatActivity {

    private Button thisWeek;
    private Button thisYear;
    private Button thisSeason;
    private Button thisMonth;

    /**
     * 回傳選擇的範圍
     */
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_task_list_period);

        findViews();

        intent = getIntent();
    }

    private void findViews() {
        thisWeek = (Button) findViewById(R.id.thisWeek);
        thisWeek.setOnClickListener(this::thisWeek);
        thisMonth = (Button) findViewById(R.id.thisMonth);
        thisMonth.setOnClickListener(this::thisMonth);
        thisSeason = (Button) findViewById(R.id.thisSeason);
        thisSeason.setOnClickListener(this::thisSeason);
        thisYear = (Button) findViewById(R.id.thisYear);
        thisYear.setOnClickListener(this::thisYear);
    }

    /**
     * 找全年行程
     * @param view
     */
    private void thisYear(View view) {
        intent.putExtra(GlobalNaming.TASK_SEARCH_MODE, GlobalNaming.TASK_SEARCH_MODE_THIS_YEAR);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 找當季行程
     * @param view
     */
    private void thisSeason(View view) {
        intent.putExtra(GlobalNaming.TASK_SEARCH_MODE, GlobalNaming.TASK_SEARCH_MODE_THIS_SEASON);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 找當月行程
     * @param view
     */
    private void thisMonth(View view) {
        intent.putExtra(GlobalNaming.TASK_SEARCH_MODE, GlobalNaming.TASK_SEARCH_MODE_THIS_MONTH);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 找當周行程
     * @param view
     */
    private void thisWeek(View view) {
        intent.putExtra(GlobalNaming.TASK_SEARCH_MODE, GlobalNaming.TASK_SEARCH_MODE_THIS_WEEK);
        setResult(RESULT_OK, intent);
        finish();
    }
}
