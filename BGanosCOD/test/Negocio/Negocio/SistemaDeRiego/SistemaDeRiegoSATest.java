package Negocio.SistemaDeRiego;

import static org.junit.Assert.*;

import java.util.Random;
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
			if(idSistemaRiego<0){
				fail("Error: altaEspecie() debería retornar ID > 0");
			}
		} catch (Exception e) {
			fail("Excepci�n");
			e.printStackTrace();
		}
		
	}

}
