package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ChoiceTaskListPeriod extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_task_list_period);

        Button btnweek =  (Button) findViewById(R.id.Btn_Week);
        Button btnmonth = (Button) findViewById(R.id.Btn_Month);
        Button btnseason = (Button) findViewById(R.id.Btn_Season);
        Button btnyear = (Button) findViewById(R.id.Btn_Year);

        btnweek.setOnClickListener(new Button.OnClickListener() {   //未帶入週行程，只是跳回行程表, startActivity需再帶回周行程的值
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoiceTaskListPeriod.this, ListTaskActivity.class);
                startActivity(intent);
            }
        });
        btnmonth.setOnClickListener(new Button.OnClickListener() {  //未帶入月行程，只是跳回行程表, 需再帶回月行程的值
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoiceTaskListPeriod.this, ListTaskActivity.class);
                startActivity(intent);
            }
        });
        btnseason.setOnClickListener(new Button.OnClickListener() { //未帶入季行程，只是跳回行程表, 需再帶回季行程的值
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoiceTaskListPeriod.this, ListTaskActivity.class);
                startActivity(intent);
            }
        });
        btnyear.setOnClickListener(new Button.OnClickListener() {   //帶入年度行程，只是跳回行程表, 需再帶回年行程的值
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoiceTaskListPeriod.this, ListTaskActivity.class);
                startActivity(intent);
            }
        });
    }
}
