package Machine;

import javax.swing.JTabbedPane;

public class EControlView
{
	public EControlView(JTabbedPane p)
	{
		jtp = p;
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
	
	private JTabbedPane jtp;
	private boolean tact;
}