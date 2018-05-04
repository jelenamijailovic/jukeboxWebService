package com.telnet.jukebox.webservice.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.telnet.jukebox.webservice.model.Cena;
import com.telnet.jukebox.webservice.model.Pesma;
import com.telnet.jukebox.webservice.service.CenaService;
import com.telnet.jukebox.webservice.service.PesmaService;

@Path("/cene")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class CenaResource {

	final static Logger logger = Logger.getLogger(CenaResource.class);

	CenaService cenaService = new CenaService();
	PesmaService pesmaService = new PesmaService();

	@SuppressWarnings("unused")
	@GET
	public Response getCene() throws ClassNotFoundException {
		logger.info("Prikaz svih cena");

		List<Cena> cene = cenaService.getCene();
		GenericEntity<List<Cena>> list = new GenericEntity<List<Cena>>(cene) {
		};

		Response r;

		if (list == null) {
			r = Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Ne postoje unete cene")
					.header("Access-Control-Allow-Methods", "GET").allow("OPTIONS").build();
			logger.error("Ne postoje unete cene");
		} else {
			r = Response.ok(list).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").allow("OPTIONS").build();
			logger.info("Cene su uspesno prikazane");
		}

		return r;
	}

	@GET
	@Path("/{cenaId}")
	public Response getCena(@PathParam("cenaId") Long cenaId) throws ClassNotFoundException {
		logger.info("Prikaz cene sa id-om " + cenaId);

		Cena c = cenaService.getCena(cenaId);

		Response r;

		if (c.getId() == 0) {
			r = Response.status(404).header("Access-Control-Allow-Origin", "*")
					.entity("Ne postoji cena sa id-om " + cenaId).header("Access-Control-Allow-Methods", "GET")
					.allow("OPTIONS").build();
			logger.error("Ne postoji cena sa id-om " + cenaId);
		} else {
			r = Response.ok(cenaService.getCena(cenaId)).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").allow("OPTIONS").build();
			logger.info("Uspesno prikazana cena sa id-om " + cenaId);
		}

		return r;
	}

	@POST
	public Cena addCena(Cena cena) throws ClassNotFoundException {
		logger.info("Unosenje cene");

		Cena c = new Cena();

		try {
			c = cenaService.addCena(cena);
			logger.info("Cena je uspesno uneta");
		} catch (Exception e) {
			logger.error("Greska pri unosu cene:\n" + e.getMessage());
		}

		return c;
	}

	@PUT
	@Path("/{cenaId}")
	public Cena updateIzvodjac(@PathParam("cenaId") Long cenaId, Cena cena) throws ClassNotFoundException {
		cena.setId(cenaId);

		logger.info("Modifikovanje cene sa id-om " + cenaId);

		Cena c = cenaService.getCena(cenaId);

		if (c.getId() == 0) {
			logger.error("Cena sa id-om " + cenaId + " ne moze biti modifikovana jer ne postoji");
		} else {
			c = cenaService.updateCena(cena);
			logger.info("Cena sa id-om " + cenaId + " je uspesno modifikovana");
		}

		return c;
	}

	@DELETE
	@Path("/{cenaId}")
	public void deleteCena(@PathParam("cenaId") Long cenaId) throws ClassNotFoundException {
		logger.info("Brisanje cene sa id-om " + cenaId);

		Cena c = cenaService.getCena(cenaId);

		if (c.getId() == 0) {
			logger.error("Cena sa id-om " + cenaId + " ne moze biti obrisana jer ne postoji");
		} else {
			cenaService.removeCena(cenaId);
			logger.info("Cena je uspesno obrisana");
		}

	}

	@GET
	@Path("/{cenaId}/pesme")
	public Response getSvePesmePoCeni(@PathParam("cenaId") Long cenaId) throws ClassNotFoundException {

		logger.info("Prikaz pesama za cenu sa id-om " + cenaId);

		List<Pesma> pesme = pesmaService.getSvePesmePoCeni(cenaId);
		GenericEntity<List<Pesma>> list = new GenericEntity<List<Pesma>>(pesme) {
		};

		Cena c = cenaService.getCena(cenaId);

		Response r;

		if (c.getId() == 0) {
			r = Response.status(404).header("Access-Control-Allow-Origin", "*")
					.entity("Ne postoje pesme za cenu sa id-om " + cenaId).header("Access-Control-Allow-Methods", "GET")
					.allow("OPTIONS").build();
			logger.error("Ne postoje pesme za cenu sa id-om " + cenaId);
		} else {
			r = Response.ok(list).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").allow("OPTIONS").build();
			logger.info("Uspesan prikaz pesama za cenu sa id-om " + cenaId);
		}

		return r;
	}

}
