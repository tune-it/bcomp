import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = 102886848843642743L;
	public void init ()
	{
		EventQueue.invokeLater(new Runnable()
		{
		public void run()
		{	 
			final JTabbedPane tabbedPane = new JTabbedPane();		
			control = new EControlView(tabbedPane);
			final EMachine machine = new EMachine(control);
			ObjectFactoryUI factory = new ObjectFactoryUI(machine);
			
			key_register = factory.createKeyRegister();
			inp1_register = factory.createInputRegister1();
			inp2_register = factory.createInputRegister2();
			
			InputRegisterUI[] inpregs = {key_register, inp1_register, inp2_register};
			input_registers = inpregs;
			
			JCheckBox movement_check = factory.createMovementCheckBox();
			JCheckBox tact_check = factory.createTactCheckBox();
			JCheckBox memory_check = factory.createMemoryCheckBox();
			work = factory.createWorkButton();
			register_check = factory.createRegisterRadioButtons();
			
			ButtonGroup group = new ButtonGroup();
			for (int i=0; i<register_check.length; i++)
				group.add(register_check[i]);

			final BasePCUI BasePC = new BasePCUI(factory, key_register, movement_check, tact_check, work);
			final IOUnitUI OutputPC = new IOUnitUI(factory, input_registers, movement_check, tact_check, work, register_check);
			final MPUnitUI MicroPC = new MPUnitUI(factory,  key_register,  movement_check, tact_check, memory_check, work);
			
			tabbedPane.addTab("Базовая ЭВМ", BasePC);
			tabbedPane.addTab("Работа с ВУ", OutputPC);
	        tabbedPane.addTab("Работа с МПУ", MicroPC);
	        tabbedPane.setSize(852, 550);
	        tabbedPane.setFocusable(false);
	        
	        final JPanel finalpanel = new JPanel();
			finalpanel.setLayout(null);
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
			        			input_registers[i].setPointerPosition(input_registers[i].getPointerPosition() + 1);
		        		
			        	tabbedPane.repaint();
	        		}
			       
			        if (e.getKeyCode() == KeyEvent.VK_LEFT)
	        		{
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].setPointerPosition(input_registers[i].getPointerPosition() - 1);
		        		
			        	tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
	        		{
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].setBit();
		        		
			        	tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_0)
	        		{
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].setBit(false);
		        		
			        	tabbedPane.repaint();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_1)
	        		{
			        	for(int i =0; i<input_registers.length; i++)
			        		if(input_registers[i].isActive())
			        			input_registers[i].setBit(true);
		        	
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
				        machine.Continue();
	        		}
			        
			        if (e.getKeyCode() == KeyEvent.VK_F9)
			        {
			        	if (work.getText() == "Работа")
						{
							work.setForeground(Color.BLACK);
							work.setText("Останов.");
						}
						else
						{
							work.setForeground(Color.RED);
							work.setText("Работа");
						}
			        	
		        		machine.StopWork();
	        		}
		        }
			});
	        
	      //Слушатель нажатия для чекбокса проверки сдвига
			ActionListener movement_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
		        	for(int i =0; i<input_registers.length; i++)
		        		if(input_registers[i].isMovement())
		        			input_registers[i].setMovement(false);
		        		else
		        			input_registers[i].setMovement(true);
					
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
					key_register.setActive(true);
					inp1_register.setActive(false); 
					inp2_register.setActive(false);
					
					tabbedPane.repaint();
				}
			};
			register_check[0].addActionListener(key_listener);
				
			//Слушатель нажатия щелчка по панели
			MouseListener tab_listener = new MouseListener()
			{
				public void mouseClicked(MouseEvent e) 
				{
					key_register.setActive(true);
					inp1_register.setActive(false); 
					inp2_register.setActive(false);
					register_check[0].setSelected(true);
					
					tabbedPane.repaint();	
				}

				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			};
			tabbedPane.addMouseListener(tab_listener);
			
			//Слушатель нажатия кнопки "Ввод в ВУ 2"
			ActionListener inp1_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
					key_register.setActive(false);
					inp1_register.setActive(true); 
					inp2_register.setActive(false);
					
					tabbedPane.repaint();
				}
			};
			register_check[1].addActionListener(inp1_listener);
			
			//Слушатель нажатия кнопки "Ввод в ВУ 3"
			ActionListener inp2_listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{	
					key_register.setActive(false);
					inp1_register.setActive(false); 
					inp2_register.setActive(true);
					
					tabbedPane.repaint();
				}
			};
			register_check[2].addActionListener(inp2_listener);
			
			//Слушатель нажатия кнопки "Работа"
			ActionListener work_listener = new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					if (work.getText() == "Работа")
					{
						work.setForeground(Color.BLACK);
						work.setText("Останов.");
					}
					else
					{
						work.setForeground(Color.RED);
						work.setText("Работа");
					}
						
	        		machine.StopWork();
				}
			};
			work.addActionListener(work_listener);
		}
	});
	}
	
	private	EControlView 			control;
	private InputRegisterUI 		key_register;
	private InputRegisterUI 		inp1_register; 
	private InputRegisterUI 		inp2_register;
	private InputRegisterUI[]		input_registers;
	private JButton 				work;
	private JRadioButton[]			register_check;

}
