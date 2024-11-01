package Negocio.SistemaDeRiego;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.BeforeClass;
import org.junit.Test;

import Negocio.Fabricante.FabricanteSA;
import Negocio.Fabricante.TFabricante;
import Negocio.FactoriaNegocio.FactoriaNegocio;
import Negocio.Invernadero.InvernaderoSA;

public class SistemaDeRiegoSATest {
	
	private static SistemaDeRiegoSA sistRiegoSA;
	private static FabricanteSA fabricanteSA;
	private static InvernaderoSA invernaderoSA;
	
	
	@BeforeClass
	public static void beforeClass(){
		sistRiegoSA = FactoriaNegocio.getInstance().getSistemaDeRiegoSA();
		fabricanteSA = FactoriaNegocio.getInstance().getFabricanteSA();
		invernaderoSA = FactoriaNegocio.getInstance().getInvernaderoSA();
	}
	
	private boolean equals(TSistemaDeRiego s1, TSistemaDeRiego s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.getId().equals(s2.getId()) &&
               s1.getNombre().equals(s2.getNombre()) &&
               s1.getPotenciaRiego().equals(s2.getPotenciaRiego()) &&
               s1.getCantidad_agua().equals(s2.getCantidad_agua()) &&
               s1.getFrecuencia().equals(s2.getFrecuencia()) &&
               s1.getActivo().equals(s2.getActivo()) &&
               s1.getIdFabricante().equals(s2.getIdFabricante());
    }
	
	private TSistemaDeRiego getTSistemaDeRiego() {
        return new TSistemaDeRiego(getNumRandom(), getNameRandom(), getNumRandom(), getNumRandom(), getNumRandom(), true, getNumRandom());
    }

	private TFabricante getTFabricante() {
      //  return new TFabricante(getNumRandom(), getNameRandom(), getNumRandom(), getNumRandom(), getNumRandom(), true, getNumRandom());
		return null;
	}

	private String getNameRandom() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder nombreAleatorio = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			int index = random.nextInt(caracteres.length());
			nombreAleatorio.append(caracteres.charAt(index));
		}
		return nombreAleatorio.toString();
	}

	private int getNumRandom(){
		return ThreadLocalRandom.current().nextInt(0, 10000 + 1);
	}
	
	@Test
	public void altaSistemaDeRiegoFabricanteActivo() {
		
		try {
			TFabricante fabricante = getTFabricante();
			int idFabricante = fabricanteSA.altaFabricante(fabricante);
			TSistemaDeRiego tSistemaRiego = getTSistemaDeRiego();
			tSistemaRiego.setIdFabricante(idFabricante);
			int idSistemaRiego = sistRiegoSA.altaSisRiego(tSistemaRiego);
			if(idSistemaRiego < 0){
				fail("Error: altaSisRiego() debería retornar ID > 0");
			}
		} catch (Exception e) {
			fail("Excepcion");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void altaSistemaDeRiegoFabricanteNOActivo() {
		
		try {
			TFabricante fabricante = getTFabricante();
			int idFabricante = fabricanteSA.altaFabricante(fabricante);
			fabricante.setActivo(false);
			TSistemaDeRiego tSistemaRiego = getTSistemaDeRiego();
			tSistemaRiego.setIdFabricante(idFabricante);
			int idSistemaRiego = sistRiegoSA.altaSisRiego(tSistemaRiego);
			if(idSistemaRiego > 0){
				fail("Error: altaSisRiego() debería retornar ID < 0 ya que fabricante inactivo");
			}
		} catch (Exception e) {
			fail("Excepcion");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void bajaSistemaDeRiego(){
		
		try{
			TSistemaDeRiego tSistemaRiego = getTSistemaDeRiego();
			int idSistemaRiego = sistRiegoSA.altaSisRiego(tSistemaRiego);
			int res = sistRiegoSA.bajaSisRiego(idSistemaRiego);
			if(res < 0){
				fail("Error: bajaSistemaDeRiego() no devuelve Id > 0");
			}
		}catch (Exception e) {
			fail("Excepción");
			e.printStackTrace();
		}	
	}
	
	@Test
	public void bajaSistemaDeRiegoInexistente(){
		
		try{
			int idSistemaRiego = 999999;
			int res = sistRiegoSA.bajaSisRiego(idSistemaRiego);
			if(res != -404){
				fail("Error: bajaSistemaDeRiego() debe dar error -404");
			}
		}catch (Exception e) {
			fail("Excepción");
			e.printStackTrace();
		}	
	}
	
	@Test
	public void bajaSistemaDeRiegoInactivo(){
		
		try{
			TSistemaDeRiego tSistemaRiego = getTSistemaDeRiego();
			int idSistemaRiego = sistRiegoSA.altaSisRiego(tSistemaRiego);
			int res = sistRiegoSA.bajaSisRiego(idSistemaRiego);
			if(res < 0){
				fail("Error: bajaSistemaDeRiego() no devuelve Id > 0");
			}
			int res2 =  sistRiegoSA.bajaSisRiego(idSistemaRiego);
			if(res2 != -2){
				fail("Error: bajaSistemaDeRiego() debe dar error -2");
			}
		}catch (Exception e) {
			fail("Excepción");
			e.printStackTrace();
		}	
	}
	
	@Test
	public void mostrarSistemaDeRiego(){
		try{
			TSistemaDeRiego tSistemaRiego = getTSistemaDeRiego();
			int idSistemaRiego = sistRiegoSA.altaSisRiego(tSistemaRiego);
			TSistemaDeRiego sistRes = sistRiegoSA.mostrarSisRiego(idSistemaRiego);
			if(sistRes.getId() != idSistemaRiego){
				fail("Error debería mostrar el sistema de riego correcto");
			}	
		}catch (Exception e) {
			fail("Excepción");
			e.printStackTrace();
		}			
	}
	
	@Test
	public void modificarSistemaDeRiego(){
		try{
			TSistemaDeRiego tSistemaRiego = getTSistemaDeRiego();
			@SuppressWarnings("unused")
			int idSistemaRiego = sistRiegoSA.altaSisRiego(tSistemaRiego);
			tSistemaRiego.setCantidad_agua(getNumRandom());
			int res = sistRiegoSA.modificarSisRiego(tSistemaRiego);
			if(res < 0){
				fail("Error al Modificar " + res );	
			}
			
		}catch (Exception e) {
			fail("Excepción");
			e.printStackTrace();
		}		
	}
	
	
	@Test
	public void listarSistemaDeRiego(){
		
		try{
			TSistemaDeRiego tSistemaRiego = getTSistemaDeRiego();
			TSistemaDeRiego tSistemaRiego2 = getTSistemaDeRiego();
	
			int idSistemaRiego = sistRiegoSA.altaSisRiego(tSistemaRiego);
			int idSistemaRiego2 = sistRiegoSA.altaSisRiego(tSistemaRiego2);
	            
	        Set<TSistemaDeRiego> sistemasDeRiego = sistRiegoSA.listarSisRiego();
		
	        boolean foundtSistemaRiego = false;
	        boolean foundtSistemaRiego2 = false;
	
	        for (TSistemaDeRiego sistema : sistemasDeRiego) {
	            if (sistema.getId() == idSistemaRiego) {
	            	foundtSistemaRiego = true;
	            } else if (sistema.getId() == idSistemaRiego2) {
	            	foundtSistemaRiego2 = true;
	            }
	        }
	        if (!foundtSistemaRiego || !foundtSistemaRiego2) {
	            fail("Error: No listo todos los sistemas");
	        }
		}catch (Exception e) {
	        fail("Excepción");
	        e.printStackTrace();
	    }
		
	}
	

}
