//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.DiscreteRange;
import com.glydian.model.GmajorPatch;
import com.glydian.model.IntRange;

public class GmEqualizer extends GmEffect {
  public static final IntRange eqGainRange = new IntRange(-12, 12) {
    public String getUnit() {
      return "dB";
    }
  };
  private String eq1Freq = GmEqualizer.EqFrequencyRange.getInstance().getMinValue();
  private int eq1Gain;
  private String eq1Width;
  private String eq2Freq;
  private int eq2Gain;
  private String eq2Width;
  private String eq3Freq;
  private int eq3Gain;
  private String eq3Width;

  public GmEqualizer(GmajorPatch parent) {
    super(parent, SINGLE_ALGO_EFFECT_TYPE.EQ);
    this.eq1Gain = Integer.parseInt(eqGainRange.getMinValue());
    this.eq1Width = GmEqualizer.EqWidthRange.getInstance().getMinValue();
    this.eq2Freq = GmEqualizer.EqFrequencyRange.getInstance().getMinValue();
    this.eq2Gain = Integer.parseInt(eqGainRange.getMinValue());
    this.eq2Width = GmEqualizer.EqWidthRange.getInstance().getMinValue();
    this.eq3Freq = GmEqualizer.EqFrequencyRange.getInstance().getMinValue();
    this.eq3Gain = Integer.parseInt(eqGainRange.getMinValue());
    this.eq3Width = GmEqualizer.EqWidthRange.getInstance().getMinValue();
  }

  public void copyValuesFrom(GmEffect libEffect) {
    if (libEffect instanceof GmEqualizer) {
      GmEqualizer copyFromEffect = (GmEqualizer)libEffect;
      this.eq1Freq = copyFromEffect.eq1Freq;
      this.eq1Gain = copyFromEffect.eq1Gain;
      this.eq1Width = copyFromEffect.eq1Width;
      this.eq2Freq = copyFromEffect.eq2Freq;
      this.eq2Gain = copyFromEffect.eq2Gain;
      this.eq2Width = copyFromEffect.eq2Width;
      this.eq3Freq = copyFromEffect.eq3Freq;
      this.eq3Gain = copyFromEffect.eq3Gain;
      this.eq3Width = copyFromEffect.eq3Width;
    }

  }

  public void writeToSysex(byte[] pData) {
    GmajorPatch.writeInt(pData, GmEqualizer.EqFrequencyRange.getInstance().getIndexForValue(this.eq1Freq), 568, 4);
    GmajorPatch.writeInt(pData, this.eq1Gain, 572, 4);
    GmajorPatch.writeInt(pData, GmEqualizer.EqWidthRange.getInstance().getIndexForValue(this.eq1Width), 576, 4);
    GmajorPatch.writeInt(pData, GmEqualizer.EqFrequencyRange.getInstance().getIndexForValue(this.eq2Freq), 580, 4);
    GmajorPatch.writeInt(pData, this.eq2Gain, 584, 4);
    GmajorPatch.writeInt(pData, GmEqualizer.EqWidthRange.getInstance().getIndexForValue(this.eq2Width), 588, 4);
    GmajorPatch.writeInt(pData, GmEqualizer.EqFrequencyRange.getInstance().getIndexForValue(this.eq3Freq), 592, 4);
    GmajorPatch.writeInt(pData, this.eq3Gain, 596, 4);
    GmajorPatch.writeInt(pData, GmEqualizer.EqWidthRange.getInstance().getIndexForValue(this.eq3Width), 600, 4);
    GmajorPatch.writeInt(pData, this.mEffectOn ? 1 : 0, 564, 4);
  }

  public void initialize(byte[] pSysExData) {
    this.eq1Freq = GmEqualizer.EqFrequencyRange.getInstance().getItem(GmajorPatch.readInt(pSysExData, 568, 4));
    this.eq1Gain = GmajorPatch.readInt(pSysExData, 572, 4);
    this.eq1Width = GmEqualizer.EqWidthRange.getInstance().getItem(GmajorPatch.readInt(pSysExData, 576, 4));
    this.eq2Freq = GmEqualizer.EqFrequencyRange.getInstance().getItem(GmajorPatch.readInt(pSysExData, 580, 4));
    this.eq2Gain = GmajorPatch.readInt(pSysExData, 584, 4);
    this.eq2Width = GmEqualizer.EqWidthRange.getInstance().getItem(GmajorPatch.readInt(pSysExData, 588, 4));
    this.eq3Freq = GmEqualizer.EqFrequencyRange.getInstance().getItem(GmajorPatch.readInt(pSysExData, 592, 4));
    this.eq3Gain = GmajorPatch.readInt(pSysExData, 596, 4);
    this.eq3Width = GmEqualizer.EqWidthRange.getInstance().getItem(GmajorPatch.readInt(pSysExData, 600, 4));
    this.mEffectOn = GmajorPatch.readInt(pSysExData, 564, 4) == 1;

  }

  public void debug() {
    super.debug();
    System.out.println("eq1Freq  \t:" + this.eq1Freq + " \teq1Gain  \t:" + this.eq1Gain + " \teq1Width \t:" + this.eq1Width);
    System.out.println("eq2Freq  \t:" + this.eq2Freq + " \teq2Gain  \t:" + this.eq2Gain + " \teq2Width \t:" + this.eq2Width);
    System.out.println("eq3Freq  \t:" + this.eq3Freq + " \teq3Gain  \t:" + this.eq3Gain + " \teq3Width \t:" + this.eq3Width);
  }

  public String getEq1Freq() {
    return this.eq1Freq;
  }

  public void setEq1Freq(String eq1Freq) {
    this.eq1Freq = eq1Freq;
  }

  public int getEq1Gain() {
    return this.eq1Gain;
  }

  public void setEq1Gain(int eq1Gain) {
    this.eq1Gain = eq1Gain;
  }

  public void setEq1Gain(String eq1Gain) {
    this.eq1Gain = Integer.parseInt(eq1Gain);
  }

  public String getEq1Width() {
    return this.eq1Width;
  }

  public void setEq1Width(String eq1Width) {
    this.eq1Width = eq1Width;
  }

  public String getEq2Freq() {
    return this.eq2Freq;
  }

  public void setEq2Freq(String eq2Freq) {
    this.eq2Freq = eq2Freq;
  }

  public int getEq2Gain() {
    return this.eq2Gain;
  }

  public void setEq2Gain(int eq2Gain) {
    this.eq2Gain = eq2Gain;
  }

  public void setEq2Gain(String eq2Gain) {
    this.eq2Gain = Integer.parseInt(eq2Gain);
  }

  public String getEq2Width() {
    return this.eq2Width;
  }

  public void setEq2Width(String eq2Width) {
    this.eq2Width = eq2Width;
  }

  public String getEq3Freq() {
    return this.eq3Freq;
  }

  public void setEq3Freq(String eq3Freq) {
    this.eq3Freq = eq3Freq;
  }

  public int getEq3Gain() {
    return this.eq3Gain;
  }

  public void setEq3Gain(int eq3Gain) {
    this.eq3Gain = eq3Gain;
  }

  public void setEq3Gain(String eq3Gain) {
    this.eq3Gain = Integer.parseInt(eq3Gain);
  }

  public String getEq3Width() {
    return this.eq3Width;
  }

  public void setEq3Width(String eq3Width) {
    this.eq3Width = eq3Width;
  }

  public static class EqFrequencyRange extends DiscreteRange {
    final String unit = "Hz";
    static final GmEqualizer.EqFrequencyRange me = new GmEqualizer.EqFrequencyRange();

    public String getUnit() {
      return this.unit;
    }

    EqFrequencyRange() {
      super(DISCRETE_RANGE_TYPE.EQ_FREQ, "40.97", "Off  ");
    }

    public static GmEqualizer.EqFrequencyRange getInstance() {
      return me;
    }

    public String getName() {
      return "EqFrequencyRange";
    }
  }

  public static class EqWidthRange extends DiscreteRange {
    final String unit = "oct";
    static final GmEqualizer.EqWidthRange me = new GmEqualizer.EqWidthRange();

    public String getUnit() {
      return this.unit;
    }

    EqWidthRange() {
      super(DISCRETE_RANGE_TYPE.EQ_WIDTH, "0.2 ", "4.0 ");
    }

    public static GmEqualizer.EqWidthRange getInstance() {
      return me;
    }

    public String getName() {
      return "EqWidthRange";
    }
  }
}
