package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ProgramBinary;
import ru.ifmo.cs.bcomp.State;
import ru.ifmo.cs.bcomp.assembler.Program;
import ru.ifmo.cs.components.DataDestination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AxlConsoleExampleTest {

    private static final String SRC =
            "int read_number() {\n"
            + "    while ((in(5) & 0x40) == 0) ;\n"
            + "    return in(4);\n"
            + "}\n"
            + "void print_number(int x) {\n"
            + "    while ((in(3) & 0x40) == 0) ;\n"
            + "    out(2, x);\n"
            + "}\n"
            + "void main() {\n"
            + "    int n; int i; int sum;\n"
            + "    n = read_number();\n"
            + "    sum = 0;\n"
            + "    for (i = 1; i <= n; i = i + 1) {\n"
            + "        sum = sum + i;\n"
            + "        print_number(i);\n"
            + "    }\n"
            + "    print_number(sum);\n"
            + "    halt();\n"
            + "}\n";

    @Test
    public void consoleInputProgressResult() throws Exception {
        String asm = AxlTestSupport.compileToAsm(SRC);
        Program p = AxlTestSupport.assemble(asm);

        BasicComp bcomp = new BasicComp();
        final CPU cpu = bcomp.getCPU();
        IOCtrl inDev = bcomp.getIOCtrls()[2];
        final IOCtrl outDev = bcomp.getIOCtrls()[1];

        final List<Integer> captured = Collections.synchronizedList(new ArrayList<Integer>());
        outDev.addDestination(0, new DataDestination() {
            @Override
            public void setValue(long v) {
                captured.add((int) (v & 0xFF));
            }
        });

        int number = 5;
        inDev.setData(number);
        inDev.setReady();
        outDev.setReady();

        final boolean[] done = {false};
        Thread acker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!done[0]) {
                    if (!outDev.isReady()) {
                        outDev.setReady();
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        acker.setDaemon(true);
        acker.start();

        bcomp.loadProgram(new ProgramBinary(p.getBinaryFormat()));
        cpu.setRunState(true);

        Thread watchdog = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                    cpu.setRunState(false);
                } catch (InterruptedException ignored) {
                }
            }
        });
        watchdog.setDaemon(true);
        watchdog.start();

        cpu.executeStart();
        watchdog.interrupt();
        done[0] = true;

        assertEquals(0, cpu.getProgramState(State.P));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 15), captured);
    }
}
