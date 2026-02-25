/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.main;

import biograph.gui.VentanaPrincipal; // Cambia esto si a tu ventana en Design le pones otro nombre

public class Main {
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing, ideal para GUIs de NetBeans
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
}

