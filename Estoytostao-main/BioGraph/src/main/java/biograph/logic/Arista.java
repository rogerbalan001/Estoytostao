/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.logic;

public class Arista {
    private Vertice destino;
    private int peso;

    public Arista(Vertice destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }
    
    @Override
    public String toString() {
        return "--(" + peso + ")--> " + destino.getNombre();
    }
}