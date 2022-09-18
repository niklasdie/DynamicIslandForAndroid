package niklasdie.dynamicislandforandroid.Presets;

public enum PresetEnum {
    CUSTOM("Custom"),
    PIXEL_6_PRO ("Pixel 6 Pro"),
    TEST("Test");

    private final String value;

    PresetEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
