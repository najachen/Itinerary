package silver.reminder.itinerary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Calendar;

import silver.reminder.itinerary.dao.ItineraryDatabaseHelper;
import silver.reminder.itinerary.util.GlobalNaming;

public class MapOptimizePathActivity extends AppCompatActivity {

    private TextView goal;
    private WebView mapDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_optimize_path);

        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(this).getReadableDatabase();

        String tmTodayLike = GlobalNaming.getTmString(Calendar.getInstance()).substring(0, 8);
        Cursor cursorNextTask = db.rawQuery("select * from task where tm like ? order by tm asc limit 1", new String[]{tmTodayLike + "%"});

        String name = "";
        String site = "";
        if (cursorNextTask.getCount() == 1 && cursorNextTask.moveToFirst()) {
            name = cursorNextTask.getString(cursorNextTask.getColumnIndexOrThrow("name"));
            site = cursorNextTask.getString(cursorNextTask.getColumnIndexOrThrow("site"));
        }

        //顯示畫面
        goal.setText(name + "\n" + site);
        mapDirection.loadUrl("file:///android_asset/directionMap.html");
        mapDirection.loadUrl("javascript:reflashDirection('" + site + "')");
    }

    private void findViews() {
        mapDirection = (WebView) findViewById(R.id.mapDirection);
        goal = (TextView) findViewById(R.id.goal);
    }
}
