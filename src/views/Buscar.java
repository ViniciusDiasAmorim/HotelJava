package views;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.stream.events.StartElement;

import dao.HospedeDAO;
import dao.ReservaDAO;
import factory.ConnectionFactory;
import modelo.Hospede;
import modelo.Reserva;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.management.RuntimeErrorException;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTabbedPane;
import java.awt.Toolkit;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class Buscar extends JFrame {

	private JPanel contentPane;
	private JTextField txtBuscar;
	private JTable tbHospedes;
	private JTable tbReservas;
	private DefaultTableModel modelo;
	private DefaultTableModel modeloHospedes;
	private JLabel labelAtras;
	private JLabel labelExit;
	int xMouse, yMouse;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Buscar frame = new Buscar();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Buscar() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 571);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);

		txtBuscar = new JTextField();
		txtBuscar.setBounds(536, 127, 193, 31);
		txtBuscar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		contentPane.add(txtBuscar);
		txtBuscar.setColumns(10);

		JLabel lblTitulo = new JLabel("SISTEMA DE BUSCA");
		lblTitulo.setForeground(new Color(12, 138, 199));
		lblTitulo.setFont(new Font("Roboto Black", Font.BOLD, 24));
		lblTitulo.setBounds(331, 62, 280, 42);
		contentPane.add(lblTitulo);

		JTabbedPane panel = new JTabbedPane(JTabbedPane.TOP);
		panel.setBackground(new Color(12, 138, 199));
		panel.setFont(new Font("Roboto", Font.PLAIN, 16));
		panel.setBounds(20, 169, 865, 328);
		contentPane.add(panel);

		tbHospedes = new JTable();
		tbHospedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbHospedes.setFont(new Font("Roboto", Font.PLAIN, 16));
		panel.addTab("Hóspedes", new ImageIcon(Buscar.class.getResource("/imagens/pessoas.png")), tbHospedes, null);
		modeloHospedes = (DefaultTableModel) tbHospedes.getModel();
		modeloHospedes.addColumn("Numero de Hóspede");
		modeloHospedes.addColumn("Nome");
		modeloHospedes.addColumn("Sobrenome");
		modeloHospedes.addColumn("Data de Nascimento");
		modeloHospedes.addColumn("Nacionalidade");
		modeloHospedes.addColumn("Telefone");
		modeloHospedes.addColumn("Numero de Reserva");
		String[] colunaHospede = { "Numero de Hóspede", "Nome", "Sobrenome", "Data de Nascimento", "Nacionalidade",
				"Telefone", "Numero de Reserva" };
		modeloHospedes.addRow(colunaHospede);

		tbReservas = new JTable();
		tbReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbReservas.setFont(new Font("Roboto", Font.PLAIN, 16));
		panel.addTab("Reservas", new ImageIcon(Buscar.class.getResource("/imagens/reservado.png")), tbReservas, null);
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tbHospedes.clearSelection();
				tbReservas.clearSelection();
			}
		});
		modelo = (DefaultTableModel) tbReservas.getModel();
		modelo.addColumn("Numero de Reserva");
		modelo.addColumn("Data Check In");
		modelo.addColumn("Data Check Out");
		modelo.addColumn("Valor");
		modelo.addColumn("Forma de Pago");
		String[] colunaReserva = { "Numero de Reserva", "Data Check In", "Data Check Out", "Valor", "Forma de Pago" };
		modelo.addRow(colunaReserva);
		try (Connection connection = new ConnectionFactory().stringConexao()) {
			ReservaDAO reserva = new ReservaDAO(connection);
			List<Reserva> reservas = reserva.listaReservas();

			HospedeDAO hospede = new HospedeDAO(connection);
			List<Hospede> hospedes = hospede.listarHospede();

			for (Reserva r : reservas) {
				Object[] objetos = new Object[5];
				objetos[0] = r.getId();
				objetos[1] = r.getDataEntrada();
				objetos[2] = r.getDataSaida();
				objetos[3] = r.getValor();
				objetos[4] = r.getFormaPagamento();

				modelo.addRow(objetos);
			}

			for (Hospede h : hospedes) {
				Object[] objetos = new Object[7];
				objetos[0] = h.getId();
				objetos[1] = h.getNome();
				objetos[2] = h.getSobrenome();
				objetos[3] = h.getDataNascimento();
				objetos[4] = h.getNacionalidade();
				objetos[5] = h.getTelefone();
				objetos[6] = h.getIdReserva();
				modeloHospedes.addRow(objetos);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		JPanel header = new JPanel();
		header.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				headerMouseDragged(e);

			}
		});
		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				headerMousePressed(e);
			}
		});
		header.setLayout(null);
		header.setBackground(Color.WHITE);
		header.setBounds(0, 0, 910, 36);
		contentPane.add(header);

		JPanel btnAtras = new JPanel();
		btnAtras.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MenuUsuario usuario = new MenuUsuario();
				usuario.setVisible(true);
				dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				btnAtras.setBackground(new Color(12, 138, 199));
				labelAtras.setForeground(Color.white);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnAtras.setBackground(Color.white);
				labelAtras.setForeground(Color.black);
			}
		});
		btnAtras.setLayout(null);
		btnAtras.setBackground(Color.WHITE);
		btnAtras.setBounds(0, 0, 53, 36);
		header.add(btnAtras);

		labelAtras = new JLabel("<");
		labelAtras.setHorizontalAlignment(SwingConstants.CENTER);
		labelAtras.setFont(new Font("Roboto", Font.PLAIN, 23));
		labelAtras.setBounds(0, 0, 53, 36);
		btnAtras.add(labelAtras);

		JPanel btnexit = new JPanel();
		btnexit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MenuUsuario usuario = new MenuUsuario();
				usuario.setVisible(true);
				dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) { // Quando o usuário passa o mouse sobre o botão, ele muda de cor
				btnexit.setBackground(Color.red);
				labelExit.setForeground(Color.white);
			}

			@Override
			public void mouseExited(MouseEvent e) { // Quando o usuário remove o mouse do botão, ele retornará ao estado
													// original
				btnexit.setBackground(Color.white);
				labelExit.setForeground(Color.black);
			}
		});
		btnexit.setLayout(null);
		btnexit.setBackground(Color.WHITE);
		btnexit.setBounds(857, 0, 53, 36);
		header.add(btnexit);

		labelExit = new JLabel("X");
		labelExit.setHorizontalAlignment(SwingConstants.CENTER);
		labelExit.setForeground(Color.BLACK);
		labelExit.setFont(new Font("Roboto", Font.PLAIN, 18));
		labelExit.setBounds(0, 0, 53, 36);
		btnexit.add(labelExit);

		JSeparator separator_1_2 = new JSeparator();
		separator_1_2.setForeground(new Color(12, 138, 199));
		separator_1_2.setBackground(new Color(12, 138, 199));
		separator_1_2.setBounds(539, 159, 193, 2);
		contentPane.add(separator_1_2);

		JPanel btnbuscar = new JPanel();
		btnbuscar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				while (modeloHospedes.getRowCount() > 1) {
					modeloHospedes.removeRow(1);
				}
				try (Connection connection = new ConnectionFactory().stringConexao()) {
					HospedeDAO hospede = new HospedeDAO(connection);
					List<Hospede> hospedes = hospede.filtrar(txtBuscar.getText());

					for (Hospede h : hospedes) {
						Object[] objetos = new Object[7];
						objetos[0] = h.getId();
						objetos[1] = h.getNome();
						objetos[2] = h.getSobrenome();
						objetos[3] = h.getDataNascimento();
						objetos[4] = h.getNacionalidade();
						objetos[5] = h.getTelefone();
						objetos[6] = h.getIdReserva();

						modeloHospedes.addRow(objetos);
					}

				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		btnbuscar.setLayout(null);
		btnbuscar.setBackground(new Color(12, 138, 199));
		btnbuscar.setBounds(748, 125, 122, 35);
		btnbuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnbuscar);

		JLabel lblBuscar = new JLabel("BUSCAR");
		lblBuscar.setBounds(0, 0, 122, 35);
		btnbuscar.add(lblBuscar);
		lblBuscar.setHorizontalAlignment(SwingConstants.CENTER);
		lblBuscar.setForeground(Color.WHITE);
		lblBuscar.setFont(new Font("Roboto", Font.PLAIN, 18));

		JPanel btnEditar = new JPanel();
		btnEditar.setLayout(null);
		btnEditar.setBackground(new Color(12, 138, 199));
		btnEditar.setBounds(635, 508, 122, 35);
		btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnEditar);
		btnEditar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selecionaLinha = tbHospedes.getSelectedRow();
				String[] objetosColuna = new String[7];
				if (selecionaLinha != -1) {
					for (int i = 0; i < tbHospedes.getColumnCount(); i++) {
						objetosColuna[i] = modeloHospedes.getValueAt(selecionaLinha, i).toString();
					}

					int confirmacao = JOptionPane.showConfirmDialog(null, "Realmente deseja Editar o campo?");

					if (confirmacao == 0) {
						try (Connection connection = new ConnectionFactory().stringConexao()) {
							HospedeDAO hospede = new HospedeDAO(connection);
							hospede.editar(Integer.parseInt(objetosColuna[0]), objetosColuna[1], objetosColuna[2],
									objetosColuna[3], objetosColuna[4], objetosColuna[5],
									Integer.parseInt(objetosColuna[6]));
							Buscar buscar = new Buscar();
							buscar.setVisible(true);
							dispose();

						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					} else {
						Buscar buscar = new Buscar();
						buscar.setVisible(true);
						dispose();
					}
				}
				int selecionaLinhaReserva = tbReservas.getSelectedRow();
				String[] valorDaColunaReserva = new String[5];
				if (selecionaLinhaReserva != -1) {
					for (int i = 0; i < tbReservas.getColumnCount(); i++) {
						valorDaColunaReserva[i] = tbReservas.getValueAt(selecionaLinhaReserva, i).toString();
					}

					int confirmacaoReserva = JOptionPane.showConfirmDialog(null, "Realmente deseja Editar o campo?");
					if (confirmacaoReserva == 0) {
						try (Connection connection = new ConnectionFactory().stringConexao()) {
							ReservaDAO reserva = new ReservaDAO(connection);
							reserva.editar(Integer.parseInt(valorDaColunaReserva[0]), valorDaColunaReserva[1],
									valorDaColunaReserva[2], valorDaColunaReserva[3]);
							Buscar buscar = new Buscar();
							buscar.setVisible(true);
							dispose();
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					} else {
						Buscar buscar = new Buscar();
						buscar.setVisible(true);
						dispose();
					}
				}
			}

		});

		JLabel lblEditar = new JLabel("EDITAR");
		lblEditar.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditar.setForeground(Color.WHITE);
		lblEditar.setFont(new Font("Roboto", Font.PLAIN, 18));
		lblEditar.setBounds(0, 0, 122, 35);
		btnEditar.add(lblEditar);

		JPanel btnDeletar = new JPanel();
		btnDeletar.setLayout(null);
		btnDeletar.setBackground(new Color(12, 138, 199));
		btnDeletar.setBounds(767, 508, 122, 35);
		btnDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnDeletar);
		btnDeletar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selecionaLinhaHospede = tbHospedes.getSelectedRow();

				if (selecionaLinhaHospede != -1 && selecionaLinhaHospede != 0) {
					int idHospede = Integer.parseInt(tbHospedes.getValueAt(selecionaLinhaHospede, 0).toString());

					int confirmacaoHospede = JOptionPane.showConfirmDialog(null, "Realmente deseja Excluir o campo?");
					if (confirmacaoHospede == 0) {
						try (Connection connection = new ConnectionFactory().stringConexao()) {

							HospedeDAO hospede = new HospedeDAO(connection);
							hospede.deletar(idHospede);
							Buscar buscar = new Buscar();
							buscar.setVisible(true);
							dispose();

						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					}
				}
				int selecionaLinhaReserva = tbReservas.getSelectedRow();

				if (selecionaLinhaReserva != -1 && selecionaLinhaReserva != 0) {
					int idReserva = Integer.parseInt(tbReservas.getValueAt(selecionaLinhaReserva, 0).toString());
					int confirmacaoReserva = JOptionPane.showConfirmDialog(null, "Realmente deseja Excluir o campo?");
					if (confirmacaoReserva == 0) {
						try (Connection connection = new ConnectionFactory().stringConexao()) {
							ReservaDAO reserva = new ReservaDAO(connection);
							reserva.deletar(idReserva);
							Buscar buscar = new Buscar();
							buscar.setVisible(true);
							dispose();

						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					}
				}

			}
		});

		JLabel lblExcluir = new JLabel("DELETAR");
		lblExcluir.setHorizontalAlignment(SwingConstants.CENTER);
		lblExcluir.setForeground(Color.WHITE);
		lblExcluir.setFont(new Font("Roboto", Font.PLAIN, 18));
		lblExcluir.setBounds(0, 0, 122, 35);
		btnDeletar.add(lblExcluir);
		setResizable(false);

	}

	// Código que permite movimentar a janela pela tela seguindo a posição de "x" e
	// "y"
	private void headerMousePressed(java.awt.event.MouseEvent evt) {
		xMouse = evt.getX();
		yMouse = evt.getY();
	}

	private void headerMouseDragged(java.awt.event.MouseEvent evt) {
		int x = evt.getXOnScreen();
		int y = evt.getYOnScreen();
		this.setLocation(x - xMouse, y - yMouse);
	}

}
