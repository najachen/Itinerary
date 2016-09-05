package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChoiceHomeActivity extends AppCompatActivity {

    private Button intinOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_home);

        findViews();
    }

    private void findViews() {
        intinOption = (Button) findViewById(R.id.intinOption);
        intinOption.setOnClickListener(this::option);
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
}
