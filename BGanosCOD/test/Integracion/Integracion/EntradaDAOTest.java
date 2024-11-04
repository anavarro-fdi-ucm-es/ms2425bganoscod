package Integracion;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.Date;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import Integracion.Entrada.EntradaDAO;
import Integracion.FactoriaIntegracion.FactoriaIntegracion;
import Integracion.Invernadero.InvernaderoDAO;
import Integracion.Transaction.Transaccion;
import Integracion.Transaction.TransaccionManager;
import Negocio.Entrada.TEntrada;
import Negocio.Invernadero.TInvernadero;

import org.junit.Test;



// para que los test se comprueben en orden
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntradaDAOTest {

	

	private boolean equals(TEntrada e1, TEntrada e2) {
//		if (e1 == null || e2 == null)
//			return false;

		return e1.getId().equals(e2.getId()) && e1.getIdInvernadero().equals(e2.getIdInvernadero())
				/*&& e1.getFecha().equals(e2.getFecha())*/ && e1.getPrecio().equals(e2.getPrecio())
				&& e1.getStock().equals(e2.getStock())
				&& e1.getActivo().equals(e2.getActivo());
	}

	private Transaccion crearTransaccion() {
		try {
			TransaccionManager tm = TransaccionManager.getInstance();
			tm.newTransaccion();
			Transaccion t = tm.getTransaccion();
			return t;
		} catch (Exception e) {
			fail("Error transaccional");
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Test
	public void A_alta() {

		EntradaDAO daoEntrada = FactoriaIntegracion.getInstance().getEntradaDAO();
		InvernaderoDAO daoInvernadero = FactoriaIntegracion.getInstance().getInvernaderoDAO();
		Transaccion t;
		t = crearTransaccion();

		try {
			t.start();

			Connection c = (Connection) t.getResource();

			// Desactivar las comprobaciones de claves foráneas, luego las vuelvo a activar,
			// solo para poder truncar todas las tablas implicadas
			Statement statement = c.createStatement();
			statement.execute("SET FOREIGN_KEY_CHECKS = 0");

			PreparedStatement ps1 = c.prepareStatement("TRUNCATE linea_factura");
			ps1.executeUpdate();
			PreparedStatement ps3 = c.prepareStatement("TRUNCATE factura");
			ps3.executeUpdate();
			PreparedStatement ps = c.prepareStatement("TRUNCATE entrada");
			ps.executeUpdate();
			PreparedStatement ps2 = c.prepareStatement("TRUNCATE invernadero");
			ps2.executeUpdate();

			ps1.close();
			ps3.close();
			ps.close();
			ps2.close();

			// Volver a activar las comprobaciones de claves foráneas
			statement.execute("SET FOREIGN_KEY_CHECKS = 1");
			statement.close(); // Cerrar el Statement

			// TODO construir nueva constructora
			// id, sustrato, nombre, tipo iluminación, activo
			TInvernadero inv = new TInvernadero(1, "inv1", "abono", "artificial", true);
			inv.setId(daoInvernadero.altaInvernadero(inv));

			// id, fecha, precio, stock, idinvernadero, activo
			TEntrada e1 = new TEntrada(1, java.sql.Date.valueOf("2012-12-12"), 12.0f, 12, inv.getId(), true);
			e1.setId(daoEntrada.altaEntrada(e1));

			if (e1.getId() < 0) {
				fail("No se ha creado correctamente la entrada");
			}

			t.commit();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void B_baja() {

		try {
			EntradaDAO daoEntrada = FactoriaIntegracion.getInstance().getEntradaDAO();
			InvernaderoDAO daoInvernadero = FactoriaIntegracion.getInstance().getInvernaderoDAO();
			Transaccion t;
			t = crearTransaccion();
			t.start();

			daoInvernadero.bajaInvernadero(1);
			daoEntrada.bajaEntrada(1);

			TEntrada entrada = daoEntrada.mostrarEntrada(1);

			if (entrada.getActivo()) {
				fail("No se ha eliminado correctamente la entrada");
			}
			t.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void C_mostrar() {

		EntradaDAO daoEntrada = FactoriaIntegracion.getInstance().getEntradaDAO();
		InvernaderoDAO daoInvernadero = FactoriaIntegracion.getInstance().getInvernaderoDAO();
		Transaccion t;
		t = crearTransaccion();

		try {
			t.start();

			Connection c = (Connection) t.getResource();

			// Desactivar las comprobaciones de claves foráneas, luego las vuelvo a activar,
			// solo para poder truncar todas las tablas implicadas
			Statement statement = c.createStatement();
			statement.execute("SET FOREIGN_KEY_CHECKS = 0");

			PreparedStatement ps = c.prepareStatement("TRUNCATE entrada");
			ps.executeUpdate();
			PreparedStatement ps1 = c.prepareStatement("TRUNCATE invernadero");
			ps1.executeUpdate();
			ps.close();
			ps1.close();

			// Volver a activar las comprobaciones de claves foráneas
			statement.execute("SET FOREIGN_KEY_CHECKS = 1");
			statement.close(); // Cerrar el Statement

			// id, sustrato, nombre, tipo iluminación, activo
			TInvernadero inv = new TInvernadero(1, "inv1", "abono", "artificial", true);
			inv.setId(daoInvernadero.altaInvernadero(inv));

			// id, fecha, precio, stock, idinvernadero, activo
			LocalDate localDate = LocalDate.of(2012, 10, 10);
			Date sqlDate = Date.valueOf(localDate); // Convertimos LocalDate a java.sql.Date
			TEntrada e1 = new TEntrada(1, sqlDate, 12.0f, 12, inv.getId(), true);			
			Integer idEntrada = daoEntrada.altaEntrada(e1);
			//System.out.println(idEntrada);
			TEntrada e2 = daoEntrada.mostrarEntrada(idEntrada);			

			//System.out.println(""+ e1.getId() +","+ e1.getIdInvernadero()+ ","+ e1.getFecha()+",    "+e1.getPrecio() + ","+ e1.getStock());

			//System.out.println(""+ e2.getId() +","+ e2.getIdInvernadero()+ ","+ e2.getFecha()+",    "+e2.getPrecio() + ","+ e2.getStock());
			
			if(!equals(e1, e2)) {
			fail("No se ha leido correctamente la entrada");
		}
			
			t.commit();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void D_actualizar() {
		
		EntradaDAO daoEntrada = FactoriaIntegracion.getInstance().getEntradaDAO();
		InvernaderoDAO daoInvernadero = FactoriaIntegracion.getInstance().getInvernaderoDAO();


		try {
			Transaccion t;
			t = crearTransaccion();
			t.start();
			Connection c = (Connection) t.getResource();
			
			// Desactivar las comprobaciones de claves foráneas, luego las vuelvo a activar,
			// solo para poder truncar todas las tablas implicadas
			Statement statement = c.createStatement();
			statement.execute("SET FOREIGN_KEY_CHECKS = 0");

			PreparedStatement ps1 = c.prepareStatement("TRUNCATE linea_factura");
			ps1.executeUpdate();
			PreparedStatement ps3 = c.prepareStatement("TRUNCATE factura");
			ps3.executeUpdate();
			PreparedStatement ps = c.prepareStatement("TRUNCATE entrada");
			ps.executeUpdate();
			PreparedStatement ps2 = c.prepareStatement("TRUNCATE invernadero");
			ps2.executeUpdate();

			ps1.close();
			ps3.close();
			ps.close();
			ps2.close();
			
			// Volver a activar las comprobaciones de claves foráneas
			statement.execute("SET FOREIGN_KEY_CHECKS = 1");
			statement.close(); // Cerrar el Statement

			
			LocalDate localDate = LocalDate.of(2012,12,12);
			Date sqlDate = Date.valueOf(localDate); // Convertimos LocalDate a java.sql.Date

			
			TInvernadero inv = new TInvernadero(1, "inv1", "abono", "artificial", true);
			inv.setId(daoInvernadero.altaInvernadero(inv));

			TEntrada entrada = new TEntrada(1, sqlDate, 12.0f, 12, inv.getId(), true);
			Integer id = daoEntrada.altaEntrada(entrada);
			
			entrada.setId(id);
			entrada.setFecha(java.sql.Date.valueOf("2040-10-10"));
			
			daoEntrada.modificarEntrada(entrada);
			
			if(entrada.getFecha() == sqlDate) {
				fail("No se ha modificado correctamente la entrada");
			}
			
			t.commit();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
