//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.glydian.model;

import java.util.Arrays;
import java.util.List;

public abstract class DiscreteRange implements Range {
  private final int mIndexOffset;
  private final int mValueOffset;
  private final int mSize;
  private final List<String> mValueArray;
  private final int startIndex;
  private final int endIndex;
  private String maxCharacterWidthValue = null;
  private static final String[] COMP_TIME_VALUES = { "1.0  ", "1.4  ", "2.0  ", "3.0  ", "5.0  ", "7.0  ", "10   ", "14   ", "20   ", "30   ", "50   ", "70   ", "100  ", "140  ", "200  ", "300  ", "500  ", "700  ", "1000 ", "1400", "2000 " };


  private static final String[] COMP_RATIO_VALUES = { "Off", "1.12:1", "1.25:1", "1.40:1", "1.60:1", "1.80:1", "2.0:1", "2.5:1", "3.2:1", "4.0:1", "5.6:1", "8.0:1", "16:1",
          "32:1", "64:1", "Inf:1" };

  private static final String[] TAP_TEMPO_VALUES = { "Ignored", "1 ", "1/2D", "1/2", "1/2T", "1/4D", "1/4", "1/4T", "1/8D", "1/8", "1/8T", "1/16D", "1/16", "1/16T", "1/32D", "1/32", "1/32T" };

  private static final String[] SPEED_VALUES = { ".050 ", ".052 ", ".053 ", ".055 ", ".056 ", ".058 ", ".060 ", ".061 ", ".063 ",
          ".065 ", ".067 ", ".069 ", ".071 ", ".073 ", ".075 ", ".077 ", ".079 ", ".082 ", ".084 ",
          ".087 ", ".089 ", ".092 ", ".094 ", ".097 ", ".100 ", ".103 ", ".106 ", ".109 ", ".112 ",
          ".115 ", ".119 ", ".122 ", ".126 ", ".130 ", ".133 ", ".137 ", ".141 ", ".145 ", ".150 ",
          ".154 ", ".158 ", ".163 ", ".168 ", ".173 ", ".178 ", ".183 ", ".188 ", ".194 ", ".200 ",
          ".205 ", ".211 ", ".218 ", ".224 ", ".230 ", ".237 ", ".244 ", ".251 ", ".259 ", ".266 ",
          ".274 ", ".282 ", ".290 ", ".299 ", ".307 ", ".316 ", ".325 ", ".335 ", ".345 ", ".355 ",
          ".365 ", ".376 ", ".387 ", ".398 ", ".410 ", ".422 ", ".434 ", ".447 ", ".460 ", ".473 ",
          ".487 ", ".501 ", ".516 ", ".531 ", ".546 ", ".562 ", ".579 ", ".596 ", ".613 ", ".631 ",
          ".649 ", ".668 ", ".688 ", ".708 ", ".729 ", ".750 ", ".772 ", ".794 ", ".818 ", ".841 ",
          ".866 ", ".891 ", ".917 ", ".944 ", ".972 ", "1.00 ", "1.03 ", "1.06 ", "1.09 ", "1.12 ",
          "1.15 ", "1.19 ", "1.22 ", "1.26 ", "1.30 ", "1.33 ", "1.37 ", "1.41 ", "1.45 ", "1.50 ",
          "1.54 ", "1.58 ", "1.63 ", "1.68 ", "1.73 ", "1.78 ", "1.83 ", "1.88 ", "1.94 ", "2.00 ",
          "2.05 ", "2.11 ", "2.18 ", "2.24 ", "2.30 ", "2.37 ", "2.44 ", "2.51 ", "2.59 ", "2.66 ",
          "2.74 ", "2.82 ", "2.90 ", "2.99 ", "3.07 ", "3.16 ", "3.25 ", "3.35 ", "3.45 ", "3.55 ",
          "3.65 ", "3.76 ", "3.87 ", "3.98 ", "4.10 ", "4.22 ", "4.34 ", "4.47 ", "4.60 ", "4.73 ",
          "4.87 ", "5.01 ", "5.16 ", "5.31 ", "5.46 ", "5.62 ", "5.79 ", "5.96 ", "6.13 ", "6.31 ",
          "6.49 ", "6.68 ", "6.88 ", "7.08 ", "7.29 ", "7.50 ", "7.72 ", "7.94 ", "8.18 ", "8.41 ",
          "8.66 ", "8.91 ", "9.17 ", "9.44 ", "9.72 ", "10.00", "10.29", "10.59", "10.90", "11.22",
          "11.55", "11.89", "12.23", "12.59", "12.96", "13.34", "13.72", "14.13", "14.54", "14.96",
          "15.40", "15.85", "16.31", "16.79", "17.28", "17.78", "18.30", "18.84", "19.39", "19.95" };

  private static final String[] HI_CUT_VALUES = { "19.95",
          "22.39", "25.12", "28.18", "31.62", "35.48", "39.81", "44.67", "50.12", "56.23", "63.10",
          "70.79", "79.43", "89.13", "100.0", "112.2", "125.9", "141.3", "158.5", "177.8", "199.5",
          "223.9", "251.2", "281.8", "316.2", "354.8", "398.1", "446.7", "501.2", "562.3", "631.0",
          "707.9", "794.3", "891.3", "1.00k", "1.12k", "1.26k", "1.41k", "1.58k", "1.78k", "2.00k",
          "2.24k", "2.51k", "2.82k", "3.16k", "3.55k", "3.98k", "4.47k", "5.01k", "5.62k", "6.31k",
          "7.08k", "7.94k", "8.91k", "10.0k", "11.2k", "12.6k", "14.1k", "15.8k", "17.8k", "Off  " };

  private static final String[] LO_CUT_VALUES = { "Off  ",
          "22.39", "25.12", "28.18", "31.62", "35.48", "39.81", "44.67", "50.12", "56.23", "63.10",
          "70.79", "79.43", "89.13", "100.0", "112.2", "125.9", "141.3", "158.5", "177.8", "199.5",
          "223.9", "251.2", "281.8", "316.2", "354.8", "398.1", "446.7", "501.2", "562.3", "631.0",
          "707.9", "794.3", "891.3", "1.00k", "1.12k", "1.26k", "1.41k", "1.58k", "1.78k", "2.00k" };

  private static final String[] EQ_WIDTH_VALUES = { "0.2 ", "0.25", "0.32", "0.4 ", "0.5 ", "0.63", "0.8 ", "1.0 ", "1.25", "1.6 ", "2.0 ", "2.5 ", "3.2 ", "4.0 " };

  private static final String[] EQ_FREQ_VALUES = { "40.97", "42.17", "43.40", "44.67", "45.97", "47.32", "48.70",
          "50.12", "51.58", "53.09", "54.64", "56.23", "57.88", "59.57", "61.31", "63.10", "64.94",
          "66.83", "68.79", "70.79", "72.86", "74.99", "77.18", "79.43", "81.75", "84.14", "86.60",
          "89.13", "91.73", "94.41", "97.16", "100.0", "102.9", "105.9", "109.0", "112.2", "115.5",
          "118.9", "122.3", "125.9", "129.6", "133.4", "137.2", "141.3", "145.4", "149.6", "154.0",
          "158.5", "163.1", "167.9", "172.8", "177.8", "183.0", "188.4", "193.9", "199.5", "205.4",
          "211.3", "217.5", "223.9", "230.4", "237.1", "244.1", "251.2", "258.5", "266.1", "273.8",
          "281.8", "290.1", "298.5", "307.3", "316.2", "325.5", "335.0", "344.7", "354.8", "365.2",
          "375.8", "386.8", "398.1", "409.7", "421.7", "434.0", "446.7", "459.7", "473.2", "487.0",
          "501.2", "515.8", "530.9", "546.4", "562.3", "578.8", "595.7", "613.1", "631.0", "649.4",
          "668.3", "687.9", "707.9", "728.6", "749.9", "771.8", "794.3", "817.5", "841.4", "866.0",
          "891.3", "917.3", "944.1", "971.6", "1.00k", "1.03k", "1.06k", "1.09k", "1.12k", "1.15k",
          "1.19k", "1.22k", "1.26k", "1.30k", "1.33k", "1.37k", "1.41k", "1.45k", "1.50k", "1.54k",
          "1.58k", "1.63k", "1.68k", "1.73k", "1.78k", "1.83k", "1.88k", "1.94k", "2.00k", "2.05k",
          "2.11k", "2.18k", "2.24k", "2.30k", "2.37k", "2.44k", "2.51k", "2.59k", "2.66k", "2.74k",
          "2.82k", "2.90k", "2.99k", "3.07k", "3.16k", "3.25k", "3.35k", "3.45k", "3.55k", "3.65k",
          "3.76k", "3.87k", "3.98k", "4.10k", "4.22k", "4.34k", "4.47k", "4.60k", "4.73k", "4.87k",
          "5.01k", "5.16k", "5.31k", "5.46k", "5.62k", "5.79k", "5.96k", "6.13k", "6.31k", "6.49k",
          "6.68k", "6.88k", "7.08k", "7.29k", "7.50k", "7.72k", "7.94k", "8.18k", "8.41k", "8.66k",
          "8.91k", "9.17k", "9.44k", "9.72k", "10.0k", "10.3k", "10.6k", "10.9k", "11.2k", "11.5k",
          "11.9k", "12.2k", "12.6k", "13.0k", "13.3k", "13.7k", "14.1k", "14.5k", "15.0k", "15.4k",
          "15.8k", "16.3k", "16.8k", "17.3k", "17.8k", "18.3k", "18.8k", "19.4k", "20.0k", "Off  " };

  private static final String[] REVERB_SIZE_VALUES = { "Box", "Tiny", "Small", "Medium", "Large", "Ex Large", "Grand", "Huge" };
  private static final String[] REVERB_HI_COLOR_VALUES = { "Wool", "Warm", "Real", "Clear", "Bright", "Crisp", "Grand" };
  private static final String[] REVERB_LO_COLOR_VALUES = { "Thick", "Round", "Real", "Light", "Tight", "Thin", "No Base" };
  protected abstract String getName();

  public int getSize() {
    return this.mSize;
  }

  public String getMinValue() {
    return this.getItem(this.mIndexOffset + this.mValueOffset);
  }

  public String getMaxValue() {
    return this.getItem(this.mIndexOffset + this.mValueOffset + this.mSize);
  }

  public String getZeroValue() {
    return this.getMinValue();
  }

  public boolean contains(String pValue) {
    return this.mValueArray.contains(pValue);
  }

  public boolean isIndexValid(int idx) {
    return idx - this.mIndexOffset >= this.startIndex && idx - this.mIndexOffset <= this.endIndex;
  }

  public String getItem(int pIndex) {
    if (this.isIndexValid(pIndex)) {
      return this.mValueArray.get(pIndex - this.mIndexOffset);
    } else {
      System.out.println(">>>>> ERROR!! " + this.getName() + ": Invalid Index " + pIndex + " ; index must be between " + (this.startIndex + this.mIndexOffset) + " and " + (this.endIndex + this.mIndexOffset) + " .");
      return this.getMinValue();
    }
  }

  public int getIndexForValue(String pValue) {
    if (this.mValueArray.contains(pValue)) {
      return this.mValueArray.indexOf(pValue) + this.mIndexOffset;
    } else {
      throw new RuntimeException("DiscreteRange: Invalid value " + pValue);
    }
  }

  protected DiscreteRange(DiscreteRange.DISCRETE_RANGE_TYPE type, String minValue, String maxValue) {
    this.mValueArray = Arrays.asList(type.values);
    this.mIndexOffset = type.index;
    this.startIndex = this.mValueArray.indexOf(minValue);
    this.mValueOffset = this.startIndex;
    this.endIndex = this.mValueArray.indexOf(maxValue);
    this.mSize = this.endIndex - this.startIndex;

    assert this.startIndex > this.endIndex;

  }

  public void debug() {
    System.out.println(" mIndexOffset: " + this.mIndexOffset + " mValueOffset: " + this.mValueOffset + " mSize: " + this.mSize);
    System.out.println(" getMinValue():" + this.getMinValue() + " getMaxValue():" + this.getMaxValue());
    System.out.println(" getIndexForValue(getMinValue()):" + this.getIndexForValue(this.getMinValue()) + " getIndexForValue(getMaxValue()):" + this.getIndexForValue(this.getMaxValue()));
  }

//  protected void initBounds(int pIndexOffset, int pValueOffset, int pSize, String[] pValues) {
//    this.mValueArray = Arrays.asList(pValues);
//    this.mIndexOffset = pIndexOffset;
//    this.mValueOffset = pValueOffset;
//    this.mSize = pSize;
//
//    assert pValues.length + this.mValueOffset > this.mIndexOffset && this.mIndexOffset >= this.mValueOffset;
//  }

  public String getMaxCharacterWidthValue() {
    if (this.maxCharacterWidthValue == null) {
      this.maxCharacterWidthValue = " ";

      for(int i = this.mValueOffset; i < this.mSize; ++i) {
        if (this.mValueArray.get(i).trim().length() > this.maxCharacterWidthValue.length()) {
          this.maxCharacterWidthValue = this.mValueArray.get(i);
        }
      }
    }

    return this.maxCharacterWidthValue;
  }

  public static class CompTimeRange extends DiscreteRange {
    final String unit = "ms";
    static final DiscreteRange.CompTimeRange meAttack = new DiscreteRange.CompTimeRange("1.0  ", "70   ");
    static final DiscreteRange.CompTimeRange meRelease = new DiscreteRange.CompTimeRange("50   ", "2000 ");
    static final DiscreteRange.CompTimeRange meDelayRelease = new DiscreteRange.CompTimeRange("20   ", "1000 ");

    public String getUnit() {
      return this.unit;
    }

    public String getName() {
      return "CompTimeRange";
    }

    CompTimeRange(String minValue, String maxValue) {
      super(DiscreteRange.DISCRETE_RANGE_TYPE.COMP_TIME, minValue, maxValue);
    }

    public static DiscreteRange.CompTimeRange getAttackInstance() {
      return meAttack;
    }

    public static DiscreteRange.CompTimeRange getReleaseInstance() {
      return meRelease;
    }

    public static DiscreteRange.CompTimeRange getDelayReleaseInstance() {
      return meDelayRelease;
    }
  }

  public enum DISCRETE_RANGE_TYPE {
    COMP_TIME(3, DiscreteRange.COMP_TIME_VALUES),
    COMP_RATIO(0, DiscreteRange.COMP_RATIO_VALUES),
    TAP_TEMPO(0, DiscreteRange.TAP_TEMPO_VALUES),
    SPEED(0, DiscreteRange.SPEED_VALUES),
    HI_CUT(0, DiscreteRange.HI_CUT_VALUES),
    LO_CUT(0, DiscreteRange.LO_CUT_VALUES),
    EQ_WIDTH(3, DiscreteRange.EQ_WIDTH_VALUES),
    EQ_FREQ(25, DiscreteRange.EQ_FREQ_VALUES),
    REVERB_SIZE(0, DiscreteRange.REVERB_SIZE_VALUES),
    REVERB_HI_COLOR(0, DiscreteRange.REVERB_HI_COLOR_VALUES),
    REVERB_LO_COLOR(0, DiscreteRange.REVERB_LO_COLOR_VALUES);

    private final int index;
    private final String[] values;

    DISCRETE_RANGE_TYPE(int idx, String[] val) {
      this.index = idx;
      this.values = val;
    }
  }

  public static class ReverbHiColorRange extends DiscreteRange {
    final String unit = " ";
    static final DiscreteRange.ReverbHiColorRange me = new DiscreteRange.ReverbHiColorRange();

    public String getUnit() {
      return this.unit;
    }

    public String getName() {
      return "ReverbHiColorRange";
    }

    private ReverbHiColorRange() {
      super(DiscreteRange.DISCRETE_RANGE_TYPE.REVERB_HI_COLOR, "Wool", "Grand");
    }

    public static DiscreteRange.ReverbHiColorRange getInstance() {
      return me;
    }
  }

  public static class ReverbLoColorRange extends DiscreteRange {
    final String unit = " ";
    static final DiscreteRange.ReverbLoColorRange me = new DiscreteRange.ReverbLoColorRange();

    public String getUnit() {
      return this.unit;
    }

    public String getName() {
      return "ReverbLoColorRange";
    }

    private ReverbLoColorRange() {
      super(DiscreteRange.DISCRETE_RANGE_TYPE.REVERB_LO_COLOR, "Thick", "No Base");
    }

    public static DiscreteRange.ReverbLoColorRange getInstance() {
      return me;
    }
  }

  public static class ReverbSizeRange extends DiscreteRange {
    final String unit = " ";
    static final DiscreteRange.ReverbSizeRange me = new DiscreteRange.ReverbSizeRange();

    public String getUnit() {
      return this.unit;
    }

    public String getName() {
      return "ReverbSizeRange";
    }

    private ReverbSizeRange() {
      super(DiscreteRange.DISCRETE_RANGE_TYPE.REVERB_SIZE, "Box", "Huge");
    }

    public static DiscreteRange.ReverbSizeRange getInstance() {
      return me;
    }
  }

  public static class TapTempoRange extends DiscreteRange {
    final String unit = " ";
    static final DiscreteRange.TapTempoRange me = new DiscreteRange.TapTempoRange();

    public String getUnit() {
      return this.unit;
    }

    public String getName() {
      return "TapTempoRange";
    }

    private TapTempoRange() {
      super(DiscreteRange.DISCRETE_RANGE_TYPE.TAP_TEMPO, "Ignored", "1/32T");
    }

    public static DiscreteRange.TapTempoRange getInstance() {
      return me;
    }
  }
}
