import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
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
			control = new EControlView(tabbedPane);
			final EMachine machine = new EMachine(control);
			
			EUIBasePCFactory factory = new EUIBasePCFactory(machine);	
			input_register = factory.CreateInputRegister();
			
			JCheckBox movement_check = factory.CreateMovementCheckBox();
			JCheckBox tact_check = factory.CreateTactCheckBox();
			JCheckBox memory_check = factory.CreateMemoryCheckBox();
			
			//Слушатель нажатия для чекбокса проверки сдвига
			ActionListener movement_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
					if(input_register.GetMovement())
						input_register.SetMovement(false);
					else
						input_register.SetMovement(true);
				}
			};
			movement_check.addActionListener(movement_listener);

			//Слушатель нажатия для чекбокса "Такт"
			ActionListener tact_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
					if(control.GetTact())
						control.ClearTact();
					else
						control.SetTact();
				}
			};
			tact_check.addActionListener(tact_listener);
			
			//Слушатель нажатия для чекбокса "Работа с памятью МК"
			ActionListener memory_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
					if(control.MicroWork())
						control.ClearMicroWork();
					else
						control.SetMicroWork();
				}
			};
			memory_check.addActionListener(memory_listener);

			final EUIBasePC BasePC = new EUIBasePC(factory, input_register, movement_check, tact_check);
			final EUIOutputPC OutputPC = new EUIOutputPC(factory,  input_register, movement_check, tact_check);
			final EUIMicroPC MicroPC = new EUIMicroPC(factory,  input_register,  movement_check, tact_check, memory_check);
			final JPanel finalpanel = new JPanel();
			finalpanel.setLayout(null);
			
			tabbedPane.addTab("Базовая ЭВМ", BasePC);
			tabbedPane.addTab("Работа с ВУ", OutputPC);
	        tabbedPane.addTab("Работа с МПУ", MicroPC);
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
	
	private	EControlView 			control;
	private EUIInputRegister 		input_register;
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
	
	private EMachine 				machine;
}
