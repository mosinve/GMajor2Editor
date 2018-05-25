//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.GmajorPatch;
import com.glydian.model.IntRange;

public class GmNoiseGate extends GmEffect {
  public static final IntRange noiseThresholdRange = new IntRange(-60, 0) {
    public String getUnit() {
      return "dB";
    }
  };
  public static final IntRange noiseMaxDampingRange = new IntRange(0, 90) {
    public String getUnit() {
      return "dB";
    }
  };
  public static final IntRange noiseReleaseRange = new IntRange(3, 200) {
    public String getUnit() {
      return "dB/sec";
    }
  };
  private GmNoiseGate.NOISE_GATE_MODE mMode;
  private int mThreshold;
  private int mMaxDamping;
  private int mRelease;

  public GmNoiseGate(GmajorPatch parent) {
    super(parent, SINGLE_ALGO_EFFECT_TYPE.NOISE_GATE);
    this.mMode = GmNoiseGate.NOISE_GATE_MODE.SOFT;
    this.mThreshold = Integer.parseInt(noiseThresholdRange.getMinValue());
    this.mMaxDamping = Integer.parseInt(noiseMaxDampingRange.getMinValue());
    this.mRelease = Integer.parseInt(noiseReleaseRange.getMinValue());
  }

  public void copyValuesFrom(GmEffect libEffect) {
    if (libEffect instanceof GmNoiseGate) {
      GmNoiseGate copyFromEffect = (GmNoiseGate)libEffect;
      this.mThreshold = copyFromEffect.mThreshold;
      this.mMaxDamping = copyFromEffect.mMaxDamping;
      this.mRelease = copyFromEffect.mRelease;
    }

  }

  public void writeToSysex(byte[] pData) {
    GmajorPatch.writeInt(pData, this.mMode.value, 548, 4);
    GmajorPatch.writeInt(pData, this.mThreshold, 552, 4);
    GmajorPatch.writeInt(pData, this.mMaxDamping, 556, 4);
    GmajorPatch.writeInt(pData, this.mRelease, 560, 4);
    GmajorPatch.writeInt(pData, this.mEffectOn ? 0 : 1, 608, 4);
  }

  public void initialize(byte[] pSysExData) {
    this.mMode = GmNoiseGate.NOISE_GATE_MODE.getMode(GmajorPatch.readInt(pSysExData, 548, 4));
    this.mThreshold = GmajorPatch.readInt(pSysExData, 552, 4);
    this.mMaxDamping = GmajorPatch.readInt(pSysExData, 556, 4);
    this.mRelease = GmajorPatch.readInt(pSysExData, 560, 4);
    this.setEffectOnOffFlag(pSysExData[608]);
  }

  public void debug() {
    super.debug();
    System.out.println("mMode       \t:" + this.mMode);
    System.out.println("mThreshold  \t:" + this.mThreshold);
    System.out.println("mMaxDamping \t:" + this.mMaxDamping);
    System.out.println("mRelease    \t:" + this.mRelease);
  }

  public GmNoiseGate.NOISE_GATE_MODE getMode() {
    return this.mMode;
  }

  public void setMode(GmNoiseGate.NOISE_GATE_MODE mode) {
    this.mMode = mode;
  }

  public int getRelease() {
    return this.mRelease;
  }

  public void setRelease(int release) {
    this.mRelease = release;
  }

  public void setRelease(String release) {
    this.mRelease = Integer.parseInt(release);
  }

  public int getThreshold() {
    return this.mThreshold;
  }

  public void setThreshold(int threshold) {
    this.mThreshold = threshold;
  }

  public void setThreshold(String threshold) {
    this.mThreshold = Integer.parseInt(threshold);
  }

  public int getMaxDamping() {
    return this.mMaxDamping;
  }

  public void setMaxDamping(int maxDamping) {
    this.mMaxDamping = maxDamping;
  }

  public void setMaxDamping(String maxDamping) {
    this.mMaxDamping = Integer.parseInt(maxDamping);
  }

  public enum NOISE_GATE_MODE {
    SOFT("SOFT", 0),
    HARD("HARD", 1);

    final String name;
    final int value;

    NOISE_GATE_MODE(String nm, int vl) {
      this.name = nm;
      this.value = vl;
    }

    static GmNoiseGate.NOISE_GATE_MODE getMode(int mode) {
      switch(mode) {
        case 0:
          return SOFT;
        case 1:
          return HARD;
        default:
          return null;
      }
    }
  }
}
