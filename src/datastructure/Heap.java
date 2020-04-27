package datastructure;

import java.util.List;

public interface Heap<E> {

  public void insert(E element);

  public void delete(int index);

  public void replaceTop(E element);

  public void sort();

  public List<E> getElemList();

  public int getElemCount();

  public int getHeapSize();

  public E getTop();

  @Override
  public String toString();
}
