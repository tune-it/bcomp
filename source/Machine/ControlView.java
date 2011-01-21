/**
 * Класс, контролирующий настройки БЭВМ из пользовательского интерфейса
 * @author Ponomarev
 * @version $Id$
 */

package Machine;

import javax.swing.JTabbedPane;

public class ControlView
{
	public ControlView(JTabbedPane p)
	{
		jtp = p;
		tact = false;
		micro_com_work=false;
		sleep = sleeps[currentSleep = 3];
	}
	
	public void setTact()
	{
		tact = true;
	}
	
	public void clearTact()
	{
		tact = false;
	}
	
	// Must Be
	public boolean getTact()
	{
		return tact;
	}
	
	// Must Repaint
	public void repaint()
	{
		jtp.repaint();
	}
	
	public long getSleep()
	{
		return sleep;
	}

	public void setSleep(int sl)
	{
		sleep = sl;
	}
	
	// !!! Временное решение для обеспечения ротации задержки
	public void incSleep()
	{
		if ((++currentSleep) >= sleeps.length)
			currentSleep = 0;

		sleep = sleeps[currentSleep];
	}

	public void decSleep()
	{
		if ((--currentSleep) < 0)
			currentSleep = sleeps.length - 1;

		sleep = sleeps[currentSleep];
	}
	
	public boolean microWork()
	{
		return micro_com_work;
	}
	
	public void setMicroWork()
	{
		micro_com_work = true;
	}
	
	public void clearMicroWork()
	{
		micro_com_work = false;
	}
	
	private JTabbedPane jtp;
	private boolean tact;
	private boolean micro_com_work;
	private long sleep;
	// !!! Временное решение для обеспечения ротации задержки
	private final long[] sleeps = { 0, 1, 5, 10, 25, 50, 100, 1000 };
	private int currentSleep;
}
