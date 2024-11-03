package Presentacion.Fabricante;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.IGUI;
import Presentacion.Controller.Command.Context;
import Presentacion.FactoriaVistas.Evento;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Set;

import javax.swing.JButton;

import Negocio.Fabricante.TFabricante;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GUIListarInformacionFabricantePorSistemasDeRiegoDeUnInvernadero extends JFrame implements IGUI {

	Set<TFabricante> listaFabricantes;
	private JTextField textId;

	public GUIListarInformacionFabricantePorSistemasDeRiegoDeUnInvernadero(Set<TFabricante> listaFabricantes) {
		super("Mostrar Fabricante");
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int ancho = 600;
		int alto = 400;
		int x = (pantalla.width - ancho) / 2;
		int y = (pantalla.height - alto) / 2;
		this.setBounds(x, y, ancho, alto);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initGUI();
	}

	public void initGUI() {
		// Panel principal con GridBagLayout para mayor control sobre la alineaci�n y el
		// centrado
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10); // M�rgenes entre los componentes
		this.setContentPane(mainPanel);

		// Titulo
		gbc.gridwidth = 2; // Toma dos columnas para el t�tulo
		JLabel msgIntro = new JLabel("Introduzca el ID del invernadero", JLabel.CENTER);
		mainPanel.add(msgIntro, gbc);

		// Resetear para los campos
		gbc.gridwidth = 1;
		gbc.gridy = 1;

		// Campo para el ID del sistema de riego
		JLabel labelId = new JLabel("ID:");
		gbc.gridx = 0; // Columna 0
		mainPanel.add(labelId, gbc);
		textId = new JTextField(20);
		gbc.gridx = 1; // Columna 1
		mainPanel.add(textId, gbc);

		// Panel de botones
		JPanel panelBotones = new JPanel();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2; // Los botones ocupar�n dos columnas
		gbc.anchor = GridBagConstraints.CENTER; // Centrar los botones
		mainPanel.add(panelBotones, gbc);

		// Boton de aceptar
		JButton botonAceptar = new JButton("Aceptar");
		botonAceptar.addActionListener(a -> {
			try {
				// Crear un contexto con el evento de mostrar y el ID del sistema
				ApplicationController.getInstance().manageRequest(
						
						new Context(Evento.LISTAR_INFORMACION_FABRICANTES_DE_SISTEMA_DE_RIEGO_DE_UN_INVERNADERO, Integer.parseInt(textId.getText())));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(GUIListarInformacionFabricantePorSistemasDeRiegoDeUnInvernadero.this, "Error en el formato del ID", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		panelBotones.add(botonAceptar);

		// Bot�n de cancelar
		JButton botonCancelar = new JButton("Cancelar");
		botonCancelar.addActionListener(a -> {
			GUIListarInformacionFabricantePorSistemasDeRiegoDeUnInvernadero.this.setVisible(false);
			ApplicationController.getInstance().manageRequest(new Context(Evento.FABRICANTE_VISTA, null));
		});
		panelBotones.add(botonCancelar);

		this.setVisible(true);

	}

	public void actualizar(Context context) {
		if (context.getEvento() == Evento.LISTAR_INFORMACION_FABRICANTES_DE_SISTEMA_DE_RIEGO_DE_UN_INVERNADERO_OK) {
			ApplicationController.getInstance().manageRequest(new Context(Evento.LISTAR_FABRICANTES_VISTA,context.getDatos()));
			dispose();
        } else if (context.getEvento() == Evento.LISTAR_INFORMACION_FABRICANTES_DE_SISTEMA_DE_RIEGO_DE_UN_INVERNADERO_KO) {
        	if(context.getDatos() == null) {
        		JOptionPane.showMessageDialog(this, "El invernadero no existe", "Error", JOptionPane.ERROR_MESSAGE);
        	}
            JOptionPane.showMessageDialog(this, "Error al tratar de listar los Fabricantes", "Error", JOptionPane.ERROR_MESSAGE);
        }
	}
}