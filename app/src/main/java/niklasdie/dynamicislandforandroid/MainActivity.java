package niklasdie.dynamicislandforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {

    public static Switch confModeSwitch;
    public static Slider sizeSlider;
    public static Slider xPosSlider;
    public static Slider yPosSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        confModeSwitch = this.findViewById(R.id.confModeSwitch);
        sizeSlider = this.findViewById(R.id.sizeSlider);
        xPosSlider = this.findViewById(R.id.xPosSlider);
        yPosSlider = this.findViewById(R.id.yPosSlider);

        WindowManager mWindowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);

        sizeSlider.setValue(0.5f);
        sizeSlider.setValueFrom(0.1f);
        sizeSlider.setValueTo(1);
        xPosSlider.setValue(0);
        xPosSlider.setValueFrom(
                - Math.abs((float) (mWindowManager.getCurrentWindowMetrics().getBounds().width() / 2)));
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
