//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.gui.controls;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.Border;

public class J2DTextFieldBorder implements Border {
  private final Insets tfInsets = new Insets(4, 10, 4, 10);

  public J2DTextFieldBorder() {
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2.setStroke(new BasicStroke(1.5F));
    g2.setColor(Color.BLACK);
    g2.drawRect(x, y, width - 2, height - 2);
  }

  public Insets getBorderInsets(Component c) {
    return this.tfInsets;
  }

  public boolean isBorderOpaque() {
    return true;
  }
}
