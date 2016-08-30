package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HomeActivity extends AppCompatActivity {

    private ListView noteOrShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.list_task_item_activity);
        setContentView(R.layout.choice_home);

        findViews();

        Button btn_outoption = (Button) findViewById(R.id.Btn_outoption);
        Button btnout = (Button) findViewById(R.id.Btn_Out);
        Button btnsos = (Button) findViewById(R.id.Btn_SOS);

        btn_outoption.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChoiceOutdoorAllOperation.class);
                startActivity (intent );
            }
        });
        btnout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, ChoiceAllActivity.class);
//                startActivity (intent );
            }
        });
        btnsos.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, ChoiceAllActivity.class);
//                startActivity (intent );
            }
        });
    }

    private void findViews() {
//        noteOrShoppingList = (ListView) findViewById(R.id.noteOrShoppingList);
//        noteOrShoppingList.setAdapter(new MyBaseAdapter());
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return getResources().getStringArray(R.array.noteTitle).length;
        }

        @Override
        public Object getItem(int position) {
            return getResources().getStringArray(R.array.noteTitle)[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View returnView = convertView;
            if(returnView == null){
                /*
                returnView = getLayoutInflater().inflate(R.layout.embedding_task_detail_list_item_note, null, false);

                TextView noteTitle = (TextView) returnView.findViewById(R.id.noteTitle);
                noteTitle.setText(getResources().getStringArray(R.array.noteTitle)[position]);
                TextView noteDetail = (TextView) returnView.findViewById(R.id.noteDetail);
                noteDetail.setText(getResources().getStringArray(R.array.noteDetail)[position]);
                */
            }
            return returnView;
        }
    }
}
