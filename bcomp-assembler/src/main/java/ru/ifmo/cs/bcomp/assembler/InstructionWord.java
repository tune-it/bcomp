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
    
    public int getOpcode() {
            return instruction.getOpcode();
    }

    public String getMnemonic() {
        return instruction.getMnemonic();
    }

    public Instruction.Type getType() {
        return instruction.getType();
    }

    @Override
    public String toString() {
        return "Instruction{ address=" + address + 
                (label != null ? ", label=" + label.name : "" ) + 
                ", mnemonic=" + getMnemonic() + ", type=" + getType() + '}' +
                (instruction.type == Instruction.Type.ADDR? " operand="+operand.toString(): "") +
                (instruction.type == Instruction.Type.BRANCH? " label="+operand.reference: "") +
                (value != UNDEFINED? " value="+Integer.toHexString(value): "");
    }   
    
}
