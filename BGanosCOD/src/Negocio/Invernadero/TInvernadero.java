/**
 * 
 */
package Negocio.Invernadero;

public class TInvernadero {

	private Integer id;

	private String sustrato;

	private String nombre;

	private String tipo_iluminacion;

	private Boolean activo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSustrato() {
		return sustrato;
	}

	public void setSustrato(String sustrato) {
		this.sustrato = sustrato;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo_iluminacion() {
		return tipo_iluminacion;
	}

	public void setTipo_iluminacion(String tipo_iluminacion) {
		this.tipo_iluminacion = tipo_iluminacion;
	}

	public Boolean isActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}