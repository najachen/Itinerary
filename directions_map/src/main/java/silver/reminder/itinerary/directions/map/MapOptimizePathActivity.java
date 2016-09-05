package silver.reminder.itinerary.directions.map;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static android.Manifest.permission;

public class MapOptimizePathActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;

    /*
        非同步等待停止線
     */
    private boolean isMapReady = false;
    private boolean isOnResume = false;

    /*
        權限請求碼
     */
    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 0x0001;

    /*
        起點與終點的位置 [0]起點 [1]終點
     */
    private LatLng[] latLngArray = new LatLng[2];

    /*
        目標地點名稱
     */
    private String site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_optimize_path);

        FragmentManager fragmentManager = getSupportFragmentManager();

        SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        isMapReady = true;

        if (isMapReady && isOnResume) {
            refreshCanvas();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnResume = true;

        //清空起點與終點
        latLngArray[0] = null;
        latLngArray[1] = null;

        if (isMapReady && isOnResume) {
            refreshCanvas();
        }
    }

    private void refreshCanvas() {

        /*
            定位資訊
         */
//        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(this).getReadableDatabase();
//
//        String tmTodayLike = GlobalNaming.getTmString(Calendar.getInstance()).substring(0, 8);
//        Cursor cursorNextTask = db.rawQuery("select * from task where tm like ? order by tm asc limit 1", new String[]{tmTodayLike + "%"});
//
//        String name = "";
//        if (cursorNextTask.getCount() == 1 && cursorNextTask.moveToFirst()) {
//            name = cursorNextTask.getString(cursorNextTask.getColumnIndexOrThrow("name"));
//            site = cursorNextTask.getString(cursorNextTask.getColumnIndexOrThrow("site"));
//        }
        //test- 測試用定位資訊
        this.site = "台北車站";



        //顯示畫面
//        goal.setText(name + "\n" + site);

        /*
            繪製地圖
         */
        int permissionStatus = ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION);
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
        } else {
            enableMyLocation();
        }

        //        mMap.clear();

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(sydney).title("Marker in Sydney").snippet("很好!!").icon(bitmapDescriptor);
//
//        map.addMarker(markerOptions);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ACCESS_FINE_LOCATION:
                if (permissions[0].equals(permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("精確定位權限未取得!!")
                            .setNeutralButton("我知道了", null)
                            .show();
                }
                break;
        }
    }

    private void enableMyLocation() {
        //noinspection MissingPermission
        map.setMyLocationEnabled(true);

        ////取得自己的位置
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        //noinspection MissingPermission
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
            latLngArray[0] = new LatLng(location.getLatitude(), location.getLongitude());
        }

        //取得目標地
        Geocoder geo = new Geocoder(this, Locale.TAIWAN);
        Address address = null;
        try {
            address = geo.getFromLocationName(this.site, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address != null) {
            latLngArray[1] = new LatLng(address.getLatitude(), address.getLongitude());
        }

        //若起點終點都有 開始繪圖
        if (latLngArray[0] == null || latLngArray[1] == null) {
            new AlertDialog.Builder(this)
                    .setMessage("您的位置或目標地取得不全,無法繪製路線!!")
                    .setNeutralButton("確定", null)
                    .show();
            return;

        } else {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

            MarkerOptions startPoint = new MarkerOptions();
            startPoint.position(latLngArray[0]).title("現在位置").icon(bitmapDescriptor);
            map.addMarker(startPoint);

            MarkerOptions endPoint = new MarkerOptions();
            endPoint.position(latLngArray[1]).title("目標地").snippet(this.site).icon(bitmapDescriptor);
            map.addMarker(endPoint);

            String url = getDirectionUrl(latLngArray[0], latLngArray[1]);

            GetDirDataAndDrawMap getDirDataAndDrawMap = new GetDirDataAndDrawMap();
            getDirDataAndDrawMap.execute(url);
        }
    }

    /**
     * @param start
     * @param end
     * @return
     */
    private String getDirectionUrl(LatLng start, LatLng end) {

        final String comm = ",";

        String origin = "origin=" + start.latitude + comm + start.longitude;
        String dest = "destination=" + end.latitude + comm + end.longitude;

        String sensor = "sensor=true";

        String para = origin + "&" + dest + "&" + sensor;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + para;
        return url;
    }

    class GetDirDataAndDrawMap extends AsyncTask<String, Void, String> {

        private StringBuffer jsonBuffer = new StringBuffer();

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                InputStream inputStream = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String readStr;
                while ((readStr = bufferedReader.readLine()) != null) {
                    jsonBuffer.append(readStr);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            Log.i("json ========", s);


        }
    }
}


//    /**
//     * 從URL下載JSON資料的方法
//     **/
//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
////            InputStream inputStream = url.openStream();
//
//
//            // Creating an http connection to communicate with url
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//
//            // Connecting to url
//            urlConnection.connect();
//
//            // Reading data from url
//            iStream = urlConnection.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    iStream));
//
//            StringBuffer sb = new StringBuffer();
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//
//            br.close();
//
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }


//
///**
// * 解析JSON格式
// **/
//private class ParserTask extends
//        AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//    // Parsing the data in non-ui thread
//    @Override
//    protected List<List<HashMap<String, String>>> doInBackground(
//            String... jsonData) {
//
//        JSONObject jObject;
//        List<List<HashMap<String, String>>> routes = null;
//
//
//        //這裡是我寫的
//        try {
//            JSONArray jsonArray = new JSONArray(jsonData[0]);
//
//
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//
//
//                JSONArray jsonArray1 = jsonArray.getJSONArray(1);
//                for (int j = 0; j < jsonArray1.length(); j++) {
//                    JSONObject jsonObject = jsonArray1.getJSONObject(j);
//
//                    String latitude = jsonObject.getString("lat");
//                    double douLatitude = Double.parseDouble(latitude);
//                    String longitude = jsonObject.getString("lng");
//                    double douLongitude = Double.parseDouble(longitude);
//
//                    LatLng latLng = new LatLng(douLatitude, douLongitude);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return routes;
//    }
//
//    // Executes in UI thread, after the parsing process
//    @Override
//    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//
////            第一個list 裝路徑 route path 所以有很多路徑
////            第二個list 裝定點 每條路徑有很多定點
////            所以 HashMap<String, String> 是一個定點 - key 為 "lat" "lng"
//
//        ArrayList<LatLng> points = null;
//        PolylineOptions lineOptions = null;
//
//        MarkerOptions markerOptions = new MarkerOptions();
//
//        // Traversing through all the routes
//        for (int i = 0; i < result.size(); i++) {
//
//            points = new ArrayList<LatLng>();
//            lineOptions = new PolylineOptions();
//
//            // Fetching i-th route
//            List<HashMap<String, String>> path = result.get(i);
//
//            // Fetching all the points in i-th route
//            for (int j = 0; j < path.size(); j++) {
//                HashMap<String, String> point = path.get(j);
//
//                double lat = Double.parseDouble(point.get("lat"));
//                double lng = Double.parseDouble(point.get("lng"));
//                LatLng position = new LatLng(lat, lng);
//
//                points.add(position);
//            }
//
//            // Adding all the points in the route to LineOptions
//            lineOptions.addAll(points); //參數型別為 Iterable<LatLng>
//            lineOptions.width(5);  //導航路徑寬度
//            lineOptions.color(Color.BLUE); //導航路徑顏色
//
//        }
//
//        // Drawing polyline in the Google Map for the i-th route
//        map.addPolyline(lineOptions);
//    }
//}
