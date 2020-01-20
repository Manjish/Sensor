package com.fourthassignment.sensortest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class LightSensor extends AppCompatActivity {

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor);
        if (!checkPermission(getApplicationContext())){
            setPermission(getApplicationContext());
        }
        lightSensor();
    }

    private void lightSensor(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
                    if (sensorEvent.values[0]<800){
                        changeScreenBrightness(getApplicationContext(),100);
                    }
                    else if (sensorEvent.values[0]>800 && sensorEvent.values[0]<900){
                        changeScreenBrightness(getApplicationContext(),200);
                    }
                    else {
                        changeScreenBrightness(getApplicationContext(),255);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermission(Context context)
    {
        boolean permission = true;
        // Get the result from below code.
        permission = Settings.System.canWrite(context);
        return permission;
    }

    // Start can modify system settings panel to let user change the write settings permission.
    private void setPermission(Context context)
    {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        context.startActivity(intent);
    }

    private void changeScreenBrightness(Context context, int screenBrightnessValue)
    {
       Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
       Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue);

        /*
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = screenBrightnessValue / 255f;
        window.setAttributes(layoutParams);
        */
    }
}
