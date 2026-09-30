/**
 *
 * @author serge
 */

package ru.ifmo.cs.bcomp.api;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static ru.ifmo.cs.bcomp.api.BCompApi.compileAndExecuteProgram;

/**
 *
 * @author serge
 */
public class BCompAPITest {
    
    @Test
    public void TestProgramExecution() {
        String prog = 
"       НАЧ 0x10 ; Начоло\n" +
"ЕДУ:   НЯМ #5\n" +
"       СУНЬ\n" +
"       ВЖУХ $ВРОТ\n" +
"       ВЫНЬ\n" +
"       ТЬФУ $ТУТ\n" +
"       СТОП\n" +
"ТУТ:   СЛОВО 0xDEDA\n" +
"ВРОТ:  НЯМ  &1\n" +
"       СРАВ #1\n" +
"       БЯКА ВЫ\n" +
"       УМЕН\n" +
"       СУНЬ\n" +
"       ВЖУХ $ВРОТ\n" +
"       ВЫНЬ\n" +
"       ПЛЮС &1\n" +
"       ТЬФУ  &1\n" +
"ВЫ:    ВОЗВР\n" +
"       КОН";
        ExecutionResults res = compileAndExecuteProgram(prog,null,false);
        assertEquals(res.getMemoryValue("ТУТ"),15L);
        System.out.println(res);
    }
    
}
