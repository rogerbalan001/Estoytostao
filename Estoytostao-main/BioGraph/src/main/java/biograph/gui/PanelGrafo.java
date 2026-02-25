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

public class PanelGrafo extends JPanel {

    private Grafo grafo;
    private final Color COLOR_FONDO = new Color(10, 15, 10);
    private final Color COLOR_NODO = new Color(57, 255, 20); // Verde Neón
    private final Color COLOR_ARISTA = new Color(200, 200, 200, 150);

    public PanelGrafo() {
        setBackground(COLOR_FONDO);
    }

    public void actualizarGrafo(Grafo grafo) {
        this.grafo = grafo;
        repaint(); // Vuelve a dibujar el panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Mejorar la calidad del dibujo (Antialiasing)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (grafo == null || grafo.getVertices().size() == 0) {
            g2.setColor(COLOR_NODO);
            g2.drawString("No hay ningún grafo cargado en memoria.", 20, 30);
            return;
        }

        Lista<Vertice> vertices = grafo.getVertices();
        int cantidadNodos = vertices.size();
        
        // Calcular posiciones circulares
        int radio = Math.min(getWidth(), getHeight()) / 2 - 50;
        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        
        int[] x2D = new int[cantidadNodos];
        int[] y2D = new int[cantidadNodos];
        double anguloSeparacion = 2 * Math.PI / cantidadNodos;

        for (int i = 0; i < cantidadNodos; i++) {
            x2D[i] = centroX + (int) (radio * Math.cos(i * anguloSeparacion));
            y2D[i] = centroY + (int) (radio * Math.sin(i * anguloSeparacion));
        }

        // 1. Dibujar las Aristas (líneas)
        g2.setColor(COLOR_ARISTA);
        for (int i = 0; i < cantidadNodos; i++) {
            Vertice vOrigen = vertices.get(i);
            Lista<Arista> adyacentes = vOrigen.getAdyacencia();
            
            for (int j = 0; j < adyacentes.size(); j++) {
                Vertice vDestino = adyacentes.get(j).getDestino();
                int peso = adyacentes.get(j).getPeso();
                
                // Buscar el índice del destino para saber sus coordenadas
                int indexDestino = -1;
                for (int k = 0; k < cantidadNodos; k++) {
                    if (vertices.get(k).getNombre().equals(vDestino.getNombre())) {
                        indexDestino = k;
                        break;
                    }
                }

                if (indexDestino != -1) {
                    g2.drawLine(x2D[i], y2D[i], x2D[indexDestino], y2D[indexDestino]);
                    // Dibujar el peso en medio de la línea
                    int midX = (x2D[i] + x2D[indexDestino]) / 2;
                    int midY = (y2D[i] + y2D[indexDestino]) / 2;
                    g2.setColor(Color.YELLOW);
                    g2.drawString(String.valueOf(peso), midX, midY);
                    g2.setColor(COLOR_ARISTA); // Restaurar color para la siguiente línea
                }
            }
        }

        // 2. Dibujar los Nodos (Círculos y texto)
        int radioNodo = 30;
        for (int i = 0; i < cantidadNodos; i++) {
            g2.setColor(COLOR_FONDO); // Relleno del nodo
            g2.fillOval(x2D[i] - radioNodo/2, y2D[i] - radioNodo/2, radioNodo, radioNodo);
            
            g2.setColor(COLOR_NODO); // Borde del nodo
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x2D[i] - radioNodo/2, y2D[i] - radioNodo/2, radioNodo, radioNodo);
            
            g2.setColor(Color.WHITE); // Texto del nodo
            g2.setFont(new Font("Consolas", Font.BOLD, 14));
            String nombre = vertices.get(i).getNombre();
            // Centrar el texto aproximadamente
            g2.drawString(nombre, x2D[i] - (g2.getFontMetrics().stringWidth(nombre)/2), y2D[i] + 5);
        }
    }
}