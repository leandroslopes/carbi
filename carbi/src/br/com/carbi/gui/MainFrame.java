package br.com.carbi.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.JTableHeader;

import br.com.carbi.collection.StudentsCollection;
import br.com.carbi.dao.StudentDAO;
import br.com.carbi.exception.ConnectionException;
import br.com.carbi.model.Student;
import br.com.carbi.report.Report;
import br.com.carbi.tablemodel.TableModelStudent;
import br.com.carbi.util.FileExtesion;
import net.sf.jasperreports.engine.JRException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField txtSearch;
	private JTableHeader jTableHeader;

	private ArrayList<Student> students;
	private StudentDAO studentDAO;

	public MainFrame() {
		StudentsCollection studentsColletion = new StudentsCollection();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnImportFile = new JButton("Importar Alunos");
		btnImportFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setDialogTitle("Importar Alunos");
				int result = jFileChooser.showDialog(null, "Importar");
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					if (FileExtesion.getFileExtension(file).equals(".csv")) {
						try {
							StudentDAO studentDAO = new StudentDAO();
							studentDAO.importStudent(file);
							JOptionPane.showMessageDialog(null, "Aluno(s) importado(s)!", "Importar Alunos", JOptionPane.INFORMATION_MESSAGE);
						} catch (ConnectionException ce) {						
							ce.printStackTrace();
							JOptionPane.showMessageDialog(null, "Erro ao importar aluno(s)!", "Importar Alunos", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Selecione um arquivo .csv", "Importar Alunos", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnImportFile.setBounds(12, 12, 147, 25);
		contentPane.add(btnImportFile);
		try {
			studentDAO = new StudentDAO();
			students = studentDAO.searchStudents(""); 
		} catch (ConnectionException ex) {
			ex.printStackTrace();
		}

		JLabel lblSearch = new JLabel("Pesquisar:");
		lblSearch.setBounds(12, 71, 83, 15);
		contentPane.add(lblSearch);

		txtSearch = new JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					studentDAO = new StudentDAO();
					students = studentDAO.searchStudents(txtSearch.getText()); 
					table.setModel(new TableModelStudent(students));
					TableModelStudent.resizeColumns(table);
				} catch (ConnectionException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro ao pesquisar aluno(s)!", "Pesquisar aluno(s)", JOptionPane.ERROR_MESSAGE);
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isLetter(e.getKeyChar())) {
					e.consume();
				}
			}
		});
		txtSearch.setBounds(95, 66, 412, 25);
		txtSearch.setFocusable(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtSearch.requestFocus();
			}
		});
		contentPane.add(txtSearch);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 98, 676, 261);
		contentPane.add(scrollPane);

		table = new JTable();
		jTableHeader = table.getTableHeader();
		jTableHeader.setFont(jTableHeader.getFont().deriveFont(Font.BOLD));
		jTableHeader.setBackground(Color.GREEN);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					final JTable jTable= (JTable)e.getSource();
					final int row = jTable.getSelectedRow();
					final String registration = (String) jTable.getValueAt(row, 0);
					final String name = (String)jTable.getValueAt(row, 1);
					final String course = (String)jTable.getValueAt(row, 2);

					studentsColletion.add(new Student(Integer.parseInt(registration), name, course));

					if (studentsColletion.getColletion().size() == 5) {
						try {
							Report.genetate(studentsColletion);
							studentsColletion.removeAllElements();
						} catch (JRException | NumberFormatException | ConnectionException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Gerar carteira", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, 
								"Falta adicionar " + (5 - studentsColletion.getColletion().size()) + " aluno(s) para imprimir as carteiras", 
								"Gerar carteira", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		table.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		table.setModel(new TableModelStudent(students));
		TableModelStudent.resizeColumns(table);
		setLocationRelativeTo(null);
		setResizable(false);
	}
}
