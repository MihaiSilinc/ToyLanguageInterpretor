package repository;

import model.ProgramState;
import model.hashtable.HeapTable;
import model.hashtable.IHeapTable;
import model.values.IValue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Repository implements IRepository{
    private List<ProgramState> programs;
    private final IHeapTable<IValue> heapTable;
    int currentProgram;
    String filename;
    PrintWriter writer;

    public Repository(String filename)
    {
        programs = new ArrayList<>();
        currentProgram = -1;
        heapTable = new HeapTable();
        this.filename = filename;
        try {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
        }
        catch(IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public IHeapTable<IValue> getHeap() {
        return heapTable;
    }

    @Override
    public void setHeap(Map<Integer, IValue> content) {
        heapTable.setContent(content);
    }

    @Override
    public void addProgram(ProgramState program)
    {
        programs.add(program);
        currentProgram++;
    }

    @Override
    public int getSize()
    {
        return programs.size();
    }

    @Override
    public List<ProgramState> getProgramsList() {
        return programs;
    }

    @Override
    public ProgramState getProgramState(int index)
    {
        return programs.get(index);
    }

    @Override
    public ProgramState getCurrentProgramState()
    {
        return programs.get(currentProgram);
    }

    @Override
    public void setProgramList(List<ProgramState> programs) {
        this.programs = programs;
    }

    @Override
    public void logProgramExecution(ProgramState program){

        writer.println(program.toString());
        writer.flush();

    }

    @Override
    public void clear()
    {
        PrintWriter clear_writer;
        try
        {
            clear_writer = new PrintWriter(filename);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e.toString());
        }
        //clear_writer.println("");
        clear_writer.close();
    }


    @Override
    public void close()
    {
        writer.close();
    }

    @Override
    public IRepository getCopy() {
        IRepository newRepo = new Repository(filename);
        newRepo.addProgram(new ProgramState(programs.get(0).getStack().peek(), newRepo.getHeap()));
        return newRepo;
    }
}
