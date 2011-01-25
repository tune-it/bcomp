/**
 * Основная память
 * @version $Id$
 */

package Machine;

public class Memory implements IRegister, IMemory
{
	public Memory(RegisterFactory factory)
	{
		this.address_register = factory.getAddressRegister();
		this.memory_width = 16;
		this.memory_length = 1 << factory.getAddressRegister().width();
		memory = new Register[memory_length];
		for (int i = 0; i < memory_length; i++)
		{
			memory[i] = new Register();
		}
	}
	
	public Memory(Register address_register, int length, int width)
	{
		this.address_register = address_register;
		this.memory_width = width;
		this.memory_length = length;
		memory = new Register[memory_length];
		for (int i = 0; i < memory_length; i++)
		{
			memory[i] = new Register();
		}
	}
	
	public int getValue()
	{
		return memory[makeAddress()].getValue();
	}
	
	public void setValue(int bits)
	{
		memory[makeAddress()].setValue(bits);
	}
	
	public int width()
	{
		return memory_width;
	}
	
	private int makeAddress()
	{
		return address_register.getValue();
	}
	
	public int[] getMemory()
	{
		int[] mem = new int[memory_length];
		for (int i = 0; i < memory_length; i++)
		{
			mem[i] = memory[i].getValue();
		}
		return mem;
	}
	
	public void SetValue(int bits, int adr)
	{
		memory[adr].setValue(bits);
	}
	
	private int			memory_length;   	// Длина памяти
	private int			memory_width;    	// Разрядность памяти
	private Register[]	memory;          	// Память
	private Register	address_register;	// Регистр адреса
}
