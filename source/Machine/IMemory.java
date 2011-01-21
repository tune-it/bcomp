/**
 * Интерфейс памяти.
 * @author Ponomarev
 * @version $Id$
 */

package Machine;

public interface IMemory {
	public int getValue();
	public void setValue(int bits);
	public int width();
	public int[] getMemory();
}
