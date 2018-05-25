//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

public interface Modifiable {
  boolean hasChanged();

  void markChanged();

  void clearChangedFlag();
}
