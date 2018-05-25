//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.GmajorPatch;
import com.glydian.model.Modifiable;

public abstract class GmEffect implements Modifiable {
  private final GmajorPatch mOwnerPatch;
  boolean mEffectOn;
  GmEffect.EFFECT_TYPE mEffectType;
  private boolean mHasChanged;

  public abstract void initialize(byte[] var1);

  public abstract void writeToSysex(byte[] var1);

  GmEffect(GmajorPatch parent, GmEffect.EFFECT_TYPE type) {
    this.mOwnerPatch = parent;
    this.mEffectType = type;
  }

  public void clearChangedFlag() {
    this.mHasChanged = false;
  }

  public boolean hasChanged() {
    return this.mHasChanged;
  }

  public void markChanged() {
    this.mHasChanged = true;
  }

  public boolean isEffectOn() {
    return this.mEffectOn;
  }

  public void setEffectOn(boolean effectOn) {
    this.mEffectOn = effectOn;
  }

  GmEffect.EFFECT_TYPE getEffectType() {
    return this.mEffectType;
  }

  public void setEffectType(GmEffect.EFFECT_TYPE effectType) {
    this.mEffectType = effectType;
  }

  void setEffectOnOffFlag(byte pOnOffByte) {
    this.mEffectOn = pOnOffByte == 0;

  }

  protected void setEffectType(int pEffectTypeByte) {
    throw new RuntimeException("setEffectType() is not implemented here");
  }

  void debug() {
    System.out.println();
    System.out.println("mEffectType\t:" + this.mEffectType);
    System.out.println("mEffectOn  \t:" + this.mEffectOn);
  }

  public String getName() {
    return this.mEffectType.getName().toLowerCase() + " of " + this.mOwnerPatch.getPatchName();
  }

  public abstract void copyValuesFrom(GmEffect var1);

  public interface EFFECT_TYPE {
    String getName();
  }

  public enum SINGLE_ALGO_EFFECT_TYPE implements GmEffect.EFFECT_TYPE {
    COMPRESSOR("COMPRESSOR"),
    NOISE_GATE("NOISE GATE"),
    EQ("EQUALIZER");

    final String name;

    public String getName() {
      return this.name;
    }

    SINGLE_ALGO_EFFECT_TYPE(String myName) {
      this.name = myName;
    }
  }
}
