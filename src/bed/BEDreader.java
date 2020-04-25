package bed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BEDreader {

  private String filePath = "";

  private Scanner scanner = null;

  public BEDreader(String filePath) {
    this.filePath = filePath;
    try {
      this.scanner = new Scanner(new FileInputStream(filePath));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public String getFilePath() {
    return this.filePath;
  }

  public boolean hasNext() {
    return this.scanner.hasNext();
  }

  public BEDline nextLine() {
    if (this.scanner.hasNextLine() == false) {
      this.scanner.close();
      return null;
    }
    String line = this.scanner.nextLine();
    String[] tokens = line.split("\t");
    if (tokens.length < 3) {
      // a valid BED line must be filled with over 3 fields
      return null;
    }
    BEDline bedline = null;
    try {
      bedline = new BEDline(tokens[0], Long.valueOf(tokens[1]),
          Long.valueOf(tokens[2]));
    } catch (NumberFormatException e) {
      return null;
    }
    if (tokens.length >= 4) {
      bedline.setRegionName(tokens[3]);
    }
    if (tokens.length >= 5) {
      try {
        bedline.setGreyLevel(Integer.valueOf(tokens[4]));
      } catch (NumberFormatException e) {
        bedline.setGreyLevel(0);
      }
    }
    if (tokens.length >= 6) {
      if (tokens[5].equals("+")) {
        bedline.setStrand(BEDline.PLUS_STRAND);
      } else if (tokens[5].equals("-")) {
        bedline.setStrand(BEDline.MINUS_STRAND);
      } else {
        return null;
      }
    }
    if (tokens.length >= 8) {
      try {
        bedline.setThickBegin(Long.valueOf(tokens[6]));
        bedline.setThickEnd(Long.valueOf(tokens[7]));
      } catch (NumberFormatException e) {
        return null;
      }
    }
    if (tokens.length >= 9) {
      bedline.setRGB(tokens[8]);
    }
    if (tokens.length >= 10) {
      try {
        int blockCount = Integer.valueOf(tokens[9]);
        bedline.setBlockCount(blockCount);
        if (blockCount == 0) {
          return bedline;
        } else {
          if (tokens.length == 12) {
            String[] strblockLengths = tokens[10].split(",");
            String[] strblockOffsets = tokens[11].split(",");
            int[] blockLengths = new int[strblockLengths.length];
            int[] blockOffsets = new int[strblockOffsets.length];
            for (int i = 0; i < blockCount; i++) {
              blockLengths[i] = Integer.valueOf(strblockLengths[i]);
              blockOffsets[i] = Integer.valueOf(strblockOffsets[i]);
            }
            bedline.setBlocksLength(blockLengths);
            bedline.setBlocksOffset(blockOffsets);
          }
        }
      } catch (Exception e) {
        return null;
      }
    }
    return bedline;
  }

}
