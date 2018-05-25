//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

public abstract class IntRange implements Range {
  private final int minValue;
  private final int maxValue;
  private final int zeroValue;
  private final int size;
  private String maxCharacterWidthValue = null;

  public IntRange(int min, int max) {
    this.minValue = min;
    this.maxValue = max;
    this.size = Math.max(this.minValue, this.maxValue) - Math.min(this.minValue, this.maxValue) + 1;
    if (this.minValue < 0 && this.maxValue > 0) {
      this.zeroValue = 0;
    } else {
      this.zeroValue = this.minValue;
    }

  }

  public String getZeroValue() {
    return String.valueOf(this.zeroValue);
  }

  public void debug() {
    System.out.println(" minValue: " + this.minValue + " maxValue: " + this.maxValue + " mSize: " + this.size);
    System.out.println(" getMinValue():" + this.getMinValue() + " getMaxValue():" + this.getMaxValue());
    System.out.println(" getIndexForValue(getMinValue()):" + this.getIndexForValue(this.getMinValue()) + " getIndexForValue(getMaxValue()):" + this.getIndexForValue(this.getMaxValue()));
  }

  public String getMaxValue() {
    return String.valueOf(this.maxValue);
  }

  public String getMinValue() {
    return String.valueOf(this.minValue);
  }

  public int getSize() {
    return this.size;
  }

  public boolean isIndexValid(int idx) {
    return idx >= 0 && idx < this.size;
  }

  public String getItem(int pIndex) {
    if (this.isIndexValid(pIndex)) {
      return String.valueOf(this.minValue + pIndex);
    } else {
      throw new RuntimeException("IntRange: Invalid index " + pIndex);
    }
  }

  public int getIndexForValue(String pValue) {
    int value = Integer.parseInt(pValue);
    if (value >= this.minValue && value <= this.maxValue) {
      return value - this.minValue;
    } else {
      throw new RuntimeException("IntRange: Invalid Value " + pValue);
    }
  }

  public boolean contains(String pValue) {
    int value = Integer.parseInt(pValue);
    return value >= this.minValue && value <= this.maxValue;
  }

  public String getMaxCharacterWidthValue() {
    if (this.maxCharacterWidthValue == null) {
      if (String.valueOf(Math.abs(this.minValue)).length() >= String.valueOf(Math.abs(this.maxValue)).length()) {
        this.maxCharacterWidthValue = String.valueOf(this.getMinValue());
      } else {
        this.maxCharacterWidthValue = String.valueOf(this.getMaxValue());
      }
    }

    return this.maxCharacterWidthValue;
  }
}
