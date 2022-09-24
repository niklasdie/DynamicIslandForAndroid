package niklasdie.dynamicislandforandroid.Presets;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import niklasdie.dynamicislandforandroid.MainActivity;

public class Preset {

    public static HashMap<PresetEnum, Preset> presents;

    public float size;
    public float xPos;
    public float yPos;
    public boolean isPill;
    public float pillWidth;

    @Override
    public boolean equals(Object o) {
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return (int) (31 * size + 37 * xPos + 41 * yPos + 47 * pillWidth + (isPill ? 0 : 1));
    }

    public Preset() {
        presents = new HashMap<>();
        presents.put(PresetEnum.CUSTOM,
                new Preset(1f, 0, 0, false, 0));
        presents.put(PresetEnum.PIXEL_6_PRO,
                new Preset(0.24f, 0, -120, false, 0));
        presents.put(PresetEnum.TEST,
                new Preset(0.5f, -300, 20, false, 0));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("json/presets.json"))) {
            writer.write(new Gson().toJson(presents));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Preset(float size, float xPos, float yPos, boolean isPill, float pillWidth) {
        this.size = size;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isPill = isPill;
        this.pillWidth = pillWidth;
    }

    public Preset get(PresetEnum presetEnum) {
        return presents.get(presetEnum);
    }
}
