/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Anastasia Prasolova <a-prasolova1507@yandex.ru>
 */
public class BasicCompTest {

	static MicroPrograms mps;
	static MicroProgram mprog;
	BasicComp bcomp;
	Assembler asm;
	String testString;
	Instruction[] instr;
	TestsSet testsSet = new TestsSet();

	@org.junit.Test
	public void main() throws Exception {
		mps = new MicroPrograms();

		String[] mprograms = {
			"base", "optimized", "extended"
		};
		CommandToTest cmd;
		KeyOperationsToTest keyOp;

		for (String mp : mprograms) {

			mprog = mps.getMicroProgram(mp);
			bcomp = new BasicComp(mprog);
			instr = bcomp.getCPU().getInstructionSet();
			asm = new Assembler(instr);
			String[] tests = testsSet.getTestStrings(mp);

			for (String testString : tests) {
				cmd = new CommandToTest(bcomp, asm, testString);
				cmd.testingCommand();
			}
			/*keyOp = new KeyOperationsToTest(bcomp.getCPU());
			keyOp.testingKeyOperations();*/
		}

	}
}
