import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JPasswordField;
/**
 * 
 * @author Matteo Moi 737574 Varese<br>
 * <br>
 * La classe Cliente gestisce il reindirizzamento ai frame :<br>
 * -Registration(che gestisce la registrazione dell'utente)<br>
 * -LogIn(tramite login o accesso libero)<br>
 * <br>
 * Nello specifico e' la classe Cliente_Sender che si occupa del reindirizzamento<br>
 * All'avvio dell'applicazione viene effettuato un tentativo di connessione al server tramite socket<br>
 * se esso non e' stato lanciato in precedenza l'app tenta di accedere al file jar del server_DB<br>
 * il quale pero' deve essere nello stesso path del progetto/app Eat_Advisor_Client ovvero nella cartella Eat_Advisor_DB<br>
 * <br>
 * Per ogni azione fatta dall'utente la classe Cliente genera un nuovo processo Thread Cliente_Sender che gestisce la richiesta/azione specifica<br>
 * <br>
 * Sono presenti due metodi frame_back() che gestioscono la funzione "Back" quando l'utente e' sul frame Registration o LogIn<br>
 */
public class Cliente {

	JFrame frame;
	private JTextField tf_ID;
	static Cliente window;
	static Registration reg = new Registration();
	static LogIn login;
	static String data;
	static String id_tmp;
	static Socket socket;
	static Frame message = new Frame();
	private JPasswordField tf_password;
	/**
	 * 
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Cliente();
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
				String path = tmp_path.replaceAll("EatAdvisor_Client", "EatAdvisor_DB");
				System.out.println(path);
				Desktop.getDesktop().open(new File(path));
				socket = new Socket(addr, 8080);
				JOptionPane.showMessageDialog(message,"Database avviato");
			}catch(IllegalArgumentException z) {
				JOptionPane.showMessageDialog(message,"Il database EA_DB_Server.jar deve essere presente"
						+ "\r\n nel progetto/cartella EatAdvisor_DB");
				System.exit(1);
			}
		}
	}

	/**
	 * Create the application.
	 */
	public Cliente() {
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
		
		JButton btnAccedi = new JButton("Accedi");
		btnAccedi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					InetAddress addr = InetAddress.getByName(null);
					socket = new Socket(addr, 8080);
					id_tmp = tf_ID.getText();
					data = tf_ID.getText()+" "+tf_password.getText();
					//System.out.println(data);
					frame.setVisible(false);
					new Cliente_Sender(socket,"Cliente_accesso");
					
					tf_ID.setText("");
					tf_password.setText("");
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
	
			}
		});
		btnAccedi.setBounds(165, 143, 89, 23);
		frame.getContentPane().add(btnAccedi);
		
		JButton btnRegistrati = new JButton("Registrati");
		btnRegistrati.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				reg.setVisible(true);
				
			}
		});
		btnRegistrati.setBounds(10, 208, 89, 23);
		frame.getContentPane().add(btnRegistrati);
		
		//
		JButton btnNoLogIn = new JButton("Versione senza login");
		btnNoLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				try {
					InetAddress addr = InetAddress.getByName(null);
					socket = new Socket(addr, 8080);
					new Cliente_Sender(socket,"Cliente_accesso_free");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				tf_ID.setText("");
				tf_password.setText("");
			}
		});
		btnNoLogIn.setBounds(295, 227, 129, 23);
		frame.getContentPane().add(btnNoLogIn);
		
		tf_ID = new JTextField();
		tf_ID.setBounds(73, 41, 86, 20);
		frame.getContentPane().add(tf_ID);
		tf_ID.setColumns(10);
		
		JLabel lblID = new JLabel("ID : ");
		lblID.setBounds(32, 44, 35, 14);
		frame.getContentPane().add(lblID);
		
		JLabel lblPassword = new JLabel("Password : ");
		lblPassword.setBounds(191, 44, 71, 14);
		frame.getContentPane().add(lblPassword);
		
		tf_password = new JPasswordField();
		tf_password.setBounds(264, 41, 86, 20);
		frame.getContentPane().add(tf_password);
	}
	
	public void frame_back_log() {
		frame.setVisible(true);
		
		
	}
	
	public void frame_back_reg() {
		frame.setVisible(true);
		reg.setVisible(false);
		//login.setVisible(false);
		
	}
}