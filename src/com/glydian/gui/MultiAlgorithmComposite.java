//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.gui;

import com.glydian.gui.controls.J2DButton;
import com.glydian.gui.controls.J2DTextFieldBorder;
import com.glydian.gui.controls.JDial;
import com.glydian.gui.controls.JLedButton;
import com.glydian.gui.controls.J2DButton.UP_DOWN_BUTTON_TYPE;
import com.glydian.gui.controls.JDial.RendererStyle;
import com.glydian.model.GmMidiCommManager;
import com.glydian.model.DiscreteRange.CompTimeRange;
import com.glydian.model.DiscreteRange.ReverbHiColorRange;
import com.glydian.model.DiscreteRange.ReverbLoColorRange;
import com.glydian.model.DiscreteRange.ReverbSizeRange;
import com.glydian.model.DiscreteRange.TapTempoRange;
import com.glydian.model.effect.GmDelayEffect;
import com.glydian.model.effect.GmEffect;
import com.glydian.model.effect.GmFilterModEffect;
import com.glydian.model.effect.GmModulationEffect;
import com.glydian.model.effect.GmMultiAlgorithmEffect;
import com.glydian.model.effect.GmPitchEffect;
import com.glydian.model.effect.GmReverbEffect;
import com.glydian.model.effect.GmDelayEffect.DelayAlgorithm;
import com.glydian.model.effect.GmDelayEffect.DualDelayAlgorithm;
import com.glydian.model.effect.GmDelayEffect.DynamicDelayAlgorithm;
import com.glydian.model.effect.GmDelayEffect.PingPongDelayAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmFilterAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmPannerAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmPhaserAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmTremoloAlgorithm;
import com.glydian.model.effect.GmFilterModEffect.GmFilterAlgorithm.FILTER_ORDER;
import com.glydian.model.effect.GmFilterModEffect.GmFilterAlgorithm.FILTER_RESPONSE;
import com.glydian.model.effect.GmFilterModEffect.GmPhaserAlgorithm.PHASER_RANGE;
import com.glydian.model.effect.GmFilterModEffect.GmTremoloAlgorithm.TREMOLO_TYPE;
import com.glydian.model.effect.GmModulationEffect.GmAdvanceChorusAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmAdvanceFlangAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmClassicChorusAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmClassicFlangAlgorithm;
import com.glydian.model.effect.GmModulationEffect.GmVibratoAlgorithm;
import com.glydian.model.effect.GmModulationEffect.ModulationAlgorithm;
import com.glydian.model.effect.GmMultiAlgorithmEffect.ALGORITHM_TYPE;
import com.glydian.model.effect.GmMultiAlgorithmEffect.DELAY_ALGO_TYPE;
import com.glydian.model.effect.GmMultiAlgorithmEffect.FILTER_ALGO_TYPE;
import com.glydian.model.effect.GmMultiAlgorithmEffect.HiCutFreqRange;
import com.glydian.model.effect.GmMultiAlgorithmEffect.LoCutRange;
import com.glydian.model.effect.GmMultiAlgorithmEffect.MODULATION_ALGO_TYPE;
import com.glydian.model.effect.GmMultiAlgorithmEffect.ONOFF_VALUE;
import com.glydian.model.effect.GmMultiAlgorithmEffect.PITCH_ALGO_TYPE;
import com.glydian.model.effect.GmMultiAlgorithmEffect.REVERB_ALGO_TYPE;
import com.glydian.model.effect.GmMultiAlgorithmEffect.SpeedRange;
import com.glydian.model.effect.GmPitchEffect.GmDetuneAlgorithm;
import com.glydian.model.effect.GmPitchEffect.GmOctaverAlgorithm;
import com.glydian.model.effect.GmPitchEffect.GmShifterAlgorithm;
import com.glydian.model.effect.GmPitchEffect.GmWhammyAlgorithm;
import com.glydian.model.effect.GmPitchEffect.OCTAVE_RANGE;
import com.glydian.model.effect.GmPitchEffect.PITCH_DIRECTION;
import com.glydian.model.effect.GmReverbEffect.REVERB_SHAPE;
import com.glydian.model.effect.GmReverbEffect.ReverbAlgorithm;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

public class MultiAlgorithmComposite extends EffectComposite {
  private final JDial dialMix;
  private final ModifierButton mixModifier;
  private final JDial dialOutLevel;
  private final ModifierButton outliveModifier;
  private final JPanel algorithmSelectionPnl = new JPanel();
  final JPanel algorithmControlPnl;
  private final JTextField algoName = new JTextField();
  private final J2DButton algoUp = new J2DButton();
  private final J2DButton algoDown = new J2DButton();
  final List<ALGORITHM_TYPE> algorithms = new Vector<>();

  MultiAlgorithmComposite() {
    this.dialMix = new JDial(RendererStyle.LABEL_RIGHT_VALUE_RIGHT);
    this.mixModifier = new ModifierButton();
    this.dialOutLevel = new JDial(RendererStyle.LABEL_RIGHT_VALUE_RIGHT);
    this.outliveModifier = new ModifierButton();
    this.algorithmControlPnl = new JPanel();
    this.algorithmControlPnl.setLayout(new SpringLayout());
    this.algorithmSelectionPnl.setLayout(new SpringLayout());
  }

  private void initAlgorithmSelectionPnl() {
    this.algorithmSelectionPnl.setOpaque(false);
    this.algorithmSelectionPnl.setBorder(this.basicBorder);
    this.algoUp.setOpaque(false);
    this.algoUp.setButtonType(UP_DOWN_BUTTON_TYPE.UP);
    this.algoUp.addActionListener(arg0 -> {
      if (MultiAlgorithmComposite.this.effect instanceof GmMultiAlgorithmEffect && MultiAlgorithmComposite.this.algorithms.contains(((GmMultiAlgorithmEffect) MultiAlgorithmComposite.this.effect).getAlgorithmType())) {
        int nextAlgoIdx = 0;
        int currIdx = MultiAlgorithmComposite.this.algorithms.indexOf(((GmMultiAlgorithmEffect)MultiAlgorithmComposite.this.effect).getAlgorithmType());
        if (MultiAlgorithmComposite.this.algorithms.size() > currIdx + 1) {
          nextAlgoIdx = currIdx + 1;
        }

        ((GmMultiAlgorithmEffect)MultiAlgorithmComposite.this.effect).setAlgorithmType(MultiAlgorithmComposite.this.algorithms.get(nextAlgoIdx));
        MultiAlgorithmComposite.this.refresh(MultiAlgorithmComposite.this.effect);
      }

    });
    this.algorithmSelectionPnl.add(this.algoUp);
    this.algoDown.setOpaque(false);
    this.algoDown.setButtonType(UP_DOWN_BUTTON_TYPE.DOWN);
    this.algoDown.addActionListener(arg0 -> {
      if (MultiAlgorithmComposite.this.effect instanceof GmMultiAlgorithmEffect && MultiAlgorithmComposite.this.algorithms.contains(((GmMultiAlgorithmEffect) MultiAlgorithmComposite.this.effect).getAlgorithmType())) {
        int nextAlgoIdx = MultiAlgorithmComposite.this.algorithms.size() - 1;
        int currIdx = MultiAlgorithmComposite.this.algorithms.indexOf(((GmMultiAlgorithmEffect)MultiAlgorithmComposite.this.effect).getAlgorithmType());
        if (currIdx > 0) {
          nextAlgoIdx = currIdx - 1;
        }

        ((GmMultiAlgorithmEffect)MultiAlgorithmComposite.this.effect).setAlgorithmType(MultiAlgorithmComposite.this.algorithms.get(nextAlgoIdx));
        MultiAlgorithmComposite.this.refresh(MultiAlgorithmComposite.this.effect);
      }

    });
    this.algorithmSelectionPnl.add(this.algoDown);
    this.algoName.setMargin(new Insets(5, 5, 5, 5));
    this.algoName.setText("");
    this.algoName.setBorder(new J2DTextFieldBorder());
    this.algoName.setFont(this.labelFont);
    this.algoName.setColumns(20);
    this.algorithmSelectionPnl.add(this.algoName);
  }

  private void layoutAlgorithmSelectionPnl() {
    SpringLayout thisLayout = (SpringLayout)this.algorithmSelectionPnl.getLayout();
    Constraints thisCon = thisLayout.getConstraints(this.algorithmSelectionPnl);
    Constraints nameCon = thisLayout.getConstraints(this.algoName);
    Constraints upCon = thisLayout.getConstraints(this.algoUp);
    Constraints downCon = thisLayout.getConstraints(this.algoDown);
    int gap = 3;
    nameCon.setX(Spring.constant(gap));
    nameCon.setY(Spring.constant(gap));
    nameCon.setHeight(Spring.sum(upCon.getHeight(), downCon.getHeight()));
    upCon.setX(Spring.sum(Spring.constant(gap), nameCon.getConstraint("East")));
    upCon.setY(Spring.constant(gap));
    downCon.setX(Spring.sum(Spring.constant(gap), nameCon.getConstraint("East")));
    downCon.setY(upCon.getConstraint("South"));
    thisCon.setWidth(Spring.sum(Spring.constant(gap * 3), Spring.sum(nameCon.getWidth(), upCon.getWidth())));
    thisCon.setHeight(Spring.sum(Spring.constant(gap * 2), Spring.sum(upCon.getHeight(), downCon.getHeight())));
  }

  public void layout(EffectComposite pnl) {
    GLydianRackArranger.getInstance().setEffectUnitDimension(this);
    this.layoutAlgorithmSelectionPnl();
    SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
    Constraints thisCon = thisLayout.getConstraints(this);
    Constraints titleCon = thisLayout.getConstraints(this.titlePanel);
    Constraints algoSelPnlCon = thisLayout.getConstraints(this.algorithmSelectionPnl);
    Constraints mixCon = thisLayout.getConstraints(this.dialMix);
    Constraints mixModCon = thisLayout.getConstraints(this.mixModifier);
    Constraints outLevelCon = thisLayout.getConstraints(this.dialOutLevel);
    Constraints outLevelModCon = thisLayout.getConstraints(this.outliveModifier);
    Constraints algoPnlCon = thisLayout.getConstraints(this.algorithmControlPnl);
    int gap = 1;
    titleCon.setX(Spring.constant(gap));
    titleCon.setY(Spring.constant(gap));
    titleCon.setWidth(thisCon.getWidth());
    Spring topSpring = Spring.sum(Spring.constant(gap * 2), titleCon.getConstraint("South"));
    algoSelPnlCon.setX(Spring.constant(gap));
    algoSelPnlCon.setY(topSpring);
    algoSelPnlCon.setWidth(Spring.sum(Spring.constant(gap), Spring.sum(mixCon.getWidth(), outLevelCon.getWidth())));
    mixCon.setX(Spring.constant(gap));
    mixCon.setY(Spring.sum(Spring.constant(gap), algoSelPnlCon.getConstraint("South")));
    mixModCon.setX(Spring.constant(gap));
    mixModCon.setY(Spring.sum(Spring.constant(gap), mixCon.getConstraint("South")));
    outLevelCon.setX(Spring.sum(Spring.constant(gap), mixCon.getConstraint("East")));
    outLevelCon.setY(Spring.sum(Spring.constant(gap), algoSelPnlCon.getConstraint("South")));
    outLevelModCon.setX(Spring.sum(Spring.constant(gap), mixCon.getConstraint("East")));
    outLevelModCon.setY(Spring.sum(Spring.constant(gap), outLevelCon.getConstraint("South")));
    algoPnlCon.setX(Spring.sum(Spring.constant(gap * 2), algoSelPnlCon.getConstraint("East")));
    algoPnlCon.setY(topSpring);
    thisCon.setWidth(Spring.sum(Spring.constant(gap * 3), Spring.sum(Spring.max(Spring.sum(Spring.max(mixCon.getWidth(), mixModCon.getWidth()), Spring.max(outLevelCon.getWidth(), outLevelModCon.getWidth())), algoSelPnlCon.getWidth()), algoPnlCon.getWidth())));
    thisCon.setHeight(Spring.sum(Spring.constant(gap * 3), Spring.sum(titleCon.getHeight(), Spring.max(Spring.sum(algoSelPnlCon.getHeight(), Spring.sum(mixCon.getHeight(), mixModCon.getHeight())), algoPnlCon.getHeight()))));
    this.layoutTitleBar();
  }

  public void initPanel() {
    this.add(this.titlePanel);
    this.initAlgorithmSelectionPnl();
    this.add(this.algorithmSelectionPnl);
    this.dialMix.setLabel("MIX");
    this.dialMix.setFont(this.labelFont);
    this.dialMix.setRange(GmMultiAlgorithmEffect.mixRange);
    this.add(this.dialMix);
    this.dialMix.addChangeListener(arg0 -> {
      ((GmMultiAlgorithmEffect)MultiAlgorithmComposite.this.effect).setMix(MultiAlgorithmComposite.this.dialMix.getDialValue().toString());
      MultiAlgorithmComposite.this.effect.markChanged();
      GmMidiCommManager.getInstance().patchUpdated();
    });
    this.dialMix.getMinimumSize();
    this.mixModifier.setOpaque(false);
    this.mixModifier.setFont(this.labelFont);
    this.add(this.mixModifier);
    this.dialOutLevel.setLabel("OUT LEVEL");
    this.dialOutLevel.setFont(this.labelFont);
    this.dialOutLevel.setRange(GmMultiAlgorithmEffect.outLevelRange);
    this.add(this.dialOutLevel);
    this.dialOutLevel.addChangeListener(arg0 -> {
      ((GmMultiAlgorithmEffect)MultiAlgorithmComposite.this.effect).setOutLevel(MultiAlgorithmComposite.this.dialOutLevel.getDialValue().toString());
      MultiAlgorithmComposite.this.effect.markChanged();
      GmMidiCommManager.getInstance().patchUpdated();
    });
    this.dialOutLevel.getMinimumSize();
    this.outliveModifier.setOpaque(false);
    this.outliveModifier.setFont(this.labelFont);
    this.add(this.outliveModifier);
    this.algorithmControlPnl.setOpaque(false);
    this.algorithmControlPnl.setLayout(new SpringLayout());
    this.add(this.algorithmControlPnl);
    this.updateUI();
  }

  void refresh(GmEffect effect) {
    super.refresh(effect);
    this.dialMix.setDialValue(((GmMultiAlgorithmEffect)effect).getMix());
    this.mixModifier.setEnabled(((GmMultiAlgorithmEffect)effect).isMixModAssignable());
    if (((GmMultiAlgorithmEffect)effect).getMixModifier() != null) {
      this.mixModifier.setModifierValue(((GmMultiAlgorithmEffect)effect).getMixModifier());
    }

    this.dialOutLevel.setDialValue(((GmMultiAlgorithmEffect)effect).getOutLevel());
    this.outliveModifier.setEnabled(((GmMultiAlgorithmEffect)effect).isOutLevelModAssignable());
    this.outliveModifier.setModifierValue(((GmMultiAlgorithmEffect)effect).getOutLvlModifier());
    this.algoName.setText(((GmMultiAlgorithmEffect)effect).getAlgorithmType().getName());
  }

  void packPanel(JPanel pnl) {
    SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
    Constraints thisCon = thisLayout.getConstraints(pnl);
    SpringLayout algoLayout = (SpringLayout)this.algorithmControlPnl.getLayout();
    Constraints algoCon = algoLayout.getConstraints(this.algorithmControlPnl);
    thisCon.setX(algoCon.getX());
    thisCon.setY(algoCon.getY());
    algoCon.setWidth(thisCon.getWidth());
    algoCon.setHeight(thisCon.getHeight());
  }

  public static class DelayComposite extends MultiAlgorithmComposite {
    private DelayComposite.DualDelayPane dualDelayPane;
    private DelayComposite.DynamicDelayPane dynamicDelayPane;
    private DelayComposite.PingPongDelayPane pingpongDelayPane;

    public DelayComposite() {
      this.algorithms.add(DELAY_ALGO_TYPE.DYNAMIC);
      this.algorithms.add(DELAY_ALGO_TYPE.PING_PONG);
      this.algorithms.add(DELAY_ALGO_TYPE.DUAL);
    }

    public void initPanel() {
      this.initTitleBar("DELAY");
      super.initPanel();
    }

    public void refresh(GmEffect pGmEffect) {
      super.refresh(pGmEffect);
      if (((GmDelayEffect)pGmEffect).getAlgorithmType() == DELAY_ALGO_TYPE.DYNAMIC) {
        if (this.dynamicDelayPane == null) {
          this.dynamicDelayPane = new DelayComposite.DynamicDelayPane();
          this.dynamicDelayPane.initPanel();
        }

        this.algorithmControlPnl.add(this.dynamicDelayPane);
        this.dynamicDelayPane.layout(this.dynamicDelayPane);
        this.dynamicDelayPane.refresh((DynamicDelayAlgorithm)((GmDelayEffect)pGmEffect).getAlgorithm());
      } else if (this.dynamicDelayPane != null) {
        this.algorithmControlPnl.remove(this.dynamicDelayPane);
      }

      if (((GmDelayEffect)pGmEffect).getAlgorithmType() == DELAY_ALGO_TYPE.PING_PONG) {
        if (this.pingpongDelayPane == null) {
          this.pingpongDelayPane = new DelayComposite.PingPongDelayPane();
          this.pingpongDelayPane.initPanel();
        }

        this.algorithmControlPnl.add(this.pingpongDelayPane);
        this.pingpongDelayPane.layout(this.pingpongDelayPane);
        this.pingpongDelayPane.refresh((PingPongDelayAlgorithm)((GmDelayEffect)pGmEffect).getAlgorithm());
      } else if (this.pingpongDelayPane != null) {
        this.algorithmControlPnl.remove(this.pingpongDelayPane);
      }

      if (((GmDelayEffect)pGmEffect).getAlgorithmType() == DELAY_ALGO_TYPE.DUAL) {
        if (this.dualDelayPane == null) {
          this.dualDelayPane = new DelayComposite.DualDelayPane();
          this.dualDelayPane.initPanel();
        }

        this.algorithmControlPnl.add(this.dualDelayPane);
        this.dualDelayPane.layout(this.dualDelayPane);
        this.dualDelayPane.refresh((DualDelayAlgorithm)((GmDelayEffect)pGmEffect).getAlgorithm());
      } else if (this.dualDelayPane != null) {
        this.algorithmControlPnl.remove(this.dualDelayPane);
      }

      this.updateUI();
      this.layout(this);
    }

    abstract class DelayPane extends JPanel {
      private JDial delayTime;
      private JDial tempo;
      private JDial feedback;
      private JDial fbHiCut;
      private JDial fbLoCut;
      private JDial delayTime2;
      private JDial tempo2;
      private JDial feedback2;
      private JDial pan1;
      private JDial pan2;
      private JDial width;
      private JDial offset;
      private JDial sensitivity;
      private JDial damp;
      private JDial release;
      ModifierButton delayModifier;
      ModifierButton fbModifier;
      ModifierButton fbHiCutModifier;
      ModifierButton fbLoCutModifier;
      ModifierButton delay2Modifier;
      ModifierButton fb2Modifier;
      ModifierButton pan1Modifier;
      ModifierButton pan2Modifier;

      DelayPane() {
      }

      void refresh(DelayAlgorithm algorithm) {
        this.delayTime.setDialValue(algorithm.getDelayTime());
        this.feedback.setDialValue(algorithm.getFeedback());
        this.tempo.setDialValue(algorithm.getTempo());
        this.fbHiCut.setDialValue(algorithm.getFbHiCut());
        this.fbLoCut.setDialValue(algorithm.getFbLoCut());
        this.delayModifier.setModifierValue(algorithm.getDelayModifier());
        this.fbModifier.setModifierValue(algorithm.getFbModifier());
        this.fbHiCutModifier.setModifierValue(algorithm.getFbHiCutModifier());
        this.fbLoCutModifier.setModifierValue(algorithm.getFbLoCutModifier());
      }

      void refreshDual(DualDelayAlgorithm algorithm) {
        this.delayTime2.setDialValue(algorithm.getDelayTime2());
        this.feedback2.setDialValue(algorithm.getFeedback2());
        this.tempo2.setDialValue(algorithm.getTempo2());
        this.pan1.setDialValue(algorithm.getPan1());
        this.pan2.setDialValue(algorithm.getPan2());
        this.delay2Modifier.setModifierValue(algorithm.getDelay2Modifier());
        this.fb2Modifier.setModifierValue(algorithm.getFb2Modifier());
        this.pan1Modifier.setModifierValue(algorithm.getPan1Modifier());
        this.pan2Modifier.setModifierValue(algorithm.getPan2Modifier());
      }

      void refreshDynamic(DynamicDelayAlgorithm algorithm) {
        this.offset.setDialValue(algorithm.getOffset());
        this.sensitivity.setDialValue(algorithm.getSensitivity());
        this.damp.setDialValue(algorithm.getDamping());
        this.release.setDialValue(algorithm.getRelease());
      }

      void refreshPingPong(PingPongDelayAlgorithm algorithm) {
        this.width.setDialValue(algorithm.getWidth());
      }

      void layout(JPanel pnl) {
        componentLayoutStrategy layout = new componentLayoutStrategy(pnl);
        if (((GmDelayEffect)DelayComposite.this.effect).getAlgorithmType() == DELAY_ALGO_TYPE.DUAL) {
          layout.addComponent(this.delayTime, this.delayTime2, null, null, this.delayModifier);
          layout.addComponent(this.delayModifier, this.delayTime2, null, this.delayTime, null);
          layout.addComponent(this.delayTime2, this.tempo, this.delayTime, null, this.delay2Modifier);
          layout.addComponent(this.delay2Modifier, this.tempo, this.delayTime, this.delayTime2, null);
          layout.addComponent(this.tempo, this.tempo2, this.delayTime2, null, null);
          layout.addComponent(this.tempo2, this.feedback, this.tempo, null, null);
          layout.addComponent(this.feedback, this.feedback2, this.tempo2, null, this.fbModifier);
          layout.addComponent(this.fbModifier, this.feedback2, this.tempo2, this.feedback, null);
          layout.addComponent(this.feedback2, this.fbHiCut, this.feedback, null, this.fb2Modifier);
          layout.addComponent(this.fb2Modifier, this.fbHiCut, this.feedback, this.feedback2, null);
          layout.addComponent(this.fbHiCut, this.fbLoCut, this.feedback2, null, this.fbHiCutModifier);
          layout.addComponent(this.fbHiCutModifier, this.fbLoCut, this.feedback2, this.fbHiCut, null);
          layout.addComponent(this.fbLoCut, this.pan1, this.fbHiCut, null, this.fbLoCutModifier);
          layout.addComponent(this.fbLoCutModifier, this.pan1, this.fbHiCut, this.fbLoCut, null);
          layout.addComponent(this.pan1, this.pan2, this.fbLoCut, null, this.pan1Modifier);
          layout.addComponent(this.pan1Modifier, this.pan2, this.fbLoCut, this.pan1, null);
          layout.addComponent(this.pan2, null, this.pan1, null, this.pan2Modifier);
          layout.addComponent(this.pan2Modifier, null, this.pan1, this.pan2, null);
        } else {
          layout.addComponent(this.delayTime, this.tempo, null, null, this.delayModifier);
          layout.addComponent(this.delayModifier, this.tempo, null, this.delayTime, null);
          layout.addComponent(this.tempo, this.feedback, this.delayTime, null, null);
          layout.addComponent(this.feedback, this.fbHiCut, this.tempo, null, this.fbModifier);
          layout.addComponent(this.fbModifier, this.fbHiCut, this.tempo, this.feedback, null);
          layout.addComponent(this.fbHiCut, this.fbLoCut, this.feedback, null, this.fbHiCutModifier);
          layout.addComponent(this.fbHiCutModifier, this.fbLoCut, this.feedback, this.fbHiCut, null);
        }

        if (((GmDelayEffect)DelayComposite.this.effect).getAlgorithmType() == DELAY_ALGO_TYPE.PING_PONG) {
          layout.addComponent(this.fbLoCut, this.width, this.fbHiCut, null, this.fbLoCutModifier);
          layout.addComponent(this.fbLoCutModifier, this.width, this.fbHiCut, this.fbLoCut, null);
          layout.addComponent(this.width, null, this.fbLoCut, null, null);
        } else if (((GmDelayEffect)DelayComposite.this.effect).getAlgorithmType() == DELAY_ALGO_TYPE.DYNAMIC) {
          layout.addComponent(this.fbLoCut, this.offset, this.fbHiCut, null, this.fbLoCutModifier);
          layout.addComponent(this.fbLoCutModifier, this.offset, this.fbHiCut, this.fbLoCut, null);
          layout.addComponent(this.offset, this.sensitivity, this.fbLoCut, null, null);
          layout.addComponent(this.sensitivity, this.damp, this.offset, null, null);
          layout.addComponent(this.damp, this.release, this.sensitivity, null, null);
          layout.addComponent(this.release, null, this.damp, null, null);
        }

        layout.layout();
        DelayComposite.this.packPanel(pnl);
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.delayModifier = DelayComposite.this.createModifierControl();
        this.add(this.delayModifier);
        this.fbModifier = DelayComposite.this.createModifierControl();
        this.add(this.fbModifier);
        this.fbHiCutModifier = DelayComposite.this.createModifierControl();
        this.add(this.fbHiCutModifier);
        this.fbLoCutModifier = DelayComposite.this.createModifierControl();
        this.add(this.fbLoCutModifier);
        this.delayTime = new JDial();
        this.delayTime.setLabel("TIME");
        this.delayTime.setFont(DelayComposite.this.labelFont);
        this.delayTime.setRange(GmDelayEffect.delayTimeRange);
        this.add(this.delayTime);
        this.delayTime.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setDelayTime(DelayPane.this.delayTime.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.tempo = new JDial();
        this.tempo.setLabel("TEMP");
        this.tempo.setFont(DelayComposite.this.labelFont);
        this.tempo.setRange(TapTempoRange.getInstance());
        this.add(this.tempo);
        this.tempo.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setTempo(DelayPane.this.tempo.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.feedback = new JDial();
        this.feedback.setLabel("FB");
        this.feedback.setFont(DelayComposite.this.labelFont);
        this.feedback.setRange(GmDelayEffect.feedbackRange);
        this.add(this.feedback);
        this.feedback.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setFeedback(DelayPane.this.feedback.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.fbHiCut = new JDial();
        this.fbHiCut.setLabel("FBHICUT");
        this.fbHiCut.setFont(DelayComposite.this.labelFont);
        this.fbHiCut.setRange(HiCutFreqRange.getDelayFbHiCutInstance());
        this.add(this.fbHiCut);
        this.fbHiCut.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setFbHiCut(DelayPane.this.fbHiCut.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.fbLoCut = new JDial();
        this.fbLoCut.setLabel("FBLOCUT");
        this.fbLoCut.setFont(DelayComposite.this.labelFont);
        this.fbLoCut.setRange(LoCutRange.getFullLoCutInstance());
        this.add(this.fbLoCut);
        this.fbLoCut.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setFbLoCut(DelayPane.this.fbLoCut.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void initDynamic() {
        this.offset = new JDial();
        this.offset.setLabel("OFFSET");
        this.offset.setFont(DelayComposite.this.labelFont);
        this.offset.setRange(GmDelayEffect.offsetTimeRange);
        this.add(this.offset);
        this.offset.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setOffset(DelayPane.this.offset.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.sensitivity = new JDial();
        this.sensitivity.setLabel("SENSIT");
        this.sensitivity.setFont(DelayComposite.this.labelFont);
        this.sensitivity.setRange(GmDelayEffect.sensitivityRange);
        this.add(this.sensitivity);
        this.sensitivity.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setSensitivity(DelayPane.this.sensitivity.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.damp = new JDial();
        this.damp.setLabel("DAMP");
        this.damp.setFont(DelayComposite.this.labelFont);
        this.damp.setRange(GmDelayEffect.dampRange);
        this.add(this.damp);
        this.damp.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setDamping(DelayPane.this.damp.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.release = new JDial();
        this.release.setLabel("RELEASE");
        this.release.setFont(DelayComposite.this.labelFont);
        this.release.setRange(CompTimeRange.getDelayReleaseInstance());
        this.add(this.release);
        this.release.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setRelease(DelayPane.this.release.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void initPingPong() {
        this.width = new JDial();
        this.width.setLabel("WIDTH");
        this.width.setFont(DelayComposite.this.labelFont);
        this.width.setRange(GmDelayEffect.widthRange);
        this.add(this.width);
        this.width.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setWidth(DelayPane.this.width.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void initDual() {
        this.delay2Modifier = DelayComposite.this.createModifierControl();
        this.add(this.delay2Modifier);
        this.fb2Modifier = DelayComposite.this.createModifierControl();
        this.add(this.fb2Modifier);
        this.pan1Modifier = DelayComposite.this.createModifierControl();
        this.add(this.pan1Modifier);
        this.pan2Modifier = DelayComposite.this.createModifierControl();
        this.add(this.pan2Modifier);
        this.delayTime.setLabel("TIME1");
        this.tempo.setLabel("TEMP1");
        this.feedback.setLabel("FB 1");
        this.delayTime2 = new JDial();
        this.delayTime2.setLabel("TIME2");
        this.delayTime2.setFont(DelayComposite.this.labelFont);
        this.delayTime2.setRange(GmDelayEffect.delayTimeRange);
        this.add(this.delayTime2);
        this.delayTime2.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setDelayTime2(DelayPane.this.delayTime2.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.tempo2 = new JDial();
        this.tempo2.setLabel("TEMP2");
        this.tempo2.setFont(DelayComposite.this.labelFont);
        this.tempo2.setRange(TapTempoRange.getInstance());
        this.add(this.tempo2);
        this.tempo2.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setTempo2(DelayPane.this.tempo2.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.feedback2 = new JDial();
        this.feedback2.setLabel("FB 2");
        this.feedback2.setFont(DelayComposite.this.labelFont);
        this.feedback2.setRange(GmDelayEffect.feedbackRange);
        this.add(this.feedback2);
        this.feedback2.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setFeedback2(DelayPane.this.feedback2.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.pan1 = new JDial();
        this.pan1.setLabel("PAN 1");
        this.pan1.setFont(DelayComposite.this.labelFont);
        this.pan1.setRange(GmDelayEffect.panRange);
        this.add(this.pan1);
        this.pan1.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setPan1(DelayPane.this.pan1.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.pan2 = new JDial();
        this.pan2.setLabel("PAN 2");
        this.pan2.setFont(DelayComposite.this.labelFont);
        this.pan2.setRange(GmDelayEffect.panRange);
        this.add(this.pan2);
        this.pan2.addChangeListener(arg0 -> {
          ((DelayAlgorithm)((GmDelayEffect)DelayComposite.this.effect).getAlgorithm()).setPan2(DelayPane.this.pan2.getDialValue().toString());
          DelayComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }
    }

    class DualDelayPane extends DelayComposite.DelayPane {
      DualDelayPane() {
        super();
      }

      void initPanel() {
        super.initPanel();
        super.initDual();
      }

      void refresh(DualDelayAlgorithm algorithm) {
        super.refresh(algorithm);
        super.refreshDual(algorithm);
      }
    }

    class DynamicDelayPane extends DelayComposite.DelayPane {
      DynamicDelayPane() {
        super();
      }

      void initPanel() {
        super.initPanel();
        super.initDynamic();
      }

      void refresh(DynamicDelayAlgorithm algorithm) {
        super.refresh(algorithm);
        super.refreshDynamic(algorithm);
      }
    }

    class PingPongDelayPane extends DelayComposite.DelayPane {
      PingPongDelayPane() {
        super();
      }

      void initPanel() {
        super.initPanel();
        super.initPingPong();
      }

      void refresh(PingPongDelayAlgorithm algorithm) {
        super.refresh(algorithm);
        super.refreshPingPong(algorithm);
      }
    }
  }

  public static class FilterComposite extends MultiAlgorithmComposite {
    private FilterComposite.ResonanceFilterPane resFilterPane;
    private FilterComposite.AutoResonanceFilterPane autoFilterPane;
    private FilterComposite.PhaserPane phaserPane;
    private FilterComposite.TremoloPane tremoloPane;
    private FilterComposite.PannerPane pannerPane;

    FilterComposite() {
      this.algorithms.add(FILTER_ALGO_TYPE.AUTO_FILTER);
      this.algorithms.add(FILTER_ALGO_TYPE.RESONANCE_FILTER);
      this.algorithms.add(FILTER_ALGO_TYPE.VINTAGE_PHASER);
      this.algorithms.add(FILTER_ALGO_TYPE.SMOOTH_PHASER);
      this.algorithms.add(FILTER_ALGO_TYPE.TREMOLO);
      this.algorithms.add(FILTER_ALGO_TYPE.PANNER);
    }

    public void initPanel() {
      this.initTitleBar("FILTER/MOD");
      super.initPanel();
    }

    public void refresh(GmEffect pGmEffect) {
      super.refresh(pGmEffect);
      if (((GmFilterModEffect)pGmEffect).getAlgorithmType() == FILTER_ALGO_TYPE.RESONANCE_FILTER) {
        if (this.resFilterPane == null) {
          this.resFilterPane = new FilterComposite.ResonanceFilterPane();
          this.resFilterPane.initPanel();
        }

        this.algorithmControlPnl.add(this.resFilterPane);
        this.resFilterPane.layout(this.resFilterPane);
        this.resFilterPane.refresh((GmFilterAlgorithm)((GmFilterModEffect)pGmEffect).getAlgorithm());
      } else if (this.resFilterPane != null) {
        this.algorithmControlPnl.remove(this.resFilterPane);
      }

      if (((GmFilterModEffect)pGmEffect).getAlgorithmType() == FILTER_ALGO_TYPE.AUTO_FILTER) {
        if (this.autoFilterPane == null) {
          this.autoFilterPane = new FilterComposite.AutoResonanceFilterPane();
          this.autoFilterPane.initPanel();
        }

        this.algorithmControlPnl.add(this.autoFilterPane);
        this.autoFilterPane.layout(this.autoFilterPane);
        this.autoFilterPane.refresh((GmFilterAlgorithm)((GmFilterModEffect)pGmEffect).getAlgorithm());
      } else if (this.autoFilterPane != null) {
        this.algorithmControlPnl.remove(this.autoFilterPane);
      }

      if (((GmFilterModEffect)pGmEffect).getAlgorithmType() != FILTER_ALGO_TYPE.VINTAGE_PHASER && ((GmFilterModEffect)pGmEffect).getAlgorithmType() != FILTER_ALGO_TYPE.SMOOTH_PHASER) {
        if (this.phaserPane != null) {
          this.algorithmControlPnl.remove(this.phaserPane);
        }
      } else {
        if (this.phaserPane == null) {
          this.phaserPane = new FilterComposite.PhaserPane();
          this.phaserPane.initPanel();
        }

        this.algorithmControlPnl.add(this.phaserPane);
        this.phaserPane.layout(this.phaserPane);
        this.phaserPane.refresh((GmPhaserAlgorithm)((GmFilterModEffect)pGmEffect).getAlgorithm());
      }

      if (((GmFilterModEffect)pGmEffect).getAlgorithmType() == FILTER_ALGO_TYPE.TREMOLO) {
        if (this.tremoloPane == null) {
          this.tremoloPane = new FilterComposite.TremoloPane();
          this.tremoloPane.initPanel();
        }

        this.algorithmControlPnl.add(this.tremoloPane);
        this.tremoloPane.layout(this.tremoloPane);
        this.tremoloPane.refresh((GmTremoloAlgorithm)((GmFilterModEffect)pGmEffect).getAlgorithm());
      } else if (this.tremoloPane != null) {
        this.algorithmControlPnl.remove(this.tremoloPane);
      }

      if (((GmFilterModEffect)pGmEffect).getAlgorithmType() == FILTER_ALGO_TYPE.PANNER) {
        if (this.pannerPane == null) {
          this.pannerPane = new FilterComposite.PannerPane();
          this.pannerPane.initPanel();
        }

        this.algorithmControlPnl.add(this.pannerPane);
        this.pannerPane.layout(this.pannerPane);
        this.pannerPane.refresh((GmPannerAlgorithm)((GmFilterModEffect)pGmEffect).getAlgorithm());
      } else if (this.pannerPane != null) {
        this.algorithmControlPnl.remove(this.pannerPane);
      }

      this.updateUI();
      this.layout(this);
    }

    class AutoResonanceFilterPane extends JPanel {
      private JLedButton secondOrder;
      private JLedButton fourthOrder;
      private JPanel orderTypePnl;
      private JLedButton fastResponse;
      private JLedButton medResponse;
      private JLedButton slowResponse;
      private JPanel responseTypePnl;
      private JDial sensitivity;
      private JDial maxFreq;

      AutoResonanceFilterPane() {
      }

      void refresh(GmFilterAlgorithm algorithm) {
        this.secondOrder.setSelected(algorithm.getFilterOrder() == FILTER_ORDER.SECOND);
        this.fourthOrder.setSelected(algorithm.getFilterOrder() == FILTER_ORDER.FORTH);
        this.fastResponse.setSelected(algorithm.getAutoResResponse() == GmFilterAlgorithm.FILTER_RESPONSE.FAST);
        this.medResponse.setSelected(algorithm.getAutoResResponse() == GmFilterAlgorithm.FILTER_RESPONSE.MED);
        this.slowResponse.setSelected(algorithm.getAutoResResponse() == GmFilterAlgorithm.FILTER_RESPONSE.SLOW);
        this.sensitivity.setDialValue(algorithm.getAutoResSensitivity());
        this.maxFreq.setDialValue(algorithm.getAutoResFreqMax());
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.orderTypePnl = new ButtonGroupPanel("FILTER ORDER", FilterComposite.this.labelFont);
        this.add(this.orderTypePnl);
        ActionListener ordListener = e -> {
          if (AutoResonanceFilterPane.this.secondOrder.isSelected()) {
            ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setFilterOrder(FILTER_ORDER.SECOND);
          }

          if (AutoResonanceFilterPane.this.fourthOrder.isSelected()) {
            ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setFilterOrder(FILTER_ORDER.FORTH);
          }

          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.secondOrder = new JLedButton("2 Order");
        this.secondOrder.setOpaque(false);
        this.secondOrder.setFont(FilterComposite.this.labelFont);
        this.orderTypePnl.add(this.secondOrder);
        this.secondOrder.addActionListener(ordListener);
        this.fourthOrder = new JLedButton("4 Order");
        this.fourthOrder.setOpaque(false);
        this.fourthOrder.setFont(FilterComposite.this.labelFont);
        this.orderTypePnl.add(this.fourthOrder);
        this.fourthOrder.addActionListener(ordListener);
        ButtonGroup orderBtnGroup = new ButtonGroup();
        orderBtnGroup.add(this.secondOrder);
        orderBtnGroup.add(this.fourthOrder);
        this.responseTypePnl = new ButtonGroupPanel("FILTER RESPONSE", FilterComposite.this.labelFont);
        this.add(this.responseTypePnl);
        ActionListener responseListener = e -> {
          if (AutoResonanceFilterPane.this.fastResponse.isSelected()) {
            ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setAutoResResponse(FILTER_RESPONSE.FAST);
          }

          if (AutoResonanceFilterPane.this.medResponse.isSelected()) {
            ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setAutoResResponse(GmFilterAlgorithm.FILTER_RESPONSE.MED);
          }

          if (AutoResonanceFilterPane.this.slowResponse.isSelected()) {
            ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setAutoResResponse(GmFilterAlgorithm.FILTER_RESPONSE.SLOW);
          }

          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.fastResponse = new JLedButton("FAST");
        this.fastResponse.setOpaque(false);
        this.fastResponse.setFont(FilterComposite.this.labelFont);
        this.responseTypePnl.add(this.fastResponse);
        this.fastResponse.addActionListener(responseListener);
        this.medResponse = new JLedButton("MEDIUM");
        this.medResponse.setOpaque(false);
        this.medResponse.setFont(FilterComposite.this.labelFont);
        this.responseTypePnl.add(this.medResponse);
        this.medResponse.addActionListener(responseListener);
        this.slowResponse = new JLedButton("SLOW");
        this.slowResponse.setOpaque(false);
        this.slowResponse.setFont(FilterComposite.this.labelFont);
        this.responseTypePnl.add(this.slowResponse);
        this.slowResponse.addActionListener(responseListener);
        ButtonGroup resBtnGroup = new ButtonGroup();
        resBtnGroup.add(this.fastResponse);
        resBtnGroup.add(this.medResponse);
        resBtnGroup.add(this.slowResponse);
        this.sensitivity = new JDial();
        this.sensitivity.setLabel("SENSI");
        this.sensitivity.setFont(FilterComposite.this.labelFont);
        this.sensitivity.setRange(GmFilterAlgorithm.autoResSensitivityRange);
        this.add(this.sensitivity);
        this.sensitivity.addChangeListener(arg0 -> {
          ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setAutoResSensitivity(AutoResonanceFilterPane.this.sensitivity.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.maxFreq = new JDial();
        this.maxFreq.setLabel("MX:FREQ");
        this.maxFreq.setFont(FilterComposite.this.labelFont);
        this.maxFreq.setRange(HiCutFreqRange.getAutoResonanceMaxFreqInstance());
        this.add(this.maxFreq);
        this.maxFreq.addChangeListener(arg0 -> {
          ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setAutoResFreqMax(AutoResonanceFilterPane.this.maxFreq.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void layout(JPanel pnl) {
        SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
        Constraints thisCon = thisLayout.getConstraints(pnl);
        Constraints ordCon = thisLayout.getConstraints(this.orderTypePnl);
        Constraints resCon = thisLayout.getConstraints(this.responseTypePnl);
        Constraints senCon = thisLayout.getConstraints(this.sensitivity);
        Constraints mfqCon = thisLayout.getConstraints(this.maxFreq);
        int gap = 2;
        ordCon.setX(Spring.constant(gap));
        ordCon.setY(Spring.constant(gap));
        resCon.setX(Spring.sum(Spring.constant(gap), ordCon.getConstraint("East")));
        resCon.setY(Spring.constant(gap));
        senCon.setX(Spring.sum(Spring.constant(gap), resCon.getConstraint("East")));
        senCon.setY(Spring.constant(gap));
        mfqCon.setX(Spring.sum(Spring.constant(gap), senCon.getConstraint("East")));
        mfqCon.setY(Spring.constant(gap));
        thisCon.setWidth(Spring.sum(Spring.constant(gap * 5), Spring.sum(ordCon.getWidth(), Spring.sum(resCon.getWidth(), Spring.sum(senCon.getWidth(), mfqCon.getWidth())))));
        thisCon.setHeight(Spring.sum(Spring.constant(gap * 2), Spring.max(resCon.getHeight(), senCon.getHeight())));
        SpringLayout algoLayout = (SpringLayout)FilterComposite.this.algorithmControlPnl.getLayout();
        Constraints algoCon = algoLayout.getConstraints(FilterComposite.this.algorithmControlPnl);
        thisCon.setX(algoCon.getX());
        thisCon.setY(algoCon.getY());
        algoCon.setWidth(thisCon.getWidth());
        algoCon.setHeight(thisCon.getHeight());
      }
    }

    class PannerPane extends JPanel {
      private JDial speed;
      private JDial width;
      private JDial tempo;
      ModifierButton speedModifier;
      ModifierButton widthModifier;

      PannerPane() {
      }

      void refresh(GmPannerAlgorithm algorithm) {
        this.speed.setDialValue(algorithm.getSpeed());
        this.width.setDialValue(algorithm.getDepth());
        this.tempo.setDialValue(algorithm.getTempo());
        this.speedModifier.setModifierValue(algorithm.getMSpeedModifier());
        this.widthModifier.setModifierValue(algorithm.getMWidthModifier());
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.speedModifier = FilterComposite.this.createModifierControl();
        this.add(this.speedModifier);
        this.widthModifier = FilterComposite.this.createModifierControl();
        this.add(this.widthModifier);
        this.speed = FilterComposite.this.createDialControl("SPEED", SpeedRange.getSpeedInstance());
        this.add(this.speed);
        this.speed.addChangeListener(arg0 -> {
          ((GmPannerAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setSpeed(PannerPane.this.speed.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.width = FilterComposite.this.createDialControl("WIDTH", GmFilterModEffect.depthRange);
        this.add(this.width);
        this.width.addChangeListener(arg0 -> {
          ((GmPannerAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setDepth(PannerPane.this.width.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.tempo = FilterComposite.this.createDialControl("TEMPO", TapTempoRange.getInstance());
        this.add(this.tempo);
        this.tempo.addChangeListener(arg0 -> {
          ((GmPannerAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setTempo(PannerPane.this.tempo.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void layout(JPanel pnl) {
        componentLayoutStrategy layout = new componentLayoutStrategy(pnl);
        layout.addComponent(this.speed, this.width, null, null, this.speedModifier);
        layout.addComponent(this.speedModifier, this.width, null, this.speed, null);
        layout.addComponent(this.width, this.tempo, this.speed, null, this.widthModifier);
        layout.addComponent(this.widthModifier, this.tempo, this.speed, this.width, null);
        layout.addComponent(this.tempo, null, this.width, null, null);
        layout.layout();
      }
    }

    class PhaserPane extends JPanel {
      private JDial speed;
      private JDial depth;
      private JDial tempo;
      private JDial feedback;
      private JLedButton phaseReverse;
      private JLedButton lowRange;
      private JLedButton hiRange;
      private JPanel rangePnl;
      ModifierButton speedModifier;
      ModifierButton depthModifier;
      ModifierButton feedbackModifier;

      PhaserPane() {
      }

      void refresh(GmPhaserAlgorithm algorithm) {
        this.phaseReverse.setSelected(algorithm.isPhaseReverse());
        this.lowRange.setSelected(algorithm.getRange() == PHASER_RANGE.LOW);
        this.hiRange.setSelected(algorithm.getRange() == PHASER_RANGE.HI);
        this.speed.setDialValue(algorithm.getSpeed());
        this.depth.setDialValue(algorithm.getDepth());
        this.tempo.setDialValue(algorithm.getTempo());
        this.feedback.setDialValue(algorithm.getFeedback());
        this.speedModifier.setModifierValue(algorithm.getMSpeedModifier());
        this.depthModifier.setModifierValue(algorithm.getMDepthModifier());
        this.feedbackModifier.setModifierValue(algorithm.getMFbModifier());
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.speedModifier = FilterComposite.this.createModifierControl();
        this.add(this.speedModifier);
        this.depthModifier = FilterComposite.this.createModifierControl();
        this.add(this.depthModifier);
        this.feedbackModifier = FilterComposite.this.createModifierControl();
        this.add(this.feedbackModifier);
        this.phaseReverse = FilterComposite.createLedButton("PHASE REVERSE");
        this.add(this.phaseReverse);
        this.phaseReverse.addActionListener(e -> {
          ((GmPhaserAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setPhaseReverse(PhaserPane.this.phaseReverse.isSelected());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.rangePnl = new ButtonGroupPanel("PHASER RANGE", FilterComposite.this.labelFont);
        this.add(this.rangePnl);
        ActionListener rngListener = e -> {
          if (PhaserPane.this.lowRange.isSelected()) {
            ((GmPhaserAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setRange(PHASER_RANGE.LOW);
          }

          if (PhaserPane.this.hiRange.isSelected()) {
            ((GmPhaserAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setRange(PHASER_RANGE.HI);
          }

          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.lowRange = FilterComposite.createLedButton("LOW RANGE");
        this.rangePnl.add(this.lowRange);
        this.lowRange.addActionListener(rngListener);
        this.hiRange = FilterComposite.createLedButton("HI RANGE");
        this.rangePnl.add(this.hiRange);
        this.hiRange.addActionListener(rngListener);
        ButtonGroup orderBtnGroup = new ButtonGroup();
        orderBtnGroup.add(this.lowRange);
        orderBtnGroup.add(this.hiRange);
        this.speed = FilterComposite.this.createDialControl("SPEED", SpeedRange.getSpeedInstance());
        this.add(this.speed);
        this.speed.addChangeListener(arg0 -> {
          ((GmPhaserAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setSpeed(PhaserPane.this.speed.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.depth = FilterComposite.this.createDialControl("DEPTH", GmFilterModEffect.depthRange);
        this.add(this.depth);
        this.depth.addChangeListener(arg0 -> {
          ((GmPhaserAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setDepth(PhaserPane.this.depth.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.tempo = FilterComposite.this.createDialControl("TEMPO", TapTempoRange.getInstance());
        this.add(this.tempo);
        this.tempo.addChangeListener(arg0 -> {
          ((GmPhaserAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setTempo(PhaserPane.this.tempo.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.feedback = FilterComposite.this.createDialControl("FB", GmMultiAlgorithmEffect.feedbackRange);
        this.add(this.feedback);
        this.feedback.addChangeListener(arg0 -> {
          ((GmPhaserAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setFeedBack(PhaserPane.this.feedback.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void layout(JPanel pnl) {
        componentLayoutStrategy layout = new componentLayoutStrategy(pnl);
        layout.addComponent(this.speed, this.depth, null, null, this.speedModifier);
        layout.addComponent(this.speedModifier, this.depthModifier, null, this.speed, null);
        layout.addComponent(this.depth, this.tempo, this.speed, null, this.depthModifier);
        layout.addComponent(this.depthModifier, this.tempo, this.speedModifier, this.depth, null);
        layout.addComponent(this.tempo, this.feedback, this.depth, null, null);
        layout.addComponent(this.feedback, this.rangePnl, this.tempo, null, this.feedbackModifier);
        layout.addComponent(this.feedbackModifier, this.rangePnl, this.tempo, this.feedback, null);
        layout.addComponent(this.rangePnl, null, this.feedback, null, this.phaseReverse);
        layout.addComponent(this.phaseReverse, null, this.feedbackModifier, this.rangePnl, null);
        layout.layout();
      }
    }

    class ResonanceFilterPane extends JPanel {
      private JLedButton secondOrder;
      private JLedButton fourthOrder;
      private JPanel orderTypePnl;
      private JDial hiCut;
      private JDial hiResonance;
      ModifierButton hiCutModifier;
      ModifierButton resonanceModifier;

      ResonanceFilterPane() {
      }

      void refresh(GmFilterAlgorithm algorithm) {
        this.secondOrder.setSelected(algorithm.getFilterOrder() == FILTER_ORDER.SECOND);
        this.fourthOrder.setSelected(algorithm.getFilterOrder() == FILTER_ORDER.FORTH);
        if (algorithm.getType() == FILTER_ALGO_TYPE.RESONANCE_FILTER) {
          this.hiCut.setDialValue(algorithm.getResHiCut());
          this.hiResonance.setDialValue(algorithm.getResHiResonance());
          this.hiCutModifier.setModifierValue(algorithm.getMResModifier());
          this.resonanceModifier.setModifierValue(algorithm.getMHiCutModifier());
        }

      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.orderTypePnl = new ButtonGroupPanel( "FILTER ORDER", FilterComposite.this.labelFont);
        this.add(this.orderTypePnl);
        ActionListener ordListener = e -> {
          if (ResonanceFilterPane.this.secondOrder.isSelected()) {
            ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setFilterOrder(FILTER_ORDER.SECOND);
          }

          if (ResonanceFilterPane.this.fourthOrder.isSelected()) {
            ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setFilterOrder(FILTER_ORDER.FORTH);
          }

          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.secondOrder = new JLedButton("2 Order");
        this.secondOrder.setOpaque(false);
        this.secondOrder.setFont(FilterComposite.this.labelFont);
        this.orderTypePnl.add(this.secondOrder);
        this.secondOrder.addActionListener(ordListener);
        this.fourthOrder = new JLedButton("4 Order");
        this.fourthOrder.setOpaque(false);
        this.fourthOrder.setFont(FilterComposite.this.labelFont);
        this.orderTypePnl.add(this.fourthOrder);
        this.fourthOrder.addActionListener(ordListener);
        ButtonGroup orderBtnGroup = new ButtonGroup();
        orderBtnGroup.add(this.secondOrder);
        orderBtnGroup.add(this.fourthOrder);
        this.hiCut = new JDial();
        this.hiCut.setLabel("HI CUT");
        this.hiCut.setFont(FilterComposite.this.labelFont);
        this.hiCut.setRange(HiCutFreqRange.getResonanceHiCutInstance());
        this.add(this.hiCut);
        this.hiCut.addChangeListener(arg0 -> {
          ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setResHiCut(ResonanceFilterPane.this.hiCut.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.hiResonance = new JDial();
        this.hiResonance.setLabel("HI RES");
        this.hiResonance.setFont(FilterComposite.this.labelFont);
        this.hiResonance.setRange(GmFilterAlgorithm.resHiResonanceRange);
        this.add(this.hiResonance);
        this.hiResonance.addChangeListener(arg0 -> {
          ((GmFilterAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setResHiResonance(ResonanceFilterPane.this.hiResonance.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.hiCutModifier = new ModifierButton();
        this.hiCutModifier.setOpaque(false);
        this.hiCutModifier.setFont(FilterComposite.this.labelFont);
        this.add(this.hiCutModifier);
        this.resonanceModifier = new ModifierButton();
        this.resonanceModifier.setOpaque(false);
        this.resonanceModifier.setFont(FilterComposite.this.labelFont);
        this.add(this.resonanceModifier);
      }

      void layout(JPanel pnl) {
        SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
        Constraints thisCon = thisLayout.getConstraints(pnl);
        Constraints ordCon = thisLayout.getConstraints(this.orderTypePnl);
        Constraints hcCon = thisLayout.getConstraints(this.hiCut);
        Constraints hcModCon = thisLayout.getConstraints(this.hiCutModifier);
        Constraints hrCon = thisLayout.getConstraints(this.hiResonance);
        Constraints hrModCon = thisLayout.getConstraints(this.resonanceModifier);
        int gap = 2;
        ordCon.setX(Spring.constant(gap));
        ordCon.setY(Spring.constant(gap));
        hcCon.setX(Spring.sum(Spring.constant(gap), ordCon.getConstraint("East")));
        hcCon.setY(Spring.constant(gap));
        hcModCon.setX(Spring.sum(Spring.constant(gap), ordCon.getConstraint("East")));
        hcModCon.setY(hcCon.getConstraint("South"));
        hrCon.setX(Spring.sum(Spring.constant(gap), hcCon.getConstraint("East")));
        hrCon.setY(Spring.constant(gap));
        hrModCon.setX(Spring.sum(Spring.constant(gap), hcCon.getConstraint("East")));
        hrModCon.setY(hrCon.getConstraint("South"));
        thisCon.setWidth(Spring.sum(Spring.constant(gap * 4), Spring.sum(ordCon.getWidth(), Spring.sum(Spring.max(hcCon.getWidth(), hcModCon.getWidth()), Spring.max(hrCon.getWidth(), hrModCon.getWidth())))));
        thisCon.setHeight(Spring.sum(Spring.constant(gap * 2), Spring.sum(hrCon.getHeight(), hrModCon.getHeight())));
        SpringLayout algoLayout = (SpringLayout)FilterComposite.this.algorithmControlPnl.getLayout();
        Constraints algoCon = algoLayout.getConstraints(FilterComposite.this.algorithmControlPnl);
        thisCon.setX(algoCon.getX());
        thisCon.setY(algoCon.getY());
        algoCon.setWidth(thisCon.getWidth());
        algoCon.setHeight(thisCon.getHeight());
      }
    }

    class TremoloPane extends JPanel {
      private JDial speed;
      private JDial depth;
      private JDial tempo;
      private JDial lfoPulseWidth;
      private JDial hiCut;
      private JLedButton softTrem;
      private JLedButton hardtrem;
      private JPanel tremTypePnl;
      ModifierButton speedModifier;
      ModifierButton depthModifier;
      ModifierButton hiCutModifier;

      TremoloPane() {
      }

      void refresh(GmTremoloAlgorithm algorithm) {
        this.softTrem.setSelected(algorithm.getTremoloType() == TREMOLO_TYPE.SOFT);
        this.hardtrem.setSelected(algorithm.getTremoloType() == TREMOLO_TYPE.HARD);
        this.speed.setDialValue(algorithm.getSpeed());
        this.depth.setDialValue(algorithm.getDepth());
        this.tempo.setDialValue(algorithm.getTempo());
        this.lfoPulseWidth.setDialValue(algorithm.getLfoPulseWidth());
        this.hiCut.setDialValue(algorithm.getHiCut());
        this.speedModifier.setModifierValue(algorithm.getMSpeedModifier());
        this.depthModifier.setModifierValue(algorithm.getMDepthModifier());
        this.hiCutModifier.setModifierValue(algorithm.getMHiCutModifier());
      }

      void layout(JPanel pnl) {
        componentLayoutStrategy layout = new componentLayoutStrategy(pnl);
        layout.addComponent(this.tremTypePnl, this.speed, null, null, null);
        layout.addComponent(this.speed, this.depth, this.tremTypePnl, null, this.speedModifier);
        layout.addComponent(this.speedModifier, this.depth, this.tremTypePnl, this.speed, null);
        layout.addComponent(this.depth, this.tempo, this.speed, null, this.depthModifier);
        layout.addComponent(this.depthModifier, this.tempo, this.speed, this.depth, null);
        layout.addComponent(this.tempo, this.lfoPulseWidth, this.depth, null, null);
        layout.addComponent(this.lfoPulseWidth, this.hiCut, this.tempo, null, null);
        layout.addComponent(this.hiCut, null, this.lfoPulseWidth, null, this.hiCutModifier);
        layout.addComponent(this.hiCutModifier, null, this.lfoPulseWidth, this.hiCut, null);
        layout.layout();
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.speedModifier = FilterComposite.this.createModifierControl();
        this.add(this.speedModifier);
        this.depthModifier = FilterComposite.this.createModifierControl();
        this.add(this.depthModifier);
        this.hiCutModifier = FilterComposite.this.createModifierControl();
        this.add(this.hiCutModifier);
        this.tremTypePnl = new ButtonGroupPanel( "TREM TYPE", FilterComposite.this.labelFont);
        this.add(this.tremTypePnl);
        ActionListener tremTypListener = e -> {
          if (TremoloPane.this.softTrem.isSelected()) {
            ((GmTremoloAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setTremoloType(TREMOLO_TYPE.SOFT);
          }

          if (TremoloPane.this.hardtrem.isSelected()) {
            ((GmTremoloAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setTremoloType(TREMOLO_TYPE.HARD);
          }

          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.softTrem = FilterComposite.createLedButton("SOFT");
        this.tremTypePnl.add(this.softTrem);
        this.softTrem.addActionListener(tremTypListener);
        this.hardtrem = FilterComposite.createLedButton("HARD");
        this.tremTypePnl.add(this.hardtrem);
        this.hardtrem.addActionListener(tremTypListener);
        ButtonGroup orderBtnGroup = new ButtonGroup();
        orderBtnGroup.add(this.softTrem);
        orderBtnGroup.add(this.hardtrem);
        this.speed = FilterComposite.this.createDialControl("SPEED", SpeedRange.getSpeedInstance());
        this.add(this.speed);
        this.speed.addChangeListener(arg0 -> {
          ((GmTremoloAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setSpeed(TremoloPane.this.speed.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.depth = FilterComposite.this.createDialControl("DEPTH", GmFilterModEffect.depthRange);
        this.add(this.depth);
        this.depth.addChangeListener(arg0 -> {
          ((GmTremoloAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setDepth(TremoloPane.this.depth.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.tempo = FilterComposite.this.createDialControl("TEMPO", TapTempoRange.getInstance());
        this.add(this.tempo);
        this.tempo.addChangeListener(arg0 -> {
          ((GmTremoloAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setTempo(TremoloPane.this.tempo.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.lfoPulseWidth = FilterComposite.this.createDialControl("LFO WIDTH", GmFilterModEffect.depthRange);
        this.add(this.lfoPulseWidth);
        this.lfoPulseWidth.addChangeListener(arg0 -> {
          ((GmTremoloAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setLfoPulseWidth(TremoloPane.this.lfoPulseWidth.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.hiCut = FilterComposite.this.createDialControl("HI CUT", HiCutFreqRange.getFullHiCutInstance());
        this.add(this.hiCut);
        this.hiCut.addChangeListener(arg0 -> {
          ((GmTremoloAlgorithm)((GmFilterModEffect)FilterComposite.this.effect).getAlgorithm()).setHiCut(TremoloPane.this.hiCut.getDialValue().toString());
          FilterComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }
    }
  }

  public static class ModulationComposite extends MultiAlgorithmComposite {
    private ClassicChorusPane claChorusPane;
    private ModulationComposite.AdvanceChorusPane advChorusPane;
    private ClassicFlangePane claFlangePane;
    private ModulationComposite.AdvanceFlangePane advFlangePane;
    private ModulationComposite.VibratoPane vibratoPane;

    public ModulationComposite() {
      this.algorithms.add(MODULATION_ALGO_TYPE.CLA_CHO);
      this.algorithms.add(MODULATION_ALGO_TYPE.ADV_CHO);
      this.algorithms.add(MODULATION_ALGO_TYPE.CLA_FLA);
      this.algorithms.add(MODULATION_ALGO_TYPE.ADV_FLA);
      this.algorithms.add(MODULATION_ALGO_TYPE.VIB);
    }

    public void initPanel() {
      this.initTitleBar("CHORUS/FLANGE");
      super.initPanel();
    }

    public void refresh(GmEffect pGmEffect) {
      super.refresh(pGmEffect);
      if (((GmModulationEffect)pGmEffect).getAlgorithmType() == MODULATION_ALGO_TYPE.CLA_CHO) {
        if (this.claChorusPane == null) {
          this.claChorusPane = new ClassicChorusPane();
          this.claChorusPane.initPanel();
        }

        this.algorithmControlPnl.add(this.claChorusPane);
        this.claChorusPane.layout(this.claChorusPane);
        this.claChorusPane.refresh((GmClassicChorusAlgorithm)((GmModulationEffect)pGmEffect).getAlgorithm());
      } else if (this.claChorusPane != null) {
        this.algorithmControlPnl.remove(this.claChorusPane);
      }

      if (((GmModulationEffect)pGmEffect).getAlgorithmType() == MODULATION_ALGO_TYPE.ADV_CHO) {
        if (this.advChorusPane == null) {
          this.advChorusPane = new ModulationComposite.AdvanceChorusPane();
          this.advChorusPane.initPanel();
        }

        this.algorithmControlPnl.add(this.advChorusPane);
        this.advChorusPane.layout(this.advChorusPane);
        this.advChorusPane.refresh((GmAdvanceChorusAlgorithm)((GmModulationEffect)pGmEffect).getAlgorithm());
      } else if (this.advChorusPane != null) {
        this.algorithmControlPnl.remove(this.advChorusPane);
      }

      if (((GmModulationEffect)pGmEffect).getAlgorithmType() == MODULATION_ALGO_TYPE.CLA_FLA) {
        if (this.claFlangePane == null) {
          this.claFlangePane = new ClassicFlangePane();
          this.claFlangePane.initPanel();
        }

        this.algorithmControlPnl.add(this.claFlangePane);
        this.claFlangePane.layout(this.claFlangePane);
        this.claFlangePane.refresh((GmClassicFlangAlgorithm)((GmModulationEffect)pGmEffect).getAlgorithm());
      } else if (this.claFlangePane != null) {
        this.algorithmControlPnl.remove(this.claFlangePane);
      }

      if (((GmModulationEffect)pGmEffect).getAlgorithmType() == MODULATION_ALGO_TYPE.ADV_FLA) {
        if (this.advFlangePane == null) {
          this.advFlangePane = new ModulationComposite.AdvanceFlangePane();
          this.advFlangePane.initPanel();
        }

        this.algorithmControlPnl.add(this.advFlangePane);
        this.advFlangePane.layout(this.advFlangePane);
        this.advFlangePane.refresh((GmAdvanceFlangAlgorithm)((GmModulationEffect)pGmEffect).getAlgorithm());
      } else if (this.advFlangePane != null) {
        this.algorithmControlPnl.remove(this.advFlangePane);
      }

      if (((GmModulationEffect)pGmEffect).getAlgorithmType() == MODULATION_ALGO_TYPE.VIB) {
        if (this.vibratoPane == null) {
          this.vibratoPane = new ModulationComposite.VibratoPane();
          this.vibratoPane.initPanel();
        }

        this.algorithmControlPnl.add(this.vibratoPane);
        this.vibratoPane.layout(this.vibratoPane);
        this.vibratoPane.refresh((GmVibratoAlgorithm)((GmModulationEffect)pGmEffect).getAlgorithm());
      } else if (this.vibratoPane != null) {
        this.algorithmControlPnl.remove(this.vibratoPane);
      }

      this.updateUI();
      this.layout(this);
    }

    class AdvanceChorusPane extends ModulationComposite.ModulationPane {
      AdvanceChorusPane() {
        super();
      }

      void initPanel() {
        this.setLayout(new SpringLayout());
        super.initPanel();
        super.initAdvance();
      }

      void refresh(ModulationAlgorithm algorithm) {
        super.refresh(algorithm);
        super.refreshAdvance(algorithm);
      }
    }

    class AdvanceFlangePane extends ModulationComposite.ModulationPane {
      AdvanceFlangePane() {
        super();
      }

      void initPanel() {
        this.setLayout(new SpringLayout());
        super.initPanel();
        super.initFlange();
        super.initAdvance();
      }

      void refresh(ModulationAlgorithm algorithm) {
        super.refresh(algorithm);
        super.refreshFlange(algorithm);
        super.refreshAdvance(algorithm);
      }
    }

    class ClassicChorusPane extends ModulationComposite.ModulationPane {
      ClassicChorusPane() {
        super();
      }

      void initPanel() {
        this.setLayout(new SpringLayout());
        super.initPanel();
      }

    }

    class ClassicFlangePane extends ModulationComposite.ModulationPane {
      ClassicFlangePane() {
        super();
      }

      void initPanel() {
        this.setLayout(new SpringLayout());
        super.initPanel();
        super.initFlange();
      }

      void refresh(ModulationAlgorithm algorithm) {
        super.refresh(algorithm);
        super.refreshFlange(algorithm);
      }
    }

    abstract class ModulationPane extends JPanel {
      JDial speed;
      JDial depth;
      JDial tempo;
      JDial hiCut;
      ModifierButton speedModifier;
      ModifierButton depthModifier;
      ModifierButton hiCutModifier;
      JDial delay;
      JLedButton goldenRatio;
      JLedButton phaseReverse;
      JPanel advanceBtnPnl;
      JDial feedback;
      JDial feedbackHiCut;
      ModifierButton fbModifier;
      ModifierButton fbHiCutModifier;
      boolean hasAdvanceCtrls = false;
      boolean hasFlangCtrls = false;

      ModulationPane() {
      }

      void refresh(ModulationAlgorithm algorithm) {
        this.speed.setDialValue(algorithm.getSpeed());
        this.depth.setDialValue(algorithm.getDepth());
        this.tempo.setDialValue(algorithm.getTempo());
        this.hiCut.setDialValue(algorithm.getHiCut());
        this.speedModifier.setModifierValue(algorithm.getSpeedModifier());
        this.depthModifier.setModifierValue(algorithm.getDepthModifier());
        this.hiCutModifier.setModifierValue(algorithm.getHiCutModifier());
      }

      void refreshAdvance(ModulationAlgorithm algorithm) {
        this.delay.setDialValue(algorithm.getDelay());
        this.goldenRatio.setSelected(algorithm.getGoldenRatio() == ONOFF_VALUE.ON);
        this.phaseReverse.setSelected(algorithm.getPhaseReverse() == ONOFF_VALUE.ON);
      }

      void refreshFlange(ModulationAlgorithm algorithm) {
        this.feedback.setDialValue(algorithm.getFeedback());
        this.feedbackHiCut.setDialValue(algorithm.getFeedbackHiCut());
        this.fbModifier.setModifierValue(algorithm.getFbModifier());
        this.fbHiCutModifier.setModifierValue(algorithm.getFbCutModifier());
      }

      void initPanel() {
        this.setOpaque(false);
        this.speedModifier = ModulationComposite.this.createModifierControl();
        this.add(this.speedModifier);
        this.depthModifier = ModulationComposite.this.createModifierControl();
        this.add(this.depthModifier);
        this.hiCutModifier = ModulationComposite.this.createModifierControl();
        this.add(this.hiCutModifier);
        this.speed = ModulationComposite.this.createDialControl("SPEED", SpeedRange.getSpeedInstance());
        this.add(this.speed);
        this.speed.addChangeListener(arg0 -> {
          ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setSpeed(ModulationPane.this.speed.getDialValue().toString());
          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.depth = ModulationComposite.this.createDialControl("DEPTH", GmMultiAlgorithmEffect.depthRange);
        this.add(this.depth);
        this.depth.addChangeListener(arg0 -> {
          ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setDepth(ModulationPane.this.depth.getDialValue().toString());
          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.tempo = ModulationComposite.this.createDialControl("TEMPO", TapTempoRange.getInstance());
        this.add(this.tempo);
        this.tempo.addChangeListener(arg0 -> {
          ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setTempo(ModulationPane.this.tempo.getDialValue().toString());
          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.hiCut = ModulationComposite.this.createDialControl("HI CUT", HiCutFreqRange.getFullHiCutInstance());
        this.add(this.hiCut);
        this.hiCut.addChangeListener(arg0 -> {
          ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setHiCut(ModulationPane.this.hiCut.getDialValue().toString());
          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void initFlange() {
        this.hasFlangCtrls = true;
        this.fbModifier = ModulationComposite.this.createModifierControl();
        this.add(this.fbModifier);
        this.fbHiCutModifier = ModulationComposite.this.createModifierControl();
        this.add(this.fbHiCutModifier);
        this.feedback = ModulationComposite.this.createDialControl("FEEDBACK", GmMultiAlgorithmEffect.feedbackRange);
        this.add(this.feedback);
        this.feedback.addChangeListener(arg0 -> {
          ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setFeedback(ModulationPane.this.feedback.getDialValue().toString());
          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.feedbackHiCut = ModulationComposite.this.createDialControl("FB HICUT", HiCutFreqRange.getFullHiCutInstance());
        this.add(this.feedbackHiCut);
        this.feedbackHiCut.addChangeListener(arg0 -> {
          ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setFeedbackHiCut(ModulationPane.this.feedbackHiCut.getDialValue().toString());
          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void initAdvance() {
        this.hasAdvanceCtrls = true;
        this.delay = ModulationComposite.this.createDialControl("DELAY", GmMultiAlgorithmEffect.shortDelayRange);
        this.add(this.delay);
        this.delay.addChangeListener(arg0 -> {
          ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setDelay(ModulationPane.this.delay.getDialValue().toString());
          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.advanceBtnPnl = new ButtonGroupPanel( " ", ModulationComposite.this.labelFont);
        this.add(this.advanceBtnPnl);
        this.goldenRatio = ModulationComposite.createLedButton("GOLDEN RATIO");
        this.advanceBtnPnl.add(this.goldenRatio);
        this.goldenRatio.addActionListener(e -> {
          if (ModulationPane.this.goldenRatio.isSelected()) {
            ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setGoldenRatio(ONOFF_VALUE.ON);
          } else {
            ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setGoldenRatio(ONOFF_VALUE.OFF);
          }

          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.phaseReverse = ModulationComposite.createLedButton("PHASE REVERSE");
        this.advanceBtnPnl.add(this.phaseReverse);
        this.phaseReverse.addActionListener(e -> {
          if (ModulationPane.this.phaseReverse.isSelected()) {
            ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setPhaseReverse(ONOFF_VALUE.ON);
          } else {
            ((ModulationAlgorithm)((GmModulationEffect)ModulationComposite.this.effect).getAlgorithm()).setPhaseReverse(ONOFF_VALUE.OFF);
          }

          ModulationComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void layout(JPanel pnl) {
        componentLayoutStrategy layout = new componentLayoutStrategy( pnl);
        layout.addComponent(this.speed, this.depth, null, null, this.speedModifier);
        layout.addComponent(this.speedModifier, this.depth, null, this.speed, null);
        layout.addComponent(this.depth, this.tempo, this.speed, null, this.depthModifier);
        layout.addComponent(this.depthModifier, this.tempo, this.speed, this.depth, null);
        layout.addComponent(this.tempo, this.hiCut, this.depth, null, null);
        if (!this.hasAdvanceCtrls && !this.hasFlangCtrls) {
          layout.addComponent(this.hiCut, null, this.tempo, null, this.hiCutModifier);
          layout.addComponent(this.hiCutModifier, null, this.tempo, this.hiCut, null);
        } else if (!this.hasAdvanceCtrls) {
          layout.addComponent(this.hiCut, this.feedback, this.tempo, null, this.hiCutModifier);
          layout.addComponent(this.hiCutModifier, this.feedback, this.tempo, this.hiCut, null);
          layout.addComponent(this.feedback, this.feedbackHiCut, this.hiCut, null, this.fbModifier);
          layout.addComponent(this.fbModifier, this.feedbackHiCut, this.hiCut, this.feedback, null);
          layout.addComponent(this.feedbackHiCut, null, this.feedback, null, this.fbHiCutModifier);
          layout.addComponent(this.fbHiCutModifier, null, this.feedback, this.feedbackHiCut, null);
        } else if (!this.hasFlangCtrls) {
          layout.addComponent(this.hiCut, this.delay, this.tempo, null, this.hiCutModifier);
          layout.addComponent(this.hiCutModifier, this.delay, this.tempo, this.hiCut, null);
          layout.addComponent(this.delay, this.advanceBtnPnl, this.hiCut, null, null);
          layout.addComponent(this.advanceBtnPnl, null, this.delay, null, null);
        } else {
          layout.addComponent(this.hiCut, this.delay, this.tempo, null, this.hiCutModifier);
          layout.addComponent(this.hiCutModifier, this.delay, this.tempo, this.hiCut, null);
          layout.addComponent(this.delay, this.advanceBtnPnl, this.hiCut, null, null);
          layout.addComponent(this.advanceBtnPnl, this.feedback, this.delay, null, null);
          layout.addComponent(this.feedback, this.feedbackHiCut, this.advanceBtnPnl, null, this.fbModifier);
          layout.addComponent(this.fbModifier, this.feedbackHiCut, this.advanceBtnPnl, this.feedback, null);
          layout.addComponent(this.feedbackHiCut, null, this.feedback, null, this.fbHiCutModifier);
          layout.addComponent(this.fbHiCutModifier, null, this.feedback, this.feedbackHiCut, null);
        }

        layout.layout();
        ModulationComposite.this.packPanel(pnl);
      }
    }

    class VibratoPane extends ModulationComposite.ModulationPane {
      VibratoPane() {
        super();
      }

      void initPanel() {
        this.setLayout(new SpringLayout());
        super.initPanel();
      }

    }
  }

  public static class PitchComposite extends MultiAlgorithmComposite {
    private PitchComposite.DetunePane detunePane;
    private PitchComposite.WhammyPane whammyPane;
    private PitchComposite.OctaverPane octaverPane;
    private PitchComposite.ShifterPane shifterPane;

    public PitchComposite() {
      this.algorithms.add(PITCH_ALGO_TYPE.DETUNE);
      this.algorithms.add(PITCH_ALGO_TYPE.SHIFTER);
      this.algorithms.add(PITCH_ALGO_TYPE.WHAMMY);
      this.algorithms.add(PITCH_ALGO_TYPE.OCTAVER);
    }

    public void initPanel() {
      this.initTitleBar("PITCH");
      super.initPanel();
    }

    public void refresh(GmEffect pGmEffect) {
      super.refresh(pGmEffect);
      if (((GmPitchEffect)pGmEffect).getAlgorithmType() == PITCH_ALGO_TYPE.DETUNE) {
        if (this.detunePane == null) {
          this.detunePane = new PitchComposite.DetunePane();
          this.detunePane.initPanel();
        }

        this.algorithmControlPnl.add(this.detunePane);
        this.detunePane.layout(this.detunePane);
        this.detunePane.refresh((GmDetuneAlgorithm)((GmPitchEffect)pGmEffect).getAlgorithm());
      } else if (this.detunePane != null) {
        this.algorithmControlPnl.remove(this.detunePane);
      }

      if (((GmPitchEffect)pGmEffect).getAlgorithmType() == PITCH_ALGO_TYPE.WHAMMY) {
        if (this.whammyPane == null) {
          this.whammyPane = new PitchComposite.WhammyPane();
          this.whammyPane.initPanel();
        }

        this.algorithmControlPnl.add(this.whammyPane);
        this.whammyPane.layout(this.whammyPane);
        this.whammyPane.refresh((GmWhammyAlgorithm)((GmPitchEffect)pGmEffect).getAlgorithm());
      } else if (this.whammyPane != null) {
        this.algorithmControlPnl.remove(this.whammyPane);
      }

      if (((GmPitchEffect)pGmEffect).getAlgorithmType() == PITCH_ALGO_TYPE.OCTAVER) {
        if (this.octaverPane == null) {
          this.octaverPane = new PitchComposite.OctaverPane();
          this.octaverPane.initPanel();
        }

        this.algorithmControlPnl.add(this.octaverPane);
        this.octaverPane.layout(this.octaverPane);
        this.octaverPane.refresh((GmOctaverAlgorithm)((GmPitchEffect)pGmEffect).getAlgorithm());
      } else if (this.octaverPane != null) {
        this.algorithmControlPnl.remove(this.octaverPane);
      }

      if (((GmPitchEffect)pGmEffect).getAlgorithmType() == PITCH_ALGO_TYPE.SHIFTER) {
        if (this.shifterPane == null) {
          this.shifterPane = new PitchComposite.ShifterPane();
          this.shifterPane.initPanel();
        }

        this.algorithmControlPnl.add(this.shifterPane);
        this.shifterPane.layout(this.shifterPane);
        this.shifterPane.refresh((GmShifterAlgorithm)((GmPitchEffect)pGmEffect).getAlgorithm());
      } else if (this.shifterPane != null) {
        this.algorithmControlPnl.remove(this.shifterPane);
      }

      this.updateUI();
      this.layout(this);
    }

    class DetunePane extends JPanel {
      private JDial voiceLeft;
      private JDial voiceRight;
      private JDial delayLeft;
      private JDial delayRight;

      DetunePane() {
      }

      void refresh(GmDetuneAlgorithm algorithm) {
        this.voiceLeft.setDialValue(algorithm.getVoiceL());
        this.voiceRight.setDialValue(algorithm.getVoiceR());
        this.delayLeft.setDialValue(algorithm.getDelayL());
        this.delayRight.setDialValue(algorithm.getDelayR());
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.voiceLeft = new JDial();
        this.voiceLeft.setLabel("VOICE L");
        this.voiceLeft.setFont(PitchComposite.this.labelFont);
        this.voiceLeft.setRange(GmDetuneAlgorithm.detuneVoiceRange);
        this.add(this.voiceLeft);
        this.voiceLeft.addChangeListener(arg0 -> {
          ((GmDetuneAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setVoiceL(DetunePane.this.voiceLeft.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.voiceRight = new JDial();
        this.voiceRight.setLabel("VOICE R");
        this.voiceRight.setFont(PitchComposite.this.labelFont);
        this.voiceRight.setRange(GmDetuneAlgorithm.detuneVoiceRange);
        this.add(this.voiceRight);
        this.voiceRight.addChangeListener(arg0 -> {
          ((GmDetuneAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setVoiceR(DetunePane.this.voiceRight.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.delayLeft = new JDial();
        this.delayLeft.setLabel("DELAY L");
        this.delayLeft.setFont(PitchComposite.this.labelFont);
        this.delayLeft.setRange(GmDetuneAlgorithm.detuneDelayRange);
        this.add(this.delayLeft);
        this.delayLeft.addChangeListener(arg0 -> {
          ((GmDetuneAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setDelayL(DetunePane.this.delayLeft.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.delayRight = new JDial();
        this.delayRight.setLabel("DELAY R");
        this.delayRight.setFont(PitchComposite.this.labelFont);
        this.delayRight.setRange(GmDetuneAlgorithm.detuneDelayRange);
        this.add(this.delayRight);
        this.delayRight.addChangeListener(arg0 -> {
          ((GmDetuneAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setDelayR(DetunePane.this.delayRight.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void layout(JPanel pnl) {
        SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
        Constraints thisCon = thisLayout.getConstraints(pnl);
        Constraints soCon = thisLayout.getConstraints(this.voiceLeft);
        Constraints foCon = thisLayout.getConstraints(this.voiceRight);
        Constraints hcCon = thisLayout.getConstraints(this.delayLeft);
        Constraints hrCon = thisLayout.getConstraints(this.delayRight);
        int gap = 3;
        soCon.setX(Spring.constant(gap));
        soCon.setY(Spring.constant(gap));
        foCon.setX(Spring.sum(Spring.constant(gap), soCon.getConstraint("East")));
        foCon.setY(Spring.constant(gap));
        hcCon.setX(Spring.sum(Spring.constant(gap), foCon.getConstraint("East")));
        hcCon.setY(Spring.constant(gap));
        hrCon.setX(Spring.sum(Spring.constant(gap), hcCon.getConstraint("East")));
        hrCon.setY(Spring.constant(gap));
        thisCon.setWidth(Spring.sum(Spring.constant(gap * 5), Spring.sum(soCon.getWidth(), Spring.sum(foCon.getWidth(), Spring.sum(hcCon.getWidth(), hrCon.getWidth())))));
        thisCon.setHeight(Spring.sum(Spring.constant(gap * 2), hrCon.getHeight()));
        PitchComposite.this.packPanel(pnl);
      }
    }

    class OctaverPane extends JPanel {
      private JLedButton directionUp;
      private JLedButton directionDown;
      private JPanel directionPnl;
      private JLedButton range1Oct;
      private JLedButton range2Oct;
      private JPanel rangePnl;

      OctaverPane() {
      }

      void refresh(GmOctaverAlgorithm algorithm) {
        this.directionUp.setSelected(algorithm.getDirection() == PITCH_DIRECTION.UP);
        this.directionDown.setSelected(algorithm.getDirection() == PITCH_DIRECTION.DOWN);
        this.range1Oct.setSelected(algorithm.getOctRange() == OCTAVE_RANGE.ONE_OCT);
        this.range2Oct.setSelected(algorithm.getOctRange() == OCTAVE_RANGE.TWO_OCT);
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.directionPnl = new ButtonGroupPanel( "DIRECTION ", PitchComposite.this.labelFont);
        ActionListener actionListener = actionEvent -> {
          if (OctaverPane.this.directionUp.isSelected()) {
            ((GmOctaverAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setDirection(PITCH_DIRECTION.UP);
          }

          if (OctaverPane.this.directionDown.isSelected()) {
            ((GmOctaverAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setDirection(PITCH_DIRECTION.DOWN);
          }

          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.add(this.directionPnl);
        this.directionUp = new JLedButton("UP");
        this.directionUp.setOpaque(false);
        this.directionUp.setFont(PitchComposite.this.labelFont);
        this.directionPnl.add(this.directionUp);
        this.directionUp.addActionListener(actionListener);
        this.directionDown = new JLedButton("DOWN");
        this.directionDown.setOpaque(false);
        this.directionDown.setFont(PitchComposite.this.labelFont);
        this.directionPnl.add(this.directionDown);
        this.directionDown.addActionListener(actionListener);
        ButtonGroup directionBtnGroup = new ButtonGroup();
        directionBtnGroup.add(this.directionUp);
        directionBtnGroup.add(this.directionDown);
        this.rangePnl = new ButtonGroupPanel("OCT RANGE", PitchComposite.this.labelFont);
        this.add(this.rangePnl);
        ActionListener actionListener1 = actionEvent -> {
          if (OctaverPane.this.range1Oct.isSelected()) {
            ((GmOctaverAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setOctRange(OCTAVE_RANGE.ONE_OCT);
          }

          if (OctaverPane.this.range2Oct.isSelected()) {
            ((GmOctaverAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setOctRange(OCTAVE_RANGE.TWO_OCT);
          }

          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.range1Oct = new JLedButton("1 OCT");
        this.range1Oct.setOpaque(false);
        this.range1Oct.setFont(PitchComposite.this.labelFont);
        this.rangePnl.add(this.range1Oct);
        this.range1Oct.addActionListener(actionListener1);
        this.range2Oct = new JLedButton("2 OCT");
        this.range2Oct.setOpaque(false);
        this.range2Oct.setFont(PitchComposite.this.labelFont);
        this.rangePnl.add(this.range2Oct);
        this.range2Oct.addActionListener(actionListener1);
        ButtonGroup octaveBtnGroup = new ButtonGroup();
        octaveBtnGroup.add(this.range1Oct);
        octaveBtnGroup.add(this.range2Oct);
      }

      void layout(JPanel pnl) {
        SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
        Constraints thisCon = thisLayout.getConstraints(pnl);
        Constraints dirPnlCon = thisLayout.getConstraints(this.directionPnl);
        Constraints rangePnlCon = thisLayout.getConstraints(this.rangePnl);
        int gap = 4;
        rangePnlCon.setX(Spring.constant(gap));
        rangePnlCon.setY(Spring.constant(gap));
        dirPnlCon.setX(Spring.sum(Spring.constant(gap), rangePnlCon.getConstraint("East")));
        dirPnlCon.setY(Spring.constant(gap));
        thisCon.setWidth(Spring.sum(Spring.constant(gap * 3), Spring.sum(dirPnlCon.getWidth(), rangePnlCon.getWidth())));
        thisCon.setHeight(Spring.sum(Spring.constant(gap * 3 + 2), Spring.max(rangePnlCon.getHeight(), rangePnlCon.getHeight())));
        PitchComposite.this.packPanel(pnl);
      }
    }

    class ShifterPane extends JPanel {
      private JDial voice1;
      private JDial voice2;
      private JDial pan1;
      private JDial pan2;
      private JDial delay1;
      private JDial delay2;
      private JDial feedback1;
      private JDial feedback2;
      private JDial level1;
      private JDial level2;
      ModifierButton voice1Modifier;
      ModifierButton voice2Modifier;
      ModifierButton pan1Modifier;
      ModifierButton pan2Modifier;
      ModifierButton fb1Modifier;
      ModifierButton fb2Modifier;

      ShifterPane() {
      }

      void refresh(GmShifterAlgorithm algorithm) {
        this.voice1.setDialValue(algorithm.getVoice1());
        this.voice2.setDialValue(algorithm.getVoice2());
        this.pan1.setDialValue(algorithm.getPan1());
        this.pan2.setDialValue(algorithm.getPan2());
        this.delay1.setDialValue(algorithm.getDelay1());
        this.delay2.setDialValue(algorithm.getDelay2());
        this.feedback1.setDialValue(algorithm.getFeedback1());
        this.feedback2.setDialValue(algorithm.getFeedback2());
        this.level1.setDialValue(algorithm.getLevel1());
        this.level2.setDialValue(algorithm.getLevel2());
        this.voice1Modifier.setModifierValue(algorithm.getVoice1Modifier());
        this.voice2Modifier.setModifierValue(algorithm.getVoice2Modifier());
        this.pan1Modifier.setModifierValue(algorithm.getPan1Modifier());
        this.pan2Modifier.setModifierValue(algorithm.getPan2Modifier());
        this.fb1Modifier.setModifierValue(algorithm.getFb1Modifier());
        this.fb2Modifier.setModifierValue(algorithm.getFb2Modifier());
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.voice1Modifier = PitchComposite.this.createModifierControl();
        this.add(this.voice1Modifier);
        this.voice2Modifier = PitchComposite.this.createModifierControl();
        this.add(this.voice2Modifier);
        this.pan1Modifier = PitchComposite.this.createModifierControl();
        this.add(this.pan1Modifier);
        this.pan2Modifier = PitchComposite.this.createModifierControl();
        this.add(this.pan2Modifier);
        this.fb1Modifier = PitchComposite.this.createModifierControl();
        this.add(this.fb1Modifier);
        this.fb2Modifier = PitchComposite.this.createModifierControl();
        this.add(this.fb2Modifier);
        this.voice1 = new JDial();
        this.voice1.setLabel("VOICE 1");
        this.voice1.setFont(PitchComposite.this.labelFont);
        this.voice1.setRange(GmShifterAlgorithm.shifterVoiceRange);
        this.add(this.voice1);
        this.voice1.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setVoice1(ShifterPane.this.voice1.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.voice2 = new JDial();
        this.voice2.setLabel("VOICE 2");
        this.voice2.setFont(PitchComposite.this.labelFont);
        this.voice2.setRange(GmShifterAlgorithm.shifterVoiceRange);
        this.add(this.voice2);
        this.voice2.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setVoice2(ShifterPane.this.voice2.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.pan1 = new JDial();
        this.pan1.setLabel("PAN 1");
        this.pan1.setFont(PitchComposite.this.labelFont);
        this.pan1.setRange(GmShifterAlgorithm.shifterPanRange);
        this.add(this.pan1);
        this.pan1.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setPan1(ShifterPane.this.pan1.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.pan2 = new JDial();
        this.pan2.setLabel("PAN 2");
        this.pan2.setFont(PitchComposite.this.labelFont);
        this.pan2.setRange(GmShifterAlgorithm.shifterPanRange);
        this.add(this.pan2);
        this.pan2.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setPan2(ShifterPane.this.pan2.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.delay1 = new JDial();
        this.delay1.setLabel("DELAY 1");
        this.delay1.setFont(PitchComposite.this.labelFont);
        this.delay1.setRange(GmShifterAlgorithm.shifterDelayRange);
        this.add(this.delay1);
        this.delay1.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setDelay1(ShifterPane.this.delay1.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.delay2 = new JDial();
        this.delay2.setLabel("DELAY 2");
        this.delay2.setFont(PitchComposite.this.labelFont);
        this.delay2.setRange(GmShifterAlgorithm.shifterDelayRange);
        this.add(this.delay2);
        this.delay2.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setDelay2(ShifterPane.this.delay2.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.feedback1 = new JDial();
        this.feedback1.setLabel("FB 1");
        this.feedback1.setFont(PitchComposite.this.labelFont);
        this.feedback1.setRange(GmShifterAlgorithm.shifterFeedbackRange);
        this.add(this.feedback1);
        this.feedback1.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setFeedback1(ShifterPane.this.feedback1.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.feedback2 = new JDial();
        this.feedback2.setLabel("FB 2");
        this.feedback2.setFont(PitchComposite.this.labelFont);
        this.feedback2.setRange(GmShifterAlgorithm.shifterDelayRange);
        this.add(this.feedback2);
        this.feedback2.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setFeedback2(ShifterPane.this.feedback2.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.level1 = new JDial();
        this.level1.setLabel("LEVEL 1");
        this.level1.setFont(PitchComposite.this.labelFont);
        this.level1.setRange(GmShifterAlgorithm.shifterLevelRange);
        this.add(this.level1);
        this.level1.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setLevel1(ShifterPane.this.level1.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.level2 = new JDial();
        this.level2.setLabel("LEVEL 2");
        this.level2.setFont(PitchComposite.this.labelFont);
        this.level2.setRange(GmShifterAlgorithm.shifterLevelRange);
        this.add(this.level2);
        this.level2.addChangeListener(arg0 -> {
          ((GmShifterAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setLevel2(ShifterPane.this.level2.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void layout(JPanel pnl) {
        componentLayoutStrategy layout = new componentLayoutStrategy(pnl);
        layout.addComponent(this.voice1, this.voice2, null, null, this.voice1Modifier);
        layout.addComponent(this.voice1Modifier, this.voice2, null, this.voice1, null);
        layout.addComponent(this.voice2, this.pan1, this.voice1, null, this.voice2Modifier);
        layout.addComponent(this.voice2Modifier, this.pan1, this.voice1Modifier, this.voice2, null);
        layout.addComponent(this.pan1, this.pan2, this.voice2, null, this.pan1Modifier);
        layout.addComponent(this.pan1Modifier, this.pan2, this.voice2, this.pan1, null);
        layout.addComponent(this.pan2, this.delay1, this.pan1, null, this.pan2Modifier);
        layout.addComponent(this.pan2Modifier, this.delay1, this.pan1Modifier, this.pan2, null);
        layout.addComponent(this.delay1, this.delay2, this.pan2, null, null);
        layout.addComponent(this.delay2, this.feedback1, this.delay1, null, null);
        layout.addComponent(this.feedback1, this.feedback2, this.delay2, null, this.fb1Modifier);
        layout.addComponent(this.fb1Modifier, this.fb2Modifier, this.delay2, this.feedback1, null);
        layout.addComponent(this.feedback2, this.level1, this.feedback1, null, this.fb2Modifier);
        layout.addComponent(this.fb2Modifier, this.level1, this.fb1Modifier, this.feedback2, null);
        layout.addComponent(this.level1, this.level2, this.feedback2, null, null);
        layout.addComponent(this.level2, null, this.level1, null, null);
        layout.layout();
        PitchComposite.this.packPanel(pnl);
      }
    }

    class WhammyPane extends JPanel {
      private JLedButton directionUp;
      private JLedButton directionDown;
      private JPanel directionPnl;
      private JLedButton range1Oct;
      private JLedButton range2Oct;
      private JPanel rangePnl;
      private JDial whammyPitch;
      private ModifierButton pitchModifier;

      WhammyPane() {
      }

      void refresh(GmWhammyAlgorithm algorithm) {
        this.directionUp.setSelected(algorithm.getDirection() == PITCH_DIRECTION.UP);
        this.directionDown.setSelected(algorithm.getDirection() == PITCH_DIRECTION.DOWN);
        this.range1Oct.setSelected(algorithm.getOctRange() == OCTAVE_RANGE.ONE_OCT);
        this.range2Oct.setSelected(algorithm.getOctRange() == OCTAVE_RANGE.TWO_OCT);
        this.whammyPitch.setDialValue(algorithm.getWhammyPitch());
        this.pitchModifier.setModifierValue(algorithm.getPitchModifier());
      }

      void initPanel() {
        this.setOpaque(false);
        this.setLayout(new SpringLayout());
        this.pitchModifier = PitchComposite.this.createModifierControl();
        this.add(this.pitchModifier);
        this.directionPnl = new ButtonGroupPanel( "DIRECTION ", PitchComposite.this.labelFont);
        this.add(this.directionPnl);
        ActionListener dirListener = e -> {
          if (WhammyPane.this.directionUp.isSelected()) {
            ((GmWhammyAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setDirection(PITCH_DIRECTION.UP);
          }

          if (WhammyPane.this.directionDown.isSelected()) {
            ((GmWhammyAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setDirection(PITCH_DIRECTION.DOWN);
          }

          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.directionUp = PitchComposite.createLedButton("UP");
        this.directionPnl.add(this.directionUp);
        this.directionUp.addActionListener(dirListener);
        this.directionDown = PitchComposite.createLedButton("DOWN");
        this.directionPnl.add(this.directionDown);
        this.directionDown.addActionListener(dirListener);
        ButtonGroup directionBtnGroup = new ButtonGroup();
        directionBtnGroup.add(this.directionUp);
        directionBtnGroup.add(this.directionDown);
        this.rangePnl = new ButtonGroupPanel("OCT RANGE", PitchComposite.this.labelFont);
        this.add(this.rangePnl);
        ActionListener octListener = e -> {
          if (WhammyPane.this.range1Oct.isSelected()) {
            ((GmWhammyAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setOctRange(OCTAVE_RANGE.ONE_OCT);
          }

          if (WhammyPane.this.range2Oct.isSelected()) {
            ((GmWhammyAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setOctRange(OCTAVE_RANGE.TWO_OCT);
          }

          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        };
        this.range1Oct = PitchComposite.createLedButton("1 OCT");
        this.rangePnl.add(this.range1Oct);
        this.range1Oct.addActionListener(octListener);
        this.range2Oct = PitchComposite.createLedButton("2 OCT");
        this.rangePnl.add(this.range2Oct);
        this.range2Oct.addActionListener(octListener);
        ButtonGroup octaveBtnGroup = new ButtonGroup();
        octaveBtnGroup.add(this.range1Oct);
        octaveBtnGroup.add(this.range2Oct);
        this.whammyPitch = PitchComposite.this.createDialControl("WHAMMY PITCH", GmWhammyAlgorithm.whammyMixRange);
        this.add(this.whammyPitch);
        this.whammyPitch.addChangeListener(arg0 -> {
          ((GmWhammyAlgorithm)((GmPitchEffect)PitchComposite.this.effect).getAlgorithm()).setWhammyPitch(WhammyPane.this.whammyPitch.getDialValue().toString());
          PitchComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }

      void layout(JPanel pnl) {
        componentLayoutStrategy layout = new componentLayoutStrategy( pnl);
        layout.addComponent(this.rangePnl, this.directionPnl, null, null, null);
        layout.addComponent(this.directionPnl, this.whammyPitch, this.rangePnl, null, null);
        layout.addComponent(this.whammyPitch, null, this.directionPnl, null, this.pitchModifier);
        layout.addComponent(this.pitchModifier, null, this.directionPnl, this.whammyPitch, null);
        layout.layout();
        PitchComposite.this.packPanel(pnl);
      }
    }
  }

  public static class ReverbComposite extends MultiAlgorithmComposite {
    private ReverbComposite.SpringReverbPane springPane;
    private ReverbComposite.HallReverbPane hallPane;
    private ReverbComposite.RoomReverbPane roomPane;
    private ReverbComposite.PlateReverbPane platePane;

    public ReverbComposite() {
      this.algorithms.add(REVERB_ALGO_TYPE.HALL);
      this.algorithms.add(REVERB_ALGO_TYPE.PLATE);
      this.algorithms.add(REVERB_ALGO_TYPE.ROOM);
      this.algorithms.add(REVERB_ALGO_TYPE.SPRING);
    }

    public void initPanel() {
      this.initTitleBar("REVERB");
      super.initPanel();
    }

    public void refresh(GmEffect pGmEffect) {
      super.refresh(pGmEffect);
      if (((GmReverbEffect)pGmEffect).getAlgorithmType() == REVERB_ALGO_TYPE.SPRING) {
        if (this.springPane == null) {
          this.springPane = new ReverbComposite.SpringReverbPane();
          this.springPane.initPanel();
        }

        this.algorithmControlPnl.add(this.springPane);
        this.springPane.layout(this.springPane);
        this.springPane.refresh((ReverbAlgorithm)((GmReverbEffect)pGmEffect).getAlgorithm());
      } else if (this.springPane != null) {
        this.algorithmControlPnl.remove(this.springPane);
      }

      if (((GmReverbEffect)pGmEffect).getAlgorithmType() == REVERB_ALGO_TYPE.HALL) {
        if (this.hallPane == null) {
          this.hallPane = new ReverbComposite.HallReverbPane();
          this.hallPane.initPanel();
        }

        this.algorithmControlPnl.add(this.hallPane);
        this.hallPane.layout(this.hallPane);
        this.hallPane.refresh((ReverbAlgorithm)((GmReverbEffect)pGmEffect).getAlgorithm());
      } else if (this.hallPane != null) {
        this.algorithmControlPnl.remove(this.hallPane);
      }

      if (((GmReverbEffect)pGmEffect).getAlgorithmType() == REVERB_ALGO_TYPE.ROOM) {
        if (this.roomPane == null) {
          this.roomPane = new ReverbComposite.RoomReverbPane();
          this.roomPane.initPanel();
        }

        this.algorithmControlPnl.add(this.roomPane);
        this.roomPane.layout(this.roomPane);
        this.roomPane.refresh((ReverbAlgorithm)((GmReverbEffect)pGmEffect).getAlgorithm());
      } else if (this.roomPane != null) {
        this.algorithmControlPnl.remove(this.roomPane);
      }

      if (((GmReverbEffect)pGmEffect).getAlgorithmType() == REVERB_ALGO_TYPE.PLATE) {
        if (this.platePane == null) {
          this.platePane = new ReverbComposite.PlateReverbPane();
          this.platePane.initPanel();
        }

        this.algorithmControlPnl.add(this.platePane);
        this.platePane.layout(this.platePane);
        this.platePane.refresh((ReverbAlgorithm)((GmReverbEffect)pGmEffect).getAlgorithm());
      } else if (this.platePane != null) {
        this.algorithmControlPnl.remove(this.platePane);
      }

      this.updateUI();
      this.layout(this);
    }

    class HallReverbPane extends ReverbComposite.ReverbPane {
      HallReverbPane() {
        super();
      }

    }

    class PlateReverbPane extends ReverbComposite.ReverbPane {
      PlateReverbPane() {
        super();
      }

    }

    abstract class ReverbPane extends JPanel {
      private final JDial decay;
      private final JDial preDelay;
      private JLedButton round;
      private final JLedButton curved;
      private final JLedButton square;
      private final JPanel shape;
      private final JDial size;
      private final JDial hiColor;
      private final JDial hiFactor;
      private final JDial loColor;
      private final JDial loFactor;
      private final JDial roomLevel;
      private final JDial reverbLevel;
      private final JDial diffuse;

      void refresh(ReverbAlgorithm algorithm) {
        this.decay.setDialValue(algorithm.getDecay());
        this.preDelay.setDialValue(algorithm.getPreDelay());
        this.round.setSelected(algorithm.getShape() == REVERB_SHAPE.ROUND);
        this.curved.setSelected(algorithm.getShape() == REVERB_SHAPE.CURVED);
        this.square.setSelected(algorithm.getShape() == REVERB_SHAPE.SQUARE);
        this.size.setDialValue(algorithm.getSize());
        this.hiColor.setDialValue(algorithm.getHiColor());
        this.hiFactor.setDialValue(algorithm.getHiFactor());
        this.loColor.setDialValue(algorithm.getLoColor());
        this.loFactor.setDialValue(algorithm.getLoFactor());
        this.roomLevel.setDialValue(algorithm.getRoomLevel());
        this.reverbLevel.setDialValue(algorithm.getReverbLevel());
        this.diffuse.setDialValue(algorithm.getDiffuse());
      }

      void layout(JPanel pnl) {
        SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
        Constraints thisCon = thisLayout.getConstraints(pnl);
        Constraints decayCon = thisLayout.getConstraints(this.decay);
        Constraints preDelayCon = thisLayout.getConstraints(this.preDelay);
        Constraints shapeCon = thisLayout.getConstraints(this.shape);
        Constraints sizeCon = thisLayout.getConstraints(this.size);
        Constraints hcCon = thisLayout.getConstraints(this.hiColor);
        Constraints hfCon = thisLayout.getConstraints(this.hiFactor);
        Constraints lcCon = thisLayout.getConstraints(this.loColor);
        Constraints lfCon = thisLayout.getConstraints(this.loFactor);
        Constraints rmlCon = thisLayout.getConstraints(this.roomLevel);
        Constraints rblCon = thisLayout.getConstraints(this.reverbLevel);
        Constraints diffCon = thisLayout.getConstraints(this.diffuse);
        int gap = 2;
        decayCon.setX(Spring.constant(gap));
        decayCon.setY(Spring.constant(gap));
        preDelayCon.setX(Spring.sum(Spring.constant(gap), decayCon.getConstraint("East")));
        preDelayCon.setY(Spring.constant(gap));
        shapeCon.setX(Spring.sum(Spring.constant(gap), preDelayCon.getConstraint("East")));
        shapeCon.setY(Spring.constant(gap));
        sizeCon.setX(Spring.sum(Spring.constant(gap), shapeCon.getConstraint("East")));
        sizeCon.setY(Spring.constant(gap));
        hcCon.setX(Spring.sum(Spring.constant(gap), sizeCon.getConstraint("East")));
        hcCon.setY(Spring.constant(gap));
        hfCon.setX(Spring.sum(Spring.constant(gap), hcCon.getConstraint("East")));
        hfCon.setY(Spring.constant(gap));
        lcCon.setX(Spring.sum(Spring.constant(gap), hfCon.getConstraint("East")));
        lcCon.setY(Spring.constant(gap));
        lfCon.setX(Spring.sum(Spring.constant(gap), lcCon.getConstraint("East")));
        lfCon.setY(Spring.constant(gap));
        rmlCon.setX(Spring.sum(Spring.constant(gap), lfCon.getConstraint("East")));
        rmlCon.setY(Spring.constant(gap));
        rblCon.setX(Spring.sum(Spring.constant(gap), rmlCon.getConstraint("East")));
        rblCon.setY(Spring.constant(gap));
        diffCon.setX(Spring.sum(Spring.constant(gap), rblCon.getConstraint("East")));
        diffCon.setY(Spring.constant(gap));
        thisCon.setWidth(Spring.sum(Spring.constant(gap * 12), Spring.sum(decayCon.getWidth(), Spring.sum(preDelayCon.getWidth(), Spring.sum(shapeCon.getWidth(), Spring.sum(sizeCon.getWidth(), Spring.sum(hcCon.getWidth(), Spring.sum(hfCon.getWidth(), Spring.sum(lcCon.getWidth(), Spring.sum(lfCon.getWidth(), Spring.sum(rmlCon.getWidth(), Spring.sum(rblCon.getWidth(), diffCon.getWidth()))))))))))));
        thisCon.setHeight(Spring.sum(Spring.constant(gap * 2), Spring.max(shapeCon.getHeight(), decayCon.getHeight())));
        ReverbComposite.this.packPanel(pnl);
      }

      ReverbPane() {
        this.setLayout(new SpringLayout());
        this.decay = new JDial();
        this.preDelay = new JDial();
        this.shape = new ButtonGroupPanel( "SHAPE ", ReverbComposite.this.labelFont);
        this.curved = new JLedButton("CURVED");
        this.square = new JLedButton("SQUARE");
        this.size = new JDial();
        this.hiColor = new JDial();
        this.hiFactor = new JDial();
        this.loColor = new JDial();
        this.loFactor = new JDial();
        this.roomLevel = new JDial();
        this.reverbLevel = new JDial();
        this.diffuse = new JDial();
      }

      void initPanel() {
        this.setOpaque(false);
        this.decay.setLabel("DECAY");
        this.decay.setFont(ReverbComposite.this.labelFont);
        this.decay.setRange(GmReverbEffect.decayRange);
        this.add(this.decay);
        this.decay.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setDecay(ReverbPane.this.decay.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.preDelay.setLabel("PRE DLY");
        this.preDelay.setFont(ReverbComposite.this.labelFont);
        this.preDelay.setRange(GmReverbEffect.preDelayRange);
        this.add(this.preDelay);
        this.preDelay.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setPreDelay(ReverbPane.this.preDelay.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        ActionListener shapeListener = arg0 -> {
          if (ReverbComposite.this.effect != null) {
            if (ReverbPane.this.round.isSelected()) {
              ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setShape(REVERB_SHAPE.ROUND);
            }

            if (ReverbPane.this.curved.isSelected()) {
              ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setShape(REVERB_SHAPE.CURVED);
            }

            if (ReverbPane.this.square.isSelected()) {
              ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setShape(REVERB_SHAPE.SQUARE);
            }

            ReverbComposite.this.effect.markChanged();
            GmMidiCommManager.getInstance().patchUpdated();
          }

        };
        this.add(this.shape);
        this.round = new JLedButton("ROUND");
        this.round.setOpaque(false);
        this.round.setFont(ReverbComposite.this.labelFont);
        this.round.addActionListener(shapeListener);
        this.shape.add(this.round);
        this.curved.setOpaque(false);
        this.curved.setFont(ReverbComposite.this.labelFont);
        this.curved.addActionListener(shapeListener);
        this.shape.add(this.curved);
        this.square.setOpaque(false);
        this.square.setFont(ReverbComposite.this.labelFont);
        this.square.addActionListener(shapeListener);
        this.shape.add(this.square);
        ButtonGroup shapeGroup = new ButtonGroup();
        shapeGroup.add(this.round);
        shapeGroup.add(this.curved);
        shapeGroup.add(this.square);
        this.size.setLabel("SIZE");
        this.size.setFont(ReverbComposite.this.labelFont);
        this.size.setRange(ReverbSizeRange.getInstance());
        this.add(this.size);
        this.size.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setSize(ReverbPane.this.size.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.hiColor.setLabel("HI COL");
        this.hiColor.setFont(ReverbComposite.this.labelFont);
        this.hiColor.setRange(ReverbHiColorRange.getInstance());
        this.add(this.hiColor);
        this.hiColor.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setHiColor(ReverbPane.this.hiColor.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.hiFactor.setLabel("HI FAC");
        this.hiFactor.setFont(ReverbComposite.this.labelFont);
        this.hiFactor.setRange(GmReverbEffect.factorRange);
        this.add(this.hiFactor);
        this.hiFactor.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setHiFactor(ReverbPane.this.hiFactor.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.loColor.setLabel("LO COL");
        this.loColor.setFont(ReverbComposite.this.labelFont);
        this.loColor.setRange(ReverbLoColorRange.getInstance());
        this.add(this.loColor);
        this.loColor.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setLoColor(ReverbPane.this.loColor.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.loFactor.setLabel("LO FAC");
        this.loFactor.setFont(ReverbComposite.this.labelFont);
        this.loFactor.setRange(GmReverbEffect.factorRange);
        this.add(this.loFactor);
        this.loFactor.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setLoFactor(ReverbPane.this.loFactor.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.roomLevel.setLabel("ROOM LVL");
        this.roomLevel.setFont(ReverbComposite.this.labelFont);
        this.roomLevel.setRange(GmReverbEffect.outLevelRange);
        this.add(this.roomLevel);
        this.roomLevel.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setRoomLevel(ReverbPane.this.roomLevel.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.reverbLevel.setLabel("REVB LVL");
        this.reverbLevel.setFont(ReverbComposite.this.labelFont);
        this.reverbLevel.setRange(GmReverbEffect.outLevelRange);
        this.add(this.reverbLevel);
        this.reverbLevel.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setReverbLevel(ReverbPane.this.reverbLevel.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
        this.diffuse.setLabel("DIFFUSE");
        this.diffuse.setFont(ReverbComposite.this.labelFont);
        this.diffuse.setRange(GmReverbEffect.factorRange);
        this.add(this.diffuse);
        this.diffuse.addChangeListener(arg0 -> {
          ((ReverbAlgorithm)((GmReverbEffect)ReverbComposite.this.effect).getAlgorithm()).setDiffuse(ReverbPane.this.diffuse.getDialValue().toString());
          ReverbComposite.this.effect.markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        });
      }
    }

    class RoomReverbPane extends ReverbComposite.ReverbPane {
      RoomReverbPane() {
        super();
      }

    }

    class SpringReverbPane extends ReverbComposite.ReverbPane {
      SpringReverbPane() {
        super();
      }

    }
  }
}
