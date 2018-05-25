//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.glydian.gui.GLydianSwing;
import java.awt.Dimension;
import javax.swing.*;

class GLydian {
  public GLydian() {
  }

  public static void main(String[] args) {
    System.out.println("GLydian Patch editor for T.C.Electronics G-MAJOR GUITAR PROCESSOR");
    System.out.println("Version 1.5 CR ");
    System.out.println("Author : Nandana Perera");
    System.out.println("Adapted By : Vitaly Mosin");
    System.out.println("Thanks to DowntownPauly for the wonderful MIDI deciphering work!");
    System.out.println("This is a beta version of the program. It may contain bugs and unfinished features. We depend on your feedback, So please give us your bug reports and feature requests");
    System.out.println("THIS PROGRAM IS PROVIDED \"AS IS\" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, \nTHE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. \nTHE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. \nSHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION. ");

    try {
      System.out.println("Lfs: " + UIManager.getCrossPlatformLookAndFeelClassName() + " / " + UIManager.getSystemLookAndFeelClassName());
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception var4) {
      var4.printStackTrace();
    }

    JDialog.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("GLydian (G-MAJOR GUITAR PROCESSOR EDITOR)");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    GLydianSwing gm = new GLydianSwing(frame);
    JScrollPane scrollPane = new JScrollPane(gm);
    frame.add(scrollPane, "Center");
    frame.setMinimumSize(new Dimension(1024, 600));
    frame.pack();
    System.out.println("height: " + frame.getSize().getHeight() + " Width: " + frame.getSize().getWidth());
    frame.setVisible(true);
  }
}
