//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.GmajorPatch;
import com.glydian.model.IntRange;
import com.glydian.model.ModifierField;
import com.glydian.model.DiscreteRange.ReverbHiColorRange;
import com.glydian.model.DiscreteRange.ReverbLoColorRange;
import com.glydian.model.DiscreteRange.ReverbSizeRange;

import java.io.IOException;

public class GmReverbEffect extends GmMultiAlgorithmEffect {
  public static final IntRange decayRange = new IntRange(1, 200) {
    public String getUnit() {
      return "/10 ms";
    }
  };
  public static final IntRange preDelayRange = new IntRange(0, 100) {
    public String getUnit() {
      return "ms";
    }
  };
  public static final IntRange factorRange = new IntRange(-25, 25) {
    public String getUnit() {
      return " ";
    }
  };

  public void setAlgorithmType(ALGORITHM_TYPE algorithmType) {
    this.algorithmType = algorithmType;
    this.algorithm = REVERB_ALGO_TYPE.createAlgorithmForType((REVERB_ALGO_TYPE)this.algorithmType);
  }

  public GmReverbEffect(GmajorPatch parent) {
    super(parent, MULTI_ALGO_EFFECT_TYPE.REVERB);
    this.setAlgorithmType(REVERB_ALGO_TYPE.HALL);
  }

  public void initialize(byte[] pData) {
    this.mEffectType = MULTI_ALGO_EFFECT_TYPE.REVERB;
    this.mix = GmajorPatch.readInt(pData, 532, 4);
    this.outLevel = GmajorPatch.readInt(pData, 536, 4);
    this.setEffectOnOffFlag(pData[544]);
    this.setAlgorithmType(REVERB_ALGO_TYPE.getAlgorithmType(GmajorPatch.readInt(pData, 484, 4)));
    this.algorithm.initialize(pData);
  }

  public void writeToSysex(byte[] pData) {
    this.writeDefaultModifierData(pData, 148, 152);
    GmajorPatch.writeInt(pData, this.mix, 532, 4);
    GmajorPatch.writeInt(pData, this.outLevel, 536, 4);
    GmajorPatch.writeInt(pData, this.mEffectOn ? 0 : 1, 544, 4);
    GmajorPatch.writeInt(pData, this.algorithmType.getValue(), 484, 4);
    this.algorithm.writeToSysex(pData);
  }

  public static class HallReverbAlgorithm extends GmReverbEffect.ReverbAlgorithm {
    public HallReverbAlgorithm() {
      this.type = REVERB_ALGO_TYPE.HALL;
    }
  }

  public static class PlateReverbAlgorithm extends GmReverbEffect.ReverbAlgorithm {
    public PlateReverbAlgorithm() {
      this.type = REVERB_ALGO_TYPE.PLATE;
    }
  }

  public enum REVERB_SHAPE {
    ROUND("Round", 0),
    CURVED("Curved", 1),
    SQUARE("Square", 2);

    final String name;
    final int value;

    REVERB_SHAPE(String nm, int vl) {
      this.name = nm;
      this.value = vl;
    }

    static GmReverbEffect.REVERB_SHAPE getOnOff(int onOff) {
      switch(onOff) {
        case 0:
          return ROUND;
        case 1:
          return CURVED;
        case 2:
          return SQUARE;
        default:
          return null;
      }
    }
  }

  public abstract static class ReverbAlgorithm extends GmEffectAlgorithm {
    int decay;
    int preDelay;
    GmReverbEffect.REVERB_SHAPE shape;
    String size;
    String hiColor;
    int hiFactor;
    String loColor;
    int loFactor;
    int roomLevel;
    int reverbLevel;
    int diffuse;

    ReverbAlgorithm() {
      this.decay = Integer.parseInt(GmReverbEffect.decayRange.getMinValue());
      this.preDelay = Integer.parseInt(GmReverbEffect.preDelayRange.getMinValue());
      this.shape = GmReverbEffect.REVERB_SHAPE.CURVED;
      this.size = ReverbSizeRange.getInstance().getMinValue();
      this.hiColor = ReverbHiColorRange.getInstance().getMinValue();
      this.hiFactor = Integer.parseInt(GmReverbEffect.factorRange.getMinValue());
      this.loColor = ReverbLoColorRange.getInstance().getMinValue();
      this.loFactor = Integer.parseInt(GmReverbEffect.factorRange.getMinValue());
      this.roomLevel = Integer.parseInt(GmReverbEffect.outLevelRange.getMinValue());
      this.reverbLevel = Integer.parseInt(GmReverbEffect.outLevelRange.getMinValue());
      this.diffuse = Integer.parseInt(GmReverbEffect.factorRange.getMinValue());
      this.mixModifier = new ModifierField(148);
      this.outLvlModifier = new ModifierField(152);
    }

    public void initialize(byte[] pData) {
      this.decay = GmajorPatch.readInt(pData, 488, 4);
      this.preDelay = GmajorPatch.readInt(pData, 492, 4);
      this.shape = GmReverbEffect.REVERB_SHAPE.getOnOff(GmajorPatch.readInt(pData, 496, 4));
      this.size = ReverbSizeRange.getInstance().getItem(GmajorPatch.readInt(pData, 500, 4));
      this.hiColor = ReverbHiColorRange.getInstance().getItem(GmajorPatch.readInt(pData, 504, 4));
      this.hiFactor = GmajorPatch.readInt(pData, 508, 4);
      this.loColor = ReverbLoColorRange.getInstance().getItem(GmajorPatch.readInt(pData, 512, 4));
      this.loFactor = GmajorPatch.readInt(pData, 516, 4);
      this.roomLevel = GmajorPatch.readInt(pData, 520, 4);
      this.reverbLevel = GmajorPatch.readInt(pData, 524, 4);
      this.diffuse = GmajorPatch.readInt(pData, 528, 4);

      try {
        this.mixModifier.initialize(pData, 148);
        this.outLvlModifier.initialize(pData, 152);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.decay, 488, 4);
      GmajorPatch.writeInt(pData, this.preDelay, 492, 4);
      GmajorPatch.writeInt(pData, this.shape.value, 496, 4);
      GmajorPatch.writeInt(pData, ReverbSizeRange.getInstance().getIndexForValue(this.size), 500, 4);
      GmajorPatch.writeInt(pData, ReverbHiColorRange.getInstance().getIndexForValue(this.hiColor), 504, 4);
      GmajorPatch.writeInt(pData, this.hiFactor, 508, 4);
      GmajorPatch.writeInt(pData, ReverbLoColorRange.getInstance().getIndexForValue(this.loColor), 512, 4);
      GmajorPatch.writeInt(pData, this.loFactor, 516, 4);
      GmajorPatch.writeInt(pData, this.roomLevel, 520, 4);
      GmajorPatch.writeInt(pData, this.reverbLevel, 524, 4);
      GmajorPatch.writeInt(pData, this.diffuse, 528, 4);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
    }

    public int getDecay() {
      return this.decay;
    }

    public void setDecay(String decay) {
      this.decay = Integer.parseInt(decay);
    }

    public int getDiffuse() {
      return this.diffuse;
    }

    public void setDiffuse(String diffuse) {
      this.diffuse = Integer.parseInt(diffuse);
    }

    public String getHiColor() {
      return this.hiColor;
    }

    public void setHiColor(String hiColor) {
      this.hiColor = hiColor;
    }

    public int getHiFactor() {
      return this.hiFactor;
    }

    public void setHiFactor(String hiFactor) {
      this.hiFactor = Integer.parseInt(hiFactor);
    }

    public String getLoColor() {
      return this.loColor;
    }

    public void setLoColor(String loColor) {
      this.loColor = loColor;
    }

    public int getLoFactor() {
      return this.loFactor;
    }

    public void setLoFactor(String loFactor) {
      this.loFactor = Integer.parseInt(loFactor);
    }

    public int getPreDelay() {
      return this.preDelay;
    }

    public void setPreDelay(String preDelay) {
      this.preDelay = Integer.parseInt(preDelay);
    }

    public int getReverbLevel() {
      return this.reverbLevel;
    }

    public void setReverbLevel(String reverbLevel) {
      this.reverbLevel = Integer.parseInt(reverbLevel);
    }

    public int getRoomLevel() {
      return this.roomLevel;
    }

    public void setRoomLevel(String roomLevel) {
      this.roomLevel = Integer.parseInt(roomLevel);
    }

    public GmReverbEffect.REVERB_SHAPE getShape() {
      return this.shape;
    }

    public void setShape(GmReverbEffect.REVERB_SHAPE shape) {
      this.shape = shape;
    }

    public String getSize() {
      return this.size;
    }

    public void setSize(String size) {
      this.size = size;
    }

    public void debug() {
      System.out.println("decay\t\t:" + this.decay + " " + GmReverbEffect.decayRange.getUnit());
      System.out.println("preDelay\t:" + this.preDelay + " " + GmReverbEffect.preDelayRange.getUnit());
      System.out.println("shape\t\t:" + this.shape);
      System.out.println("size\t\t:" + this.size);
      System.out.println("hiColor\t\t:" + this.hiColor);
      System.out.println("hiFactor \t:" + this.hiFactor);
      System.out.println("loColor\t\t:" + this.loColor);
      System.out.println("loFactor\t:" + this.loFactor);
      System.out.println("roomLevel\t:" + this.roomLevel);
      System.out.println("reverbLevel\t:" + this.reverbLevel);
    }
  }

  public static class RoomReverbAlgorithm extends GmReverbEffect.ReverbAlgorithm {
    public RoomReverbAlgorithm() {
      this.type = REVERB_ALGO_TYPE.ROOM;
    }
  }

  public static class SpringReverbAlgorithm extends GmReverbEffect.ReverbAlgorithm {
    public SpringReverbAlgorithm() {
      this.type = REVERB_ALGO_TYPE.SPRING;
    }
  }
}
