package com.telnet.jukebox.webservice.model;

import java.sql.Date;
import java.text.ParseException;

public class Promet {

	private long id;
	private Date datum;
	private Long pesmaId;
	private String pesmaNaziv;
	private Long cenaKolicina;
	private Long repetition;
	private String izvodjacIme;

	public Promet() {
	}

	public Promet(long id, Date datum) throws ParseException {
		super();
		// DateFormat df = new SimpleDateFormat("MMM dd hh:mm:ss yyyy", Locale.ENGLISH);
		this.id = id;
		// this.datum = df.parse(df.format(datum));
		this.datum = datum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public String getPesmaNaziv() {
		return pesmaNaziv;
	}

	public void setPesmaNaziv(String pesmaNaziv) {
		this.pesmaNaziv = pesmaNaziv;
	}

	public Long getCenaKolicina() {
		return cenaKolicina;
	}

	public void setCenaKolicina(Long cenaKolicina) {
		this.cenaKolicina = cenaKolicina;
	}

	public Long getPesmaId() {
		return pesmaId;
	}

	public void setPesmaId(Long pesmaId) {
		this.pesmaId = pesmaId;
	}

	public Long getRepetition() {
		return repetition;
	}

	public void setRepetition(Long repetition) {
		this.repetition = repetition;
	}

	public String getIzvodjacIme() {
		return izvodjacIme;
	}

	public void setIzvodjacIme(String izvodjacIme) {
		this.izvodjacIme = izvodjacIme;
	}

}
