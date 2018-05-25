//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

abstract class AppParameters {
  private final String propertiesFilename;
  private final String propertiesDescription;
  Properties properties = null;

  AppParameters() {
    this.propertiesFilename = "GLydian.properties";
    this.propertiesDescription = "GLydian user properties";
  }

  protected abstract void setDefaults(Properties var1);

  protected abstract void updatePropertiesFromSettings();

  protected abstract void updateSettingsFromProperties();

  void getParameters() {
    Properties defaults = new Properties();
    FileInputStream in = null;
    this.setDefaults(defaults);
    this.properties = new Properties(defaults);
    System.out.println("Before load");
    this.properties.list(System.out);

    try {
      String folder = System.getProperty("user.home");
      String fileSep = System.getProperty("file.separator");
      in = new FileInputStream(folder + fileSep + this.propertiesFilename);
      this.properties.load(in);
      System.out.println("After Load");
      this.properties.list(System.out);
    } catch (FileNotFoundException var15) {
      in = null;
      System.out.println("Can't find properties file. Using defaults.");
    } catch (IOException var16) {
      System.out.println("Can't read properties file. Using defaults.");
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException ignored) {
        }

        in = null;
      }

    }

    this.updateSettingsFromProperties();
  }

  void saveParameters() {
    this.updatePropertiesFromSettings();
    boolean DEBUG = false;
    if (DEBUG) {
      System.out.println("Just set properties: " + this.propertiesDescription);
    }

    FileOutputStream out = null;

    try {
      String folder = System.getProperty("user.home");
      String fileSep = System.getProperty("file.separator");
      out = new FileOutputStream(folder + fileSep + this.propertiesFilename);
      this.properties.store(out, this.propertiesDescription);
    } catch (IOException var12) {
      System.out.println("Can't save properties. Oh well, it's not a big deal.");
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException var11) {
          System.out.println("Error closing " + this.propertiesFilename);
        }
        out = null;
      }

    }

  }
}
