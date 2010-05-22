package Machine;

/*-----------------------------------------------------------------------------
	Интерфейс памяти.
-----------------------------------------------------------------------------*/
public interface IMemory {
	public int SendData();
	public void GetData(int bits);
	public int Width();
	public int[] GetMemory();
}
