package sgtmelon.livewallpaper.app.model;

public class ItemInfo {

    private final String label, value;

    public ItemInfo(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

}
