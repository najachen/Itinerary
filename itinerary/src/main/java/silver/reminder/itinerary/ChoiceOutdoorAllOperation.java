package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ChoiceOutdoorAllOperation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_outdoor_all_operation);
        Button btn_day = (Button)  findViewById(R.id.Btn_Day);
        Button btn_all = (Button) findViewById(R.id.Btn_All);
        Button btn_newoper = (Button) findViewById(R.id.Btn_NewOper);
        Button btn_newsound= (Button) findViewById(R.id.Btn_NewSound);

        btn_day.setOnClickListener(new Button.OnClickListener() {   //日行程按鈕
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoiceOutdoorAllOperation.this, ListTaskActivity.class);
                startActivity(intent);
            }
        });
        btn_all.setOnClickListener(new Button.OnClickListener() {   //所有行程按鈕
        @Override
        public void onClick(View v) {
            Intent intent = new Intent (ChoiceOutdoorAllOperation.this, ChoiceTaskListPeriod.class);
            startActivity(intent);
        }
    });
        btn_newoper.setOnClickListener(new Button.OnClickListener() {   //新增行程按鈕
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ChoiceOutdoorAllOperation.this, CeateOrEditTask.class);
            startActivity(intent);
        }
    });
    btn_newsound.setOnClickListener(new Button.OnClickListener() {  //新增音訊提醒按鈕
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ChoiceOutdoorAllOperation.this, CeateSoundFile.class);
            startActivity(intent);
        }
    });

    }
}
