//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import com.glydian.model.effect.GmEffect;
import java.util.Collection;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Observable;
//import java.util.Observer;
import java.util.Vector;

public class PatchLibrary extends Observable {
  private final HashMap<String, GmajorPatch> patchEntryMap;
  private static PatchLibrary self;

  public static PatchLibrary getInstance() {
    if (self == null) {
      self = new PatchLibrary();
    }

    return self;
  }

  private PatchLibrary() {
    this.patchEntryMap = new HashMap<>();
  }

  public void add(GmajorPatch patch) {
    this.patchEntryMap.put(patch.patchName, patch);
    this.setChanged();
  }

  public void remove(String patchName) {
    this.patchEntryMap.remove(patchName);
    this.setChanged();
  }

  public void clear() {
    this.patchEntryMap.clear();
    this.setChanged();
  }

  public Collection<GmajorPatch> getPatchList() {
    return this.patchEntryMap.values();
  }

  public List<GmEffect> getCopyableEffects(GmEffect editingEffect) {
    List<GmEffect> copyableEffects = new Vector<>();

    for (GmajorPatch element : this.patchEntryMap.values()) {
      GmEffect copyableEffect = element.getCopyableEffect(editingEffect);
      if (copyableEffect != null) {
        copyableEffects.add(copyableEffect);
      }
    }

    return copyableEffects;
  }
}
