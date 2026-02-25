/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.util;

import biograph.logic.Arista;
import biograph.logic.Grafo;
import biograph.logic.Lista;
import biograph.logic.Vertice;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

public class ManejadorArchivos {

    // Método para LEER el archivo e insertarlo en el Grafo
    public void cargarArchivo(File archivo, Grafo grafo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String origen = partes[0].trim();
                    String destino = partes[1].trim();
                    try {
                        int peso = Integer.parseInt(partes[2].trim());
                        grafo.insertarArista(origen, destino, peso);
                    } catch (NumberFormatException e) {
                        System.out.println("Peso inválido en línea: " + linea);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Grafo cargado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error leyendo archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para GUARDAR el grafo en un archivo de texto
    public void guardarArchivo(File archivo, Grafo grafo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            Lista<Vertice> vertices = grafo.getVertices();
            
            for (int i = 0; i < vertices.size(); i++) {
                Vertice v = vertices.get(i);
                Lista<Arista> adyacencia = v.getAdyacencia();
                
                for (int j = 0; j < adyacencia.size(); j++) {
                    Arista a = adyacencia.get(j);
                    // Como el grafo es no dirigido, solo guardamos si el nombre del origen es "menor" al del destino alfabéticamente
                    // Así evitamos escribir P1,P2,10 y luego P2,P1,10
                    if (v.getNombre().compareTo(a.getDestino().getNombre()) < 0) {
                        pw.println(v.getNombre() + "," + a.getDestino().getNombre() + "," + a.getPeso());
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Repositorio actualizado correctamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}