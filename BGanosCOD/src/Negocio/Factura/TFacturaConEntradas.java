/**
 * 
 */
package Negocio.Factura;

import java.util.Set;


/** 
* <!-- begin-UML-doc -->
* <!-- end-UML-doc -->
* @author airam
* @generated "UML a Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
*/
public class TFacturaConEntradas {
	/** 
	* <!-- begin-UML-doc -->
	* <!-- end-UML-doc -->
	* @generated "UML a Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	*/
	private Set<TLineaFactura> tLineaFactura;
	/** 
	* <!-- begin-UML-doc -->
	* <!-- end-UML-doc -->
	* @generated "UML a Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	*/
	private TFactura tFactura;
	
	
	public Set<TLineaFactura> gettLineaFactura() {
		return tLineaFactura;
	}
	public void incluirLineaEntrada(TLineaFactura TLineaFactura) {
		tLineaFactura.add(TLineaFactura);
	}
	public TFactura gettFactura() {
		return tFactura;
	}
	public void settFactura(TFactura tFactura) {
		this.tFactura = tFactura;
	}
	
	
}