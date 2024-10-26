package Presentacion.SistemaDeRiego;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import Negocio.SistemaDeRiego.TSistemaDeRiego;

import javax.swing.JLabel;
import javax.swing.JPanel;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class GUIModificarSistemaDeRiego extends JFrame implements IGUI {
    private JTextField textId;
    private JTextField textNombre;
    private JTextField textPotenciaRiego;
    private JTextField textCantidadAgua;
    private JTextField textFrecuencia;
    private JTextField textIdFabricante;


    public GUIModificarSistemaDeRiego() {
        super("Modificar Sistema de Riego");
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
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // M�rgenes entre los componentes
        this.setContentPane(mainPanel);

        // T�tulo
        gbc.gridwidth = 2; // Toma dos columnas para el t�tulo
        JLabel msgIntro = new JLabel("Introduzca los nuevos datos del sistema de riego", JLabel.CENTER);
        mainPanel.add(msgIntro, gbc);

        // Campo para el ID
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel labelId = new JLabel("ID:");
        gbc.gridx = 0; // Columna 0
        mainPanel.add(labelId, gbc);
        textId = new JTextField(20);
        gbc.gridx = 1; // Columna 1
        mainPanel.add(textId, gbc);

        // Campo para el nombre
        gbc.gridy = 2;
        JLabel labelNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        mainPanel.add(labelNombre, gbc);
        textNombre = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(textNombre, gbc);

        // Campo para la potencia de riego
        gbc.gridy = 3;
        JLabel labelPotencia = new JLabel("Potencia Riego:");
        gbc.gridx = 0;
        mainPanel.add(labelPotencia, gbc);
        textPotenciaRiego = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(textPotenciaRiego, gbc);

        // Campo para la cantidad de agua
        gbc.gridy = 4;
        JLabel labelCantidadAgua = new JLabel("Cantidad Agua:");
        gbc.gridx = 0;
        mainPanel.add(labelCantidadAgua, gbc);
        textCantidadAgua = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(textCantidadAgua, gbc);

        // Campo para la frecuencia
        gbc.gridy = 5;
        JLabel labelFrecuencia = new JLabel("Frecuencia:");
        gbc.gridx = 0;
        mainPanel.add(labelFrecuencia, gbc);
        textFrecuencia = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(textFrecuencia, gbc);

        // Campo para el ID del fabricante
        gbc.gridy = 6;
        JLabel labelIdFabricante = new JLabel("ID Fabricante:");
        gbc.gridx = 0;
        mainPanel.add(labelIdFabricante, gbc);
        textIdFabricante = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(textIdFabricante, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Los botones ocupar�n dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centrar los botones
        mainPanel.add(panelBotones, gbc);

        // Bot�n de aceptar
        JButton botonAceptar = new JButton("Aceptar");
        botonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer id = Integer.parseInt(textId.getText());
                    String nombre = textNombre.getText();
                    Integer potenciaRiego = Integer.parseInt(textPotenciaRiego.getText());
                    Integer cantidadAgua = Integer.parseInt(textCantidadAgua.getText());
                    Integer frecuencia = Integer.parseInt(textFrecuencia.getText());
                    Integer idFabricante = Integer.parseInt(textIdFabricante.getText());

                    // Crear y configurar el sistema de riego
                    TSistemaDeRiego sistemaDeRiego = new TSistemaDeRiego();
                    sistemaDeRiego.setId(id); // Establecer el ID del sistema a modificar
                    sistemaDeRiego.setNombre(nombre);
                    sistemaDeRiego.setPotenciaRiego(potenciaRiego);
                    sistemaDeRiego.setCantidad_agua(cantidadAgua);
                    sistemaDeRiego.setFrecuencia(frecuencia);
                    sistemaDeRiego.setIdFabricante(idFabricante);

                    ApplicationController.getInstance().manageRequest(new Context(Evento.MODIFICAR_SISTEMA_RIEGO, sistemaDeRiego));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GUIModificarSistemaDeRiego.this, "Error en el formato de los datos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelBotones.add(botonAceptar);

        // Bot�n de cancelar
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIModificarSistemaDeRiego.this.setVisible(false);
                ApplicationController.getInstance().manageRequest(new Context(Evento.SISTEMA_RIEGO_VISTA, null));
            }
        });
        panelBotones.add(botonCancelar);

        this.setVisible(true);
    }

 
    @Override
    public void actualizar(Context context) {
        if (context.getEvento() == Evento.MODIFICAR_SISTEMA_DE_RIEGO_OK) {
            JOptionPane.showMessageDialog(this, "Sistema de riego modificado correctamente", "�xito", JOptionPane.INFORMATION_MESSAGE);
        } else if (context.getEvento() == Evento.MODIFICAR_SISTEMA_DE_RIEGO_KO) {
           
            int resultado = (int) context.getDatos();
            switch (resultado) {
            case -3:
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos requeridos.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case -2:
                JOptionPane.showMessageDialog(this, "Error: Ya existe un sistema de riego con el mismo nombre y est� activo.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case -511:
                JOptionPane.showMessageDialog(this, "Error: El fabricante asociado no est� activo.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case -404:
                JOptionPane.showMessageDialog(this, "Error: El fabricante especificado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Error desconocido al modificar el sistema de riego.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }
}
