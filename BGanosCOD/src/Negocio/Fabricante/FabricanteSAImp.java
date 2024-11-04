package Negocio.Fabricante;

import java.util.LinkedHashSet;
import java.util.Set;

import Integracion.Fabricante.FabricanteDAO;
import Integracion.FactoriaIntegracion.FactoriaIntegracion;
import Integracion.FactoriaQuery.FactoriaQuery;
import Integracion.FactoriaQuery.Query;
import Integracion.Invernadero.InvernaderoDAO;
import Integracion.Transaction.Transaccion;
import Integracion.Transaction.TransaccionManager;
import Negocio.Invernadero.TInvernadero;

public class FabricanteSAImp implements FabricanteSA {

	public Integer altaFabricante(TFabricante fabricante) {
		int ret = -1;

		if (fabricante.getNombre().isEmpty() || fabricante.getCodFabricante().isEmpty()
				|| fabricante.getTelefono().isEmpty())// faltan datos
			return -2;

		if (fabricante.getClass() == TFabricanteLocal.class && (((TFabricanteLocal) fabricante).getImpuesto() < 0
				|| ((TFabricanteLocal) fabricante).getSubvencion() < 0))// faltan datos local
			return -2;

		if (fabricante.getClass() == TFabricanteExtranjero.class
				&& (((TFabricanteExtranjero) fabricante).getAranceles() < 0
						|| ((TFabricanteExtranjero) fabricante).getPaisDeOrigen().isEmpty()))// faltan datos extranjero
			return -2;

		if (!comprobarTelefono(fabricante.getTelefono())) {
			return -4;
		}

		try {
			TransaccionManager tm = TransaccionManager.getInstance();
			Transaccion t = tm.newTransaccion();
			t.start();
			FactoriaIntegracion fi = FactoriaIntegracion.getInstance();
			FabricanteDAO fd = fi.getFabricanteDAO();

			TFabricante tfa = fd.leerPorCodFabricante(fabricante.getCodFabricante());

			if (tfa == null) { // no existe por lo que le damos de alta
				ret = fd.altaFabricante(fabricante);
				t.commit();
			} else if (!tfa.getActivo()) { // esta desactivado, lo activamos
				fabricante.setActivo(true);
				fabricante.setId(tfa.getId());
				ret = fd.modificarFabricante(fabricante);
				t.commit();
			} else { // ya existe y esta activado
				ret = -3;
				t.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	public Integer bajaFabricante(Integer idFabricante) {
		int ret = -1;

		try {
			TransaccionManager tm = TransaccionManager.getInstance();
			Transaccion t = tm.newTransaccion();
			t.start();

			FactoriaIntegracion fi = FactoriaIntegracion.getInstance();
			FabricanteDAO fd = fi.getFabricanteDAO();
			TFabricante tf = fd.mostrarFabricantePorId(idFabricante);

			if (tf != null) {
				if (tf.getActivo()) {
					ret = fd.bajaFabricante(idFabricante);
					t.commit();
				} else {
					ret = -2; // No esta activo
					t.rollback();
				}
			} else {
				ret = -3; // No existe
				t.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	public Integer modificarFabricante(TFabricante fabricante) {
		int ret = -1;

		if (fabricante.getNombre().isEmpty() || fabricante.getCodFabricante().isEmpty()
				|| fabricante.getTelefono().isEmpty())// faltan datos
			return -2;

		if ((fabricante instanceof TFabricanteLocal) && (((TFabricanteLocal) fabricante).getImpuesto() < 0
				|| ((TFabricanteLocal) fabricante).getSubvencion() < 0))// faltan datos local
			return -2;

		if ((fabricante instanceof TFabricanteExtranjero) && (((TFabricanteExtranjero) fabricante).getAranceles() < 0
				|| ((TFabricanteExtranjero) fabricante).getPaisDeOrigen().isEmpty()))// faltan datos extranjero
			return -2;

		if (!comprobarTelefono(fabricante.getTelefono()))
			return -6;

		try {
			TransaccionManager tm = TransaccionManager.getInstance();
			Transaccion t = tm.newTransaccion();
			t.start();

			FactoriaIntegracion fi = FactoriaIntegracion.getInstance();
			FabricanteDAO fd = fi.getFabricanteDAO();
			TFabricante tfa = fd.mostrarFabricantePorId(fabricante.getId());

			if (tfa == null) { // no existe
				ret = -3;
				t.rollback();
			} else if (tfa instanceof TFabricanteExtranjero && fabricante instanceof TFabricanteLocal) { // el de la BD
																											// es local
																											// y
																											// intentamos
																											// que se
																											// extranjero
				ret = -4;
				t.rollback();
			} else if (fabricante instanceof TFabricanteExtranjero && tfa instanceof TFabricanteLocal) { // el de la BD
																											// es
																											// extranjero
																											// y
																											// intentamos
																											// que se
																											// local
				ret = -5;
				t.rollback();
			} else { // lo modificamos
				ret = fd.modificarFabricante(fabricante);
				t.commit();
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	public TFabricante mostrarFabricantePorId(Integer id) {
		TFabricante tf = null;
		try {
			TransaccionManager tm = TransaccionManager.getInstance();
			Transaccion t = tm.newTransaccion();
			t.start();

			FactoriaIntegracion fi = FactoriaIntegracion.getInstance();
			FabricanteDAO fd = fi.getFabricanteDAO();

			tf = fd.mostrarFabricantePorId(id);

			if (tf != null) {
				t.commit();
			} else {
				tf = new TFabricante();
				tf.setId(id);// No encontrado dejamos el id para sacar el mensaje de error
				t.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tf;
	}

	public Set<TFabricante> listarFabricantes() {

		Set<TFabricante> listaFab = new LinkedHashSet<>();

		try {

			TransaccionManager tm = TransaccionManager.getInstance();
			Transaccion t = tm.newTransaccion();
			t.start();

			FactoriaIntegracion fi = FactoriaIntegracion.getInstance();
			FabricanteDAO fd = fi.getFabricanteDAO();

			listaFab = fd.listarFabricantes();
			t.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaFab;
	}

	public Set<TFabricante> listarFabricantesLocales() {

		Set<TFabricante> listaFab = new LinkedHashSet<>();

		try {

			TransaccionManager tm = TransaccionManager.getInstance();
			Transaccion t = tm.newTransaccion();
			t.start();

			FactoriaIntegracion fi = FactoriaIntegracion.getInstance();
			FabricanteDAO fd = fi.getFabricanteDAO();

			listaFab = fd.listarFabricantesLocales();
			t.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaFab;
	}

	public Set<TFabricante> listarFabricantesExtranjeros() {
		Set<TFabricante> listaFab = new LinkedHashSet<>();

		try {

			TransaccionManager tm = TransaccionManager.getInstance();
			Transaccion t = tm.newTransaccion();
			t.start();

			FactoriaIntegracion fi = FactoriaIntegracion.getInstance();
			FabricanteDAO fd = fi.getFabricanteDAO();

			listaFab = fd.listarFabricantesExtranjeros();
			t.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaFab;
	}

	@SuppressWarnings("unchecked")
	public Set<TFabricante> listarFabricantesPorInvernadero(Integer id) {
		Set<TFabricante> listaFab = new LinkedHashSet<>();

		try {

			TransaccionManager tm = TransaccionManager.getInstance();
			Transaccion t = tm.newTransaccion();
			t.start();

			FactoriaIntegracion fi = FactoriaIntegracion.getInstance();
			InvernaderoDAO finv = fi.getInvernaderoDAO();

			TInvernadero ti = finv.mostrarInvernaderoPorID(id);

			if (ti != null) { // el invernadero existe
				FactoriaQuery fq = FactoriaQuery.getInstance();
				Query q = fq.getNewQuery("ListarInformacionFabricantePorSistemasDeRiegoDeUnInvernadero");
				
				listaFab = (Set<TFabricante>) q.execute(id);
				t.commit();
			} else { // el invernadero no existe
				t.rollback();
				return null;
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaFab;
	}

	private boolean comprobarTelefono(String telefono) {
		return telefono.matches("\\d{9}");
	}
}