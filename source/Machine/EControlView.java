package Machine;

import javax.swing.JTabbedPane;

public class EControlView
{
	public EControlView(JTabbedPane p)
	{
		jtp = p;
		tact = false;
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
	
	private JTabbedPane jtp;
	private boolean tact;
	private int sleep;
}