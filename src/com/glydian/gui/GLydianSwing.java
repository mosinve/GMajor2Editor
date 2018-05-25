//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.gui;

import com.glydian.gui.EffectComposite.CompressorComposite;
import com.glydian.gui.EffectComposite.EqualizerComposite;
import com.glydian.gui.EffectComposite.NoiseGateComposite;
import com.glydian.gui.MultiAlgorithmComposite.DelayComposite;
import com.glydian.gui.MultiAlgorithmComposite.FilterComposite;
import com.glydian.gui.MultiAlgorithmComposite.ModulationComposite;
import com.glydian.gui.MultiAlgorithmComposite.PitchComposite;
import com.glydian.gui.MultiAlgorithmComposite.ReverbComposite;
import com.glydian.gui.controls.J2DButton;
import com.glydian.gui.controls.J2DTextFieldBorder;
import com.glydian.gui.controls.JDial;
import com.glydian.gui.controls.JLedButton;
import com.glydian.gui.controls.J2DButton.UP_DOWN_BUTTON_TYPE;
import com.glydian.gui.controls.JDial.RendererStyle;
import com.glydian.model.GLydianParameters;
import com.glydian.model.GmMidiCommManager;
import com.glydian.model.GmMidiManager;
import com.glydian.model.GmajorPatch;
import com.glydian.model.PatchLibrary;
import com.glydian.model.GmMidiManager.MidiInitException;
import com.glydian.model.GmajorPatch.InvalidGmajorSysexException;
import com.glydian.model.GmajorPatch.PATCH_BANK;
import com.glydian.model.effect.GmEffect;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
//import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.SysexMessage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import javax.swing.filechooser.FileFilter;

public class GLydianSwing extends JPanel {
  static int MAX_NO_ITEMS_IN_A_MENU = 10;
  static ImageIcon imagePnlOnBg;
  static ImageIcon imageDlgBg;
  static ImageIcon imageTitleBg;
  public static final ImageIcon imageUpArrow = new ImageIcon(Toolkit.getDefaultToolkit().getImage(GLydianParameters.getInstance().getClass().getResource("/resources/up_arrow.png")));
  public static final ImageIcon imageDownArrow = new ImageIcon(Toolkit.getDefaultToolkit().getImage(GLydianParameters.getInstance().getClass().getResource("/resources/down_arrow.png")));
  private boolean layOutWideScreenFormat = true;
  private final JFrame frame;
  private final Font labelFont;
  private final Font editFont;
  private final GLydianSwing.MainDisplay displayMain;
  private final NoiseGateComposite displayNoiseGate;
  private final CompressorComposite displayCompressor;
  private final FilterComposite displayFilter;
  private final PitchComposite displayPitch;
  private final ModulationComposite displayModulation;
  private final EqualizerComposite displayEqualizer;
  private final ReverbComposite displayReverb;
  private final DelayComposite displayDelay;
  private GmajorPatch editingPatch;
  private final GmMidiManager midiDeviceHandler;
  private JLedButton midiIn;
  private JLedButton midiOut;
  private JLedButton midiRealTime;
  private final ActionListener loadRunLibListener = actionEvent -> GLydianSwing.this.loadLibraryFromSysExFiles();
  private final ActionListener saveRunLibListener = actionEvent -> GLydianSwing.this.saveLibraryToDir();
  private final ActionListener addToRunLibListener = actionEvent -> {
    if (GLydianSwing.this.getEditingPatch() != null) {
      try {
        GmajorPatch newGmPatch = new GmajorPatch(GLydianSwing.this.getEditingPatch().createSysex());
        PatchLibrary.getInstance().add(newGmPatch);
      } catch (InvalidGmajorSysexException | IOException | InvalidMidiDataException e) {
        e.printStackTrace();
      }

      PatchLibrary.getInstance().notifyObservers("Editing Patch added to Patch Lib ");
    }

  };
  private final ActionListener cleanRunLibListener = actionEvent -> {
    PatchLibrary.getInstance().clear();
    PatchLibrary.getInstance().notifyObservers("Patch Lib cleared");
  };
  private final ActionListener openSyExFromDiskListener = actionEvent -> {
    GmajorPatch patch = GLydianSwing.this.openSyseExFile();
    if (patch != null) {
      GLydianSwing.this.refresh(patch);
    }

  };
  private final ActionListener saveSyExToDiskListener = actionEvent -> GLydianSwing.this.saveSysExFile(GLydianSwing.this.getEditingPatch());
  private final ActionListener midiSetupListener = actionEvent -> {
    if (GLydianSwing.this.setupMidiDevice()) {
      GLydianSwing.this.midiIn.setText("IN  (" + GLydianParameters.getInstance().getMidiInputDevice() + ")");
      GLydianSwing.this.midiIn.setSelected(true);
      GLydianSwing.this.midiOut.setText("OUT (" + GLydianParameters.getInstance().getMidiOutputDevice() + ")");
      GLydianSwing.this.midiOut.setSelected(true);
    } else {
      GLydianSwing.this.midiIn.setText("IN (none)");
      GLydianSwing.this.midiOut.setText("OUT (none)");
    }

  };
  private final ActionListener sendSysExToDeviceListener = actionEvent -> {
    if (GLydianSwing.this.getEditingPatch() != null) {
      GLydianSwing.this.uploadPatch(GLydianSwing.this.getEditingPatch(), false);
    }

  };
  private final ActionListener receiveSysExFromDeviceListener = actionEvent -> {
    int patchId = 0;
    PATCH_BANK bank = PATCH_BANK.FACTORY;
    GLydianSwing.this.downloadPatch(bank, patchId);
  };
  private final FileFilter sysExFilter = new FileFilter() {
    public boolean accept(File f) {
      if (f != null) {
        if (f.isDirectory()) {
          return true;
        }

        String extension = this.getExtension(f);
        return extension != null && extension.equalsIgnoreCase("syx");
      }

      return false;
    }

    String getExtension(File f) {
      if (f != null) {
        String filename = f.getName();
        int i = filename.lastIndexOf(46);
        if (i > 0 && i < filename.length() - 1) {
          return filename.substring(i + 1).toLowerCase();
        }
      }

      return null;
    }

    public String getDescription() {
      return null;
    }
  };
  private JFileChooser chooser;

  private void setEditingPatch(GmajorPatch pEditingPatch) {
    this.editingPatch = pEditingPatch;
    GmMidiCommManager.getInstance().setEditingPatch(this.editingPatch);
  }

  private GmajorPatch getEditingPatch() {
    return this.editingPatch;
  }

  public GLydianSwing(JFrame frame) {
    this.frame = frame;
    this.setupMenus(frame);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
      }
    });
    this.layOutWideScreenFormat = GLydianParameters.getInstance().getProperty("WIDE_SCREEN_FORMAT").equalsIgnoreCase("true");
    MAX_NO_ITEMS_IN_A_MENU = GLydianParameters.getInstance().getNumerticPropertyValue("NO_OF_ITEMS_IN_A_MENU");
    imagePnlOnBg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(GLydianParameters.getInstance().getProperty("EFFECT_BACKGROUND_ON_OVERLAY_IMG"))));
    imageDlgBg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(GLydianParameters.getInstance().getProperty("EFFECT_BACKGROUND_IMG"))));
    imageTitleBg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(GLydianParameters.getInstance().getProperty("TITLEBAR_BACKGROUND_IMG"))));
    this.labelFont = new Font(GLydianParameters.getInstance().getProperty("LABEL_FONT"), GLydianParameters.getInstance().getNumerticPropertyValue("LABEL_FONT_STYLE"), GLydianParameters.getInstance().getNumerticPropertyValue("LABEL_FONT_SIZE"));
    this.editFont = new Font(GLydianParameters.getInstance().getProperty("EDIT_FONT"), Font.PLAIN, GLydianParameters.getInstance().getNumerticPropertyValue("EDIT_FONT_SIZE"));
    this.midiDeviceHandler = new GmMidiManager();
    this.setEditingPatch(new GmajorPatch());
    this.displayMain = new GLydianSwing.MainDisplay();
    this.displayNoiseGate = new NoiseGateComposite();
    this.displayCompressor = new CompressorComposite();
    this.displayEqualizer = new EqualizerComposite();
    this.displayFilter = new FilterComposite();
    this.displayPitch = new PitchComposite();
    this.displayModulation = new ModulationComposite();
    this.displayDelay = new DelayComposite();
    this.displayReverb = new ReverbComposite();
    GmMidiCommManager.getInstance().setDeviceHandler(this.midiDeviceHandler);
    this.initGUI();
    this.refresh(this.getEditingPatch());
    this.updateUI();
  }

  private void setupMenus(JFrame frame) {
    Font menuFont = new Font(GLydianParameters.getInstance().getProperty("MENU_FONT"), GLydianParameters.getInstance().getNumerticPropertyValue("MENU_FONT_STYLE"), GLydianParameters.getInstance().getNumerticPropertyValue("MENU_FONT_SIZE"));
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    fileMenu.setFont(menuFont);
    fileMenu.setMnemonic(70);
    menuBar.add(fileMenu);
    JMenuItem menuItemOpenSyEx = new JMenuItem("Open SyEx from your computer");
    menuItemOpenSyEx.setFont(menuFont);
    menuItemOpenSyEx.setAccelerator(KeyStroke.getKeyStroke("control O"));
    menuItemOpenSyEx.addActionListener(this.openSyExFromDiskListener);
    fileMenu.add(menuItemOpenSyEx);
    JMenuItem menuItemSaveSyEx = new JMenuItem("Save current patch as a SyEx in to your computer");
    menuItemSaveSyEx.setFont(menuFont);
    menuItemSaveSyEx.setAccelerator(KeyStroke.getKeyStroke("control S"));
    menuItemSaveSyEx.addActionListener(this.saveSyExToDiskListener);
    fileMenu.add(menuItemSaveSyEx);
    JMenu libMenu = new JMenu("Patch Library");
    libMenu.setFont(menuFont);
    libMenu.setMnemonic(76);
    menuBar.add(libMenu);
    JMenuItem menuItemAddToRunLib = new JMenuItem("Add current editing patch to the Patch Library");
    menuItemAddToRunLib.setFont(menuFont);
    menuItemAddToRunLib.setAccelerator(KeyStroke.getKeyStroke("control V"));
    menuItemAddToRunLib.addActionListener(this.addToRunLibListener);
    libMenu.add(menuItemAddToRunLib);
    OpenPatchFromLibMenu patchListMenu = new OpenPatchFromLibMenu();
    libMenu.add(patchListMenu);
    JMenuItem menuItemCleanLib = new JMenuItem("Clear all the patches in the library");
    menuItemCleanLib.setFont(menuFont);
    menuItemCleanLib.addActionListener(this.cleanRunLibListener);
    libMenu.add(menuItemCleanLib);
    libMenu.add(new JSeparator());
    JMenuItem menuItemLoadRunLibFromDisk = new JMenuItem("Load Patch Library form SyEx files in your computer");
    menuItemLoadRunLibFromDisk.setFont(menuFont);
    menuItemLoadRunLibFromDisk.addActionListener(this.loadRunLibListener);
    libMenu.add(menuItemLoadRunLibFromDisk);
    JMenuItem menuItemSaveRunLibToDisk = new JMenuItem("Save Patch Library to SyEx files in your computer");
    menuItemSaveRunLibToDisk.setFont(menuFont);
    menuItemSaveRunLibToDisk.addActionListener(this.saveRunLibListener);
    libMenu.add(menuItemSaveRunLibToDisk);
    libMenu.add(new JSeparator());
    JMenuItem menuItemLoadFactoryPatchesToRunLib = new JMenuItem("Add G-Major Factory Patches  to the Patch Library");
    menuItemLoadFactoryPatchesToRunLib.setFont(menuFont);
    menuItemLoadFactoryPatchesToRunLib.addActionListener(new GLydianSwing.AddPatchesToLibFromDeviceListener(PATCH_BANK.FACTORY));
    libMenu.add(menuItemLoadFactoryPatchesToRunLib);
    JMenuItem menuItemLoadUserPatchesToRunLib = new JMenuItem("Add G-Major User Patches  to the Patch Library");
    menuItemLoadUserPatchesToRunLib.setFont(menuFont);
    menuItemLoadUserPatchesToRunLib.addActionListener(new GLydianSwing.AddPatchesToLibFromDeviceListener(PATCH_BANK.USER));
    libMenu.add(menuItemLoadUserPatchesToRunLib);
    JMenu midiMenu = new JMenu("Midi");
    midiMenu.setFont(menuFont);
    midiMenu.setMnemonic(77);
    menuBar.add(midiMenu);
    JMenuItem menuItemMidiSetup = new JMenuItem("Midi port setup");
    menuItemMidiSetup.setFont(menuFont);
    menuItemMidiSetup.setAccelerator(KeyStroke.getKeyStroke("control M"));
    menuItemMidiSetup.addActionListener(this.midiSetupListener);
    midiMenu.add(menuItemMidiSetup);
    JMenuItem menuItemSendSysEx = new JMenuItem("Send current patch to G-Major");
    menuItemSendSysEx.setFont(menuFont);
    menuItemSendSysEx.setAccelerator(KeyStroke.getKeyStroke("control X"));
    menuItemSendSysEx.addActionListener(this.sendSysExToDeviceListener);
    midiMenu.add(menuItemSendSysEx);
    JMenuItem menuItemReceiveSysEx = new JMenuItem("Receive the current patch in G-Major to GLydian");
    menuItemReceiveSysEx.setFont(menuFont);
    menuItemReceiveSysEx.setAccelerator(KeyStroke.getKeyStroke("control C"));
    menuItemReceiveSysEx.addActionListener(this.receiveSysExFromDeviceListener);
    midiMenu.add(menuItemReceiveSysEx);
    JMenu helpMenu = new JMenu("Help");
    helpMenu.setFont(menuFont);
    menuBar.add(helpMenu);
    JMenuItem menuItemAbout = new JMenuItem("About GLydian");
    menuItemAbout.setFont(menuFont);
    menuItemAbout.addActionListener(e -> {
      String message = "<html><body><p style=\"text-align: center;\"><big style=\"color: rgb(138, 46, 138);\"><big><span style=\"font-weight: bold; \">GLydian</span> </big></big><br></p><div style=\"text-align: center;\">Version 1.4.4 CR</div><div style=\"text-align: center; font-family: Helvetica,Arial,sans-serif;\">Patch Editor for T.C. ELectronics G-Major Guitar Processor<br></div><p style=\"text-align: center;\"><span style=\"font-style: italic;\">Developed by</span></p><p style=\"text-align: center; color: rgb(153, 51, 153);\"><span style=\"font-weight: bold; font-style: italic;\">Kuruwitage Nandana Perera</span></p><p></p><p style=\"text-align: left;font-size: 12pt;  \">Thanks to <span style=\"font-weight: bold; font-style: italic; color: rgb(153, 51, 153);\">downtownpaulyp</span> <span style=\"text-align: center;\">for deciphering the G-Major SysEx MIDI message format. <br>Thanks for all the GLydian users for their valuable feedback.</span></p><small><p>This software is still in active development. <br>Please post your questions and suggestions on the web forums hosting this software. </p></small><p></p><div style=\"text-align: left;\"><small>DISCLAMER: </small><br><small> GLydian is noway related to T.C. Electronics.<br> This software is provided&nbsp;\"AS IS\" and use at your own risk.<br>Copyright 2008 - Nandana Perera</small></div></body></html>";
      JEditorPane editorPane = new JEditorPane();
      editorPane.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);
      editorPane.setFont(new Font("Arial", Font.BOLD, 13));
      editorPane.setEditable(false);
      editorPane.setContentType("text/html");
      editorPane.setText(message);
      JOptionPane.showMessageDialog(null, new JScrollPane(editorPane), "About GLydian", JOptionPane.PLAIN_MESSAGE);
    });
    helpMenu.add(menuItemAbout);
    frame.setJMenuBar(menuBar);
  }

  private void refresh(GmajorPatch pGmPatch) {
    this.setEditingPatch(pGmPatch);
    this.displayMain.refresh(null);
    this.displayNoiseGate.refresh(this.getEditingPatch().getNoiseGateEft());
    this.displayEqualizer.refresh(this.getEditingPatch().getEqualizerEft());
    this.displayCompressor.refresh(this.getEditingPatch().getCompressorEft());
    this.displayFilter.refresh(this.getEditingPatch().getFilterEft());
    this.displayPitch.refresh(this.getEditingPatch().getPitchEft());
    this.displayModulation.refresh(this.getEditingPatch().getModulatorEft());
    this.displayReverb.refresh(this.getEditingPatch().getReverbEffect());
    this.displayDelay.refresh(this.getEditingPatch().getDelayEffect());
    this.layout(this);
  }

  private void initGUI() {
    try {
      this.setLayout(new SpringLayout());
      this.displayMain.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayMain);
      this.displayMain.initPanel();
      this.add(this.displayMain);
      this.displayNoiseGate.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayNoiseGate);
      this.displayNoiseGate.initPanel();
      this.add(this.displayNoiseGate);
      this.displayCompressor.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayCompressor);
      this.displayCompressor.initPanel();
      this.add(this.displayCompressor);
      this.displayEqualizer.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayEqualizer);
      this.displayEqualizer.initPanel();
      this.add(this.displayEqualizer);
      this.displayFilter.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayFilter);
      this.displayFilter.initPanel();
      this.add(this.displayFilter);
      this.displayPitch.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayPitch);
      this.displayPitch.initPanel();
      this.add(this.displayPitch);
      this.displayModulation.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayModulation);
      this.displayModulation.initPanel();
      this.add(this.displayModulation);
      this.displayDelay.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayDelay);
      this.displayDelay.initPanel();
      this.add(this.displayDelay);
      this.displayReverb.setLabelFont(this.labelFont);
      GLydianRackArranger.getInstance().setEffectUnitDimension(this.displayReverb);
      this.displayReverb.initPanel();
      this.add(this.displayReverb);
    } catch (Exception var2) {
      var2.printStackTrace();
    }

  }

  private void layout(JPanel pnl) {
    SpringLayout thisLayout = (SpringLayout)pnl.getLayout();
    Constraints thisCon = thisLayout.getConstraints(this);
    Constraints mainCon = thisLayout.getConstraints(this.displayMain);
    Constraints noiseCon = thisLayout.getConstraints(this.displayNoiseGate);
    Constraints compCon = thisLayout.getConstraints(this.displayCompressor);
    Constraints eqCon = thisLayout.getConstraints(this.displayEqualizer);
    Constraints filterCon = thisLayout.getConstraints(this.displayFilter);
    Constraints pitchCon = thisLayout.getConstraints(this.displayPitch);
    Constraints modCon = thisLayout.getConstraints(this.displayModulation);
    Constraints revCon = thisLayout.getConstraints(this.displayReverb);
    Constraints delayCon = thisLayout.getConstraints(this.displayDelay);
    int gap = 1;
    mainCon.setX(Spring.constant(gap));
    mainCon.setY(Spring.constant(gap));
    if (this.layOutWideScreenFormat) {
      noiseCon.setX(Spring.constant(gap));
      noiseCon.setY(Spring.sum(Spring.constant(gap), mainCon.getConstraint("South")));
      eqCon.setX(Spring.constant(gap));
      eqCon.setY(Spring.sum(Spring.constant(gap), noiseCon.getConstraint("South")));
      compCon.setX(Spring.constant(gap));
      compCon.setY(Spring.sum(Spring.constant(gap), eqCon.getConstraint("South")));
      Spring mainMargin = Spring.sum(Spring.constant(gap), Spring.max(mainCon.getConstraint("East"), Spring.max(noiseCon.getConstraint("East"), Spring.max(eqCon.getConstraint("East"), compCon.getConstraint("East")))));
      filterCon.setX(mainMargin);
      filterCon.setY(Spring.constant(gap));
      pitchCon.setX(mainMargin);
      pitchCon.setY(Spring.sum(Spring.constant(gap), filterCon.getConstraint("South")));
      modCon.setX(mainMargin);
      modCon.setY(Spring.sum(Spring.constant(gap), pitchCon.getConstraint("South")));
      delayCon.setX(mainMargin);
      delayCon.setY(Spring.sum(Spring.constant(gap), modCon.getConstraint("South")));
      revCon.setX(mainMargin);
      revCon.setY(Spring.sum(Spring.constant(gap), delayCon.getConstraint("South")));
      thisCon.setWidth(Spring.sum(Spring.sum(Spring.constant(gap * 2), Spring.max(mainCon.getWidth(), Spring.max(eqCon.getWidth(), Spring.max(noiseCon.getWidth(), compCon.getWidth())))), Spring.sum(Spring.constant(gap * 2), Spring.max(filterCon.getWidth(), Spring.max(pitchCon.getWidth(), Spring.max(modCon.getWidth(), Spring.max(delayCon.getWidth(), revCon.getWidth())))))));
      thisCon.setHeight(Spring.max(Spring.sum(Spring.constant(gap * 5), Spring.sum(Spring.sum(mainCon.getHeight(), Spring.sum(noiseCon.getHeight(), compCon.getHeight())), eqCon.getHeight())), Spring.sum(Spring.constant(gap * 6), Spring.sum(filterCon.getHeight(), Spring.sum(pitchCon.getHeight(), Spring.sum(modCon.getHeight(), Spring.sum(delayCon.getHeight(), revCon.getHeight())))))));
    } else {
      eqCon.setX(Spring.sum(Spring.constant(gap), mainCon.getConstraint("East")));
      eqCon.setY(Spring.constant(gap));
      noiseCon.setX(Spring.sum(Spring.constant(gap), eqCon.getConstraint("East")));
      noiseCon.setY(Spring.constant(gap));
      compCon.setX(Spring.sum(Spring.constant(gap), eqCon.getConstraint("East")));
      compCon.setY(Spring.sum(Spring.constant(gap), noiseCon.getConstraint("South")));
      filterCon.setX(Spring.constant(gap));
      filterCon.setY(Spring.sum(Spring.constant(gap), Spring.max(mainCon.getConstraint("South"), compCon.getConstraint("South"))));
      pitchCon.setX(Spring.constant(gap));
      pitchCon.setY(Spring.sum(Spring.constant(gap), filterCon.getConstraint("South")));
      modCon.setX(Spring.constant(gap));
      modCon.setY(Spring.sum(Spring.constant(gap), pitchCon.getConstraint("South")));
      delayCon.setX(Spring.constant(gap));
      delayCon.setY(Spring.sum(Spring.constant(gap), modCon.getConstraint("South")));
      revCon.setX(Spring.constant(gap));
      revCon.setY(Spring.sum(Spring.constant(gap), delayCon.getConstraint("South")));
      thisCon.setWidth(Spring.max(Spring.sum(Spring.constant(gap * 4), Spring.sum(mainCon.getWidth(), Spring.sum(eqCon.getWidth(), Spring.max(noiseCon.getWidth(), compCon.getWidth())))), Spring.sum(Spring.constant(gap * 2), Spring.max(filterCon.getWidth(), Spring.max(pitchCon.getWidth(), Spring.max(modCon.getWidth(), Spring.max(delayCon.getWidth(), revCon.getWidth())))))));
      thisCon.setHeight(Spring.sum(Spring.constant(gap * 7), Spring.sum(Spring.max(Spring.max(mainCon.getHeight(), Spring.sum(noiseCon.getHeight(), compCon.getHeight())), eqCon.getHeight()), Spring.sum(filterCon.getHeight(), Spring.sum(pitchCon.getHeight(), Spring.sum(modCon.getHeight(), Spring.sum(delayCon.getHeight(), revCon.getHeight())))))));
    }

  }

  private JFileChooser getFileChooser(FileFilter fileFilter, boolean chooseFolder) {
    if (this.chooser == null) {
      String workFolder = GLydianParameters.getInstance().getWorkFolder();
      if (workFolder != null && workFolder.length() > 0) {
        this.chooser = new JFileChooser(workFolder);
      } else {
        this.chooser = new JFileChooser();
      }

      this.chooser.setMultiSelectionEnabled(false);
      if (!chooseFolder) {
        this.chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.chooser.setFileFilter(fileFilter);
      } else {
        this.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      }
    }

    return this.chooser;
  }

  private void saveSysExFile(GmajorPatch gmajorPatch) {
    JFileChooser chooser = this.getFileChooser(this.sysExFilter, false);
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnVal = chooser.showSaveDialog(this.frame);
    if (returnVal == 0) {
      System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());

      try {
        FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
        SysexMessage sysex = gmajorPatch.createSysex();
        fos.write(sysex.getMessage());
        fos.flush();
        fos.close();
      } catch (IOException | InvalidMidiDataException e) {
        e.printStackTrace();
      }
    }

  }

  private GmajorPatch openSyseExFile() {
    JFileChooser chooser = this.getFileChooser(this.sysExFilter, false);
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnVal = chooser.showOpenDialog(this.frame);
    if (returnVal == 0) {
      GLydianParameters.getInstance().setWorkFolder(chooser.getSelectedFile().getParent());
      System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());

      try {
        byte[] sysexData = new byte[615];
        InputStream is = new FileInputStream(chooser.getSelectedFile());
        is.read(sysexData, 0, 615);
        SysexMessage sysex = new SysexMessage();
        sysex.setMessage(sysexData, sysexData.length);
        GmajorPatch newGmPatch = new GmajorPatch(sysex);
        is.close();
        return newGmPatch;
      } catch (IOException | InvalidGmajorSysexException | InvalidMidiDataException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  private void loadLibraryFromSysExFiles() {
    JFileChooser chooser = this.getFileChooser(this.sysExFilter, false);
    chooser.setMultiSelectionEnabled(true);
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    int returnVal = chooser.showOpenDialog(this.frame);
    if (returnVal == 0) {
      File[] files = chooser.getSelectedFiles();
      if (files != null) {
        PatchLibrary.getInstance().clear();

        for (File file : files) {
          try {
            byte[] sysExData = new byte[615];
            InputStream is = new FileInputStream(file);
            is.read(sysExData, 0, 615);
            SysexMessage sysEx = new SysexMessage();
            sysEx.setMessage(sysExData, sysExData.length);
            GmajorPatch newGmPatch = new GmajorPatch(sysEx);
            PatchLibrary.getInstance().add(newGmPatch);
            is.close();
          } catch (IOException | InvalidMidiDataException | InvalidGmajorSysexException e) {
            e.printStackTrace();
          }
        }

        PatchLibrary.getInstance().notifyObservers("Patch Lib loaded from SysExFiles");
      }
    }

  }

  private void saveLibraryToDir() {
    JFileChooser chooser = this.getFileChooser(this.sysExFilter, true);
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int returnVal = chooser.showSaveDialog(this.frame);
    if (returnVal == 0) {
      System.out.println("You chose to save files in: " + chooser.getSelectedFile().getName());
      Collection<GmajorPatch> patchList = PatchLibrary.getInstance().getPatchList();

      for (GmajorPatch libPatch : patchList) {
        try {
          File sysExFile = new File(chooser.getSelectedFile().getAbsolutePath() + "/" + libPatch.getPatchName().trim() + ".syx");
          FileOutputStream fos = new FileOutputStream(sysExFile);
          SysexMessage sysEx = libPatch.createSysex();
          fos.write(sysEx.getMessage());
          fos.flush();
          fos.close();
        } catch (IOException | InvalidMidiDataException e) {
          e.printStackTrace();
        }
      }
    }

  }

  private void downloadPatch(PATCH_BANK bank, int patchNumber) {
    SysexMessage sysEx = null;

    try {
      this.midiDeviceHandler.initializeMidiPorts(GLydianParameters.getInstance().getMidiInputDevice(), GLydianParameters.getInstance().getMidiOutputDevice());
      sysEx = this.midiDeviceHandler.getPatchFromGmajor(bank, patchNumber);
    } catch (MidiInitException | MidiUnavailableException | InvalidMidiDataException e) {
      e.printStackTrace();
    } finally {
      this.midiDeviceHandler.closeDevices();
    }

    try {
      if (sysEx != null) {
        GmajorPatch newGmPatch = new GmajorPatch(sysEx);
        this.refresh(newGmPatch);
      }
    } catch (InvalidGmajorSysexException | IOException e) {
      e.printStackTrace();
    }

  }

  private void uploadPatch(GmajorPatch patch, boolean sendAsExternalPatch) {
    try {
      SysexMessage sysEx = patch.createSysex(sendAsExternalPatch);
      this.midiDeviceHandler.initializeMidiPorts(GLydianParameters.getInstance().getMidiInputDevice(), GLydianParameters.getInstance().getMidiOutputDevice());
      this.midiDeviceHandler.sendPatch(sysEx);
      this.midiDeviceHandler.closeDevices();
    } catch (MidiInitException | MidiUnavailableException | InvalidMidiDataException e) {
      e.printStackTrace();
    }

  }

  private boolean setupMidiDevice() {
    final JDialog dlg = new JDialog(this.frame);
    JPanel midiSetupPnl = new JPanel() {
      public void paintComponent(Graphics g) {
        if (this.isOpaque()) {
          super.paintComponent(g);
        }

        GLydianSwing.imageDlgBg.paintIcon(this, g, 0, 0);
      }
    };
    midiSetupPnl.setBackground(Color.LIGHT_GRAY);
    midiSetupPnl.setLayout(new SpringLayout());
    Vector<String> inDevices = GmMidiManager.getMidiDeviceNameList(true, false);
    Vector<String> outDevices = GmMidiManager.getMidiDeviceNameList(false, true);
    JLabel title = new JLabel("Select In and Out MIDI Device names");
    title.setFont(new Font("Tahoma", Font.PLAIN, 11));
    midiSetupPnl.add(title);
    final JTextField inDevice = new JTextField();
    JPanel inSelectionPnl = this.createSelectionPanel(inDevice, inDevices, "INPUT DEVICE  ");
    inDevice.setText(GLydianParameters.getInstance().getMidiInputDevice());
    midiSetupPnl.add(inSelectionPnl);
    final JTextField outDevice = new JTextField();
    JPanel outSelectionPnl = this.createSelectionPanel(outDevice, outDevices, "OUTPUT DEVICE ");
    outDevice.setText(GLydianParameters.getInstance().getMidiOutputDevice());
    midiSetupPnl.add(outSelectionPnl);
    J2DButton ok = new J2DButton("OK");
    ok.setText("OK");
    ok.setOpaque(false);
    ok.addActionListener(actionEvent -> {
      GLydianParameters.getInstance().setMidiInputDevice(inDevice.getText());
      GLydianParameters.getInstance().setMidiOutputDevice(outDevice.getText());
      dlg.setVisible(false);
    });
    midiSetupPnl.add(ok);
    J2DButton cancel = new J2DButton("CANCEL");
    cancel.setText("CANCEL");
    cancel.setOpaque(false);
    cancel.addActionListener(actionEvent -> dlg.setVisible(false));
    midiSetupPnl.add(cancel);
    SpringLayout thisLayout = (SpringLayout)midiSetupPnl.getLayout();
    Constraints thisCon = thisLayout.getConstraints(midiSetupPnl);
    Constraints titleCon = thisLayout.getConstraints(title);
    Constraints inCon = thisLayout.getConstraints(inSelectionPnl);
    Constraints outCon = thisLayout.getConstraints(outSelectionPnl);
    Constraints okCon = thisLayout.getConstraints(ok);
    Constraints cancelCon = thisLayout.getConstraints(cancel);
    titleCon.setX(Spring.constant(10));
    titleCon.setY(Spring.constant(15));
    inCon.setX(Spring.constant(5));
    inCon.setY(Spring.sum(Spring.constant(5), titleCon.getConstraint("South")));
    outCon.setX(Spring.constant(5));
    outCon.setY(Spring.sum(Spring.constant(5), inCon.getConstraint("South")));
    okCon.setX(Spring.sum(inCon.getConstraint("East"), Spring.minus(Spring.sum(Spring.constant(5), Spring.sum(okCon.getWidth(), cancelCon.getWidth())))));
    okCon.setY(Spring.sum(Spring.constant(5), outCon.getConstraint("South")));
    cancelCon.setX(Spring.sum(Spring.constant(5), okCon.getConstraint("East")));
    cancelCon.setY(Spring.sum(Spring.constant(5), outCon.getConstraint("South")));
    thisCon.setWidth(Spring.max(Spring.sum(Spring.constant(10), inCon.getWidth()), Spring.sum(Spring.constant(10), outCon.getWidth())));
    thisCon.setHeight(Spring.sum(Spring.constant(60), Spring.sum(titleCon.getHeight(), Spring.sum(inCon.getHeight(), outCon.getHeight()))));
    dlg.add(midiSetupPnl);
    dlg.setModal(true);
    dlg.setSize(425, 152);
    dlg.setLocationRelativeTo(this.frame);
    dlg.setVisible(true);
    return GLydianParameters.getInstance().getMidiInputDevice().trim().length() > 0 && GLydianParameters.getInstance().getMidiOutputDevice().trim().length() > 0;
  }

  private JPanel createSelectionPanel(final JTextField inDevice, final Vector<String> dataVector, String label) {
    final ComboBoxModel comboModel = new DefaultComboBoxModel<>(dataVector);
    comboModel.setSelectedItem(dataVector.get(0));
    JPanel selectionPnl = new JPanel();
    selectionPnl.setLayout(new SpringLayout());
    selectionPnl.setOpaque(false);
    JLabel title = new JLabel(label);
    title.setFont(this.labelFont);
    selectionPnl.add(title);
    inDevice.setMargin(new Insets(5, 5, 5, 5));
    inDevice.setText("");
    inDevice.setBorder(new J2DTextFieldBorder());
    inDevice.setFont(this.editFont);
    inDevice.setColumns(30);
    selectionPnl.add(inDevice);
    inDevice.setText((String)comboModel.getSelectedItem());
    J2DButton up = new J2DButton();
    up.setOpaque(false);
    up.setButtonType(UP_DOWN_BUTTON_TYPE.UP);
    up.addActionListener(actionEvent -> {
      int selectedIdx = dataVector.indexOf(comboModel.getSelectedItem());
      if (selectedIdx < dataVector.size() - 1) {
        ++selectedIdx;
      } else {
        selectedIdx = 0;
      }

      comboModel.setSelectedItem(comboModel.getElementAt(selectedIdx));
      inDevice.setText((String)comboModel.getSelectedItem());
    });
    selectionPnl.add(up);
    J2DButton down = new J2DButton();
    down.setOpaque(false);
    down.setButtonType(UP_DOWN_BUTTON_TYPE.DOWN);
    down.addActionListener(actionEvent -> {
      int selectedIdx = dataVector.indexOf(comboModel.getSelectedItem());
      if (selectedIdx > 0) {
        --selectedIdx;
      } else {
        selectedIdx = dataVector.size() - 1;
      }

      comboModel.setSelectedItem(comboModel.getElementAt(selectedIdx));
      inDevice.setText((String)comboModel.getSelectedItem());
    });
    selectionPnl.add(down);
    SpringLayout thisLayout = (SpringLayout)selectionPnl.getLayout();
    Constraints thisCon = thisLayout.getConstraints(selectionPnl);
    Constraints titleCon = thisLayout.getConstraints(title);
    Constraints textCon = thisLayout.getConstraints(inDevice);
    Constraints upCon = thisLayout.getConstraints(up);
    Constraints downCon = thisLayout.getConstraints(down);
    int gap = 1;
    titleCon.setX(Spring.constant(5));
    titleCon.setY(Spring.constant(5));
    textCon.setX(Spring.constant(90));
    textCon.setY(Spring.constant(gap));
    textCon.setHeight(Spring.sum(upCon.getHeight(), downCon.getHeight()));
    upCon.setX(Spring.sum(Spring.constant(gap), textCon.getConstraint("East")));
    upCon.setY(Spring.constant(gap));
    downCon.setY(upCon.getConstraint("South"));
    downCon.setX(Spring.sum(Spring.constant(gap), textCon.getConstraint("East")));
    thisCon.setWidth(Spring.sum(Spring.constant(gap * 5), Spring.sum(Spring.constant(90), Spring.sum(textCon.getWidth(), upCon.getWidth()))));
    thisCon.setHeight(Spring.sum(Spring.constant(gap * 2), Spring.sum(upCon.getHeight(), downCon.getHeight())));
    return selectionPnl;
  }

  public void paintComponent(Graphics g) {
    if (this.isOpaque()) {
      super.paintComponent(g);
    }

    imageDlgBg.paintIcon(this, g, 0, 0);
  }

  class AddPatchesToLibFromDeviceListener implements ActionListener {
    PATCH_BANK bank;

    AddPatchesToLibFromDeviceListener(PATCH_BANK bank) {
      this.bank = PATCH_BANK.FACTORY;
      this.bank = bank;
    }

    public void actionPerformed(ActionEvent arg0) {
      int patchId = 1;

      try {
        GLydianSwing.this.midiDeviceHandler.initializeMidiPorts(GLydianParameters.getInstance().getMidiInputDevice(), GLydianParameters.getInstance().getMidiOutputDevice());
        boolean noMorePatchesInDevice = false;

        while(patchId <= 100 && !noMorePatchesInDevice) {
          SysexMessage sysEx = GLydianSwing.this.midiDeviceHandler.getPatchFromGmajor(this.bank, patchId);
          ++patchId;
          if (sysEx != null) {
            try {
              GmajorPatch newGmPatch = new GmajorPatch(sysEx);
              PatchLibrary.getInstance().add(newGmPatch);
            } catch (InvalidGmajorSysexException | IOException var13) {
              noMorePatchesInDevice = true;
              var13.printStackTrace();
            }
          }
        }
      } catch (MidiInitException | MidiUnavailableException | InvalidMidiDataException e) {
        e.printStackTrace();
      } finally {
        PatchLibrary.getInstance().notifyObservers("G-Major " + this.bank + " patches added to Patch Lib ");
        GLydianSwing.this.midiDeviceHandler.closeDevices();
      }

    }
  }

  class MainDisplay extends EffectComposite {
    private final JTextField patchName;
    private final JTextField patchNumber;
    private final JLedButton factBank;
    private final JLedButton userBank;
    private final J2DButton up;
    private final J2DButton down;
    private final JPanel patchNamePnl;
    private final J2DButton sendAsSelectedPatchBtn;
    private final J2DButton receiveSelectedPatchBtn;
    private final J2DButton sendToBankPatchBtn;
    private final J2DButton receiveFromBankPatchBtn;
    private final J2DButton openFromDiskBtn;
    private final J2DButton saveToDiskBtn;
    private final J2DButton addToLibBtn;
    private final J2DButton openFromLibBtn;
    private final JPanel mainButtonPnl;
    private final JLedButton serialRouting;
    private final JLedButton semiParallelRouting;
    private final JLedButton parallelRouting;
    private JPanel patchRoutingPnl;
    private final JPanel patchInfoPnl;
    private final JLedButton relayOne;
    private final JLedButton relayTwo;
    private JPanel relayRealTimePnl;
    private final JDial outLevel;
    private final JDial dialTapTempo;
    private final JPanel globalParamsPnl;

    MainDisplay() {
      this.onOffButton = null;
      this.patchNamePnl = new JPanel();
      this.up = new J2DButton();
      this.down = new J2DButton();
      this.patchName = new JTextField();
      this.patchNumber = new JTextField();
      this.mainButtonPnl = new JPanel();
      this.sendAsSelectedPatchBtn = new J2DButton("Tx Editing");
      this.sendAsSelectedPatchBtn.setToolTipText("Send editing patch to G-Major for Audition/Play as the current patch. <br> This will not be saved in G-Major");
      this.receiveSelectedPatchBtn = new J2DButton("Rx Playing");
      this.receiveSelectedPatchBtn.setToolTipText("Receive currently Auditioning/Playing patch <br>from G-Major to GLydian as editing patch");
      this.sendToBankPatchBtn = new J2DButton("Tx Bank");
      this.sendToBankPatchBtn.setToolTipText("Send editing patch to G-Major User Patch Bank. <br> This will save the editing patch in to G-Major User Patch Bank.");
      this.receiveFromBankPatchBtn = new J2DButton("Rx Bank");
      this.receiveFromBankPatchBtn.setToolTipText("Receive patch from G-Major User or Factory Bank <br>(specified by patch number and Bank values in GLydian) to GLydian as editing patch");
      this.openFromDiskBtn = new J2DButton("Open Disk");
      this.openFromDiskBtn.setToolTipText("Open sysEx from the Hard Disk to edit in GLydian");
      this.saveToDiskBtn = new J2DButton("Save Disk");
      this.saveToDiskBtn.setToolTipText("Save editing patch to the Hard Disk as a sysEx");
      this.addToLibBtn = new J2DButton("Add Lib");
      this.addToLibBtn.setToolTipText("Add editing patch to the Runtime Patch Library.");
      this.openFromLibBtn = new J2DButton("Get Lib");
      this.openFromLibBtn.setToolTipText("Recall editing patch from the Runtime Patch Library version <br>if available in the Library.");
      this.factBank = new JLedButton("FACTORY");
      this.userBank = new JLedButton("USER");
      this.patchInfoPnl = new JPanel();
      this.serialRouting = new JLedButton("SERIAL");
      this.semiParallelRouting = new JLedButton("SEMI PARALLEL");
      this.parallelRouting = new JLedButton("PARALLEL");
      this.relayOne = new JLedButton("RELAY 1");
      this.relayTwo = new JLedButton("RELAY 2");
      GLydianSwing.this.midiIn = new JLedButton("IN (" + GLydianParameters.getInstance().getMidiInputDevice() + ")");
      GLydianSwing.this.midiOut = new JLedButton("OUT (" + GLydianParameters.getInstance().getMidiOutputDevice() + ")");
      GLydianSwing.this.midiRealTime = new JLedButton("Tx RealTime");
      this.globalParamsPnl = new JPanel();
      this.outLevel = new JDial(RendererStyle.LABEL_BOTTOM_VALUE_BOTTOM);
      this.dialTapTempo = new JDial(RendererStyle.LABEL_BOTTOM_VALUE_BOTTOM);
    }

    public void layout(EffectComposite pnl) {
      GLydianRackArranger.getInstance().setEffectUnitDimension(this);
      this.layoutMainButtonPnl();
      this.layoutPatchSelectionPnl();
      this.layoutGlobalParamPnl();
      this.layoutPatchInfoPnl();
      int gap = 2;
      SpringLayout thisLayout = (SpringLayout)this.getLayout();
      Constraints thisCon = thisLayout.getConstraints(this);
      Constraints dtl1Con = thisLayout.getConstraints(this.mainButtonPnl);
      Constraints dtl2Con = thisLayout.getConstraints(this.patchNamePnl);
      Constraints dtl4Con = thisLayout.getConstraints(this.patchInfoPnl);
      dtl1Con.setX(Spring.constant(gap));
      dtl1Con.setY(Spring.constant(gap));
      dtl2Con.setX(Spring.constant(gap));
      dtl2Con.setY(Spring.sum(Spring.constant(gap), dtl1Con.getConstraint("South")));
      dtl4Con.setX(Spring.constant(gap));
      dtl4Con.setY(Spring.sum(Spring.constant(gap), dtl2Con.getConstraint("South")));
      dtl1Con.setWidth(dtl2Con.getWidth());
      dtl4Con.setWidth(dtl2Con.getWidth());
      thisCon.setHeight(Spring.sum(Spring.constant(gap * 4), Spring.sum(dtl1Con.getHeight(), Spring.sum(dtl2Con.getHeight(), dtl4Con.getHeight()))));
      thisCon.setWidth(Spring.sum(Spring.constant(gap * 2), Spring.max(dtl1Con.getWidth(), Spring.max(dtl2Con.getWidth(), dtl4Con.getWidth()))));
    }

    void layoutGlobalParamPnl() {
      SpringLayout globalParamsLayout = (SpringLayout)this.globalParamsPnl.getLayout();
      Constraints globalParamsPnlCon = globalParamsLayout.getConstraints(this.globalParamsPnl);
      Constraints outLvl = globalParamsLayout.getConstraints(this.outLevel);
      Constraints tempoLvl = globalParamsLayout.getConstraints(this.dialTapTempo);
      outLvl.setX(Spring.constant(1));
      outLvl.setY(Spring.constant(1));
      tempoLvl.setX(Spring.sum(Spring.constant(1), outLvl.getConstraint("East")));
      tempoLvl.setY(Spring.constant(1));
      globalParamsPnlCon.setHeight(Spring.sum(Spring.constant(4), outLvl.getHeight()));
      globalParamsPnlCon.setWidth(Spring.sum(Spring.constant(4), Spring.sum(outLvl.getWidth(), tempoLvl.getWidth())));
    }

    void layoutPatchInfoPnl() {
      SpringLayout patchInfoLayout = (SpringLayout)this.patchInfoPnl.getLayout();
      Constraints patchInfoCon = patchInfoLayout.getConstraints(this.patchInfoPnl);
      Constraints routingCon = patchInfoLayout.getConstraints(this.patchRoutingPnl);
      Constraints gbParamCon = patchInfoLayout.getConstraints(this.globalParamsPnl);
      Constraints relayRtCon = patchInfoLayout.getConstraints(this.relayRealTimePnl);
      routingCon.setX(Spring.constant(1));
      routingCon.setY(Spring.constant(1));
      gbParamCon.setX(Spring.sum(Spring.constant(1), routingCon.getConstraint("East")));
      gbParamCon.setY(Spring.constant(1));
      relayRtCon.setX(Spring.sum(Spring.constant(1), gbParamCon.getConstraint("East")));
      relayRtCon.setY(Spring.constant(1));
      patchInfoCon.setWidth(Spring.sum(Spring.constant(4), Spring.sum(routingCon.getWidth(), Spring.sum(gbParamCon.getWidth(), relayRtCon.getWidth()))));
    }

    void layoutMainButtonPnl() {
      SpringLayout thisLayout = (SpringLayout)this.mainButtonPnl.getLayout();
      Constraints thisCon = thisLayout.getConstraints(this.mainButtonPnl);
      Constraints sendCon = thisLayout.getConstraints(this.sendAsSelectedPatchBtn);
      Constraints receiveCon = thisLayout.getConstraints(this.receiveSelectedPatchBtn);
      Constraints sendBnkCon = thisLayout.getConstraints(this.sendToBankPatchBtn);
      Constraints receiveBnkCon = thisLayout.getConstraints(this.receiveFromBankPatchBtn);
      Constraints saveCon = thisLayout.getConstraints(this.saveToDiskBtn);
      Constraints openCon = thisLayout.getConstraints(this.openFromDiskBtn);
      Constraints addLibCon = thisLayout.getConstraints(this.addToLibBtn);
      Constraints openLibCon = thisLayout.getConstraints(this.openFromLibBtn);
      int gap = 2;
      sendCon.setX(Spring.constant(gap));
      sendCon.setY(Spring.constant(gap));
      receiveCon.setX(Spring.constant(gap));
      receiveCon.setY(sendCon.getConstraint("South"));
      sendBnkCon.setX(Spring.sum(Spring.constant(gap), sendCon.getConstraint("East")));
      sendBnkCon.setY(Spring.constant(gap));
      receiveBnkCon.setX(sendBnkCon.getConstraint("West"));
      receiveBnkCon.setY(sendBnkCon.getConstraint("South"));
      saveCon.setX(Spring.sum(Spring.constant(gap), sendBnkCon.getConstraint("East")));
      saveCon.setY(Spring.constant(gap));
      openCon.setX(saveCon.getConstraint("West"));
      openCon.setY(saveCon.getConstraint("South"));
      addLibCon.setX(Spring.sum(Spring.constant(gap), saveCon.getConstraint("East")));
      addLibCon.setY(Spring.constant(gap));
      openLibCon.setX(addLibCon.getConstraint("West"));
      openLibCon.setY(addLibCon.getConstraint("South"));
      thisCon.setWidth(Spring.sum(Spring.constant(gap * 5), Spring.sum(Spring.max(sendCon.getWidth(), receiveCon.getWidth()), Spring.sum(Spring.max(sendBnkCon.getWidth(), receiveBnkCon.getWidth()), Spring.sum(Spring.max(saveCon.getWidth(), openCon.getWidth()), Spring.max(addLibCon.getWidth(), openLibCon.getWidth()))))));
      thisCon.setHeight(Spring.sum(Spring.constant(gap * 2), Spring.max(Spring.sum(sendCon.getHeight(), receiveCon.getHeight()), Spring.max(Spring.sum(sendBnkCon.getHeight(), receiveBnkCon.getHeight()), Spring.max(Spring.sum(saveCon.getHeight(), openCon.getHeight()), Spring.sum(addLibCon.getHeight(), openLibCon.getHeight()))))));
      receiveCon.setWidth(sendCon.getWidth());
      receiveBnkCon.setWidth(sendBnkCon.getWidth());
      openCon.setWidth(saveCon.getWidth());
      openLibCon.setWidth(addLibCon.getWidth());
    }

    void layoutPatchSelectionPnl() {
      SpringLayout thisLayout = (SpringLayout)this.patchNamePnl.getLayout();
      Constraints thisCon = thisLayout.getConstraints(this.patchNamePnl);
      Constraints factCon = thisLayout.getConstraints(this.factBank);
      Constraints userCon = thisLayout.getConstraints(this.userBank);
      Constraints nameCon = thisLayout.getConstraints(this.patchName);
      Constraints numCon = thisLayout.getConstraints(this.patchNumber);
      Constraints upCon = thisLayout.getConstraints(this.up);
      Constraints downCon = thisLayout.getConstraints(this.down);
      int gap = 2;
      factCon.setX(Spring.constant(gap));
      factCon.setY(Spring.constant(gap));
      userCon.setX(factCon.getConstraint("East"));
      userCon.setY(Spring.constant(gap));
      nameCon.setX(Spring.sum(Spring.constant(gap), userCon.getConstraint("East")));
      nameCon.setY(Spring.constant(gap));
      nameCon.setHeight(Spring.sum(upCon.getHeight(), downCon.getHeight()));
      numCon.setX(Spring.sum(Spring.constant(gap), nameCon.getConstraint("East")));
      numCon.setY(Spring.constant(gap));
      numCon.setHeight(Spring.sum(upCon.getHeight(), downCon.getHeight()));
      upCon.setX(Spring.sum(Spring.constant(gap), numCon.getConstraint("East")));
      upCon.setY(Spring.constant(gap));
      downCon.setY(upCon.getConstraint("South"));
      downCon.setX(Spring.sum(Spring.constant(gap), numCon.getConstraint("East")));
      thisCon.setWidth(Spring.sum(Spring.constant(gap * 5), Spring.sum(factCon.getWidth(), Spring.sum(userCon.getWidth(), Spring.sum(nameCon.getWidth(), Spring.sum(numCon.getWidth(), upCon.getWidth()))))));
      thisCon.setHeight(Spring.sum(Spring.constant(gap * 2), Spring.sum(upCon.getHeight(), downCon.getHeight())));
    }

    public void initPanel() {
      this.patchNamePnl.setBorder(this.basicBorder);
      this.patchNamePnl.setLayout(new SpringLayout());
      this.patchNamePnl.setOpaque(false);
      ActionListener bankListener = arg0 -> {
        if (GLydianSwing.this.getEditingPatch() != null) {
          if (MainDisplay.this.factBank.isSelected()) {
            GLydianSwing.this.getEditingPatch().setEffectBank(PATCH_BANK.FACTORY);
          } else if (MainDisplay.this.userBank.isSelected()) {
            GLydianSwing.this.getEditingPatch().setEffectBank(PATCH_BANK.USER);
          }
        }

      };
      this.factBank.setOpaque(false);
      this.factBank.setFont(this.labelFont);
      this.factBank.addActionListener(bankListener);
      this.patchNamePnl.add(this.factBank);
      this.userBank.setOpaque(false);
      this.userBank.setFont(this.labelFont);
      this.userBank.addActionListener(bankListener);
      this.patchNamePnl.add(this.userBank);
      ButtonGroup bankBtnGroup = new ButtonGroup();
      bankBtnGroup.add(this.factBank);
      bankBtnGroup.add(this.userBank);
      this.patchName.setMargin(new Insets(5, 5, 5, 5));
      this.patchName.setText("");
      this.patchName.setBorder(new J2DTextFieldBorder());
      this.patchName.setFont(this.labelFont);
      this.patchName.setColumns(20);
      this.patchNamePnl.add(this.patchName);
      this.patchName.addFocusListener(new FocusListener() {
        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
          if (MainDisplay.this.patchName.getText() != null && MainDisplay.this.patchName.getText().trim().length() != 0) {
            if (GLydianSwing.this.getEditingPatch() != null) {
              if (MainDisplay.this.patchName.getText().trim().length() > 20) {
                MainDisplay.this.patchName.setText(MainDisplay.this.patchName.getText().substring(0, 19));
              }

              MainDisplay.this.patchName.setBackground(Color.WHITE);
              GLydianSwing.this.getEditingPatch().setPatchName(MainDisplay.this.patchName.getText());
            }
          } else {
            MainDisplay.this.patchName.setBackground(Color.RED);
          }

        }
      });
      this.patchNumber.setBorder(new J2DTextFieldBorder());
      this.patchNumber.setFont(this.labelFont);
      this.patchNumber.setColumns(2);
      this.patchNamePnl.add(this.patchNumber);
      this.patchNumber.addFocusListener(new FocusListener() {
        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
          System.out.println("Focus lost  :" + MainDisplay.this.patchNumber.getText());

          try {
            if (MainDisplay.this.patchNumber.getText() != null && MainDisplay.this.patchNumber.getText().trim().length() > 0 && Integer.parseInt(MainDisplay.this.patchNumber.getText()) >= 0 && GLydianSwing.this.getEditingPatch() != null) {
              GLydianSwing.this.getEditingPatch().setPatchNumber(Integer.parseInt(MainDisplay.this.patchNumber.getText()));
            }

            MainDisplay.this.patchNumber.setBackground(Color.WHITE);
          } catch (NumberFormatException var3) {
            var3.printStackTrace();
            MainDisplay.this.patchNumber.setBackground(Color.RED);
          }

        }
      });
      this.up.setOpaque(false);
      this.up.setButtonType(UP_DOWN_BUTTON_TYPE.UP);
      this.up.setMnemonic(38);
      this.up.addActionListener(arg0 -> {
        if (GLydianSwing.this.getEditingPatch() != null && GLydianSwing.this.getEditingPatch().getPatchNumber() <= 100) {
          GLydianSwing.this.downloadPatch(GLydianSwing.this.getEditingPatch().getEffectBank(), GLydianSwing.this.getEditingPatch().getPatchNumber() + 1);
        } else {
          GLydianSwing.this.downloadPatch(PATCH_BANK.FACTORY, 0);
        }

      });
      this.patchNamePnl.add(this.up);
      this.down.setOpaque(false);
      this.down.setButtonType(UP_DOWN_BUTTON_TYPE.DOWN);
      this.down.setMnemonic(40);
      this.down.addActionListener(arg0 -> {
        if (GLydianSwing.this.getEditingPatch() != null && GLydianSwing.this.getEditingPatch().getPatchNumber() > 0) {
          GLydianSwing.this.downloadPatch(GLydianSwing.this.getEditingPatch().getEffectBank(), GLydianSwing.this.getEditingPatch().getPatchNumber() - 1);
        } else {
          GLydianSwing.this.downloadPatch(PATCH_BANK.FACTORY, 0);
        }

      });
      this.patchNamePnl.add(this.down);
      this.mainButtonPnl.setBorder(this.basicBorder);
      this.mainButtonPnl.setLayout(new SpringLayout());
      this.mainButtonPnl.setOpaque(false);
      this.sendAsSelectedPatchBtn.setFont(this.labelFont);
      this.sendAsSelectedPatchBtn.setOpaque(false);
      this.sendAsSelectedPatchBtn.addActionListener(arg0 -> {
        if (GLydianSwing.this.getEditingPatch() != null) {
          GLydianSwing.this.uploadPatch(GLydianSwing.this.getEditingPatch(), true);
        }

      });
      this.mainButtonPnl.add(this.sendAsSelectedPatchBtn);
      this.receiveSelectedPatchBtn.setFont(this.labelFont);
      this.receiveSelectedPatchBtn.setOpaque(false);
      this.receiveSelectedPatchBtn.addActionListener(arg0 -> GLydianSwing.this.downloadPatch(PATCH_BANK.FACTORY, 0));
      this.mainButtonPnl.add(this.receiveSelectedPatchBtn);
      this.sendToBankPatchBtn.setFont(this.labelFont);
      this.sendToBankPatchBtn.setOpaque(false);
      this.sendToBankPatchBtn.addActionListener(GLydianSwing.this.sendSysExToDeviceListener);
      this.mainButtonPnl.add(this.sendToBankPatchBtn);
      this.receiveFromBankPatchBtn.setFont(this.labelFont);
      this.receiveFromBankPatchBtn.setOpaque(false);
      this.receiveFromBankPatchBtn.addActionListener(arg0 -> {
        int patchId = 0;
        if (MainDisplay.this.patchNumber.getText().trim().length() > 0) {
          patchId = Integer.parseInt(MainDisplay.this.patchNumber.getText());
        }

        PATCH_BANK bank = PATCH_BANK.FACTORY;
        if (MainDisplay.this.factBank.isSelected()) {
          bank = PATCH_BANK.FACTORY;
        } else if (MainDisplay.this.userBank.isSelected()) {
          bank = PATCH_BANK.USER;
        }

        GLydianSwing.this.downloadPatch(bank, patchId);
      });
      this.mainButtonPnl.add(this.receiveFromBankPatchBtn);
      this.openFromDiskBtn.setFont(this.labelFont);
      this.openFromDiskBtn.setOpaque(false);
      this.openFromDiskBtn.addActionListener(GLydianSwing.this.openSyExFromDiskListener);
      this.mainButtonPnl.add(this.openFromDiskBtn);
      this.saveToDiskBtn.setFont(this.labelFont);
      this.saveToDiskBtn.setOpaque(false);
      this.saveToDiskBtn.addActionListener(GLydianSwing.this.saveSyExToDiskListener);
      this.mainButtonPnl.add(this.saveToDiskBtn);
      this.addToLibBtn.setFont(this.labelFont);
      this.addToLibBtn.setOpaque(false);
      this.addToLibBtn.addActionListener(GLydianSwing.this.addToRunLibListener);
      this.mainButtonPnl.add(this.addToLibBtn);
      this.openFromLibBtn.setFont(this.labelFont);
      this.openFromLibBtn.setOpaque(false);
      this.mainButtonPnl.add(this.openFromLibBtn);
      this.patchInfoPnl.setOpaque(false);
      this.patchInfoPnl.setLayout(new SpringLayout());
      ActionListener routingListener = arg0 -> {
        if (GLydianSwing.this.getEditingPatch() != null) {
          if (MainDisplay.this.serialRouting.isSelected()) {
            GLydianSwing.this.getEditingPatch().setRouting(0);
          }

          if (MainDisplay.this.semiParallelRouting.isSelected()) {
            GLydianSwing.this.getEditingPatch().setRouting(1);
          }

          if (MainDisplay.this.parallelRouting.isSelected()) {
            GLydianSwing.this.getEditingPatch().setRouting(2);
          }

          GLydianSwing.this.getEditingPatch().markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        }

      };
      this.patchRoutingPnl = new ButtonGroupPanel( "EFFECT ROUTING", this.labelFont);
      this.serialRouting.setOpaque(false);
      this.serialRouting.setFont(this.labelFont);
      this.serialRouting.addActionListener(routingListener);
      this.patchRoutingPnl.add(this.serialRouting);
      this.semiParallelRouting.setOpaque(false);
      this.semiParallelRouting.setFont(this.labelFont);
      this.semiParallelRouting.addActionListener(routingListener);
      this.patchRoutingPnl.add(this.semiParallelRouting);
      this.parallelRouting.setOpaque(false);
      this.parallelRouting.setFont(this.labelFont);
      this.parallelRouting.addActionListener(routingListener);
      this.patchRoutingPnl.add(this.parallelRouting);
      this.patchRoutingPnl.add(Box.createVerticalGlue());
      ButtonGroup routBtnGroup = new ButtonGroup();
      routBtnGroup.add(this.serialRouting);
      routBtnGroup.add(this.semiParallelRouting);
      routBtnGroup.add(this.parallelRouting);
      this.patchInfoPnl.add(this.patchRoutingPnl);
      this.globalParamsPnl.setOpaque(false);
      this.globalParamsPnl.setLayout(new SpringLayout());
      this.outLevel.setLabel("OUT LEVEL");
      this.outLevel.setFont(this.labelFont);
      this.outLevel.setRange(GmajorPatch.effectOutLevelRange);
      this.outLevel.addChangeListener(arg0 -> {
        if (GLydianSwing.this.getEditingPatch() != null) {
          GLydianSwing.this.getEditingPatch().setOutLevel(Integer.parseInt(MainDisplay.this.outLevel.getDialValue().toString()));
          GLydianSwing.this.getEditingPatch().markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        }

      });
      this.globalParamsPnl.add(this.outLevel);
      this.dialTapTempo.setLabel("TAP TEMPO");
      this.dialTapTempo.setFont(this.labelFont);
      this.dialTapTempo.setRange(GmajorPatch.tapTempoRange);
      this.dialTapTempo.addChangeListener(arg0 -> {
        if (GLydianSwing.this.getEditingPatch() != null) {
          GLydianSwing.this.getEditingPatch().setTapTempo(Integer.parseInt(MainDisplay.this.dialTapTempo.getDialValue().toString()));
          GLydianSwing.this.getEditingPatch().markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        }

      });
      this.globalParamsPnl.add(this.dialTapTempo);
      this.patchInfoPnl.add(this.globalParamsPnl);
      this.relayRealTimePnl = new JPanel();
      this.relayRealTimePnl.setOpaque(false);
      this.relayRealTimePnl.setLayout(new BoxLayout(this.relayRealTimePnl, BoxLayout.Y_AXIS));
      JPanel relayPnl = new ButtonGroupPanel("RELAYS", this.labelFont);
      this.relayOne.setFont(this.labelFont);
      this.relayOne.setOpaque(false);
      this.relayOne.addActionListener(arg0 -> {
        if (GLydianSwing.this.getEditingPatch().isRelayOneOpen() != MainDisplay.this.relayOne.isSelected()) {
          GLydianSwing.this.getEditingPatch().setRelayOne(MainDisplay.this.relayOne.isSelected());
          GLydianSwing.this.getEditingPatch().markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        }

      });
      relayPnl.add(this.relayOne);
      this.relayTwo.setFont(this.labelFont);
      this.relayTwo.setOpaque(false);
      this.relayTwo.addActionListener(arg0 -> {
        if (GLydianSwing.this.getEditingPatch().isRelayTwoOpen() != MainDisplay.this.relayTwo.isSelected()) {
          GLydianSwing.this.getEditingPatch().setRelayTwo(MainDisplay.this.relayTwo.isSelected());
          GLydianSwing.this.getEditingPatch().markChanged();
          GmMidiCommManager.getInstance().patchUpdated();
        }

      });
      relayPnl.add(this.relayTwo);
      this.relayRealTimePnl.add(relayPnl);
      this.relayRealTimePnl.add(Box.createVerticalGlue());
      this.relayRealTimePnl.add(Box.createHorizontalGlue());
      GLydianSwing.this.midiRealTime.setFont(this.labelFont);
      GLydianSwing.this.midiRealTime.setOpaque(false);
      GLydianSwing.this.midiRealTime.addActionListener(actionEvent -> GmMidiCommManager.getInstance().setRealTimeCommunication(GLydianSwing.this.midiRealTime.isSelected()));
      this.relayRealTimePnl.add(GLydianSwing.this.midiRealTime);
      this.patchInfoPnl.add(this.relayRealTimePnl);
      this.add(this.patchInfoPnl);
      this.add(this.patchNamePnl);
      this.add(this.mainButtonPnl);
    }

    void refresh(GmEffect gmEffect) {
      this.patchName.setText(GLydianSwing.this.getEditingPatch().getPatchName());
      this.patchNumber.setText(String.valueOf(GLydianSwing.this.getEditingPatch().getPatchNumber()));
      this.userBank.setSelected(GLydianSwing.this.getEditingPatch().getEffectBank() == PATCH_BANK.USER);
      this.factBank.setSelected(GLydianSwing.this.getEditingPatch().getEffectBank() == PATCH_BANK.FACTORY);
      this.serialRouting.setSelected(GLydianSwing.this.getEditingPatch().getRouting() == 0);
      this.semiParallelRouting.setSelected(GLydianSwing.this.getEditingPatch().getRouting() == 1);
      this.parallelRouting.setSelected(GLydianSwing.this.getEditingPatch().getRouting() == 2);
      this.dialTapTempo.setDialValue(GLydianSwing.this.getEditingPatch().getTapTempo());
      this.outLevel.setDialValue(GLydianSwing.this.getEditingPatch().getOutLevel());
      this.relayOne.setSelected(GLydianSwing.this.getEditingPatch().isRelayOneOpen());
      this.relayTwo.setSelected(GLydianSwing.this.getEditingPatch().isRelayTwoOpen());
      this.layout(this);
    }
  }

  class OpenPatchFromLibMenu extends JMenu implements Observer {
    private final Font menuFont = new Font(GLydianParameters.getInstance().getProperty("MENU_FONT"), GLydianParameters.getInstance().getNumerticPropertyValue("MENU_FONT_STYLE"), GLydianParameters.getInstance().getNumerticPropertyValue("MENU_FONT_SIZE"));

    OpenPatchFromLibMenu() {
      super("Open Patch ");
      this.setFont(this.menuFont);
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
      Collection<GmajorPatch> patchList = PatchLibrary.getInstance().getPatchList();
      if (patchList.isEmpty()) {
        JMenuItem menuItem = new JMenuItem("No patches in the library ..");
        menuItem.setFont(this.menuFont);
        this.add(menuItem);
      } else {
        int itemsInOwnerMenu = 0;
        JMenu ownerMenu = this;

        for (GmajorPatch aPatchList : patchList) {
          if (itemsInOwnerMenu > GLydianSwing.MAX_NO_ITEMS_IN_A_MENU) {
            itemsInOwnerMenu = 0;
            ownerMenu.add(new JSeparator());
            JMenu childMenu = new JMenu("More ..");
            childMenu.setFont(this.menuFont);
            ownerMenu.add(childMenu);
            ownerMenu = childMenu;
          }

          ++itemsInOwnerMenu;
          final GmajorPatch libPatch = aPatchList;
          JMenu patchMenu = new JMenu(libPatch.getPatchName());
          patchMenu.setFont(this.menuFont);
          ownerMenu.add(patchMenu);
          JMenuItem openPatchItem = new JMenuItem("Open ");
          openPatchItem.setFont(this.menuFont);
          patchMenu.add(openPatchItem);
          openPatchItem.addActionListener(actionEvent -> {
            System.out.println("Opening [" + libPatch.getPatchName() + "] .");

            try {
              GmajorPatch copyOfLibPatch = new GmajorPatch(libPatch.createSysex());
              GLydianSwing.this.refresh(copyOfLibPatch);
            } catch (InvalidGmajorSysexException | IOException | InvalidMidiDataException var3) {
              var3.printStackTrace();
            }

          });
          JMenuItem deletePatchItem = new JMenuItem("Remove ");
          deletePatchItem.setFont(this.menuFont);
          patchMenu.add(deletePatchItem);
          deletePatchItem.addActionListener(actionEvent -> {
            System.out.println("Remove [" + libPatch.getPatchName() + "] .");
            PatchLibrary.getInstance().remove(libPatch.getPatchName());
            PatchLibrary.getInstance().notifyObservers("Item Removed");
          });
        }

      }
    }
  }
}
