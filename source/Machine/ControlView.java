package Machine;

import javax.swing.JTabbedPane;

/**
 * Класс, контролирующий настройки БЭВМ из пользовательского интерфейса
 * @author Ponomarev
 */
public class ControlView
{
	public ControlView(JTabbedPane p)
	{
		jtp = p;
		tact = false;
		micro_com_work=false;
		sleep = 10L;
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
}