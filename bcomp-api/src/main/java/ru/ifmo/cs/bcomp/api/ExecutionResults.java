
package ru.ifmo.cs.bcomp.api;

import java.util.ArrayList;
import java.util.List;
import ru.ifmo.cs.bcomp.assembler.Program;
import ru.ifmo.cs.components.Memory;


/**
 *
 * @author serge
 */
public class ExecutionResults {

    private Memory memory = null;
    private Program pobj = null;
    public List<String> compilation_errors = null;
    public List<Registers> traceTable = new ArrayList<>();
    
    public void setProgramObjectAndMemory(Program pobj,Memory memory) {
        if (memory == null)
            throw new InternalRuntimeException("Setting bcomp memory after execution that is null");
        if (pobj == null)
            throw new InternalRuntimeException("Setting object data that is null");
        this.memory = memory;
        this.pobj = pobj;
    }
    
    public String toTraceTable (boolean mpu) {
        StringBuilder trace = new StringBuilder();
        trace.append(Registers.traceTabHeader(mpu));
        for(Registers r:traceTable) {
            trace.append(r.toTraceTab(mpu)).append('\n');
        }
        return trace.toString();
    };

    private void checkTraceTable() {
        if (traceTable == null) 
            throw new InternalRuntimeException("Trace table is not created");
        if (traceTable.size()<=0)
            throw new InternalRuntimeException("Trace table not populated with registers walues");            
    }

    public Program getProgramObject() {return pobj;} //TODO - remove
    public long getLabelAddress(String label) {return pobj.getLabelAddr(label);}
    public long getStartAddress() {return pobj.start_address;}
    public long getLoadAddress() {return pobj.load_address;}
    public long getMemoryValue(long address) {return memory.getValue(address);}
    public long getMemoryValue(String label) {return memory.getValue(getLabelAddress(label));}
 
    public Registers getLastRegisters() {
        checkTraceTable();
        return traceTable.get(traceTable.size()-1);
    }

    public int countMemoryRequests() {
        checkTraceTable();
        return (int) traceTable.stream().filter(i -> i.mop > 0).count();
    }
    
    public int countTraceSteps() {
        return traceTable.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (memory==null) sb.append("Memory not defined\n");
        else {
            int count_non_zero =0;
            for (long i=0;i<0x7ffL;i++) {
                if (memory.getValue(i)!=0) count_non_zero++;
            }
            sb.append("Memory contains ").append(count_non_zero).append(" non empty cells\n");
        }
        if (pobj == null) sb.append("Binary programm not defined\n");
        else {
            sb.append("Program loads at 0x").append(Utils.toHex(pobj.load_address,11));
            sb.append(" starts at 0x").append(Utils.toHex(pobj.start_address,11));
            sb.append('\n');
            if (pobj.content != null) {
                sb.append("Program binary contains ").append(pobj.content.size()).append(" elements\n");
            }
            if (pobj.labels != null) {
                sb.append("Defined labels: ");
                pobj.labels.forEach((name, label) -> {
                    sb.append(label.getFullName()).append(":0x").append(Utils.toHex(label.address,11)).append(',');
                });
                sb.append('\n');
            }
            if (pobj.lineInfo != null) {
                sb.append("Debugging line info contains ").append(pobj.lineInfo.size()).append(" elements\n");
            }
        }
        if (compilation_errors != null) {
            sb.append("Compilation errors: (").append(compilation_errors.size()).append(")\n");
            compilation_errors.forEach((error) -> {sb.append(error).append('\n');});
        }
        if (traceTable == null) sb.append("Trace table not defined\n");
        else {
            sb.append("Trace table contains ").append(traceTable.size()).append(" elements\n");
            traceTable.forEach((r) -> {sb.append(r.toString()).append('\n');});
        }
        return sb.toString();
    }

}
