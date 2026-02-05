/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.gui;

import biograph.logic.Arista;
import biograph.logic.Grafo;
import biograph.logic.Lista;
import biograph.logic.Vertice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelGrafo extends JPanel implements MouseListener, MouseMotionListener {

    private Grafo grafo;
    private int cantidadNodos;
    
    // Coordenadas 3D Originales (El modelo estático)
    private double[] x3D, y3D, z3D;
    // Coordenadas 2D Proyectadas
    private int[] x2D, y2D;
    private float[] escalas; 
    private String[] nombres;

    // --- VARIABLES DE CONTROL DE CÁMARA ---
    private double anguloY = 0; // Rotación Horizontal
    private double anguloX = 0; // Rotación Vertical
    
    private int prevMouseX, prevMouseY; // Para calcular el arrastre
    private final double SENSIBILIDAD = 0.01; // Qué tan rápido gira

    // Configuración Visual
    private final Color COLOR_FONDO = new Color(10, 15, 10);
    private final Color COLOR_NODO = new Color(0, 255, 100);    
    private final Color COLOR_ARISTA = new Color(0, 100, 50);   
    
    public PanelGrafo() {
        this.setBackground(COLOR_FONDO);
        this.grafo = null;
        
        // ACTIVAMOS EL MOUSE
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void actualizarGrafo(Grafo nuevoGrafo) {
        this.grafo = nuevoGrafo;
        Lista<Vertice> vertices = grafo.getVertices();
        this.cantidadNodos = vertices.size();

        if (cantidadNodos > 0) {
            x3D = new double[cantidadNodos];
            y3D = new double[cantidadNodos];
            z3D = new double[cantidadNodos];
            
            x2D = new int[cantidadNodos];
            y2D = new int[cantidadNodos];
            escalas = new float[cantidadNodos];
            nombres = new String[cantidadNodos];

            // DISTRIBUCIÓN ESFÉRICA (Fibonacci Sphere)
            double phi = Math.PI * (3. - Math.sqrt(5.));
            int radio = 250; 

            for (int i = 0; i < cantidadNodos; i++) {
                double y = 1 - (i / (double) (cantidadNodos - 1)) * 2;
                double radius = Math.sqrt(1 - y * y);
                double theta = phi * i;

                x3D[i] = Math.cos(theta) * radius * radio;
                y3D[i] = y * radio;
                z3D[i] = Math.sin(theta) * radius * radio;
                
                nombres[i] = vertices.get(i).getNombre();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (grafo == null || cantidadNodos == 0) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Consolas", Font.PLAIN, 14));
            g2.drawString("SISTEMA EN ESPERA... CARGUE DATOS", getWidth()/2 - 100, getHeight()/2);
            g2.drawString("(Arrastre el mouse para rotar)", getWidth()/2 - 90, getHeight()/2 + 20);
            return;
        }

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int focalLength = 400; 

        // 1. CÁLCULO MATEMÁTICO DE ROTACIÓN (Matriz de Rotación Básica)
        double cosY = Math.cos(anguloY);
        double sinY = Math.sin(anguloY);
        double cosX = Math.cos(anguloX);
        double sinX = Math.sin(anguloX);

        for (int i = 0; i < cantidadNodos; i++) {
            double x = x3D[i];
            double y = y3D[i];
            double z = z3D[i];

            // Rotación Eje Y (Izquierda/Derecha)
            double x1 = x * cosY - z * sinY;
            double z1 = x * sinY + z * cosY;

            // Rotación Eje X (Arriba/Abajo)
            double y2 = y * cosX - z1 * sinX;
            double z2 = y * sinX + z1 * cosX; // Profundidad final

            // Proyección 3D -> 2D
            double dist = focalLength + 300 + z2; 
            double scale = focalLength / dist;
            
            x2D[i] = (int) (cx + x1 * scale);
            y2D[i] = (int) (cy + y2 * scale);
            escalas[i] = (float) scale;
        }

        // 2. DIBUJAR ARISTAS (Con transparencia por profundidad)
        Lista<Vertice> vertices = grafo.getVertices();
        
        for (int i = 0; i < cantidadNodos; i++) {
            Vertice v = vertices.get(i);
            Lista<Arista> ady = v.getAdyacencia();
            
            for (int k = 0; k < ady.size(); k++) {
                String destName = ady.get(k).getDestino().getNombre();
                int idxDest = -1;
                // Búsqueda simple
                for(int m=0; m<cantidadNodos; m++) {
                    if(nombres[m].equals(destName)) { idxDest = m; break; }
                }

                if(idxDest != -1) {
                    float prof = (escalas[i] + escalas[idxDest]) / 2;
                    int alpha = (int) (100 * (prof * 0.8));
                    if(alpha > 255) alpha = 255;
                    if(alpha < 20) alpha = 20;

                    g2.setColor(new Color(COLOR_ARISTA.getRed(), COLOR_ARISTA.getGreen(), COLOR_ARISTA.getBlue(), alpha));
                    g2.setStroke(new BasicStroke(1));
                    g2.drawLine(x2D[i], y2D[i], x2D[idxDest], y2D[idxDest]);
                }
            }
        }

        // 3. DIBUJAR NODOS
        for (int i = 0; i < cantidadNodos; i++) {
            float s = escalas[i];
            int radio = (int) (30 * s); 
            
            int alpha = (int) (255 * s); 
            if(alpha > 255) alpha = 255; 
            if(alpha < 50) alpha = 50;

            Color colorActual = new Color(COLOR_NODO.getRed(), COLOR_NODO.getGreen(), COLOR_NODO.getBlue(), alpha);

            // Relleno
            g2.setColor(new Color(0, 50, 0, alpha));
            g2.fillOval(x2D[i] - radio/2, y2D[i] - radio/2, radio, radio);
            
            // Borde
            g2.setColor(colorActual);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x2D[i] - radio/2, y2D[i] - radio/2, radio, radio);

            // Texto
            if (s > 0.6) {
                g2.setFont(new Font("Consolas", Font.BOLD, (int)(12 * s)));
                g2.setColor(Color.WHITE);
                g2.drawString(nombres[i], x2D[i] - 5, y2D[i] + 5);
            }
        }
    }

    // --- EVENTOS DEL MOUSE ---

    @Override
    public void mousePressed(MouseEvent e) {
        // Guardamos dónde hiciste clic para empezar a calcular la diferencia
        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Calculamos cuánto se movió el mouse desde la última posición
        int dx = e.getX() - prevMouseX;
        int dy = e.getY() - prevMouseY;

        // Actualizamos los ángulos (Eje Y mueve horizontal, Eje X mueve vertical)
        anguloY += dx * SENSIBILIDAD;
        anguloX += dy * SENSIBILIDAD;

        // Guardamos la posición actual para el siguiente ciclo
        prevMouseX = e.getX();
        prevMouseY = e.getY();

        // Ordenamos repintar la pantalla con los nuevos ángulos
        repaint();
    }

    // Métodos obligatorios de la interfaz que no usaremos pero deben estar vacíos
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}