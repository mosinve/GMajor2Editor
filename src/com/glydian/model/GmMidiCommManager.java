//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import com.glydian.model.GmMidiManager.MidiInitException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.SysexMessage;

public class GmMidiCommManager {
  private static GmMidiCommManager self;
  private boolean enableRealTimeComm = false;
  private GmajorPatch editingPatch;
  private final GmMidiCommManager.MidiCommQueue theMidiCommQueue = new GmMidiCommManager.MidiCommQueue();
  private GmMidiManager midiDeviceHandler;

  public static GmMidiCommManager getInstance() {
    if (self == null) {
      self = new GmMidiCommManager();
    }

    return self;
  }

  private GmMidiCommManager() {
  }

  public void setDeviceHandler(GmMidiManager handler) {
    this.midiDeviceHandler = handler;
  }

  public void setRealTimeCommunication(boolean enable) {
    this.enableRealTimeComm = enable;
  }

  public void setEditingPatch(GmajorPatch patch) {
    this.editingPatch = patch;
  }

  public void patchUpdated() {
    if (this.enableRealTimeComm && this.editingPatch != null && this.editingPatch.hasChanged()) {
      this.theMidiCommQueue.addPatchToTransmit(this.editingPatch);
      this.editingPatch.clearChangedFlag();
    }

  }

  class MidiCommQueue {
    private GmajorPatch patchInTheQue = null;
    private Thread commThread = null;
    private boolean midiPortIsOpen = false;

    MidiCommQueue() {
      this.commThread = new Thread(() -> {
        while(true) {
          if (MidiCommQueue.this.patchInTheQue != null) {
            GmajorPatch patchToTransmit = MidiCommQueue.this.patchInTheQue;
            MidiCommQueue.this.patchInTheQue = null;

            try {
              patchToTransmit.debug();
              SysexMessage sysex = patchToTransmit.createSysex(true);
              MidiCommQueue.this.openMidiPorts();
              GmMidiCommManager.this.midiDeviceHandler.sendPatch(sysex);
              MidiCommQueue.this.closeMidiPort();
            } catch (InvalidMidiDataException | MidiInitException | MidiUnavailableException e) {
              e.printStackTrace();
            }
          } else {
            MidiCommQueue.this.closeMidiPort();
            Thread.yield();

            try {
              Thread.sleep(550L);
            } catch (InterruptedException var6) {
              System.out.println("Thread " + var6.getMessage());
            }
          }
        }
      });
      this.commThread.start();
    }

    void addPatchToTransmit(GmajorPatch patch) {
      this.patchInTheQue = patch;
      this.commThread.interrupt();
    }

    private void openMidiPorts() throws MidiInitException, MidiUnavailableException {
      if (!this.midiPortIsOpen) {
        GmMidiCommManager.this.midiDeviceHandler.initializeMidiPorts(GLydianParameters.getInstance().getMidiInputDevice(), GLydianParameters.getInstance().getMidiOutputDevice());
        this.midiPortIsOpen = true;
      }

    }

    private void closeMidiPort() {
      if (this.midiPortIsOpen) {
        GmMidiCommManager.this.midiDeviceHandler.closeDevices();
        this.midiPortIsOpen = false;
      }

    }
  }
}
