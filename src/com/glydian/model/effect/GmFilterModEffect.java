//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.GmajorPatch;
import com.glydian.model.IntRange;
import com.glydian.model.ModifierField;
import com.glydian.model.DiscreteRange.TapTempoRange;

import java.io.IOException;

public class GmFilterModEffect extends GmMultiAlgorithmEffect {
  public GmFilterModEffect(GmajorPatch parent) {
    super(parent, MULTI_ALGO_EFFECT_TYPE.FILTER);
    this.setAlgorithmType(FILTER_ALGO_TYPE.AUTO_FILTER);
  }

  public void initialize(byte[] pData) {
    this.mEffectType = MULTI_ALGO_EFFECT_TYPE.FILTER;
    this.mix = GmajorPatch.readInt(pData, 280, 4);
    this.outLevel = GmajorPatch.readInt(pData, 284, 4);
    this.setEffectOnOffFlag(pData[288]);
    this.setAlgorithmType(FILTER_ALGO_TYPE.getAlgorithmType(GmajorPatch.readInt(pData, 228, 4)));
    this.algorithm.initialize(pData);
  }

  public void writeToSysex(byte[] pData) {
    this.writeDefaultModifierData(pData, 28, 44);
    GmajorPatch.writeInt(pData, this.mix, 280, 4);
    GmajorPatch.writeInt(pData, this.outLevel, 284, 4);
    GmajorPatch.writeInt(pData, this.mEffectOn ? 0 : 1, 288, 4);
    GmajorPatch.writeInt(pData, this.algorithmType.getValue(), 228, 4);
    this.algorithm.writeToSysex(pData);
  }

  public void setAlgorithmType(ALGORITHM_TYPE algorithmType) {
    this.algorithmType = algorithmType;
    this.algorithm = FILTER_ALGO_TYPE.createAlgorithmForType((FILTER_ALGO_TYPE)this.algorithmType);
  }

  public boolean isMixModAssignable() {
    return this.algorithm.getType() != FILTER_ALGO_TYPE.TREMOLO && this.algorithm.getType() != FILTER_ALGO_TYPE.PANNER;
  }

  public static class GmFilterAlgorithm extends GmEffectAlgorithm {
    public static final IntRange autoResSensitivityRange = new IntRange(0, 10) {
      public String getUnit() {
        return " ";
      }
    };
    public static IntRange autoResFreqMaxRange = new IntRange(1, 10) {
      public String getUnit() {
        return "kHz";
      }
    };
    public static final IntRange resHiResonanceRange = new IntRange(0, 100) {
      public String getUnit() {
        return "%";
      }
    };
    GmFilterModEffect.GmFilterAlgorithm.FILTER_ORDER mFilterOrder;
    int mAutoResSensitivity;
    FILTER_RESPONSE mAutoResResponse;
    String mAutoResFreqMax;
    String mResHiCut;
    int mResHiResonance;
    ModifierField mHiCutModifier;
    ModifierField mResModifier;

    GmFilterAlgorithm(FILTER_ALGO_TYPE filType) {
      this.mFilterOrder = GmFilterModEffect.GmFilterAlgorithm.FILTER_ORDER.SECOND;
      this.mAutoResSensitivity = Integer.parseInt(autoResSensitivityRange.getMinValue());
      this.mAutoResResponse = FILTER_RESPONSE.SLOW;
      this.mAutoResFreqMax = HiCutFreqRange.getAutoResonanceMaxFreqInstance().getMinValue();
      this.mResHiCut = HiCutFreqRange.getResonanceHiCutInstance().getMinValue();
      this.mResHiResonance = Integer.parseInt(resHiResonanceRange.getMinValue());
      this.type = filType;
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        this.mixModifier = new ModifierField(28);
        this.outLvlModifier = new ModifierField(32);
      } else if (this.type == FILTER_ALGO_TYPE.RESONANCE_FILTER) {
        this.mHiCutModifier = new ModifierField(28);
        this.mResModifier = new ModifierField(32);
        this.mixModifier = new ModifierField(36);
        this.outLvlModifier = new ModifierField(40);
      }

    }

    public void initialize(byte[] pData) {
      this.mFilterOrder = GmFilterModEffect.GmFilterAlgorithm.FILTER_ORDER.getOrder(pData[232]);

      try {
        if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
          this.mixModifier.initialize(pData, 28);
          this.outLvlModifier.initialize(pData, 32);
          this.mAutoResSensitivity = GmajorPatch.readInt(pData, 236, 4);
          this.mAutoResResponse = FILTER_RESPONSE.getResponse(GmajorPatch.readInt(pData, 240, 4));
          this.mAutoResFreqMax = HiCutFreqRange.getAutoResonanceMaxFreqInstance().getItem(GmajorPatch.readInt(pData, 252, 4));
        } else {
          if (this.type != FILTER_ALGO_TYPE.RESONANCE_FILTER) {
            System.out.println("Fatal ERROR!!!");
            throw new RuntimeException("Fatal ERROR!!!");
          }

          this.mHiCutModifier.initialize(pData, 28);
          this.mResModifier.initialize(pData, 32);
          this.mixModifier.initialize(pData, 36);
          this.outLvlModifier.initialize(pData, 40);
          this.mResHiCut = HiCutFreqRange.getResonanceHiCutInstance().getItem(GmajorPatch.readInt(pData, 272, 4));
          this.mResHiResonance = GmajorPatch.readInt(pData, 276, 4);
        }
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.mFilterOrder.value, 232, 4);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        GmajorPatch.writeInt(pData, this.mAutoResSensitivity, 236, 4);
        GmajorPatch.writeInt(pData, this.mAutoResResponse.value, 240, 4);
        GmajorPatch.writeInt(pData, HiCutFreqRange.getAutoResonanceMaxFreqInstance().getIndexForValue(this.mAutoResFreqMax), 252, 4);
      } else {
        if (this.type != FILTER_ALGO_TYPE.RESONANCE_FILTER) {
          System.out.println("Fatal ERROR!!!");
          throw new RuntimeException("Fatal ERROR!!!");
        }

        this.mHiCutModifier.writeToSysEx(pData);
        this.mResModifier.writeToSysEx(pData);
        GmajorPatch.writeInt(pData, HiCutFreqRange.getResonanceHiCutInstance().getIndexForValue(this.mResHiCut), 272, 4);
        GmajorPatch.writeInt(pData, this.mResHiResonance, 276, 4);
      }

    }

    public GmFilterModEffect.GmFilterAlgorithm.FILTER_ORDER getFilterOrder() {
      return this.mFilterOrder;
    }

    public void setFilterOrder(GmFilterModEffect.GmFilterAlgorithm.FILTER_ORDER filterOrder) {
      this.mFilterOrder = filterOrder;
    }

    public String getAutoResFreqMax() {
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        return this.mAutoResFreqMax;
      } else {
        throw new RuntimeException("Error");
      }
    }

    public void setAutoResFreqMax(String autoResFreqMax) {
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        this.mAutoResFreqMax = autoResFreqMax;
      } else {
        throw new RuntimeException("Error");
      }
    }

    public FILTER_RESPONSE getAutoResResponse() {
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        return this.mAutoResResponse;
      } else {
        throw new RuntimeException("Error");
      }
    }

    public void setAutoResResponse(FILTER_RESPONSE autoResResponse) {
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        this.mAutoResResponse = autoResResponse;
      } else {
        throw new RuntimeException("Error at setAutoResResponse");
      }
    }

    public int getAutoResSensitivity() {
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        return this.mAutoResSensitivity;
      } else {
        throw new RuntimeException("Error");
      }
    }

    public void setAutoResSensitivity(String autoResSensitivity) {
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        this.mAutoResSensitivity = Integer.parseInt(autoResSensitivity);
      } else {
        throw new RuntimeException("Error");
      }
    }

    public String getResHiCut() {
      if (this.type == FILTER_ALGO_TYPE.RESONANCE_FILTER) {
        return this.mResHiCut;
      } else {
        throw new RuntimeException("Error");
      }
    }

    public void setResHiCut(String resHiCut) {
      if (this.type == FILTER_ALGO_TYPE.RESONANCE_FILTER) {
        this.mResHiCut = resHiCut;
      } else {
        throw new RuntimeException("Error");
      }
    }

    public int getResHiResonance() {
      if (this.type == FILTER_ALGO_TYPE.RESONANCE_FILTER) {
        return this.mResHiResonance;
      } else {
        throw new RuntimeException("Error");
      }
    }

    public void setResHiResonance(int resHiResonance) {
      if (this.type == FILTER_ALGO_TYPE.RESONANCE_FILTER) {
        this.mResHiResonance = resHiResonance;
      } else {
        throw new RuntimeException("Error");
      }
    }

    public void setResHiResonance(String resHiResonance) {
      if (this.type == FILTER_ALGO_TYPE.RESONANCE_FILTER) {
        this.mResHiResonance = Integer.parseInt(resHiResonance);
      } else {
        throw new RuntimeException("Error");
      }
    }

    public ModifierField getMHiCutModifier() {
      return this.mHiCutModifier;
    }

    public ModifierField getMResModifier() {
      return this.mResModifier;
    }

    public void debug() {
      System.out.println("mFilterOrder\t:" + this.mFilterOrder);
      if (this.type == FILTER_ALGO_TYPE.AUTO_FILTER) {
        System.out.println("mAutoResSensitivity \t:" + this.mAutoResSensitivity);
        System.out.println("mAutoResResponse    \t:" + this.mAutoResResponse);
        System.out.println("mAutoResFreqMax     \t:" + this.mAutoResFreqMax);
      } else {
        System.out.println("mResHiCut  \t\t\t:" + this.mResHiCut);
        System.out.println("mResHiResonance \t:" + this.mResHiResonance);
      }

    }

    public enum FILTER_ORDER {
      SECOND("2nd", 0),
      FORTH("4th", 1);

      final String name;
      final int value;

      FILTER_ORDER(String nm, int vl) {
        this.name = nm;
        this.value = vl;
      }

      static GmFilterModEffect.GmFilterAlgorithm.FILTER_ORDER getOrder(int order) {
        switch(order) {
          case 0:
            return SECOND;
          case 1:
            return FORTH;
          default:
            return null;
        }
      }
    }

    public enum FILTER_RESPONSE {
      SLOW("SLOW", 0),
      MED("MEDIUM", 1),
      FAST("FAST", 2);

      final String name;
      final int value;

      FILTER_RESPONSE(String nm, int vl) {
        this.name = nm;
        this.value = vl;
      }

      static FILTER_RESPONSE getResponse(int res) {
        switch(res) {
          case 0:
            return SLOW;
          case 1:
            return MED;
          case 2:
            return FAST;
          default:
            System.out.println("\n ERROR: FILTER_RESPONSE " + res);
            return SLOW;
        }
      }
    }
  }

  public static class GmPannerAlgorithm extends GmFilterModEffect.SdtFilterAlgorithm {
    final ModifierField mWidthModifier;

    GmPannerAlgorithm(FILTER_ALGO_TYPE filType) {
      this.type = filType;
      this.mSpeedModifier = new ModifierField(28);
      this.mDepthModifier = null;
      this.mFbModifier = null;
      this.mWidthModifier = new ModifierField(32);
      this.outLvlModifier = new ModifierField(36);
    }

    public void initialize(byte[] pData) {
      this.initSpeedDepthTempo(pData);

      try {
        this.mWidthModifier.initialize(pData, 32);
        this.outLvlModifier.initialize(pData, 36);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public ModifierField getMWidthModifier() {
      return this.mWidthModifier;
    }

    public void writeToSysex(byte[] pData) {
      this.mWidthModifier.writeToSysEx(pData);
      super.writeToSysex(pData);
    }

  }

  public static class GmPhaserAlgorithm extends GmFilterModEffect.SdtFilterAlgorithm {
    int mPhaserFeedback;
    GmFilterModEffect.GmPhaserAlgorithm.PHASER_RANGE mPhaserRange;
    boolean mPhaserPhaseReverse;

    GmPhaserAlgorithm(FILTER_ALGO_TYPE filType) {
      this.mPhaserFeedback = Integer.parseInt(GmFilterModEffect.feedbackRange.getMinValue());
      this.mPhaserRange = GmFilterModEffect.GmPhaserAlgorithm.PHASER_RANGE.LOW;
      this.mPhaserPhaseReverse = false;
      this.type = filType;
      this.mSpeedModifier = new ModifierField(28);
      this.mDepthModifier = new ModifierField(32);
      this.mFbModifier = new ModifierField(36);
      this.mixModifier = new ModifierField(40);
      this.outLvlModifier = new ModifierField(44);
    }

    public void initialize(byte[] pData) {
      this.initSpeedDepthTempo(pData);
      this.mPhaserFeedback = GmajorPatch.readInt(pData, 260, 4);
      this.mPhaserRange = GmFilterModEffect.GmPhaserAlgorithm.PHASER_RANGE.getRange(GmajorPatch.readInt(pData, 264, 4));
      this.mPhaserPhaseReverse = GmajorPatch.readInt(pData, 268, 4) == 0;

      try {
        this.mixModifier.initialize(pData, 40);
        this.outLvlModifier.initialize(pData, 44);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.mPhaserFeedback, 260, 4);
      GmajorPatch.writeInt(pData, this.mPhaserRange.value, 264, 4);
      GmajorPatch.writeInt(pData, this.mPhaserPhaseReverse ? 0 : 1, 268, 4);
      super.writeToSysex(pData);
    }

    public int getFeedback() {
      return this.mPhaserFeedback;
    }

    public void setFeedBack(String fb) {
      this.mPhaserFeedback = Integer.parseInt(fb);
    }

    public GmFilterModEffect.GmPhaserAlgorithm.PHASER_RANGE getRange() {
      return this.mPhaserRange;
    }

    public void setRange(GmFilterModEffect.GmPhaserAlgorithm.PHASER_RANGE range) {
      this.mPhaserRange = range;
    }

    public boolean isPhaseReverse() {
      return this.mPhaserPhaseReverse;
    }

    public void setPhaseReverse(boolean rev) {
      this.mPhaserPhaseReverse = rev;
    }

    public void debug() {
      super.debug();
      System.out.println("PhaserFeedback\t\t:" + this.mPhaserFeedback);
      System.out.println("PhaserRange\t\t\t:" + this.mPhaserRange);
      System.out.println("PhaserPhaseReverse\t:" + this.mPhaserPhaseReverse);
    }

    public enum PHASER_RANGE {
      LOW("LOW", 0),
      HI("HI", 1);

      final String name;
      final int value;

      PHASER_RANGE(String nm, int vl) {
        this.name = nm;
        this.value = vl;
      }

      static GmFilterModEffect.GmPhaserAlgorithm.PHASER_RANGE getRange(int range) {
        switch(range) {
          case 0:
            return LOW;
          case 1:
            return HI;
          default:
            return null;
        }
      }
    }
  }

  public static class GmTremoloAlgorithm extends GmFilterModEffect.SdtFilterAlgorithm {
    static final IntRange lfoWidthRange = new IntRange(0, 100) {
      public String getUnit() {
        return "%";
      }
    };
    GmFilterModEffect.GmTremoloAlgorithm.TREMOLO_TYPE mTremoloType;
    int mLfoPulseWidth;
    String mTremoloHiCut;
    final ModifierField mHiCutModifier;

    GmTremoloAlgorithm(FILTER_ALGO_TYPE filType) {
      this.mTremoloType = GmFilterModEffect.GmTremoloAlgorithm.TREMOLO_TYPE.SOFT;
      this.mLfoPulseWidth = Integer.parseInt(lfoWidthRange.getMinValue());
      this.mTremoloHiCut = HiCutFreqRange.getFullHiCutInstance().getMinValue();
      this.type = filType;
      this.mSpeedModifier = new ModifierField(28);
      this.mDepthModifier = new ModifierField(32);
      this.mHiCutModifier = new ModifierField(36);
      this.mFbModifier = null;
      this.outLvlModifier = new ModifierField(40);
    }

    public void initialize(byte[] pData) {
      this.mTremoloType = GmFilterModEffect.GmTremoloAlgorithm.TREMOLO_TYPE.getType(GmajorPatch.readInt(pData, 256, 4));
      this.mLfoPulseWidth = GmajorPatch.readInt(pData, 260, 4);
      this.mTremoloHiCut = HiCutFreqRange.getFullHiCutInstance().getItem(GmajorPatch.readInt(pData, 272, 4));
      this.initSpeedDepthTempo(pData);

      try {
        this.mHiCutModifier.initialize(pData, 36);
        this.outLvlModifier.initialize(pData, 40);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.mTremoloType.value, 256, 4);
      GmajorPatch.writeInt(pData, this.mLfoPulseWidth, 260, 4);
      GmajorPatch.writeInt(pData, HiCutFreqRange.getFullHiCutInstance().getIndexForValue(this.mTremoloHiCut), 272, 4);
      super.writeToSysex(pData);
      this.mHiCutModifier.writeToSysEx(pData);
    }

    public GmFilterModEffect.GmTremoloAlgorithm.TREMOLO_TYPE getTremoloType() {
      return this.mTremoloType;
    }

    public void setTremoloType(GmFilterModEffect.GmTremoloAlgorithm.TREMOLO_TYPE tremType) {
      this.mTremoloType = tremType;
    }

    public int getLfoPulseWidth() {
      return this.mLfoPulseWidth;
    }

    public void setLfoPulseWidth(String lfoPulseWidth) {
      this.mLfoPulseWidth = Integer.parseInt(lfoPulseWidth);
    }

    public String getHiCut() {
      return this.mTremoloHiCut;
    }

    public void setHiCut(String hiCut) {
      this.mTremoloHiCut = hiCut;
    }

    public ModifierField getMHiCutModifier() {
      return this.mHiCutModifier;
    }

    public void debug() {
      super.debug();
      System.out.println("mTremoloType   \t:" + this.mTremoloType);
      System.out.println("mLfoPulseWidth \t:" + this.mLfoPulseWidth);
      System.out.println("mTremoloHiCut  \t:" + this.mTremoloHiCut);
    }

    public enum TREMOLO_TYPE {
      SOFT("SOFT", 0),
      HARD("HARD", 1);

      final String name;
      final int value;

      TREMOLO_TYPE(String nm, int vl) {
        this.name = nm;
        this.value = vl;
      }

      static GmFilterModEffect.GmTremoloAlgorithm.TREMOLO_TYPE getType(int type) {
        switch(type) {
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

  abstract static class SdtFilterAlgorithm extends GmEffectAlgorithm {
    String mSpeed = SpeedRange.getSpeedInstance().getMinValue();
    int mDepth;
    String mTempo;
    ModifierField mSpeedModifier;
    ModifierField mDepthModifier;
    ModifierField mFbModifier;

    SdtFilterAlgorithm() {
      this.mDepth = Integer.parseInt(GmFilterModEffect.depthRange.getMinValue());
      this.mTempo = TapTempoRange.getInstance().getMinValue();
    }

    void initSpeedDepthTempo(byte[] pData) {
      this.mSpeed = SpeedRange.getSpeedInstance().getItem(GmajorPatch.readInt(pData, 244, 4));
      this.mDepth = GmajorPatch.readInt(pData, 248, 4);
      this.mTempo = TapTempoRange.getInstance().getItem(GmajorPatch.readInt(pData, 252, 4));

      try {
        this.mSpeedModifier.initialize(pData, 28);
        if (this.mDepthModifier != null) {
          this.mDepthModifier.initialize(pData, 32);
        }

        if (this.mFbModifier != null) {
          this.mFbModifier.initialize(pData, 36);
        }
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      if (this.mixModifier != null) {
        this.mixModifier.writeToSysEx(pData);
      }

      this.outLvlModifier.writeToSysEx(pData);
      this.mSpeedModifier.writeToSysEx(pData);
      if (this.mDepthModifier != null) {
        this.mDepthModifier.writeToSysEx(pData);
      }

      if (this.mFbModifier != null) {
        this.mFbModifier.writeToSysEx(pData);
      }

      GmajorPatch.writeInt(pData, SpeedRange.getSpeedInstance().getIndexForValue(this.mSpeed), 244, 4);
      GmajorPatch.writeInt(pData, this.mDepth, 248, 4);
      GmajorPatch.writeInt(pData, TapTempoRange.getInstance().getIndexForValue(this.mTempo), 252, 4);
    }

    public String getSpeed() {
      return this.mSpeed;
    }

    public void setSpeed(String speed) {
      this.mSpeed = speed;
    }

    public int getDepth() {
      return this.mDepth;
    }

    public void setDepth(String depth) {
      this.mDepth = Integer.parseInt(depth);
    }

    public String getTempo() {
      return this.mTempo;
    }

    public void setTempo(String tempo) {
      this.mTempo = tempo;
    }

    public ModifierField getMDepthModifier() {
      return this.mDepthModifier;
    }

    public ModifierField getMFbModifier() {
      return this.mFbModifier;
    }

    public ModifierField getMSpeedModifier() {
      return this.mSpeedModifier;
    }

    public void debug() {
      System.out.println("Speed\t\t:" + this.mSpeed + " " + SpeedRange.getSpeedInstance().getUnit());
      System.out.println("Depth\t\t:" + this.mDepth);
      System.out.println("Tempo\t\t:" + this.mTempo + " " + TapTempoRange.getInstance().getUnit());
    }
  }
}
