/**
 *
 * @author serge
 */

package ru.ifmo.cs.bcomp.api;

import java.util.List;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ProgramBinary;
import ru.ifmo.cs.bcomp.Reg;
import ru.ifmo.cs.bcomp.assembler.AsmNg;
import ru.ifmo.cs.bcomp.assembler.Instruction;
import ru.ifmo.cs.bcomp.assembler.InstructionWord;
import ru.ifmo.cs.bcomp.assembler.Program;

/**
 *
 * @author serge
 */
public class BCompApi {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    /**
     * Trace each microcommand of single instruction until Interrupt cycle.
     * @param prog - program with single command and memories values, used with commands.
     * @param reg - initial state of registers.
     * @return 
     */
    public static ExecutionResults traceSingleInstruction (String prog, Registers reg) {
        ExecutionResults result = new ExecutionResults();
        if (reg==null)
            reg = new Registers();
        result.traceTable.add(reg); //initial raft to traceTable;
        CPU cpu = null;
        try {
            BasicComp bcomp = new BasicComp();
            AsmNg asm = new AsmNg(prog);
            Program pobj = asm.compile();
            cpu = bcomp.getCPU();
            result.setProgramObjectAndMemory(pobj, cpu.getMemory());
            // Сохранение ошибок компиляции программы
            result.compilation_errors = asm.getErrors();
            bcomp.loadProgram(new ProgramBinary(pobj.getBinaryFormat()));
            //int start_addr = pobj.start_address;
            // Пуск
            cpu.setRunState(true);  // Работа
            cpu.setClockState(false);
            cpu.executeStart();
            //skip statrup cycle
            int stopTraceAt = cpu.findLabel("INFETCH");
            while (cpu.getRegValue(Reg.MP) != stopTraceAt) {
                cpu.executeContinue(); //step();
                //System.out.println("MIP="+Utils.toHex(cpu.getRegValue(Reg.MP), 8));
            }
            saveRegisterStateToCPU(cpu, reg);
            //System.out.println(hexv(cpu.getRegister(Reg.PS).getValue()));
            //line count
            int lines = 0;
            stopTraceAt = cpu.findLabel("INT");
            do {
                //registers after execute mircoop
                Registers raft = new Registers();
                long MPPRE = cpu.getRegValue(Reg.MP);
                raft.mppre = MPPRE;
                cpu.executeContinue();
                long mr = cpu.getRegValue(Reg.MR);
                //if OMC in mr1111
                if ((mr & (1L << ControlSignal.TYPE.ordinal())) == 0) {
                    // if A_insn access memory
                    if ((mr & ((1L << ControlSignal.STOR.ordinal())|
                               (1L << ControlSignal.LOAD.ordinal())))!=0) {
                        // op: 1 - load, 2 - store
                        long op = (mr & (1L << ControlSignal.STOR.ordinal())) != 0 ? 2L : 1L;
                        long addr = cpu.getRegValue(Reg.AR);
                        long value = cpu.getMemory().getValue(addr);
                        //res.memoryRequests++;
                        raft.mop = op;
                        raft.maddr = addr;
                        raft.mval = value;
                    }
                }
                lines++;
                result.traceTable.add(getCPURegistersState(cpu, raft));
                
            } while (cpu.getRegValue(Reg.MP) != stopTraceAt);

        } catch (Exception e) {
            throw new InternalRuntimeException("Errors during trace execution",e);
        } finally {
            if (cpu != null) {
                cpu.stopCPU();
            }
        }
        return result;
    }

    /**
     * Execute Bcomp assembler program, with initial list of memory state and registers
     * @param prog program to execute
     * @param regs several registers and memory state. Last registers in list is set up to cpu.
     * List just technically needed to save student-friendly memory values for demonstating in task.
     * @param need_step true - save all registers after each command (trace mode)
     * @return ExecutionResults populated with trace values, memories state, and other useful things. @see ExecutionResults
     */
    public static ExecutionResults compileAndExecuteProgram(String prog, List<Registers> regs, boolean need_step) {
        ExecutionResults result = new ExecutionResults();
        //this need to save memory state to be passed to student task
        //look at Milestone 2. it is for following lines:
        //*  00A  *      063C       * 000 *  0000 *  000 *  0000 *  0000 *  0000 * 0000 *
        //*  00B  *      02A0       * 000 *  0000 *  000 *  0000 *  0000 *  0000 * 0000 *
        //*  00C  *      74CC       * 000 *  0000 *  000 *  0000 *  0000 *  0000 * 0000 *
        if (regs != null) {
            result.traceTable.addAll(regs);
        }
        CPU cpu = null;
        Program pobj = null;
        try {
            BasicComp bcomp = new BasicComp();
            AsmNg asm = new AsmNg(prog);
            pobj = asm.compile();
            cpu = bcomp.getCPU();
            result.setProgramObjectAndMemory(pobj, cpu.getMemory());
            // Save compiller errors
            result.compilation_errors = asm.getErrors();
            //
            bcomp.loadProgram(new ProgramBinary(pobj.getBinaryFormat()));
            // Set run state to true if you don't need step
            cpu.setRunState(!need_step);
            // Пуск
            cpu.executeStart();
            if (regs != null) {
                Registers reg = regs.get(regs.size()-1); //last register before program
                saveRegisterStateToCPU(cpu, reg);
            }
            long cmd = 0;
            if (need_step) do {
                int ip_before = (int)cpu.getRegValue(Reg.IP);
                cmd = cpu.getMemory().getValue(ip_before);
                InstructionWord i = (InstructionWord)pobj.content.get(ip_before);
                if (i == null) {
                    throw new InternalRuntimeException("InstructionWord cant be fetch from object file, internal program compilation errors?");
                }
                cpu.executeContinue();
                
                Registers raft = getCPURegistersState(cpu, null);
                //TODO: full command disassembly ? Now it support only ADDR w direct mode and no addr.
                raft.mop = 0L; raft.maddr = ip_before;
                if (ip_before == pobj.start_address) raft.mmnemo = "+"; else raft.mmnemo = " ";
                raft.mmnemo += (i.instruction.mnemonic+"       ").substring(0, 5);
                raft.mmnemo += (i.operand != null) ? Utils.toHex( i.value &0x7ff, 11 ) : "   ";
                raft.mval = i.instruction.opcode; //TODO need regression? should not add mval while didnt have memory operation
                result.traceTable.add(raft);
                
            } while (cmd != Instruction.HLT.opcode); 
            if (!need_step) {
                result.traceTable.add(getCPURegistersState(cpu, null));
            }

        } catch (Exception e) {
            throw new InternalRuntimeException("Errors during programm execution",e);
        } finally {
            if (cpu != null) {
                cpu.stopCPU();
            }
        }
        return result;
    }
    
    private static Registers getCPURegistersState(CPU cpu, Registers reg) {
        if (cpu == null) throw new InternalRuntimeException("CPU not initialized");
        Registers raft = reg;
        //if reg is null make new empty registers
        if (raft == null) raft = new Registers();
        raft.ar = cpu.getRegValue(Reg.AR);
        raft.br = cpu.getRegValue(Reg.BR);
        raft.cr = cpu.getRegValue(Reg.CR);
        raft.dr = cpu.getRegValue(Reg.DR);
        raft.acc = cpu.getRegValue(Reg.AC);
        raft.sp = cpu.getRegValue(Reg.SP);
        raft.ip = cpu.getRegValue(Reg.IP);
        long ps = cpu.getRegValue(Reg.PS);
        raft.bitc = ( ps & 1) != 0 ? 1L : 0L;
        raft.bitv = ( ps & 2) != 0 ? 1L : 0L;
        raft.bitz = ( ps & 4) != 0 ? 1L : 0L;
        raft.bitn = ( ps & 8) != 0 ? 1L : 0L;
        raft.mpafter = cpu.getRegValue(Reg.MP);
        return raft;
    }
    
    private static void saveRegisterStateToCPU(CPU cpu, Registers reg) {
        if (cpu == null) throw new InternalRuntimeException("CPU not initialized");
        if (reg == null) throw new InternalRuntimeException("Saving CPU state to non-existing Registers");
        cpu.getRegister(Reg.AR).setValue(reg.ar);
        cpu.getRegister(Reg.BR).setValue(reg.br);
        cpu.getRegister(Reg.CR).setValue(reg.cr);
        cpu.getRegister(Reg.DR).setValue(reg.dr);
        cpu.getRegister(Reg.AC).setValue(reg.acc);
        cpu.getRegister(Reg.SP).setValue(reg.sp);
        cpu.getRegister(Reg.PS).setValue(reg.bitc, 1, 0);
        cpu.getRegister(Reg.PS).setValue(reg.bitv, 1, 1);
        cpu.getRegister(Reg.PS).setValue(reg.bitz, 1, 2);
        cpu.getRegister(Reg.PS).setValue(reg.bitn, 1, 3);         
    }

    //TODO remove? with reference ExecutionResults.getProgramObject() need only in Lab2 and Lab3
    protected static StringBuilder object2BinaryTaskRepresentation(Program pobj) {
        StringBuilder mem = new StringBuilder();
        long addr = pobj.load_address; //should be same as base_addr
        for(int i : pobj.binary) {
            mem.append(Utils.toHex(addr,11));
            if (addr++ == pobj.start_address) {
                mem.append(": + ");
            } else {
                mem.append(":   ");
            }
            mem.append(Utils.toHex(i,16));
            mem.append('\n');
        }
        return mem;
    }


}
