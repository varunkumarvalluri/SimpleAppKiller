package com.example.varun.pagertabstriptutorial;

/**
 * Created by varun on 12/22/15.
 */

import android.graphics.drawable.Drawable;

/**
 * Created by varun on 12/21/15.
 */
public class Country {
    Drawable app_icon;
    String code = null;
    String name = null;
    boolean selected = false;

    public Country(String code, String name, Drawable app_icon, boolean selected) {
        super();
        this.app_icon = app_icon;
        this.code = code;
        this.name = name;
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
