import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author jokmoi<br>
 * <br>
 * All'avvio dell'applicazione viene effettuato un tentativo di connessione al server tramite socket<br>
 * se esso non � stato lanciato in precedenza l'app tenta di accedere al file jar del server_DB<br>
 * il quale per� deve essere nello stesso path del progetto/app Eat_Advisor_Restaurant ovvero nella cartella Eat_Advisor_DB<br>
 * <br>
 * La classe Restaurant gestisce unicamente il collegamento al server e il reidirizzamento al frame Registration<br>
 * frame_back() e un metodo utilizzato dal frame registration per lo switch di visibilit� con il frame Restaurant
 */

public class Restaurant {
	static Restaurant window = new Restaurant();
	static Restaurant_Registration reg = new Restaurant_Registration();
	static Restaurant_Sender r_sender;
	private static Socket socket;
	private JFrame frame;
	static Frame message = new Frame();
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		

		InetAddress addr = InetAddress.getByName(null);
		try {socket = new Socket(addr, 8080);
		}catch(ConnectException  e) {
			JOptionPane.showMessageDialog(message,"Connessione al server fallita! \r\n Avvio del server tramite app.");
			//System.exit(1);
			try {
				File file = new File("EA_DB_Server.jar");
				String tmp_path = file.getAbsolutePath();
				String path = tmp_path.replaceAll("EatAdvisor_Restaurant", "EatAdvisor_DB");
				System.out.println(path);
				Desktop.getDesktop().open(new File(path));
				socket = new Socket(addr, 8080);
				JOptionPane.showMessageDialog(message,"Database avviato");
			}catch(IllegalArgumentException z) {
				JOptionPane.showMessageDialog(message,"Il database EA_DB_Server.jar deve essere presente"
						+ "\r\n nel progetto/cartella EatAdvisor_Restaurant");
				System.exit(1);
			}
			
		}
		
		
		r_sender = new Restaurant_Sender(window, reg, socket);
	}

	/**
	 * Create the application.
	 */
	public Restaurant() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//inizio btn registrazione
		JButton btnRegistrati = new JButton("Registrati");
		btnRegistrati.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				window.frame.setVisible(false);
				reg.setVisible(true);
			}
		});
		btnRegistrati.setBounds(158, 110, 116, 55);
		frame.getContentPane().add(btnRegistrati);
		//fine btn registrazione
	}
	
	public void frame_back() {
		reg.setVisible(false);
		reg.dispose();
		window.frame.setVisible(true);
		
	}
}
