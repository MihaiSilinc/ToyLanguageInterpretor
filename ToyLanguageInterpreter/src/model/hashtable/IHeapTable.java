package model.hashtable;

import model.values.IValue;

import java.util.Map;
import java.util.Set;

public interface IHeapTable<V> {
    V get(Integer address);
    void remove(Integer address);
    int insert(V content);
    void set(int address, V value);
    Set<Map.Entry<Integer, V>> entrySet();
    void setContent(Map<Integer, V> table);

    Map<Integer, String> getFormattedHeap();
}
