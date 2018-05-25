//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.DiscreteRange;
import com.glydian.model.GmajorPatch;
import com.glydian.model.IntRange;
import com.glydian.model.DiscreteRange.CompTimeRange;

public class GmCompressorEffect extends GmEffect {
  private int mCompThreshold;
  private String mCompRatio;
  private String mCompAttack;
  private String mCompRelease;
  private int mCompGain;
  public static final IntRange compThresholdRange = new IntRange(-40, 40) {
    public String getUnit() {
      return "dB";
    }
  };
  public static final IntRange compGainRange = new IntRange(-6, 6) {
    public String getUnit() {
      return "dB";
    }
  };

  public GmCompressorEffect(GmajorPatch parent) {
    super(parent, SINGLE_ALGO_EFFECT_TYPE.COMPRESSOR);
    this.mCompThreshold = Integer.parseInt(compThresholdRange.getMinValue());
    this.mCompRatio = GmCompressorEffect.CompRatioRange.getInstance().getMinValue();
    this.mCompAttack = CompTimeRange.getAttackInstance().getMinValue();
    this.mCompRelease = CompTimeRange.getReleaseInstance().getMinValue();
    this.mCompGain = Integer.parseInt(compGainRange.getMinValue());
  }

  public void initialize(byte[] pData) {
    this.mEffectType = SINGLE_ALGO_EFFECT_TYPE.COMPRESSOR;
    this.mCompThreshold = GmajorPatch.readInt(pData, 164, 4);
    this.mCompRatio = GmCompressorEffect.CompRatioRange.getInstance().getItem(GmajorPatch.readInt(pData, 168, 4));
    this.mCompAttack = CompTimeRange.getAttackInstance().getItem(GmajorPatch.readInt(pData, 172, 4));
    this.mCompRelease = CompTimeRange.getReleaseInstance().getItem(GmajorPatch.readInt(pData, 176, 4));
    this.mCompGain = GmajorPatch.readInt(pData, 180, 4);
    this.setEffectOnOffFlag(pData[224]);
  }

  public void writeToSysex(byte[] pData) {
    GmajorPatch.writeInt(pData, this.mCompThreshold, 164, 4);
    GmajorPatch.writeInt(pData, GmCompressorEffect.CompRatioRange.getInstance().getIndexForValue(this.mCompRatio), 168, 4);
    GmajorPatch.writeInt(pData, CompTimeRange.getAttackInstance().getIndexForValue(this.mCompAttack), 172, 4);
    GmajorPatch.writeInt(pData, CompTimeRange.getReleaseInstance().getIndexForValue(this.mCompRelease), 176, 4);
    GmajorPatch.writeInt(pData, this.mCompGain, 180, 4);
    GmajorPatch.writeInt(pData, this.mEffectOn ? 0 : 1, 224, 4);
  }

  public void copyValuesFrom(GmEffect libEffect) {
    if (libEffect.getEffectType() == this.getEffectType()) {
      GmCompressorEffect copyFromEffect = (GmCompressorEffect)libEffect;
      this.mCompThreshold = copyFromEffect.mCompThreshold;
      this.mCompRatio = copyFromEffect.mCompRatio;
      this.mCompAttack = copyFromEffect.mCompAttack;
      this.mCompRelease = copyFromEffect.mCompRelease;
      this.mCompGain = copyFromEffect.mCompGain;
    }

  }

  public void debug() {
    super.debug();
    System.out.println("mCompThreshold \t:" + this.mCompThreshold + " dB");
    System.out.println("mCompRatio     \t:" + this.mCompRatio);
    System.out.println("mCompAttack    \t:" + this.mCompAttack);
    System.out.println("mCompRelease   \t:" + this.mCompRelease);
    System.out.println("mCompGain      \t:" + this.mCompGain + " dB");
  }

  public String getCompAttack() {
    return this.mCompAttack;
  }

  public void setCompAttack(String compAttack) {
    this.mCompAttack = compAttack;
  }

  public int getCompGain() {
    return this.mCompGain;
  }

  public void setCompGain(int compGain) {
    this.mCompGain = compGain;
  }

  public void setCompGain(String compGain) {
    this.mCompGain = Integer.parseInt(compGain);
  }

  public String getCompRatio() {
    return this.mCompRatio;
  }

  public void setCompRatio(String compRatio) {
    this.mCompRatio = compRatio;
  }

  public String getCompRelease() {
    return this.mCompRelease;
  }

  public void setCompRelease(String compRelease) {
    this.mCompRelease = compRelease;
  }

  public int getCompThreshold() {
    return this.mCompThreshold;
  }

  public void setCompThreshold(int compThreshold) {
    this.mCompThreshold = compThreshold;
  }

  public void setCompThreshold(String compThreshold) {
    this.mCompThreshold = Integer.parseInt(compThreshold);
  }

  public static class CompRatioRange extends DiscreteRange {
    final String unit = " ";
    static final GmCompressorEffect.CompRatioRange me = new GmCompressorEffect.CompRatioRange();

    public String getUnit() {
      return this.unit;
    }

    public static GmCompressorEffect.CompRatioRange getInstance() {
      return me;
    }

    private CompRatioRange() {
      super(DISCRETE_RANGE_TYPE.COMP_RATIO, "Off", "Inf:1");
    }

    public String getName() {
      return "CompRatioRange";
    }
  }
}
