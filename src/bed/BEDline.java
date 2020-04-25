package bed;

public class BEDline {
  public static final String STR_UNSET = "";
  public static final int NUM_UNSET = -1;

  public static final int PLUS_STRAND = 1;
  public static final int MINUS_STRAND = 0;

  private String chromesome = STR_UNSET;
  private long transRegionBegin = NUM_UNSET;
  private long transRegionEnd = NUM_UNSET;
  private String regionName = STR_UNSET;
  private int greyLevel = NUM_UNSET;
  private int strand = NUM_UNSET;
  private long thickBegin = NUM_UNSET;
  private long thickEnd = NUM_UNSET;
  private String RGB = STR_UNSET;
  private int blockCount = NUM_UNSET;
  private int[] blockLength = null;
  private int[] blockOffset = null;

  public BEDline(String chromesome, long transRegionBegin,
      long transRegionEnd) {
    this.chromesome = chromesome;
    this.transRegionBegin = transRegionBegin;
    this.transRegionEnd = transRegionEnd;
  }

  public boolean hasRegionName() {
    if (this.regionName.equals(STR_UNSET)) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasStrand() {
    if (this.strand == NUM_UNSET) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasThick() {
    if (this.thickBegin == NUM_UNSET || this.thickEnd == NUM_UNSET) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasRGB() {
    if (this.RGB.equals(STR_UNSET)) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasBlock() {
    if (this.blockCount == NUM_UNSET) {
      return false;
    } else {
      return true;
    }
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public void setGreyLevel(int greyLevel) {
    this.greyLevel = greyLevel;
  }

  public void setStrand(int strand) {
    if (strand != PLUS_STRAND && strand != MINUS_STRAND) {
      System.out.println("error: strand invalid - " + strand);
    } else {
      this.strand = strand;
    }
  }

  public void setThickBegin(long thickBegin) {
    if (thickBegin >= 0) {
      this.thickBegin = thickBegin;
    } else {
      System.out.println("error: thick begin invalid - " + thickBegin);
    }
  }

  public void setThickEnd(long thickEnd) {
    if (thickEnd >= 0) {
      this.thickEnd = thickEnd;
    } else {
      System.out.println("error: thick end invalid - " + thickEnd);
    }
  }

  public void setRGB(String RGB) {
    this.RGB = RGB;
  }

  public void setBlockCount(int blockCount) {
    if (blockCount >= 0) {
      this.blockCount = blockCount;
    } else {
      System.out.println("error: block count invalid - " + blockCount);
    }
  }

  public void setBlocksLength(int[] blockLength) {
    this.blockLength = blockLength.clone();
  }

  public void setBlockLength(int blockLength, int index) {
    if (index < this.blockCount) {
      if (blockLength > 0) {
        this.blockLength[index] = blockLength;
      } else {
        System.out.println("error: block length invalid - " + blockLength);
      }
    } else {
      System.out.println("error: block index out of block count");
    }
  }

  public void setBlocksOffset(int[] blockOffset) {
    this.blockOffset = blockOffset.clone();
  }

  public void setBlockOffset(int blockOffset, int index) {
    if (index < this.blockCount) {
      if (blockOffset >= 0) {
        this.blockOffset[index] = blockOffset;
      } else {
        System.out.println("error: block Offset invalid - " + blockOffset);
      }
    } else {
      System.out.println("error: block index out of block count");
    }
  }

  public String getChromesome() {
    return this.chromesome;
  }

  public long getTransRegionBegin() {
    return this.transRegionBegin;
  }

  public long getTransRegionEnd() {
    return this.transRegionEnd;
  }

  public String getRegionName() {
    return this.regionName;
  }

  public int getGreyLevel() {
    return this.greyLevel;
  }

  public int getStrand() {
    return this.strand;
  }

  public long getThickBegin() {
    return this.thickBegin;
  }

  public long getThickEnd() {
    return this.thickEnd;
  }

  public String getRGB() {
    return this.RGB;
  }

  public int getBlockCount() {
    return this.blockCount;
  }

  public int getBlockLength(int blockIndex) {
    if (blockIndex < this.blockCount) {
      return this.blockLength[blockIndex];
    } else {
      return -1;
    }
  }

  public int getBlockOffset(int blockIndex) {
    if (blockIndex < this.blockCount) {
      return this.blockOffset[blockIndex];
    } else {
      return -1;
    }
  }

  @Override
  public String toString() {
    String str = "";
    str = this.chromesome + "\t" + this.transRegionBegin + "\t"
        + this.transRegionEnd + "\t" + this.regionName + "\t" + this.greyLevel
        + "\t" + this.strand + "\t" + this.thickBegin +
        "\t" + this.thickEnd + "\t" + this.RGB + "\t" + this.blockCount + "\t"
        + this.blockLength + "\t" + this.blockOffset;
    return str;
  }

}
