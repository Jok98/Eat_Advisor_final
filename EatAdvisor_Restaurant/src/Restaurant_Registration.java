import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;

/**
 * 
 * @author jokmoi
 * La clesse permette tramite GUI all'utente di inserire i dati necessari per iscrivere/inserire nel db il ristorante<br>
 * con la conferma dell'invio dei dati la classe crea un stringa contenente tutti i dati e che tramite Restaurant_Sender verr� inviata al server e poi al db per l'iscrizione<br>
 */
public class Restaurant_Registration extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Restaurant_Registration frame = new Restaurant_Registration();
	static Restaurant window = new Restaurant();
	static JComboBox comboBox;
	
	private JPanel contentPane;
	private JTextField tf_nome;
	private JTextField tf_indirizzo;
	private JTextField tf_cell;
	private JTextField tf_tipologia;
	private JTextField tf_sito;
	
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
	@SuppressWarnings("unchecked")
	public Restaurant_Registration() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//inizio btn back
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				window.frame_back();
				
			}
		});
		btnBack.setBounds(335, 227, 89, 23);
		contentPane.add(btnBack);
		//fine btn back
		
		JButton btnConferma = new JButton("Conferma");
		btnConferma.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					String tupla = create_tupla();
					//System.out.println(tupla);
					window.r_sender.send_data(tupla);
					
				} catch (IOException e1) {
				
					e1.printStackTrace();
				}
				
			}
		});
		btnConferma.setBounds(148, 213, 111, 37);
		contentPane.add(btnConferma);
		
		tf_nome = new JTextField();
		tf_nome.setBounds(94, 13, 86, 20);
		contentPane.add(tf_nome);
		tf_nome.setColumns(10);
		
		tf_indirizzo = new JTextField();
		tf_indirizzo.setBounds(313, 54, 86, 20);
		contentPane.add(tf_indirizzo);
		tf_indirizzo.setColumns(10);
		
		tf_cell = new JTextField();
		tf_cell.setBounds(313, 13, 86, 20);
		contentPane.add(tf_cell);
		tf_cell.setColumns(10);
		
		tf_tipologia = new JTextField();
		tf_tipologia.setBounds(313, 99, 86, 20);
		contentPane.add(tf_tipologia);
		tf_tipologia.setColumns(10);
		
		JLabel lblcell = new JLabel("Cell : ");
		lblcell.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblcell.setBounds(258, 13, 68, 17);
		contentPane.add(lblcell);
		
		JLabel lblsito = new JLabel("Sito web : ");
		lblsito.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblsito.setBounds(10, 100, 89, 19);
		contentPane.add(lblsito);
		
		tf_sito = new JTextField();
		tf_sito.setBounds(94, 99, 86, 20);
		contentPane.add(tf_sito);
		tf_sito.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome : ");
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNome.setBounds(10, 12, 56, 19);
		contentPane.add(lblNome);
		
		JLabel lblIndirizzo = new JLabel("Indirizzo : ");
		lblIndirizzo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIndirizzo.setBounds(229, 53, 89, 19);
		contentPane.add(lblIndirizzo);
		
		JLabel lblTipologia = new JLabel("Tipologia : ");
		lblTipologia.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTipologia.setBounds(229, 98, 80, 19);
		contentPane.add(lblTipologia);
		
		JLabel lblComune = new JLabel("Comune : ");
		lblComune.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblComune.setBounds(10, 57, 74, 14);
		contentPane.add(lblComune);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"","Agrigento", "Alessandria", "Ancona", "Aosta", "Arezzo", "Ascoli Piceno", "Asti",
				"Avellino", "Bari", "Barletta-Andria-Trani", "Belluno", "Benevento", "Bergamo", "Biella", "Bologna", "Bolzano", "Brescia", "Brindisi",
				"Cagliari", "Caltanissetta", "Campobasso", "Carbonia-Iglesias", "Caserta", "Catania", "Catanzaro", "Chieti", "Como", "Cosenza", "Cremona",
				"Crotone", "Cuneo", "Enna", "Fermo", "Ferrara", "Firenze", "Foggia", "Forl\u00EC-Cesena", "Frosinone", "Genova", "Gorizia", "Grosseto", "Imperia",
				"Isernia", "La Spezia", "L'Aquila", "Latina", "Lecce", "Lecco", "Livorno", "Lodi", "Lucca", "Macerata", "Mantova", "Massa-Carrara", "Matera", "Messina",
				"Milano", "Modena", "Monza e della Brianza", "Napoli", "Novara", "Nuoro", "Olbia-Tempio", "Oristano", "Padova", "Palermo", "Parma", "Pavia", "Perugia",
				"Pesaro e Urbino", "Pescara", "Piacenza", "Pisa", "Pistoia", "Pordenone", "Potenza", "Prato", "Ragusa", "Ravenna", "Reggio Calabria", "Reggio Emilia",
				"Rieti", "Rimini", "Roma", "Rovigo", "Salerno", "Medio Campidano", "Sassari", "Savona", "Siena", "Siracusa", "Sondrio", "Taranto", "Teramo", "Terni",
				"Torino", "Ogliastra", "Trapani", "Trento", "Treviso", "Trieste", "Udine", "Varese", "Venezia", "Verbano-Cusio-Ossola", "Vercelli", "Verona",
				"Vibo Valentia", "Vicenza", "Viterbo"}));
		comboBox.setToolTipText("");
		comboBox.setBounds(94, 53, 86, 22);
		contentPane.add(comboBox);
		
	}
	/**
	 * vengono concatenati in un unica stringa tutti i dati inseriti dall'utente 
	 * che verranno poi trasferiti al server
	 * @return tupla
	 */
	public String create_tupla() {
		String tmp = (String) comboBox.getSelectedItem();
		String tupla = tf_nome.getText()+" "+tmp+" "+tf_tipologia.getText()+" "+tf_indirizzo.getText()+" "+tf_cell.getText()+" "+tf_sito.getText();
		//System.out.println("dal metodo : "+tupla);
		return tupla;
		
	}
}
