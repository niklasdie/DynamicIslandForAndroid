package niklasdie.dynamicislandforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;

import niklasdie.dynamicislandforandroid.Presets.Preset;

public class MainActivity extends AppCompatActivity {

    public static Button permissions;
    public static Button configuration;
    public static Button stopService;
    public static Button startService;

    public static Spinner presetSpinner;
    public static Switch confModeSwitch;
    public static Slider sizeSlider;
    public static Slider xPosSlider;
    public static Slider yPosSlider;
    public static Button resetButton;
    public static Button saveButton;

    public static Preset preset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.configuration); // TODO change to activity_main

        permissions = this.findViewById(R.id.permissions);
        configuration = this.findViewById(R.id.configure);
        stopService = this.findViewById(R.id.stop_service);
        startService = this.findViewById(R.id.start_service);

//        configuration.setOnClickListener(event ->
//                this.setContentView(R.layout.configuration));
//        stopService.setOnClickListener(event ->
//                this.stopService(new Intent(this, ForegroundService.class)));
//        startService.setOnClickListener(event -> this.startService());

        presetSpinner = this.findViewById(R.id.preset_spinner);
        confModeSwitch = this.findViewById(R.id.confModeSwitch);
        sizeSlider = this.findViewById(R.id.sizeSlider);
        xPosSlider = this.findViewById(R.id.xPosSlider);
        yPosSlider = this.findViewById(R.id.yPosSlider);
        saveButton = this.findViewById(R.id.save_button);
        resetButton = this.findViewById(R.id.reset_button);

        preset = new Preset();

        WindowManager mWindowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);

        ArrayAdapter adapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, new ArrayList<>(Preset.presents.keySet()));
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        presetSpinner.setAdapter(adapter);

        sizeSlider.setValue(0.5f);
        sizeSlider.setValueFrom(0.1f);
        sizeSlider.setValueTo(1);
        xPosSlider.setValue(0);
        xPosSlider.setValueFrom(
                -Math.abs((float) (mWindowManager.getCurrentWindowMetrics().getBounds().width() / 2)));
        xPosSlider.setValueTo(
                Math.abs((float) (mWindowManager.getCurrentWindowMetrics().getBounds().width() / 2)));
        yPosSlider.setValue(0);
        yPosSlider.setValueFrom(0);
        yPosSlider.setValueTo(160);

        this.checkOverlayPermission();
        this.startService();
    }

    // method for starting the service
    public void startService() {
        // check if the user has already granted
        // the Draw over other apps permission
        if (Settings.canDrawOverlays(this)) {
            // start the service based on the android version
            this.startForegroundService(new Intent(this, ForegroundService.class));
        }
    }

    // method to ask user to grant the Overlay permission
    public void checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            // send user to the device settings
            this.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
        }
    }

    // check for permission again when user grants it from
    // the device settings, and start the service
    @Override
    protected void onResume() {
        super.onResume();
        this.startService();
    }

    public void restartService() {
        this.stopService(new Intent(this, ForegroundService.class));
        this.startService();
    }
}
