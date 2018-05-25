//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import com.glydian.model.GmajorPatch.PATCH_BANK;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiDevice.Info;

public class GmMidiManager {
  private static int instanceCount = 0;
  private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
  private MidiDevice mOutputDevice = null;
  private MidiDevice mInputDevice = null;
  private Receiver mReceiver = null;
  private Transmitter mTransmitter = null;
  private static final Integer mutex = 0;

  public GmMidiManager() {
    ++instanceCount;
    System.out.println("GmMidiManager instanceCount" + instanceCount);
  }

  public void initializeMidiPorts(String pDeviceName) throws GmMidiManager.MidiInitException, MidiUnavailableException {
    this.initializeMidiPorts(pDeviceName, pDeviceName);
  }

  public synchronized void initializeMidiPorts(String inPutDeviceName, String outPutDeviceName) throws GmMidiManager.MidiInitException, MidiUnavailableException {
    this.mOutputDevice = null;
    this.mInputDevice = null;
    this.mReceiver = null;
    this.mTransmitter = null;
    if (inPutDeviceName != null && outPutDeviceName != null) {
      Info infoOut = getMidiDeviceInfo(outPutDeviceName, true);
      if (infoOut == null) {
        throw new GmMidiManager.MidiInitException("No output device info found for name " + outPutDeviceName);
      }

      this.mOutputDevice = MidiSystem.getMidiDevice(infoOut);
      Info infoIn = getMidiDeviceInfo(inPutDeviceName, false);
      if (infoIn == null) {
        throw new GmMidiManager.MidiInitException("No input device info found for name " + inPutDeviceName);
      }

      this.mInputDevice = MidiSystem.getMidiDevice(infoIn);
    }

    this.initializeMidiPorts();
  }

  private synchronized void initializeMidiPorts() throws MidiUnavailableException, GmMidiManager.MidiInitException {
    if (this.mInputDevice != null && this.mOutputDevice != null) {
      this.mOutputDevice.open();
      if (this.mOutputDevice == null) {
        throw new GmMidiManager.MidiInitException("wasn't able to open MidiDevice" + this.mOutputDevice.getDeviceInfo().getName());
      }

      this.mReceiver = this.mOutputDevice.getReceiver();
      this.mInputDevice.open();
      if (this.mInputDevice == null) {
        throw new GmMidiManager.MidiInitException("Wasn't able to open MidiDevice " + this.mInputDevice.getDeviceInfo().getName());
      }

      this.mTransmitter = this.mInputDevice.getTransmitter();
    } else {
      this.mReceiver = MidiSystem.getReceiver();
      this.mTransmitter = MidiSystem.getTransmitter();
    }

    if (this.mReceiver == null || this.mTransmitter == null) {
      throw new GmMidiManager.MidiInitException("Wasn't able to retrieve Receiver and Transmitter MidiDevices");
    }
  }

  public synchronized void closeDevices() {
    if (this.mReceiver != null) {
      this.mReceiver.close();
    }

    this.mReceiver = null;
    if (this.mTransmitter != null) {
      this.mTransmitter.close();
    }

    this.mTransmitter = null;
    if (this.mOutputDevice != null) {
      this.mOutputDevice.close();
    }

    if (this.mInputDevice != null) {
      this.mInputDevice.close();
    }

  }

  private synchronized SysexMessage preparePatchRequestMsg(PATCH_BANK pPatchBank, int patchNumber) throws InvalidMidiDataException {
    SysexMessage sysexMessage = new SysexMessage();
    int lsbNr;
    if (pPatchBank == PATCH_BANK.USER) {
      lsbNr = patchNumber < 28 ? patchNumber + 100 : 100 + patchNumber - 128;
    } else {
      lsbNr = patchNumber;
    }

    String lSb = intToHex(lsbNr);
    String mSb = pPatchBank == PATCH_BANK.USER && patchNumber > 27 ? "01" : "00";
    String presetRequestMessageHead = "F000201f006645";
    String presetRequestMessage = presetRequestMessageHead + lSb + mSb + "F7";
    int lengthInBytes = presetRequestMessage.length() / 2;
    byte[] abMessage = new byte[lengthInBytes];

    for(int i = 0; i < lengthInBytes; ++i) {
      abMessage[i] = (byte)Integer.parseInt(presetRequestMessage.substring(i * 2, i * 2 + 2), 16);
    }

    sysexMessage.setMessage(abMessage, abMessage.length);
    return sysexMessage;
  }

  private static String intToHex(int i) {
    return "" + hexDigits[(i & 240) >> 4] + hexDigits[i & 15];
  }

  public SysexMessage getCurrentEditingPatchFromGmajor() throws InvalidMidiDataException {
    return this.getPatchFromGmajor(PATCH_BANK.FACTORY, 0);
  }

  public void sendTapTempoMsg() {
    ShortMessage tempoMsg = new ShortMessage();

    try {
      tempoMsg.setMessage(176, 0, 80, 100);
      this.mReceiver.send(tempoMsg, -1L);
    } catch (InvalidMidiDataException var3) {
      var3.printStackTrace();
    }

  }

  public SysexMessage sendPatch(SysexMessage sysexMsg) {
    GmMidiManager.PatchReceiver gmPatchReceiver = new GmMidiManager.PatchReceiver();
    synchronized(mutex) {
      this.mTransmitter.setReceiver(gmPatchReceiver);
      this.mReceiver.send(sysexMsg, -1L);

      try {
        Thread.sleep(450L);
      } catch (InterruptedException var5) {
        System.out.println("Done");
      }
    }

    System.out.println("Received \n" + gmPatchReceiver.getPatchString());
    return gmPatchReceiver.getPatch();
  }

  public SysexMessage getPatchFromGmajor(PATCH_BANK bank, int patchNr) throws InvalidMidiDataException {
    System.out.println(">>>> GET PATCH: " + bank + " NUMBER: " + patchNr);
    SysexMessage patchRequestMsg = this.preparePatchRequestMsg(bank, patchNr);
    return this.sendPatch(patchRequestMsg);
  }

  static void printUsageAndExit() {
    System.out.println("GmMidiManager: usage:");
    System.out.println("  java GmMidiManager [<device name>] <hexstring>");
    System.out.println("  <device name>   output to named device. If not given, Java Sound's default device is used.");
    System.exit(1);
  }

  private static Info getMidiDeviceInfo(String strDeviceName, boolean forOutput) {
    Info[] aInfos = MidiSystem.getMidiDeviceInfo();

    for (Info aInfo : aInfos) {
      if (aInfo.getName().equals(strDeviceName)) {
        try {
          MidiDevice device = MidiSystem.getMidiDevice(aInfo);
          boolean allowsInput = device.getMaxTransmitters() != 0;
          boolean allowsOutput = device.getMaxReceivers() != 0;
          if (allowsOutput && forOutput || allowsInput && !forOutput) {
            return aInfo;
          }
        } catch (MidiUnavailableException var7) {
          var7.printStackTrace();
        }
      }
    }

    return null;
  }

  public static Vector<String> getMidiDeviceNameList(boolean bForInput, boolean bForOutput) {
    Vector<String> deviceList = new Vector<>();
    System.out.println("Available MIDI Devices:");
    Info[] aInfos = MidiSystem.getMidiDeviceInfo();

    for(int i = 0; i < aInfos.length; ++i) {
      try {
        MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
        boolean bAllowsInput = device.getMaxTransmitters() != 0;
        boolean bAllowsOutput = device.getMaxReceivers() != 0;
        if (bAllowsInput && bForInput) {
          deviceList.add(aInfos[i].getName());
        }

        if (bAllowsOutput && bForOutput) {
          deviceList.add(aInfos[i].getName());
        }

        System.out.println(i + "  " + (bAllowsInput ? "IN " : "   ") + (bAllowsOutput ? "OUT " : "    ") + aInfos[i].getName() + ", ");
      } catch (MidiUnavailableException var8) {
        var8.printStackTrace();
      }
    }

    if (aInfos.length == 0) {
      System.out.println("[No devices available]");
    }

    return deviceList;
  }

  public static class MidiInitException extends Exception {
    private static final long serialVersionUID = 3256725061221167672L;

    MidiInitException(String msg) {
      super(msg);
    }
  }

  static class PatchReceiver implements Receiver {
    private SysexMessage mPatch;
    private String mPatchHexString;

    SysexMessage getPatch() {
      return this.mPatch;
    }

    String getPatchString() {
      return this.mPatchHexString;
    }

    PatchReceiver() {
    }

    public void close() {
      System.out.println("Receiver.close()");
    }

    public void send(MidiMessage message, long time) {
      if (message instanceof SysexMessage) {
        this.mPatch = (SysexMessage)message;
        this.mPatchHexString = this.decodeMessage(this.mPatch);
      } else if (message instanceof ShortMessage) {
        ShortMessage sm = (ShortMessage)message;
        System.out.println("Rx: Short MidiMessage Command- " + sm.getCommand() + " status- " + sm.getStatus() + " D1- " + sm.getData1() + " D2-" + sm.getData2());
      } else {
        System.out.println("unknown message type <" + this.decodeMessage(message) + ">");
      }

    }

    String decodeMessage(MidiMessage message) {
      String strMessage;
      byte[] abData;
      if (message.getStatus() == 240) {
        abData = ((SysexMessage)message).getData();
        strMessage = "F0" + getHexString(abData);
      } else if (message.getStatus() == 247) {
        abData = ((SysexMessage)message).getData();
        strMessage = "F7" + getHexString(abData);
      } else {
        abData = message.getMessage();
        strMessage = getHexString(abData);
      }

      return strMessage;
    }

    static String getHexString(byte[] aByte) {
      StringBuffer sbuf = new StringBuffer(aByte.length * 3 + 2);

      for (byte anAByte : aByte) {
        sbuf.append(GmMidiManager.hexDigits[(anAByte & 240) >> 4]);
        sbuf.append(GmMidiManager.hexDigits[anAByte & 15]);
      }

      return new String(sbuf);
    }
  }
}
