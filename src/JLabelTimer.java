import javax.swing.JLabel;


public abstract class JLabelTimer extends JLabel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private int time ;
	private Thread th ;
	private boolean running = true ;
	public JLabelTimer() {
		this("");
	}
	
	public JLabelTimer(String text) {
		super(text);				
		this.text = text ;		
	}
	
	
	
	@Override
	public void run() {
		while (time > 0 || running) {
			time--;
			setText(String.valueOf(time));
			repaint();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		this.finished();
		
		
	}
	
	public void start(int time){
		if(time <= 0){
			throw new RuntimeException("Time não pode ser menor ou igual a zero");
		}
		this.time = time ;
		th = new Thread(this);
		th.start();
	}
	
	public void stop(){
		running = false;
	}
	
	public void reset(){
		setText("");
		stop();
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	protected abstract void finished();
}
