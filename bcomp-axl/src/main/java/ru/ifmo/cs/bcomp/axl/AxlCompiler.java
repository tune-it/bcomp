package ru.ifmo.cs.bcomp.axl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public final class AxlCompiler {

    private final List<String> errors = new ArrayList<String>();

    public List<String> getErrors() {
        return errors;
    }

    public String compile(String src) {
        errors.clear();
        CharStream cs = CharStreams.fromString(src);
        AxlLexer lexer = new AxlLexer(cs);
        Collector collector = new Collector();
        lexer.removeErrorListeners();
        lexer.addErrorListener(collector);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AxlParser parser = new AxlParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(collector);
        AxlParser.ProgramContext tree = parser.program();
        if (!errors.isEmpty()) {
            return null;
        }
        try {
            Ast.Program prog = new AstBuilder().build(tree);
            return new Gen().generate(prog);
        } catch (CompileException ce) {
            errors.add(ce.getMessage());
            return null;
        }
    }

    private final class Collector extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                int charPositionInLine, String msg, RecognitionException e) {
            errors.add("syntax error at " + line + ":" + charPositionInLine + " - " + msg);
        }
    }
}
