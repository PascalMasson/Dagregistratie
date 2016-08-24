package com.naugrim.zeecontainer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.naugrim.zeecontainer.frame.Dag;
import com.naugrim.zeecontainer.frame.Person;
import com.naugrim.zeecontainer.utils.DatabaseManager;
import com.toedter.calendar.JDateChooser;

public class Main extends JFrame {

	public static ArrayList<Person> Persons = new ArrayList<>();
	boolean init = false;
	public static DatabaseManager manager;

	public static Dag filterDag;
	private JPanel contentPane;
	private JTable table;
	private CustomRenderer renderer;

	/**
	 * Create the frame.
	 */
	// INSERT INTO `zeecontainer`.`bezoeken` (`idbezoeken`, `Inschrijfnummer`,
	// `Datum`) VALUES ('2', '50', '2014-05-26');

	public Main() {
		renderer = new CustomRenderer();
		String host = "jdbc:mysql://192.168.178.28/zeecontainer";

		manager = new DatabaseManager(host, "java", "javapw");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 537);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnRegistreer = new JMenu("Registreer");
		menuBar.add(mnRegistreer);

		JMenu mnZoek = new JMenu("Zoek");
		menuBar.add(mnZoek);

		JMenuItem mntmPersoon = new JMenuItem("Client");
		mnZoek.add(mntmPersoon);

		JMenuItem mntmDag = new JMenuItem("Dag");
		mnZoek.add(mntmDag);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JDateChooser dateChooser = new JDateChooser(Calendar.getInstance().getTime());
		dateChooser.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!init)
					return;
				Calendar c = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				c2.setTime(c.getTime());
				c.setTime(dateChooser.getDate());
				System.out.println(c.getTime().compareTo(c2.getTime()));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String seldatstr = sdf.format(c.getTime());
				String curdagstr = sdf.format(c2.getTime());
				
				java.util.Date seldag = null;
				java.util.Date curdag = null;
				
				try{
					seldag = (java.util.Date) sdf.parse(seldatstr);
					curdag = (java.util.Date) sdf.parse(curdagstr);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				int timediff = seldag.compareTo(curdag);
				System.out.println("sel dat: " + c.getTime().toString());
				System.out.println("cur dat: " + c2.getTime().toString());
				System.out.println("TimeDif: " + timediff);
				switch (timediff) {
					case -1:
						System.out.println("Vroeger");
						// get all visits from database where date
						break;
					case 1:
						System.out.println("Toekomst");
						ChangeFilter(Dag.getDagFromDagnumber(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
						break;
					default:
						System.out.println("Vandaag/default");
						filterDag = Dag.getDagFromDagnumber(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
						ArrayList<Person> tmp = Persons;

						// mark people who have already visited
						Integer[] visitors = null;
						java.util.Date utildate = c.getTime();
						java.sql.Date sqldate = new java.sql.Date(utildate.getTime());
						try {
							visitors = manager.getBezoeken(sqldate);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						int i = 0;
						for (Iterator iterator = tmp.iterator(); iterator.hasNext();) {
							Person person = (Person) iterator.next();
							if (Arrays.asList(visitors).contains(person.inschrijfnummer)) {
								table.getColumnModel().getColumn(0).setCellRenderer(renderer);
								table.getColumnModel().getColumn(1).setCellRenderer(renderer);
								table.getColumnModel().getColumn(2).setCellRenderer(renderer);
								table.getColumnModel().getColumn(3).setCellRenderer(renderer);
								table.getColumnModel().getColumn(4).setCellRenderer(renderer);
								
								renderer.colorModel.put(i, Color.green);
								
								i++;
							}
						}

						populateTable(table, tmp);
						break;
				}
			}
		});
		contentPane.add(dateChooser, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

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

				for (int i = 0; i < Persons.size(); i++) {
					Person person = Persons.get(i);
					if (person.inschrijfnummer == Inschrijfnummer) {
						if (person.achternaam == achternaam) {
							if (person.voornaam == voornaam) {
								if (person.volwassenen == volwassenen) {
									if (person.kinderen == kinderen) {
										System.out.println("gevonden");
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
										java.sql.Date sqldate = new java.sql.Date(
												Calendar.getInstance().getTime().getTime());
										manager.logBezoek(person.inschrijfnummer, sqldate);

										table.getColumnModel().getColumn(0).setCellRenderer(renderer);
										table.getColumnModel().getColumn(1).setCellRenderer(renderer);
										table.getColumnModel().getColumn(2).setCellRenderer(renderer);
										table.getColumnModel().getColumn(3).setCellRenderer(renderer);
										table.getColumnModel().getColumn(4).setCellRenderer(renderer);
										
										renderer.colorModel.put(row, Color.green);
										
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

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null, null }, },
				new String[] { "Inschrijfnummer", "Voornaam", "Achternaam", "Volwassenen", "Kinderen" }));
		table.setComponentPopupMenu(popup);
		table.getColumnModel().getColumn(0).setCellRenderer(renderer);
		table.getColumnModel().getColumn(1).setCellRenderer(renderer);
		table.getColumnModel().getColumn(2).setCellRenderer(renderer);
		table.getColumnModel().getColumn(3).setCellRenderer(renderer);
		table.getColumnModel().getColumn(4).setCellRenderer(renderer);
		scrollPane.setViewportView(table);

		try {
			Persons = new ArrayList<>(Arrays.asList(manager.request("SELECT * FROM data;")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dag weekdag = Dag.getDagFromDagnumber(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
		System.out.println("Het is " + weekdag.toString());
		ChangeFilter(weekdag);
		init = true;
	}

	public static void populateTable(JTable table, ArrayList<Person> list) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Person cur;
		model.setRowCount(0);
		for (int i = 0; i < list.size(); i++) {
			cur = list.get(i);
			if (filterDag == Dag.ALLE || filterDag == cur.dag)
				model.addRow(new Object[] { cur.inschrijfnummer, cur.voornaam, cur.achternaam, cur.volwassenen,
						cur.kinderen });

		}
	}

	void ChangeFilter(Dag dag) {
		filterDag = dag;
		((DefaultTableModel) table.getModel()).setRowCount(0);
		populateTable(table, Persons);

	}

	public static class CustomRenderer extends DefaultTableCellRenderer {
		public HashMap<Integer, Color> colorModel = new HashMap<>();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			// TODO Auto-generated method stub
			Component result = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (colorModel.get(row) != null) {
				setBackground(colorModel.get(row));
			} else if (!isSelected){
				setBackground(null);
			}

			return result;

		}
	}

}
