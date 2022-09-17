package niklasdie.dynamicislandforandroid.Presets;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Present {

    private HashMap<PresetEnum, Present> presents;

    public float size;
    public float xPos;
    public float yPos;
    public boolean isPill;
    public float pillWidth;

    public Present() {
        this.presents = new HashMap<>();
        this.presents.put(PresetEnum.PIXEL_6_PRO, new Present(0.24f, 0, -120, false, 0));
        this.presents.put(PresetEnum.TEST, new Present(0.24f, 0, -120, false, 0));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("json/presets.json"))) {
            writer.write(new Gson().toJson(this.presents));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Present(float size, float xPos, float yPos, boolean isPill, float pillWidth) {
        this.size = size;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isPill = isPill;
        this.pillWidth = pillWidth;
    }

    public Present get(PresetEnum presetEnum) {
        return this.presents.get(presetEnum);
    }
}
