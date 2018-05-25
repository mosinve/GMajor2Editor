//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.gui;

import com.glydian.gui.controls.J2DButton;
import com.glydian.gui.controls.JDial;
import com.glydian.gui.controls.JLedButton;
import com.glydian.gui.controls.JDial.RendererStyle;
import com.glydian.model.GLydianParameters;
import com.glydian.model.GmMidiCommManager;
import com.glydian.model.PatchLibrary;
import com.glydian.model.Range;
import com.glydian.model.DiscreteRange.CompTimeRange;
import com.glydian.model.effect.GmCompressorEffect;
import com.glydian.model.effect.GmEffect;
import com.glydian.model.effect.GmEqualizer;
import com.glydian.model.effect.GmNoiseGate;
import com.glydian.model.effect.GmCompressorEffect.CompRatioRange;
import com.glydian.model.effect.GmEqualizer.EqFrequencyRange;
import com.glydian.model.effect.GmEqualizer.EqWidthRange;
import com.glydian.model.effect.GmNoiseGate.NOISE_GATE_MODE;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
//import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_ROUND;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;

public abstract class EffectComposite extends JPanel {
  GmEffect effect;
  String effectName;
  Font labelFont;
  final JPanel titlePanel;
  JLedButton onOffButton;
  private final EffectComposite.EditEffectMenu editPopupMenu;
  private final J2DButton editMenuBtn;

  final Border basicBorder = new AbstractBorder() {
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      if (EffectComposite.this.ui != null) {
        Graphics scratchGraphics = g == null ? null : g.create();

        try {
          Graphics2D g2d = (Graphics2D)g;
          g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2d.setStroke(new BasicStroke(1.5F, CAP_ROUND, JOIN_ROUND));
          g2d.setColor(Color.WHITE.brighter());
          g2d.drawRoundRect(x, y, width - 1, height - 1, 8, 8);
        } finally {
          scratchGraphics.dispose();
        }
      }

    }

    public boolean isBorderOpaque() {
      return true;
    }
  };

  public abstract void initPanel();

  public abstract void layout(EffectComposite var1);

  EffectComposite() {
    this.setLayout(new SpringLayout());
    this.setBorder(this.basicBorder);
    this.editPopupMenu = new EffectComposite.EditEffectMenu(this);
    this.onOffButton = new JLedButton(this.effectName);
    this.editMenuBtn = new J2DButton("E");
    this.titlePanel = new JPanel() {
      public void paintComponent(Graphics g) {
        if (this.isOpaque()) {
          super.paintComponent(g);
        }

        GLydianSwing.imageTitleBg.paintIcon(this, g, 0, 2);
      }
    };
    this.titlePanel.setLayout(new SpringLayout());
  }

  private GmEffect getEffect() {
    return this.effect;
  }

  void setLabelFont(Font f) {
    this.labelFont = f;
  }

  public void paintComponent(Graphics g) {
    if (this.isOpaque()) {
      super.paintComponent(g);
    }

    GLydianSwing.imageDlgBg.paintIcon(this, g, 0, 0);
    if (this.onOffButton != null && !this.onOffButton.isSelected()) {
      GLydianSwing.imageDlgBg.paintIcon(this, g, 0, 0);
    } else {
      GLydianSwing.imagePnlOnBg.paintIcon(this, g, 0, 0);
    }

  }

  void initTitleBar(String pEffectName) {
    this.effectName = pEffectName;
    this.onOffButton.setText(this.effectName);
    this.onOffButton.setFont(this.labelFont);
    this.onOffButton.setForeground(Color.white);
    this.onOffButton.setOpaque(false);
    this.onOffButton.addActionListener(actionEvent -> {
      EffectComposite.this.effect.setEffectOn(EffectComposite.this.onOffButton.isSelected());
      EffectComposite.this.effect.markChanged();
      GmMidiCommManager.getInstance().patchUpdated();
      EffectComposite.this.updateUI();
    });
    this.titlePanel.add(this.onOffButton);
    this.editMenuBtn.setFont(this.labelFont);
    this.editMenuBtn.setToolTipText("Copy effect values to this effect <br> from another patch in the Runtime Patch Library ");
    this.editMenuBtn.addActionListener(e -> EffectComposite.this.editPopupMenu.show(EffectComposite.this.editMenuBtn, 1, 1));
    this.titlePanel.add(this.editMenuBtn);
  }

  void layoutTitleBar() {
    SpringLayout titlePnlLayout = (SpringLayout)this.titlePanel.getLayout();
    Constraints thisCon = titlePnlLayout.getConstraints(this.titlePanel);
    Constraints onOffCon = titlePnlLayout.getConstraints(this.onOffButton);
    onOffCon.setX(Spring.constant(4));
    onOffCon.setY(Spring.constant(2));
    thisCon.setHeight(onOffCon.getHeight());
    titlePnlLayout.putConstraint("South", this.editMenuBtn, -2, "South", this.titlePanel);
    titlePnlLayout.putConstraint("North", this.editMenuBtn, 2, "North", this.titlePanel);
    titlePnlLayout.putConstraint("East", this.editMenuBtn, -4, "East", this.titlePanel);
  }

  void refresh(GmEffect gmEffect) {
    this.updateUI();
    this.onOffButton.setSelected(gmEffect.isEffectOn());
    this.effect = gmEffect;
  }

  JDial createDialControl(String label, Range range) {
    JDial dial = new JDial(RendererStyle.LABEL_BOTTOM_VALUE_BOTTOM);
    dial.setFont(this.labelFont);
    dial.setLabel(label);
    dial.setRange(range);
    return dial;
  }

  static JLedButton createLedButton(String label) {
    Font labelFont = new Font(GLydianParameters.getInstance().getProperty("LABEL_FONT"), GLydianParameters.getInstance().getNumerticPropertyValue("LABEL_FONT_STYLE"), GLydianParameters.getInstance().getNumerticPropertyValue("LABEL_FONT_SIZE"));
    JLedButton ledBtn = new JLedButton(label);
    ledBtn.setOpaque(false);
    ledBtn.setFont(labelFont);
    return ledBtn;
  }

  ModifierButton createModifierControl() {
    ModifierButton modifier = new ModifierButton();
    modifier.setOpaque(false);
    modifier.setFont(this.labelFont);
    return modifier;
  }

  class ButtonGroupPanel extends JPanel {
    ButtonGroupPanel(String label, Font labelFont) {
      this.setLayout(new BoxLayout(this,   BoxLayout.Y_AXIS));
      this.setBorder(BorderFactory.createTitledBorder(EffectComposite.this.basicBorder, label, 1, 1, labelFont));
      this.setOpaque(false);
    }
  }

  public static class CompressorComposite extends EffectComposite {
    private JDial dialAttack;
    private JDial dialRatio;
    private JDial dialCompThreshold;
    private JDial dialRelease;
    private JDial dialGain;

    CompressorComposite() {
    }

    public void initPanel() {
      this.initTitleBar("COMPRESSOR");
      this.add(this.titlePanel);
      this.dialAttack = this.createDialControl("ATTACK", CompTimeRange.getAttackInstance());
      this.add(this.dialAttack);
      this.dialAttack.addChangeListener(changeEvent -> {
        ((GmCompressorEffect)CompressorComposite.this.effect).setCompAttack(CompressorComposite.this.dialAttack.getDialValue().toString());
        CompressorComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialRatio = this.createDialControl("RATIO", CompRatioRange.getInstance());
      this.add(this.dialRatio);
      this.dialRatio.addChangeListener(changeEvent -> {
        ((GmCompressorEffect)CompressorComposite.this.effect).setCompRatio(CompressorComposite.this.dialRatio.getDialValue().toString());
        CompressorComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialCompThreshold = this.createDialControl("THRESHOLD", GmCompressorEffect.compThresholdRange);
      this.add(this.dialCompThreshold);
      this.dialCompThreshold.addChangeListener(changeEvent -> {
        ((GmCompressorEffect)CompressorComposite.this.effect).setCompThreshold(CompressorComposite.this.dialCompThreshold.getDialValue().toString());
        CompressorComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialRelease = this.createDialControl("RELEASE", CompTimeRange.getReleaseInstance());
      this.add(this.dialRelease);
      this.dialRelease.addChangeListener(changeEvent -> {
        ((GmCompressorEffect)CompressorComposite.this.effect).setCompRelease(CompressorComposite.this.dialRelease.getDialValue().toString());
        CompressorComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialGain = this.createDialControl("GAIN", GmCompressorEffect.compGainRange);
      this.add(this.dialGain);
      this.dialGain.addChangeListener(changeEvent -> {
        ((GmCompressorEffect)CompressorComposite.this.effect).setCompGain(CompressorComposite.this.dialGain.getDialValue().toString());
        CompressorComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
    }

    public void layout(EffectComposite pnl) {
      GLydianRackArranger.getInstance().setEffectUnitDimension(this);
      SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
      Constraints thisCon = thisLayout.getConstraints(this);
      Constraints titleCon = thisLayout.getConstraints(this.titlePanel);
      Constraints attackCon = thisLayout.getConstraints(this.dialAttack);
      Constraints ratioCon = thisLayout.getConstraints(this.dialRatio);
      Constraints threshCon = thisLayout.getConstraints(this.dialCompThreshold);
      Constraints releaseCon = thisLayout.getConstraints(this.dialRelease);
      Constraints gainCon = thisLayout.getConstraints(this.dialGain);
      int gap = 1;
      titleCon.setX(Spring.constant(gap));
      titleCon.setY(Spring.constant(gap));
      titleCon.setWidth(thisCon.getWidth());
      Spring topSpring = Spring.sum(Spring.constant(gap), titleCon.getConstraint("South"));
      attackCon.setX(Spring.constant(gap));
      attackCon.setY(topSpring);
      ratioCon.setX(Spring.sum(Spring.constant(gap), attackCon.getConstraint("East")));
      ratioCon.setY(topSpring);
      threshCon.setX(Spring.sum(Spring.constant(gap), ratioCon.getConstraint("East")));
      threshCon.setY(topSpring);
      releaseCon.setX(Spring.sum(Spring.constant(gap), threshCon.getConstraint("East")));
      releaseCon.setY(topSpring);
      gainCon.setX(Spring.sum(Spring.constant(gap), releaseCon.getConstraint("East")));
      gainCon.setY(topSpring);
      thisCon.setWidth(Spring.sum(Spring.constant(gap * 6), Spring.sum(attackCon.getWidth(), Spring.sum(ratioCon.getWidth(), Spring.sum(threshCon.getWidth(), Spring.sum(releaseCon.getWidth(), gainCon.getWidth()))))));
      thisCon.setHeight(Spring.sum(Spring.constant(gap * 3), Spring.sum(titleCon.getHeight(), Spring.max(attackCon.getHeight(), Spring.max(ratioCon.getHeight(), Spring.max(threshCon.getHeight(), Spring.max(releaseCon.getHeight(), gainCon.getHeight())))))));
      this.layoutTitleBar();
    }

    public void refresh(GmEffect pGmEffect) {
      GmCompressorEffect gmEffect = (GmCompressorEffect)pGmEffect;
      super.refresh(gmEffect);
      this.onOffButton.setSelected(gmEffect.isEffectOn());
      this.dialCompThreshold.setDialValue(String.valueOf(gmEffect.getCompThreshold()));
      this.dialRatio.setDialValue(gmEffect.getCompRatio());
      this.dialAttack.setDialValue(gmEffect.getCompAttack());
      this.dialRelease.setDialValue(gmEffect.getCompRelease());
      this.dialGain.setDialValue(String.valueOf(gmEffect.getCompGain()));
      this.layout(this);
    }
  }

  class EditEffectMenu extends JPopupMenu implements Observer {
    final EffectComposite ownerPnl;
    private final Font menuFont = new Font(GLydianParameters.getInstance().getProperty("MENU_FONT"), GLydianParameters.getInstance().getNumerticPropertyValue("MENU_FONT_STYLE"), GLydianParameters.getInstance().getNumerticPropertyValue("MENU_FONT_SIZE"));

    EditEffectMenu(EffectComposite ownerPnl) {
      super("Copy values from");
      this.setFont(this.menuFont);
      this.ownerPnl = ownerPnl;
      this.refreshMenus();
      PatchLibrary.getInstance().addObserver(this);
    }

    public void update(Observable o, Object arg) {
      if (o.equals(PatchLibrary.getInstance())) {
        System.out.println("Refreshing Lib menu because Observer sent:" + arg);
        this.refreshMenus();
      }

    }

    void refreshMenus() {
      this.removeAll();
      this.setFont(this.menuFont);
      if (this.ownerPnl.effect == null) {
        JMenuItem menuItemX = new JMenuItem("No effects available for copy ");
        menuItemX.setFont(this.menuFont);
        this.add(menuItemX);
      } else {
        int itemsInOwnerMenu = 0;
        JComponent ownerMenu = this;
        List<GmEffect> effects = PatchLibrary.getInstance().getCopyableEffects(EffectComposite.this.effect);

        for (GmEffect effect : effects) {
          if (itemsInOwnerMenu > GLydianSwing.MAX_NO_ITEMS_IN_A_MENU) {
            itemsInOwnerMenu = 0;
            ownerMenu.add(new JSeparator());
            JMenu childMenu = new JMenu("More ..");
            childMenu.setFont(this.menuFont);
            ownerMenu.add(childMenu);
            ownerMenu = childMenu;
          }

          ++itemsInOwnerMenu;
          JMenuItem menuItem = new JMenuItem("Copy values from " + effect.getName());
          menuItem.setFont(this.menuFont);
          ownerMenu.add(menuItem);
          menuItem.addActionListener(e -> {
            System.out.println("Popup menu item [" + e.getActionCommand() + "] was pressed.");
            EditEffectMenu.this.ownerPnl.getEffect().copyValuesFrom(effect);
            EditEffectMenu.this.ownerPnl.getEffect().markChanged();
            GmMidiCommManager.getInstance().patchUpdated();
            EditEffectMenu.this.ownerPnl.refresh(EditEffectMenu.this.ownerPnl.effect);
          });
        }

      }
    }
  }

  public static class EqualizerComposite extends EffectComposite {
    private final JLabel freq = new JLabel("FREQ");
    private final JLabel width = new JLabel("WIDTH");
    private final JLabel gain = new JLabel("GAIN");
    private final JLabel band1 = new JLabel("BAND 1");
    private final JDial dialEq1Freq;
    private final JDial dialEq1Width;
    private final JDial dialEq1Gain;
    private final JLabel band2 = new JLabel("BAND 2");
    private final JDial dialEq2Freq;
    private final JDial dialEq2Width;
    private final JDial dialEq2Gain;
    private final JLabel band3 = new JLabel("BAND 3");
    private final JDial dialEq3Freq;
    private final JDial dialEq3Width;
    private final JDial dialEq3Gain;

    EqualizerComposite() {
      this.dialEq1Freq = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
      this.dialEq1Width = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
      this.dialEq1Gain = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
      this.dialEq2Freq = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
      this.dialEq2Width = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
      this.dialEq2Gain = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
      this.dialEq3Freq = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
      this.dialEq3Width = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
      this.dialEq3Gain = new JDial(RendererStyle.LABEL_NULL_VALUE_BOTTOM);
    }

    public void layout(EffectComposite pnl) {
      this.layoutTitleBar();
      SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
      Constraints thisCon = thisLayout.getConstraints(this);
      Constraints titleCon = thisLayout.getConstraints(this.titlePanel);
      Constraints bnd1Con = thisLayout.getConstraints(this.band1);
      Constraints bnd2Con = thisLayout.getConstraints(this.band2);
      Constraints bnd3Con = thisLayout.getConstraints(this.band3);
      Constraints freqCon = thisLayout.getConstraints(this.freq);
      Constraints widthCon = thisLayout.getConstraints(this.width);
      Constraints gainCon = thisLayout.getConstraints(this.gain);
      Constraints eq1FreqCon = thisLayout.getConstraints(this.dialEq1Freq);
      Constraints eq2FreqCon = thisLayout.getConstraints(this.dialEq2Freq);
      Constraints eq3FreqCon = thisLayout.getConstraints(this.dialEq3Freq);
      Constraints eq1WidthCon = thisLayout.getConstraints(this.dialEq1Width);
      Constraints eq2WidthCon = thisLayout.getConstraints(this.dialEq2Width);
      Constraints eq3WidthCon = thisLayout.getConstraints(this.dialEq3Width);
      Constraints eq1GainCon = thisLayout.getConstraints(this.dialEq1Gain);
      Constraints eq2GainCon = thisLayout.getConstraints(this.dialEq2Gain);
      Constraints eq3GainCon = thisLayout.getConstraints(this.dialEq3Gain);
      int gap = 1;
      titleCon.setX(Spring.constant(gap));
      titleCon.setY(Spring.constant(gap));
      titleCon.setWidth(thisCon.getWidth());
      Spring topSpring = Spring.sum(Spring.constant(gap), titleCon.getConstraint("South"));
      freqCon.setX(Spring.constant(2));
      widthCon.setX(Spring.constant(2));
      gainCon.setX(Spring.constant(2));
      bnd1Con.setX(widthCon.getConstraint("East"));
      bnd1Con.setY(topSpring);
      eq1FreqCon.setX(widthCon.getConstraint("East"));
      eq1FreqCon.setY(bnd1Con.getConstraint("South"));
      eq1WidthCon.setX(widthCon.getConstraint("East"));
      eq1WidthCon.setY(eq1FreqCon.getConstraint("South"));
      eq1GainCon.setX(widthCon.getConstraint("East"));
      eq1GainCon.setY(eq1WidthCon.getConstraint("South"));
      freqCon.setY(Spring.sum(Spring.constant(10), eq1FreqCon.getConstraint("North")));
      widthCon.setY(Spring.sum(Spring.constant(10), eq1WidthCon.getConstraint("North")));
      gainCon.setY(Spring.sum(Spring.constant(10), eq1GainCon.getConstraint("North")));
      bnd2Con.setX(eq1WidthCon.getConstraint("East"));
      bnd2Con.setY(topSpring);
      eq2FreqCon.setX(eq1WidthCon.getConstraint("East"));
      eq2FreqCon.setY(bnd2Con.getConstraint("South"));
      eq2WidthCon.setX(eq1WidthCon.getConstraint("East"));
      eq2WidthCon.setY(eq2FreqCon.getConstraint("South"));
      eq2GainCon.setX(eq1WidthCon.getConstraint("East"));
      eq2GainCon.setY(eq2WidthCon.getConstraint("South"));
      bnd3Con.setX(eq2WidthCon.getConstraint("East"));
      bnd3Con.setY(topSpring);
      eq3FreqCon.setX(eq2WidthCon.getConstraint("East"));
      eq3FreqCon.setY(bnd3Con.getConstraint("South"));
      eq3WidthCon.setX(eq2WidthCon.getConstraint("East"));
      eq3WidthCon.setY(eq3FreqCon.getConstraint("South"));
      eq3GainCon.setX(eq2WidthCon.getConstraint("East"));
      eq3GainCon.setY(eq3WidthCon.getConstraint("South"));
      thisCon.setWidth(Spring.sum(
        Spring.constant(gap * 5),
        Spring.sum(
          widthCon.getWidth(),
          Spring.max(
            Spring.sum(
              eq1FreqCon.getWidth(),
              Spring.sum(eq2FreqCon.getWidth(), eq3FreqCon.getWidth())
            ),
            Spring.max(
              Spring.sum(
                eq1GainCon.getWidth(),
                Spring.sum(eq2GainCon.getWidth(), eq3GainCon.getWidth())
              ),
              Spring.sum(
                eq1WidthCon.getWidth(),
                Spring.sum(eq2WidthCon.getWidth(), eq3WidthCon.getWidth())
              )
            )
          )
        )
      ));
      thisCon.setHeight(Spring.sum(
        Spring.constant(gap),
        Spring.sum(
          titleCon.getHeight(),
          Spring.max(
            Spring.sum(
              bnd1Con.getHeight(),
              Spring.sum(
                eq1FreqCon.getHeight(),
                Spring.sum(
                  eq1WidthCon.getHeight(),
                  eq1GainCon.getHeight()
                )
              )
            ),
            Spring.max(Spring.sum(
              bnd2Con.getHeight(),
              Spring.sum(
                eq2FreqCon.getHeight(),
                Spring.sum(
                  eq2WidthCon.getHeight(),
                  eq2GainCon.getHeight()
                )
              )
              ),
              Spring.sum(
                bnd3Con.getHeight(),
                Spring.sum(
                  eq3FreqCon.getHeight(),
                  Spring.sum(
                    eq3WidthCon.getHeight(),
                    eq3GainCon.getHeight()
                  )
                )
              )
            )
          )
        )
      ));
      this.updateUI();
    }

    public void initPanel() {
      this.initTitleBar("EQUALIZER");
      this.add(this.titlePanel);
      this.freq.setFont(this.labelFont);
      this.add(this.freq);
      this.width.setFont(this.labelFont);
      this.add(this.width);
      this.gain.setFont(this.labelFont);
      this.add(this.gain);
      this.band1.setFont(this.labelFont);
      this.add(this.band1);
      this.band2.setFont(this.labelFont);
      this.add(this.band2);
      this.band3.setFont(this.labelFont);
      this.add(this.band3);
      this.dialEq1Freq.setLabel("FREQ  ");
      this.dialEq1Freq.setFont(this.labelFont);
      this.dialEq1Freq.setRange(EqFrequencyRange.getInstance());
      this.add(this.dialEq1Freq);
      this.dialEq1Freq.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq1Freq(EqualizerComposite.this.dialEq1Freq.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialEq1Width.setLabel("WIDTH");
      this.dialEq1Width.setFont(this.labelFont);
      this.dialEq1Width.setRange(EqWidthRange.getInstance());
      this.add(this.dialEq1Width);
      this.dialEq1Width.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq1Width(EqualizerComposite.this.dialEq1Width.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialEq1Gain.setLabel("GAIN   ");
      this.dialEq1Gain.setFont(this.labelFont);
      this.dialEq1Gain.setRange(GmEqualizer.eqGainRange);
      this.add(this.dialEq1Gain);
      this.dialEq1Gain.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq1Gain(EqualizerComposite.this.dialEq1Gain.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialEq2Freq.setLabel("FREQ");
      this.dialEq2Freq.setFont(this.labelFont);
      this.dialEq2Freq.setRange(EqFrequencyRange.getInstance());
      this.add(this.dialEq2Freq);
      this.dialEq2Freq.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq2Freq(EqualizerComposite.this.dialEq2Freq.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });

      this.dialEq2Width.setLabel("WIDTH");
      this.dialEq2Width.setFont(this.labelFont);
      this.dialEq2Width.setRange(EqWidthRange.getInstance());
      this.add(this.dialEq2Width);
      this.dialEq2Width.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq2Width(EqualizerComposite.this.dialEq2Width.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });

      this.dialEq2Gain.setLabel("GAIN");
      this.dialEq2Gain.setFont(this.labelFont);
      this.dialEq2Gain.setRange(GmEqualizer.eqGainRange);
      this.add(this.dialEq2Gain);
      this.dialEq2Gain.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq2Gain(EqualizerComposite.this.dialEq2Gain.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });

      this.dialEq3Freq.setLabel("FREQ");
      this.dialEq3Freq.setFont(this.labelFont);
      this.dialEq3Freq.setRange(EqFrequencyRange.getInstance());
      this.add(this.dialEq3Freq);
      this.dialEq3Freq.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq3Freq(EqualizerComposite.this.dialEq3Freq.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialEq3Width.setLabel("WIDTH");
      this.dialEq3Width.setFont(this.labelFont);
      this.dialEq3Width.setRange(EqWidthRange.getInstance());
      this.add(this.dialEq3Width);
      this.dialEq3Width.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq3Width(EqualizerComposite.this.dialEq3Width.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialEq3Gain.setLabel("GAIN");
      this.dialEq3Gain.setFont(this.labelFont);
      this.dialEq3Gain.setRange(GmEqualizer.eqGainRange);
      this.add(this.dialEq3Gain);
      this.dialEq3Gain.addChangeListener(changeEvent -> {
        ((GmEqualizer)EqualizerComposite.this.effect).setEq3Gain(EqualizerComposite.this.dialEq3Gain.getDialValue().toString());
        EqualizerComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
    }

    public void refresh(GmEffect pGmEffect) {
      GmEqualizer gmEffect = (GmEqualizer)pGmEffect;
      super.refresh(gmEffect);
      this.onOffButton.setSelected(gmEffect.isEffectOn());
      this.dialEq1Width.setDialValue(gmEffect.getEq1Width());
      this.dialEq1Freq.setDialValue(gmEffect.getEq1Freq());
      this.dialEq1Gain.setDialValue(gmEffect.getEq1Gain());
      this.dialEq2Width.setDialValue(gmEffect.getEq2Width());
      this.dialEq2Freq.setDialValue(gmEffect.getEq2Freq());
      this.dialEq2Gain.setDialValue(gmEffect.getEq2Gain());
      this.dialEq3Width.setDialValue(gmEffect.getEq3Width());
      this.dialEq3Freq.setDialValue(gmEffect.getEq3Freq());
      this.dialEq3Gain.setDialValue(gmEffect.getEq3Gain());
      this.layout(this);
    }
  }

  public static class NoiseGateComposite extends EffectComposite {
    private JLedButton softMode;
    private JLedButton hardMode;
    private JPanel modeBtnPnl;
    private JDial dialMaxDamping;
    private JDial dialThreshold;
    private JDial dialRelease;

    NoiseGateComposite() {
    }

    public void layout(EffectComposite pnl) {
      GLydianRackArranger.getInstance().setEffectUnitDimension(this);
      SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
      Constraints thisCon = thisLayout.getConstraints(pnl);
      Constraints titleCon = thisLayout.getConstraints(this.titlePanel);
      Constraints modeCon = thisLayout.getConstraints(this.modeBtnPnl);
      Constraints dampCon = thisLayout.getConstraints(this.dialMaxDamping);
      Constraints threCon = thisLayout.getConstraints(this.dialThreshold);
      Constraints releCon = thisLayout.getConstraints(this.dialRelease);
      int gap = 1;
      titleCon.setX(Spring.constant(gap));
      titleCon.setY(Spring.constant(gap));
      titleCon.setWidth(thisCon.getWidth());
      Spring top = Spring.sum(Spring.constant(gap), titleCon.getConstraint("South"));
      modeCon.setX(Spring.constant(gap));
      modeCon.setY(top);
      dampCon.setX(Spring.sum(Spring.constant(gap), modeCon.getConstraint("East")));
      dampCon.setY(top);
      threCon.setX(Spring.sum(Spring.constant(gap), dampCon.getConstraint("East")));
      threCon.setY(top);
      releCon.setX(Spring.sum(Spring.constant(gap), threCon.getConstraint("East")));
      releCon.setY(top);
      thisCon.setWidth(Spring.sum(Spring.constant(gap * 5), Spring.sum(modeCon.getWidth(), Spring.sum(dampCon.getWidth(), Spring.sum(threCon.getWidth(), releCon.getWidth())))));
      thisCon.setHeight(Spring.sum(Spring.constant(gap * 3), Spring.sum(titleCon.getHeight(), Spring.max(dampCon.getHeight(), Spring.max(threCon.getHeight(), releCon.getHeight())))));
      this.layoutTitleBar();
    }

    public void initPanel() {
      this.softMode = new JLedButton("SOFT");
      this.hardMode = new JLedButton("HARD");
      this.initTitleBar("NOISE GATE");
      this.add(this.titlePanel);
      this.modeBtnPnl = new EffectComposite.ButtonGroupPanel("GATE TYPE", this.labelFont);
      ActionListener modeListener = actionEvent -> {
        if (NoiseGateComposite.this.effect != null) {
          if (NoiseGateComposite.this.softMode.isSelected()) {
            ((GmNoiseGate)NoiseGateComposite.this.effect).setMode(NOISE_GATE_MODE.SOFT);
          }

          if (NoiseGateComposite.this.hardMode.isSelected()) {
            ((GmNoiseGate)NoiseGateComposite.this.effect).setMode(NOISE_GATE_MODE.HARD);
          }

          NoiseGateComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        }

      };
      this.modeBtnPnl.setOpaque(false);
      this.modeBtnPnl.setLayout(new BoxLayout(this.modeBtnPnl, BoxLayout.Y_AXIS));
      this.softMode.setOpaque(false);
      this.softMode.setFont(this.labelFont);
      this.softMode.addActionListener(modeListener);
      this.modeBtnPnl.add(this.softMode);
      this.hardMode.setOpaque(false);
      this.hardMode.setFont(this.labelFont);
      this.hardMode.addActionListener(modeListener);
      this.modeBtnPnl.add(this.hardMode);
      this.modeBtnPnl.add(Box.createVerticalGlue());
      this.add(this.modeBtnPnl);
      ButtonGroup modeBtnGroup = new ButtonGroup();
      modeBtnGroup.add(this.softMode);
      modeBtnGroup.add(this.hardMode);
      this.dialThreshold = new JDial();
      this.dialThreshold.setLabel("THRESHOLD");
      this.dialThreshold.setFont(this.labelFont);
      this.dialThreshold.setRange(GmNoiseGate.noiseThresholdRange);
      this.add(this.dialThreshold);
      this.dialThreshold.addChangeListener(changeEvent -> {
        ((GmNoiseGate)NoiseGateComposite.this.effect).setThreshold(NoiseGateComposite.this.dialThreshold.getDialValue().toString());
        NoiseGateComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialMaxDamping = new JDial();
      this.dialMaxDamping.setLabel("MAX DAMP");
      this.dialMaxDamping.setFont(this.labelFont);
      this.dialMaxDamping.setRange(GmNoiseGate.noiseMaxDampingRange);
      this.add(this.dialMaxDamping);
      this.dialMaxDamping.addChangeListener(changeEvent -> {
        ((GmNoiseGate)NoiseGateComposite.this.effect).setMaxDamping(NoiseGateComposite.this.dialMaxDamping.getDialValue().toString());
        NoiseGateComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
      this.dialRelease = new JDial();
      this.dialRelease.setLabel("RELEASE");
      this.dialRelease.setFont(this.labelFont);
      this.dialRelease.setRange(GmNoiseGate.noiseReleaseRange);
      this.add(this.dialRelease);
      this.dialRelease.addChangeListener(changeEvent -> {
        ((GmNoiseGate)NoiseGateComposite.this.effect).setRelease(NoiseGateComposite.this.dialRelease.getDialValue().toString());
        NoiseGateComposite.this.effect.markChanged();
        GmMidiCommManager.getInstance().patchUpdated();
      });
    }

    public void refresh(GmEffect pGmEffect) {
      GmNoiseGate gmEffect = (GmNoiseGate)pGmEffect;
      super.refresh(gmEffect);
      this.onOffButton.setSelected(gmEffect.isEffectOn());
      if (gmEffect.getMode() == NOISE_GATE_MODE.SOFT) {
        this.softMode.setSelected(true);
      } else {
        this.hardMode.setSelected(false);
      }

      if (gmEffect.getMode() == NOISE_GATE_MODE.HARD) {
        this.hardMode.setSelected(true);
      } else {
        this.softMode.setSelected(false);
      }

      this.dialThreshold.setDialValue(String.valueOf(gmEffect.getThreshold()));
      this.dialMaxDamping.setDialValue(gmEffect.getMaxDamping());
      this.dialRelease.setDialValue(gmEffect.getRelease());
      this.layout(this);
    }
  }

  class componentLayoutStrategy {
    final int gap = 2;
    final JPanel ownerPnl;
    final List<componentLayoutStrategy.componentAssociation> componentList;
    final Map<JComponent, componentLayoutStrategy.componentAssociation> componentMap;

    componentLayoutStrategy(JPanel ownerPnl) {
      this.ownerPnl = ownerPnl;
      this.componentList = new Vector<>();
      this.componentMap = new HashMap<>();
    }

    void addComponent(JComponent item, JComponent eastItem, JComponent westItem, JComponent northItem, JComponent southItem) {
      componentLayoutStrategy.componentAssociation asso = new componentLayoutStrategy.componentAssociation(item, eastItem, westItem, northItem, southItem);
      this.componentList.add(asso);
      this.componentMap.put(item, asso);
    }

    void layout() {
      SpringLayout thisLayout = (SpringLayout)this.ownerPnl.getLayout();
      Constraints thisCon = thisLayout.getConstraints(this.ownerPnl);

      for (componentAssociation aComponentList : this.componentList) {
        Constraints itemConstraints = thisLayout.getConstraints(aComponentList.item);
        if (aComponentList.westItem != null) {
          itemConstraints.setX(Spring.sum(Spring.constant(this.gap), thisLayout.getConstraints(aComponentList.westItem).getConstraint("East")));
        } else {
          itemConstraints.setX(Spring.constant(this.gap));
        }

        if (aComponentList.northItem != null) {
          if (aComponentList.item instanceof ModifierButton) {
            itemConstraints.setY(Spring.sum(Spring.constant(this.gap - 2), thisLayout.getConstraints(aComponentList.northItem).getConstraint("South")));
          } else {
            itemConstraints.setY(Spring.sum(Spring.constant(this.gap), thisLayout.getConstraints(aComponentList.northItem).getConstraint("South")));
          }
        } else {
          itemConstraints.setY(Spring.constant(this.gap));
        }
      }

      Spring totalWidth = null;
      Vector<EffectComposite.componentLayoutStrategy.componentAssociation> newList = new Vector<>(this.componentList);

      while(newList.size() != 0) {
        componentLayoutStrategy.componentAssociation elementX = newList.remove(0);
        Spring width = thisLayout.getConstraints(elementX.item).getWidth();
        componentLayoutStrategy.componentAssociation assox;
        boolean b;
        if (elementX.northItem != null) {
          assox = this.componentMap.get(elementX.northItem);
          b = newList.contains(assox);
          if (b) {
            newList.remove(assox);
            width = Spring.max(width, thisLayout.getConstraints(elementX.northItem).getWidth());
          }
        }

        if (elementX.southItem != null) {
          assox = this.componentMap.get(elementX.southItem);
          b = newList.contains(assox);
          if (b) {
            newList.remove(assox);
            width = Spring.max(width, thisLayout.getConstraints(elementX.southItem).getWidth());
          }
        }

        if (totalWidth != null) {
          totalWidth = Spring.sum(totalWidth, Spring.sum(Spring.constant(this.gap), width));
        } else {
          totalWidth = Spring.sum(Spring.constant(this.gap), width);
        }
      }

      totalWidth = Spring.sum(totalWidth, Spring.constant(this.gap));
      Spring totalHeight = null;
      newList = new Vector<>(this.componentList);

      while(newList.size() != 0) {
        componentLayoutStrategy.componentAssociation element = newList.remove(0);
        Spring height = thisLayout.getConstraints(element.item).getHeight();
        componentLayoutStrategy.componentAssociation asso;
        if (element.northItem != null) {
          asso = this.componentMap.get(element.northItem);
          if (newList.remove(asso)) {
            height = Spring.sum(height, thisLayout.getConstraints(element.northItem).getHeight());
          }
        }

        if (element.southItem != null) {
          asso = this.componentMap.get(element.southItem);
          if (newList.remove(asso)) {
            height = Spring.sum(height, thisLayout.getConstraints(element.southItem).getHeight());
          }
        }

        if (!(element.item instanceof EffectComposite.ButtonGroupPanel)) {
          if (totalHeight != null) {
            totalHeight = Spring.max(totalHeight, Spring.sum(Spring.constant(this.gap * 2), height));
          } else {
            totalHeight = Spring.sum(Spring.constant(this.gap * 2), height);
          }
        }
      }

      thisCon.setWidth(totalWidth);
      thisCon.setHeight(totalHeight);
    }

    class componentAssociation {
      final JComponent item;
      final JComponent eastItem;
      final JComponent westItem;
      final JComponent northItem;
      final JComponent southItem;

      componentAssociation(JComponent item, JComponent eastItem, JComponent westItem, JComponent northItem, JComponent southItem) {
        this.item = item;
        this.eastItem = eastItem;
        this.westItem = westItem;
        this.northItem = northItem;
        this.southItem = southItem;
      }
    }
  }
}
