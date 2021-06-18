package repository;

import model.ProgramState;
import model.hashtable.IHeapTable;
import model.values.IValue;

import java.util.List;
import java.util.Map;

public interface IRepository {

    List<ProgramState> getProgramsList();

    ProgramState getCurrentProgramState();

    ProgramState getProgramState(int index);

    IHeapTable<IValue> getHeap();

    int getSize();

    void setHeap(Map<Integer, IValue> content);

    void setProgramList(List<ProgramState> programs);

    void addProgram(ProgramState program);

    void logProgramExecution(ProgramState state);

    IRepository getCopy();

    void close();

    void clear();
}
