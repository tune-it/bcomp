package Machine;

public interface InternalDevice
{
	public EFlag getStateFlag();
	public ERegister getDataRegister();
	public IChannel getChannel();
	public void connect();
}