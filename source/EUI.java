import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import Machine.EControlView;
import Machine.EMachine;

public class EUI extends JApplet
{	
	public void init ()
	{
		EventQueue.invokeLater(new Runnable()
		{
		public void run()
		{	 
			final JTabbedPane tabbedPane = new JTabbedPane();
			
			EControlView c = new EControlView(tabbedPane);
			c.SetTact();
			final EMachine machine = new EMachine(c);
			
			EUIBasePCFactory factory = new EUIBasePCFactory(machine);
			input_register = new EUIInputRegister(machine.GetRegFac().InputRegister(), 1, 460, 60, 32, 478, "Клавишный Регистр");
			
			final EUIBasePC BasePC = new EUIBasePC(factory, input_register);
			final EUIOutputPC OutputPC = new EUIOutputPC(factory,  input_register);
			final EUIMicroPC MicroPC = new EUIMicroPC(factory,  input_register);
			final JPanel finalpanel = new JPanel();
			finalpanel.setLayout(null);
			
			
			
			
			tabbedPane.addTab("Базовая ЭВМ", BasePC);
			tabbedPane.addTab("Ввод/Вывод", OutputPC);
	        tabbedPane.addTab("Работа с МК", MicroPC);
	        tabbedPane.setSize(852, 550);
	        tabbedPane.setFocusable(false);
	        finalpanel.add(tabbedPane);
	        finalpanel.setFocusable(true);
       
	        finalpanel.setBackground(Color.WHITE);
	        add(finalpanel);
	        
	        
	        
	        finalpanel.addKeyListener(new KeyAdapter() {
	        	public void keyPressed(KeyEvent e) 
		        {     
			        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
	        		{
			        	input_register.SetPointerPosition(input_register.GetPointerPosition() + 1);
		        		tabbedPane.repaint();
	        		}
			       
			        if (e.getKeyCode() == KeyEvent.VK_LEFT)
	        		{
			        	input_register.SetPointerPosition(input_register.GetPointerPosition() - 1);
		        		tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
	        		{
			        	input_register.SetBit();
		        		tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_0)
	        		{
			        	input_register.SetBit(false);
		        		tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_1)
	        		{
			        	input_register.SetBit(true);
		        		tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_F4)
			        {
		        		machine.Adress();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_F5)
			        {
		        		machine.Record();
	        		}

			        if (e.getKeyCode() == KeyEvent.VK_F7)
			        {
		        		machine.StopWork();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_F8)
			        {
				        Runnable r = new MachineRunnable(machine);
				        final Thread t = new Thread(r);
			        	t.start();
	        		}

		        }
			});	        

		}
	});
	}
	
	EUIInputRegister input_register;
}

class MachineRunnable implements Runnable
{
	public MachineRunnable(EMachine machine)
	{
		this.machine = machine;
	}
	
	public void run()
	{
		machine.Continue();
	}
	private EMachine machine;
}
