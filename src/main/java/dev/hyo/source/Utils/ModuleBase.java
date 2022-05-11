package dev.hyo.source.Utils;

public abstract class ModuleBase {
    private boolean enabled = false;
    private String name;
    private String description;

    public ModuleBase(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public String getName() {
        return name;
    }
    public String getDescription(){
        return description;
    }
    public boolean isEnabled(){
        return enabled;
    }
    public void toggleEnabled() {
        enabled = !enabled;
        if (enabled)
            onEnable();
        else
            onDisable();
    }
}
