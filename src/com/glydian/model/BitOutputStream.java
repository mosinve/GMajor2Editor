//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import java.io.IOException;
import java.io.OutputStream;

class BitOutputStream {
  private OutputStream out;
  private int buffer;
  private int bitCount;

  public BitOutputStream(OutputStream out) {
    this.out = out;
  }

  public synchronized void writeBit(int bit) throws IOException {
    if (this.out == null) {
      throw new IOException("Already closed");
    } else if (bit != 0 && bit != 1) {
      throw new IOException(bit + " is not a bit");
    } else {
      this.buffer |= bit << this.bitCount;
      ++this.bitCount;
      if (this.bitCount == 8) {
        this.flush();
      }

    }
  }

  private void flush() throws IOException {
    if (this.bitCount > 0) {
      this.out.write((byte)this.buffer);
      this.bitCount = 0;
      this.buffer = 0;
    }

  }

  public void close() throws IOException {
    this.flush();
    this.out.close();
    this.out = null;
  }
}
