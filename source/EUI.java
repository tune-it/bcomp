import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

public class EUI extends JApplet
{	
	public void init ()
	{
		EventQueue.invokeLater(new Runnable()
		{
		public void run()
		{	 
			
			EUIMicroPC MicroPC = new EUIMicroPC();
			MicroPC.addKeyListener(new KeyAdapter() {
		        public void keyReleased(KeyEvent e) 
		        {
		        	//if (e.getKeyCode() == KeyEvent.VK_DOWN)
		            // inputreg.SetPointerPosition(inputreg.GetPointerPosition()+1);
		        }
			         });
			
			
			EUIBasePC BasePC = new EUIBasePC();
			EUIOutputPC OutputPC = new EUIOutputPC();
			
			JPanel finalpanel = new JPanel();
			finalpanel.setLayout(null);
			
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Базовая ЭВМ", BasePC);
			tabbedPane.addTab("Ввод/Вывод", OutputPC);
	        tabbedPane.addTab("Работа с МК", MicroPC);
	        tabbedPane.setSize(852, 550);
	        finalpanel.add(tabbedPane);
	        
	        finalpanel.setBackground(Color.WHITE);
	        
	        JPanel radiopanel = new JPanel();
	        radiopanel.setLayout(null);
	        ButtonGroup group = new ButtonGroup();
	        JRadioButton but1 = new JRadioButton("Работа с ОП", true);
	        JRadioButton but2 = new JRadioButton("Работа с ПМК", false);
	        group.add(but1);
	        group.add(but2);
	        
	        radiopanel.add(but1);
	        radiopanel.add(but2);
	        
	        MicroPC.setFocusable(true);
	        add(finalpanel);

		}
	});
	}
}
