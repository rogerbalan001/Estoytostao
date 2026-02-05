/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.main;

import biograph.gui.PantallaCarga;

public class Main {
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing
        // Nota: Para Splash Screens, a veces es mejor manejar el hilo directamente
        // pero esta forma es segura y estable.
        
        PantallaCarga splash = new PantallaCarga();
        
        // Ejecutamos la animación en un hilo separado para que la interfaz se pinte bien
        new Thread(() -> {
            splash.animar();
        }).start();
    }
}
