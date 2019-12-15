/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.assembler;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public enum Instruction {
    
        //address
        AND(0x2000,"AND",Type.ADDR),
        OR(0x3000,"OR" ,Type.ADDR),
        ADD(0x4000,"ADD",Type.ADDR),
        ADC(0x5000,"ADC",Type.ADDR),
        SUB(0x6000,"SUB",Type.ADDR),
        CMP(0x7000,"CMP",Type.ADDR),
        LOOP(0x8000,"LOOP",Type.ADDR),
        LD(0xA000,"LD",Type.ADDR),
        SWAM(0xB000,"SWAM",Type.ADDR),
        JUMP(0xC000,"JUMP",Type.ADDR),
        CALL(0xD000,"CALL",Type.ADDR),
        ST(0xE000,"ST",Type.ADDR),
        //addressless
        NOP(0x0000,"NOP",Type.NONADDR),
        HLT(0x0100,"HLT",Type.NONADDR),
        CLA(0x0200,"CLA",Type.NONADDR),
        NOT(0x0280,"NOT",Type.NONADDR),
        CLC(0x0300,"CLC",Type.NONADDR),
        CMC(0x0380,"CMC",Type.NONADDR),
        ROL(0x0400,"ROL",Type.NONADDR),
        ROR(0x0480,"ROR",Type.NONADDR),
        ASL(0x0500,"ASL",Type.NONADDR),
        ASR(0x0580,"ASR",Type.NONADDR),
        SXTB(0x0600,"SXTB",Type.NONADDR),
        SWAB(0x0680,"SWAB",Type.NONADDR),
        INC(0x0700,"INC",Type.NONADDR),
        DEC(0x0740,"DEC",Type.NONADDR),
        NEG(0x0780,"DEC",Type.NONADDR),
        POP(0x0800,"POP",Type.NONADDR),
        POPF(0x0900,"POPF",Type.NONADDR),
        RET(0x0A00,"RET",Type.NONADDR),
        IRET(0x0B00,"IRET",Type.NONADDR),
        PUSH(0x0C00,"PUSH",Type.NONADDR),
        PUSHF(0x0D00,"PUSHF",Type.NONADDR),
        SWAP(0x0E00,"SWAP",Type.NONADDR),
        //branch
        BEQ(0xF000,"BEQ",Type.BRANCH),
        BNE(0xF100,"BNE",Type.BRANCH),
        BMI(0xF200,"BMI",Type.BRANCH),
        BPL(0xF300,"BPL",Type.BRANCH),
        BCS(0xF400,"BCS",Type.BRANCH),
        BCC(0xF500,"BCC",Type.BRANCH),
        BVS(0xF600,"BVS",Type.BRANCH),
        BVC(0xF700,"BVC",Type.BRANCH),
        BLT(0xF800,"BLT",Type.BRANCH),
        BGE(0xF900,"BGE",Type.BRANCH),
        BR(0xCE00,"BR", Type.BRANCH),

        END(1,"",Type.NONADDR);

        Instruction(int opcode, String mnemonic, Type type) {
            this.opcode = opcode;
            this.mnemonic = mnemonic;
            this.type = type;
        }
        
        public enum Type {
		ADDR, NONADDR, BRANCH, IO
	};

	public final int opcode;
	public final String mnemonic;
	public Type type;

	public int getOpcode() {
		return opcode;
	}

	public String getMnemonic() {
            return mnemonic;
	}

	public Type getType() {
            return type;
	}
        
        public String getTypeString() {
            switch (type) {
                case ADDR: return "ADDR";
                case NONADDR: return "NONADDR";
                case BRANCH: return "BRANCH";
                case IO: return "IO";
                default: return "";
            }
        }
        
}
