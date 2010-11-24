package Machine;

public interface InternalDevice
{
	public Flag		getStateFlag();
	public Register	getDataRegister();
	public IChannel		getDataChannel();
	
	public IChannel	getIORequestChannel();
	public IChannel	getAdressChannel();
	public IChannel	getStateFlagChannel();
	public IChannel	getInterruptionRequestChannel();
}