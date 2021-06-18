package sample.controllers;

import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;



public class RunProgramController implements Initializable {
    private Controller controller;

    private ProgramState currentProgram;

    @FXML
    private TableView<Map.Entry<Integer, String>> heapTableView;

    @FXML
    private TableView<Map.Entry<String, String>> symbolsTableView;

    @FXML
    private TableView<Map.Entry<Integer, String>> lockTableView;

    @FXML
    private TableView<Map.Entry<Integer, String>> latchTableView;

    @FXML
    private TableView<Map.Entry<Integer, Pair<Integer, String>>> barrierTableView;

    @FXML
    private ListView<Integer> threadsIdListView;

    @FXML
    private ListView<String> filesTableListView;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> executionStackListView;

    @FXML
    private ListView<String> proceduresListView;

    @FXML
    private Button runOneStepButton;

    @FXML
    private TextField threadsCount;

    @FXML
    TableColumn<Map.Entry<Integer, String>, Integer> heapAddressColumn;

    @FXML
    TableColumn<Map.Entry<Integer, String>, String> heapValueColumn;

    @FXML
    TableColumn<Map.Entry<Integer, String>, String> lockValueColumn;

    @FXML
    TableColumn<Map.Entry<Integer, String>, Integer> lockAddressColumn;

    @FXML
    TableColumn<Map.Entry<Integer, String>, Integer> latchAddressColumn;

    @FXML
    TableColumn<Map.Entry<Integer, String>, String> latchValueColumn;

    @FXML
    TableColumn<Map.Entry<Integer, Pair<Integer, String>>, Integer> barrierAddressColumn;

    @FXML
    TableColumn<Map.Entry<Integer, Pair<Integer, String>>, Integer> barrierValueColumn;

    @FXML
    TableColumn<Map.Entry<Integer, Pair<Integer, String>>, String> barrierListColumn;

    @FXML
    TableColumn<Map.Entry<String, String>, String> symbolsNameColumn;

    @FXML
    TableColumn<Map.Entry<String, String>, String> symbolsValueColumn;


    public void setController(Controller controller)
    {
        this.controller = controller;
        populateThreadsListView();
        currentProgram = controller.getProgramStates().get(0);
        changeProgram(currentProgram);
    }

    private void populateThreadsListView()
    {
        List<ProgramState> programs = controller.getProgramStates();
        List<Integer> programsIds = programs.stream().map(ProgramState::getId).collect(Collectors.toList());
        threadsIdListView.setItems(FXCollections.observableList(programsIds));

        threadsCount.setText("" + programs.size());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        heapAddressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        heapValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        lockAddressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        lockValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        latchAddressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        latchValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        barrierAddressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        barrierValueColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue().getKey()).asObject());
        barrierListColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getValue()));

        symbolsNameColumn.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().getKey()));
        symbolsValueColumn.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().getValue()));

        threadsIdListView.setOnMouseClicked(e->changeProgram(getSelectedProgram()));
        runOneStepButton.setOnAction(e->runOneStep());
    }

    private void runOneStep()
    {
        if(currentProgram == null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Selection");
            alert.setHeaderText(null);
            alert.setContentText("There is no program selected!");
            alert.showAndWait();
            return;
        }

        if(currentProgram.getStack().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Done");
            alert.setHeaderText(null);
            alert.setContentText("No more steps to go!");
            alert.showAndWait();
            return;
        }

        controller.executeOneStep();
        changeProgram(currentProgram);
        getSelectedProgram();
        populateThreadsListView();
    }

    private ProgramState getSelectedProgram()
    {
        int index = threadsIdListView.getSelectionModel().getSelectedIndex();
        if(index < 0)
            return null;

        int programId = threadsIdListView.getSelectionModel().getSelectedItem();

        currentProgram = controller.getProgramById(programId);
        return currentProgram;
    }

    private void changeProgram(ProgramState program)
    {
        if(program == null)
            return;

        populateHeapTableView();
        populateSymbolsTableView();
        populateLockTableView();
        populateFilesTableListView();
        populateOutputListView();
        populateExecutionStackListView();
        populateProceduresListView();
        populateLatchTableView();
        populateBarrierTableView();
    }

    private void populateProceduresListView() {
        Map<String, String> procedures = currentProgram.getProceduresTable().getFormattedTable();
        proceduresListView.setItems(FXCollections.observableArrayList(new ArrayList<>(procedures.entrySet().stream().map(e->e.getKey()+e.getValue()).collect(Collectors.toList()))));
        proceduresListView.refresh();
    }

    private void populateBarrierTableView()
    {
        IHeapTable<Pair<Integer, List<Integer>>> barrierTable = currentProgram.getBarrierTable();

        Map<Integer, Pair<Integer, String>> formattedBarrierTable = new HashMap<Integer, Pair<Integer, String>>();
        for(var elem : barrierTable.entrySet())
        {
            StringBuilder barrierList = new StringBuilder();
            barrierList.append("[ ");
            for(var e: elem.getValue().getValue())
                barrierList.append(e).append(" ");
            barrierList.append(" ]");
            formattedBarrierTable.put(elem.getKey(), new Pair<>(elem.getValue().getKey(), barrierList.toString()));
        }

        barrierTableView.setItems(FXCollections.observableArrayList(new ArrayList<>(formattedBarrierTable.entrySet())));
        barrierTableView.refresh();
    }

    private void populateHeapTableView()
    {
        IHeapTable<IValue> heapTable = controller.getRepository().getHeap();

        heapTableView.setItems(FXCollections.observableArrayList(new ArrayList<>(heapTable.getFormattedHeap().entrySet())));
        heapTableView.refresh();
    }

    private void populateLockTableView()
    {
        IHeapTable<Integer> lockTable = currentProgram.getLockTable();

        lockTableView.setItems(FXCollections.observableArrayList(new ArrayList<>(lockTable.getFormattedHeap().entrySet())));
        lockTableView.refresh();
    }

    private void populateLatchTableView()
    {
        IHeapTable<Integer> latchTable = currentProgram.getLatchTable();

        latchTableView.setItems(FXCollections.observableArrayList(new ArrayList<>(latchTable.getFormattedHeap().entrySet())));
        latchTableView.refresh();
    }

    private void populateSymbolsTableView()
    {
        IHashTable<String, IValue> symbolsTable = currentProgram.getSymbolTable();

        symbolsTableView.setItems(FXCollections.observableArrayList(new ArrayList<>(symbolsTable.getFormattedTable().entrySet())));
        symbolsTableView.refresh();
    }

    private void populateFilesTableListView()
    {
        IHashTable<StringValue, BufferedReader> fileTable = currentProgram.getFileTable();

        filesTableListView.setItems(FXCollections.observableArrayList(fileTable.entrySet().stream()
                .map(e->e.getKey().toString())
                .collect(Collectors.toList())));
        filesTableListView.refresh();
    }

    private void populateOutputListView()
    {
        List<String> output = currentProgram.getOutput().getOutput();

        outputListView.setItems(FXCollections.observableArrayList(output));
        outputListView.refresh();
    }

    private void populateExecutionStackListView()
    {
        List<String> stack = currentProgram.getStack().getStack();

        executionStackListView.setItems(FXCollections.observableArrayList(stack));
        executionStackListView.refresh();
    }
}
