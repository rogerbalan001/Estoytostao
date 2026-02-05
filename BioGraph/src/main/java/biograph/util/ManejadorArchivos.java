/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.util;

import biograph.logic.Grafo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class ManejadorArchivos {

    public void cargarArchivo(File archivo, Grafo grafo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue; // Saltar líneas vacías
                
                // Formato: Origen,Destino,Peso
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
            JOptionPane.showMessageDialog(null, "Grafo cargado exitosamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error leyendo archivo: " + e.getMessage());
        }
    }
}
