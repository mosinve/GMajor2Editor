//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.gui.controls;

import com.glydian.model.DiscreteRange;
import com.glydian.model.IntRange;
import com.glydian.model.Range;
import com.glydian.model.effect.GmEqualizer.EqFrequencyRange;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Arc2D.Double;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JDial extends JComponent {
  private Range valueRange;
  private String label;
  private Font font;
  private double dialValue;
  private Object scaleReading;
  private final JDial.JDialRenderer dialRenderer;
  private boolean mouseOver;
  private transient ChangeEvent changeEvent;
  private boolean layoutsCreated;
  private TextLayout labelLayout;
  private TextLayout valueLayout;
  private TextLayout unitLayout;
  private final Color veryBrightWhite;

  public void setFont(Font font) {
    this.font = font;
  }

  public void setLabel(String string) {
    this.label = string;
  }

  public void setRange(Range range) {
    this.valueRange = range;
  }

  public JDial(JDial.RendererStyle renderingStyle) {
    this.valueRange = null;
    this.label = null;
    this.font = null;
    ChangeListener changeListener = this.createChangeListener();
    this.changeEvent = null;
    this.layoutsCreated = false;
    this.labelLayout = null;
    this.valueLayout = null;
    this.unitLayout = null;
    this.veryBrightWhite = Color.WHITE.brighter().brighter();
    this.setFocusable(true);
    this.dialRenderer = new JDial.JDialRenderer(renderingStyle);
    this.addMouseMotionListener(new MouseMotionListener() {
      public void mouseMoved(MouseEvent e) {
      }

      public void mouseDragged(MouseEvent e) {
        if (!JDial.this.hasFocus()) {
          JDial.this.requestFocusInWindow();
        }

        int x = e.getX();
        int y = e.getY();
        if (JDial.this.dialRenderer.isPointRelated(x, y)) {
          double dig = JDial.this.dialRenderer.computeThetaForMousePoint(x, y);
          double newDialValue = (double)(Math.round(Math.rint((330.0D - dig) / 300.0D * (double)JDial.this.valueRange.getSize())) + (long)JDial.this.valueRange.getIndexForValue(JDial.this.valueRange.getMinValue()));
          if (Math.abs(JDial.this.dialValue - newDialValue) >= 1.0D && JDial.this.valueRange.isIndexValid((int)newDialValue)) {
            JDial.this.dialValue = newDialValue;
            JDial.this.setDialValue(JDial.this.valueRange.getItem((int)JDial.this.dialValue));
          }
        }

      }
    });
    this.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent arg0) {
      }

      public void mouseReleased(MouseEvent arg0) {
      }

      public void mouseEntered(MouseEvent arg0) {
        JDial.this.mouseOver = true;
        JDial.this.repaint();
      }

      public void mouseExited(MouseEvent arg0) {
        JDial.this.mouseOver = false;
        JDial.this.repaint();
      }

      public void mouseClicked(MouseEvent e) {
        if (JDial.this.hasFocus()) {
          int x = e.getX();
          int y = e.getY();
          if (JDial.this.dialRenderer.isPointRelated(x, y)) {
            double dig = JDial.this.dialRenderer.computeThetaForMousePoint(x, y);
            double newDialValue = (double)(Math.round(Math.rint((330.0D - dig) / 300.0D * (double)JDial.this.valueRange.getSize())) + (long)JDial.this.valueRange.getIndexForValue(JDial.this.valueRange.getMinValue()));
            if (newDialValue > JDial.this.dialValue) {
              newDialValue = JDial.this.dialValue + 1.0D;
            } else if (newDialValue < JDial.this.dialValue) {
              newDialValue = JDial.this.dialValue - 1.0D;
            }

            if (newDialValue != JDial.this.dialValue && JDial.this.valueRange.isIndexValid((int)newDialValue)) {
              JDial.this.dialValue = newDialValue;
              JDial.this.setDialValue(JDial.this.valueRange.getItem((int)JDial.this.dialValue));
            }
          }
        } else {
          JDial.this.requestFocusInWindow();
        }

      }
    });
    this.addMouseWheelListener(e -> {
      if (!JDial.this.hasFocus()) {
        JDial.this.requestFocusInWindow();
      }

      double newDialValue;
      if (e.getWheelRotation() > 0) {
        newDialValue = JDial.this.dialValue - 1.0D;
      } else {
        newDialValue = JDial.this.dialValue + 1.0D;
      }

      if (JDial.this.valueRange.isIndexValid((int)newDialValue)) {
        JDial.this.dialValue = newDialValue;
        JDial.this.setDialValue(JDial.this.valueRange.getItem((int)newDialValue));
      }

      e.consume();
    });
    this.addKeyListener(new KeyListener() {
      public void keyTyped(KeyEvent e) {
      }

      public void keyPressed(KeyEvent e) {
        int id = e.getID();
        if (id == 401) {
          int c = e.getKeyCode();
          if (c == 38 || c == 40 || c == 37 || c == 39) {
            switch(c) {
              case 37:
                JDial.this.changeDialValue(JDial.this.dialValue, false, false);
                break;
              case 38:
                JDial.this.changeDialValue(JDial.this.dialValue, true, true);
                break;
              case 39:
                JDial.this.changeDialValue(JDial.this.dialValue, true, false);
                break;
              case 40:
                JDial.this.changeDialValue(JDial.this.dialValue, false, true);
            }
          }
        }

      }

      public void keyReleased(KeyEvent e) {
      }
    });
    this.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        JDial.this.repaint();
      }

      public void focusLost(FocusEvent e) {
        JDial.this.repaint();
      }
    });
  }

  private void changeDialValue(double newDialValue, boolean increment, boolean bigStep) {
    int stepSize = bigStep ? (this.valueRange.getSize() > 10 ? this.valueRange.getSize() / 10 : 1) : 1;
    if (bigStep && !this.valueRange.isIndexValid((int)(increment ? newDialValue + (double)stepSize : newDialValue - (double)stepSize))) {
      stepSize = 1;
    }

    newDialValue = increment ? newDialValue + (double)stepSize : newDialValue - (double)stepSize;
    if (newDialValue != this.dialValue && this.valueRange.isIndexValid((int)newDialValue)) {
      this.dialValue = newDialValue;
      this.setDialValue(this.valueRange.getItem((int)this.dialValue));
    }

  }

  public JDial() {
    this(JDial.RendererStyle.LABEL_BOTTOM_VALUE_BOTTOM);
  }

  public void setDialValue(Object scaleReading) {
    if (this.valueRange != null && this.valueRange.contains(scaleReading.toString())) {
      this.scaleReading = scaleReading;
      this.dialValue = (double)this.valueRange.getIndexForValue(scaleReading.toString());
      double digVal = 330.0D - (this.dialValue - (double)this.valueRange.getIndexForValue(this.valueRange.getMinValue())) / (double)this.valueRange.getSize() * 300.0D;
      this.dialRenderer.setThetaDig(digVal);
      this.repaint();
      this.fireStateChanged();
    } else if (this.valueRange != null) {
      this.dialValue = (double)this.valueRange.getIndexForValue(this.valueRange.getMinValue());
      this.resetSizeUsingGraphics();
    }

  }

  private ChangeListener createChangeListener() {
    return new JDial.ModelListener();
  }

  public void addChangeListener(ChangeListener l) {
    this.listenerList.add(ChangeListener.class, l);
  }

  public void removeChangeListener(ChangeListener l) {
    this.listenerList.remove(ChangeListener.class, l);
  }

  public ChangeListener[] getChangeListeners() {
    return this.listenerList.getListeners(ChangeListener.class);
  }

  private void fireStateChanged() {
    Object[] listeners = this.listenerList.getListenerList();

    for(int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ChangeListener.class) {
        if (this.changeEvent == null) {
          this.changeEvent = new ChangeEvent(this);
        }

        ((ChangeListener)listeners[i + 1]).stateChanged(this.changeEvent);
      }
    }

  }

  public Object getDialValue() {
    return this.scaleReading;
  }

  public void setBounds(int x, int y, int w, int h) {
    this.layoutsCreated = false;
    super.setBounds(x, y, w, h);
  }

  private void resetSizeUsingGraphics() {
    if (this.font != null && this.valueRange != null) {
      Graphics g = this.getGraphics();
      if (g != null) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (this.font != null && this.label != null && this.valueRange != null) {
          this.labelLayout = new TextLayout(this.label, this.font, g2d.getFontRenderContext());
          TextLayout valueLayoutMax = new TextLayout(this.valueRange.getMaxValue(), this.font, g2d.getFontRenderContext());
          TextLayout valueLayoutMin = new TextLayout(this.valueRange.getMinValue(), this.font, g2d.getFontRenderContext());
          if (valueLayoutMax.getBounds().getWidth() > valueLayoutMin.getBounds().getWidth()) {
            this.valueLayout = valueLayoutMax;
          } else {
            this.valueLayout = valueLayoutMin;
          }

          this.unitLayout = new TextLayout(this.valueRange.getUnit(), this.font, g2d.getFontRenderContext());
          this.setLabelBoundsForGraphics(this.labelLayout.getBounds(), this.valueLayout.getBounds(), this.unitLayout.getBounds());
        }

        this.layoutsCreated = this.labelLayout != null && this.valueLayout != null && this.unitLayout != null;
      }
    }

  }

  private void setLabelBoundsForGraphics(Rectangle2D labelRect, Rectangle2D valueRect, Rectangle2D unitRect) {
    this.dialRenderer.reSizesLabelsUsingGraphicsBasedDimensions(new Dimension((int)Math.rint(labelRect.getWidth()), (int)Math.rint(labelRect.getHeight())), new Dimension((int)Math.rint(valueRect.getWidth()), (int)Math.rint(valueRect.getHeight())), new Dimension((int)Math.rint(unitRect.getWidth()), (int)Math.rint(unitRect.getHeight())));
    this.dialRenderer.computeGeometry();
  }

  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    if (!this.layoutsCreated) {
      this.resetSizeUsingGraphics();
    }

    if (this.hasFocus()) {
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      int hSize = 5;
      g2d.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
      g2d.drawLine(this.dialRenderer.dialRect.x, this.dialRenderer.dialRect.y + hSize, this.dialRenderer.dialRect.x, this.dialRenderer.dialRect.y);
      g2d.drawLine(this.dialRenderer.dialRect.x, this.dialRenderer.dialRect.y, this.dialRenderer.dialRect.x + hSize, this.dialRenderer.dialRect.y);
      g2d.drawLine(this.dialRenderer.dialRect.x + this.dialRenderer.dialRect.width - hSize, this.dialRenderer.dialRect.y, this.dialRenderer.dialRect.x + this.dialRenderer.dialRect.width, this.dialRenderer.dialRect.y);
      g2d.drawLine(this.dialRenderer.dialRect.x + this.dialRenderer.dialRect.width, this.dialRenderer.dialRect.y, this.dialRenderer.dialRect.x + this.dialRenderer.dialRect.width, this.dialRenderer.dialRect.y + hSize);
      g2d.drawLine(this.dialRenderer.dialRect.x + this.dialRenderer.dialRect.width - hSize, this.dialRenderer.dialRect.y + this.dialRenderer.dialRect.height, this.dialRenderer.dialRect.x + this.dialRenderer.dialRect.width, this.dialRenderer.dialRect.y + this.dialRenderer.dialRect.height);
      g2d.drawLine(this.dialRenderer.dialRect.x + this.dialRenderer.dialRect.width, this.dialRenderer.dialRect.y + this.dialRenderer.dialRect.height, this.dialRenderer.dialRect.x + this.dialRenderer.dialRect.width, this.dialRenderer.dialRect.y + this.dialRenderer.dialRect.height - hSize);
      g2d.drawLine(this.dialRenderer.dialRect.x, this.dialRenderer.dialRect.y + this.dialRenderer.dialRect.height - hSize, this.dialRenderer.dialRect.x, this.dialRenderer.dialRect.y + this.dialRenderer.dialRect.height);
      g2d.drawLine(this.dialRenderer.dialRect.x, this.dialRenderer.dialRect.y + this.dialRenderer.dialRect.height, this.dialRenderer.dialRect.x + hSize, this.dialRenderer.dialRect.y + this.dialRenderer.dialRect.height);
    }

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(Color.BLACK);
    g2d.fillOval(this.dialRenderer.Xc - this.dialRenderer.radius - 2, this.dialRenderer.Yc - this.dialRenderer.radius - 2, this.dialRenderer.radius * 2 + 4, this.dialRenderer.radius * 2 + 4);
    g2d.setColor(Color.DARK_GRAY);
    g2d.setPaint(new GradientPaint((float)(this.dialRenderer.Xc - this.dialRenderer.radius), (float)(this.dialRenderer.Yc - this.dialRenderer.radius), Color.GRAY, (float)this.dialRenderer.dialRect.width, (float)this.dialRenderer.dialRect.height, Color.white));
    g2d.fillOval(this.dialRenderer.Xc - this.dialRenderer.radius, this.dialRenderer.Yc - this.dialRenderer.radius, this.dialRenderer.radius * 2, this.dialRenderer.radius * 2);
    g2d.setStroke(new BasicStroke(2.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2d.setColor(Color.WHITE);
    g2d.drawOval(this.dialRenderer.Xc - this.dialRenderer.radius + 1, this.dialRenderer.Yc - this.dialRenderer.radius + 1, this.dialRenderer.radius * 2 - 2, this.dialRenderer.radius * 2 - 2);
    g2d.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2d.setColor(Color.WHITE.brighter());
    g2d.drawArc(this.dialRenderer.Xc - this.dialRenderer.radius - 4, this.dialRenderer.Yc - this.dialRenderer.radius - 4, this.dialRenderer.radius * 2 + 8, this.dialRenderer.radius * 2 + 8, -60, 300);
    g2d.setFont(this.font);
    g2d.setColor(this.getForeground());
    if (this.dialRenderer.labelVisible) {
      this.labelLayout.draw(g2d, (float)this.dialRenderer.labelRect.x, (float)(this.dialRenderer.labelRect.y + this.dialRenderer.labelRect.height));
    }

    if (this.dialRenderer.valueVisible) {
      this.unitLayout.draw(g2d, (float)this.dialRenderer.unitRect.x, (float)(this.dialRenderer.unitRect.y + this.dialRenderer.unitRect.height));
    }

    g2d.setColor(Color.WHITE.brighter());
    if (this.dialRenderer.valueVisible && this.scaleReading != null) {
      g2d.setColor(Color.DARK_GRAY.brighter());
      this.valueLayout = new TextLayout(this.scaleReading.toString(), this.font, g2d.getFontRenderContext());
      this.valueLayout.draw(g2d, (float)this.dialRenderer.textRect.x, (float)(this.dialRenderer.textRect.y + this.dialRenderer.textRect.height));
    }

    g2d.setStroke(new BasicStroke(3.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2d.setColor(Color.CYAN.brighter());
    Arc2D arc = null;
    if (this.valueRange.getZeroValue().equals(this.valueRange.getMinValue())) {
      arc = new Double((double)(this.dialRenderer.Xc - this.dialRenderer.radius - 5), (double)(this.dialRenderer.Yc - this.dialRenderer.radius - 5), (double)(this.dialRenderer.radius * 2 + 10), (double)(this.dialRenderer.radius * 2 + 10), 240.0D, -330.0D + Math.toDegrees(this.dialRenderer.getThetaRad()), Arc2D.OPEN);
    } else if (!this.valueRange.getZeroValue().equals(this.valueRange.getMinValue()) && !this.valueRange.getZeroValue().equals(this.valueRange.getMaxValue())) {
      arc = new Double((double)(this.dialRenderer.Xc - this.dialRenderer.radius - 5), (double)(this.dialRenderer.Yc - this.dialRenderer.radius - 5), (double)(this.dialRenderer.radius * 2 + 10), (double)(this.dialRenderer.radius * 2 + 10), 90.0D, Math.toDegrees(this.dialRenderer.getThetaRad()) - 180.0D, Arc2D.OPEN);
    }

    if (arc != null) {
      g2d.draw(arc);
    }

    g2d.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
    g2d.setColor(this.veryBrightWhite);
    g2d.translate(this.dialRenderer.Xc, this.dialRenderer.Yc);
    g2d.rotate(-5.235987755982989D);

    for(int i = 0; i < 11; ++i) {
      g2d.drawLine(this.dialRenderer.radius + 5, 0, this.dialRenderer.radius + 5 + this.dialRenderer.scaleMarkerHeight + 1, 0);
      g2d.rotate(-0.5235987755982988D);
    }

    g2d.setStroke(new BasicStroke(2.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    if (!this.hasFocus() && !this.mouseOver) {
      g2d.setColor(Color.BLACK.brighter());
    } else {
      g2d.setColor(Color.GREEN.brighter());
    }

    g2d.rotate(-this.dialRenderer.getThetaRad());
    g2d.drawLine(3, 0, this.dialRenderer.radius - 4, 0);
    g2d.rotate(this.dialRenderer.getThetaRad());
    g2d.translate(0, 0);
  }

  public Dimension getPreferredSize() {
    this.resetSizeUsingGraphics();
    return this.dialRenderer.getPreferredSize();
  }

  public Dimension getMaximumSize() {
    return this.dialRenderer.getMaximumSize();
  }

  public Dimension getMinimumSize() {
    return this.dialRenderer.getMinimumSize();
  }

  public static void main(String[] args) {
    JPanel p = new JPanel() {
      public void paintComponent(Graphics g) {
        Dimension dim = this.getSize();
        if (this.isOpaque()) {
          Graphics2D g2d = (Graphics2D)g;
          g2d.setPaint(new GradientPaint(0.0F, 0.0F, Color.GRAY, (float)dim.width, (float)dim.height, Color.WHITE));
          g2d.fillRect(0, 0, dim.width, dim.height);
        }

      }
    };
    JFrame f = new JFrame();
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    Font font = new Font("Bitstream Vera Serif", Font.PLAIN, 10);
    IntRange range = new IntRange(-100, 100) {
      public String getUnit() {
        return "dB";
      }
    };
    range.debug();
    final JDial dial1 = new JDial();
    dial1.setRange(range);
    dial1.setDialValue(range.getMinValue());
    dial1.label = "FBHICUT";
    dial1.setFont(font);
    p.add(dial1, "Center");
    dial1.addChangeListener(changeEvent -> System.out.println("VALUE: " + dial1.getDialValue()));
    DiscreteRange range2 = EqFrequencyRange.getInstance();
    range2.debug();
    JDial dial2 = new JDial();
    dial2.setRange(range2);
    dial2.label = "FBLOCUT (OK)";
    dial2.setDialValue(range2.getMinValue());
    dial2.setFont(font);
    p.add(dial2, "Center");
    dial2.addChangeListener(e -> {
    });
    f.getContentPane().add(p);
    f.pack();
    f.setVisible(true);
  }

  static class JDialRenderer {
    private final JDial.RendererStyle style;
    private int labelPosition = 3;
    private int valuePosition = 3;
    private double theta;
    private boolean labelVisible = true;
    private boolean valueVisible = true;
    private int Xc;
    private int Yc;
    private Rectangle dialRect;
    private Rectangle labelRect;
    private Rectangle textRect;
    private Rectangle unitRect;
    private final int radius = 10;
    private final int itemGap = 4;
    private final int scaleMarkerHeight = 2;

    JDialRenderer(JDial.RendererStyle rendererStyle) {
      this.style = rendererStyle;
      this.labelVisible = true;
      this.valueVisible = true;
      if (rendererStyle == JDial.RendererStyle.LABEL_BOTTOM_VALUE_BOTTOM) {
        this.labelPosition = 3;
        this.valuePosition = 3;
      } else if (rendererStyle == JDial.RendererStyle.LABEL_TOP_VALUE_BOTTOM) {
        this.labelPosition = 1;
        this.valuePosition = 3;
      } else if (rendererStyle == JDial.RendererStyle.LABEL_RIGHT_VALUE_RIGHT) {
        this.labelPosition = 4;
        this.valuePosition = 4;
      } else if (rendererStyle == JDial.RendererStyle.LABEL_NULL_VALUE_BOTTOM) {
        this.labelPosition = -1;
        this.valuePosition = 3;
        this.labelVisible = false;
      }

      this.initRenderer();
      this.computeGeometry();
    }

    void initRenderer() {
      this.dialRect = new Rectangle();
      this.labelRect = new Rectangle();
      this.textRect = new Rectangle();
      this.unitRect = new Rectangle();
    }

    void reSizesLabelsUsingGraphicsBasedDimensions(Dimension label, Dimension text, Dimension unit) {
      if (label != null) {
        this.labelRect.width = label.width;
        this.labelRect.height = label.height;
      }

      if (text != null) {
        this.textRect.width = text.width;
        this.textRect.height = text.height;
      }

      if (unit != null) {
        this.unitRect.width = unit.width;
        this.unitRect.height = unit.height;
      }

    }

    void computeGeometry() {
      this.dialRect.x = this.itemGap;
      this.dialRect.y = this.itemGap;
      this.dialRect.width = (this.itemGap + this.scaleMarkerHeight + this.radius + 2) * 2;
      this.dialRect.height = this.dialRect.width;
      this.computeLabelAndDialRects();
      this.computeValueRects();
      this.Xc = this.dialRect.x + this.itemGap + this.scaleMarkerHeight + this.radius + 2;
      this.Yc = this.dialRect.y + this.itemGap + this.scaleMarkerHeight + this.radius + 2;
    }

    private void computeValueRects() {
      if (this.valuePosition == 3) {
        this.textRect.x = this.itemGap;
        this.textRect.y = this.dialRect.y + this.dialRect.height + this.itemGap + (this.labelVisible && this.labelPosition == 3 ? this.labelRect.height + this.itemGap : 0);
      } else if (this.valuePosition == 4) {
        this.textRect.x = this.dialRect.x + this.dialRect.width + this.itemGap;
        this.textRect.y = this.dialRect.y + this.dialRect.height - this.textRect.height - (this.labelVisible && this.labelPosition == 4 ? this.labelRect.height + this.itemGap : 0) + (this.labelVisible && this.labelPosition == 3 ? this.labelRect.height + this.itemGap * 2 : 0);
      }

      this.unitRect.x = this.textRect.x + this.textRect.width + this.itemGap;
      this.unitRect.y = this.textRect.y;
    }

    private void computeLabelAndDialRects() {
      if (this.labelVisible) {
        switch(this.labelPosition) {
          case 1:
            this.labelRect.x = this.itemGap;
            this.labelRect.y = this.itemGap;
            this.dialRect.x = this.itemGap;
            this.dialRect.y = this.itemGap * 2 + this.labelRect.height;
            break;
          case 2:
            this.labelRect.x = this.itemGap;
            this.labelRect.y = this.itemGap + this.dialRect.height - this.labelRect.height;
            this.dialRect.x = this.itemGap * 2 + this.labelRect.width;
            this.dialRect.y = this.itemGap;
            break;
          case 3:
            this.labelRect.x = this.itemGap;
            this.labelRect.y = this.itemGap * 2 + this.dialRect.height;
            this.dialRect.x = this.itemGap;
            this.dialRect.y = this.itemGap;
            break;
          case 4:
            this.labelRect.x = this.itemGap * 2 + this.dialRect.width;
            this.labelRect.y = this.itemGap + this.dialRect.height - this.labelRect.height;
            this.dialRect.x = this.itemGap;
            this.dialRect.y = this.itemGap;
        }
      } else {
        this.dialRect.x = this.itemGap;
        this.dialRect.y = this.itemGap;
      }

    }

    Dimension getMinimumSize() {
      Dimension dim = new Dimension();
      Rectangle rect = new Rectangle();
      rect = rect.union(this.dialRect);
      if (this.labelVisible) {
        rect = rect.union(this.labelRect);
      }

      if (this.valueVisible) {
        rect = rect.union(this.textRect).union(this.unitRect);
      }

      dim.height = rect.height + this.itemGap;
      dim.width = rect.width + this.itemGap;
      return dim;
    }

    Dimension getPreferredSize() {
      Dimension dim = new Dimension();
      Rectangle rect = new Rectangle();
      rect = rect.union(this.dialRect);
      if (this.labelVisible) {
        rect = rect.union(this.labelRect);
      }

      if (this.valueVisible) {
        rect = rect.union(this.textRect).union(this.unitRect);
      }

      dim.height = rect.height + this.itemGap;
      dim.width = rect.width + this.itemGap;
      return dim;
    }

    Dimension getMaximumSize() {
      Dimension dim = new Dimension();
      Rectangle rect = new Rectangle();
      rect = rect.union(this.dialRect);
      if (this.labelVisible) {
        rect = rect.union(this.labelRect);
      }

      if (this.valueVisible) {
        rect = rect.union(this.textRect).union(this.unitRect);
      }

      dim.height = rect.height + this.itemGap;
      dim.width = rect.width + this.itemGap;
      return dim;
    }

    boolean isPointRelated(int x, int y) {
      double dist = Math.sqrt(Math.pow((double)(this.Xc - x), 2.0D) + Math.pow((double)(this.Yc - y), 2.0D));
      return dist - (double)(this.radius - 10) < 20.0D;
    }

    double getThetaRad() {
      return this.theta;
    }

    void setThetaDig(double digAngle) {
      this.theta = Math.toRadians(digAngle);
    }

    double computeThetaForMousePoint(int x, int y) {
      double beta = Math.atan2((double)(x - this.Xc), (double)(y - this.Yc));
      double dig = Math.rint((double)Math.round(Math.toDegrees(beta)));
      return dig > 0.0D ? dig : 360.0D + dig;
    }
  }

  private class ModelListener implements ChangeListener, Serializable {
    private ModelListener() {
    }

    public void stateChanged(ChangeEvent e) {
      JDial.this.fireStateChanged();
    }
  }

  public enum RendererStyle {
    LABEL_BOTTOM_VALUE_BOTTOM,
    LABEL_RIGHT_VALUE_RIGHT,
    LABEL_TOP_VALUE_BOTTOM,
    LABEL_NULL_VALUE_BOTTOM;

    RendererStyle() {
    }
  }
}
