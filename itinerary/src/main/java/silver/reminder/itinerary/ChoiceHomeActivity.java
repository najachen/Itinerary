package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChoiceHomeActivity extends AppCompatActivity {

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
    }

    /**
     * 通報外出(最佳化外出路徑)
     *
     * @param view
     */
    private void bulletin(View view) {
        Intent intent = new Intent(this, MapOptimizePathActivity.class);
        startActivity(intent);
    }

    /**
     * 外出選項
     *
     * @param view
     */
    private void option(View view) {
        Intent intent = new Intent(this, ChoiceOutdoorAllOperationActivity.class);
        startActivity(intent);
    }

    /**
     * 求救(先暫時不做)
     *
     * @param view
     */
    private void sos(View view) {
        Intent intent = new Intent(this, MapSosActivity.class);
        startActivity(intent);
    }
}
