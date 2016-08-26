package silver.reminder.itinerary;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/22.
 */
public class CeateSoundFile extends AppCompatActivity {

    private Button savesound;
    private Button soundList;
    private Button cancelRecord;
    private ImageButton startStoprecord;
    private EditText soundfilepath;
    private MediaRecorder MR;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_sound_file);
        savesound = (Button) findViewById(R.id.saveSound);
        soundList = (Button) findViewById(R.id.soundList);
        cancelRecord = (Button) findViewById(R.id.cancelRecord);
        startStoprecord = (ImageButton) findViewById(R.id.startOrStopRecord);
        soundfilepath = (EditText) findViewById(R.id.soundFilePath);
        String sfp = soundfilepath.getText().toString();

        savesound.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 問鬼哥
            }
        }); //存儲新音效檔

        soundList.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CeateSoundFile.this, ListSoundFileActivity.class);
                startActivity(intent);
            }
        });
        cancelRecord.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//            Toast.makeText(CeateSoundFile.this, "儲存成功", Toast.LENGTH_SHORT).show();
//            finish();
//            以下參考
//        if (uid.equals("jack") && pw.equals("1234")) {   //登入成功
//
//            SharedPreferences pref = getSharedPreferences("atm", MODE_PRIVATE);
//            pref.edit()
//                    .putString("PREF_USERID", uid)
//                    .commit();  //開始寫入
//
//            Toast.makeText(this, "登入成功", Toast.LENGTH_LONG).show();
//            setResult(RESULT_OK, getIntent());
//            finish();
//        }

        startStoprecord.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count != 1) {
                    String fileName = "record.amr"; //設定錄音檔名
                    try {
                        File SDCardpath = Environment.getExternalStorageDirectory();
                        File myDataPath = new File(SDCardpath.getAbsolutePath() + "/download");
                        if (!myDataPath.exists()) myDataPath.mkdirs();
                        File recodFile = new File(SDCardpath.getAbsolutePath() + "/download/" + fileName);

                        MR = new MediaRecorder();
                        MR.setAudioSource(MediaRecorder.AudioSource.MIC);   //設定音源，MIC
                        MR.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); //設定輸出格式
                        MR.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); //設定編碼
                        recodFile = File.createTempFile("raw", ".amr", Environment.getExternalStorageDirectory());
                        MR.setOutputFile(recodFile.getAbsolutePath()); //設定錄音檔位置
                        MR.prepare();   //錄音準備
                        MR.start(); //開始錄音
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    if (MR != null){
                        MR.stop();  //停止錄音
                        MR.release();   //釋放手機資源
                        MR = null;
                    }
            }
        });
        // 停止錄音
//        stopButn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(mediaRecorder != null) {
//                    mediaRecorder.stop();
//                    mediaRecorder.release();
//                    mediaRecorder = null;
//                }
//            }
//        });
    }
}
