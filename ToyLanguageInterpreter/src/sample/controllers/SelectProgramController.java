package sample.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.hashtable.ProceduresTable;
import model.procedures.Procedure;
import model.statements.IStatement;

import controller.Controller;
import exception.TypeCheckerException;
import model.ProgramState;
import model.expressions.*;
import model.hashtable.IHashTable;
import model.hashtable.TypeEnvironment;
import model.statements.*;
import model.types.*;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.Repository;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SelectProgramController implements Initializable{
    @FXML
    private Button executeButton;
    @FXML
    private ListView<IStatement> programsListView;

    private List<IStatement> statements;

    private RunProgramController runProgramController;

    public void setRunProgramController(RunProgramController runProgramController)
    {
        this.runProgramController = runProgramController;
    }

    private void populateList()
    {
        statements = new ArrayList();

        IStatement ex1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement( new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));

        statements.add(ex1);

        IStatement ex2 = new CompoundStatement( new VariableDeclarationStatement("a",new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b",new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),new
                                ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(new AssignStatement("b",new ArithmeticExpression('+',new VariableExpression("a"), new
                                        ValueExpression(new IntValue(1)))), new PrintStatement(new VariableExpression("b"))))));

        statements.add(ex2);

        IStatement ex3 = new CompoundStatement(new VariableDeclarationStatement("a",new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(  new VariableExpression("a"),
                                        new AssignStatement("v",new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));

        statements.add(ex3);

        IStatement ex4 = new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
                new CompoundStatement(new AssignStatement("varf", new ValueExpression(new StringValue("E:\\IONUT\\Semestrul 3\\Advanced Programming Methods\\src\\test.in"))),
                        new CompoundStatement(new OpenReadFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
                                        new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseReadFileStatement(new VariableExpression("varf"))))))))));

        statements.add(ex4);

        //if( ((2 < 3) and (3 >= 2)) or (2 == 3)))
        IStatement ex5 = new IfStatement(
                new LogicalExpression(new LogicalExpression(
                        new RelationalExpression(new ValueExpression(new IntValue(2)), new ValueExpression(new IntValue(3)), "<"),
                        new RelationalExpression(new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(2)), ">="), "and"),
                        new RelationalExpression(new ValueExpression(new IntValue(2)), new ValueExpression(new IntValue(3)), "=="),
                        "or"),
                new PrintStatement(new ValueExpression(new StringValue("OK!"))),
                new PrintStatement(new ValueExpression(new StringValue("NOT OK!"))));

        statements.add(ex5);

        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a) - Exception, v is not a referneceType
        IStatement ex6 = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement( "a", new ReferenceType(new ReferenceType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement((new VariableExpression("a"))))))));

        statements.add(ex6);

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); write(readHeap(v));write(readHeap(readHeap(a))+5)

        IStatement ex7 = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement( "a", new ReferenceType(new ReferenceType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression('+',
                                                        new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                                        new ValueExpression(new IntValue(5)))))))));

        statements.add(ex7);

        //Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);

        IStatement ex8 = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(new ArithmeticExpression('+',
                                                new ReadHeapExpression(new VariableExpression("v")),
                                                new ValueExpression(new IntValue(5))))))));

        statements.add(ex8);

        //int v; v=4; (while (v>0) print(v);v=v-1);print(v)
        IStatement ex9 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new WhileStatement(/*new ValueExpression(new IntValue(2))*/new RelationalExpression(
                                new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignStatement("v",
                                                new ArithmeticExpression('-',
                                                        new VariableExpression("v"),
                                                        new ValueExpression(new IntValue(1)))))),
                                new PrintStatement(new VariableExpression("v")))));

        statements.add(ex9);

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
        IStatement ex10 = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));

        statements.add(ex10);

        //int v; Ref int a; v=10;new(a,22);
        //fork(wH(a,30);v=32;print(v);print(rH(a)));
        //print(v);print(rH(a));

        IStatement ex11 = new CompoundStatement(new ProcedureDeclarationStatement( new Procedure("main",
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new IntType())),
                        new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(32))),
                                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))))))),
                new CallStatement("main"));

        statements.add(ex11);

        IStatement ex12 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement( new AssignStatement("v", new ValueExpression(new BoolValue(false))),
                        new PrintStatement(new VariableExpression("v"))));

        statements.add(ex12);

        IStatement ex13 =
                new CompoundStatement(
                        new ProcedureDeclarationStatement(
                                new Procedure("sum",
                                        new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                                                new CompoundStatement(
                                                        new AssignStatement("v", new ArithmeticExpression('+',
                                                                new VariableExpression("a"), new VariableExpression("b"))),
                                                        new PrintStatement(new VariableExpression("v")))),
                                        new VariableDeclarationStatement("a", new IntType()),
                                        new VariableDeclarationStatement("b", new IntType()))),
                        new CompoundStatement(
                                new ProcedureDeclarationStatement(
                                        new Procedure("product",
                                                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                                                        new CompoundStatement(
                                                                new AssignStatement("v", new ArithmeticExpression('*',
                                                                        new VariableExpression("a"), new VariableExpression("b"))),
                                                                new PrintStatement(new VariableExpression("v")))),
                                                new VariableDeclarationStatement("a", new IntType()),
                                                new VariableDeclarationStatement("b", new IntType()))),
                                new CompoundStatement(
                                        new ProcedureDeclarationStatement(
                                                new Procedure( "main",
                                                        new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                                                                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                                                        new CompoundStatement(new VariableDeclarationStatement("w", new IntType()),
                                                                                new CompoundStatement(new AssignStatement("w", new ValueExpression(new IntValue(5))),
                                                                                        new CompoundStatement( new CallStatement("sum",
                                                                                                new ArithmeticExpression('*',
                                                                                                        new VariableExpression("v"), new ValueExpression(new IntValue(10))),
                                                                                                new VariableExpression("w")),
                                                                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                                                        new CompoundStatement(new ForkStatement( new CallStatement("product",
                                                                                                                new VariableExpression("v"), new VariableExpression("w"))),
                                                                                                                new ForkStatement(new CallStatement("sum",
                                                                                                                        new VariableExpression("v"), new VariableExpression("w")))))))))))), new CallStatement("main"))));

        statements.add(ex13);


        /*
        v=10;
        (fork(v=v-1;v=v-1;print(v)); sleep(10);print(v*10)
         */

        IStatement ex14 = new CompoundStatement( new ProcedureDeclarationStatement(new Procedure("main",
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(new ForkStatement(new CompoundStatement(
                                        new AssignStatement("v", new ArithmeticExpression('-',
                                                new VariableExpression("v"), new ValueExpression(new IntValue(1)))),
                                        new CompoundStatement(
                                                new AssignStatement("v", new ArithmeticExpression('-',
                                                        new VariableExpression("v"), new ValueExpression(new IntValue(1)))),
                                                new PrintStatement(new VariableExpression("v"))))),
                                        new CompoundStatement(new SleepStatement(new ValueExpression(new IntValue(10))),
                                                new PrintStatement(new ArithmeticExpression( '*',
                                                        new VariableExpression("v"), new ValueExpression(new IntValue(10)))))))))),
                new CallStatement("main"));

       statements.add(ex14);

        /*
        Ref int a; new(a,20);
        (for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a)));
        print(rh(a))
        The final Out should be {0,1,2,20}
         */
        IStatement ex15 = new CompoundStatement(new ProcedureDeclarationStatement(new Procedure("main", new CompoundStatement(
                new VariableDeclarationStatement("a", new ReferenceType(new IntType())), new CompoundStatement(
                new NewStatement("a", new ValueExpression(new IntValue(20))), new CompoundStatement(
                new VariableDeclarationStatement("v", new IntType()), new CompoundStatement(
                new ForStatement("v", new ValueExpression(new IntValue(0)), new ValueExpression(new IntValue(3)),
                        new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntValue(1))),
                        new ForkStatement(new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                new AssignStatement("v", new ArithmeticExpression('*', new VariableExpression("v"),
                                        new ReadHeapExpression(new VariableExpression("a"))))))),
                new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))))),
                new CallStatement("main"));

        statements.add(ex15);

        /*
        Ref int v1; Ref int v2; int x; int q;
        new(v1,20);new(v2,30);newLock(x);
        fork(
        fork(
        lock(x);wh(v1,rh(v1)-1);unlock(x)
        );
        lock(x);wh(v1,rh(v1)*10);unlock(x)
        );newLock(q);
        fork(
        fork(lock(q);wh(v2,rh(v2)+5);unlock(q));
        lock(q);wh(v2,rh(v2)*10);unlock(q)

        );
        nop;nop;nop;nop;
        lock(x); print(rh(v1)); unlock(x);
        lock(q); print(rh(v2)); unlock(q);
        The final Out should be {190 or 199,350 or 305}
         */

        IStatement ex16 = new CompoundStatement(new ProcedureDeclarationStatement(new Procedure("main",
                new CompoundStatement(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())),
                        new CompoundStatement(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())),
                                new CompoundStatement(new VariableDeclarationStatement("x", new IntType()),
                                        new CompoundStatement(new VariableDeclarationStatement("q", new IntType()),
                                                new CompoundStatement(new NewStatement("v1", new ValueExpression(new IntValue(20))),
                                                        new CompoundStatement(new NewStatement("v2", new ValueExpression(new IntValue(30))),
                                                                new CompoundStatement(new NewLockStatement("x"),
                                                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new ForkStatement(
                                                                                new CompoundStatement(new LockStatement("x"),
                                                                                        new CompoundStatement(new WriteHeapStatement("v1",
                                                                                                new ArithmeticExpression('-',
                                                                                                        new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                        new ValueExpression(new IntValue(1)))), new UnlockStatement("x")))),
                                                                                new CompoundStatement(new LockStatement("x"),
                                                                                        new CompoundStatement(new WriteHeapStatement("v1",
                                                                                                new ArithmeticExpression('*',
                                                                                                        new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                        new ValueExpression(new IntValue(10)))), new UnlockStatement("x"))))),
                                                                                new CompoundStatement(new NewLockStatement("q"),
                                                                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new ForkStatement(
                                                                                                new CompoundStatement(new LockStatement("q"),
                                                                                                        new CompoundStatement(new WriteHeapStatement("v2",
                                                                                                                new ArithmeticExpression('+',
                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                        new ValueExpression(new IntValue(5)))), new UnlockStatement("q")))),
                                                                                                new CompoundStatement(new LockStatement("q"),
                                                                                                        new CompoundStatement(new WriteHeapStatement("v2",
                                                                                                                new ArithmeticExpression('*',
                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                        new ValueExpression(new IntValue(10)))), new UnlockStatement("q"))))),
                                                                                                new CompoundStatement(new SleepStatement(new ValueExpression(new IntValue(4))),
                                                                                                        new CompoundStatement(new LockStatement("x"),
                                                                                                                new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                                        new CompoundStatement(new UnlockStatement("x"),
                                                                                                                                new CompoundStatement(new LockStatement("q"),
                                                                                                                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                                                                new UnlockStatement("q"))))))))))))))))))),
                new CallStatement("main"));

        statements.add(ex16);

        /*
        Ref int a; Ref int b; int v;
        new(a,0); new(b,0);
        wh(a,1); wh(b,2);
        v=(rh(a)<rh(b))?100:200;
        print(v);
        v= ((rh(b)-2)>rh(a))?100:200;
        print(v);
        The final Out should be {100,200}
         */

        IStatement ex17 = new CompoundStatement(new ProcedureDeclarationStatement(new Procedure("main",
                new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new IntType())),
                        new CompoundStatement(new VariableDeclarationStatement("b", new ReferenceType(new IntType())),
                                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                                        new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(0))),
                                                new CompoundStatement(new NewStatement("b", new ValueExpression(new IntValue(0))),
                                                        new CompoundStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(1))),
                                                                new CompoundStatement(new WriteHeapStatement("b", new ValueExpression(new IntValue(2))),
                                                                        new CompoundStatement(new ConditionalAssignmentStatement(
                                                                                new RelationalExpression(new ReadHeapExpression( new VariableExpression("a")),
                                                                                        new ReadHeapExpression(new VariableExpression("b")), "<"),
                                                                                new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200)), "v"),
                                                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                                        new CompoundStatement(new ConditionalAssignmentStatement(
                                                                                                new RelationalExpression(new ArithmeticExpression('-',
                                                                                                        new ReadHeapExpression(new VariableExpression("b")),
                                                                                                        new ValueExpression(new IntValue(2))),
                                                                                                        new ReadHeapExpression(new VariableExpression("a")), ">"),
                                                                                                new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200)), "v"),
                                                                                                new PrintStatement(new VariableExpression("v")))))))))))))), new CallStatement("main"));

        statements.add(ex17);

        /*Ref int v1; Ref int v2; Ref int v3; int cnt;
        new(v1,2);new(v2,3);new(v3,4);newLatch(cnt,rH(v2));
        fork(wh(v1,rh(v1)*10));print(rh(v1));countDown(cnt);
        fork(wh(v2,rh(v2)*10));print(rh(v2));countDown(cnt);
        fork(wh(v3,rh(v3)*10));print(rh(v3));countDown(cnt))));
        await(cnt);
        print(100);
        countDown(cnt);
        print(100)*/
        IStatement ex18 = new CompoundStatement(new ProcedureDeclarationStatement(new Procedure("main",
                new CompoundStatement(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())),
                        new CompoundStatement(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())),
                                new CompoundStatement(new VariableDeclarationStatement("v3", new ReferenceType(new IntType())),
                                        new CompoundStatement(new VariableDeclarationStatement("cnt", new IntType()),
                                                new CompoundStatement(new NewStatement("v1", new ValueExpression(new IntValue(2))),
                                                        new CompoundStatement(new NewStatement("v2", new ValueExpression(new IntValue(3))),
                                                                new CompoundStatement(new NewStatement("v3", new ValueExpression(new IntValue(4))),
                                                                        new CompoundStatement(new NewLatchStatement("cnt", new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteHeapStatement("v1", new ArithmeticExpression('*',
                                                                                        new ReadHeapExpression(new VariableExpression("v1")),
                                                                                        new ValueExpression(new IntValue(10)))),
                                                                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                new CompoundStatement(new CountDownStatement("cnt"),
                                                                                                        new ForkStatement(new CompoundStatement(new WriteHeapStatement("v2", new ArithmeticExpression('*',
                                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                new ValueExpression(new IntValue(10)))),
                                                                                                                new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                                        new CompoundStatement(new CountDownStatement("cnt"),
                                                                                                                                new ForkStatement(new CompoundStatement(new WriteHeapStatement("v3", new ArithmeticExpression('*',
                                                                                                                                        new ReadHeapExpression(new VariableExpression("v3")),
                                                                                                                                        new ValueExpression(new IntValue(10)))),
                                                                                                                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v3"))),
                                                                                                                                                new CountDownStatement("cnt")))))))))))), new CompoundStatement( new AwaitStatement("cnt"),
                                                                                        new CompoundStatement(new PrintStatement(new ValueExpression(new IntValue(100))),
                                                                                                new CompoundStatement(new CountDownStatement("cnt"),
                                                                                                        new PrintStatement(new ValueExpression(new IntValue(100))))))))))))))))),
                new CallStatement("main"));

        statements.add(ex18);

        IStatement ex19 = new CompoundStatement(new ProcedureDeclarationStatement(new Procedure("main",
                new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new IntValue(1))),
                                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
                                        new CompoundStatement(new AssignStatement("b", new ValueExpression(new IntValue(2))),
                                                new CompoundStatement(new VariableDeclarationStatement("c", new IntType()),
                                                        new CompoundStatement(new AssignStatement("c", new ValueExpression(new IntValue(5))),
                                                                new CompoundStatement(new SwitchStatement(new ArithmeticExpression('*',
                                                                        new VariableExpression("a"),
                                                                        new ValueExpression(new IntValue(10))),
                                                                        new ArithmeticExpression('*', new VariableExpression("b"), new VariableExpression("c")),
                                                                        new PrintStatement(new VariableExpression("b")),
                                                                        new ValueExpression(new IntValue(10)),
                                                                        new CompoundStatement(new PrintStatement(new ValueExpression(new IntValue(100))),
                                                                                new PrintStatement(new ValueExpression(new IntValue(200)))),
                                                                        new PrintStatement(new ValueExpression(new IntValue(300)))),
                                                                        new PrintStatement(new ValueExpression(new IntValue(300)))))))))))), new CallStatement("main"));

        statements.add(ex19);

        IStatement ex20 = new CompoundStatement(new ProcedureDeclarationStatement(new Procedure("main",
                new CompoundStatement(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())),
                        new CompoundStatement(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())),
                                new CompoundStatement(new VariableDeclarationStatement("v3", new ReferenceType(new IntType())),
                                        new CompoundStatement(new VariableDeclarationStatement("cnt", new IntType()),
                                                new CompoundStatement(new NewStatement("v1", new ValueExpression(new IntValue(2))),
                                                        new CompoundStatement(new NewStatement("v2", new ValueExpression(new IntValue(3))),
                                                                new CompoundStatement(new NewStatement("v3", new ValueExpression(new IntValue(4))),
                                                                        new CompoundStatement(new NewBarrierStatement("cnt", new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                new CompoundStatement(new ForkStatement(new CompoundStatement(new BarrierAwaitStatement("cnt"),
                                                                                        new CompoundStatement(new WriteHeapStatement("v1", new ArithmeticExpression('*',
                                                                                                new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                new ValueExpression(new IntValue(10)))),
                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1")))))),
                                                                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new BarrierAwaitStatement("cnt"),
                                                                                                new CompoundStatement(new WriteHeapStatement("v2", new ArithmeticExpression('*',
                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                        new ValueExpression(new IntValue(10)))),
                                                                                                        new CompoundStatement(new WriteHeapStatement("v2", new ArithmeticExpression('*',
                                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                new ValueExpression(new IntValue(10)))),
                                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v2"))))))),
                                                                                                new CompoundStatement(new BarrierAwaitStatement("cnt"),
                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v3")))))))))))))))),
                new CallStatement("main"));

        statements.add(ex20);

        programsListView.setItems(FXCollections.observableArrayList(statements));
    }

    private void executeButtonClicked()
    {
        int index = programsListView.getSelectionModel().getSelectedIndex();

        if(index < 0)
            return;

        String logFilePath = "E:\\IONUT\\Semestrul 3\\ToyLanguageInterpreter - Copy\\src\\Log\\logFile.txt";
        Repository repository = new Repository(logFilePath);
        IHashTable<String, IType> typeEnvironment = new TypeEnvironment();
        IHashTable<String, Procedure> proceduresTable = new ProceduresTable();
        IStatement ex = programsListView.getItems().get(index);

        try{
            ex.typeCheck(typeEnvironment, proceduresTable);
        }
        catch(TypeCheckerException e)
        {
            //Error Box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("TypeCheck Error");
            alert.setHeaderText(null);
            alert.setContentText(e.toString());

            alert.showAndWait();
            return;
        }

        ProgramState program = new ProgramState(ex, repository.getHeap());
        repository.addProgram(program);
        Controller controller = new Controller(repository);
        controller.setDebugFlag(true);
        controller.setLogFlag(true);


        runProgramController.setController(controller);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        populateList();
        executeButton.setOnAction(e->executeButtonClicked());
    }
}
