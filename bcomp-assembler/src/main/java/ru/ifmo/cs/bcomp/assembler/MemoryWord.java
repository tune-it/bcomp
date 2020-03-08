/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.bcomp.assembler;

/**
 *
 * @author serge
 */
public class MemoryWord {
    public final static int UNDEFINED = 0xDEADBEEF;
    public final static int MAX_ADDRESS = 0x7FF;
    public final static int MAX_UNSIGNED = 0xFFFF;
    public volatile int address = UNDEFINED;
    public volatile Label label = null;
    public volatile int value = UNDEFINED;
    public volatile String value_addr_reference = null;

    @Override
    public String toString() {
        return Integer.toHexString(address+0x100000).substring(3) + "| " +
                (label != null ? label.name + ": " : "" ) +
                "WORD " +
                (value != UNDEFINED? " 0x"+Integer.toHexString(value): "");
    }
    
    
}
