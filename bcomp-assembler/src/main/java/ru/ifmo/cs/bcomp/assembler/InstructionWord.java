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
public class InstructionWord extends MemoryWord {
    public volatile Instruction instruction;
    public volatile AddressingMode operand = null;
    
    @Override
    public String toString() {
        return  Integer.toHexString(address+0x100000).substring(3) + "| " +
                (label != null ? label.name + ": " : "" ) +
                instruction.mnemonic + " " +
                (instruction.type == Instruction.Type.ADDR? operand.toString(): "") +
                (instruction.type == Instruction.Type.BRANCH? " label="+operand.reference: "") +
                " \t; type=" + instruction.type + 
                (value != UNDEFINED? " value=0x"+Integer.toHexString(value): "");
    }   
    
}
