//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.GmajorPatch;
import com.glydian.model.IntRange;
import com.glydian.model.ModifierField;
import com.glydian.model.DiscreteRange.CompTimeRange;
import com.glydian.model.DiscreteRange.TapTempoRange;

import java.io.IOException;

public class GmDelayEffect extends GmMultiAlgorithmEffect {
  public static final IntRange delayTimeRange = new IntRange(0, 1800) {
    public String getUnit() {
      return "ms";
    }
  };
  public static final IntRange widthRange = new IntRange(0, 100) {
    public String getUnit() {
      return "%";
    }
  };
  public static final IntRange feedbackRange = new IntRange(0, 100) {
    public String getUnit() {
      return "%";
    }
  };
  public static final IntRange offsetTimeRange = new IntRange(-200, 200) {
    public String getUnit() {
      return "ms";
    }
  };
  public static final IntRange sensitivityRange = new IntRange(-50, 0) {
    public String getUnit() {
      return "dB";
    }
  };
  public static final IntRange dampRange = new IntRange(0, 100) {
    public String getUnit() {
      return "dB";
    }
  };
  public static final IntRange panRange = new IntRange(-50, 50) {
    public String getUnit() {
      return " ";
    }
  };

  public void setAlgorithmType(ALGORITHM_TYPE algorithmType) {
    this.algorithmType = algorithmType;
    this.algorithm = DELAY_ALGO_TYPE.createAlgorithmForType((DELAY_ALGO_TYPE)this.algorithmType);
  }

  public GmDelayEffect(GmajorPatch parent) {
    super(parent, MULTI_ALGO_EFFECT_TYPE.DELAY);
    this.setAlgorithmType(DELAY_ALGO_TYPE.DYNAMIC);
  }

  public void initialize(byte[] pData) {
    this.mEffectType = MULTI_ALGO_EFFECT_TYPE.DELAY;
    this.mix = GmajorPatch.readInt(pData, 472, 4);
    this.outLevel = GmajorPatch.readInt(pData, 476, 4);
    this.setEffectOnOffFlag(pData[480]);
    this.setAlgorithmType(DELAY_ALGO_TYPE.getAlgorithmType(GmajorPatch.readInt(pData, 420, 4)));
    this.algorithm.initialize(pData);
  }

  public void writeToSysex(byte[] pData) {
    this.writeDefaultModifierData(pData, 108, 144);
    GmajorPatch.writeInt(pData, this.mix, 472, 4);
    GmajorPatch.writeInt(pData, this.outLevel, 476, 4);
    GmajorPatch.writeInt(pData, this.mEffectOn ? 0 : 1, 480, 4);
    GmajorPatch.writeInt(pData, this.algorithmType.getValue(), 420, 4);
    this.algorithm.writeToSysex(pData);
  }

  public abstract static class DelayAlgorithm extends GmEffectAlgorithm {
    int delayTime;
    String tempo;
    int feedback;
    String fbHiCut;
    String fbLoCut;
    ModifierField delayModifier;
    ModifierField fbModifier;
    ModifierField fbHiCutModifier;
    ModifierField fbLoCutModifier;
    int width;
    int delayTime2;
    String tempo2;
    int feedback2;
    int pan1;
    int pan2;
    ModifierField delay2Modifier;
    ModifierField fb2Modifier;
    ModifierField pan1Modifier;
    ModifierField pan2Modifier;
    int offset;
    int sensitivity;
    int damping;
    String release;

    DelayAlgorithm() {
      this.delayTime = Integer.parseInt(GmDelayEffect.delayTimeRange.getMinValue());
      this.tempo = TapTempoRange.getInstance().getMinValue();
      this.feedback = Integer.parseInt(GmDelayEffect.feedbackRange.getMinValue());
      this.fbHiCut = HiCutFreqRange.getDelayFbHiCutInstance().getMinValue();
      this.fbLoCut = LoCutRange.getFullLoCutInstance().getMinValue();
      this.width = Integer.parseInt(GmDelayEffect.widthRange.getMinValue());
      this.delayTime2 = Integer.parseInt(GmDelayEffect.delayTimeRange.getMinValue());
      this.tempo2 = TapTempoRange.getInstance().getMinValue();
      this.feedback2 = Integer.parseInt(GmDelayEffect.feedbackRange.getMinValue());
      this.pan1 = Integer.parseInt(GmDelayEffect.panRange.getMinValue());
      this.pan2 = Integer.parseInt(GmDelayEffect.panRange.getMaxValue());
      this.offset = Integer.parseInt(GmDelayEffect.offsetTimeRange.getMinValue());
      this.sensitivity = Integer.parseInt(GmDelayEffect.sensitivityRange.getMinValue());
      this.damping = Integer.parseInt(GmDelayEffect.dampRange.getMinValue());
      this.release = CompTimeRange.getDelayReleaseInstance().getMinValue();
    }

    public void initialize(byte[] pData) {
      this.delayTime = GmajorPatch.readInt(pData, 424, 4);
      this.tempo = TapTempoRange.getInstance().getItem(GmajorPatch.readInt(pData, 432, 4));
      this.feedback = GmajorPatch.readInt(pData, 440, 4);
      this.fbHiCut = HiCutFreqRange.getDelayFbHiCutInstance().getItem(GmajorPatch.readInt(pData, 448, 4));
      this.fbLoCut = LoCutRange.getFullLoCutInstance().getItem(GmajorPatch.readInt(pData, 452, 4));
    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.delayTime, 424, 4);
      GmajorPatch.writeInt(pData, TapTempoRange.getInstance().getIndexForValue(this.tempo), 432, 4);
      GmajorPatch.writeInt(pData, this.feedback, 440, 4);
      GmajorPatch.writeInt(pData, HiCutFreqRange.getDelayFbHiCutInstance().getIndexForValue(this.fbHiCut), 448, 4);
      GmajorPatch.writeInt(pData, LoCutRange.getFullLoCutInstance().getIndexForValue(this.fbLoCut), 452, 4);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
      this.delayModifier.writeToSysEx(pData);
      this.fbModifier.writeToSysEx(pData);
      this.fbHiCutModifier.writeToSysEx(pData);
      this.fbLoCutModifier.writeToSysEx(pData);
    }

    void initializePingPong(byte[] pData) {
      this.width = GmajorPatch.readInt(pData, 436, 4);

      try {
        this.mixModifier.initialize(pData, 124);
        this.outLvlModifier.initialize(pData, 128);
        this.delayModifier.initialize(pData, 108);
        this.fbModifier.initialize(pData, 112);
        this.fbHiCutModifier.initialize(pData, 116);
        this.fbLoCutModifier.initialize(pData, 120);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    void writeToSysexPingPong(byte[] pData) {
      GmajorPatch.writeInt(pData, this.width, 436, 4);
    }

    void initializeDual(byte[] pData) {
      this.delayTime2 = GmajorPatch.readInt(pData, 428, 4);
      this.tempo2 = TapTempoRange.getInstance().getItem(GmajorPatch.readInt(pData, 436, 4));
      this.feedback2 = GmajorPatch.readInt(pData, 444, 4);
      this.pan1 = GmajorPatch.readInt(pData, 456, 4);
      this.pan2 = GmajorPatch.readInt(pData, 460, 4);

      try {
        this.mixModifier.initialize(pData, 140);
        this.outLvlModifier.initialize(pData, 144);
        this.delayModifier.initialize(pData, 108);
        this.delay2Modifier.initialize(pData, 112);
        this.fbModifier.initialize(pData, 116);
        this.fb2Modifier.initialize(pData, 120);
        this.fbHiCutModifier.initialize(pData, 124);
        this.fbLoCutModifier.initialize(pData, 128);
        this.pan1Modifier.initialize(pData, 132);
        this.pan2Modifier.initialize(pData, 136);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    void writeToSysexDual(byte[] pData) {
      GmajorPatch.writeInt(pData, this.delayTime2, 428, 4);
      GmajorPatch.writeInt(pData, TapTempoRange.getInstance().getIndexForValue(this.tempo2), 436, 4);
      GmajorPatch.writeInt(pData, this.feedback2, 444, 4);
      GmajorPatch.writeInt(pData, this.pan1, 456, 4);
      GmajorPatch.writeInt(pData, this.pan2, 460, 4);
      this.delay2Modifier.writeToSysEx(pData);
      this.fb2Modifier.writeToSysEx(pData);
      this.pan1Modifier.writeToSysEx(pData);
      this.pan2Modifier.writeToSysEx(pData);
    }

    void initializeDynamic(byte[] pData) {
      this.offset = GmajorPatch.readInt(pData, 456, 4);
      this.sensitivity = GmajorPatch.readInt(pData, 460, 4);
      this.damping = GmajorPatch.readInt(pData, 464, 4);
      this.release = CompTimeRange.getDelayReleaseInstance().getItem(GmajorPatch.readInt(pData, 468, 4));

      try {
        this.mixModifier.initialize(pData, 124);
        this.outLvlModifier.initialize(pData, 128);
        this.delayModifier.initialize(pData, 108);
        this.fbModifier.initialize(pData, 112);
        this.fbHiCutModifier.initialize(pData, 116);
        this.fbLoCutModifier.initialize(pData, 120);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    void writeToSysexDynamic(byte[] pData) {
      GmajorPatch.writeInt(pData, this.offset, 456, 4);
      GmajorPatch.writeInt(pData, this.sensitivity, 460, 4);
      GmajorPatch.writeInt(pData, this.damping, 464, 4);
      GmajorPatch.writeInt(pData, CompTimeRange.getDelayReleaseInstance().getIndexForValue(this.release), 468, 4);
    }

    public int getDelayTime() {
      return this.delayTime;
    }

    public void setDelayTime(String x) {
      this.delayTime = Integer.parseInt(x);
    }

    public String getTempo() {
      return this.tempo;
    }

    public void setTempo(String x) {
      this.tempo = x;
    }

    public int getFeedback() {
      return this.feedback;
    }

    public void setFeedback(String x) {
      this.feedback = Integer.parseInt(x);
    }

    public String getFbHiCut() {
      return this.fbHiCut;
    }

    public void setFbHiCut(String x) {
      this.fbHiCut = x;
    }

    public String getFbLoCut() {
      return this.fbLoCut;
    }

    public void setFbLoCut(String x) {
      this.fbLoCut = x;
    }

    public int getDelayTime2() {
      return this.delayTime2;
    }

    public void setDelayTime2(String delayTime2) {
      this.delayTime2 = Integer.parseInt(delayTime2);
    }

    public int getFeedback2() {
      return this.feedback2;
    }

    public void setFeedback2(String feedback2) {
      this.feedback2 = Integer.parseInt(feedback2);
    }

    public String getTempo2() {
      return this.tempo2;
    }

    public void setTempo2(String tempo2) {
      this.tempo2 = tempo2;
    }

    public int getPan1() {
      return this.pan1;
    }

    public void setPan1(String pan) {
      this.pan1 = Integer.parseInt(pan);
    }

    public int getPan2() {
      return this.pan2;
    }

    public void setPan2(String pan) {
      this.pan2 = Integer.parseInt(pan);
    }

    public int getOffset() {
      return this.offset;
    }

    public void setOffset(String offset) {
      this.offset = Integer.parseInt(offset);
    }

    public String getRelease() {
      return this.release;
    }

    public void setRelease(String release) {
      this.release = release;
    }

    public int getSensitivity() {
      return this.sensitivity;
    }

    public void setSensitivity(String sensitivity) {
      this.sensitivity = Integer.parseInt(sensitivity);
    }

    public int getDamping() {
      return this.damping;
    }

    public void setDamping(String x) {
      this.damping = Integer.parseInt(x);
    }

    public int getWidth() {
      return this.width;
    }

    public void setWidth(String width) {
      this.width = Integer.parseInt(width);
    }

    public void debug() {
      System.out.println("delayTime\t:" + this.delayTime + " " + GmDelayEffect.delayTimeRange.getUnit());
      System.out.println("tempo\t\t:" + this.tempo + " " + TapTempoRange.getInstance().getUnit());
      System.out.println("feedback\t:" + this.feedback + " " + GmDelayEffect.feedbackRange.getUnit());
      System.out.println("fbHiCut\t\t:" + this.fbHiCut + " " + HiCutFreqRange.getDelayFbHiCutInstance().getUnit());
      System.out.println("fbLoCut\t\t:" + this.fbLoCut + " " + LoCutRange.getFullLoCutInstance().getUnit());
    }

    void debugPingPong() {
      System.out.println("width\t\t:" + this.width + " " + GmDelayEffect.widthRange.getUnit());
    }

    void debugDual() {
      System.out.println("delayTime2\t:" + this.delayTime2 + " " + GmDelayEffect.delayTimeRange.getUnit());
      System.out.println("tempo2\t\t:" + this.tempo2 + " " + TapTempoRange.getInstance().getUnit());
      System.out.println("feedback2\t:" + this.feedback2 + " " + GmDelayEffect.feedbackRange.getUnit());
    }

    void debugDynamic() {
      System.out.println("offset\t\t:" + this.offset + " " + GmDelayEffect.offsetTimeRange.getUnit());
      System.out.println("sensitivity\t:" + this.sensitivity + " " + GmDelayEffect.sensitivityRange.getUnit());
      System.out.println("damping\t\t:" + this.damping + " " + GmDelayEffect.dampRange.getUnit());
      System.out.println("release\t\t:" + this.release + " " + CompTimeRange.getDelayReleaseInstance().getUnit());
    }

    public ModifierField getDelay2Modifier() {
      return this.delay2Modifier;
    }

    public ModifierField getDelayModifier() {
      return this.delayModifier;
    }

    public ModifierField getFb2Modifier() {
      return this.fb2Modifier;
    }

    public ModifierField getFbHiCutModifier() {
      return this.fbHiCutModifier;
    }

    public ModifierField getFbLoCutModifier() {
      return this.fbLoCutModifier;
    }

    public ModifierField getFbModifier() {
      return this.fbModifier;
    }

    public ModifierField getPan1Modifier() {
      return this.pan1Modifier;
    }

    public ModifierField getPan2Modifier() {
      return this.pan2Modifier;
    }
  }

  public static class DualDelayAlgorithm extends GmDelayEffect.DelayAlgorithm {
    public DualDelayAlgorithm() {
      this.type = DELAY_ALGO_TYPE.DUAL;
      this.mixModifier = new ModifierField(140);
      this.outLvlModifier = new ModifierField(144);
      this.delayModifier = new ModifierField(108);
      this.fbModifier = new ModifierField(116);
      this.fbHiCutModifier = new ModifierField(124);
      this.fbLoCutModifier = new ModifierField(128);
      this.delay2Modifier = new ModifierField(112);
      this.fb2Modifier = new ModifierField(120);
      this.pan1Modifier = new ModifierField(132);
      this.pan2Modifier = new ModifierField(136);
    }

    public void initialize(byte[] pData) {
      super.initialize(pData);
      super.initializeDual(pData);
    }

    public void writeToSysex(byte[] pData) {
      super.writeToSysex(pData);
      super.writeToSysexDual(pData);
    }

    public void debug() {
      super.debug();
      super.debugDual();
    }
  }

  public static class DynamicDelayAlgorithm extends GmDelayEffect.DelayAlgorithm {
    public DynamicDelayAlgorithm() {
      this.type = DELAY_ALGO_TYPE.DYNAMIC;
      this.mixModifier = new ModifierField(124);
      this.outLvlModifier = new ModifierField(128);
      this.delayModifier = new ModifierField(108);
      this.fbModifier = new ModifierField(112);
      this.fbHiCutModifier = new ModifierField(116);
      this.fbLoCutModifier = new ModifierField(120);
    }

    public void initialize(byte[] pData) {
      super.initialize(pData);
      super.initializeDynamic(pData);
    }

    public void writeToSysex(byte[] pData) {
      super.writeToSysex(pData);
      super.writeToSysexDynamic(pData);
    }

    public void debug() {
      super.debug();
      super.debugDynamic();
    }
  }

  public static class PingPongDelayAlgorithm extends GmDelayEffect.DelayAlgorithm {
    public PingPongDelayAlgorithm() {
      this.type = DELAY_ALGO_TYPE.PING_PONG;
      this.mixModifier = new ModifierField(124);
      this.outLvlModifier = new ModifierField(128);
      this.delayModifier = new ModifierField(108);
      this.fbModifier = new ModifierField(112);
      this.fbHiCutModifier = new ModifierField(116);
      this.fbLoCutModifier = new ModifierField(120);
    }

    public void initialize(byte[] pData) {
      super.initialize(pData);
      super.initializePingPong(pData);
    }

    public void writeToSysex(byte[] pData) {
      super.writeToSysex(pData);
      super.writeToSysexPingPong(pData);
    }

    public void debug() {
      super.debug();
      super.debugPingPong();
    }
  }
}
