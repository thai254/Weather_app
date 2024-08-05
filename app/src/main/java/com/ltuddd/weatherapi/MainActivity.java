package com.ltuddd.weatherapi;

import static com.ltuddd.weatherapi.Districts.findPositionByDistrictName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.health.connect.datatypes.ExerciseRoute;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    Spinner spinProvince, spinDistrict;
    TextView txtTemp, txtTemp2, txtTemp3, clockText, DayText;
    Switch btnLoca;
    String t_huyen, t_tinh;
    ImageView iconWeather;
    RelativeLayout layBG;
    String customDateTime;
    LinearLayout L_Temp, L_UV, L_Water;
    private final int REQUEST_CODE = 100;
    private List<Provinces> provinceList;
    private ArrayAdapter<Provinces> adapter;
    private List<Districts> districtsList;
    private ArrayAdapter<Districts> districtsAdapter;
    private TempDatabase tempDB;
    boolean switchChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempDB = new TempDatabase(MainActivity.this);

        spinProvince = findViewById(R.id.search_prvince);
        txtTemp = findViewById(R.id.txtTemp);
        txtTemp2 = findViewById(R.id.txtTemp2);
        txtTemp3 = findViewById(R.id.txtTemp3);
        spinDistrict = findViewById(R.id.search_district);
        iconWeather = findViewById(R.id.icon_weather);
        layBG = findViewById(R.id.background_weather);
        clockText = findViewById(R.id.clockText);
        DayText = findViewById(R.id.DayText);
        btnLoca = findViewById(R.id.LocationCurren_Btn);
        L_Temp = findViewById(R.id.LL_Temp);
        L_Water = findViewById(R.id.LL_Water);
        L_UV = findViewById(R.id.LL_UV);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        get63Provinces();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 0);


        spinProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Provinces selectedProvince = (Provinces) parent.getItemAtPosition(position);
                String provinceId = selectedProvince.getId();
                String provinceName = selectedProvince.getName();

                getDistrictByProvinceID(provinceId);

                //Log.v("Selected province: ", provinceId + " - " + provinceName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Districts selectedDistrict = (Districts) parent.getItemAtPosition(position);
                String dis_latitude = selectedDistrict.getLat();
                String dis_longitude = selectedDistrict.getLong();

                getCurrentWeatherData(dis_latitude, dis_longitude);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnLoca.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchChecked = true;
                    getLocation();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (switchChecked) {
                                getLocation();
                            }
                        }
                    }, 2000);
                }
            }
        });

        Cursor cursor = tempDB.DisplayLocation();
        if (cursor.getCount() > 0) {
            StringBuffer data = new StringBuffer();
            while (cursor.moveToNext())
            {
                String prov = cursor.getString(1);
                String dist = cursor.getString(2);
            }
        }
        cursor.close();

        L_UV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoreTempActivity.class);
                startActivity(intent);
            }
        });

        L_Water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoreTempActivity.class);
                startActivity(intent);
            }
        });

        L_Temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoreTempActivity.class);
                startActivity(intent);
            }
        });

    }


    private void updateTime() {
        // Lấy thời gian hiện tại
        long currentTimeMillis = System.currentTimeMillis();

        // Định dạng thời gian
        SimpleDateFormat wFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
        String currentDay = wFormat.format(new Date(currentTimeMillis));

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = dateFormat.format(new Date(currentTimeMillis));

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String currentDate = dayFormat.format(new Date(currentTimeMillis));

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        String currentMonth = monthFormat.format(new Date(currentTimeMillis));

        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
        customDateTime = customFormat.format(new Date(currentTimeMillis));

        // Hiển thị thời gian trên TextView
        clockText.setText(currentTime);
        DayText.setText(currentDay + ", Ngày " + currentDate + " Tháng " + currentMonth);
    }

    private void askPermisson() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    public void get63Provinces(){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://esgoo.net/api-tinhthanh/1/0.htm";
        Log.v("step1: ", "done");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.v("step2: ", "done");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            provinceList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                String provinceId = item.getString("id");
                                String provinceName = item.getString("full_name");
                                String provinceNameEn= item.getString("name_en");

                                Provinces province = new Provinces(provinceId, provinceName, provinceNameEn);
                                provinceList.add(province);
                            }
                            Log.v("step3: ", "done");
                            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, provinceList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinProvince.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    public class DataTransferUtil {
        public void transferDataToTempList(JSONArray timeData, JSONArray tempData, JSONArray humidityData, JSONArray rainData, JSONArray windData, String TimeNow, int isDay) {
            try {
                int length = timeData.length();
                String[] timeList = new String[length];
                String[] tempList = new String[length];
                String[] humidityList = new String[length];
                String[] rainList = new String[length];
                String[] windList = new String[length];

                for (int i = 0; i < length; i++) {
                    timeList[i] = timeData.getString(i);
                    tempList[i] = tempData.getString(i);
                    humidityList[i] = humidityData.getString(i);
                    rainList[i] = rainData.getString(i);
                    windList[i] = windData.getString(i);
                }

                TempArrayData.TimeList = timeList;
                TempArrayData.TempList = tempList;
                TempArrayData.WaterList = humidityList;
                TempArrayData.RainList = rainList;
                TempArrayData.WindList = windList;
                TempArrayData.TimeCurrent = TimeNow;
                TempArrayData.isDay = isDay;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getDistrictByProvinceID(String id_pro) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        if (id_pro.length() < 2) {
            id_pro = "0" + id_pro;
        }
        String url = "https://esgoo.net/api-tinhthanh/2/" + id_pro + ".htm";
        Log.v("id prov: ", String.valueOf(id_pro));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                           districtsList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject districtObject = jsonArray.getJSONObject(i);
                                String fullName = districtObject.getString("full_name");
                                String NameEn = districtObject.getString("name_en");
                                String latitude = districtObject.getString("latitude");
                                String longitude = districtObject.getString("longitude");

                                Districts district = new Districts(fullName, NameEn, latitude, longitude);
                                districtsList.add(district);
                            }

                            // Tạo adapter cho Spinner
                            districtsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, districtsList);
                            districtsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinDistrict.setAdapter(districtsAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(stringRequest);
    }
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.v("loca 1: ", "ok");
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    Log.v("loca 2: ", "ok");
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                    String addressLine = addresses.get(0).getAddressLine(0);
                                    String[] parts = addressLine.split(",");

                                    t_huyen = parts[2].trim();
                                    t_tinh = parts[3].trim();
                                    //t_huyen = "Tiền Hải";
                                    //t_tinh = "Thái Bình";

                                    ChangeText changeText = new ChangeText(t_huyen);
                                    t_huyen = changeText.getOutput();

                                    ChangeText changeText1 = new ChangeText(t_tinh);
                                    t_tinh = changeText1.getOutput();

                                    String provinceId = Provinces.findProvincesIdByEnName(provinceList, t_tinh);
                                    String[] districtId = Districts.findDistrictLatLonByEnName(districtsList, t_huyen);

                                    int position = -1;
                                    if (provinceId != null) {
                                        for (int i = 0; i < provinceList.size(); i++) {
                                            if (provinceList.get(i).getId().equals(provinceId)) {
                                                position = i;
                                                break;
                                            }
                                        }
                                    }

                                    spinProvince.setSelection(position);

                                    if (districtId != null) {
                                        String latitude = districtId[0];
                                        String longitude = districtId[1];
                                        String name_dis = districtId[2];
                                        Log.v("Latitude: ", latitude);
                                        Log.v("Longitude: ", longitude);
                                        Log.v("name: ", name_dis);

                                        int position1 = findPositionByDistrictName(districtsList, t_huyen);
                                        if (position1 != -1) {
                                            spinDistrict.setSelection(position1);
                                            long check = tempDB.insertNewLocation(t_tinh, t_huyen);
                                            Log.v("data", "ok");
                                        }
                                    } else {
                                        Log.v("Spinner District", "Không tìm thấy thông tin quận/huyện.");
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
        } else {
            askPermisson();
        }
    }

    public void getCurrentWeatherData(String dis_lat, String dis_long){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String url = "https://api.open-meteo.com/v1/forecast?" +
//                "latitude=" + dis_lat + "&longitude="+dis_long+"&current=temperature_2m,is_day&hourly=temperature_2m," +
//                "relative_humidity_2m,precipitation_probability&daily=uv_index_max&wind_speed_unit=mph&timezone=auto&forecast_days=1";
        String url1 = "https://api.open-meteo.com/v1/forecast?" +
                "latitude="+ dis_lat +"&longitude=" +dis_long+ "&current=temperature_2m,relative_humidity_2m,is_day," +
                "wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,precipitation_probability,precipitation," +
                "cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high,evapotranspiration,wind_speed_10m&" +
                "daily=uv_index_max&forecast_days=1";
        Log.v("url: ", url1);

        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);

        int HourCurrent = (calendar.get(Calendar.HOUR_OF_DAY));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject currentData = jsonObject.getJSONObject("current");
                            double temperature2m = currentData.getDouble("temperature_2m");
                            int isDay = currentData.getInt("is_day");
                            String time = currentData.getString("time");


                            JSONObject dailyData = jsonObject.getJSONObject("daily");
                            JSONArray uvIndexMax = (JSONArray) dailyData.get("uv_index_max");
                            double uvIndexMaxValue = (double) uvIndexMax.get(0);

                            JSONObject hourlyData = jsonObject.getJSONObject("hourly");
                            JSONArray timeData = hourlyData.getJSONArray("time");
                            JSONArray TempData = hourlyData.getJSONArray("temperature_2m");
                            JSONArray humidityData = hourlyData.getJSONArray("relative_humidity_2m");
                            JSONArray RainData = hourlyData.getJSONArray("precipitation_probability");
                            JSONArray WindData = hourlyData.getJSONArray("wind_speed_10m");


                            DataTransferUtil dataTransferUtil = new DataTransferUtil();
                            dataTransferUtil.transferDataToTempList(timeData, TempData, humidityData, RainData, WindData, customDateTime, isDay);

                            int humidity = humidityData.getInt(HourCurrent);

                            if(isDay == 1){
                                layBG.setBackgroundResource(R.drawable.days);
                                iconWeather.setBackgroundResource(R.drawable.sun_logo);
                            }else if(isDay == 0){
                                layBG.setBackgroundResource(R.drawable.night);
                                iconWeather.setBackgroundResource(R.drawable.moon_logo);
                            }

                            txtTemp.setText(String.valueOf(temperature2m) + "℃");
                            txtTemp2.setText(String.valueOf(uvIndexMaxValue));
                            txtTemp3.setText(String.valueOf(humidity) + "%");
                            //30.5 - 1 - [7.4]
                            Log.v("temp dis: ",temperature2m +" - "+ isDay +" - "+ uvIndexMax);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }else {
                Toast.makeText(this, "Vui lòng cấp quyền truy cập vị trị cho ứng dụng!", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}