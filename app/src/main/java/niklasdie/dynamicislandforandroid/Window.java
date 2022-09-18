package niklasdie.dynamicislandforandroid;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import niklasdie.dynamicislandforandroid.Presets.Preset;
import niklasdie.dynamicislandforandroid.Presets.PresetEnum;

public class Window implements AdapterView.OnItemSelectedListener {

    private final Context context;
    private View mView;
    private final WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private final LayoutInflater layoutInflater;

    public float oldSize;
    public float oldXPos;
    public float oldYPos;

    public Window(Context context) {
        this.context = context;

        // set the layout parameters of the window
        mParams = new WindowManager.LayoutParams(
                // Shrink the window to wrap the content rather
                // than filling the screen
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                // Display it on top of other application windows
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                // Don't let it grab the input focus
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                // Make the underlying application window visible
                // through any transparent parts
                PixelFormat.TRANSLUCENT);

        // getting a LayoutInflater
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.dynamic_island_window, null);
        mView.setForegroundGravity(Gravity.TOP);

        // Define the position of the window within the screen
        mParams.gravity = Gravity.TOP;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        // View logic
        MainActivity.presetSpinner.setOnItemSelectedListener(this);

        MainActivity.sizeSlider.addOnChangeListener((slider, value, fromUser) -> {
            mView.setScaleX(slider.getValue());
            mView.setScaleY(slider.getValue());
            MainActivity.yPosSlider.setValueFrom(-(150 - MainActivity.sizeSlider.getValue() * 150) - 40);
            if (MainActivity.yPosSlider.getValue() < MainActivity.yPosSlider.getValueFrom()) {
                MainActivity.yPosSlider.setValue(MainActivity.yPosSlider.getValueFrom());
            }
        });
        MainActivity.xPosSlider.addOnChangeListener((slider, value, fromUser) ->
                mView.setTranslationX(slider.getValue()));
        MainActivity.yPosSlider.addOnChangeListener((slider, value, fromUser) ->
                mView.setTranslationY(slider.getValue()));

        MainActivity.confModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            close();
            if (MainActivity.confModeSwitch.isChecked()) {
                mView = layoutInflater.inflate(R.layout.dynamic_island_window_configure, null);
                mView.setForegroundGravity(Gravity.TOP);
            } else {
                mView = layoutInflater.inflate(R.layout.dynamic_island_window, null);
                mView.setForegroundGravity(Gravity.TOP);
            }
            open();
            MainActivity.sizeSlider.setValue(oldSize);
            MainActivity.xPosSlider.setValue(oldXPos);
            MainActivity.yPosSlider.setValue(oldYPos);
        });

        MainActivity.saveButton.setOnClickListener(event -> {
            if (Preset.presents.containsKey(PresetEnum.CUSTOM)) {
                Preset.presents.replace(PresetEnum.CUSTOM, new Preset(
                        MainActivity.sizeSlider.getValue(),
                        MainActivity.xPosSlider.getValue(),
                        MainActivity.yPosSlider.getValue(),
                        false,
                        0f));
            } else {
                Preset.presents.put(PresetEnum.CUSTOM, new Preset(
                        MainActivity.sizeSlider.getValue(),
                        MainActivity.xPosSlider.getValue(),
                        MainActivity.yPosSlider.getValue(),
                        false,
                        0f));
            }
        });

    }

    public void open() {

        try {
            // check if the view is already
            // inflated or present in the window
            if (mView.getWindowToken() == null) {
                if (mView.getParent() == null) {
                    mWindowManager.addView(mView, mParams);
                    // set slider scale and position
                    mView.setScaleX(MainActivity.sizeSlider.getValue());
                    mView.setScaleY(MainActivity.sizeSlider.getValue());
                    mView.setTranslationX(MainActivity.xPosSlider.getValue());
                    mView.setTranslationY(MainActivity.yPosSlider.getValue());
                    loadPreset(PresetEnum.PIXEL_6_PRO);
                    MainActivity.yPosSlider.setValueFrom(-(150 - MainActivity.sizeSlider.getValue() * 150) - 40);
                    if (MainActivity.yPosSlider.getValue() < MainActivity.yPosSlider.getValueFrom()) {
                        MainActivity.yPosSlider.setValue(MainActivity.yPosSlider.getValueFrom());
                    }
                }
            }
        } catch (Exception e) {
            Log.d("Error1", e.toString());
        }

    }

    public void close() {

        try {
            oldSize = MainActivity.sizeSlider.getValue();
            oldXPos = MainActivity.xPosSlider.getValue();
            oldYPos = MainActivity.yPosSlider.getValue();
            // remove the view from the window
            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            // invalidate the view
            mView.invalidate();
            // remove all views
            ((ViewGroup) mView.getParent()).removeAllViews();

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (Exception e) {
            Log.d("Error2", e.toString());
        }
    }

    public void loadPreset(PresetEnum presetEnum) {
        Preset preset = MainActivity.preset.get(presetEnum);
        oldSize = MainActivity.sizeSlider.getValue();
        oldXPos = MainActivity.xPosSlider.getValue();
        oldYPos = MainActivity.yPosSlider.getValue();
        MainActivity.sizeSlider.setValue(preset.size);
        MainActivity.xPosSlider.setValue(preset.xPos);
        MainActivity.yPosSlider.setValue(preset.yPos);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        this.loadPreset((PresetEnum) MainActivity.presetSpinner.getAdapter().getItem(pos));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
//        MainActivity.sizeSlider.setValue(oldSize);
//        MainActivity.xPosSlider.setValue(oldXPos);
//        MainActivity.yPosSlider.setValue(oldYPos);
    }
}