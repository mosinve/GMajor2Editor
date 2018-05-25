//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.gui.controls;

import com.glydian.gui.GLydianSwing;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolTip;

public class J2DButton extends JButton {
  private boolean isUpDownButton = false;
  private Dimension imageSize;

  public J2DButton() {
  }

  public J2DButton(Icon arg0) {
    super(arg0);
  }

  public J2DButton(String arg0) {
    super(arg0);
  }

  public J2DButton(Action arg0) {
    super(arg0);
  }

  public J2DButton(String arg0, Icon arg1) {
    super(arg0, arg1);
  }

  public void setButtonType(J2DButton.UP_DOWN_BUTTON_TYPE pUpDownType) {
    if (pUpDownType == J2DButton.UP_DOWN_BUTTON_TYPE.UP) {
      this.setIcon(GLydianSwing.imageUpArrow);
    } else {
      this.setIcon(GLydianSwing.imageDownArrow);
    }

    this.isUpDownButton = true;
    this.imageSize = new Dimension(GLydianSwing.imageUpArrow.getIconWidth() + 10, GLydianSwing.imageUpArrow.getIconHeight() + 2);
  }

  public Dimension getPreferredSize() {
    return this.isUpDownButton ? this.imageSize : super.getPreferredSize();
  }

  public Dimension getMinimumSize() {
    return this.imageSize;
  }

  public JToolTip createToolTip() {
    return new J2DButton.CustomToolTip(this);
  }

  class CustomToolTip extends JToolTip {
    CustomToolTip(JComponent component) {
      this.setComponent(component);
      this.setBackground(Color.yellow);
      Font tipFont = J2DButton.this.getFont();
      tipFont = tipFont.deriveFont(Font.ITALIC, 12.0F);
      this.setFont(tipFont);
    }

    public void setTipText(String tipText) {
      String tip = "<html>" + tipText + "</html>";
      super.setTipText(tip);
    }
  }

  public enum UP_DOWN_BUTTON_TYPE {
    UP,
    DOWN;

    UP_DOWN_BUTTON_TYPE() {
    }
  }
}
