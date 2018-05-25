//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.gui;

import com.glydian.gui.EffectComposite.CompressorComposite;
import com.glydian.gui.EffectComposite.EqualizerComposite;
import com.glydian.gui.EffectComposite.NoiseGateComposite;
import com.glydian.gui.GLydianSwing.MainDisplay;
import com.glydian.model.GLydianParameters;
import java.awt.Dimension;

public class GLydianRackArranger {
  private final int u1Width;
  private final int u1Height;
  private final int mainDisplayWidth;
  private final int mainDisplayHeight;
  private final int eqWidth;
  private final int eqHeight;
  private final int smallUnitWidth;
  private final int smallUnitHeight;
  private static GLydianRackArranger self;

  public static GLydianRackArranger getInstance() {
    if (self == null) {
      self = new GLydianRackArranger();
    }

    return self;
  }

  private GLydianRackArranger() {
    boolean layOutWideScreenFormat = GLydianParameters.getInstance().getProperty("WIDE_SCREEN_FORMAT").equalsIgnoreCase("true");
    this.u1Width = GLydianParameters.getInstance().getNumerticPropertyValue("RACK_U1_WIDTH");
    this.u1Height = GLydianParameters.getInstance().getNumerticPropertyValue("RACK_U1_HEIGHT");
    this.mainDisplayWidth = GLydianParameters.getInstance().getNumerticPropertyValue("RACK_MAIN_DISPLAY_WIDTH");
    if (layOutWideScreenFormat) {
      this.mainDisplayHeight = GLydianParameters.getInstance().getNumerticPropertyValue("RACK_MAIN_DISPLAY_HEIGHT_WIDE_FORMAT");
      this.eqWidth = this.mainDisplayWidth;
      this.eqHeight = GLydianParameters.getInstance().getNumerticPropertyValue("RACK_EQPANEL_HEIGHT_WIDE_FORMAT");
      this.smallUnitWidth = this.mainDisplayWidth;
      this.smallUnitHeight = (this.u1Height * 5 - (this.eqHeight + this.mainDisplayHeight)) / 2;
    } else {
      this.mainDisplayHeight = GLydianParameters.getInstance().getNumerticPropertyValue("RACK_MAIN_DISPLAY_HEIGHT");
      this.eqWidth = GLydianParameters.getInstance().getNumerticPropertyValue("RACK_EQPANEL_WIDTH");
      this.eqHeight = this.mainDisplayHeight;
      this.smallUnitWidth = this.u1Width - (this.mainDisplayWidth + this.eqWidth);
      this.smallUnitHeight = this.mainDisplayHeight / 2;
    }

  }

  public void setEffectUnitDimension(EffectComposite effectUnit) {
    Dimension effectDim = this.getEffectUnitSize(effectUnit);
    effectUnit.setMaximumSize(effectDim);
    effectUnit.setPreferredSize(effectDim);
  }

  private Dimension getEffectUnitSize(EffectComposite effectUnit) {
    Dimension effectDim = null;
    if (effectUnit instanceof MainDisplay) {
      effectDim = new Dimension(this.mainDisplayWidth, this.mainDisplayHeight);
    } else if (effectUnit instanceof EqualizerComposite) {
      effectDim = new Dimension(this.eqWidth, this.eqHeight);
    } else if (!(effectUnit instanceof CompressorComposite) && !(effectUnit instanceof NoiseGateComposite)) {
      if (!(effectUnit instanceof MultiAlgorithmComposite)) {
        throw new RuntimeException("Unknown Effect Unit type" + effectUnit.effectName);
      }

      effectDim = new Dimension(this.u1Width, this.u1Height);
    } else {
      effectDim = new Dimension(this.smallUnitWidth, this.smallUnitHeight);
    }

    return effectDim;
  }
}
