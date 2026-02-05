/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.gui;

import biograph.logic.Grafo;
import biograph.util.ManejadorArchivos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class VentanaPrincipal extends JFrame {
    
    private Grafo grafo;
    private ManejadorArchivos manejador;
    
    // Componentes Gráficos
    private PanelGrafo panelVisualizador; // <--- NUESTRO NUEVO COMPONENTE
    private JLabel lblEstado;
    
    // Colores
    private final Color COLOR_FONDO = new Color(10, 15, 10);
    private final Color COLOR_VERDE_NEON = new Color(57, 255, 20);

    public VentanaPrincipal() {
        grafo = new Grafo();
        manejador = new ManejadorArchivos();

        // Configuración ventana
        setTitle("SYSTEM: BioGraph // VISUALIZER MODULE");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_FONDO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titulo = new JLabel("RED DE INTERACCIÓN PROTEICA");
        titulo.setFont(new Font("Consolas", Font.BOLD, 20));
        titulo.setForeground(COLOR_VERDE_NEON);
        
        lblEstado = new JLabel("[ SISTEMA ONLINE ]");
        lblEstado.setFont(new Font("Consolas", Font.PLAIN, 12));
        lblEstado.setForeground(Color.GRAY);
        
        panelSuperior.add(titulo, BorderLayout.WEST);
        panelSuperior.add(lblEstado, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // --- CENTRO: VISUALIZADOR GRÁFICO ---
        panelVisualizador = new PanelGrafo();
        // Le ponemos un borde fino verde
        panelVisualizador.setBorder(BorderFactory.createLineBorder(new Color(0, 60, 0), 1));
        add(panelVisualizador, BorderLayout.CENTER);

        // --- PIE DE PÁGINA: BOTONES ---
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        JButton btnCargar = crearBoton("CARGAR DATASET");
        JButton btnRefrescar = crearBoton("CENTRAR VISTA");

        panelBotones.add(btnCargar);
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.SOUTH);

        // --- ACCIONES ---
        btnCargar.addActionListener(e -> cargarArchivo());
        
        btnRefrescar.addActionListener(e -> {
            if(grafo.getVertices().size() > 0){
                panelVisualizador.actualizarGrafo(grafo);
                lblEstado.setText(">>> VISTA RECALCULADA");
            }
        });
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Consolas", Font.BOLD, 14));
        btn.setForeground(COLOR_FONDO);
        btn.setBackground(COLOR_VERDE_NEON);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover simple
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_VERDE_NEON);
            }
        });
        return btn;
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);
        
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            // Reiniciar grafo
            grafo = new Grafo(); 
            manejador.cargarArchivo(archivo, grafo);
            
            // Actualizar la vista gráfica
            panelVisualizador.actualizarGrafo(grafo);
            
            lblEstado.setText(">>> CARGA EXITOSA: " + grafo.getVertices().size() + " PROTEÍNAS DETECTADAS");
        }
    }
}