/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.bcomp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is only used as container for binary
 * @author serge
 */
public class ProgramBinary {
    public final static int UNDEFINED = -1;
    public int load_address = UNDEFINED;
    public int start_address = UNDEFINED;
    
    public List<Integer> binary = null;
    
    public ProgramBinary() {
        //default
    }
    
    public ProgramBinary(List<Integer> prog) {
        loadBinaryFormat(prog);
    }
    
    public final List<Integer> getBinaryFormat() {
        if (start_address == UNDEFINED || load_address == UNDEFINED ||
                binary == null || binary.isEmpty()) 
            throw new RuntimeException("BcompNG: Program data is corrupted");
        LinkedList<Integer> prog = new LinkedList<Integer>(binary);
        prog.add(0,start_address);
        prog.add(0,load_address);
        return prog;
    }
    
    public final void loadBinaryFormat(List<Integer> prog) {
        Iterator<Integer> i = prog.iterator();
        if (!i.hasNext()) throw new IndexOutOfBoundsException("BcompNG: Программа пуста: load_address");
        load_address = i.next();
        if (!i.hasNext()) throw new IndexOutOfBoundsException("BcompNG: Программа пуста: start_address");
        start_address = i.next();
        if (prog.size()<3) throw new IndexOutOfBoundsException("BcompNG: Программа пуста: binary body");
        binary = new LinkedList<Integer>();
        while (i.hasNext()) binary.add(i.next());
    }

    public String toBinaryRepresentation(int columns) {
        StringBuilder sb = new StringBuilder();
        int col = 8;
        if (columns >= 1) col = columns;
        int i = 1;
        for (Integer w : getBinaryFormat()) {
            sb.append(Integer.toHexString(w+0x100000).substring(2)).append(' ');
            if (i++ % col == 0) sb.append('\n');
        }
        return sb.toString();
    }
    
    public String toBinaryRepresentation() {
        return toBinaryRepresentation(8);
    }


    @Override
    public String toString() {
        return toBinaryRepresentation();
    }
    
    

}
