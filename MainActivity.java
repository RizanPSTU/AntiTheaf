package com.example.velocity_carbon.code;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private static final int REQUEST_CALL = 1;
    private EditText mEditTextNumber;
    Sensor accelerometer;

    TextView xValue;
    EditText editTextG;
    int sam = 5;



    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xValue = (TextView) findViewById(R.id.xValue);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        mEditTextNumber = findViewById(R.id.edit_text_number);
        ImageView imageCall = findViewById(R.id.image_call);

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });
        editTextG = (EditText) findViewById(R.id.editText);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);

    }


    private  void getmylocation(){
        GPS_traker g=new GPS_traker(getApplicationContext());
        Location l = g.getLocation();
        if(l != null){
            double lat= l.getLatitude();
            double lon= l.getLongitude();
            Toast.makeText(this, "lat: " +lat+"lon :"+lon, Toast.LENGTH_SHORT).show();
            editTextG.setText("Latitude :"+lat+" "+"Longitude :"+lon);
        }   }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xValue.setText("X : "+sensorEvent.values[0]);
        sam++;
        if (sensorEvent.values[0] >=4 && sam%10==0){
            sam=0;
            getmylocation();

        }

    }


    private  void sendSMS(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)  != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.SEND_SMS)){
                ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.SEND_SMS},2);
            }else {
               ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.SEND_SMS},2);
            }

        }else {
            String number =mEditTextNumber.getText().toString();
            String sms =editTextG.getText().toString();
            try {
                SmsManager smsManager =SmsManager.getDefault();
                smsManager.sendTextMessage(number,null,sms,null,null);
                Toast.makeText(this, "Sent!!!", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(this, "Failed :(", Toast.LENGTH_SHORT).show();
            }


        }
    }



    private void makePhoneCall(){
      String number =mEditTextNumber.getText().toString();
        if(number.trim().length() >0){
            if(ContextCompat.checkSelfPermission(  MainActivity.this, Manifest.permission.CALL_PHONE) !=
            PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE },REQUEST_CALL);
            }else {
                String dial="tel:"+ number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

            }

        }else {
            Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }


        switch (requestCode){
            case 2: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission dise", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Permission pai nai", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }


}
