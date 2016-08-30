package silver.reminder.itinerary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ChoiceHomeActivity extends AppCompatActivity {

    private ListView noteOrShoppingList;

    private Button intinBulletin;
    private Button intinOption;
    private Button intinSos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_home);

        findViews();
    }

    private void findViews() {
        intinBulletin = (Button) findViewById(R.id.intinBulletin);
        intinBulletin.setOnClickListener(this::bulletin);

        intinOption = (Button) findViewById(R.id.intinOption);
        intinOption.setOnClickListener(this::option);

        intinSos = (Button) findViewById(R.id.intinSos);
        intinSos.setOnClickListener(this::sos);

//        btn_outoption.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ChoiceHomeActivity.this, ChoiceOutdoorAllOperationActivity.class);
//                startActivity (intent );
//            }
//        });
//        btnout.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(ChoiceHomeActivity.this, ChoiceAllActivity.class);
////                startActivity (intent );
//            }
//        });
//        btnsos.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(ChoiceHomeActivity.this, ChoiceAllActivity.class);
////                startActivity (intent );
//            }
//        });

    }

    /**
     * 通報外出
     * @param view
     */
    private void bulletin(View view){

    }

    /**
     * 外出選項
     * @param view
     */
    private void option(View view){

    }

    /**
     * 求救(先暫時不做)
     * @param view
     */
    private void sos(View view){

    }
}
