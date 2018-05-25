//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.DiscreteRange;
import com.glydian.model.GmajorPatch;
import com.glydian.model.IntRange;
import com.glydian.model.ModifierField;
import com.glydian.model.effect.GmDelayEffect.DualDelayAlgorithm;
import com.glydian.model.effect.GmDelayEffect.DynamicDelayAlgorithm;
import com.glydian.model.effect.GmDelayEffect.PingPongDelayAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmFilterAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmPannerAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmPhaserAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmTremoloAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmAdvanceChorusAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmAdvanceFlangAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmClassicChorusAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmClassicFlangAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmVibratoAlgorithm;
import com.glydian.model.effect.GmPitchEffect.GmDetuneAlgorithm;
import com.glydian.model.effect.GmPitchEffect.GmOctaverAlgorithm;
import com.glydian.model.effect.GmPitchEffect.GmShifterAlgorithm;
import com.glydian.model.effect.GmPitchEffect.GmWhammyAlgorithm;
import com.glydian.model.effect.GmReverbEffect.HallReverbAlgorithm;
import com.glydian.model.effect.GmReverbEffect.PlateReverbAlgorithm;
import com.glydian.model.effect.GmReverbEffect.RoomReverbAlgorithm;
import com.glydian.model.effect.GmReverbEffect.SpringReverbAlgorithm;

public abstract class GmMultiAlgorithmEffect extends GmEffect {
  public static final IntRange depthRange = new IntRange(0, 100) {
    public String getUnit() {
      return "%";
    }
  };
  public static final IntRange feedbackRange = new IntRange(-100, 100) {
    public String getUnit() {
      return " ";
    }
  };
  public static final IntRange shortDelayRange = new IntRange(1, 500) {
    public String getUnit() {
      return "/10 ms";
    }
  };
  public static final IntRange mixRange = new IntRange(0, 100) {
    public String getUnit() {
      return "%";
    }
  };
  public static final IntRange outLevelRange = new IntRange(-100, 0) {
    public String getUnit() {
      return "dB";
    }
  };
  GmMultiAlgorithmEffect.ALGORITHM_TYPE algorithmType;
  GmMultiAlgorithmEffect.GmEffectAlgorithm algorithm;
  int mix;
  int outLevel;

  GmMultiAlgorithmEffect(GmajorPatch parent, EFFECT_TYPE type) {
    super(parent, type);
  }

  public void debug() {
    super.debug();
    System.out.println("algorithmType \t:" + this.algorithmType);
    this.algorithm.debug();
    System.out.println("mix \t\t  \t:" + this.getMix());
    System.out.println("outLevel \t  \t:" + this.getOutLevel());
    System.out.println("mix Modifier  \t:");
    if (this.getMixModifier() != null) {
      this.getMixModifier().debug();
    }

    System.out.println("Out Level Modifier  \t:");
    this.getOutLvlModifier().debug();
  }

  public GmMultiAlgorithmEffect.GmEffectAlgorithm getAlgorithm() {
    return this.algorithm;
  }

  protected void setAlgorithm(GmMultiAlgorithmEffect.GmEffectAlgorithm algorithm) {
    this.algorithm = algorithm;
  }

  public GmMultiAlgorithmEffect.ALGORITHM_TYPE getAlgorithmType() {
    return this.algorithmType;
  }

  public abstract void setAlgorithmType(GmMultiAlgorithmEffect.ALGORITHM_TYPE var1);

  public int getMix() {
    return this.mix;
  }

  public void setMix(String mix) {
    this.mix = Integer.parseInt(mix);
  }

  public int getOutLevel() {
    return this.outLevel;
  }

  public void setOutLevel(String outLevel) {
    this.outLevel = Integer.parseInt(outLevel);
  }

  public boolean isMixModAssignable() {
    return true;
  }

  public boolean isOutLevelModAssignable() {
    return true;
  }

  public ModifierField getMixModifier() {
    return this.algorithm.getMixModifier();
  }

  public ModifierField getOutLvlModifier() {
    return this.algorithm.getOutLvlModifier();
  }

  public void copyValuesFrom(GmEffect libEffect) {
    if (libEffect.getEffectType() == this.getEffectType()) {
      GmMultiAlgorithmEffect copyFromEffect = (GmMultiAlgorithmEffect)libEffect;
      this.algorithmType = copyFromEffect.algorithmType;
      this.algorithm = copyFromEffect.algorithm;
      this.outLevel = copyFromEffect.outLevel;
      this.mix = copyFromEffect.mix;
    }

  }

  void writeDefaultModifierData(byte[] pData, int start, int end) {
    ModifierField defaultModifier = new ModifierField("Default");

    for(int i = start; i <= end; i += 4) {
      defaultModifier.writeBitsToByteArray(pData, i);
    }

  }

  public interface ALGORITHM_TYPE {
    String getName();

    int getValue();
  }

  public enum DELAY_ALGO_TYPE implements GmMultiAlgorithmEffect.ALGORITHM_TYPE {
    PING_PONG("PING PONG", 0),
    DYNAMIC("DYNAMIC", 1),
    DUAL("DUAL", 2);

    final String name;
    private final int value;

    public String getName() {
      return this.name;
    }

    public int getValue() {
      return this.value;
    }

    DELAY_ALGO_TYPE(String myName, int val) {
      this.name = myName;
      this.value = val;
    }

    static GmMultiAlgorithmEffect.DELAY_ALGO_TYPE getAlgorithmType(int pEffectTypeByte) {
      switch(pEffectTypeByte) {
        case 0:
          return PING_PONG;
        case 1:
          return DYNAMIC;
        case 2:
        case 12:
          return DUAL;
        default:
          throw new RuntimeException("Wrong EffectTypeByte " + pEffectTypeByte);
      }
    }

    static GmMultiAlgorithmEffect.GmEffectAlgorithm createAlgorithmForType(GmMultiAlgorithmEffect.DELAY_ALGO_TYPE type) {
      switch(type.value) {
        case 0:
          return new PingPongDelayAlgorithm();
        case 1:
          return new DynamicDelayAlgorithm();
        case 2:
          return new DualDelayAlgorithm();
        default:
          return null;
      }
    }
  }

  public enum FILTER_ALGO_TYPE implements GmMultiAlgorithmEffect.ALGORITHM_TYPE {
    AUTO_FILTER("AUTO FILTER", 0),
    RESONANCE_FILTER("RESONANCE FILTER", 1),
    VINTAGE_PHASER("VINTAGE PHASER", 2),
    SMOOTH_PHASER("SMOOTH PHASER", 3),
    TREMOLO("TREMOLO", 4),
    PANNER("PANNER", 5);

    private final int value;
    private final String name;

    public String getName() {
      return this.name;
    }

    public int getValue() {
      return this.value;
    }

    FILTER_ALGO_TYPE(String myName, int val) {
      this.value = val;
      this.name = myName;
    }

    static GmMultiAlgorithmEffect.FILTER_ALGO_TYPE getAlgorithmType(int pEffectTypeByte) {
      switch(pEffectTypeByte) {
        case 0:
          return AUTO_FILTER;
        case 1:
          return RESONANCE_FILTER;
        case 2:
          return VINTAGE_PHASER;
        case 3:
          return SMOOTH_PHASER;
        case 4:
          return TREMOLO;
        case 5:
          return PANNER;
        default:
          throw new RuntimeException("Wrong EffectTypeByte");
      }
    }

    static GmMultiAlgorithmEffect.GmEffectAlgorithm createAlgorithmForType(GmMultiAlgorithmEffect.FILTER_ALGO_TYPE filType) {
      switch(filType.getValue()) {
        case 0:
          return new GmFilterAlgorithm(AUTO_FILTER);
        case 1:
          return new GmFilterAlgorithm(RESONANCE_FILTER);
        case 2:
          return new GmPhaserAlgorithm(VINTAGE_PHASER);
        case 3:
          return new GmPhaserAlgorithm(SMOOTH_PHASER);
        case 4:
          return new GmTremoloAlgorithm(TREMOLO);
        case 5:
          return new GmPannerAlgorithm(PANNER);
        default:
          return null;
      }
    }
  }

  public abstract static class GmEffectAlgorithm {
    GmMultiAlgorithmEffect.ALGORITHM_TYPE type;
    ModifierField mixModifier;
    ModifierField outLvlModifier;

    GmEffectAlgorithm() {
    }

    public GmMultiAlgorithmEffect.ALGORITHM_TYPE getType() {
      return this.type;
    }

    ModifierField getMixModifier() {
      return this.mixModifier;
    }

    ModifierField getOutLvlModifier() {
      return this.outLvlModifier;
    }

    public abstract void initialize(byte[] var1);

    public abstract void writeToSysex(byte[] var1);

    public abstract void debug();
  }

  public static class HiCutFreqRange extends DiscreteRange {
    final String unit = "Hz";
    static final GmMultiAlgorithmEffect.HiCutFreqRange meResonanceHiCut = new GmMultiAlgorithmEffect.HiCutFreqRange("158.5", "14.1k");
    static final GmMultiAlgorithmEffect.HiCutFreqRange meFullHiCut = new GmMultiAlgorithmEffect.HiCutFreqRange("19.95", "Off  ");
    static final GmMultiAlgorithmEffect.HiCutFreqRange meAutoResMaxFreq = new GmMultiAlgorithmEffect.HiCutFreqRange("1.00k", "10.0k");
    static final GmMultiAlgorithmEffect.HiCutFreqRange meDelayFbHiCut = new GmMultiAlgorithmEffect.HiCutFreqRange("2.00k", "Off  ");

    public String getUnit() {
      return this.unit;
    }

    public String getName() {
      return "HiCutFreqRange";
    }

    HiCutFreqRange(String minValue, String maxValue) {
      super(DISCRETE_RANGE_TYPE.HI_CUT, minValue, maxValue);
    }

    public static GmMultiAlgorithmEffect.HiCutFreqRange getResonanceHiCutInstance() {
      return meResonanceHiCut;
    }

    public static GmMultiAlgorithmEffect.HiCutFreqRange getFullHiCutInstance() {
      return meFullHiCut;
    }

    public static GmMultiAlgorithmEffect.HiCutFreqRange getAutoResonanceMaxFreqInstance() {
      return meAutoResMaxFreq;
    }

    public static GmMultiAlgorithmEffect.HiCutFreqRange getDelayFbHiCutInstance() {
      return meDelayFbHiCut;
    }
  }

  public static class LoCutRange extends DiscreteRange {
    final String unit = "Hz";
    static final GmMultiAlgorithmEffect.LoCutRange meLoCut = new GmMultiAlgorithmEffect.LoCutRange("2.00k");

    public String getUnit() {
      return this.unit;
    }

    public String getName() {
      return "LO CUT";
    }

    LoCutRange(String maxValue) {
      super(DISCRETE_RANGE_TYPE.LO_CUT, "Off  ", maxValue);
    }

    public static GmMultiAlgorithmEffect.LoCutRange getFullLoCutInstance() {
      return meLoCut;
    }
  }

  public enum MODULATION_ALGO_TYPE implements GmMultiAlgorithmEffect.ALGORITHM_TYPE {
    CLA_CHO("CLASSIC CHORUS", 0),
    ADV_CHO("ADVANCE CHORUS", 1),
    CLA_FLA("CLASSIC FLANGE", 2),
    ADV_FLA("ADVANCE FLANGE", 3),
    VIB("VIBRATO", 4);

    private final String name;
    private final int value;

    public String getName() {
      return this.name;
    }

    public int getValue() {
      return this.value;
    }

    MODULATION_ALGO_TYPE(String myName, int val) {
      this.name = myName;
      this.value = val;
    }

    static GmMultiAlgorithmEffect.MODULATION_ALGO_TYPE getAlgorithmType(int pEffectTypeByte) {
      switch(pEffectTypeByte) {
        case 0:
          return CLA_CHO;
        case 1:
          return ADV_CHO;
        case 2:
          return CLA_FLA;
        case 3:
          return ADV_FLA;
        case 4:
          return VIB;
        default:
          throw new RuntimeException("Wrong EffectTypeByte");
      }
    }

    static GmMultiAlgorithmEffect.GmEffectAlgorithm createAlgorithmForType(GmMultiAlgorithmEffect.MODULATION_ALGO_TYPE type) {
      switch(type.getValue()) {
        case 0:
          return new GmClassicChorusAlgorithm();
        case 1:
          return new GmAdvanceChorusAlgorithm();
        case 2:
          return new GmClassicFlangAlgorithm();
        case 3:
          return new GmAdvanceFlangAlgorithm();
        case 4:
          return new GmVibratoAlgorithm();
        default:
          return null;
      }
    }
  }

  public enum MULTI_ALGO_EFFECT_TYPE implements EFFECT_TYPE {
    FILTER("FILTER/MOD"),
    PITCH("PITCH"),
    CHOFLA("CHO/FLA"),
    REVERB("REVERB"),
    DELAY("DELAY");

    final String name;

    public String getName() {
      return this.name;
    }

    MULTI_ALGO_EFFECT_TYPE(String myName) {
      this.name = myName;
    }
  }

  public enum OFFON_VALUE {
    ON("On", 0),
    OFF("Off", 1);

    final String name;
    final int value;

    OFFON_VALUE(String nm, int vl) {
      this.name = nm;
      this.value = vl;
    }

    public static GmMultiAlgorithmEffect.OFFON_VALUE getOnOff(int onOff) {
      switch(onOff) {
        case 0:
          return ON;
        case 1:
          return OFF;
        default:
          return null;
      }
    }
  }

  public enum ONOFF_VALUE {
    ON("On", 1),
    OFF("Off", 0);

    final String name;
    public final int value;

    ONOFF_VALUE(String nm, int vl) {
      this.name = nm;
      this.value = vl;
    }

    public static GmMultiAlgorithmEffect.ONOFF_VALUE getOnOff(int onOff) {
      switch(onOff) {
        case 0:
          return OFF;
        case 1:
          return ON;
        default:
          return null;
      }
    }
  }

  public enum PITCH_ALGO_TYPE implements GmMultiAlgorithmEffect.ALGORITHM_TYPE {
    DETUNE("DETUNE", 0),
    WHAMMY("WHAMMY", 1),
    OCTAVER("OCTAVER", 2),
    SHIFTER("PITCH SHIFTER", 3);

    final String name;
    private final int value;

    public String getName() {
      return this.name;
    }

    public int getValue() {
      return this.value;
    }

    PITCH_ALGO_TYPE(String myName, int val) {
      this.name = myName;
      this.value = val;
    }

    static GmMultiAlgorithmEffect.PITCH_ALGO_TYPE getAlgorithmType(int pEffectTypeByte) {
      switch(pEffectTypeByte) {
        case 0:
          return DETUNE;
        case 1:
          return WHAMMY;
        case 2:
          return OCTAVER;
        case 3:
          return SHIFTER;
        default:
          throw new RuntimeException("Wrong EffectTypeByte");
      }
    }

    static GmMultiAlgorithmEffect.GmEffectAlgorithm createAlgorithmForType(GmMultiAlgorithmEffect.PITCH_ALGO_TYPE type) {
      switch(type.getValue()) {
        case 0:
          return new GmDetuneAlgorithm();
        case 1:
          return new GmWhammyAlgorithm();
        case 2:
          return new GmOctaverAlgorithm();
        case 3:
          return new GmShifterAlgorithm();
        default:
          return null;
      }
    }
  }

  public enum REVERB_ALGO_TYPE implements GmMultiAlgorithmEffect.ALGORITHM_TYPE {
    SPRING("SPRING", 0),
    HALL("HALL", 1),
    ROOM("ROOM", 2),
    PLATE("PLATE", 3);

    final String name;
    private final int value;

    public String getName() {
      return this.name;
    }

    public int getValue() {
      return this.value;
    }

    REVERB_ALGO_TYPE(String myName, int val) {
      this.name = myName;
      this.value = val;
    }

    static GmMultiAlgorithmEffect.REVERB_ALGO_TYPE getAlgorithmType(int pEffectTypeByte) {
      switch(pEffectTypeByte) {
        case 0:
          return SPRING;
        case 1:
          return HALL;
        case 2:
          return ROOM;
        case 3:
          return PLATE;
        default:
          throw new RuntimeException("Wrong EffectTypeByte");
      }
    }

    static GmMultiAlgorithmEffect.GmEffectAlgorithm createAlgorithmForType(GmMultiAlgorithmEffect.REVERB_ALGO_TYPE type) {
      switch(type.getValue()) {
        case 1:
          return new SpringReverbAlgorithm();
        case 2:
          return new HallReverbAlgorithm();
        case 3:
          return new RoomReverbAlgorithm();
        case 4:
          return new PlateReverbAlgorithm();
        default:
          return null;
      }
    }
  }

  public static class SpeedRange extends DiscreteRange {
    final String unit = "ms";
    static final GmMultiAlgorithmEffect.SpeedRange meSpeed = new GmMultiAlgorithmEffect.SpeedRange();

    public String getUnit() {
      return this.unit;
    }

    public String getName() {
      return "SpeedRange";
    }

    SpeedRange() {
      super(DISCRETE_RANGE_TYPE.SPEED, ".050 ", "19.95");
    }

    public static GmMultiAlgorithmEffect.SpeedRange getSpeedInstance() {
      return meSpeed;
    }
  }
}
