//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.GmajorPatch;
import com.glydian.model.ModifierField;
import com.glydian.model.DiscreteRange.TapTempoRange;

import java.io.IOException;

public class GmModulationEffect extends GmMultiAlgorithmEffect {
  public void setAlgorithmType(ALGORITHM_TYPE algorithmType) {
    this.algorithmType = algorithmType;
    this.algorithm = MODULATION_ALGO_TYPE.createAlgorithmForType((MODULATION_ALGO_TYPE)this.algorithmType);
  }

  public GmModulationEffect(GmajorPatch parent) {
    super(parent, MULTI_ALGO_EFFECT_TYPE.CHOFLA);
    this.setAlgorithmType(MODULATION_ALGO_TYPE.ADV_FLA);
  }

  public void initialize(byte[] pData) {
    this.mEffectType = MULTI_ALGO_EFFECT_TYPE.CHOFLA;
    this.mix = GmajorPatch.readInt(pData, 396, 4);
    this.outLevel = GmajorPatch.readInt(pData, 400, 4);
    this.setEffectOnOffFlag(pData[416]);
    this.setAlgorithmType(MODULATION_ALGO_TYPE.getAlgorithmType(GmajorPatch.readInt(pData, 356, 4)));
    this.algorithm.initialize(pData);
  }

  public void writeToSysex(byte[] pData) {
    this.writeDefaultModifierData(pData, 80, 104);
    GmajorPatch.writeInt(pData, this.mix, 396, 4);
    GmajorPatch.writeInt(pData, this.outLevel, 400, 4);
    GmajorPatch.writeInt(pData, this.mEffectOn ? 0 : 1, 416, 4);
    GmajorPatch.writeInt(pData, this.algorithmType.getValue(), 356, 4);
    this.algorithm.writeToSysex(pData);
  }

  public boolean isMixModAssignable() {
    return this.algorithm.getType() != MODULATION_ALGO_TYPE.VIB;
  }

  public static class GmAdvanceChorusAlgorithm extends GmModulationEffect.ModulationAlgorithm {
    public GmAdvanceChorusAlgorithm() {
      this.type = MODULATION_ALGO_TYPE.ADV_CHO;
      this.mixModifier = new ModifierField(92);
      this.outLvlModifier = new ModifierField(96);
    }

    public void initialize(byte[] pData) {
      super.initialize(pData);
      super.initializeAdvance(pData);

      try {
        this.mixModifier.initialize(pData, 92);
        this.outLvlModifier.initialize(pData, 96);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      super.writeToSysex(pData);
      super.writeToSysexAdvance(pData);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
    }

    public int getDelay() {
      return this.mDelay;
    }

    public void setDelay(String x) {
      this.mDelay = Integer.parseInt(x);
    }

    public ONOFF_VALUE getGoldenRatio() {
      return this.mGoldRatio;
    }

    public void setGoldenRatio(ONOFF_VALUE x) {
      this.mGoldRatio = x;
    }

    public ONOFF_VALUE getPhaseReverse() {
      return this.mPhaseReverse;
    }

    public void setPhaseReverse(ONOFF_VALUE x) {
      this.mPhaseReverse = x;
    }

    public void debug() {
      super.debug();
      super.debugAdvance();
    }
  }

  public static class GmAdvanceFlangAlgorithm extends GmModulationEffect.ModulationAlgorithm {
    public GmAdvanceFlangAlgorithm() {
      this.type = MODULATION_ALGO_TYPE.ADV_FLA;
      this.mixModifier = new ModifierField(100);
      this.outLvlModifier = new ModifierField(104);
    }

    public void initialize(byte[] pData) {
      super.initialize(pData);
      super.initializeAdvance(pData);
      super.initializeFlang(pData);
    }

    public void writeToSysex(byte[] pData) {
      super.writeToSysex(pData);
      super.writeToSysexFlang(pData);
      super.writeToSysexAdvance(pData);
    }

    public int getFeedback() {
      return this.mFeedBack;
    }

    public void setFeedback(String x) {
      this.mFeedBack = Integer.parseInt(x);
    }

    public String getFeedbackHiCut() {
      return this.mFeedBackHiCut;
    }

    public void setFeedbackHiCut(String x) {
      this.mFeedBackHiCut = x;
    }

    public int getDelay() {
      return this.mDelay;
    }

    public void setDelay(String x) {
      this.mDelay = Integer.parseInt(x);
    }

    public ONOFF_VALUE getGoldenRatio() {
      return this.mGoldRatio;
    }

    public void setGoldenRatio(ONOFF_VALUE x) {
      this.mGoldRatio = x;
    }

    public ONOFF_VALUE getPhaseReverse() {
      return this.mPhaseReverse;
    }

    public void setPhaseReverse(ONOFF_VALUE x) {
      this.mPhaseReverse = x;
    }

    public void debug() {
      super.debug();
      super.debugAdvance();
      super.debugFlang();
    }
  }

  public static class GmClassicChorusAlgorithm extends GmModulationEffect.ModulationAlgorithm {
    public GmClassicChorusAlgorithm() {
      this.type = MODULATION_ALGO_TYPE.CLA_CHO;
      this.mixModifier = new ModifierField(92);
      this.outLvlModifier = new ModifierField(96);
    }

    public void initialize(byte[] pData) {
      super.initialize(pData);

      try {
        this.mixModifier.initialize(pData, 92);
        this.outLvlModifier.initialize(pData, 96);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      super.writeToSysex(pData);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
    }

  }

  public static class GmClassicFlangAlgorithm extends GmModulationEffect.ModulationAlgorithm {
    public GmClassicFlangAlgorithm() {
      this.type = MODULATION_ALGO_TYPE.CLA_FLA;
      this.mixModifier = new ModifierField(100);
      this.outLvlModifier = new ModifierField(104);
    }

    public void initialize(byte[] pData) {
      super.initialize(pData);
      super.initializeFlang(pData);
    }

    public void writeToSysex(byte[] pData) {
      super.writeToSysex(pData);
      super.writeToSysexFlang(pData);
    }

    public int getFeedback() {
      return this.mFeedBack;
    }

    public void setFeedback(String x) {
      this.mFeedBack = Integer.parseInt(x);
    }

    public String getFeedbackHiCut() {
      return this.mFeedBackHiCut;
    }

    public void setFeedbackHiCut(String x) {
      this.mFeedBackHiCut = x;
    }

    public void debug() {
      super.debug();
      super.debugFlang();
    }
  }

  public static class GmVibratoAlgorithm extends GmModulationEffect.ModulationAlgorithm {
    public GmVibratoAlgorithm() {
      this.type = MODULATION_ALGO_TYPE.VIB;
      this.outLvlModifier = new ModifierField(92);
    }

    public void initialize(byte[] pData) {
      super.initialize(pData);

      try {
        this.outLvlModifier.initialize(pData, 92);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      super.writeToSysex(pData);
      this.outLvlModifier.writeToSysEx(pData);
    }

  }

  public abstract static class ModulationAlgorithm extends GmEffectAlgorithm {
    String mSpeed = SpeedRange.getSpeedInstance().getMinValue();
    int mDepth;
    String mTempo;
    String mHiCut;
    int mDelay;
    ONOFF_VALUE mGoldRatio;
    ONOFF_VALUE mPhaseReverse;
    int mFeedBack;
    String mFeedBackHiCut;
    final ModifierField speedModifier;
    final ModifierField depthModifier;
    final ModifierField hiCutModifier;
    final ModifierField fbModifier;
    final ModifierField fbCutModifier;

    ModulationAlgorithm() {
      this.mDepth = Integer.parseInt(GmModulationEffect.depthRange.getMinValue());
      this.mTempo = TapTempoRange.getInstance().getMinValue();
      this.mHiCut = HiCutFreqRange.getFullHiCutInstance().getMinValue();
      this.mDelay = Integer.parseInt(GmModulationEffect.shortDelayRange.getMinValue());
      this.mGoldRatio = ONOFF_VALUE.OFF;
      this.mPhaseReverse = ONOFF_VALUE.OFF;
      this.mFeedBack = Integer.parseInt(GmModulationEffect.feedbackRange.getMinValue());
      this.mFeedBackHiCut = HiCutFreqRange.getFullHiCutInstance().getMinValue();
      this.speedModifier = new ModifierField(80);
      this.depthModifier = new ModifierField(84);
      this.hiCutModifier = new ModifierField(88);
      this.fbModifier = new ModifierField(92);
      this.fbCutModifier = new ModifierField(96);
    }

    public void initialize(byte[] pData) {
      this.mSpeed = SpeedRange.getSpeedInstance().getItem(GmajorPatch.readInt(pData, 360, 4));
      this.mDepth = GmajorPatch.readInt(pData, 364, 4);
      this.mTempo = TapTempoRange.getInstance().getItem(GmajorPatch.readInt(pData, 368, 4));
      this.mHiCut = HiCutFreqRange.getFullHiCutInstance().getItem(GmajorPatch.readInt(pData, 372, 4));

      try {
        this.speedModifier.initialize(pData, 80);
        this.depthModifier.initialize(pData, 84);
        this.hiCutModifier.initialize(pData, 88);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    void initializeFlang(byte[] pData) {
      this.mFeedBack = GmajorPatch.readInt(pData, 376, 4);
      this.mFeedBackHiCut = HiCutFreqRange.getFullHiCutInstance().getItem(GmajorPatch.readInt(pData, 380, 4));

      try {
        this.fbModifier.initialize(pData, 92);
        this.fbCutModifier.initialize(pData, 96);
        this.mixModifier.initialize(pData, 100);
        this.outLvlModifier.initialize(pData, 104);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    void initializeAdvance(byte[] pData) {
      this.mDelay = GmajorPatch.readInt(pData, 384, 4);
      this.mGoldRatio = ONOFF_VALUE.getOnOff(GmajorPatch.readInt(pData, 388, 4));
      this.mPhaseReverse = ONOFF_VALUE.getOnOff(GmajorPatch.readInt(pData, 392, 4));
    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, SpeedRange.getSpeedInstance().getIndexForValue(this.mSpeed), 360, 4);
      GmajorPatch.writeInt(pData, this.mDepth, 364, 4);
      GmajorPatch.writeInt(pData, TapTempoRange.getInstance().getIndexForValue(this.mTempo), 368, 4);
      GmajorPatch.writeInt(pData, HiCutFreqRange.getFullHiCutInstance().getIndexForValue(this.mHiCut), 372, 4);
      this.speedModifier.writeToSysEx(pData);
      this.depthModifier.writeToSysEx(pData);
      this.hiCutModifier.writeToSysEx(pData);
    }

    void writeToSysexFlang(byte[] pData) {
      GmajorPatch.writeInt(pData, this.mFeedBack, 376, 4);
      GmajorPatch.writeInt(pData, HiCutFreqRange.getFullHiCutInstance().getIndexForValue(this.mFeedBackHiCut), 380, 4);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
      this.fbModifier.writeToSysEx(pData);
      this.fbCutModifier.writeToSysEx(pData);
    }

    void writeToSysexAdvance(byte[] pData) {
      GmajorPatch.writeInt(pData, this.mDelay, 384, 4);
      GmajorPatch.writeInt(pData, this.mGoldRatio.value, 388, 4);
      GmajorPatch.writeInt(pData, this.mPhaseReverse.value, 392, 4);
    }

    public String getSpeed() {
      return this.mSpeed;
    }

    public void setSpeed(String x) {
      this.mSpeed = x;
    }

    public int getDepth() {
      return this.mDepth;
    }

    public void setDepth(String x) {
      this.mDepth = Integer.parseInt(x);
    }

    public String getTempo() {
      return this.mTempo;
    }

    public void setTempo(String x) {
      this.mTempo = x;
    }

    public String getHiCut() {
      return this.mHiCut;
    }

    public void setHiCut(String x) {
      this.mHiCut = x;
    }

    public int getDelay() {
      return this.mDelay;
    }

    public void setDelay(String x) {
      this.mDelay = Integer.parseInt(x);
    }

    public ONOFF_VALUE getGoldenRatio() {
      return this.mGoldRatio;
    }

    public void setGoldenRatio(ONOFF_VALUE x) {
      this.mGoldRatio = x;
    }

    public ONOFF_VALUE getPhaseReverse() {
      return this.mPhaseReverse;
    }

    public void setPhaseReverse(ONOFF_VALUE x) {
      this.mPhaseReverse = x;
    }

    public int getFeedback() {
      return this.mFeedBack;
    }

    public void setFeedback(String x) {
      this.mFeedBack = Integer.parseInt(x);
    }

    public String getFeedbackHiCut() {
      return this.mFeedBackHiCut;
    }

    public void setFeedbackHiCut(String x) {
      this.mFeedBackHiCut = x;
    }

    public ModifierField getDepthModifier() {
      return this.depthModifier;
    }

    public ModifierField getFbCutModifier() {
      return this.fbCutModifier;
    }

    public ModifierField getFbModifier() {
      return this.fbModifier;
    }

    public ModifierField getHiCutModifier() {
      return this.hiCutModifier;
    }

    public ModifierField getSpeedModifier() {
      return this.speedModifier;
    }

    void debugAdvance() {
      System.out.println("Delay\t\t:" + this.mDelay);
      System.out.println("GoldRatio\t:" + this.mGoldRatio);
      System.out.println("PhaseReverse\t:" + this.mPhaseReverse);
    }

    void debugFlang() {
      System.out.println("FeedBack\t\t:" + this.mFeedBack);
      System.out.println("FeedBackHiCut\t:" + this.mFeedBackHiCut + " " + HiCutFreqRange.getFullHiCutInstance().getUnit());
    }

    public void debug() {
      System.out.println("Speed\t\t:" + this.mSpeed + " " + SpeedRange.getSpeedInstance().getUnit());
      System.out.println("Depth\t\t:" + this.mDepth + " " + GmModulationEffect.depthRange.getUnit());
      System.out.println("Tempo\t\t:" + this.mTempo + " " + TapTempoRange.getInstance().getUnit());
      System.out.println("HiCut\t\t:" + this.mHiCut + " " + HiCutFreqRange.getFullHiCutInstance().getUnit());
    }
  }
}
