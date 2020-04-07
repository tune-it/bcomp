/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.assembler;
import static ru.ifmo.cs.bcomp.assembler.Instruction.Type.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public enum Instruction {
        
    //address
    AND(0x2000,ADDR), OR(0x3000 ,ADDR), ADD(0x4000,ADDR), ADC(0x5000,ADDR),
    SUB(0x6000,ADDR), CMP(0x7000,ADDR), LOOP(0x8000,ADDR), LD(0xA000,ADDR),
    SWAM(0xB000,ADDR), JUMP(0xC000,ADDR), CALL(0xD000,ADDR), ST(0xE000,ADDR),
    //addressless
    NOP(0x0000,NONADDR), HLT(0x0100,NONADDR), CLA(0x0200,NONADDR),
    NOT(0x0280,NONADDR), CLC(0x0300,NONADDR), CMC(0x0380,NONADDR),
    ROL(0x0400,NONADDR), ROR(0x0480,NONADDR), ASL(0x0500,NONADDR),
    ASR(0x0580,NONADDR), SXTB(0x0600,NONADDR), SWAB(0x0680,NONADDR),
    INC(0x0700,NONADDR), DEC(0x0740,NONADDR), NEG(0x0780,NONADDR),
    POP(0x0800,NONADDR), POPF(0x0900,NONADDR), RET(0x0A00,NONADDR),
    IRET(0x0B00,NONADDR), PUSH(0x0C00,NONADDR), PUSHF(0x0D00,NONADDR),
    SWAP(0x0E00,NONADDR),
    //branch
    BEQ(0xF000,BRANCH), BNE(0xF100,BRANCH), BMI(0xF200,BRANCH), BPL(0xF300,BRANCH),
    BCS(0xF400,BRANCH), BCC(0xF500,BRANCH), BVS(0xF600,BRANCH), BVC(0xF700,BRANCH),
    BLT(0xF800,BRANCH), BGE(0xF900,BRANCH), BR(0xCE00, BRANCH),
    //io
    DI(0x1000,NONADDR),EI(0x1100,NONADDR),IN(0x1200,IO),OUT(0x1300,IO),INT(0x1800,IO),

    END(1,Type.NONADDR);

    public static final Instruction [] values = Instruction.values();

    Instruction(int opcode, Type type) {
        this.opcode = opcode;
        this.mnemonic = this.name();
        this.type = type;
    }

    public enum Type {
            ADDR, NONADDR, BRANCH, IO
    };

    public final int opcode;
    public final String mnemonic;
    public Type type;

    public String getTypeString() {
       return type.name();
    }
        
}
