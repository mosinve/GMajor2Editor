//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

class BitInputStream {
  private InputStream in;
  private int buffer;
  private int nextBit = 8;

  public BitInputStream(InputStream in) {
    this.in = in;
  }

  public synchronized int readBit() throws IOException {
    if (this.in == null) {
      throw new IOException("Already closed");
    } else {
      int bitsInByte = 8;
      if (this.nextBit == bitsInByte) {
        this.buffer = this.in.read();
        if (this.buffer == -1) {
          throw new EOFException();
        }

        this.nextBit = 0;
      }

      int bit = this.buffer & 1 << this.nextBit;
      ++this.nextBit;
      bit = bit == 0 ? 0 : 1;
      return bit;
    }
  }

  public void close() throws IOException {
    this.in.close();
    this.in = null;
  }
}
