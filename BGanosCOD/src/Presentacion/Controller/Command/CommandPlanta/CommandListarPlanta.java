package Presentacion.Controller.Command.CommandPlanta;

import java.util.HashSet;
import java.util.Set;

import Negocio.FactoriaNegocio.FactoriaNegocio;
import Negocio.Planta.*;
import Presentacion.Controller.Command.Command;
import Presentacion.Controller.Command.Context;
import Presentacion.FactoriaVistas.Evento;

public class CommandListarPlanta implements Command {

	public Context execute(Object datos) {
		Set<TPlanta> res = new HashSet<>();
	
		res = FactoriaNegocio.getInstance().getPlantaSA().listarPlanta();

		
		if(res.isEmpty() || res.size() == 0){return new Context(Evento.LISTAR_PLANTAS_KO,res);}
		
		
		return new Context(Evento.LISTAR_PLANTAS_OK, res);
	}
}