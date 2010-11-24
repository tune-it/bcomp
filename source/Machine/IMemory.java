package Machine;

/**
 * Интерфейс памяти.
 * @author Ponomarev
 */
public interface IMemory {
	public int getValue();
	public void setValue(int bits);
	public int width();
	public int[] getMemory();
}
