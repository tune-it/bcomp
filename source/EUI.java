import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
			key_register = factory.CreateKeyRegister();
			inp1_register = factory.CreateInputRegister1();
			inp2_register = factory.CreateInputRegister2();
			
			EUIInputRegister[] inpregs = {key_register, inp1_register, inp2_register};
			input_registers = inpregs;
			
			JCheckBox movement_check = factory.CreateMovementCheckBox();
			JCheckBox tact_check = factory.CreateTactCheckBox();
			JCheckBox memory_check = factory.CreateMemoryCheckBox();
			JLabel work = factory.CreateWorkLabel();
			JRadioButton[] register_check = factory.CreateRegisterRadioButtons();
			
			
			ButtonGroup group = new ButtonGroup();
			for (int i=0; i<register_check.length; i++)
				group.add(register_check[i]);
				
			//Слушатель нажатия для чекбокса проверки сдвига
			ActionListener movement_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
		        	for(int i =0; i<input_registers.length; i++)
		        		if(input_registers[i].isMovement())
		        			input_registers[i].SetMovement(false);
		        		else
		        			input_registers[i].SetMovement(true);
					
					tabbedPane.repaint();
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
					tabbedPane.repaint();
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
					tabbedPane.repaint();
				}
			};
			memory_check.addActionListener(memory_listener);
			
			//Слушатель нажатия кнопки "Ввод в КР"
			ActionListener key_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
					key_register.SetActive(true);
					inp1_register.SetActive(false); 
					inp2_register.SetActive(false);
					tabbedPane.repaint();
				}
			};
			register_check[0].addActionListener(key_listener);
			
			
			//Слушатель нажатия кнопки "Ввод в КР"
			MouseListener tab_listener = new MouseListener()
			{
				public void mouseClicked(MouseEvent e) 
				{
					key_register.SetActive(true);
					inp1_register.SetActive(false); 
					inp2_register.SetActive(false);
					tabbedPane.repaint();	
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			};
			register_check[0].addActionListener(key_listener);
			tabbedPane.addMouseListener(tab_listener);
			
			//Слушатель нажатия кнопки "Ввод в ВУ 2"
			ActionListener inp1_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
					key_register.SetActive(false);
					inp1_register.SetActive(true); 
					inp2_register.SetActive(false);
					tabbedPane.repaint();
				}
			};
			register_check[1].addActionListener(inp1_listener);
			
			//Слушатель нажатия кнопки "Ввод в ВУ 3"
			ActionListener inp2_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
					key_register.SetActive(false);
					inp1_register.SetActive(false); 
					inp2_register.SetActive(true);
					tabbedPane.repaint();
				}
			};
			register_check[2].addActionListener(inp2_listener);
			
			final EUIBasePC BasePC = new EUIBasePC(factory, key_register, movement_check, tact_check, work);
			final EUIOutputPC OutputPC = new EUIOutputPC(factory,  input_registers, movement_check, tact_check, work, register_check);
			final EUIMicroPC MicroPC = new EUIMicroPC(factory,  key_register,  movement_check, tact_check, memory_check, work);
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
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].SetPointerPosition(input_registers[i].GetPointerPosition() + 1);
		        		
			        	tabbedPane.repaint();
	        		}
			       
			        if (e.getKeyCode() == KeyEvent.VK_LEFT)
	        		{
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].SetPointerPosition(input_registers[i].GetPointerPosition() - 1);
		        		
			        	tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
	        		{
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].SetBit();
		        		
			        	tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_0)
	        		{
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].SetBit(false);
		        		
			        	tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_1)
	        		{
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].SetBit(true);
		        	
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
		        		machine.Start();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_F8)
			        {
				        Runnable r = new MachineRunnable(machine);
				        final Thread t = new Thread(r);
			        	t.start();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_F9)
			        {
		        		machine.StopWork();
	        		}
		        }
			});	        

		}
	});
	}
	
	private	EControlView 			control;
	private EUIInputRegister 		key_register;
	private EUIInputRegister[]		input_registers;
	private EUIInputRegister 		inp1_register; 
	private EUIInputRegister 		inp2_register;
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
