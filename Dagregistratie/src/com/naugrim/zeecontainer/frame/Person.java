package com.naugrim.zeecontainer.frame;

import java.io.Serializable;

public class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5370307042704985068L;
	public Dag dag;
	public String voornaam, achternaam, adres, postcode, woonplaats, telefoonnummer, emailadres, instantie,
			contactInstantie, telefoonnummerContact, emailContact;
	public int databaseID, inschrijfnummer, volwassenen, kinderen;
	public boolean reglementen;

	public Person(Dag dag, String voornaam, String achternaam, String adres, String postcode, String woonplaats,
			String telefoonnummer, String emailadres, String instantie, String contactInstantie,
			String telefoonnummerContact, String emailContact, int databaseID, int inschrijfnummer, int volwassenen,
			int kinderen, boolean reglementen) {
		this.dag = dag;
		this.voornaam = voornaam;
		this.achternaam = achternaam;
		this.adres = adres;
		this.postcode = postcode;
		this.woonplaats = woonplaats;
		this.telefoonnummer = telefoonnummer;
		this.emailadres = emailadres;
		this.instantie = instantie;
		this.contactInstantie = contactInstantie;
		this.telefoonnummerContact = telefoonnummerContact;
		this.emailContact = emailContact;
		this.databaseID = databaseID;
		this.inschrijfnummer = inschrijfnummer;
		this.volwassenen = volwassenen;
		this.kinderen = kinderen;
		this.reglementen = reglementen;
	}

}
