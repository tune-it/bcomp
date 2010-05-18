
public class EMicrocommandMemory extends EMemory {

	public EMicrocommandMemory(ERegisterFactory factory)
	{
		super(factory.MicroInstructionPointer(), 256, 16);
	}
}
