package controller;

import model.ProgramState;
import model.hashtable.IHeapTable;
import model.types.ReferenceType;
import model.values.IValue;
import model.values.ReferenceValue;
import repository.IRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repository;
    private boolean debugFlag;
    private boolean logFlag;
    private ExecutorService executor;

    public Controller(IRepository repository)
    {
        this.repository = repository;
        debugFlag = false;
    }

    public ProgramState getProgramById(int id)
    {
        for(var p : repository.getProgramsList())
        {
            if(p.getId() == id)
                return p;
        }
        return null;
    }

    public List<ProgramState> getProgramStates()
    {
        return repository.getProgramsList();
    }

    public IRepository getRepository()
    {
        return repository;
    }

    public void setDebugFlag(boolean state)
    {
        debugFlag = state;
    }

    public void setLogFlag(boolean state)
    {
        logFlag = state;
    }

    private Map<Integer, IValue> cleanHeap(IHeapTable<IValue> heap)
    {
        return heap.entrySet().stream() .filter(this::inSymbolsTable)
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private boolean inSymbolsTable(Map.Entry<Integer, IValue> entry)
    {
        return repository.getProgramsList().stream().map(ProgramState::getSymbolTable)
                        .anyMatch(table -> table.getValues().stream().anyMatch(e->(match(e,entry))));
    }

    private boolean match(IValue v1, Map.Entry<Integer, IValue> entry)
    {
        if(v1 instanceof ReferenceValue)
        {
            ReferenceValue value = (ReferenceValue) v1;
            int address = value.getAddress();

            if(address == entry.getKey())
                return true;

            IHeapTable<IValue> heap = repository.getCurrentProgramState().getHeapTable();
            if(value.getLocationType() instanceof ReferenceType)
                return match(heap.get(address), entry);

        }
        return false;
    }

    public void executeOneStep()
    {
        //repository.clear();
        executor = Executors.newFixedThreadPool(5);
        List<ProgramState> programs = removeCompletedPrograms(repository.getProgramsList());
        ShowStateOfPrograms(programs);
        if(programs.size() > 0){
            repository.setHeap(cleanHeap(repository.getHeap()));
            oneStepForAllPrograms(programs);
            ShowStateOfPrograms(programs);
            programs = removeCompletedPrograms(repository.getProgramsList());
        }

        executor.shutdownNow();

        repository.setProgramList(programs);
        //repository.close();
    }

    public void oneStepForAllPrograms(List<ProgramState> programs)
    {
        List<Callable<ProgramState>> callableProgramsList = programs.stream()
                .map((ProgramState program) -> (Callable<ProgramState>)(program::oneStep))
                .collect(Collectors.toList());

        try {
            List<ProgramState> newPrograms = executor.invokeAll(callableProgramsList).stream()
                    .map(future -> {
                                        try {
                                            return future.get();
                                        }
                                        catch (InterruptedException | ExecutionException | RuntimeException e) {
                                            System.out.println(e.toString());
                                            return null;
                                        }
                                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            programs.addAll(newPrograms);

            repository.setProgramList(programs);

        }
        catch(InterruptedException e)
        {
            throw new RuntimeException(e.toString());
        }
    }

    public void allSteps()
    {
        repository.clear();
        executor = Executors.newFixedThreadPool(5);
        List<ProgramState> programs = removeCompletedPrograms(repository.getProgramsList());
        ShowStateOfPrograms(programs);
        while(programs.size() > 0){
            repository.setHeap(cleanHeap(repository.getHeap()));
            oneStepForAllPrograms(programs);
            ShowStateOfPrograms(programs);
            programs = removeCompletedPrograms(repository.getProgramsList());
        }

        executor.shutdownNow();

        repository.setProgramList(programs);
        repository.close();
    }

    private List<ProgramState> removeCompletedPrograms(List<ProgramState> programs)
    {
        return programs.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());
    }

    private void ShowStateOfPrograms(List<ProgramState> programs)
    {
        programs.forEach(this::ShowState);
    }

    private void ShowState(ProgramState program) {
        if (debugFlag)
            System.out.print(program.toString());
        if(logFlag)
            repository.logProgramExecution(program);
    }
}
