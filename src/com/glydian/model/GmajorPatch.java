//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import com.glydian.model.effect.GmCompressorEffect;
import com.glydian.model.effect.GmDelayEffect;
import com.glydian.model.effect.GmEffect;
import com.glydian.model.effect.GmEqualizer;
import com.glydian.model.effect.GmFilterModEffect;
import com.glydian.model.effect.GmModulationEffect;
import com.glydian.model.effect.GmNoiseGate;
import com.glydian.model.effect.GmPitchEffect;
import com.glydian.model.effect.GmReverbEffect;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

public class GmajorPatch implements Modifiable {
  public static final IntRange tapTempoRange = new IntRange(100, 3000) {
    public String getUnit() {
      return "ms";
    }
  };
  public static final IntRange effectOutLevelRange = new IntRange(-100, 0) {
    public String getUnit() {
      return "dB";
    }
  };
  private SysexMessage patch;
  String patchName;
  private int patchNumber;
  private GmajorPatch.PATCH_BANK effectBank;
  private int routing;
  private int relayOne;
  private int relayTwo;
  private int outLevel;
  private int tapTempo;
  private final GmCompressorEffect compressorEft;
  private GmNoiseGate noiseGateEft;
  private GmEqualizer eqEft;
  private final GmFilterModEffect filterEft;
  private final GmPitchEffect pitchEft;
  private final GmModulationEffect modulationEft;
  private final GmDelayEffect delayEft;
  private final GmReverbEffect reverbEft;
  private int checkSum;
  private boolean mHasChanged;
  private static final SysexMessage defaultSysex = createEmptyGMajorSysEx();

  public void clearChangedFlag() {
    this.mHasChanged = false;
    this.compressorEft.clearChangedFlag();
    this.noiseGateEft.clearChangedFlag();
    this.eqEft.clearChangedFlag();
    this.filterEft.clearChangedFlag();
    this.pitchEft.clearChangedFlag();
    this.modulationEft.clearChangedFlag();
    this.delayEft.clearChangedFlag();
    this.reverbEft.clearChangedFlag();
  }

  public boolean hasChanged() {
    return this.mHasChanged || this.compressorEft.hasChanged() || this.noiseGateEft.hasChanged() || this.eqEft.hasChanged() || this.filterEft.hasChanged() || this.pitchEft.hasChanged() || this.modulationEft.hasChanged() || this.delayEft.hasChanged() || this.reverbEft.hasChanged();
  }

  public void markChanged() {
    this.mHasChanged = true;
  }

  public GmajorPatch() {
    this.patch = defaultSysex;
    this.effectBank = GmajorPatch.PATCH_BANK.FACTORY;
    this.patchNumber = 0;
    this.patchName = "G-Lydian";
    this.routing = 0;
    this.outLevel = -6;
    this.relayOne = 1;
    this.relayTwo = 1;
    this.tapTempo = 335;
    this.noiseGateEft = new GmNoiseGate(this);
    this.eqEft = new GmEqualizer(this);
    this.compressorEft = new GmCompressorEffect(this);
    this.filterEft = new GmFilterModEffect(this);
    this.pitchEft = new GmPitchEffect(this);
    this.modulationEft = new GmModulationEffect(this);
    this.delayEft = new GmDelayEffect(this);
    this.reverbEft = new GmReverbEffect(this);
  }

  public GmajorPatch(SysexMessage pPatch) throws GmajorPatch.InvalidGmajorSysexException, IOException {
    this();
    byte[] patchData = pPatch.getData();
    if (patchData.length != 614) {
      throw new GmajorPatch.InvalidGmajorSysexException();
    } else {
      this.patch = pPatch;
      this.initialize(patchData);
    }
  }

  private static SysexMessage createEmptyGMajorSysEx() {
    byte[] data = new byte[615];
    SysexMessage emptyGMajorSysEx = new SysexMessage();

    try {
      data[0] = -16;
      data[1] = 0;
      data[2] = 32;
      data[3] = 31;
      data[4] = 0;
      data[5] = 102;
      data[6] = 32;
      data[614] = -9;
      emptyGMajorSysEx.setMessage(data, data.length);
    } catch (InvalidMidiDataException var3) {
      var3.printStackTrace();
    }

    return emptyGMajorSysEx;
  }

  public SysexMessage createSysex() throws InvalidMidiDataException {
    return this.createSysex(false);
  }

  public SysexMessage createSysex(boolean sendAsExternalPatch) throws InvalidMidiDataException {
    System.out.println("patch.getData().length: " + this.patch.getData().length);
    byte[] srcData = this.patch.getData();
    byte[] pData = new byte[srcData.length];

    System.arraycopy(srcData, 0, pData, 0, srcData.length);

    if (this.effectBank != GmajorPatch.PATCH_BANK.FACTORY && !sendAsExternalPatch) {
      pData[6] = (byte)(this.patchNumber < 28 ? this.patchNumber + 100 : this.patchNumber - 28);
      pData[7] = (byte)(this.patchNumber >= 28 ? 1 : 0);
    } else {
      pData[6] = 0;
      pData[7] = 0;
    }

    char[] nameBytes = this.patchName.trim().toCharArray();

    for(int i = 0; i < 20; ++i) {
      if (i < nameBytes.length) {
        pData[8 + i] = (byte)nameBytes[i];
      } else {
        pData[8 + i] = 32;
      }
    }

    BitmaskField firstBitField = new BitmaskField(28);
    firstBitField.setBitsForInt(this.routing, 0, 2);
    firstBitField.setBitsForInt(100 + this.outLevel, 2, 7);
    firstBitField.setBitsForInt(this.relayOne, 9, 1);
    firstBitField.setBitsForInt(this.relayTwo, 10, 1);
    firstBitField.writeBitsToByteArray(pData, 156);
    writeInt(pData, this.tapTempo, 160, 4);
    this.noiseGateEft.writeToSysex(pData);
    this.eqEft.writeToSysex(pData);
    this.compressorEft.writeToSysex(pData);
    this.filterEft.writeToSysex(pData);
    this.pitchEft.writeToSysex(pData);
    this.pitchEft.writeToSysex(pData);
    this.modulationEft.writeToSysex(pData);
    this.delayEft.writeToSysex(pData);
    this.reverbEft.writeToSysex(pData);
    int sumOfBytes = 0;
    pData[4] = 102;
    for(int i = 28; i < 612; ++i) {
      sumOfBytes += pData[i];
    }

    System.out.println("checkSum    \t:" + this.checkSum);
    System.out.println("sumOfBytes:" + sumOfBytes);
    System.out.println("Calculated checkSum:" + (sumOfBytes & 127));
    this.checkSum = sumOfBytes & 127;
    pData[612] = (byte)this.checkSum;

    SysexMessage sysexMessage = new SysexMessage();
    sysexMessage.setMessage(240, pData, pData.length);
    return sysexMessage;
  }

  private void initialize(byte[] patchData) throws GmajorPatch.InvalidGmajorSysexException, IOException {
    System.out.println("Msg length :" + patchData.length);
    this.checkSum = patchData[612];
    System.out.println("checkSum    \t:" + this.checkSum);
    int sumOfBytes = 0;

    for(int i = 28; i < 612; ++i) {
      sumOfBytes += patchData[i];
    }

    System.out.println("sumOfBytes:" + sumOfBytes);
    System.out.println("Calculated checkSum:" + (sumOfBytes & 127));
    if (this.checkSum != (sumOfBytes & 127)) {
      System.out.println("Invalid Sysex, Checksum error ! < " + this.checkSum + " <>" + (sumOfBytes & 127) + ">");
    }

    ByteArrayInputStream patchInStream = new ByteArrayInputStream(patchData, 0, 164);
    DataInputStream patchDataStream = new DataInputStream(patchInStream);
    this.readData(patchDataStream);
    int msgType = patchDataStream.readByte();
    if (msgType != 32) {
      throw new GmajorPatch.InvalidGmajorSysexException();
    } else {
      short lsb = patchDataStream.readByte();
      short msb = patchDataStream.readByte();
      if (msb != 1 && lsb <= 100) {
        this.effectBank = GmajorPatch.PATCH_BANK.FACTORY;
      } else {
        this.effectBank = GmajorPatch.PATCH_BANK.USER;
      }

      if (this.effectBank == GmajorPatch.PATCH_BANK.USER && lsb > 100) {
        this.patchNumber = lsb - 100;
      } else if (this.effectBank == GmajorPatch.PATCH_BANK.USER && lsb <= 100) {
        this.patchNumber = lsb + 28;
      } else {
        this.patchNumber = lsb;
      }

      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < 20; ++i) {
        sb.append((char)patchDataStream.readByte());
      }

      this.patchName = sb.toString();
      BitmaskField firstBitField = new BitmaskField(28);
      firstBitField.readBitsFromByteArray(patchData, 156);
      this.routing = firstBitField.getIntFromBits(0, 2);
      this.outLevel = firstBitField.getIntFromBits(2, 7) - 100;
      this.relayOne = firstBitField.getIntFromBits(9, 1);
      this.relayTwo = firstBitField.getIntFromBits(10, 1);
      this.tapTempo = readInt(patchData, 160, 4);
      this.noiseGateEft.initialize(patchData);
      this.eqEft.initialize(patchData);
      this.compressorEft.initialize(patchData);
      this.filterEft.initialize(patchData);
      this.pitchEft.initialize(patchData);
      this.modulationEft.initialize(patchData);
      this.delayEft.initialize(patchData);
      this.reverbEft.initialize(patchData);
    }
  }

  private void readData(DataInputStream pDataStream) throws IOException {
    for(int i = 0; i < 5; ++i) {
      pDataStream.readByte();
    }

  }

  public void debug() {
    System.out.println();
    System.out.println("effectBank  \t:" + this.effectBank);
    System.out.println("patchNumber \t:" + this.patchNumber);
    System.out.println("patchName   \t:" + this.patchName);
    System.out.println("routing     \t:" + this.routing);
    System.out.println("outLevel    \t:" + this.outLevel);
    System.out.println("relayOne    \t:" + this.relayOne);
    System.out.println("relayTwo    \t:" + this.relayTwo);
    System.out.println("tapTempo    \t:" + this.tapTempo);
    this.noiseGateEft.debug();
    this.eqEft.debug();
    this.compressorEft.debug();
    this.filterEft.debug();
    this.pitchEft.debug();
    this.modulationEft.debug();
    this.delayEft.debug();
    this.reverbEft.debug();
    System.out.println("checkSum    \t:" + this.checkSum);
  }

  public static int readInt(byte[] pData, int pOffset, int pSize) {
    int intValue = 0;
    boolean negative = pData[pOffset + pSize - 1] == 7;

    for(int i = 0; i < pSize - 1; ++i) {
      int byteVal = pData[pOffset + i];
      if (negative) {
        if (byteVal != 127) {
          byteVal -= 128;
        } else {
          byteVal = 0;
        }
      }

      intValue = (int)((double)intValue + (double)byteVal * Math.pow(2.0D, (double)(i * 7)));
    }

    return intValue;
  }

  public static void writeInt(byte[] pData, int pValue, int pOffset, int pSize) {
    boolean negative = pValue < 0;
    int absValue = Math.abs(pValue);
    if (!negative) {
      pData[pOffset + pSize - 1] = 0;
    } else {
      pData[pOffset + pSize - 1] = 7;
    }

    for(int i = 0; i < pSize - 1; ++i) {
      int divisor = (int)Math.rint(Math.pow(2.0D, (double)(i * 7)));
      if (absValue >= divisor) {
        byte byteVal = (byte)(127 & (negative ? 128 - absValue / divisor : absValue / divisor));
        pData[pOffset + i] = byteVal;
      } else if (!negative) {
        pData[pOffset + i] = 0;
      } else {
        pData[pOffset + i] = 127;
      }
    }

  }

  public SysexMessage getPatch() {
    return this.patch;
  }

  public GmajorPatch.PATCH_BANK getEffectBank() {
    return this.effectBank;
  }

  public void setEffectBank(GmajorPatch.PATCH_BANK effectBank) {
    this.effectBank = effectBank;
  }

  public String getPatchName() {
    return this.patchName;
  }

  public void setPatchName(String patchName) {
    this.patchName = patchName;
  }

  public int getPatchNumber() {
    return this.patchNumber;
  }

  public void setPatchNumber(int patchNumber) {
    this.patchNumber = patchNumber;
  }

  public int getRouting() {
    return this.routing;
  }

  public void setRouting(int routing) {
    this.routing = routing;
  }

  public boolean isRelayOneOpen() {
    return this.relayOne == 0;
  }

  public void setRelayOne(boolean open) {
    this.relayOne = open ? 0 : 1;
  }

  public boolean isRelayTwoOpen() {
    return this.relayTwo == 0;
  }

  public void setRelayTwo(boolean open) {
    this.relayTwo = open ? 0 : 1;
  }

  public int getOutLevel() {
    return this.outLevel;
  }

  public void setOutLevel(int outLevel) {
    this.outLevel = outLevel;
  }

  public int getTapTempo() {
    return this.tapTempo;
  }

  public void setTapTempo(int tapTempo) {
    this.tapTempo = tapTempo;
  }

  public GmCompressorEffect getCompressorEft() {
    return this.compressorEft;
  }

  public GmFilterModEffect getFilterEft() {
    return this.filterEft;
  }

  public GmNoiseGate getNoiseGateEft() {
    return this.noiseGateEft;
  }

  public void setNoiseGateEft(GmNoiseGate noiseGateEft) {
    this.noiseGateEft = noiseGateEft;
  }

  public GmEqualizer getEqualizerEft() {
    return this.eqEft;
  }

  public void setEqualizerEft(GmEqualizer eqEft) {
    this.eqEft = eqEft;
  }

  public GmPitchEffect getPitchEft() {
    return this.pitchEft;
  }

  public GmModulationEffect getModulatorEft() {
    return this.modulationEft;
  }

  public GmDelayEffect getDelayEffect() {
    return this.delayEft;
  }

  public GmReverbEffect getReverbEffect() {
    return this.reverbEft;
  }

  GmEffect getCopyableEffect(GmEffect referenceEffect) {
    if (referenceEffect instanceof GmNoiseGate) {
      if (this.noiseGateEft.isEffectOn()) {
        return this.noiseGateEft;
      }
    } else if (referenceEffect instanceof GmEqualizer) {
      if (this.eqEft.isEffectOn()) {
        return this.eqEft;
      }
    } else if (referenceEffect instanceof GmCompressorEffect) {
      if (this.compressorEft.isEffectOn()) {
        return this.compressorEft;
      }
    } else if (referenceEffect instanceof GmFilterModEffect) {
      if (this.filterEft.isEffectOn()) {
        return this.filterEft;
      }
    } else if (referenceEffect instanceof GmPitchEffect) {
      if (this.pitchEft.isEffectOn()) {
        return this.pitchEft;
      }
    } else if (referenceEffect instanceof GmModulationEffect) {
      if (this.modulationEft.isEffectOn()) {
        return this.modulationEft;
      }
    } else if (referenceEffect instanceof GmDelayEffect) {
      if (this.delayEft.isEffectOn()) {
        return this.delayEft;
      }
    } else if (referenceEffect instanceof GmReverbEffect && this.reverbEft.isEffectOn()) {
      return this.reverbEft;
    }

    return null;
  }

  public class InvalidGmajorSysexException extends Exception {
    private static final long serialVersionUID = 1L;

    InvalidGmajorSysexException() {
      super("Invalid Sysex");
    }
  }

  public enum PATCH_BANK {
    FACTORY,
    USER;

    PATCH_BANK() {
    }
  }
}
