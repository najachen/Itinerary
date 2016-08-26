package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/8/22.
 */
public class CeateOrEditTask extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_edit_task);
        Button btnenter = (Button) findViewById(R.id.Btn_Enter);
        Button btnclearn = (Button) findViewById(R.id.Btn_Clearn);
        Button btnback = (Button) findViewById(R.id.Btn_Back);
        Button create_ding_dongfind = (Button) findViewById(R.id.create_ding_dong); //設定音效
        EditText datevalue = (EditText) findViewById(R.id.date_value);  //日期設定
        EditText timevalue = (EditText) findViewById(R.id.time_value);  //時間設定
        EditText sitevalue = (EditText) findViewById(R.id.site_value);  //地點設定
        btnenter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CeateOrEditTask.this, ListTaskActivity.class);   //確認帶回修改的值
                startActivity(intent);
            }
        });
        btnclearn.setOnClickListener(new Button.OnClickListener() {  //清空日期時間地點edit Text
            @Override
            public void onClick(View view) {

            }
        });
        btnback.setOnClickListener(new Button.OnClickListener() {   //返回上一頁
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CeateOrEditTask.this, ListTaskActivity.class);
                startActivity(intent);
            }
        });
        create_ding_dongfind.setOnClickListener(new Button.OnClickListener() {  //設定音效，跳到音效list
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CeateOrEditTask.this, ListSoundFileActivity.class);
                startActivity(intent);
            }
        });

    }
}
