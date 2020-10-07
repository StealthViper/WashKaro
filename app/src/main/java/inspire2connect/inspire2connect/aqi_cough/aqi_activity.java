package inspire2connect.inspire2connect.aqi_cough;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class aqi_activity extends BaseActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.aqi_activity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        
        //Newly Added Code Below
        final TextView avgAQI = (TextView) findViewById(R.id.avgAQI);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        } catch (Exception e){
            currentLatitude = 28.5455;
            currentLongitude = 77.2731;
        }

        OkHttpClient client = new OkHttpClient();

        //String url = "https://api.waqi.info/feed/delhi/?token=262ba73b900a0ff15d3ac7bbbee593ddbb543aa9";

        String url = "http://api.weatherbit.io/v2.0/history/airquality?lat=" + currentLatitude + "&lon=" + currentLongitude + "&key=6a0d2a4497cc4e51b394784a9fb433a9";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    String finalAQI = "Unavailable";
                    try {
                        JSONObject aqiJSON = new JSONObject(myResponse);
                        JSONArray aqiData = aqiJSON.getJSONArray("data");
                        JSONObject hourAQI = aqiData.getJSONObject(24);
                        finalAQI = "" + hourAQI.get("aqi").toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String finalAQI1 = finalAQI;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            avgAQI.setText(finalAQI1);
                        }
                    });
                }

            }
        });

        Button resetButton = (Button) findViewById(R.id.resetButton);
        Button geotagButton = (Button) findViewById(R.id.geotagButton);

        final EditText dataCollectedAt = (EditText) findViewById(R.id.dataCollectedAt);
        final EditText aqiPredictText = (EditText) findViewById(R.id.aqiPredictText);
        final EditText bmiText = (EditText) findViewById(R.id.bmiText);
        final EditText ageText = (EditText) findViewById(R.id.ageText);

        final CheckBox bronchitisCheck = (CheckBox) findViewById(R.id.bronchitisCheck);
        final CheckBox asthmaCheck = (CheckBox) findViewById(R.id.asthmaCheck);
        final CheckBox pneumoniaCheck = (CheckBox) findViewById(R.id.pneumoniaCheck);
        final CheckBox tbCheck = (CheckBox) findViewById(R.id.tbCheck);
        final CheckBox cancerCheck = (CheckBox) findViewById(R.id.lungCancerCheck);
        final CheckBox otherRespCheck = (CheckBox) findViewById(R.id.otherRespCheck);

        final CheckBox femaleCheck = (CheckBox) findViewById(R.id.femaleCheck);
        final CheckBox maleCheck = (CheckBox) findViewById(R.id.maleCheck);
        final CheckBox otherGenderCheck = (CheckBox) findViewById(R.id.otherGenderCheck);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataCollectedAt.setText("", TextView.BufferType.EDITABLE);
                aqiPredictText.setText("", TextView.BufferType.EDITABLE);
                bmiText.setText("", TextView.BufferType.EDITABLE);
                ageText.setText("", TextView.BufferType.EDITABLE);
                bronchitisCheck.setChecked(false);
                asthmaCheck.setChecked(false);
                pneumoniaCheck.setChecked(false);
                cancerCheck.setChecked(false);
                tbCheck.setChecked(false);
                otherRespCheck.setChecked(false);
                femaleCheck.setChecked(false);
                maleCheck.setChecked(false);
                otherGenderCheck.setChecked(false);

            }
        });

        geotagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String aqiPredictString = aqiPredictText.getText().toString();
                String dataLocationString = dataCollectedAt.getText().toString();
                String bmiString = bmiText.getText().toString();
                String ageString = ageText.getText().toString();
                boolean bronchitisVal = bronchitisCheck.isChecked();    //To Convert to String : Boolean.toString(bronchitisVal)
                boolean asthmaVal = asthmaCheck.isChecked();
                boolean pneumoniaVal = pneumoniaCheck.isChecked();
                boolean cancerVal = cancerCheck.isChecked();
                boolean tbVal = tbCheck.isChecked();
                boolean otherRespVal = otherRespCheck.isChecked();
                boolean femaleVal = femaleCheck.isChecked();
                boolean maleVal = maleCheck.isChecked();
                boolean otherGenderVal = otherGenderCheck.isChecked();
                Date currentTime = Calendar.getInstance().getTime();


                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    } else {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                UserHelperClass helperClass = new UserHelperClass(aqiPredictString, avgAQI.getText().toString(), "" + currentLatitude, "" + currentLongitude, dataLocationString, bmiString, ageString, bronchitisVal, asthmaVal, pneumoniaVal, cancerVal, tbVal, otherRespVal, femaleVal, maleVal, otherGenderVal, currentTime);
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("AqiUserData");
                reference.push().setValue(helperClass);

                Intent geotagIntent = new Intent(MainActivity.this, GeoTagPage.class);
                startActivity(geotagIntent);
            }
        });

        maleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maleCheck.isChecked()) {
                    femaleCheck.setChecked(false);
                    otherGenderCheck.setChecked(false);
                } else if (femaleCheck.isChecked()) {
                    otherGenderCheck.setChecked(false);
                    maleCheck.setChecked(false);
                } else if (otherGenderCheck.isChecked()) {
                    maleCheck.setChecked(false);
                    femaleCheck.setChecked(false);
                }
            }
        });

        femaleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maleCheck.isChecked()) {
                    femaleCheck.setChecked(false);
                    otherGenderCheck.setChecked(false);
                } else if (femaleCheck.isChecked()) {
                    otherGenderCheck.setChecked(false);
                    maleCheck.setChecked(false);
                } else if (otherGenderCheck.isChecked()) {
                    maleCheck.setChecked(false);
                    femaleCheck.setChecked(false);
                }
            }
        });

        otherGenderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maleCheck.isChecked()) {
                    femaleCheck.setChecked(false);
                    otherGenderCheck.setChecked(false);
                } else if (femaleCheck.isChecked()) {
                    otherGenderCheck.setChecked(false);
                    maleCheck.setChecked(false);
                } else if (otherGenderCheck.isChecked()) {
                    maleCheck.setChecked(false);
                    femaleCheck.setChecked(false);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }
                        return;
                    }

                    try {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                    } catch (Exception e){
                        currentLatitude = 28.5455;
                        currentLongitude = 77.2731;
                    }

                    final TextView avgAQI = (TextView) findViewById(R.id.avgAQI);

                    if(currentLatitude != 0 && currentLongitude != 0) {
                        OkHttpClient client = new OkHttpClient();

                        //String url = "https://api.waqi.info/feed/delhi/?token=262ba73b900a0ff15d3ac7bbbee593ddbb543aa9";

                        String url = "http://api.weatherbit.io/v2.0/history/airquality?lat=" + currentLatitude + "&lon=" + currentLongitude + "&key=6a0d2a4497cc4e51b394784a9fb433a9";

                        Request request = new Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    final String myResponse = response.body().string();
                                    String finalAQI = "Unavailable";
                                    try {
                                        JSONObject aqiJSON = new JSONObject(myResponse);
                                        JSONArray aqiData = aqiJSON.getJSONArray("data");
                                        JSONObject hourAQI = aqiData.getJSONObject(24);
                                        finalAQI = "" + hourAQI.get("aqi").toString();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    final String finalAQI1 = finalAQI;
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            avgAQI.setText(finalAQI1);
                                        }
                                    });
                                }

                            }
                        });
                    }
                    Button resetButton = (Button) findViewById(R.id.resetButton);
                    Button geotagButton = (Button) findViewById(R.id.geotagButton);

                    final EditText dataCollectedAt = (EditText) findViewById(R.id.dataCollectedAt);
                    final EditText aqiPredictText = (EditText) findViewById(R.id.aqiPredictText);
                    final EditText bmiText = (EditText) findViewById(R.id.bmiText);
                    final EditText ageText = (EditText) findViewById(R.id.ageText);

                    final CheckBox bronchitisCheck = (CheckBox) findViewById(R.id.bronchitisCheck);
                    final CheckBox asthmaCheck = (CheckBox) findViewById(R.id.asthmaCheck);
                    final CheckBox pneumoniaCheck = (CheckBox) findViewById(R.id.pneumoniaCheck);
                    final CheckBox tbCheck = (CheckBox) findViewById(R.id.tbCheck);
                    final CheckBox cancerCheck = (CheckBox) findViewById(R.id.lungCancerCheck);
                    final CheckBox otherRespCheck = (CheckBox) findViewById(R.id.otherRespCheck);

                    final CheckBox femaleCheck = (CheckBox) findViewById(R.id.femaleCheck);
                    final CheckBox maleCheck = (CheckBox) findViewById(R.id.maleCheck);
                    final CheckBox otherGenderCheck = (CheckBox) findViewById(R.id.otherGenderCheck);

                    resetButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dataCollectedAt.setText("", TextView.BufferType.EDITABLE);
                            aqiPredictText.setText("", TextView.BufferType.EDITABLE);
                            bmiText.setText("", TextView.BufferType.EDITABLE);
                            ageText.setText("", TextView.BufferType.EDITABLE);
                            bronchitisCheck.setChecked(false);
                            asthmaCheck.setChecked(false);
                            pneumoniaCheck.setChecked(false);
                            cancerCheck.setChecked(false);
                            tbCheck.setChecked(false);
                            otherRespCheck.setChecked(false);
                            femaleCheck.setChecked(false);
                            maleCheck.setChecked(false);
                            otherGenderCheck.setChecked(false);

                        }
                    });

                    geotagButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String aqiPredictString = aqiPredictText.getText().toString();
                            String dataLocationString = dataCollectedAt.getText().toString();
                            String bmiString = bmiText.getText().toString();
                            String ageString = ageText.getText().toString();
                            boolean bronchitisVal = bronchitisCheck.isChecked();    //To Convert to String : Boolean.toString(bronchitisVal)
                            boolean asthmaVal = asthmaCheck.isChecked();
                            boolean pneumoniaVal = pneumoniaCheck.isChecked();
                            boolean cancerVal = cancerCheck.isChecked();
                            boolean tbVal = tbCheck.isChecked();
                            boolean otherRespVal = otherRespCheck.isChecked();
                            boolean femaleVal = femaleCheck.isChecked();
                            boolean maleVal = maleCheck.isChecked();
                            boolean otherGenderVal = otherGenderCheck.isChecked();
                            Date currentTime = Calendar.getInstance().getTime();

                            try {
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                } else {
                                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    currentLatitude = location.getLatitude();
                                    currentLongitude = location.getLongitude();
                                }
                            }
                            catch (Exception e) {
                                currentLatitude = 28.5455;
                                currentLongitude = 77.2731;
                            }

                            if (currentLatitude != 0 && currentLongitude != 0) {
                                OkHttpClient client = new OkHttpClient();

                                //String url = "https://api.waqi.info/feed/delhi/?token=262ba73b900a0ff15d3ac7bbbee593ddbb543aa9";

                                String url = "http://api.weatherbit.io/v2.0/history/airquality?lat=" + currentLatitude + "&lon=" + currentLongitude + "&key=6a0d2a4497cc4e51b394784a9fb433a9";

                                Request request = new Request.Builder()
                                        .url(url)
                                        .build();

                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        if (response.isSuccessful()) {
                                            final String myResponse = response.body().string();
                                            String finalAQI = "Unavailable";
                                            try {
                                                JSONObject aqiJSON = new JSONObject(myResponse);
                                                JSONArray aqiData = aqiJSON.getJSONArray("data");
                                                JSONObject hourAQI = aqiData.getJSONObject(24);
                                                finalAQI = "" + hourAQI.get("aqi").toString();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            final String finalAQI1 = finalAQI;
                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    avgAQI.setText(finalAQI1);
                                                }
                                            });
                                        }

                                    }
                                });
                            }


                            UserHelperClass helperClass = new UserHelperClass(aqiPredictString, avgAQI.getText().toString(), ""+currentLatitude, ""+currentLongitude ,dataLocationString,bmiString,ageString,bronchitisVal,asthmaVal,pneumoniaVal,cancerVal,tbVal,otherRespVal,femaleVal,maleVal,otherGenderVal,currentTime);
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("AqiUserData");
                            reference.push().setValue(helperClass);

                            Intent geotagIntent = new Intent(MainActivity.this, GeoTagPage.class);
                            startActivity(geotagIntent);
                        }
                    });

                    maleCheck.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(maleCheck.isChecked()){
                                femaleCheck.setChecked(false);
                                otherGenderCheck.setChecked(false);
                            }
                            else if(femaleCheck.isChecked()){
                                otherGenderCheck.setChecked(false);
                                maleCheck.setChecked(false);
                            }
                            else if(otherGenderCheck.isChecked()){
                                maleCheck.setChecked(false);
                                femaleCheck.setChecked(false);
                            }
                        }
                    });

                    femaleCheck.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(maleCheck.isChecked()){
                                femaleCheck.setChecked(false);
                                otherGenderCheck.setChecked(false);
                            }
                            else if(femaleCheck.isChecked()){
                                otherGenderCheck.setChecked(false);
                                maleCheck.setChecked(false);
                            }
                            else if(otherGenderCheck.isChecked()){
                                maleCheck.setChecked(false);
                                femaleCheck.setChecked(false);
                            }
                        }
                    });

                    otherGenderCheck.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(maleCheck.isChecked()){
                                femaleCheck.setChecked(false);
                                otherGenderCheck.setChecked(false);
                            }
                            else if(femaleCheck.isChecked()){
                                otherGenderCheck.setChecked(false);
                                maleCheck.setChecked(false);
                            }
                            else if(otherGenderCheck.isChecked()){
                                maleCheck.setChecked(false);
                                femaleCheck.setChecked(false);
                            }
                        }
                    });

                }

            }
            break;
        }
    }

}
