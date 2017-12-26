import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class SnakePanel extends JPanel implements Runnable,KeyListener,Updatable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int  SIZE = 5 ;
	private final int SIZE_ELO = 10; 
	private Directions direcao;
	private Point[] corpo ;	
	private int erro ;
	private boolean moved = false;
	private Random rd = new Random();
	private int panelWidth = 0, panelHeight = 0;
	private Point peca = null;
	private final boolean AUTO = true;
	private int pontos;
	private Updatable update;
	private int speed ;
	private int selectedDSeed;
	private boolean stop;
	private int count ;
	private Point bonusPoint ;
	private int v = 1 ;


	public void addUpdateListener(Updatable up){
		this.update = up;
	}

	public int getPontos(){
		return this.pontos;
	}

	@Override
	protected void paintComponent(Graphics g) {		
		super.paintComponent(g);

		for (int i = 0; i < corpo.length; i++) {
			Point p = corpo[i];			
			g.drawRect(p.x * SIZE_ELO, p.y * SIZE_ELO, SIZE_ELO, SIZE_ELO);			
		}

		if((peca == null) && (panelWidth > 0) && (panelHeight > 0)){
			do  {
				peca = new Point(rd.nextInt(panelWidth / SIZE_ELO), rd.nextInt(panelHeight / SIZE_ELO));
				
			} while (checkPlacePeca());
		}

		if(peca != null){
			g.setColor(Color.red);
			g.fillRect(peca.x * SIZE_ELO, peca.y * SIZE_ELO, SIZE_ELO, SIZE_ELO);
		}
		
		if(bonusPoint == null && (panelWidth > 0) && (panelHeight > 0)){
			bonusPoint = new Point(rd.nextInt(panelWidth / SIZE_ELO), rd.nextInt(panelHeight / SIZE_ELO));
		}
		
		
		if(bonusPoint != null && count >= 2 ){
			g.setColor(Color.ORANGE);			
			g.fillRect(bonusPoint.x * SIZE_ELO, bonusPoint.y * SIZE_ELO, SIZE_ELO, SIZE_ELO);				
		}
				
	}
	
	private boolean checkPlacePeca(){		
		for (Point p : corpo) {
			if(peca.equals(p)){
				System.out.println("peca: " + peca);
				System.out.println("p: " + p);
				return true;
			}
		}
		return false;		
	}

	private void doMoveP(Point p){
		int WPanel =  panelWidth / SIZE_ELO;
		int HPanel =  panelHeight / SIZE_ELO;

		switch (direcao) {
		case RIGHT:
			p.x++;
			if (p.x >= WPanel) p.x = 0; 
			break;		
		case LEFT:
			p.x--;
			if (p.x < 0) p.x = WPanel - 1;
			break;
		case UP:
			p.y--;
			if (p.y < 0) p.y = HPanel - 1;
			break;
		case DOWN:
			p.y++;
			if (p.y >= HPanel) p.y = 0;
			break;
		}
	}

	private Point getMoveP(Point ap){
		Point p = new Point(ap);
		doMoveP(p);		
		return p;
	}

	private void move(){
		panelWidth = getWidth();
		panelHeight = getHeight();	

		Point p = getMoveP(corpo[corpo.length-1]);
		
		if(bonusPoint != null && p.equals(bonusPoint)){			
			pontos = pontos + 30 ;
			bonusPoint = null ;
			count = 0 ;
			v = 1 ;

			if(this.update != null)	this.update.update(String.valueOf(pontos));
			
		}else if((peca != null) && p.equals(peca)){			
			Point[] par = new Point[corpo.length + 1];
			System.arraycopy(corpo, 0, par, 0, corpo.length);
			par[corpo.length] = p;
			corpo = par;
			peca = null;
			pontos = pontos + 10 ;
			count++;
			if(this.update != null) this.update.update(String.valueOf(pontos));
		} else {		
			for (int i = 0; i < corpo.length -1; i++) {
				corpo[i].x = corpo[i+1].x;
				corpo[i].y = corpo[i+1].y;			
			}

			p = corpo[corpo.length-1];		
			doMoveP(p);
		}

		for (int i = 0; i < corpo.length -1; i++) {
			if(corpo[i].equals(p)){
				erro = 1;
			}
		}

		repaint();
		moved = true;


	}




	@Override
	public void keyPressed(KeyEvent e) {
		if (erro != 0) return;

		if (!moved) move();
		moved = false;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if(direcao != Directions.DOWN ){
				direcao = Directions.UP;
			}

			break;		
		case KeyEvent.VK_DOWN:

			if(direcao != Directions.UP ){
				direcao = Directions.DOWN;
			}
			break;
		case KeyEvent.VK_LEFT:
			if(direcao != Directions.RIGHT ){
				direcao = Directions.LEFT;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if(direcao != Directions.LEFT ){
				direcao = Directions.RIGHT;
			}
			break;
		}		
		if (!AUTO) move();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		while (true) {
			erro = 0;
			direcao = Directions.RIGHT;
			corpo = new Point[SIZE];	
			erro = 0;
			moved = false;				
			@SuppressWarnings("unused")
			Point peca = null;		
			pontos = 0;
			update.update(String.valueOf(pontos));
			speed = 400 ;
			setSelectedDSeed(5) ;
			stop = false ;			
			v = 1 ;

			for (int i = 0; i < corpo.length; i++) {
				corpo[i] = new Point(i,0);			
			}

			while ((erro == 0)){		
				if(! stop){
					try {
						Thread.sleep(speed / selectedDSeed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (AUTO) move();
				}

			}

			int i = JOptionPane.showConfirmDialog(this, "Novo Jogo ?", "Info", JOptionPane.YES_NO_OPTION);

			if (i != JOptionPane.YES_OPTION) break;			
		}

	}

	@Override
	public void update(String st) {
		System.out.println("Pontos: " + st);

	}

	public void setSelectedDSeed(int selectedDSeed) {
		this.selectedDSeed = selectedDSeed;
	}

	public int getSelectedDSeed() {
		return selectedDSeed;
	}

	public void stop(){
		this.stop = true ;
	}

	public void resume(){
		this.stop = false ;
	}

}
