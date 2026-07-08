package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

// Доказательство ОШИБКИ 1: составное присваивание (op=) в lvalue с побочным
// эффектом должно вычислять адрес ровно один раз.
// До фикса эти тесты падают, после фикса — проходят.
// Положить в: bcomp-axl/src/test/java/ru/ifmo/cs/bcomp/axl/
public class AxlCompoundLvalueTest {

    @Test
    public void pointerPostIncCompound() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int buf[3]; int r0; int r1; int rp;\n"
                + "void main() {\n"
                + "  int *p;\n"
                + "  buf[0] = 10; buf[1] = 20; buf[2] = 30;\n"
                + "  p = buf;\n"
                + "  *p++ += 5;\n"
                + "  r0 = buf[0]; r1 = buf[1]; rp = p - buf;\n"
                + "  halt();\n"
                + "}");
        assertEquals(15, r.mem("g_r0")); // buf[0] += 5
        assertEquals(20, r.mem("g_r1")); // buf[1] не тронут
        assertEquals(1, r.mem("g_rp"));  // p сдвинулся ровно на один элемент
    }

    @Test
    public void arrayIndexPostIncCompound() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int a[4]; int ri; int a0; int a1;\n"
                + "void main() {\n"
                + "  int i; i = 0;\n"
                + "  a[0] = 0; a[1] = 0; a[2] = 0; a[3] = 0;\n"
                + "  a[i++] += 10;\n"
                + "  ri = i; a0 = a[0]; a1 = a[1];\n"
                + "  halt();\n"
                + "}");
        assertEquals(1, r.mem("g_ri"));  // i увеличился ровно один раз
        assertEquals(10, r.mem("g_a0")); // a[0] обновлён
        assertEquals(0, r.mem("g_a1"));  // a[1] не тронут
    }
}
