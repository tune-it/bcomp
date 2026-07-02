package ru.ifmo.cs.bcomp.axl;

import ru.ifmo.cs.bcomp.assembler.AsmNg;
import ru.ifmo.cs.bcomp.assembler.Program;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class Cli {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("usage: Cli <file.axl> [--asm]");
            System.exit(2);
            return;
        }
        boolean asmOnly = false;
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--asm")) {
                asmOnly = true;
            }
        }

        byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
        String src = new String(bytes, Charset.forName("UTF-8"));

        AxlCompiler cc = new AxlCompiler();
        String asm = cc.compile(src);
        if (asm == null) {
            for (String e : cc.getErrors()) {
                System.err.println(e);
            }
            System.exit(1);
            return;
        }

        if (asmOnly) {
            System.out.print(asm);
            return;
        }

        AsmNg assembler = new AsmNg(asm);
        Program prog = assembler.compile();
        if (prog == null || !assembler.getErrors().isEmpty()) {
            System.err.println("assembler errors:");
            for (String e : assembler.getErrors()) {
                System.err.println(e);
            }
            System.err.println(asm);
            System.exit(1);
            return;
        }

        System.out.println("load_address=0x" + Integer.toHexString(prog.load_address));
        System.out.println("start_address=0x" + Integer.toHexString(prog.start_address));
        List<Integer> binary = prog.binary;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < binary.size(); i++) {
            sb.append(String.format("%04X ", binary.get(i) & 0xFFFF));
            if ((i & 7) == 7) {
                sb.append('\n');
            }
        }
        System.out.println(sb.toString().trim());
    }

    private Cli() {
    }
}
