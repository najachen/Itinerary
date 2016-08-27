package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by hsuan on 2016/8/27.
 */
public class CreateDingDong extends AppCompatActivity {
//    public static  final int FUNC_SOUNDLIST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.create_ding_dong);

        Button btnnewsound = (Button) findViewById(R.id.Btn_NewSound);
        Button btnback = (Button) findViewById(R.id.Btn_Back);
        Button btnok = (Button) findViewById(R.id.Btn_OK);
        Button btnbrowse = (Button) findViewById(R.id.Btn_Browse_SoundFile);

        btnnewsound.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateDingDong.this, ListSoundFileActivity.class);
                startActivity(intent);
            }
        });
        btnback.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnok.setOnClickListener(new Button.OnClickListener() { //味帶入選好的音笑檔回傳值)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateDingDong.this, CeateOrEditTask.class);
                startActivity (intent);
            }
        });
        btnbrowse.setOnClickListener (new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateDingDong.this, ListSoundFileActivity.class);
            }
        });

    }
}
