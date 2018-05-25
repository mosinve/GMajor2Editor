//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import java.io.IOException;

public class ModifierField extends BitmaskField {
  public static final IntRange modOutRange = new IntRange(0, 100) {
    public String getUnit() {
      return "%";
    }
  };
  private MODIFIER modifier;
  private int minOut;
  private int midOut;
  private int maxOut;
  private int dataByteOffSet;

  public int getMaxOut() {
    return this.maxOut;
  }

  public void setMaxOut(int maxOut) {
    this.maxOut = maxOut;
  }

  public int getMidOut() {
    return this.midOut;
  }

  public void setMidOut(int midOut) {
    this.midOut = midOut;
  }

  public int getMinOut() {
    return this.minOut;
  }

  public void setMinOut(int minOut) {
    this.minOut = minOut;
  }

  public MODIFIER getModifier() {
    return this.modifier;
  }

  public void setModifier(MODIFIER modifier) {
    this.modifier = modifier;
  }

  public ModifierField(int offSet) {
    super(28);
    this.modifier = MODIFIER.OFF;
    this.minOut = 0;
    this.midOut = 50;
    this.maxOut = 100;
    this.modifier = MODIFIER.OFF;
    this.dataByteOffSet = offSet;
  }

  public ModifierField(String defaultMarker) {
    super(28);
    this.modifier = MODIFIER.OFF;
    this.minOut = 0;
    this.midOut = 50;
    this.maxOut = 100;

    try {
      byte[] defaultBytes = new byte[]{0, 16, 35, 6};
      this.skipGoffyPoint = false;
      this.readBitsFromByteArray(defaultBytes, 0);
      this.skipGoffyPoint = true;
    } catch (IOException var3) {
      var3.printStackTrace();
    }

  }

  public void initialize(byte[] patchData, int offset) throws IOException {
    this.dataByteOffSet = offset;
    this.readBitsFromByteArray(patchData, this.dataByteOffSet);
    this.modifier = MODIFIER.getModifierForInt(this.getIntFromBits(0, 3));
    this.minOut = this.getIntFromBits(3, 7);
    this.midOut = this.getIntFromBits(10, 7);
    this.maxOut = this.getIntFromBits(17, 7);
  }

  public void writeToSysEx(byte[] pData) {
    this.setBitsForInt(this.modifier.value, 0, 3);
    this.setBitsForInt(this.minOut, 3, 7);
    this.setBitsForInt(this.midOut, 10, 7);
    this.setBitsForInt(this.maxOut, 17, 7);
    this.writeBitsToByteArray(pData, this.dataByteOffSet);
  }

  public void debug() {
    System.out.println(" Modifier  \t:" + this.modifier.text);
    System.out.println(" minOut    \t:" + this.minOut);
    System.out.println(" midOut    \t:" + this.midOut);
    System.out.println(" maxOut    \t:" + this.maxOut);
  }

  public enum MODIFIER {
    OFF(0, "OFF"),
    M1(1, "M1"),
    M2(2, "M2"),
    M3(3, "M3"),
    M4(4, "M4");

    final int value;
    final String text;

    MODIFIER(int i, String name) {
      this.value = i;
      this.text = name;
    }

    static MODIFIER getModifierForInt(int i) {
      switch(i) {
        case 1:
          return M1;
        case 2:
          return M2;
        case 3:
          return M3;
        case 4:
          return M4;
        default:
          return OFF;
      }
    }

    public String toString() {
      return this.text;
    }
  }
}
