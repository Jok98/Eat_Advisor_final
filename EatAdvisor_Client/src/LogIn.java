import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import java.awt.SystemColor;

/**
 * 
 * @author Matteo Moi 737574 Varese<br><br>
 * <br>
 *La classe gestisce 4 funzioni : <br>
 *- Ricerca dei ristorante tramite nome | provincia | tipologia | tipologia e provincia<br>
 *- Mostra a schermo una lista dei risultati ottenuti dalla ricerca <br>
 *- Selezionando un nome del ristorante dalla lista e' possibile cliccando il pulsante vedi di visualizzare<br>
 *  a schermo le informazioni riguardanti quello specifico ristorante<br>
 *- se l'utente ha fatto l'accesso al frame LogIn tramite le credenziali allora avra' la possibilita'<br>
 *  (dopo aver selezionato e visualizzato le informazioni del ristorante)<br>
 *  di inserire un commento/recensione del ristorante. tramite btnInvia tale commento verra' inviato al server<br>
 *  (passando da Client_sender) al Sever che poi lo inserira' nel DB;
 *<br>
 *I metodi : <br>
 *- create_tupla() crea la stringa da inviare al server necessaria per la ricerca del ristorante<br>
 *- create_comment() crea la stringa da inviare al server necessaria per inserire il commento nella tabella Restaurant del db<br>
 */

public class LogIn {

	JFrame frame;
	
	static Cliente window = new Cliente();
	static Cliente_Sender c_sender;
	static Registration reg = new Registration();
	static String tupla;
	static Registry registry;
	static String [] values;
	static int name_index;
	static String comment;
	static ArrayList<String> restaurant_name;
	static Boolean show = true;
	private JPanel contentPane;
	static JList list_result;
	private JTextField tf_nome;
	static JComboBox cb_star;
	static JButton btnInvia;
	private JTextField tf_tipologia;
	static JComboBox comboBox = new JComboBox();
	static JTextField tf_comment;
	static JLabel lblRecensione;
	static Frame message = new Frame();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn window = new LogIn();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LogIn() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 491, 409);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.info);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNome = new JLabel("Nome : ");
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNome.setBounds(10, 24, 46, 14);
		contentPane.add(lblNome);
		
		JLabel lblProvincia = new JLabel("Provincia : ");
		lblProvincia.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblProvincia.setBounds(10, 63, 75, 14);
		contentPane.add(lblProvincia);
		
		JLabel lblTipologia = new JLabel("Tipologia : ");
		lblTipologia.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTipologia.setBounds(10, 107, 75, 14);
		contentPane.add(lblTipologia);
		
		tf_nome = new JTextField();
		tf_nome.setBounds(95, 22, 121, 20);
		contentPane.add(tf_nome);
		tf_nome.setColumns(10);

		//inizio jtext info_restaurant
		JTextPane info_restaurant = new JTextPane();
		info_restaurant.setEditable(false);
		info_restaurant.setBounds(44, 243, 206, 124);
		contentPane.add(info_restaurant);
		//fine jtext info_restaurant
		
		//inzio lista
				JList list_result = new JList();
				list_result.setModel(new AbstractListModel() {
					String[] values = new String[] {"Lista ristoranti"};
					public int getSize() {
						return values.length;
					}
					public Object getElementAt(int index) {
						return values[index];
					}
				});
				list_result.setSelectedIndex(-1);
				list_result.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list_result.setVisibleRowCount(15);
				list_result.setToolTipText("");
				list_result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
				list_result.setBounds(285, 37, 180, 111);
				contentPane.add(list_result);
				
				JScrollPane scroll = new JScrollPane (list_result);
				scroll.setBounds(285, 37, 180, 111);
				contentPane.add(scroll);
				
				//fine  lista
		
				
		//inizio btn cerca
		JButton btnCerca = new JButton("Cerca");
		btnCerca.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for(int j = 0; j < 2; j++) {
				try {
					InetAddress addr = InetAddress.getByName(null);
					Socket socket = new Socket(addr, 8080);
					tupla = create_tupla();
					new  Cliente_Sender(socket,"Cliente_search");
					registry = LocateRegistry.getRegistry(1099);
					//while(Cliente_Sender.continue_==false) {Thread.sleep(100);}
					//list_result.removeAll();
					
					Server_Client_Int restaurant =(Server_Client_Int) registry.lookup("Ristorante");
					restaurant_name = restaurant.get_restaurant();
					values = new String[restaurant_name.size() / 7];
					
					int position;
					for (int i = 0; i < restaurant_name.size() / 7; i++) {
						position = i * 7;
						values[i] = restaurant_name.get(position);
						
					}

						list_result.setModel(new AbstractListModel() {

							public int getSize() {
								return values.length;
							}

							public Object getElementAt(int index) {
								return values[index];
							}
						});
					
						info_restaurant.setText("");
						Thread.sleep(100);
					
				} catch (IOException | NotBoundException | InterruptedException e1) {
					System.err.println("Errore ricerca/comunicazione con db");
					e1.printStackTrace();
				}
				
				}
				
				System.out.println("Ricevuto dati tramite RMI ");
				System.out.println("Size lista : " + restaurant_name.size());
				
				if(restaurant_name.isEmpty()) {JOptionPane.showMessageDialog(message,"Nessun risultato trovato");
				}else JOptionPane.showMessageDialog(message,"Ricerca effettuata");
				
			}
			
		});
		btnCerca.setBounds(108, 159, 89, 23);
		contentPane.add(btnCerca);
		//fine btn cerca
		
		tf_tipologia = new JTextField();
		tf_tipologia.setBounds(95, 105, 121, 20);
		contentPane.add(tf_tipologia);
		tf_tipologia.setColumns(10);
		
		
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
		comboBox.setBounds(95, 62, 121, 18);
		contentPane.add(comboBox);
		
		//inizio btn back
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				info_restaurant.setText("");
				tf_nome.setText("");
				tf_tipologia.setText("");
				JList list_result = new JList();
				window.frame_back_log();
				frame.setVisible(false);
				//contentPane.setVisible(false);
				
				
					
			}
		});
		btnBack.setBounds(0, 159, 89, 23);
		contentPane.add(btnBack);
		
		JLabel lblRisultatiRicerca = new JLabel("Risultati ricerca : ");
		lblRisultatiRicerca.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRisultatiRicerca.setBounds(292, 0, 132, 25);
		contentPane.add(lblRisultatiRicerca);
		
		
		
		
		JLabel label = new JLabel("-----------------------------------------------------------------------");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(0, 193, 434, 14);
		contentPane.add(label);
		
		JLabel lblInfoRistorante = new JLabel("Info ristorante : ");
		lblInfoRistorante.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInfoRistorante.setBounds(44, 218, 115, 14);
		contentPane.add(lblInfoRistorante);
		
		tf_comment = new JTextField();
		tf_comment.setBounds(285, 271, 180, 71);
		contentPane.add(tf_comment);
		tf_comment.setColumns(10);
		

		
		JScrollPane scroll_info = new JScrollPane (info_restaurant);
		scroll_info.setBounds(44, 243, 206, 124);
		contentPane.add(scroll_info);
		
		//inizio btn vedi
		JButton btnVedi = new JButton("Vedi");
		btnVedi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				name_index = list_result.getSelectedIndex();
				if (name_index!=0)name_index = name_index*7;
				//System.out.println("Indice INFO : " + name_index);
				info_restaurant.setText("Nome : "+restaurant_name.get(name_index)+"\r\n"+"Provincia : "+
				restaurant_name.get(name_index+1)+"\r\n"+"Indirizzo : " + restaurant_name.get(name_index+3)
				+"\r\n"+"Tipologia : " + restaurant_name.get(name_index+2)+"\r\n"+"Cell : " + restaurant_name.get(name_index+4)
				+"\r\n"+"Sito web : " + restaurant_name.get(name_index+5)+"\r\n"+"Commento : " + restaurant_name.get(name_index+6));
				
				;
			}
		});
		btnVedi.setBounds(312, 159, 132, 23);
		contentPane.add(btnVedi);
		//fine btn vedi
		
		lblRecensione = new JLabel("Recensione :");
		lblRecensione.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRecensione.setBounds(285, 218, 122, 14);
		contentPane.add(lblRecensione);
		
		
		//inizio btnInvia
		btnInvia = new JButton("Invia");
		
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tmp = tf_comment.getText();
				if(tmp.length()<257) {
			
				try {
					InetAddress addr = InetAddress.getByName(null);
					Socket socket = new Socket(addr, 8080);
					new Cliente_Sender(socket, "Client_comment");
					
				} catch (IOException e) {
				
					e.printStackTrace();
				}
			}else {JOptionPane.showMessageDialog(message,"Testo superiore ai 256 caratteri! \r\n Diminuirne la dimensione!");}
			
			}});
		btnInvia.setBounds(285, 342, 180, 25);
		contentPane.add(btnInvia);
		
		cb_star = new JComboBox();
		cb_star.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		cb_star.setBounds(327, 238, 62, 22);
		contentPane.add(cb_star);
		
		JLabel lblNewLabel = new JLabel("Stelle :");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(285, 243, 46, 14);
		contentPane.add(lblNewLabel);
		//fine invia
		
	}
	
	public String create_tupla() {
		String nome = (tf_nome.getText().equals("")) ? "null":tf_nome.getText();
		String tmp = (String) comboBox.getSelectedItem();
		String provincia = (comboBox.getSelectedItem().equals("")) ? "null":tmp;
		
		String tipologia = (tf_tipologia.getText().equals("")) ? "null":tf_tipologia.getText() ;
		
		String tupla = nome+" "+provincia+" "+tipologia;
		
		System.out.println("dal metodo : "+tupla);
		return tupla;
		
	}
	
	public String create_comment() {
		String tmp_star = (String) cb_star.getSelectedItem();
		//String star = (cb_star.getSelectedItem().equals("")) ? "null":tmp_star;
		String tmp = tf_comment.getText();
		String restaurant = restaurant_name.get(name_index);
		String id = Cliente.id_tmp;
		comment = restaurant+"--"+tmp+"--"+tmp_star+"--"+id;
		tf_comment.setText("");
		return comment;
	}
	}