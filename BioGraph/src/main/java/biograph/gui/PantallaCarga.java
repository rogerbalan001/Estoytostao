/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class PantallaCarga extends JWindow {

    private JProgressBar barraProgreso;
    private JTextArea textoConsola;
    private JLabel lblPorcentaje;

    // Colores Hacker
    private final Color COLOR_FONDO = new Color(0, 10, 0); // Casi negro
    private final Color COLOR_NEON = new Color(57, 255, 20); // Verde Matrix
    private final Font FUENTE_MATRIX = new Font("Consolas", Font.PLAIN, 12);
    private final Font FUENTE_NOMBRES = new Font("Courier New", Font.BOLD, 16);

    // Nombres del Equipo
    private final String[] DEVELOPERS = {
        "ROGER BALAN",
        "CHRISTIAN GARCIA",
        "ANGELYNA VILLAROEL"
    };

    // Frases falsas de carga para el efecto visual
    private final String[] LOGS = {
        "Initialising Bio-Kernel v4.2...",
        "Allocating memory segments...",
        "Loading protein structures...",
        "Parsing CSV logic modules...",
        "Verifying user permissions...",
        "Access granted: ROOT",
        "Connecting to Neural Network...",
        "Decrypting DNA sequences...",
        "Optimising rendering engine...",
        "Compiling vertex shaders...",
        "Mounting file system...",
        "Checking integrity...",
        "SYSTEM READY."
    };

    public PantallaCarga() {
        // Configuración básica
        setSize(600, 400);
        setLocationRelativeTo(null); // Centrar
        setLayout(new BorderLayout());
        
        // Fondo General
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(COLOR_NEON, 2)); // Borde Neón
        add(panelPrincipal);

        // --- CENTRO: NOMBRES DEL EQUIPO ---
        JPanel panelNombres = new JPanel();
        panelNombres.setLayout(new BoxLayout(panelNombres, BoxLayout.Y_AXIS));
        panelNombres.setBackground(COLOR_FONDO);
        panelNombres.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JLabel titulo = new JLabel("BIOGRAPH SYSTEMS ©");
        titulo.setFont(new Font("Impact", Font.PLAIN, 28));
        titulo.setForeground(COLOR_NEON);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitulo = new JLabel("AUTHORIZED PERSONNEL ONLY:");
        subtitulo.setFont(new Font("Consolas", Font.ITALIC, 12));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelNombres.add(titulo);
        panelNombres.add(Box.createVerticalStrut(20)); // Espacio
        panelNombres.add(subtitulo);
        panelNombres.add(Box.createVerticalStrut(10)); // Espacio

        // Agregar los nombres
        for (String nombre : DEVELOPERS) {
            JLabel lblNombre = new JLabel("> " + nombre);
            lblNombre.setFont(FUENTE_NOMBRES);
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelNombres.add(lblNombre);
            panelNombres.add(Box.createVerticalStrut(5));
        }
        
        panelPrincipal.add(panelNombres, BorderLayout.CENTER);

        // --- SUR: CONSOLA Y BARRA ---
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(COLOR_FONDO);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Área de texto que simula código corriendo
        textoConsola = new JTextArea(5, 40);
        textoConsola.setFont(FUENTE_MATRIX);
        textoConsola.setForeground(COLOR_NEON);
        textoConsola.setBackground(new Color(10, 10, 10));
        textoConsola.setEditable(false);
        textoConsola.setBorder(null);
        panelSur.add(textoConsola, BorderLayout.NORTH);

        // Barra de progreso
        barraProgreso = new JProgressBar();
        barraProgreso.setForeground(COLOR_NEON);
        barraProgreso.setBackground(new Color(20, 20, 20));
        barraProgreso.setBorderPainted(false);
        barraProgreso.setPreferredSize(new Dimension(100, 10)); // Fina
        panelSur.add(barraProgreso, BorderLayout.CENTER);

        // Porcentaje
        lblPorcentaje = new JLabel("0%");
        lblPorcentaje.setForeground(COLOR_NEON);
        lblPorcentaje.setFont(FUENTE_MATRIX);
        lblPorcentaje.setHorizontalAlignment(SwingConstants.RIGHT);
        panelSur.add(lblPorcentaje, BorderLayout.SOUTH);

        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
    }

    // Método para animar la carga
    public void animar() {
        setVisible(true);
        Random rand = new Random();
        
        // Simulación de carga (0 a 100%)
        try {
            for (int i = 0; i <= 100; i++) {
                // Actualizar barra y porcentaje
                barraProgreso.setValue(i);
                lblPorcentaje.setText("SYSTEM LOADING... " + i + "%");

                // Añadir texto aleatorio a la consola cada cierto tiempo
                if (i % 8 == 0) { // Cada 8% agrega una línea
                    String log = LOGS[rand.nextInt(LOGS.length)];
                    textoConsola.append("> " + log + "\n");
                    // Auto-scroll hacia abajo
                    textoConsola.setCaretPosition(textoConsola.getDocument().getLength());
                }

                // Velocidad variable para realismo (rápido y lento)
                int velocidad = rand.nextInt(50) + 10; 
                if (i > 80) velocidad += 60; // Se pone lento al final (efecto tensión)
                
                Thread.sleep(velocidad);
            }
            Thread.sleep(500); // Pausa dramática al 100%
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // CERRAR CARGA Y ABRIR PROGRAMA
        this.dispose(); // Destruye esta ventana
        new VentanaPrincipal().setVisible(true); // Abre la principal
    }
}