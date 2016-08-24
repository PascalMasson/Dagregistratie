package com.naugrim.zeecontainer.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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

	/**
	 * @param sql
	 */
	public Person[] request(String sql) throws Exception {
		// TODO RETURN PERSON ARRAY. PROCESS THIS ARRAY IN CALLING FUNCTION
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		// TODO CREATE PERSON FROM VARIABLED THEN ADD THAT PERSON TO AN ARRAY.
		// THIS ARRAY WILL HAVE TO BE FED INTO THE DATA AND THE TABLE
		ArrayList<Person> plist = new ArrayList<>();
		while (rs.next()) {
			System.out.println("RS.NEXT()");
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

			plist.add(new Person(dag, voornaam, achternaam, adres, postcode, woonplaats, telefoonnummer, mail, instantie, contperInstantie, telefoonnummerContact, emailContact, DBID, inschrijfnummer, volwassenen, kinderen, reglement));
		}
		System.out.println(rs.getRow());
		System.out.println(rs.getFetchSize() + "/" + rs.getFetchDirection());

		Person[] parr = new Person[plist.size()];

		int i = 0;
		for (Iterator<Person> iterator = plist.iterator(); iterator.hasNext();) {
			Person person = iterator.next();
			parr[i] = person;
			i++;
		}

		return parr;
	}

	public void SendPassword(String PW) {
		String sql = "INSERT INTO `zeecontainer`.`password` (`hash`) VALUES ('" + PW.hashCode() + "');";

		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void put(String table_name, Person[] Values) {
		String sql = "";
		for (int i = 0; i < Values.length; i++) {
			sql = "INSERT INTO `zeecontainer`.`" + table_name + "` (`Voornaam`, `Achternaam`, `Dag`, " + "`Adres`, `Postcode`, `Woonplaats`) VALUES ('" + Values[i].voornaam + "', '" + Values[i].achternaam + "', '" + Values[i].dag.toString() + "', '" + Values[i].adres + "', ' " + Values[i].postcode + "', '" + Values[i].woonplaats + "');";
			try {
				stmt.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public int getNextID() {

		return 1;
	}

	public void Delete(String string) throws Exception {
		stmt = con.createStatement();
		stmt.executeUpdate(string);
	}

	public int getPassword() {
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM zeecontainer.password;");
			int pwhash = 0;

			while (rs.next()) {
				pwhash = rs.getInt(2);
			}

			System.out.println(pwhash);
			return pwhash;

		} catch (Exception e) {

		}
		return 0;
	}
}
