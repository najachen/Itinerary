package silver.reminder.itinerary;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import silver.reminder.itinerary.util.GlobalNaming;
import silver.reminder.itinerary.util.Pager;

/**
 * Created by Administrator on 2016/8/23.
 */
public class ListSoundFileActivity extends AppCompatActivity {

    private Button backCreateDingDong;
    private ListView soundFileList;
    private FloatingActionButton nextPageSoundFile;
    private FloatingActionButton prePageSoundFile;

    /*
        分頁相關
     */
    private static final int PAGE_SIZE = 5;
    public int currentPage = 1;

    /*
        table name and field name
     */
    private static final String SOUND_FILE_TABLE_NAME = "soundFile";
    private static final String SOUND_FILE_FIELD_ID = "_id";
    private static final String SOUND_FILE_FIELD_FILE_NAME = "fileName";

    /**
     * 是否正在播放音樂
     */
    private boolean isMusicPlaying = false;
    //音樂播放器
    private MediaPlayer mediaPlayer;
    //
    ImageButton startOrStopMusicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_sound_file_activity);

        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        listSoundFile(null);
    }

    private void findViews() {
        nextPageSoundFile = (FloatingActionButton) findViewById(R.id.nextPageSoundFile);
        nextPageSoundFile.setOnClickListener(this::nextPageSoundFile);
        prePageSoundFile = (FloatingActionButton) findViewById(R.id.prePageSoundFile);
        prePageSoundFile.setOnClickListener(this::prePageSoundFile);
        backCreateDingDong = (Button) findViewById(R.id.backCreateDingDong);
        backCreateDingDong.setOnClickListener(this::backCreateDingDong);

        soundFileList = (ListView) findViewById(R.id.soundFileList);
        soundFileList.setOnItemClickListener(this::onSoundFileClick);
    }

    /**
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    private void onSoundFileClick(AdapterView<?> adapterView, View view, int i, long l) {

        TextView soundFileId = (TextView) view.findViewById(R.id.soundFileId);
        TextView soundFileName = (TextView) view.findViewById(R.id.soundFileName);

        Toast.makeText(this, getString(R.string.whatYouChoice) + soundFileName.getText().toString(), Toast.LENGTH_LONG).show();

        int intSoundFileId = Integer.parseInt(soundFileId.getText().toString());

        //test-
        Log.d("soundFileId", String.valueOf(soundFileId));

        Intent intent = getIntent();
        intent.putExtra(GlobalNaming.SOUND_FILE_ID, intSoundFileId);
        intent.putExtra(GlobalNaming.SOUND_FILE_NAME, soundFileName.getText().toString());
        setResult(RESULT_OK, intent);

        finish();
    }

    /**
     * @param view
     */
    private void backCreateDingDong(View view) {
        finish();
    }

    /**
     * @param view
     */
    private void prePageSoundFile(View view) {
        listSoundFile(Pager.GO_BACKWARD);
    }

    /**
     * @param view
     */
    private void nextPageSoundFile(View view) {
        listSoundFile(Pager.GO_FORWARD);
    }

    /**
     * 播放與停止音樂
     *
     * @param view
     */
    private void startOrStopPlayMusic(View view) {

        if(!isMusicPlaying){
            //取得音樂檔名
            LinearLayout linearLayout = (LinearLayout) view.getParent();
            TextView soundFileName = (TextView) linearLayout.findViewById(R.id.soundFileName);
            String fileName = soundFileName.getText().toString();

            File musicPublicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File musicFile = new File(musicPublicDir, CreateSoundFileActivity.DIR_SILVER_REMINDER + CreateSoundFileActivity.DIR_SILVER_REMINDER_STORE + fileName);

            Uri musicFileUri = Uri.parse(musicFile.getAbsolutePath());
            mediaPlayer = MediaPlayer.create(this, musicFileUri);

            //regist play completion listener
            mediaPlayer.setOnCompletionListener(this::OnMediaPlayerCompletion);

            mediaPlayer.start();

            //switch state
            isMusicPlaying = !isMusicPlaying;

            //改為停止鈕
            startOrStopMusicButton = (ImageButton)view;
            startOrStopMusicButton.setImageResource(R.drawable.button_pause);

        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            //switch state
            isMusicPlaying = !isMusicPlaying;

            //改為開始鈕
            startOrStopMusicButton.setImageResource(R.drawable.button_play);
            startOrStopMusicButton = null;
        }
    }

    /**
     * when play completion
     * @param mediaPlayer
     */
    private void OnMediaPlayerCompletion(MediaPlayer mediaPlayer){

        //switch state
        isMusicPlaying = !isMusicPlaying;

        //改為開始鈕
        startOrStopMusicButton.setImageResource(R.drawable.button_play);
        startOrStopMusicButton = null;

        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listSoundFile(Boolean isGoForward) {

        Pager pager = new Pager(this, PAGE_SIZE);
        Cursor cursor = pager.getPagedCursorForMusicList(isGoForward);
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this
                , R.layout.embedding_sound_file_list_item
                , cursor
                , new String[]{SOUND_FILE_FIELD_ID, SOUND_FILE_FIELD_FILE_NAME}
                , new int[]{R.id.soundFileId, R.id.soundFileName}
                , 0) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);

                ImageButton startOrStopPlayMusic = (ImageButton) view.findViewById(R.id.startOrStopPlayMusic);

                ComponentName componentName = ListSoundFileActivity.this.getCallingActivity();
                if (componentName != null) {
                    String componentShortName = componentName.getShortClassName();
                    componentShortName = componentShortName.replace(".", "");

                    //test-
                    Log.d("componentShortName", componentShortName);
                    Log.d("compShortName equal", String.valueOf(CreateDingDongActivity.class.getSimpleName().equals(componentShortName)));

                    if (CreateDingDongActivity.class.getSimpleName().equals(componentShortName)) {
                        startOrStopPlayMusic.setEnabled(false);
                        startOrStopPlayMusic.setFocusable(false);
                        startOrStopPlayMusic.setVisibility(View.GONE);
                    }
                }

//                startOrStopPlayMusic.setEnabled(true);
//                startOrStopPlayMusic.setFocusable(true);
//                startOrStopPlayMusic.setVisibility(View.VISIBLE);
                startOrStopPlayMusic.setOnClickListener(ListSoundFileActivity.this::startOrStopPlayMusic);

                //test-
                Log.d("componentName != null", String.valueOf(componentName != null));
            }
        };
        soundFileList.setAdapter(simpleCursorAdapter);
    }
}
