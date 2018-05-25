//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model.effect;

import com.glydian.model.GmajorPatch;
import com.glydian.model.IntRange;
import com.glydian.model.ModifierField;

import java.io.IOException;

public class GmPitchEffect extends GmMultiAlgorithmEffect {
  public GmPitchEffect(GmajorPatch parent) {
    super(parent, MULTI_ALGO_EFFECT_TYPE.PITCH);
    this.setAlgorithmType(PITCH_ALGO_TYPE.DETUNE);
  }

  public void initialize(byte[] pData) {
    this.mEffectType = MULTI_ALGO_EFFECT_TYPE.PITCH;
    this.mix = GmajorPatch.readInt(pData, 340, 4);
    this.outLevel = GmajorPatch.readInt(pData, 344, 4);
    this.setEffectOnOffFlag(pData[352]);
    this.setAlgorithmType(PITCH_ALGO_TYPE.getAlgorithmType(GmajorPatch.readInt(pData, 292, 4)));
    this.algorithm.initialize(pData);
  }

  public void writeToSysex(byte[] pData) {
    this.writeDefaultModifierData(pData, 48, 76);
    GmajorPatch.writeInt(pData, this.mix, 340, 4);
    GmajorPatch.writeInt(pData, this.outLevel, 344, 4);
    GmajorPatch.writeInt(pData, this.mEffectOn ? 0 : 1, 352, 4);
    GmajorPatch.writeInt(pData, this.algorithmType.getValue(), 292, 4);
    this.algorithm.writeToSysex(pData);
  }

  public void setAlgorithmType(ALGORITHM_TYPE algorithmType) {
    this.algorithmType = algorithmType;
    this.algorithm = PITCH_ALGO_TYPE.createAlgorithmForType((PITCH_ALGO_TYPE)this.algorithmType);
  }

  public boolean isMixModAssignable() {
    return this.algorithm.getType() != PITCH_ALGO_TYPE.WHAMMY;
  }

  public static class GmDetuneAlgorithm extends GmEffectAlgorithm {
    public static final IntRange detuneVoiceRange = new IntRange(-100, 100) {
      public String getUnit() {
        return "cents";
      }
    };
    public static final IntRange detuneDelayRange = new IntRange(0, 50) {
      public String getUnit() {
        return "ms";
      }
    };
    int voiceL;
    int voiceR;
    int delayL;
    int delayR;

    public GmDetuneAlgorithm() {
      this.voiceL = Integer.parseInt(detuneVoiceRange.getMinValue());
      this.voiceR = Integer.parseInt(detuneVoiceRange.getMaxValue());
      this.delayL = Integer.parseInt(detuneDelayRange.getMinValue());
      this.delayR = Integer.parseInt(detuneDelayRange.getMaxValue());
      this.type = PITCH_ALGO_TYPE.DETUNE;
      this.mixModifier = new ModifierField(48);
      this.outLvlModifier = new ModifierField(52);
    }

    public void initialize(byte[] pData) {
      this.voiceL = GmajorPatch.readInt(pData, 296, 4);
      this.voiceR = GmajorPatch.readInt(pData, 300, 4);
      this.delayL = GmajorPatch.readInt(pData, 312, 4);
      this.delayR = GmajorPatch.readInt(pData, 316, 4);

      try {
        this.mixModifier.initialize(pData, 48);
        this.outLvlModifier.initialize(pData, 52);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.voiceL, 296, 4);
      GmajorPatch.writeInt(pData, this.voiceR, 300, 4);
      GmajorPatch.writeInt(pData, this.delayL, 312, 4);
      GmajorPatch.writeInt(pData, this.delayR, 316, 4);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
    }

    public int getVoiceL() {
      return this.voiceL;
    }

    public void setVoiceL(String vl) {
      this.voiceL = Integer.parseInt(vl);
    }

    public int getVoiceR() {
      return this.voiceR;
    }

    public void setVoiceR(String vr) {
      this.voiceR = Integer.parseInt(vr);
    }

    public int getDelayL() {
      return this.delayL;
    }

    public void setDelayL(String dl) {
      this.delayL = Integer.parseInt(dl);
    }

    public int getDelayR() {
      return this.delayR;
    }

    public void setDelayR(String dr) {
      this.delayR = Integer.parseInt(dr);
    }

    public void debug() {
      System.out.println("voiceL :" + this.voiceL + " " + detuneVoiceRange.getUnit());
      System.out.println("voiceR :" + this.voiceR + " " + detuneVoiceRange.getUnit());
      System.out.println("delayL :" + this.delayL + " " + detuneDelayRange.getUnit());
      System.out.println("delayR :" + this.delayR + " " + detuneDelayRange.getUnit());
    }
  }

  public static class GmOctaverAlgorithm extends GmEffectAlgorithm {
    GmPitchEffect.PITCH_DIRECTION direction;
    GmPitchEffect.OCTAVE_RANGE octRange;

    public GmOctaverAlgorithm() {
      this.direction = GmPitchEffect.PITCH_DIRECTION.DOWN;
      this.octRange = GmPitchEffect.OCTAVE_RANGE.ONE_OCT;
      this.type = PITCH_ALGO_TYPE.OCTAVER;
      this.mixModifier = new ModifierField(48);
      this.outLvlModifier = new ModifierField(52);
    }

    public void initialize(byte[] pData) {
      this.direction = GmPitchEffect.PITCH_DIRECTION.getDirection(GmajorPatch.readInt(pData, 332, 4));
      this.octRange = GmPitchEffect.OCTAVE_RANGE.getOctRange(GmajorPatch.readInt(pData, 336, 4));

      try {
        this.mixModifier.initialize(pData, 48);
        this.outLvlModifier.initialize(pData, 52);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.direction.value, 332, 4);
      GmajorPatch.writeInt(pData, this.octRange.value, 336, 4);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
    }

    public GmPitchEffect.PITCH_DIRECTION getDirection() {
      return this.direction;
    }

    public void setDirection(GmPitchEffect.PITCH_DIRECTION dir) {
      this.direction = dir;
    }

    public GmPitchEffect.OCTAVE_RANGE getOctRange() {
      return this.octRange;
    }

    public void setOctRange(GmPitchEffect.OCTAVE_RANGE rang) {
      this.octRange = rang;
    }

    public void debug() {
      System.out.println("direction :" + this.direction);
      System.out.println("octRange :" + this.octRange + "-OCT");
    }
  }

  public static class GmShifterAlgorithm extends GmEffectAlgorithm {
    public static final IntRange shifterVoiceRange = new IntRange(-2400, 2400) {
      public String getUnit() {
        return "cents";
      }
    };
    public static final IntRange shifterPanRange = new IntRange(-50, 50) {
      public String getUnit() {
        return " ";
      }
    };
    public static final IntRange shifterDelayRange = new IntRange(0, 350) {
      public String getUnit() {
        return "ms";
      }
    };
    public static final IntRange shifterFeedbackRange = new IntRange(0, 100) {
      public String getUnit() {
        return "%";
      }
    };
    public static final IntRange shifterLevelRange = new IntRange(-100, 0) {
      public String getUnit() {
        return "%";
      }
    };
    int voice1;
    int voice2;
    int pan1;
    int pan2;
    int delay1;
    int delay2;
    int feedback1;
    int feedback2;
    int level1;
    int level2;
    final ModifierField voice1Modifier;
    final ModifierField voice2Modifier;
    final ModifierField pan1Modifier;
    final ModifierField pan2Modifier;
    final ModifierField fb1Modifier;
    final ModifierField fb2Modifier;

    GmShifterAlgorithm() {
      this.voice1 = Integer.parseInt(shifterVoiceRange.getMinValue());
      this.voice2 = Integer.parseInt(shifterVoiceRange.getMaxValue());
      this.pan1 = Integer.parseInt(shifterPanRange.getMinValue());
      this.pan2 = Integer.parseInt(shifterPanRange.getMaxValue());
      this.delay1 = Integer.parseInt(shifterDelayRange.getMinValue());
      this.delay2 = Integer.parseInt(shifterDelayRange.getMinValue());
      this.feedback1 = Integer.parseInt(shifterFeedbackRange.getMinValue());
      this.feedback2 = Integer.parseInt(shifterFeedbackRange.getMaxValue());
      this.level1 = Integer.parseInt(shifterLevelRange.getMinValue());
      this.level2 = Integer.parseInt(shifterLevelRange.getMaxValue());
      this.type = PITCH_ALGO_TYPE.SHIFTER;
      this.voice1Modifier = new ModifierField(48);
      this.voice2Modifier = new ModifierField(52);
      this.pan1Modifier = new ModifierField(56);
      this.pan2Modifier = new ModifierField(60);
      this.fb1Modifier = new ModifierField(64);
      this.fb2Modifier = new ModifierField(68);
      this.mixModifier = new ModifierField(72);
      this.outLvlModifier = new ModifierField(76);
    }

    public void initialize(byte[] pData) {
      this.voice1 = GmajorPatch.readInt(pData, 296, 4);
      this.voice2 = GmajorPatch.readInt(pData, 300, 4);
      this.pan1 = GmajorPatch.readInt(pData, 304, 4);
      this.pan2 = GmajorPatch.readInt(pData, 308, 4);
      this.delay1 = GmajorPatch.readInt(pData, 312, 4);
      this.delay2 = GmajorPatch.readInt(pData, 316, 4);
      this.feedback1 = GmajorPatch.readInt(pData, 320, 4);
      this.feedback2 = GmajorPatch.readInt(pData, 324, 4);
      this.level1 = GmajorPatch.readInt(pData, 328, 4);
      this.level2 = GmajorPatch.readInt(pData, 332, 4);

      try {
        this.voice1Modifier.initialize(pData, 48);
        this.voice2Modifier.initialize(pData, 52);
        this.pan1Modifier.initialize(pData, 56);
        this.pan2Modifier.initialize(pData, 60);
        this.fb1Modifier.initialize(pData, 64);
        this.fb2Modifier.initialize(pData, 68);
        this.mixModifier.initialize(pData, 72);
        this.outLvlModifier.initialize(pData, 76);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.voice1, 296, 4);
      GmajorPatch.writeInt(pData, this.voice2, 300, 4);
      GmajorPatch.writeInt(pData, this.pan1, 304, 4);
      GmajorPatch.writeInt(pData, this.pan2, 308, 4);
      GmajorPatch.writeInt(pData, this.delay1, 312, 4);
      GmajorPatch.writeInt(pData, this.delay2, 316, 4);
      GmajorPatch.writeInt(pData, this.feedback1, 320, 4);
      GmajorPatch.writeInt(pData, this.feedback2, 324, 4);
      GmajorPatch.writeInt(pData, this.level1, 328, 4);
      GmajorPatch.writeInt(pData, this.level2, 332, 4);
      this.voice1Modifier.writeToSysEx(pData);
      this.voice2Modifier.writeToSysEx(pData);
      this.pan1Modifier.writeToSysEx(pData);
      this.pan2Modifier.writeToSysEx(pData);
      this.fb1Modifier.writeToSysEx(pData);
      this.fb2Modifier.writeToSysEx(pData);
      this.mixModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
    }

    public int getVoice1() {
      return this.voice1;
    }

    public void setVoice1(String x) {
      this.voice1 = Integer.parseInt(x);
    }

    public int getVoice2() {
      return this.voice2;
    }

    public void setVoice2(String x) {
      this.voice2 = Integer.parseInt(x);
    }

    public int getPan1() {
      return this.pan1;
    }

    public void setPan1(String x) {
      this.pan1 = Integer.parseInt(x);
    }

    public int getPan2() {
      return this.pan2;
    }

    public void setPan2(String x) {
      this.pan2 = Integer.parseInt(x);
    }

    public int getDelay1() {
      return this.delay1;
    }

    public void setDelay1(String x) {
      this.delay1 = Integer.parseInt(x);
    }

    public int getDelay2() {
      return this.delay2;
    }

    public void setDelay2(String x) {
      this.delay2 = Integer.parseInt(x);
    }

    public int getFeedback1() {
      return this.feedback1;
    }

    public void setFeedback1(String x) {
      this.feedback1 = Integer.parseInt(x);
    }

    public int getFeedback2() {
      return this.feedback2;
    }

    public void setFeedback2(String x) {
      this.feedback2 = Integer.parseInt(x);
    }

    public int getLevel1() {
      return this.level1;
    }

    public void setLevel1(String x) {
      this.level1 = Integer.parseInt(x);
    }

    public int getLevel2() {
      return this.level2;
    }

    public void setLevel2(String x) {
      this.level2 = Integer.parseInt(x);
    }

    public ModifierField getFb1Modifier() {
      return this.fb1Modifier;
    }

    public ModifierField getFb2Modifier() {
      return this.fb2Modifier;
    }

    public ModifierField getPan1Modifier() {
      return this.pan1Modifier;
    }

    public ModifierField getPan2Modifier() {
      return this.pan2Modifier;
    }

    public ModifierField getVoice1Modifier() {
      return this.voice1Modifier;
    }

    public ModifierField getVoice2Modifier() {
      return this.voice2Modifier;
    }

    public void debug() {
      System.out.println("voice1 :" + this.voice1 + " " + shifterVoiceRange.getUnit());
      System.out.println("voice2 :" + this.voice2 + " " + shifterVoiceRange.getUnit());
      System.out.println("pan1 :" + this.pan1 + " " + shifterPanRange.getUnit());
      System.out.println("pan2 :" + this.pan2 + " " + shifterPanRange.getUnit());
      System.out.println("delay1 :" + this.delay1 + " " + shifterDelayRange.getUnit());
      System.out.println("delay2 :" + this.delay2 + " " + shifterDelayRange.getUnit());
      System.out.println("feedback1 :" + this.feedback1 + " " + shifterFeedbackRange.getUnit());
      System.out.println("feedback2 :" + this.feedback2 + " " + shifterFeedbackRange.getUnit());
      System.out.println("level1 :" + this.level1 + " " + shifterLevelRange.getUnit());
      System.out.println("level2 :" + this.level2 + " " + shifterLevelRange.getUnit());
    }
  }

  public static class GmWhammyAlgorithm extends GmEffectAlgorithm {
    public static final IntRange whammyMixRange = new IntRange(0, 100) {
      public String getUnit() {
        return "%";
      }
    };
    int whammyPitch;
    GmPitchEffect.PITCH_DIRECTION direction;
    GmPitchEffect.OCTAVE_RANGE octRange;
    final ModifierField pitchModifier;

    GmWhammyAlgorithm() {
      this.whammyPitch = Integer.parseInt(whammyMixRange.getMinValue());
      this.direction = GmPitchEffect.PITCH_DIRECTION.DOWN;
      this.octRange = GmPitchEffect.OCTAVE_RANGE.ONE_OCT;
      this.type = PITCH_ALGO_TYPE.WHAMMY;
      this.pitchModifier = new ModifierField(48);
      this.outLvlModifier = new ModifierField(52);
    }

    public void initialize(byte[] pData) {
      this.whammyPitch = GmajorPatch.readInt(pData, 328, 4);
      this.direction = GmPitchEffect.PITCH_DIRECTION.getDirection(GmajorPatch.readInt(pData, 332, 4));
      this.octRange = GmPitchEffect.OCTAVE_RANGE.getOctRange(GmajorPatch.readInt(pData, 336, 4));

      try {
        this.pitchModifier.initialize(pData, 48);
        this.outLvlModifier.initialize(pData, 52);
      } catch (IOException var3) {
        var3.printStackTrace();
      }

    }

    public void writeToSysex(byte[] pData) {
      GmajorPatch.writeInt(pData, this.whammyPitch, 328, 4);
      GmajorPatch.writeInt(pData, this.direction.value, 332, 4);
      GmajorPatch.writeInt(pData, this.octRange.value, 336, 4);
      this.pitchModifier.writeToSysEx(pData);
      this.outLvlModifier.writeToSysEx(pData);
    }

    public GmPitchEffect.PITCH_DIRECTION getDirection() {
      return this.direction;
    }

    public void setDirection(GmPitchEffect.PITCH_DIRECTION dir) {
      this.direction = dir;
    }

    public GmPitchEffect.OCTAVE_RANGE getOctRange() {
      return this.octRange;
    }

    public void setOctRange(GmPitchEffect.OCTAVE_RANGE rang) {
      this.octRange = rang;
    }

    public int getWhammyPitch() {
      return this.whammyPitch;
    }

    public void setWhammyPitch(String wmMix) {
      this.whammyPitch = Integer.parseInt(wmMix);
    }

    public ModifierField getPitchModifier() {
      return this.pitchModifier;
    }

    public void debug() {
      System.out.println("whammyMix :" + this.whammyPitch + " " + whammyMixRange.getUnit());
      System.out.println("direction :" + this.direction);
      System.out.println("octRange :" + this.octRange + "-OCT");
    }
  }

  public enum OCTAVE_RANGE {
    ONE_OCT("1-OCT", 1),
    TWO_OCT("2-OCT", 2);

    final String name;
    final int value;

    OCTAVE_RANGE(String nm, int vl) {
      this.name = nm;
      this.value = vl;
    }

    static GmPitchEffect.OCTAVE_RANGE getOctRange(int octave) {
      switch(octave) {
        case 0:
        case 1:
          return ONE_OCT;
        case 2:
          return TWO_OCT;
        default:
          System.out.println("Undefined value for Octave Range:" + octave);
          return null;
      }
    }
  }

  public enum PITCH_DIRECTION {
    DOWN("DOWN", 0),
    UP("UP", 1);

    final String name;
    final int value;

    PITCH_DIRECTION(String nm, int vl) {
      this.name = nm;
      this.value = vl;
    }

    static GmPitchEffect.PITCH_DIRECTION getDirection(int dir) {
      switch(dir) {
        case 0:
          return DOWN;
        case 1:
          return UP;
        default:
          return null;
      }
    }
  }
}
