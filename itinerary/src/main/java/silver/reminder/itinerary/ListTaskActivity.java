package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ListTaskActivity extends AppCompatActivity {


    private ListView tasklist;
//    MyAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_activity);
        Button btnback = (Button) findViewById(R.id.Btn_Back);
        tasklist = (ListView) findViewById(R.id.taskList);
        FloatingActionButton fabnextpage = (FloatingActionButton) findViewById(R.id.fabNextPage);
        FloatingActionButton fabaddtask = (FloatingActionButton) findViewById(R.id.fabAddTask);
        FloatingActionButton fabprepage = (FloatingActionButton) findViewById(R.id.fabPrePage);

        String [] taskTitle;
        String [] taskDetail;
        String [] showData3 ;
//        adapter = new MyAdapter(this);


        btnback.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); ;
            }
        });
//       ListView myListView = (ListView)findViewById(R.id.ListView01);
//       將設定的內容利用Adapter顯示在ListView上
//       myListView.setAdapter(new ArrayAdapter<String>(this,
//       android.R.layout.simple_list_item_1, menuItem));
//        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1 );
//        tasklist.setAdapter (adapter);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.taskTitle, android.R.layout.simple_list_item_2);
//        SimpleAdapter
        tasklist.setAdapter(adapter);
//        tasklist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, taskTitle));
//        tasklist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, showData2));
//        tasklist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, showData3));
        tasklist.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String se1 = tasklist.getItemAtPosition(i).toString();
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String se1 = tasklist.getItemAtPosition(arg2).toString();
                Intent intent = new Intent(ListTaskActivity.this, CreateOrEditTask.class);
                startActivity(intent);
//                new AlertDialog.Builder(ListTaskActivity.this);
            }
        });

        fabnextpage.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick (View view) {
//                tasklist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, showData2));
            }
        });
        fabaddtask.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(ListTaskActivity.this, CreateOrEditTask.class);
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
