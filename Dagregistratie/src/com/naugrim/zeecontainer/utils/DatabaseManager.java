package com.naugrim.zeecontainer.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.mysql.jdbc.PreparedStatement;
import com.naugrim.zeecontainer.frame.Dag;
import com.naugrim.zeecontainer.frame.Person;

public class DatabaseManager {
	Connection con;
	Statement stmt;
	String host, username, password;

	public DatabaseManager(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
		connect();

	}

	public void connect() {
		try {
			con = DriverManager.getConnection(host, username, password);
			stmt = con.createStatement();
		} catch (SQLException e) {
			System.err.println("Failed to create a connection with the provided database");
			e.printStackTrace();
		}
	}

	public Person[] request(String sql) throws Exception {
		// TODO RETURN PERSON ARRAY. PROCESS THIS ARRAY IN CALLING FUNCTION
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		// TODO CREATE PERSON FROM VARIABLED THEN ADD THAT PERSON TO AN ARRAY.
		// THIS ARRAY WILL HAVE TO BE FED INTO THE DATA AND THE TABLE
		ArrayList<Person> plist = new ArrayList<>();
		while (rs.next()) {
			int DBID = rs.getInt("DatabaseID"); // hidden
			int inschrijfnummer = rs.getInt("Inschrijfnummer"); // table
			String voornaam = rs.getString("Voornaam"); // table
			String achternaam = rs.getString("Achternaam"); // table
			boolean reglement = (rs.getInt("Reglementen") == 0) ? false : true; // table
			String instantie = rs.getString("Instantie"); // details
			String contperInstantie = rs.getString("ContactpersoonInstantie"); // details
			String telefoonnummerContact = rs.getString("TelefoonnummerContact"); // details
			String emailContact = rs.getString("EmailContact"); // details
			String adres = rs.getString("Adres"); // table
			String postcode = rs.getString("Postcode"); // table
			String woonplaats = rs.getString("Woonplaats"); // table
			String telefoonnummer = rs.getString("Telefoonnummer"); // table
			String mail = rs.getString("Emailadres"); // table
			int volwassenen = rs.getInt("Volwassenen"); // table
			int kinderen = rs.getInt("Kinderen"); // table
			Dag dag = Dag.fromString(rs.getString("Winkeldag")); // table

			plist.add(new Person(dag, voornaam, achternaam, adres, postcode, woonplaats, telefoonnummer, mail,
					instantie, contperInstantie, telefoonnummerContact, emailContact, DBID, inschrijfnummer,
					volwassenen, kinderen, reglement));
		}
		System.out.println(plist.size() + " row(s) returned");

		Person[] parr = new Person[plist.size()];

		int i = 0;
		for (Iterator<Person> iterator = plist.iterator(); iterator.hasNext();) {
			Person person = iterator.next();
			parr[i] = person;
			i++;
		}

		return parr;
	}

	private int getNextID() throws SQLException {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT MAX(idbezoeken) FROM bezoeken;");
		
		String lastid = "";
		while(rs.next()){
			lastid = rs.getString(1);
		}
		if(lastid == null){
			lastid = "0";
		}
		return Integer.valueOf(lastid) + 1;
	}

	public boolean logBezoek(int inschrijf, Date date) {
		try {

			Statement st = con.createStatement();
			boolean rs = st.execute(
					"INSERT INTO `zeecontainer`.`bezoeken` (`idbezoeken`, `Inschrijfnummer`, `Datum`) VALUES ('"
							+ getNextID() + "', '" + inschrijf + "', '" + date + "');");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Integer[] getBezoeken(java.sql.Date date) throws SQLException {
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Inschrijfnummer FROM bezoeken WHERE Datum='" + date.toString() + "';");
		ArrayList<Integer> l = new ArrayList<>();
		while (rs.next()) {
			l.add(rs.getInt("Inschrijfnummer"));
		}
		System.out.println(l.size() + " people already visited today");
		return Arrays.copyOfRange(l.toArray(), 0, l.toArray().length, Integer[].class);
	}
}
