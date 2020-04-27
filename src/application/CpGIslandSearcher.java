package application;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bed.BEDline;

public class CpGIslandSearcher {
  public static final String FASTA_FILEPATH1 = "src/testcase/chr22.hg19.fa";
  public static final String FASTA_FILEPATH2 = "src/testcase/chr22.hg19_test.fa";

  private final int cpgLength = 1000;

  private List<Long> promotersPos = new ArrayList<>();
  private List<String> CpGIslands = new ArrayList<>();

  private long bpStartPos = 0;
  private int bpPerLine = 0;
  private int lineShifterLength = 0;

  public CpGIslandSearcher(List<BEDline> sortedBEDlines) {
    this.extractPromotersPos(sortedBEDlines);
//    this.testRandomAccessFile();
  }

  public static void main(String[] args) {
    TopKGenomeSearcher searcher = new TopKGenomeSearcher(
        TopKGenomeSearcher.CHROMESOME, TopKGenomeSearcher.K);
    CpGIslandSearcher cpg = new CpGIslandSearcher(
        searcher.searchFile(TopKGenomeSearcher.TEST_FILEPATH1));
    List<String> result = cpg.searchFastaFile(FASTA_FILEPATH1);
    for (int i = 0; i < result.size(); i++) {
      System.out.println("promoter(" + (i + 1) + "): " + result.get(i));
    }
  }

  public List<String> searchFastaFile(String filePath) {
    this.accessArgmuments(filePath);
    System.out.println(
        "bpStartPos: " + this.bpStartPos + ", bpPerLine: " + this.bpPerLine);
    try {
      RandomAccessFile reader = new RandomAccessFile(new File(filePath), "r");
      byte[] buffer = new byte[this.bpPerLine];
      byte[] lineShifterBuffer = new byte[this.lineShifterLength];

      for (int i = 0; i < this.promotersPos.size(); i++) {
        long startPos = this.promotersPos.get(i); // assume it's 0-base position
        long lineIndex = startPos / this.bpPerLine; // 0-base position
        long bpRemained = this.cpgLength;

        long filePointer = this.bpStartPos
            + lineIndex * (this.bpPerLine + this.lineShifterLength);
        reader.seek(filePointer);

        StringBuilder cpg = new StringBuilder("");
        long firstIndex = startPos - this.bpPerLine * lineIndex;
        long endIndex = (this.cpgLength + firstIndex - 1) % this.bpPerLine;
        while (bpRemained > 0) {
          reader.read(buffer);
          reader.read(lineShifterBuffer);
          String lineRead = new String(buffer);
          StringBuilder extractor = new StringBuilder(lineRead);
          if (bpRemained == this.cpgLength) { // process the first line
            if (this.cpgLength <= (this.bpPerLine - firstIndex)) {
              // if CpG is very short - shorter than bpPerLine
              cpg.append(
                  extractor.substring((int) firstIndex, (int) (endIndex + 1)));
              break;
            } else {
              // else, need to process other lines
              cpg.append(extractor.substring((int) firstIndex));
              bpRemained = bpRemained - (this.bpPerLine - firstIndex);
            }
          } else {
            // process the following lines
            if (bpRemained <= this.bpPerLine) {
              // if bpRemained is smaller than bpPerLine, then this is the end line
              cpg.append(extractor.substring(0, (int) (endIndex + 1)));
              bpRemained = bpRemained - (endIndex + 1);
            } else {
              // else, extract all bp of this line
              cpg.append(extractor);
              bpRemained = bpRemained - this.bpPerLine;
            }
          }
        }
        this.CpGIslands.add(cpg.toString());
//        System.out.println("searched:\n" + cpg.toString());
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>(this.CpGIslands);
  }

  @SuppressWarnings("unused")
  private void testRandomAccessFile() {
    this.accessArgmuments(FASTA_FILEPATH2);
    System.out.println(
        "bpStartPos: " + this.bpStartPos + ", bpPerLine: " + this.bpPerLine);
    try {
      RandomAccessFile reader = new RandomAccessFile(new File(FASTA_FILEPATH2),
          "r");
      byte[] buffer = new byte[this.bpPerLine];
      byte[] lineShifterBuffer = new byte[this.lineShifterLength];

      reader.seek(this.bpStartPos);

      int hasData = 1;
      int loopCount = 1;
      while (hasData != -1) {
        reader.read(buffer);
        hasData = reader.read(lineShifterBuffer);
        System.out
            .println("line" + loopCount + ": \n|" + new String(buffer) + "|");
        loopCount++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void extractPromotersPos(List<BEDline> sortedBEDlines) {
    for (int i = 0; i < sortedBEDlines.size(); i++) {
      long promoterPos = sortedBEDlines.get(i).getTransRegionBegin();
      this.promotersPos.add(promoterPos - this.cpgLength);
    }
  }

  private void accessArgmuments(String filePath) {
    try {
      Scanner scanner = new Scanner(new FileInputStream(filePath));
      if (scanner.hasNextLine()) {
        String firstLine = scanner.nextLine(); // ignore this line
        this.bpStartPos = firstLine.length();
        if (scanner.hasNextLine()) {
          this.bpPerLine = scanner.nextLine().length();
        }
      }
      scanner.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    BufferedInputStream bis;
    try {
      bis = new BufferedInputStream(new FileInputStream(filePath));
      byte[] buffer = new byte[(int) this.bpStartPos + 2];
      bis.read(buffer);
      if (new String(buffer).contains("\r")) {
        this.lineShifterLength = 2;
      } else {
        this.lineShifterLength = 1;
      }
      this.bpStartPos += this.lineShifterLength;
      bis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
