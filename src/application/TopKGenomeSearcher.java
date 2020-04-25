package application;

import java.util.List;

import bed.BEDline;
import bed.BEDreader;
import datastructure.BEDlineInferiorityHeap;
import datastructure.Heap;

public class TopKGenomeSearcher {
  public static final String TEST_FILEPATH1 = "src/testcase/refGene.hg19.sorted.bed";
  public static final String TEST_FILEPATH2 = "src/testcase/hg19_200.bed";
  public static final int K = 20;

  private String chromesomeSearched = "";
  private int topK = 0;

  public TopKGenomeSearcher(String chromesomeSearched, int topK) {
    this.chromesomeSearched = chromesomeSearched;
    this.topK = topK;
  }

  public List<BEDline> searchFile(String filePath) {
    BEDreader reader = new BEDreader(filePath);
    Heap<BEDline> heap = new BEDlineInferiorityHeap(this.topK);
    int count = 0;
    while (reader.hasNext()) {
      BEDline line = reader.nextLine();
      if (line != null
          && line.getChromesome().equals(this.chromesomeSearched)) {
        heap.insert(line);
      }
      System.out.println(++count + "..." + heap.getTop());
      System.out.println(heap.toString());
    }
    return heap.getElemList();
  }

  public static void main(String[] args) {
    TopKGenomeSearcher searcher = new TopKGenomeSearcher("chr22", K);
    List<BEDline> topK = searcher.searchFile(TopKGenomeSearcher.TEST_FILEPATH1);
    for (int i = 0; i < topK.size(); i++) {
      System.out.println(topK.get(i));
    }
  }

}
