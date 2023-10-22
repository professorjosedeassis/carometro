/*
 * The MIT License
 *
 * Copyright 2023 Professor José de Assis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import utils.Validador;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.Desktop;

public class Carometro extends JFrame {

	DAO dao = new DAO();
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;

	private FileInputStream fis;
	private int tamanho;
	private boolean fotoCarregada = false;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblStatus;
	private JLabel lblData;
	private JTextField txtRA;
	private JTextField txtNome;
	private JLabel lblFoto;
	private JButton btnCarregar;
	private JButton btnAdicionar;
	private JScrollPane scrollPaneLista;
	private JList<String> listNomes;
	private JButton btnExcluir;
	private JButton btnBuscar;
	private JButton btnEditar;
	private JButton btnSobre;
	private JButton btnPdf;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Carometro frame = new Carometro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Carometro() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				status();
				setarData();
			}
		});
		setTitle("Carômetro");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Carometro.class.getResource("/img/instagram.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPaneLista = new JScrollPane();
		scrollPaneLista.setBorder(null);
		scrollPaneLista.setVisible(false);
		scrollPaneLista.setBounds(76, 100, 201, 75);
		contentPane.add(scrollPaneLista);

		listNomes = new JList<String>();
		listNomes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				buscarNome();
			}
		});
		listNomes.setBorder(null);
		scrollPaneLista.setViewportView(listNomes);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(0, 272, 624, 49);
		contentPane.add(panel);
		panel.setLayout(null);

		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dboff.png")));
		lblStatus.setBounds(566, 11, 32, 32);
		panel.add(lblStatus);

		lblData = new JLabel("");
		lblData.setForeground(SystemColor.text);
		lblData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblData.setBounds(30, 11, 390, 27);
		panel.add(lblData);

		JLabel lblNewLabel = new JLabel("RA");
		lblNewLabel.setBounds(20, 42, 46, 14);
		contentPane.add(lblNewLabel);

		txtRA = new JTextField();
		txtRA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0123456789";
				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}
		});
		txtRA.setBounds(54, 39, 64, 20);
		contentPane.add(txtRA);
		txtRA.setColumns(10);
		txtRA.setDocument(new Validador(6));

		JLabel lblNewLabel_1 = new JLabel("Nome");
		lblNewLabel_1.setBounds(20, 84, 46, 14);
		contentPane.add(lblNewLabel_1);

		txtNome = new JTextField();
		txtNome.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtNome.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				listarNomes();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					scrollPaneLista.setVisible(false);
					int confirma = JOptionPane.showConfirmDialog(null,
							"Aluno não cadastrado.\nDeseja cadastrar este aluno?", "Aviso", JOptionPane.YES_OPTION);
					if (confirma == JOptionPane.YES_OPTION) {
						txtRA.setEditable(false);
						btnBuscar.setEnabled(false);
						btnCarregar.setEnabled(true);
						btnAdicionar.setEnabled(true);
						btnPdf.setEnabled(false);
					} else {
						reset();
					}
				}
			}
		});
		txtNome.setBounds(76, 81, 201, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		txtNome.setDocument(new Validador(30));

		lblFoto = new JLabel("");
		lblFoto.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblFoto.setIcon(new ImageIcon(Carometro.class.getResource("/img/camera.png")));
		lblFoto.setBounds(347, 8, 256, 256);
		contentPane.add(lblFoto);

		btnCarregar = new JButton("Carregar foto");
		btnCarregar.setEnabled(false);
		btnCarregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarFoto();
			}
		});
		btnCarregar.setForeground(SystemColor.textHighlight);
		btnCarregar.setBounds(20, 127, 112, 23);
		contentPane.add(btnCarregar);

		btnAdicionar = new JButton("");
		btnAdicionar.setEnabled(false);
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		btnAdicionar.setToolTipText("Adicionar");
		btnAdicionar.setIcon(new ImageIcon(Carometro.class.getResource("/img/create.png")));
		btnAdicionar.setBounds(20, 190, 64, 64);
		contentPane.add(btnAdicionar);

		JButton btnReset = new JButton("");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		btnReset.setIcon(new ImageIcon(Carometro.class.getResource("/img/eraser.png")));
		btnReset.setToolTipText("Limpar campos");
		btnReset.setBounds(262, 190, 64, 64);
		contentPane.add(btnReset);

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarRA();
			}
		});
		btnBuscar.setForeground(SystemColor.textHighlight);
		btnBuscar.setBounds(128, 38, 75, 22);
		contentPane.add(btnBuscar);

		btnEditar = new JButton("");
		btnEditar.setEnabled(false);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editar();
			}
		});
		btnEditar.setIcon(new ImageIcon(Carometro.class.getResource("/img/update.png")));
		btnEditar.setToolTipText("Editar");
		btnEditar.setBounds(86, 190, 64, 64);
		contentPane.add(btnEditar);

		btnExcluir = new JButton("");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluir();
			}
		});
		btnExcluir.setIcon(new ImageIcon(Carometro.class.getResource("/img/delete.png")));
		btnExcluir.setToolTipText("Excluir");
		btnExcluir.setBounds(152, 190, 64, 64);
		contentPane.add(btnExcluir);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Carometro.class.getResource("/img/search.png")));
		lblNewLabel_2.setBounds(280, 78, 24, 24);
		contentPane.add(lblNewLabel_2);

		btnSobre = new JButton("");
		btnSobre.setToolTipText("Sobre");
		btnSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sobre sobre = new Sobre();
				sobre.setVisible(true);
			}
		});
		btnSobre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSobre.setContentAreaFilled(false);
		btnSobre.setBorderPainted(false);
		btnSobre.setIcon(new ImageIcon(Carometro.class.getResource("/img/info.png")));
		btnSobre.setBounds(289, 8, 48, 48);
		contentPane.add(btnSobre);

		btnPdf = new JButton("");
		btnPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarPdf();
			}
		});
		btnPdf.setToolTipText("Gerar lista de alunos");
		btnPdf.setIcon(new ImageIcon(Carometro.class.getResource("/img/pdf.png")));
		btnPdf.setBounds(262, 115, 64, 64);
		contentPane.add(btnPdf);

		this.setLocationRelativeTo(null);
	}

	private void status() {
		try {
			con = dao.conectar();
			if (con == null) {
				lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dboff.png")));
			} else {
				lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dbon.png")));
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void setarData() {
		Date data = new Date();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
		lblData.setText(formatador.format(data));
	}

	private void carregarFoto() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecionar arquivo");
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de imagens (*.PNG,*.JPG,*.JPEG)", "png", "jpg", "jpeg"));
		int resultado = jfc.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			try {
				fis = new FileInputStream(jfc.getSelectedFile());
				tamanho = (int) jfc.getSelectedFile().length();
				Image foto = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblFoto.getWidth(),
						lblFoto.getHeight(), Image.SCALE_SMOOTH);
				lblFoto.setIcon(new ImageIcon(foto));
				lblFoto.updateUI();
				fotoCarregada = true;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private void adicionar() {
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o nome");
			txtNome.requestFocus();
		} else if (tamanho == 0) {
			JOptionPane.showMessageDialog(null, "Selecione a foto");
		} else {
			String insert = "insert into alunos(nome,foto) values(?,?)";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(insert);
				pst.setString(1, txtNome.getText());
				pst.setBlob(2, fis, tamanho);
				int confirma = pst.executeUpdate();
				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");
					reset();
				} else {
					JOptionPane.showMessageDialog(null, "Erro! Aluno não cadastrado.");
				}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private void buscarRA() {
		if (txtRA.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Digite o RA");
			txtRA.requestFocus();
		} else {
			String readRA = "select * from alunos where ra = ?";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(readRA);
				pst.setString(1, txtRA.getText());
				rs = pst.executeQuery();
				if (rs.next()) {
					txtNome.setText(rs.getString(2));
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(),
							lblFoto.getHeight(), Image.SCALE_SMOOTH));
					lblFoto.setIcon(foto);
					txtRA.setEditable(false);
					btnBuscar.setEnabled(false);
					btnCarregar.setEnabled(true);
					btnEditar.setEnabled(true);
					btnExcluir.setEnabled(true);
					btnPdf.setEnabled(false);
				} else {
					int confirma = JOptionPane.showConfirmDialog(null,
							"Aluno não cadastrado.\nDeseja iniciar um novo cadastro?", "Aviso", JOptionPane.YES_OPTION);
					if (confirma == JOptionPane.YES_OPTION) {
						txtRA.setEditable(false);
						txtRA.setText(null);
						btnBuscar.setEnabled(false);
						txtNome.setText(null);
						txtNome.requestFocus();
						btnCarregar.setEnabled(true);
						btnAdicionar.setEnabled(true);
					} else {
						reset();
					}
				}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private void listarNomes() {
		DefaultListModel<String> modelo = new DefaultListModel<>();
		listNomes.setModel(modelo);
		String readLista = "select * from alunos where nome like '" + txtNome.getText() + "%'" + "order by nome";
		try {
			con = dao.conectar();
			pst = con.prepareStatement(readLista);
			rs = pst.executeQuery();
			while (rs.next()) {
				scrollPaneLista.setVisible(true);
				modelo.addElement(rs.getString(2));
				if (txtNome.getText().isEmpty()) {
					scrollPaneLista.setVisible(false);
				}
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void buscarNome() {
		int linha = listNomes.getSelectedIndex();
		if (linha >= 0) {
			String readNome = "select * from alunos where nome like '" + txtNome.getText() + "%'"
					+ "order by nome limit " + (linha) + ", 1";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(readNome);
				rs = pst.executeQuery();
				while (rs.next()) {
					scrollPaneLista.setVisible(false);
					txtRA.setText(rs.getString(1));
					txtNome.setText(rs.getString(2));
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(),
							lblFoto.getHeight(), Image.SCALE_SMOOTH));
					lblFoto.setIcon(foto);
					lblFoto.setIcon(foto);
					txtRA.setEditable(false);
					btnBuscar.setEnabled(false);
					btnCarregar.setEnabled(true);
					btnEditar.setEnabled(true);
					btnExcluir.setEnabled(true);
					btnPdf.setEnabled(false);
				}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			scrollPaneLista.setVisible(false);
		}
	}

	private void editar() {
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o nome");
			txtNome.requestFocus();
		} else {
			if (fotoCarregada == true) {
				String update = "update alunos set nome=? ,foto=? where ra=?";
				try {
					con = dao.conectar();
					pst = con.prepareStatement(update);
					pst.setString(1, txtNome.getText());
					pst.setBlob(2, fis, tamanho);
					pst.setString(3, txtRA.getText());
					int confirma = pst.executeUpdate();
					if (confirma == 1) {
						JOptionPane.showMessageDialog(null, "Dados do aluno alterados.");
						reset();
					} else {
						JOptionPane.showMessageDialog(null, "Erro! Dados do aluno não alterados.");
					}
					con.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			} else {
				String update = "update alunos set nome=? where ra=?";
				try {
					con = dao.conectar();
					pst = con.prepareStatement(update);
					pst.setString(1, txtNome.getText());
					pst.setString(2, txtRA.getText());
					int confirma = pst.executeUpdate();
					if (confirma == 1) {
						JOptionPane.showMessageDialog(null, "Dados do aluno alterados.");
						reset();
					} else {
						JOptionPane.showMessageDialog(null, "Erro! Dados do aluno não alterados.");
					}
					con.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
	}

	private void excluir() {
		int confirmaExcluir = JOptionPane.showConfirmDialog(null, "Confirma a exclusão deste aluno ?", "Atenção!",
				JOptionPane.YES_NO_OPTION);
		if (confirmaExcluir == JOptionPane.YES_OPTION) {
			String delete = "delete from alunos where ra=?";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(delete);
				pst.setString(1, txtRA.getText());
				int confirma = pst.executeUpdate();
				if (confirma == 1) {
					reset();
					JOptionPane.showMessageDialog(null, "Aluno excluído");
				}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private void gerarPdf() {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("alunos.pdf"));
			document.open();
			Date data = new Date();
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
			document.add(new Paragraph(formatador.format(data)));
			document.add(new Paragraph("Listagem de alunos:"));
			document.add(new Paragraph(" "));
			PdfPTable tabela = new PdfPTable(3);
			PdfPCell col1 = new PdfPCell(new Paragraph("RA"));
			tabela.addCell(col1);
			PdfPCell col2 = new PdfPCell(new Paragraph("Nome"));
			tabela.addCell(col2);
			PdfPCell col3 = new PdfPCell(new Paragraph("Foto"));
			tabela.addCell(col3);
			String readLista = "select * from alunos order by nome";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(readLista);
				rs = pst.executeQuery();
				while (rs.next()) {
					tabela.addCell(rs.getString(1));
					tabela.addCell(rs.getString(2));
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(img);
					tabela.addCell(image);
				}
				con.close();
			} catch (Exception ex) {
				System.out.println(ex);
			}
			document.add(tabela);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			document.close();
		}

		try {
			Desktop.getDesktop().open(new File("alunos.pdf"));
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}

	private void reset() {
		scrollPaneLista.setVisible(false);
		txtRA.setText(null);
		txtNome.setText(null);
		lblFoto.setIcon(new ImageIcon(Carometro.class.getResource("/img/camera.png")));
		txtNome.requestFocus();
		fotoCarregada = false;
		tamanho = 0;
		txtRA.setEditable(true);
		btnBuscar.setEnabled(true);
		btnCarregar.setEnabled(false);
		btnAdicionar.setEnabled(false);
		btnEditar.setEnabled(false);
		btnExcluir.setEnabled(false);
		btnPdf.setEnabled(true);
	}
}
