package model.hashtable;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BarrierTable implements IHeapTable<Pair<Integer, List<Integer>>>{
    private int nextAddress;
    private final Map<Integer, Pair<Integer, List<Integer>>> table;

    public BarrierTable()
    {
        table = new HashMap<>();
        nextAddress = 1;
    }


    @Override
    public Map<Integer,String> getFormattedHeap()
    {
        return null;
    }

    @Override
    public synchronized Pair<Integer, List<Integer>> get(Integer address)
    {
        return table.get(address);
    }

    @Override
    public synchronized void remove(Integer address)
    {
        table.remove(address);
    }

    @Override
    public int insert(Pair<Integer, List<Integer>> value)
    {
        table.put(nextAddress, value);
        return nextAddress++;
    }

    @Override
    public Set<Map.Entry<Integer, Pair<Integer, List<Integer>>>> entrySet()
    {
        return table.entrySet();
    }

    @Override
    public synchronized void set(int address, Pair<Integer, List<Integer>> value)
    {
        table.put(address, value);
    }

    @Override
    public synchronized void setContent(Map<Integer, Pair<Integer, List<Integer>>> table)
    {
        this.table.clear();
        this.table.putAll(table);
    }

    @Override
    public String toString()
    {
        StringBuilder text = new StringBuilder();
        for (HashMap.Entry<Integer, Pair<Integer, List<Integer>>> entry : this.table.entrySet()) {
            text.append(entry.getKey().toString()).append(" -> ").append(entry.getValue().toString()).append("; ");
        }

        return text.toString();
    }
}
