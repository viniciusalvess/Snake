import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame implements Updatable {

	private static final long serialVersionUID = 1L;	
	private JLabel lblPontos;
	private SnakePanel sp;
	private JLabelTimer tm;
	private JPanel panel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainWindow();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {

		setBounds(100, 100, 500, 500);

		sp = new SnakePanel();
		sp.addUpdateListener(this);

		getContentPane().add(sp,BorderLayout.CENTER);

		addKeyListener(sp);

		panel = new JPanel();
		panel.setBorder(new LineBorder(Color.CYAN));
		FlowLayout fl = new FlowLayout();
		fl.setHgap(10);
		fl.setAlignment(FlowLayout.LEFT);
		final JCheckBox bt = new JCheckBox("Pause");
		bt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(! bt.isSelected()){
					sp.resume();						
				}else{
					sp.stop();
				}

			}
		});

		bt.addKeyListener(sp);
		panel.setLayout(fl);

		

		
		
		lblPontos = new JLabel();
		panel.add(lblPontos);		
		panel.add(bt);
		
		tm = new JLabelTimer() {
			
			@Override
			protected void finished() {
				setVisible(false);
//				System.out.println("update MainWindow");
				
			}
		};
		
		this.panel.add(tm);


		getContentPane().add(panel, BorderLayout.SOUTH);
		Thread thread = new Thread(sp);
		thread.start();

		setJMenuBar(buildMenu());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void update(String st) {
		this.lblPontos.setText("Pontos: " + st);		
	}

	private JMenuBar buildMenu(){
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Level");		
		JMenuItem it0 ;

		for (int i = 0; i < 10; i++) {
			it0 = new JMenuItem("Speed " + (i+1));
			it0.setName(String.valueOf(i));
			it0.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {					
					sp.setSelectedDSeed(Integer.parseInt(((JMenuItem)e.getSource()).getName()) + 1);
				}
			});
			menu.add(it0);
		}

		bar.add(menu);
		setTitle("GV - Snake");

		return bar;


	}


}
