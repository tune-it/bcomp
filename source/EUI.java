import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import Machine.EMachine;
import Machine.ERegister;

public class EUI extends JApplet
{	
	public void init ()
	{
		EventQueue.invokeLater(new Runnable()
		{
		public void run()
		{	 
			final EMachine machine = new EMachine();
			EUIBasePCFactory factory = new EUIBasePCFactory(machine);
			
			EUIBasePC BasePC = new EUIBasePC(factory);
			EUIOutputPC OutputPC = new EUIOutputPC(factory);
			final EUIMicroPC MicroPC = new EUIMicroPC(factory);
			
			final JPanel finalpanel = new JPanel();
			finalpanel.setLayout(null);
			
			final JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Базовая ЭВМ", BasePC);
			tabbedPane.addTab("Ввод/Вывод", OutputPC);
	        tabbedPane.addTab("Работа с МК", MicroPC);
	        tabbedPane.setSize(852, 550);
	        finalpanel.add(tabbedPane);
	        
	        finalpanel.setBackground(Color.WHITE);
	        add(finalpanel);
	        
	        tabbedPane.addKeyListener(new KeyAdapter() {
		        public void keyPressed(KeyEvent e) 
		        {
		        	if (e.getKeyCode() == KeyEvent.VK_DOWN)
		        		{
			        		machine.Start();
			        		tabbedPane.repaint();
		        		}
		        	if (e.getKeyCode() == KeyEvent.VK_UP)
	        		{
	        		machine.Continue();
	        		tabbedPane.repaint();
	        		}

		        		}
			         });	        

		}
	});
	}
}
