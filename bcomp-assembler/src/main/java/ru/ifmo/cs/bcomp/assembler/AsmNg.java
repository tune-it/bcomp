/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.bcomp.assembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.ifmo.cs.bcomp.grammar.*;
import ru.ifmo.cs.bcomp.grammar.BCompNGParser.*;

/**
 *
 * @author serge
 */
public class AsmNg {

    /**
     * If you don't have ORG directive assembler starts code generation from
     * base_address
     */
    public static final int BASE_ADDRESS = 0x10;

    public static void main(String[] args) throws Exception {
        AsmNg asmng = new AsmNg(
                "ORG FF\n"
                + "START: LOOP START\n"
                + "LD   #FF\n"
                + "IN \n"
                + "ad: and ad\n"
                + "ORG 030h\n"
                + "    OR $ad\n"
                + "bc:\n"
                + "    WORD бяка\n"
                + "    LD #0xFF\n"
                + "    LD #-0x10\n"
                + "    LD #0x-10\n"
                + "    ST &0\n"
                + "    ВЖУХ бяка\n"
                + "eb:    WORD 44H,33,49,50\n"
                + "бяка: WORD 22H\n"
                + "    BR бяка\n"
                + "    ПРЫГ (bc)\n"
                + "    WORD 1 dup(-0x10)\n"
                + "    WORD 0x12,?,0x13 ; komment\n"
                + "");
        Program prog = asmng.compile();
        System.out.println("-------errors--------");
        System.out.println(asmng.getErrors());
        if (prog != null) {
            System.out.println("-------words--------");
            System.out.println(prog.toCompiledWords());
            System.out.println("-------binary--------");
            System.out.println(prog.toBinaryRepresentation());
        } else {
            System.out.println("Program is not compiled");
        }
    }

    private CodePointCharStream program;
    private BCompNGLexer lexer;
    private CommonTokenStream tokens;
    private BCompNGParser parser;
    private AssemblerAntlrErrorStrategy errHandler;
    private HashMap<String, Label> labels;
    private HashMap<Integer, MemoryWord> memory;
    private List<String> errors;

    protected AsmNg(CodePointCharStream program) {
        this.program = program;
        labels = new HashMap<String, Label>();
        memory = new HashMap<Integer, MemoryWord>();
        //
        lexer = new BCompNGLexer(program);
        tokens = new CommonTokenStream(lexer);
        this.parser = new BCompNGParser(tokens);
        errHandler = new AssemblerAntlrErrorStrategy();
        parser.setErrorHandler(errHandler);
        errors = new ArrayList<String>();
        ANTLRErrorListener lsnr = new AsmNGErrorListener(errors);
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(lsnr);
        parser.addErrorListener(lsnr);
    }

    public AsmNg(String program) {
        //TODO fix grammar prog statement
        this(CharStreams.fromString(program+"\n"));
    }

    public BCompNGParser getParser() {
        return parser;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Program compile() {
        Program prog = null;
        try {
            //decode commands and collect all labels
            //System.out.println("first pass");        
            firstPass();
            //debug output
            //System.out.println(labels);     
            //System.out.println("second pass");
            prog = secondPass();
        } catch (AssemblerException e) {
            reportAndRecoverFromError(e);
        }
        return prog;
    }

    protected void firstPass() {
        RuleContext tree = getParser().prog();
        ParseTreeWalker walker = new ParseTreeWalker();
        BCompNGListener fp = new BCompNGBaseListener() {
            private int address = BASE_ADDRESS;

            @Override
            public void enterLine(LineContext ctx) {
                //verbose output for debug only
                //System.out.println("sourceline = "+ctx.getText());
            }

            @Override
            public void exitInstructionLine(InstructionLineContext ctx) {
                LblContext LCtx = ctx.lbl();
                Label label = null;
                if (LCtx != null) {
                    String labelname = LCtx.label().getText();
                    label = labels.get(labelname);
                }
                InstructionContext ICtx = ctx.instruction();
                if (ICtx != null) {
                    TerminalNode t = getTerminalNode(ICtx);
                    if (t == null) {
                        reportAndRecoverFromError(new AssemblerException("Internal error: TerminalNode occasionally is null", parser, ICtx));
                        return;
                    }
                    InstructionWord i = new InstructionWord();
                    Instruction instr = instructionByParserType(t.getSymbol().getType());
                    if (instr == null) {
                        //parser has instruction description but ASM NG has not
                        //add new instruction to method instructionByParserType and Instruction
                        reportAndRecoverFromError(new AssemblerException("Internal error: Parser has instruction but assebler hasn't", parser, ICtx));
                        return;
                    }
                    i.instruction = instr;
                    i.address = address;
                    if (label != null) {
                        i.label = label;  //labels can also be null by default
                    }
                    OperandContext OCtx = ICtx.operand();
                    if (OCtx != null) {
                        AddressingMode am = addressingModeByParserContext(OCtx);
                        i.operand = am;
                    }
                    //process branches. Label in InstructionContext for now can only
                    //be in branches
                    if (instr.type == Instruction.Type.BRANCH && ICtx.label() != null) {
                        //make fake AddressingMode with references set up
                        AddressingMode am = new AddressingMode();
                        i.operand = am;
                        //make String copy. Do not remove new String(..)
                        i.operand.reference = new String(ICtx.label().getText());
                    }
                    if (instr.type == Instruction.Type.IO) {
                        AssemblerException ae = new AssemblerException("Device or vector shall be valid number", parser, ICtx);
                        if (ICtx.dev() == null) {
                                reportAndRecoverFromError(ae);return;
                        }
                        NumberContext nc = ICtx.dev().number();
                        if (nc == null) {
                            reportAndRecoverFromError(ae);return;
                        }
                        Integer devnum = parseIntFromNumberContext(nc,parser);
                        if (devnum == null) {
                            reportAndRecoverFromError(ae);return;
                        }
                        i.device = devnum;
                    }
                    memory.put(i.address, i);
                    address++;
                }
            }

            @Override
            public void exitWordArgument(WordArgumentContext ctx) {
                MemoryWord m = new MemoryWord();
                m.address = address;
                //parse direct numbers
                NumberContext nc = ctx.number();
                if (nc != null) {
                    Integer i = parseIntFromNumberContext(nc,parser);
                    m.value = i;
                }
                //undefined number will assume to 0
                if ("?".equals(ctx.getText())) {
                    m.value = 0;
                }
                LabelContext lc = ctx.label();
                if (lc != null) {
                    m.value_addr_reference = new String(lc.getText());
                }
                //find out label if one and set it up to the first WORD
                if (ctx.getParent().getParent() instanceof WordDirectiveContext) {
                    WordDirectiveContext wdctx = (WordDirectiveContext) ctx.getParent().getParent();
                    //if label exsist in line
                    if (wdctx.lbl() != null) {
                        //look for this label address
                        Label l = labels.get(wdctx.lbl().label().getText());
                        if (l != null) {
                            // if label points to this first word instruction
                            if (l.address == address) {
                                m.label = l;
                            }
                        }
                    }
                }
                DupArgumentContext dactx = ctx.dupArgument();
                if (dactx != null) {
                    Integer count = parseIntFromNumberContext(dactx.count().number(),parser);
                    if (count <= 1) {
                        //throw new RuntimeException("Internal error: count should be greater than 1");
                        reportError(new AssemblerException("DUP count should be greater than 1", parser, dactx));
                        return;
                    }
                    WordArgumentContext what = dactx.wordArgument();
                    int whatnum = 0;
                    if (!"?".equals(what.getText())) {
                        whatnum = parseIntFromNumberContext(what.number(),parser);
                    }
                    //System.out.println("DUP="+count+" of "+whatnum);
                    for (int mm = 1; mm < count; mm++) {
                        MemoryWord dupm = new MemoryWord();
                        dupm.address = address++;
                        dupm.value = whatnum;
                        memory.put(dupm.address, dupm);
                    }
                    return;
                }

                memory.put(m.address, m);

                //System.out.println("WORD value = "+i);
                address++;
            }

            @Override
            public void exitLbl(LblContext ctx) {
                Label lab = new Label();
                //make String copy. Do not remove new String(..)
                lab.name = new String(ctx.label().getText().trim());
                lab.address = address;
                if (labels.containsKey(lab.name)) {
                    //TODO FIX IT with common error message
                    reportAndRecoverFromError(new AssemblerException("Error: already defined label " + lab.name, parser, ctx));
                    return;
                }
                //TODO fix this special case for start label
                if ("START".equalsIgnoreCase(lab.name) || "НАЧАЛО".equalsIgnoreCase(lab.name)) {
                    labels.put(lab.name, lab);
                    lab.name = "START";
                }
                labels.put(lab.name, lab);
            }

            @Override
            public void exitOrgAddress(OrgAddressContext ctx) {
                NumberContext n = ctx.address().number();
                Integer i = parseIntFromNumberContext(n,parser);
                address = i;
            }

        };
        walker.walk(fp, tree);
    }

    protected Program secondPass() {
        if (memory.keySet().isEmpty()) {
            //we need to stop compiling. Cant compile nothing
            AssemblerException ae = new AssemblerException("Second pass failed: no instruction was compiled on first pass.", parser);
            reportError(ae);
            return null;
        }
        LinkedList<Integer> addresses = new LinkedList<Integer>(memory.keySet());
        LinkedList<Integer> binary = new LinkedList<Integer>();
        Program prog = new Program();
        //
        Collections.sort(addresses);
        prog.load_address = addresses.getFirst();
        prog.start_address = prog.load_address;
        if (labels.containsKey("START")) {
            prog.start_address = labels.get("START").address;
        }
        int prev = addresses.getFirst();
        for (Integer addr : addresses) {
            MemoryWord w = memory.get(addr);
            if (w instanceof InstructionWord) {
                InstructionWord iw = (InstructionWord) w;
                iw.value = iw.instruction.opcode;
                switch (iw.instruction.type) {
                    case NONADDR:
                        break;
                    case ADDR:
                        compileOperand(iw);
                        break;
                    case BRANCH:
                        iw.value = iw.instruction.opcode | convertReferenceToDisplacement(iw);
                        break;
                    case IO:
                        if (iw.instruction.opcode == Instruction.INT.opcode) {
                            if (iw.device < 0 || iw.device > 7) {
                                reportError(new AssemblerException("Second pass: vector exceed limits [0..7]",parser));
                            }
                            iw.value = iw.instruction.opcode | iw.device;
                            break;
                        }
                        if (iw.device < 0 || iw.device > 255) {
                            reportError(new AssemblerException("Second pass: device number exceed limits [0..0xff]",parser));
                        }
                        iw.value = iw.instruction.opcode | iw.device;
                        break;
                }
            }
            if (w.value_addr_reference != null) {
                Label l = labels.get(w.value_addr_reference);
                if (l == null) {
                    //TODO error
                    reportError(new AssemblerException("Second pass: Label " + w.value_addr_reference + " not found", parser));
                } else {
                    w.value = l.address;
                }
            }
            while (w.address - prev > 1) {
                //generate zeroes when hole found
                binary.add(0);
                prev++;
            }
            binary.add(w.value);
            prev = w.address; //to be sure
            //System.out.println(w);
        }
        prog.binary = binary;
        prog.labels = labels;
        prog.content = memory;
        return prog;
    }

    private static Integer parseIntFromNumberContext(NumberContext nc,Parser parser) {
        Integer number = null;
        String text = null;
        if (nc.DECIMAL() != null) {
            text = nc.DECIMAL().getText();
            text = text.replaceAll("0[dD]", "");
            //System.out.println(text);
            number = Integer.parseInt(text);
            return number;
        }
        if (nc.HEX() != null) {
            text = nc.HEX().getText();
            //System.out.println(text);
            text = text.replaceAll("(0[xX])|[hH]", "");
            //System.out.println(text);
            number = Integer.parseInt(text, 16);
            return number;
        }
        if (number==null) {
            throw new AssemblerException("Could not recognize valid number while parsing "+nc.getText()+" operand",parser);
        }
        return number;
    }

    private static TerminalNode getTerminalNode(ParseTree p) {
        TerminalNode t = null;
        for (int i = 0; i < p.getChildCount(); i++) {
            ParseTree internal = p.getChild(i);
            if (internal instanceof TerminalNode) {
                t = (TerminalNode) internal;
                break;
            } else {
                t = getTerminalNode(internal);
                if (t != null) {
                    break;
                }
            }
        }
        return t;
    }

    public final Instruction instructionByParserType(int parserType) {
        Instruction i = null;
        switch (parserType) {
            //address commands
            case BCompNGParser.AND:
                i = Instruction.AND;
                break;
            case BCompNGParser.OR:
                i = Instruction.OR;
                break;
            case BCompNGParser.ADD:
                i = Instruction.ADD;
                break;
            case BCompNGParser.ADC:
                i = Instruction.ADC;
                break;
            case BCompNGParser.SUB:
                i = Instruction.SUB;
                break;
            case BCompNGParser.CMP:
                i = Instruction.CMP;
                break;
            case BCompNGParser.LOOP:
                i = Instruction.LOOP;
                break;
            case BCompNGParser.LD:
                i = Instruction.LD;
                break;
            case BCompNGParser.SWAM:
                i = Instruction.SWAM;
                break;
            case BCompNGParser.JUMP:
                i = Instruction.JUMP;
                break;
            case BCompNGParser.CALL:
                i = Instruction.CALL;
                break;
            case BCompNGParser.ST:
                i = Instruction.ST;
                break;
            //Addressless
            case BCompNGParser.NOP:
                i = Instruction.NOP;
                break;
            case BCompNGParser.HLT:
                i = Instruction.HLT;
                break;
            case BCompNGParser.CLA:
                i = Instruction.CLA;
                break;
            case BCompNGParser.NOT:
                i = Instruction.NOT;
                break;
            case BCompNGParser.CLC:
                i = Instruction.CLC;
                break;
            case BCompNGParser.CMC:
                i = Instruction.CMC;
                break;
            case BCompNGParser.ROL:
                i = Instruction.ROL;
                break;
            case BCompNGParser.ROR:
                i = Instruction.ROR;
                break;
            case BCompNGParser.ASL:
                i = Instruction.ASL;
                break;
            case BCompNGParser.ASR:
                i = Instruction.ASR;
                break;
            case BCompNGParser.SXTB:
                i = Instruction.SXTB;
                break;
            case BCompNGParser.SWAB:
                i = Instruction.SWAB;
                break;
            case BCompNGParser.INC:
                i = Instruction.INC;
                break;
            case BCompNGParser.DEC:
                i = Instruction.DEC;
                break;
            case BCompNGParser.NEG:
                i = Instruction.NEG;
                break;
            case BCompNGParser.POP:
                i = Instruction.POP;
                break;
            case BCompNGParser.POPF:
                i = Instruction.POPF;
                break;
            case BCompNGParser.RET:
                i = Instruction.RET;
                break;
            case BCompNGParser.IRET:
                i = Instruction.IRET;
                break;
            case BCompNGParser.PUSH:
                i = Instruction.PUSH;
                break;
            case BCompNGParser.PUSHF:
                i = Instruction.PUSHF;
                break;
            case BCompNGParser.SWAP:
                i = Instruction.SWAP;
                break;
            //branch
            case BCompNGParser.BEQ:
                i = Instruction.BEQ;
                break;
            case BCompNGParser.BNE:
                i = Instruction.BNE;
                break;
            case BCompNGParser.BMI:
                i = Instruction.BMI;
                break;
            case BCompNGParser.BPL:
                i = Instruction.BPL;
                break;
            case BCompNGParser.BCS:
                i = Instruction.BCS;
                break;
            case BCompNGParser.BCC:
                i = Instruction.BCC;
                break;
            case BCompNGParser.BVS:
                i = Instruction.BVS;
                break;
            case BCompNGParser.BVC:
                i = Instruction.BVC;
                break;
            case BCompNGParser.BLT:
                i = Instruction.BLT;
                break;
            case BCompNGParser.BGE:
                i = Instruction.BGE;
                break;
            case BCompNGParser.BR:
                i = Instruction.BR;
                break;
            case BCompNGParser.EI:
                i = Instruction.EI;
                break;
            case BCompNGParser.DI:
                i = Instruction.DI;
                break;
            case BCompNGParser.IN:
                i = Instruction.IN;
                break;
            case BCompNGParser.OUT:
                i = Instruction.OUT;
                break;
            case BCompNGParser.INT:
                i = Instruction.INT;
                break;
                
            default:
        }
        return i;
    }

    private AddressingMode addressingModeByParserContext(OperandContext octx) {
        AddressingMode am = new AddressingMode();
        ParseTree pt = octx.getChild(0);
        if (pt == null | !(pt instanceof RuleContext)) {
            throw new AssemblerException("Internal error: after parser addressing mode cant be null and should be RuleContext", parser);
        }
        //System.out.println("!!!"+((RuleContext)pt).getRuleIndex());        
        switch (((RuleContext) pt).getRuleIndex()) {
            case BCompNGParser.RULE_directAbsolute:
                am.addressation = AddressingMode.AddressingType.DIRECT_ABSOLUTE;
                DirectAbsoluteContext dactx = octx.directAbsolute();
                if (dactx.address() != null) {
                    am.number = parseIntFromNumberContext(dactx.address().number(),parser);
                }
                if (dactx.label() != null) {
                    am.reference = referenceByLabelContext(dactx.label());
                }
                break;
            case BCompNGParser.RULE_indirect:
                am.addressation = AddressingMode.AddressingType.INDIRECT;
                am.reference = referenceByLabelContext(octx.indirect().label());
                break;
            case BCompNGParser.RULE_postIncrement:
                am.addressation = AddressingMode.AddressingType.POST_INCREMENT;
                am.reference = referenceByLabelContext(octx.postIncrement().label());
                break;
            case BCompNGParser.RULE_preDecrement:
                am.addressation = AddressingMode.AddressingType.PRE_DECREMENT;
                am.reference = referenceByLabelContext(octx.preDecrement().label());
                break;
            case BCompNGParser.RULE_displacementSP:
                am.addressation = AddressingMode.AddressingType.DISPLACEMENT_SP;
                Integer number_sp = parseIntFromNumberContext(octx.displacementSP().number(),parser);
                am.number = number_sp;
                break;
            case BCompNGParser.RULE_directRelative:
                am.addressation = AddressingMode.AddressingType.DIRECT_RELATIVE;
                am.reference = referenceByLabelContext(octx.directRelative().label());
                break;
            case BCompNGParser.RULE_directLoad:
                am.addressation = AddressingMode.AddressingType.DIRECT_LOAD;
                Integer number_dl = parseIntFromNumberContext(octx.directLoad().number(),parser);
                am.number = number_dl;
                break;
            default:
                throw new AssemblerException("Internal error: Wrong OperandContext while parsing addressing mode",parser);
        }
        return am;
    }

    private String referenceByLabelContext(LabelContext lctx) {
        if (lctx == null) {
            AssemblerException ae =  new AssemblerException("Internal error: LabelContex cant be null here",parser);
            reportError(ae);
        }
        //make String copy. Do not remove new String(..)
        return new String(lctx.getText());
    }

    private void compileOperand(InstructionWord iw) {
        if (iw.operand == null) {
            return;
        }
        int num = MemoryWord.UNDEFINED;
        switch (iw.operand.addressation) {
            case DIRECT_ABSOLUTE:
                if (iw.operand.number != MemoryWord.UNDEFINED) {
                    num = iw.operand.number;
                }
                if (iw.operand.reference != null) {
                    Label l = labels.get(iw.operand.reference);
                    if (l == null) {
                        reportError(new AssemblerException("Second pass: label refference "+iw.operand.reference+" not found",parser));
                    }
                    else { 
                        num = l.address;
                    }
                }
                if ((num > MemoryWord.MAX_ADDRESS) || (num < 0)) {
                    //TODO error number exceed limit values
                    reportError(new AssemblerException("Second pass: memory address 0x"+Integer.toHexString(num)+" out of range [0..0x7FF]",parser));
                }
                iw.value = iw.instruction.opcode | (num & MemoryWord.MAX_ADDRESS);
                break;
            case INDIRECT:
                iw.value = iw.instruction.opcode | 0x0800 | convertReferenceToDisplacement(iw);
                break;
            case POST_INCREMENT:
                iw.value = iw.instruction.opcode | 0x0A00 | convertReferenceToDisplacement(iw);
                break;
            case PRE_DECREMENT:
                iw.value = iw.instruction.opcode | 0x0B00 | convertReferenceToDisplacement(iw);
                break;
            case DISPLACEMENT_SP:
                if (iw.operand.number != MemoryWord.UNDEFINED) {
                    num = iw.operand.number;
                } else {
                    reportError(new AssemblerException("Second pass: number shoud present in command",parser));
                }
                if (num > 127 || num < -128) {
                    reportError(new AssemblerException("Second pass: stack displasment exceed limits [-127..128]",parser));
                }
                iw.value = iw.instruction.opcode | 0x0C00 | (num & 0xFF);
                break;
            case DIRECT_RELATIVE:
                iw.value = iw.instruction.opcode | 0x0E00 | convertReferenceToDisplacement(iw);
                break;
            case DIRECT_LOAD:
                if (iw.operand.number != MemoryWord.UNDEFINED) {
                    num = iw.operand.number;
                } else {
                    reportError(new AssemblerException("Second pass: number shoud present in command",parser));
                }
                if (num > 255 || num < -128) {
                    //TODO error number exceed limit values
                    throw new AssemblerException("Second pass: direct load operand exceed limits [-128..255]", parser);
                    //throw new RuntimeException("Internal error: ");
                }
                iw.value = iw.instruction.opcode | 0x0F00 | (num & 0xFF);
                break;
            default:
                reportError(new AssemblerException("Second pass: addressing mode is not properly defined",parser));
        }
    }

    private int convertReferenceToDisplacement(InstructionWord iw) {
        int num = MemoryWord.UNDEFINED;
        String reference = null;
        //address instructions
        if (iw.operand.reference != null) {
            reference = iw.operand.reference;
        }
        Label l = labels.get(reference);
        if (l == null) {
            AssemblerException ae = new AssemblerException("Second pass: label refference "+reference+" not found",parser);
            reportError(ae);
            return 0;
        }
        l.referenced = true;
        num = l.address - iw.address - 1; //-1 to fix impact of fetch cycle
        //TODO FIX
        if (num > 127 || num < -128) {
            AssemblerException ae = new AssemblerException("Second pass: label "+reference+" displacement exceed limits [-127..128]",parser);
            reportError(ae);
            num = 0;
        }
        return num & 0xFF;
    }

    private void reportError(AssemblerException ae) {
        errHandler.reportError(parser, ae);
    }
    
    private void reportAndRecoverFromError(AssemblerException ae) {
        errHandler.reportError(parser, ae);
        errHandler.recover(parser, ae);
    }

}

class AsmNGErrorListener extends BaseErrorListener {

    public List<String> errors;

    AsmNGErrorListener(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        StringBuilder sb = new StringBuilder();
        String symbol = offendingSymbol.toString();
        if (offendingSymbol instanceof org.antlr.v4.runtime.Token) {
            symbol = ((org.antlr.v4.runtime.Token) offendingSymbol).getText();
        }
        sb.append("at ").append(line).append(":").append(charPositionInLine);
        if (!("<EOF>".equalsIgnoreCase(symbol))) {
            sb.append(" near ").append(escapeWSAndQuote(symbol));
        }
        sb.append(" - ").append(msg);
        errors.add(sb.toString());
        //System.out.println("MY ERROR "+sb.toString());
    }

    protected String escapeWSAndQuote(String s) {
        if (s == null) {
            return s;
        }
        s = s.replace("\n", "\\n");
        s = s.replace("\r", "\\r");
        s = s.replace("\t", "\\t");
        return "'" + s + "'";
    }
};
