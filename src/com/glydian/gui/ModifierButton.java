//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.gui;

import com.glydian.gui.controls.JDial;
import com.glydian.gui.controls.JLedButton;
import com.glydian.model.GLydianParameters;
import com.glydian.model.ModifierField;
import com.glydian.model.ModifierField.MODIFIER;
import com.glydian.model.effect.GmMultiAlgorithmEffect;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

class ModifierButton extends JLedButton {
  private ModifierField modifierValue;
  private final ModifierPopupMenu modifierConfigPopupMenu;
  private final Font labelFont;

  ModifierButton() {
    super("OFF");
    this.labelFont = new Font(GLydianParameters.getInstance().getProperty("LABEL_FONT"), GLydianParameters.getInstance().getNumerticPropertyValue("LABEL_FONT_STYLE"), GLydianParameters.getInstance().getNumerticPropertyValue("LABEL_FONT_SIZE"));
    this.modifierConfigPopupMenu = new ModifierPopupMenu();
    this.addActionListener(actionEvent -> ModifierButton.this.refresh());
  }

  void setModifierValue(ModifierField m) {
    this.modifierValue = m;
  }

  private void refresh() {
    if (this.getModifierValue() != null) {
      this.setText(this.getModifierValue().getModifier().toString());
      this.setSelected(this.getModifierValue().getModifier() != MODIFIER.OFF);
      this.modifierConfigPopupMenu.refresh();
    }

  }

  private ModifierField getModifierValue() {
    return this.modifierValue;
  }

  class ModifierLevelsMenuItem extends ModifierButton.PanelMenuElement {
    final JDial minOut = new JDial();
    final JDial midOut;
    final JDial maxOut;

    void refresh() {
      if (ModifierButton.this.getModifierValue() != null) {
        this.minOut.setDialValue(String.valueOf(ModifierButton.this.getModifierValue().getMinOut()));
        this.midOut.setDialValue(String.valueOf(ModifierButton.this.getModifierValue().getMidOut()));
        this.maxOut.setDialValue(String.valueOf(ModifierButton.this.getModifierValue().getMaxOut()));
      }

    }

    ModifierLevelsMenuItem() {
      super();
      this.minOut.setLabel("MIN OUT");
      this.minOut.setFont(ModifierButton.this.labelFont);
      this.minOut.setRange(GmMultiAlgorithmEffect.mixRange);
      this.add(this.minOut);
      this.minOut.addChangeListener(arg0 -> {
        if (ModifierButton.this.getModifierValue() != null) {
          ModifierButton.this.getModifierValue().setMinOut(Integer.parseInt(ModifierLevelsMenuItem.this.minOut.getDialValue().toString()));
        }

      });
      this.midOut = new JDial();
      this.midOut.setLabel("MID OUT");
      this.midOut.setFont(ModifierButton.this.labelFont);
      this.midOut.setRange(GmMultiAlgorithmEffect.mixRange);
      this.add(this.midOut);
      this.midOut.addChangeListener(arg0 -> {
        if (ModifierButton.this.getModifierValue() != null) {
          ModifierButton.this.getModifierValue().setMidOut(Integer.parseInt(ModifierLevelsMenuItem.this.midOut.getDialValue().toString()));
        }

      });
      this.maxOut = new JDial();
      this.maxOut.setLabel("MAX OUT");
      this.maxOut.setFont(ModifierButton.this.labelFont);
      this.maxOut.setRange(GmMultiAlgorithmEffect.mixRange);
      this.add(this.maxOut);
      this.maxOut.addChangeListener(arg0 -> {
        if (ModifierButton.this.getModifierValue() != null) {
          ModifierButton.this.getModifierValue().setMaxOut(Integer.parseInt(ModifierLevelsMenuItem.this.maxOut.getDialValue().toString()));
        }

      });
    }
  }

  class ModifierPopupMenu extends JPopupMenu {
    final ModifierSelectionMenuElement modifierSelectionMenuElement;
    final ModifierLevelsMenuItem outLevelMenuItem;

    ModifierPopupMenu() {
      JMenuItem title = new JMenuItem("Set Modifiers and Modifier Levels");
      title.addActionListener(e -> {
      });
      title.setFont(ModifierButton.this.labelFont.deriveFont(Font.BOLD));
      this.add(title);
      this.add(new JSeparator());
      this.modifierSelectionMenuElement = ModifierButton.this.new ModifierSelectionMenuElement();
      this.add(this.modifierSelectionMenuElement);
      this.add(new JSeparator());
      this.outLevelMenuItem = ModifierButton.this.new ModifierLevelsMenuItem();
      this.add(this.outLevelMenuItem);
      this.doLayout();
      ModifierButton.this.addActionListener(e -> {
        ModifierPopupMenu.this.pack();
        ModifierPopupMenu.this.show(ModifierButton.this, 5, 15);
        ModifierPopupMenu.this.setVisible(true);
      });
    }

    void refresh() {
      this.modifierSelectionMenuElement.refresh();
      this.outLevelMenuItem.refresh();
    }
  }

  class ModifierSelectionMenuElement extends ModifierButton.PanelMenuElement {
    private final JLedButton menuItem;
    private final JLedButton menuItem1;
    private final JLedButton menuItem2;
    private final JLedButton menuItem3;
    private final JLedButton menuItem4;

    void refresh() {
      if (ModifierButton.this.getModifierValue() != null) {
        this.menuItem.setSelected(ModifierButton.this.getModifierValue().getModifier() == MODIFIER.OFF);
        this.menuItem1.setSelected(ModifierButton.this.getModifierValue().getModifier() == MODIFIER.M1);
        this.menuItem2.setSelected(ModifierButton.this.getModifierValue().getModifier() == MODIFIER.M2);
        this.menuItem3.setSelected(ModifierButton.this.getModifierValue().getModifier() == MODIFIER.M3);
        this.menuItem4.setSelected(ModifierButton.this.getModifierValue().getModifier() == MODIFIER.M4);
      }

    }

    ModifierSelectionMenuElement() {
      super();
      this.menuItem = MultiAlgorithmComposite.createLedButton(MODIFIER.OFF.toString());
      this.menuItem.addActionListener(e -> {
        if (ModifierButton.this.getModifierValue() != null) {
          ModifierButton.this.getModifierValue().setModifier(MODIFIER.OFF);
          ModifierSelectionMenuElement.this.refresh();
        }

      });
      this.add(this.menuItem);
      this.menuItem1 = MultiAlgorithmComposite.createLedButton(MODIFIER.M1.toString());
      this.menuItem1.addActionListener(e -> {
        if (ModifierButton.this.getModifierValue() != null) {
          ModifierButton.this.getModifierValue().setModifier(MODIFIER.M1);
          ModifierSelectionMenuElement.this.refresh();
        }

      });
      this.add(this.menuItem1);
      this.menuItem2 = MultiAlgorithmComposite.createLedButton(MODIFIER.M2.toString());
      this.menuItem2.addActionListener(e -> {
        if (ModifierButton.this.getModifierValue() != null) {
          ModifierButton.this.getModifierValue().setModifier(MODIFIER.M2);
          ModifierSelectionMenuElement.this.refresh();
        }

      });
      this.add(this.menuItem2);
      this.menuItem3 = MultiAlgorithmComposite.createLedButton(MODIFIER.M3.toString());
      this.menuItem3.addActionListener(e -> {
        if (ModifierButton.this.getModifierValue() != null) {
          ModifierButton.this.getModifierValue().setModifier(MODIFIER.M3);
          ModifierSelectionMenuElement.this.refresh();
        }

      });
      this.add(this.menuItem3);
      this.menuItem4 = MultiAlgorithmComposite.createLedButton(MODIFIER.M4.toString());
      this.menuItem4.addActionListener(e -> {
        if (ModifierButton.this.getModifierValue() != null) {
          ModifierButton.this.getModifierValue().setModifier(MODIFIER.M4);
          ModifierSelectionMenuElement.this.refresh();
        }

      });
      this.add(this.menuItem4);
      ButtonGroup btnGroup = new ButtonGroup();
      btnGroup.add(this.menuItem);
      btnGroup.add(this.menuItem1);
      btnGroup.add(this.menuItem2);
      btnGroup.add(this.menuItem3);
      btnGroup.add(this.menuItem4);
    }
  }

  class PanelMenuElement extends JPanel implements MenuElement {
    PanelMenuElement() {
      this.setFocusable(true);
      this.setFocusTraversalKeysEnabled(false);
      this.addFocusListener(new FocusListener() {
        public void focusGained(FocusEvent e) {
          PanelMenuElement.this.setBackground(PanelMenuElement.this.getBackground().darker());
        }

        public void focusLost(FocusEvent e) {
          PanelMenuElement.this.setBackground(PanelMenuElement.this.getBackground().brighter());
        }
      });
      this.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (!PanelMenuElement.this.hasFocus()) {
            PanelMenuElement.this.requestFocusInWindow();
          }

        }

        public void mouseEntered(MouseEvent e) {
          if (!PanelMenuElement.this.hasFocus()) {
            PanelMenuElement.this.requestFocusInWindow();
          }

        }

        public void mouseExited(MouseEvent e) {
          ModifierButton.this.refresh();
        }

        public void mousePressed(MouseEvent e) {
          if (!PanelMenuElement.this.hasFocus()) {
            PanelMenuElement.this.requestFocusInWindow();
          }

        }

        public void mouseReleased(MouseEvent e) {
          ModifierButton.this.refresh();
        }
      });
    }

    public InputContext getInputContext() {
      return null;
    }

    public Component getComponent() {
      return this;
    }

    public MenuElement[] getSubElements() {
      return new MenuElement[0];
    }

    public void menuSelectionChanged(boolean isIncluded) {
    }

    public void processKeyEvent(KeyEvent event, MenuElement[] path, MenuSelectionManager manager) {
    }

    public void processMouseEvent(MouseEvent event, MenuElement[] path, MenuSelectionManager manager) {
    }
  }
}
