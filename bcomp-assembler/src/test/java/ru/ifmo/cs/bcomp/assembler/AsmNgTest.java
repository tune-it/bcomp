package ru.ifmo.cs.bcomp.assembler;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class AsmNgTest {
    @Test
    public void directAbsoluteAddressingMode() {
        validateConversion("JUMP 0x5", "C005");
        validateConversion("SAME: ADD $SAME", "4000");

        assertHasErrors("LD -10");
        assertHasErrors("LD 0x800");
        assertHasErrors("LD $UNDEFINED");
    }

    @Test
    public void indirectAddressingMode() {
        validateConversion("SAME: ADD (SAME)", "48FF");

        assertHasErrors("LD (10)");
        assertHasErrors("LD $UNDEFINED");
    }

    @Test
    public void postIncrementAddressingMode() {
        validateConversion("SAME: ADD (SAME)+", "4AFF");

        assertHasErrors("LD (10)+");
        assertHasErrors("LD (UNDEFINED)+");
    }

    @Test
    public void preDecrementAddressingMode() {
        validateConversion("SAME: ADD -(SAME)", "4BFF");

        assertHasErrors("LD -(10)");
        assertHasErrors("LD -(UNDEFINED)");
    }

    @Test
    public void displacementSPAddressingMode() {
        validateConversion("LD (Sp+-0x5)", "ACFB");
        validateConversion("LD &0xF", "AC0F");

        assertHasErrors("LD (sp+128)");
        assertHasErrors("LD &UNDEFINED");
    }

    @Test
    public void directRelativeAddressingMode() {
        validateConversion("JUMP (ip+5)", "CE05");
        validateConversion("SAME: ADD SAME", "4EFF");
        validateConversion("LD (ip+-2)", "AEFE");

        assertHasErrors("LD (ip+-129)");
        assertHasErrors("LD (ip+128)");
        assertHasErrors("LD UNDEFINED");
    }

    @Test
    public void directLoadAddressingMode() {
        validateConversion("LD #0x10", "AF10");
        validateConversion("LD #0xFF", "AFFF");

        assertHasErrors("LD #256");
        assertHasErrors("LD #-129");
        assertHasErrors("LD #UNDEFINED");
    }

    private static void validateConversion(String assembly, String instruction) {
        validateProgram(Collections.singletonMap(assembly, instruction));
    }

    private static void validateProgram(Map<String, String> assemblyToInstruction) {
        AsmNg sourceCode = getAssemblySource(assemblyToInstruction.keySet());
        Program program = sourceCode.compile();
        assertNotNull(program);
        assertEquals("Errors occurred while compiling [" + assemblyToInstruction.keySet() + "]", Collections.EMPTY_LIST, sourceCode.getErrors());
        String[] correctInstructions = assemblyToInstruction.values().toArray(new String[]{});

        for (int i = 0; i < program.binary.size(); i++) {
            String actual = getHexInstruction(program.binary.get(i));
            String expected = correctInstructions[i];
            assertEquals(expected, actual);
        }
    }

    private static void assertHasErrors(String assembly) {
        AsmNg sourceCode = getAssemblySource(Collections.singleton(assembly));
        sourceCode.compile();
        assertNotEquals("No errors occurred while compiling [" + assembly + "]", Collections.EMPTY_LIST, sourceCode.getErrors());
    }

    private static AsmNg getAssemblySource(Collection<String> assembly) {
        return new AsmNg(assembly.stream().collect(Collectors.joining("\n", "ORG 0\nSTART: \n", "")));
    }

    private static String getHexInstruction(int value) {
        return Integer.toHexString(value).toUpperCase();
    }
}