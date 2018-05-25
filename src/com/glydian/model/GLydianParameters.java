//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import java.util.Properties;

public class GLydianParameters extends AppParameters {
  public static final String layOutWideScreenFormat = "WIDE_SCREEN_FORMAT";
  private String midiInputDevice;
  public static final String midiInputDeviceName = "MIDI_DEVICE_IN";
  private String midiOutputDevice;
  public static final String midiOutputDeviceName = "MIDI_DEVICE_OUT";
  private String workDir;
  public static final String workDirName = "WORK_DIR";
  public static final String labelFontProperty = "LABEL_FONT";
  public static final String labelFontSizeProperty = "LABEL_FONT_SIZE";
  public static final String labelFontStyleProperty = "LABEL_FONT_STYLE";
  public static final String menuFontProperty = "MENU_FONT";
  public static final String menuFontSizeProperty = "MENU_FONT_SIZE";
  public static final String menuFontStyleProperty = "MENU_FONT_STYLE";
  public static final String editFontProperty = "EDIT_FONT";
  public static final String editFontSizeProperty = "EDIT_FONT_SIZE";
  public static final String itemCountInMenu = "NO_OF_ITEMS_IN_A_MENU";
  public static final String effectBgImage = "EFFECT_BACKGROUND_IMG";
  public static final String effectBgImageOnOverlay = "EFFECT_BACKGROUND_ON_OVERLAY_IMG";
  public static final String mainDisplayBgImage = "MAINDISPLAY_BACKGROUND_IMG";
  public static final String mainDisplayBgImageOverlay = "MAINDISPLAY_BACKGROUND_OVERLAY_IMG";
  public static final String titlebarBgImage = "TITLEBAR_BACKGROUND_IMG";
  public static final String u1Width = "RACK_U1_WIDTH";
  public static final String u1Height = "RACK_U1_HEIGHT";
  public static final String mainDisplayWidth = "RACK_MAIN_DISPLAY_WIDTH";
  public static final String mainDisplayHeight = "RACK_MAIN_DISPLAY_HEIGHT";
  public static final String mainDisplayHeightWide = "RACK_MAIN_DISPLAY_HEIGHT_WIDE_FORMAT";
  public static final String eqWidth = "RACK_EQPANEL_WIDTH";
  public static final String eqHeightWideMode = "RACK_EQPANEL_HEIGHT_WIDE_FORMAT";
  private static GLydianParameters self;

  public String getMidiInputDevice() {
    return this.midiInputDevice;
  }

  public void setMidiInputDevice(String midiInputDevice) {
    this.midiInputDevice = midiInputDevice;
    this.saveParameters();
  }

  public String getMidiOutputDevice() {
    return this.midiOutputDevice;
  }

  public void setMidiOutputDevice(String midiOutputDevice) {
    this.midiOutputDevice = midiOutputDevice;
    this.saveParameters();
  }

  public String getWorkFolder() {
    return this.workDir;
  }

  public void setWorkFolder(String folderName) {
    this.workDir = folderName;
    this.saveParameters();
  }

  public String getProperty(String propName) {
    return this.properties.getProperty(propName);
  }

  public void setProperty(String propName, String propValue) {
    this.properties.setProperty(propName, propValue);
  }

  public int getNumerticPropertyValue(String propName) {
    return Integer.parseInt(this.properties.getProperty(propName));
  }

  public static GLydianParameters getInstance() {
    if (self == null) {
      self = new GLydianParameters();
      self.getParameters();
    }

    return self;
  }

  private GLydianParameters() {
    super();
  }

  public void setDefaults(Properties defaults) {
    defaults.put("WIDE_SCREEN_FORMAT", "true");
    defaults.put("MIDI_DEVICE_IN", "");
    defaults.put("MIDI_DEVICE_OUT", "");
    defaults.put("WORK_DIR", ".");
    defaults.put("LABEL_FONT", "Tahoma");
    defaults.put("LABEL_FONT_SIZE", "10");
    defaults.put("LABEL_FONT_STYLE", String.valueOf(0));
    defaults.put("MENU_FONT", "Tahoma");
    defaults.put("MENU_FONT_SIZE", "10");
    defaults.put("MENU_FONT_STYLE", String.valueOf(0));
    defaults.put("EDIT_FONT", "Arial");
    defaults.put("EDIT_FONT_SIZE", "11");
    defaults.put("NO_OF_ITEMS_IN_A_MENU", "15");
    defaults.put("EFFECT_BACKGROUND_IMG", "/resources/GLydianTexture6.png");
    defaults.put("EFFECT_BACKGROUND_ON_OVERLAY_IMG", "/resources/GLydianTextureGradiant3.png");
    defaults.put("MAINDISPLAY_BACKGROUND_IMG", "/resources/GLydianTexture6.png");
    defaults.put("MAINDISPLAY_BACKGROUND_OVERLAY_IMG", "/resources/GLydianTextureGradiant3.png");
    defaults.put("TITLEBAR_BACKGROUND_IMG", "/resources/TitleBarBacground.png");
    defaults.put("RACK_U1_WIDTH", "765");
    defaults.put("RACK_U1_HEIGHT", "115");
    defaults.put("RACK_MAIN_DISPLAY_WIDTH", "320");
    defaults.put("RACK_MAIN_DISPLAY_HEIGHT_WIDE_FORMAT", "170");
    defaults.put("RACK_MAIN_DISPLAY_HEIGHT", "200");
    defaults.put("RACK_EQPANEL_WIDTH", "180");
    defaults.put("RACK_EQPANEL_HEIGHT_WIDE_FORMAT", "210");
  }

  public void updatePropertiesFromSettings() {
    this.properties.put("WIDE_SCREEN_FORMAT", this.properties.getProperty("WIDE_SCREEN_FORMAT"));
    this.properties.put("MIDI_DEVICE_IN", this.midiInputDevice);
    this.properties.put("MIDI_DEVICE_OUT", this.midiOutputDevice);
    this.properties.put("WORK_DIR", this.workDir);
    this.properties.put("LABEL_FONT", this.properties.getProperty("LABEL_FONT"));
    this.properties.put("LABEL_FONT_SIZE", this.properties.getProperty("LABEL_FONT_SIZE"));
    this.properties.put("LABEL_FONT_STYLE", this.properties.getProperty("LABEL_FONT_STYLE"));
    this.properties.put("MENU_FONT", this.properties.getProperty("MENU_FONT"));
    this.properties.put("MENU_FONT_SIZE", this.properties.getProperty("MENU_FONT_SIZE"));
    this.properties.put("MENU_FONT_STYLE", this.properties.getProperty("MENU_FONT_STYLE"));
    this.properties.put("EDIT_FONT", this.properties.getProperty("EDIT_FONT"));
    this.properties.put("EDIT_FONT_SIZE", this.properties.getProperty("EDIT_FONT_SIZE"));
    this.properties.put("NO_OF_ITEMS_IN_A_MENU", this.properties.getProperty("NO_OF_ITEMS_IN_A_MENU"));
    this.properties.put("EFFECT_BACKGROUND_IMG", this.properties.getProperty("EFFECT_BACKGROUND_IMG"));
    this.properties.put("EFFECT_BACKGROUND_ON_OVERLAY_IMG", this.properties.getProperty("EFFECT_BACKGROUND_ON_OVERLAY_IMG"));
    this.properties.put("MAINDISPLAY_BACKGROUND_IMG", this.properties.getProperty("MAINDISPLAY_BACKGROUND_IMG"));
    this.properties.put("MAINDISPLAY_BACKGROUND_OVERLAY_IMG", this.properties.getProperty("MAINDISPLAY_BACKGROUND_OVERLAY_IMG"));
    this.properties.put("TITLEBAR_BACKGROUND_IMG", this.properties.getProperty("TITLEBAR_BACKGROUND_IMG"));
    this.properties.put("RACK_U1_WIDTH", this.properties.getProperty("RACK_U1_WIDTH"));
    this.properties.put("RACK_U1_HEIGHT", this.properties.getProperty("RACK_U1_HEIGHT"));
    this.properties.put("RACK_MAIN_DISPLAY_WIDTH", this.properties.getProperty("RACK_MAIN_DISPLAY_WIDTH"));
    this.properties.put("RACK_MAIN_DISPLAY_HEIGHT", this.properties.getProperty("RACK_MAIN_DISPLAY_HEIGHT"));
    this.properties.put("RACK_MAIN_DISPLAY_HEIGHT_WIDE_FORMAT", this.properties.getProperty("RACK_MAIN_DISPLAY_HEIGHT_WIDE_FORMAT"));
    this.properties.put("RACK_EQPANEL_WIDTH", this.properties.getProperty("RACK_EQPANEL_WIDTH"));
    this.properties.put("RACK_EQPANEL_HEIGHT_WIDE_FORMAT", this.properties.getProperty("RACK_EQPANEL_HEIGHT_WIDE_FORMAT"));
  }

  public void updateSettingsFromProperties() {
    this.midiInputDevice = this.properties.getProperty("MIDI_DEVICE_IN");
    this.midiOutputDevice = this.properties.getProperty("MIDI_DEVICE_OUT");
    this.workDir = this.properties.getProperty("WORK_DIR");
  }
}
