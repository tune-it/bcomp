package Machine;

import javax.swing.JTabbedPane;

public class EControlView
{
	public EControlView(JTabbedPane p)
	{
		jtp = p;
		tact = false;
		micro_com_work=false;
	}
	
	public void SetTact()
	{
		tact = true;
	}
	
	public void ClearTact()
	{
		tact = false;
	}
	
	// Must Be
	public boolean GetTact()
	{
		return tact;
	}
	
	// Must Repaint
	public void Repaint()
	{
		jtp.repaint();
	}
	
	public int GetSleep()
	{
		return sleep;
	}
	
	public void SetSleep(int sl)
	{
		sleep = sl;
	}
	
	public boolean MicroWork()
	{
		return micro_com_work;
	}
	
	public void SetMicroWork()
	{
		micro_com_work = true;
	}
	
	public void GetMicroWork()
	{
		micro_com_work = false;
	}
	
	private JTabbedPane jtp;
	private boolean tact;
	private boolean micro_com_work;
	private int sleep;
}