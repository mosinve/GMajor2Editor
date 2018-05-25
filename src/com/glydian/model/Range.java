//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

public interface Range {
  int getSize();

  String getMinValue();

  String getMaxValue();

  String getZeroValue();

  boolean isIndexValid(int var1);

  String getItem(int var1);

  int getIndexForValue(String var1);

  boolean contains(String var1);

  String getUnit();

  String getMaxCharacterWidthValue();
}
