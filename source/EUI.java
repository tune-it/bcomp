import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class EUI extends JApplet
{	
	public void init ()
	{
		EventQueue.invokeLater(new Runnable()
		{
		public void run()
		{	 
			
			EUIMicroPC MicroPC = new EUIMicroPC();
			EUIBasePC BasePC = new EUIBasePC();
			EUIOutputPC OutputPC = new EUIOutputPC();
		
			JPanel finalpanel = new JPanel();
			finalpanel.setLayout(null);
			
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Базовая ЭВМ", BasePC);
			tabbedPane.addTab("Ввод/Вывод", OutputPC);
	        tabbedPane.addTab("Работа с МК", MicroPC);
	        tabbedPane.setSize(852, 460);
	        finalpanel.add(tabbedPane);
	     
	        finalpanel.setBackground(Color.WHITE);
	        
	        JPanel radiopanel = new JPanel();
	        ButtonGroup group = new ButtonGroup();
	        JRadioButton but1 = new JRadioButton("Работа с ОП", true);
	        JRadioButton but2 = new JRadioButton("Работа с ПМК", false);
	        group.add(but1);
	        group.add(but2);
	        
	        radiopanel.add(but1);
	        radiopanel.add(but2);
	        add(finalpanel);
	        add(radiopanel, BorderLayout.SOUTH);
		}
	});
	}
}
