/**
 * Фабрика устройств ввода/вывода
 * @author Ponomarev
 * @version $Id$
 */

package Machine;

public class DeviceFactory
{
	public DeviceFactory(RegisterFactory reg_factory)
	{
		dev = new InternalDevice[3];
		dev[0] = new InternalDevice(reg_factory, 0);
		dev[1] = new InternalDevice(reg_factory, 1);
		dev[2] = new InternalDevice(reg_factory);
	}
	
	public InternalDevice getInternalDevice(int deviceAdress)
	{
		
		deviceAdress--;
		if (deviceAdress < dev.length)
		{
			
			return dev[deviceAdress];
		}
		else
		{
			return null;
		}
		
	}
	
	public void clearAllFlags()
	{
		for(InternalDevice x : dev)
		{
			x.getStateFlag().clearFlag();
		}
	}
	
	public void closeAllChannels()
	{
		for (InternalDevice x : dev)
		{
				if ( x.getInputChannel() != null )	x.getInputChannel().close();
				if ( x.getOutputChannel() != null )	x.getOutputChannel().close();
				x.getAddressChannel().close();
				x.getInterruptionRequestChannel().close();
				x.getIORequestChannel().close();
				x.getStateFlagChannel().close();
		}
	}
	
	public boolean deviceRequest()
	{
		boolean request = false;
		
		for (InternalDevice x : dev)
		{
			request = request || (x.getStateFlag().getValue() == 1);
		}
		return request;
	}
	
	private InternalDevice dev[];
}
