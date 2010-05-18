import java.awt.EventQueue;
import javax.swing.JApplet;



public class EUI extends JApplet
{
	public void init ()
	{
		EventQueue.invokeLater(new Runnable()
		{
		public void run()
		{	 

			EUIBasePC BasePC = new EUIBasePC();	
			EUIMicroPC MicroPC = new EUIMicroPC();
			add(MicroPC);
			
			
		}
	});
	}
}
