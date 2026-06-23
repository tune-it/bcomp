package ru.ifmo.cs.bcomp.axl;

import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ProgramBinary;
import ru.ifmo.cs.bcomp.Reg;
import ru.ifmo.cs.bcomp.State;
import ru.ifmo.cs.bcomp.assembler.AsmNg;
import ru.ifmo.cs.bcomp.assembler.Program;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public final class AxlTestSupport {

    public static final class Result {
        public BasicComp bcomp;
        public CPU cpu;
        public Program program;
        public String asm;

        public int ac() {
            return (int) (cpu.getRegValue(Reg.AC) & 0xFFFF);
        }

        public int mem(String label) {
            int addr = program.getLabelAddr(label);
            return (int) (cpu.getMemory().getValue(addr) & 0xFFFF);
        }

        public int flag(State s) {
            return (int) cpu.getProgramState(s);
        }
    }

    public static String compileToAsm(String bc) {
        AxlCompiler cc = new AxlCompiler();
        String asm = cc.compile(bc);
        if (asm == null) {
            fail("compiler errors: " + cc.getErrors());
        }
        return asm;
    }

    public static Program assemble(String asm) {
        AsmNg a = new AsmNg(asm);
        Program p = a.compile();
        if (!a.getErrors().isEmpty()) {
            fail("assembler errors: " + a.getErrors() + "\n" + asm);
        }
        assertNotNull(p);
        return p;
    }

    public static Result run(String bc) throws Exception {
        String asm = compileToAsm(bc);
        Program p = assemble(asm);
        BasicComp bcomp = new BasicComp();
        final CPU cpu = bcomp.getCPU();
        bcomp.loadProgram(new ProgramBinary(p.getBinaryFormat()));
        cpu.setRunState(true);

        Thread watchdog = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    cpu.setRunState(false);
                } catch (InterruptedException ignored) {
                }
            }
        });
        watchdog.setDaemon(true);
        watchdog.start();

        cpu.executeStart();
        watchdog.interrupt();

        assertTrue("program did not halt (possible infinite loop)\n" + asm,
                cpu.getProgramState(State.P) == 0);

        Result r = new Result();
        r.bcomp = bcomp;
        r.cpu = cpu;
        r.program = p;
        r.asm = asm;
        return r;
    }

    private AxlTestSupport() {
    }
}
