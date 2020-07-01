
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
/**
 * 
 * @author Matteo Moi 737574 Varese<br><br>
 * <br>
 * La classe estrae i dati inseriti dall'utente e tramite create_tupla() crea una Stringa contenente tutti i dati neccesari per l'iscrizione nel DB<br>
 * <br>
 * Il pulsante Back nascode il frame attuale (Registration) e mostra nuovamente il frame Cliente<br>
 *
 */
public class Registration extends JFrame {
	static Registration frame = new Registration();
	static Cliente window = new Cliente();
	static Cliente_Sender c_sender;
	static String tupla;
	private JPanel contentPane;
	private JTextField tf_nome;
	private JTextField tf_cognome;
	private JTextField tf_email;
	private JTextField tf_comune_residenza;
	private JTextField tf_sigla_provincia_residenza;
	private JTextField tf_nickname;
	private JTextField tf_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(false);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public Registration(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.info);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//inizio btn back
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tf_nome.setText("");
				tf_cognome.setText("");
				tf_comune_residenza.setText("");
				tf_sigla_provincia_residenza.setText("");
				tf_email.setText("");
				tf_nickname.setText("");
				tf_password.setText("");

				window.frame_back_reg();
				
			}
		});
		btnBack.setBounds(335, 227, 89, 23);
		contentPane.add(btnBack);
		//fine btn back
		
		//inizio btnConferma
		JButton btnConferma = new JButton("Conferma");
		btnConferma.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					InetAddress addr = InetAddress.getByName(null);
					Socket socket = new Socket(addr, 8080);
					tupla = create_tupla();
					new  Cliente_Sender(socket,"Cliente");
					
					//System.out.println(tupla);
					
				} catch (IOException e1) {
				
					e1.printStackTrace();
				}
				
			}
		});
		btnConferma.setBounds(148, 213, 111, 37);
		contentPane.add(btnConferma);
		//fine btnConferma
		
		//inizio tf_nome
		tf_nome = new JTextField();
		tf_nome.setBounds(65, 39, 86, 20);
		contentPane.add(tf_nome);
		tf_nome.setColumns(10);
		//fine 		tf_nome
		
		//inizio tf_cognome
		tf_cognome = new JTextField();
		tf_cognome.setBounds(268, 40, 86, 20);
		contentPane.add(tf_cognome);
		tf_cognome.setColumns(10);
		//fine tf_cognome
		
		//inizio tf_email
		tf_email = new JTextField();
		tf_email.setBounds(65, 80, 86, 20);
		contentPane.add(tf_email);
		tf_email.setColumns(10);
		//fine tf_email
		
		//inizio tf_comune_residenza
		tf_comune_residenza = new JTextField();
		tf_comune_residenza.setBounds(268, 145, 86, 20);
		contentPane.add(tf_comune_residenza);
		tf_comune_residenza.setColumns(10);
		//fine tf_comune_residenza
		
		//inizio tf_sigla_provincia_residenza
		tf_sigla_provincia_residenza = new JTextField();
		tf_sigla_provincia_residenza.setBounds(268, 176, 86, 20);
		contentPane.add(tf_sigla_provincia_residenza);
		tf_sigla_provincia_residenza.setColumns(10);
		//fine tf_sigla_provincia_residenza
		
		//inizio lblEmail
		JLabel lblEmail = new JLabel("Email : ");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmail.setBounds(10, 80, 68, 17);
		contentPane.add(lblEmail);
		//fine lblEmail
		
		//inizio lblNickname
		JLabel lblNickname = new JLabel("Nickname : ");
		lblNickname.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNickname.setBounds(184, 81, 89, 19);
		contentPane.add(lblNickname);
		//fine lblNickname
		
		//inizio tf_nickname
		tf_nickname = new JTextField();
		tf_nickname.setBounds(268, 80, 86, 20);
		contentPane.add(tf_nickname);
		tf_nickname.setColumns(10);
		//fine tf_nickname
		
		//inizio lblPassword
		JLabel lblPassword = new JLabel("Password : ");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPassword.setBounds(184, 111, 89, 23);
		contentPane.add(lblPassword);
		//fine lblPassword
		
		//inizio tf_password
		tf_password = new JTextField();
		tf_password.setBounds(268, 114, 86, 20);
		contentPane.add(tf_password);
		tf_password.setColumns(10);
		//fine tf_password
		
		//inizio lblNome
		JLabel lblNome = new JLabel("Nome : ");
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNome.setBounds(10, 38, 56, 19);
		contentPane.add(lblNome);
		//fine lblNome
		
		//inizio lblCognome
		JLabel lblCognome = new JLabel("Cognome : ");
		lblCognome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCognome.setBounds(184, 39, 89, 19);
		contentPane.add(lblCognome);
		//fine lblCognome
		
		//inizio lblComuneDiResidenza
		JLabel lblComuneDiResidenza = new JLabel("Comune di residenza : ");
		lblComuneDiResidenza.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblComuneDiResidenza.setBounds(69, 146, 166, 14);
		contentPane.add(lblComuneDiResidenza);
		//fine lblComuneDiResidenza
		
		//inizio lblSiglaProvinciaDi
		JLabel lblSiglaProvinciaDi = new JLabel("Sigla provincia di residenza : ");
		lblSiglaProvinciaDi.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSiglaProvinciaDi.setBounds(69, 174, 249, 20);
		contentPane.add(lblSiglaProvinciaDi);
		//fine lblSiglaProvinciaDi
		
		//inizio lblNewLabel
		JLabel lblNewLabel = new JLabel("Registrazione utente");
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 14));
		lblNewLabel.setBounds(148, 11, 135, 17);
		contentPane.add(lblNewLabel);
		//fine lblNewLabel
		
	}
	/**
	 * vengono concatenati in un unica stringa tutti i dati inseriti dall'utente 
	 * che verranno poi trasferiti al server
	 * @return tupla
	 */
	public String create_tupla() {
		String tupla = tf_nome.getText()+" "+tf_cognome.getText()
		+" "+tf_comune_residenza.getText()+" "+tf_sigla_provincia_residenza.getText()
		+" "+tf_email.getText()+" "+tf_nickname.getText()+" "+tf_password.getText();
		System.out.println("dal metodo : "+tupla);
		return tupla;
		
	}
}