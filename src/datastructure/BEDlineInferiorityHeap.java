package datastructure;

import java.util.ArrayList;
import java.util.List;

import bed.BEDline;

public class BEDlineInferiorityHeap implements Heap<BEDline> {

  private List<BEDline> elemList = new ArrayList<>();
  private int heapSize = 0;

  public BEDlineInferiorityHeap(int heapSize) {
    this.heapSize = heapSize;
  }

  @Override
  public void insert(BEDline element) {
    if (this.elemList.size() < this.heapSize) {
      this.elemList.add(element);
      int i = this.elemList.size();
      if (i <= 1) {
        return;
      }
      while (i > 1) {
        int childIndex = i - 1;
        int fatherIndex = (i >> 1) - 1;
        BEDline child = this.elemList.get(childIndex);
        BEDline father = this.elemList.get(fatherIndex);
        if (this.inferiorThan(child, father)) {
          this.swap(childIndex, fatherIndex);
        } else {
          break;
        }
        i = i >> 1;
      }
    } else {
      // if the heap is full
      if (this.inferiorThan(this.elemList.get(0), element)) {
        this.elemList.set(0, element);
        this.adjustHeap(0, this.elemList.size());
      } else {
        // ignore the input element
      }
    }
  }

  @Override
  public void delete(int index) {
    System.out
        .println("warning: delete method of BEDlineMaxHeap not implemented");
  }

  @Override
  public void replaceTop(BEDline element) {
    this.elemList.set(0, element);
    this.adjustHeap(0, this.elemList.size());
  }

  /**
   * Adjust the heap.
   * 
   * @param i    0-base index of temporary element
   * @param size size of the part of heap to be adjusted
   */
  private void adjustHeap(int i, int size) {
    BEDline temp = this.elemList.get(i);
    for (int k = (i << 1) + 1; k < size; k = (k << 1) + 1) {
      int lchildIndex = k;
      int rchildIndex = k + 1;
      if (rchildIndex < size) {
        BEDline lchild = this.elemList.get(lchildIndex);
        BEDline rchild = this.elemList.get(rchildIndex);
        if (this.inferiorThan(rchild, lchild)) {
          k = k + 1;
        }
      }
      if (this.inferiorThan(this.elemList.get(k), temp)) {
        this.elemList.set(i, this.elemList.get(k));
        i = k;
      } else {
        break;
      }
    }
    this.elemList.set(i, temp);
  }

  @Override
  public void sort() {
    for (int i = this.elemList.size() - 1; i > 0; i--) {
      this.swap(0, i);
      this.adjustHeap(0, i);
    }
  }

  @Override
  public List<BEDline> getElemList() {
    this.sort();
    for (int i = 0; i < this.elemList.size(); i++) {
      System.out.println("i(" + i + "): " + this.elemList.get(i));
    }
    return new ArrayList<>(this.elemList);
  }

  @Override
  public int getElemCount() {
    return this.elemList.size();
  }

  @Override
  public int getHeapSize() {
    return this.heapSize;
  }

  @Override
  public BEDline getTop() {
    if (this.elemList.size() > 0) {
      return this.elemList.get(0);
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    String str = "";
    int layer = (int) Math.ceil(Math.log(this.elemList.size()) / Math.log(2))
        + 1;
    int index = 0;
    for (int i = 0; layer >= 0 && index < this.elemList.size(); layer--, i++) {
      int lineCount = (int) Math.pow(2, i);
      for (int j = 0; j < lineCount
          && index < this.elemList.size(); index++, j++) {
        str = str + "(" + this.elemList.get(index).getTransRegionBegin() + ","
            + this.elemList.get(index).getTransRegionEnd() + ")\t";
      }
      str = str + "\n";
    }
    return str;
  }

  private boolean inferiorThan(BEDline line1, BEDline line2) {
    long transBegin1 = line1.getTransRegionBegin();
    long transBegin2 = line2.getTransRegionBegin();
    if (transBegin1 > transBegin2) {
      return true;
    } else if (transBegin1 == transBegin2) {
      if (line1.getTransRegionEnd() > line2.getTransRegionEnd()) {
        return true;
      }
    }
    return false;
  }

  private void swap(int index1, int index2) {
    BEDline temp = this.elemList.get(index2);
    this.elemList.set(index2, this.elemList.get(index1));
    this.elemList.set(index1, temp);
  }

}
