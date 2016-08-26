package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ListTaskActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_activity);
        Button btnback = (Button) findViewById(R.id.Btn_Back);
        ListView tasklist = (ListView) findViewById(R.id.taskList);
        FloatingActionButton fabnextpage = (FloatingActionButton) findViewById(R.id.fabNextPage);
        FloatingActionButton fabaddtask = (FloatingActionButton) findViewById(R.id.fabAddTask);
        FloatingActionButton fabprepage = (FloatingActionButton) findViewById(R.id.fabPrePage);
        String [] showData1 =  {"行程1", "行程2", "行程3", "行程4"};
        String [] showData2 =  {"行程5", "行程6", "行程7", "行程8"};
        String [] showData3 =  {"行程9", "行程A", "行程B", "行程C"};


        btnback.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                return ;
            }
        });
//       ListView myListView = (ListView)findViewById(R.id.ListView01);
//       將設定的內容利用Adapter顯示在ListView上
//       myListView.setAdapter(new ArrayAdapter<String>(this,
//       android.R.layout.simple_list_item_1, menuItem));
//        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1 );
//        tasklist.setAdapter (adapter);

        tasklist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, showData1));
        tasklist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, showData2));
        tasklist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, showData3));


        fabnextpage.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick (View view) {
//                tasklist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, showData2));
            }
        });
        fabaddtask.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(ListTaskActivity.this, CeateOrEditTask.class);
                startActivity(intent);
            }
        });
        fabprepage.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick (View view) {
//                Intent intent = new Intent(ListTaskActivity.this, ListTaskActivity.class);
//                startActivity(intent);
            }
        });
    }
}
