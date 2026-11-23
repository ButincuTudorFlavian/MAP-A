
package interpreter.view;

import interpreter.model.stmt.*;
import interpreter.model.exp.*;
import interpreter.model.type.*;
import interpreter.model.value.*;

public final class Examples {

    private Examples() {
    }

    // Ex1: int v; v = 2; print(v)
    public static final IStmt EX1 =
            new CompStmt(
                    new VarDeclStmt("v", new IntType()),
                    new CompStmt(
                            new AssignStmt("v",
                                    new ValueExp(new IntValue(2))),
                            new PrintStmt(new VarExp("v"))
                    )
            );

    // Ex2: int a; int b; a = 2 + 3 * 5;
    // b = a + 1; print(b)
    public static final IStmt EX2 =
            new CompStmt(
                    new VarDeclStmt("a", new IntType()),
                    new CompStmt(
                            new VarDeclStmt("b", new IntType()),
                            new CompStmt(
                                    new AssignStmt(
                                            "a",
                                            new ArithExp(
                                                    ArithOp.ADD,
                                                    new ValueExp(new IntValue(2)),
                                                    new ArithExp(
                                                            ArithOp.MUL,
                                                            new ValueExp(new IntValue(3)),
                                                            new ValueExp(new IntValue(5))
                                                    )
                                            )
                                    ),
                                    new CompStmt(
                                            new AssignStmt(
                                                    "b",
                                                    new ArithExp(
                                                            ArithOp.ADD,
                                                            new VarExp("a"),
                                                            new ValueExp(new IntValue(1))
                                                    )
                                            ),
                                            new PrintStmt(new VarExp("b"))
                                    )
                            )
                    )
            );

    // Ex3: bool a; int v; a = true;
    // if a then v = 2 else v = 3; print(v)
    public static final IStmt EX3 =
            new CompStmt(
                    new VarDeclStmt("a", new BoolType()),
                    new CompStmt(
                            new VarDeclStmt("v", new IntType()),
                            new CompStmt(
                                    new AssignStmt(
                                            "a",
                                            new ValueExp(new BoolValue(true))
                                    ),
                                    new CompStmt(
                                            new IfStmt(
                                                    new VarExp("a"),
                                                    new AssignStmt(
                                                            "v",
                                                            new ValueExp(new IntValue(2))
                                                    ),
                                                    new AssignStmt(
                                                            "v",
                                                            new ValueExp(new IntValue(3))
                                                    )
                                            ),
                                            new PrintStmt(new VarExp("v"))
                                    )
                            )
                    )
            );

    // Ex4: bool a; a = 2; print(a)
    public static final IStmt EX4 =
            new CompStmt(
                    new VarDeclStmt("a", new BoolType()),
                    new CompStmt(
                            new AssignStmt("a",
                                    new ValueExp(new IntValue(2))),
                            new PrintStmt(new VarExp("a"))
                    )
            );


    // Construiește o secvență de instrucțiuni
    // folosind CompStmt.
    private static IStmt sequence(IStmt... statements) {
        if (statements.length == 0) {
            throw new IllegalArgumentException(
                    "The program must contain a statement"
            );
        }

        IStmt result = statements[statements.length - 1];

        for (int i = statements.length - 2; i >= 0; i--) {
            result = new CompStmt(statements[i], result);
        }

        return result;
    }

    // EX5: citește două numere dintr-un fișier.
    public static IStmt fileExample(String fileName) {
        return sequence(
                new VarDeclStmt("varf", new StringType()),

                new AssignStmt(
                        "varf",
                        new ValueExp(new StringValue(fileName))
                ),

                new OpenRFileStmt(new VarExp("varf")),

                new VarDeclStmt("varc", new IntType()),

                new ReadFileStmt(new VarExp("varf"), "varc"),

                new PrintStmt(new VarExp("varc")),

                new ReadFileStmt(new VarExp("varf"), "varc"),

                new PrintStmt(new VarExp("varc")),

                new CloseRFileStmt(new VarExp("varf"))
        );
    }

    public static final IStmt EX5 = fileExample("test.in");

    // EX6: int a; int b; a = 5; b = 7;
    // if a < b then print(a) else print(b)
    public static final IStmt EX6 = sequence(
            new VarDeclStmt("a", new IntType()),

            new VarDeclStmt("b", new IntType()),

            new AssignStmt(
                    "a",
                    new ValueExp(new IntValue(5))
            ),

            new AssignStmt(
                    "b",
                    new ValueExp(new IntValue(7))
            ),

            new IfStmt(
                    new RelationalExp(
                            RelOp.LT,
                            new VarExp("a"),
                            new VarExp("b")
                    ),
                    new PrintStmt(new VarExp("a")),
                    new PrintStmt(new VarExp("b"))
            )
    );


    //Ex7: int a, int b, bool c; a=1;b=2; c=(a!=b); print(c)
    public static final IStmt EX7 = sequence(
            new VarDeclStmt("a", new IntType()),
            new VarDeclStmt("b", new IntType()),
            new VarDeclStmt("c", new BoolType()),

            new AssignStmt(
                    "a",
                    new ValueExp(new IntValue(1))
            ),

            new AssignStmt(
                    "b",
                    new ValueExp(new IntValue(2))
            ),

            new AssignStmt(
                    "c",
                    new RelationalExp(
                            RelOp.NE,
                            new VarExp("a"),
                            new VarExp("b")
                    )
            ),

            new PrintStmt(new VarExp("c"))
    );

}
