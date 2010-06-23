package Machine;

public interface InternalDevice
{
	public EFlag		getStateFlag();
	public ERegister	getDataRegister();
	public IChannel		getDataChannel();
	
	public IChannel	getIORequestChannel();
	public IChannel	getAdressChannel();
	public IChannel	getStateFlagChannel();
	public IChannel	getInterruptionRequestChannel();
}