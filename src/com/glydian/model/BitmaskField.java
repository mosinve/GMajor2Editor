//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BitmaskField {
  private final int[] bits;
  boolean skipGoffyPoint = true;

  public BitmaskField(int size) {
    this.bits = new int[size];
  }

  public void setBitsForInt(int value, int bitOffset, int bitSize) {
    for(int i = 0; i < bitSize; ++i) {
      int k = 1 << i;
      int l = k & value;
      this.bits[bitOffset + i] = l / k;
    }

  }

  public int getIntFromBits(int bitOffset, int bitSize) {
    int outValue = 0;

    for(int i = 0; i < bitSize; ++i) {
      outValue += this.bits[bitOffset + i] * (1 << i);
    }

    return outValue;
  }

  public void writeBitsToByteArray(byte[] pData, int byteOffset) {
    for(int i = 0; i < this.bits.length / 7; ++i) {
      pData[byteOffset + i] = (byte)(this.bits[i * 7] + this.bits[1 + i * 7] * 2 + this.bits[2 + i * 7] * 4 + this.bits[3 + i * 7] * 8 + this.bits[4 + i * 7] * 16 + this.bits[5 + i * 7] * 32 + this.bits[6 + i * 7] * 64);
    }

  }

  void readBitsFromByteArray(byte[] pData, int byteOffset) throws IOException {
    BitInputStream packedDataStream = new BitInputStream(new ByteArrayInputStream(pData, byteOffset, this.bits.length / 7));
    int goofBitCounter = 0;

    for(int i = 0; i < this.bits.length; ++i) {
      ++goofBitCounter;
      if (this.skipGoffyPoint && goofBitCounter == 8) {
        packedDataStream.readBit();
        goofBitCounter = 1;
      }

      this.bits[i] = packedDataStream.readBit();
    }

  }
}
