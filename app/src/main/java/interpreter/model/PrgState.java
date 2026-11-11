
package interpreter.model;

import interpreter.model.adt.IStack;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;


public class PrgState {

    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;

    private final IStmt originalProgram;


    public PrgState(
            IStack<IStmt> exeStack,
            IDictionary<String, IValue> symTable,
            IList<IValue> out,
            IDictionary<StringValue, BufferedReader> fileTable,
            IStmt program
    ) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.originalProgram = program;

        exeStack.push(program);
    }

    public PrgState(
            IStack<IStmt> exeStack,
            IDictionary<String, IValue> symTable,
            IList<IValue> out,
            IStmt program
    ) {
        this(
                exeStack,
                symTable,
                out,
                new MyDictionary<>(),
                program
        );
    }

    public static PrgState of(IStmt program) {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                program
        );
    }


    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(
            IDictionary<String, IValue> symTable
    ) {
        this.symTable = symTable;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    public IDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(
            IDictionary<StringValue, BufferedReader> fileTable
    ) {
        this.fileTable = fileTable;
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack +
                "\nSymTable: " + symTable +
                "\nOut: " + out +
                "\nFileTable: " + fileTable;
    }


    public String toLogString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ExeStack:\n");
        exeStack.toList().forEach(
                stmt -> sb.append(stmt).append('\n')
        );

        sb.append("SymTable:\n");
        symTable.toMap().forEach(
                (key, value) ->
                        sb.append(key)
                                .append(" --> ")
                                .append(value)
                                .append('\n')
        );

        sb.append("Out:\n");
        out.toList().forEach(
                value -> sb.append(value).append('\n')
        );

        sb.append("FileTable:\n");
        fileTable.toMap().keySet().forEach(
                file -> sb.append(file.val()).append('\n')
        );

        return sb.toString();
    }

}
