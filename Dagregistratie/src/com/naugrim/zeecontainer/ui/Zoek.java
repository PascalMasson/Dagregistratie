package com.naugrim.zeecontainer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.naugrim.zeecontainer.frame.Person;

@SuppressWarnings("serial")
public class Zoek extends JFrame implements DocumentListener {

	public static Zoek instance;
	private JPanel contentPane;
	final JTable table;

	final TableRowSorter<TableModel> sorter;
	DefaultTableModel model;
	private JTextField textField;

	public Zoek() {
		
		instance = this;
		model = new DefaultTableModel();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setCellSelectionEnabled(true);
		table.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null, null }, },
				new String[] { "Inschrijfnummer", "Voornaam", "Achternaam", "Volwassenen", "Kinderen" }));
		JPopupMenu popup = new JPopupMenu();
		popup.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popup, new Point(0, 0), table));
						if (rowAtPoint > -1) {
							table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
						}
					}
				});
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				// TODO Auto-generated method stub

			}
		});
		JMenuItem registreer = new JMenuItem("Registreer");
		registreer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();

				int Inschrijfnummer = (int) table.getValueAt(row, 0);
				String voornaam = (String) table.getValueAt(row, 1);
				String achternaam = (String) table.getValueAt(row, 2);
				int volwassenen = (int) table.getValueAt(row, 3);
				int kinderen = (int) table.getValueAt(row, 4);

				for (int i = 0; i < Main.Persons.size(); i++) {
					Person person = Main.Persons.get(i);
					if (person.inschrijfnummer == Inschrijfnummer) {
						if (person.achternaam == achternaam) {
							if (person.voornaam == voornaam) {
								if (person.volwassenen == volwassenen) {
									if (person.kinderen == kinderen) {
										if (Main.renderer.colorModel.containsKey(row))
											return;

										System.out.println("gevonden");
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
										java.sql.Date sqldate = new java.sql.Date(
												Calendar.getInstance().getTime().getTime());
										Main.manager.logBezoek(person.inschrijfnummer, sqldate);

										table.getColumnModel().getColumn(0).setCellRenderer(Main.renderer);
										table.getColumnModel().getColumn(1).setCellRenderer(Main.renderer);
										table.getColumnModel().getColumn(2).setCellRenderer(Main.renderer);
										table.getColumnModel().getColumn(3).setCellRenderer(Main.renderer);
										table.getColumnModel().getColumn(4).setCellRenderer(Main.renderer);

										Main.renderer.colorModel.put(row, Color.green);
										((DefaultTableModel) Main.table.getModel()).fireTableDataChanged();
										table.clearSelection();

									} else {
										continue;
									}
								} else {
									continue;
								}
							} else {
								continue;
							}
						} else {
							continue;
						}
					} else {
						continue;
					}
				}

			}
		});
		popup.add(registreer);
		table.setComponentPopupMenu(popup);
		((DefaultTableModel) table.getModel()).setRowCount(0);
		scrollPane.setViewportView(table);
		sorter = new TableRowSorter<TableModel>(table.getModel());
		table.setRowSorter(sorter);

		textField = new JTextField();
		textField.getDocument().addDocumentListener(this);
		contentPane.add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		table.getColumnModel().getColumn(0).setCellRenderer(Main.renderer);
		table.getColumnModel().getColumn(1).setCellRenderer(Main.renderer);
		table.getColumnModel().getColumn(2).setCellRenderer(Main.renderer);
		table.getColumnModel().getColumn(3).setCellRenderer(Main.renderer);
		table.getColumnModel().getColumn(4).setCellRenderer(Main.renderer);
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		updateTableFilter();

	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		updateTableFilter();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		updateTableFilter();
	}

	public void updateTableFilter() {
		String text = textField.getText();
		if (text.length() == 0) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(RowFilter.regexFilter(text));
		}
	}
}
