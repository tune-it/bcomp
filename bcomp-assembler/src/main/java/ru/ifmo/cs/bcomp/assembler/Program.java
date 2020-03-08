/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.bcomp.assembler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is only used as container for binary
 * @author serge
 */
public class Program {
    public final static int UNDEFINED = -1;
    public int load_address = UNDEFINED;
    public int start_address = UNDEFINED;
    
    public List<Integer> binary = null;
    public HashMap<String, Label> labels = null;
    public HashMap<Integer, MemoryWord> content = null;
    
    public List<Integer> getBinaryFormat() {
        if (start_address == UNDEFINED || load_address == UNDEFINED ||
                binary == null || binary.isEmpty()) 
            throw new RuntimeException("AsmNG Program.getBinaryFormat: Program data is corrupted");
        //actually we can regenerate programm data here if labels and content
        //are not null but we would not
        LinkedList<Integer> prog = new LinkedList<Integer>(binary);
        prog.add(0,start_address);
        prog.add(0,load_address);
        return prog;
    }
    
    public void loadBinaryFormat(List<Integer> prog) {
        Iterator<Integer> i = prog.iterator();
        if (!i.hasNext()) throw new IndexOutOfBoundsException("AsmNG Program.loadBinaryFormat: Программа пуста: load_address");
        load_address = i.next();
        if (!i.hasNext()) throw new IndexOutOfBoundsException("AsmNG Program.loadBinaryFormat: Программа пуста: start_address");
        start_address = i.next();
        if (prog.size()<3) throw new IndexOutOfBoundsException("AsmNG Program.loadBinaryFormat: Программа пуста: binary body");
        binary = new LinkedList<Integer>();
        while (i.hasNext()) binary.add(i.next());
    }
    
    public int getLabelAddr (String label) {
        if (labels == null) 
            throw new RuntimeException("AsmNG Program.getLabelAddr: Labels are not set up");
        if (!labels.containsKey(label))
            throw new IllegalArgumentException("AsmNG Program.getLabelAddr: Label "+label+" not found");
        return labels.get(label).address;
    }
    
    public String toBinaryRepresentation(int columns) {
        StringBuilder sb = new StringBuilder();
        int col = 8;
        if (columns >= 1) col = columns;
        int i = 1;
        for (Integer w : getBinaryFormat()) {
            String s = "0000"+Integer.toHexString(w);
            s = s.substring(s.length()-4, s.length());
            sb.append(s).append(' ');
            if (i++ % col == 0) sb.append('\n');
        }
        return sb.toString();
    }
    
    public String toBinaryRepresentation() {
        return toBinaryRepresentation(8);
    }
    
    public String toCompiledWords() {
        if (content == null) return "UnSet";
        StringBuilder sb = new StringBuilder();
        LinkedList<Integer> addresses = new LinkedList<Integer>(content.keySet());
        Collections.sort(addresses);
        for (Integer addr : addresses ) {
            MemoryWord w = content.get(addr);
            sb.append(w).append('\n');
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        return toBinaryRepresentation();
    }

}
