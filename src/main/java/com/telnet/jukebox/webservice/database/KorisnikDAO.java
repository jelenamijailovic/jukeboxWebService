package com.telnet.jukebox.webservice.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import com.telnet.jukebox.webservice.model.Korisnik;
import com.telnet.jukebox.webservice.model.Login;

public class KorisnikDAO {

	Statement stmt = null;
	PreparedStatement prepStmt = null;
	ResultSet resultSet = null;

	public List<Korisnik> getKorisnici() throws ClassNotFoundException {
		List<Korisnik> korisnici = new ArrayList<>();

		try {
			Connection con = DatabaseConnector.conStat();
			stmt = con.createStatement();
			resultSet = stmt.executeQuery("select * from korisnici");
			while (resultSet.next()) {
				Korisnik korisnik = new Korisnik();
				korisnik.setId(resultSet.getInt(1));
				korisnik.setEmail(resultSet.getString(2));
				korisnik.setSifra(resultSet.getString(3));
				korisnici.add(korisnik);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return korisnici;
	}

	public Korisnik getKorisnik(int korisnikId) throws ClassNotFoundException {
		Korisnik korisnik = new Korisnik();

		try {
			Connection con = DatabaseConnector.conStat();
			prepStmt = con.prepareStatement("select * from korisnici where korisnici_id= ?");
			prepStmt.setLong(1, korisnikId);
			resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {
				korisnik.setId(resultSet.getInt(1));
				korisnik.setEmail(resultSet.getString(2));
				korisnik.setSifra(resultSet.getString(3));
				return korisnik;
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			korisnik.setSifra(null);
			korisnik.setEmail(null);
		}

		return korisnik;
	}

	public Korisnik insertKorisnik(Korisnik korisnik) throws ClassNotFoundException, SQLException {
		try {
			Connection con = DatabaseConnector.conStat();
			prepStmt = con.prepareStatement("insert into korisnici (korisnici_email, korisnici_sifra) values (?,?)");
			prepStmt.setString(1, korisnik.getEmail());
			prepStmt.setString(2, encryptPassword(korisnik.getSifra()));
			prepStmt.executeUpdate();
			resultSet = prepStmt.getGeneratedKeys();
			if (resultSet.next()) {
				korisnik.setId(resultSet.getInt(1));
			}
			con.close();
		} catch (IOException e) {
			System.out.println("insert");
			e.printStackTrace();
		}

		return korisnik;
	}

	public Korisnik loginKorisnik(Login login) throws ClassNotFoundException {
		Korisnik korisnik = new Korisnik();
		String sifra1 = encryptPassword(login.getSifra());

		try {
			Connection con = DatabaseConnector.conStat();
			prepStmt = con.prepareStatement("select * from korisnici where korisnici_email= ?");
			prepStmt.setString(1, login.getEmail());
			resultSet = prepStmt.executeQuery();

			while (resultSet.next()) {
				korisnik.setId(resultSet.getInt(1));
				korisnik.setEmail(resultSet.getString(2));
				korisnik.setSifra(resultSet.getString(3));

			}
			String korSifra = korisnik.getSifra();

			if (korSifra.equals(sifra1)) {
				korisnik.setSifra(login.getSifra());
			} else {
				System.out.println("Pogresna sifra");
				korisnik.setSifra(null);
				korisnik.setEmail(null);
			}
			con.close();
		} catch (SQLException e) {

		} catch (IOException e) {
			System.out.println("iocatch");
			korisnik.setSifra(null);
			korisnik.setEmail(null);
		}

		return korisnik;
	}

	public Korisnik updateKorisnik(Korisnik korisnik) throws ClassNotFoundException {
		try {
			Connection con = DatabaseConnector.conStat();
			prepStmt = con.prepareStatement(
					"update korisnici set korisnici_sifra=?, korisnici_email=? where korisnici_id= ?");
			prepStmt.setString(1, korisnik.getSifra());
			prepStmt.setString(2, korisnik.getEmail());
			prepStmt.setInt(3, korisnik.getId());
			prepStmt.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return korisnik;
	}

	public void deleteKorisnik(int korisnikId) throws ClassNotFoundException {
		try {
			Connection con = DatabaseConnector.conStat();
			prepStmt = con.prepareStatement("delete from korisnici where korisnici_id= ?");
			prepStmt.setInt(1, korisnikId);
			prepStmt.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String encryptPassword(String password) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

}
