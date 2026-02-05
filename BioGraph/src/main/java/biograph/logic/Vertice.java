/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.logic;

public class Vertice {
    private String nombre;
    private Lista<Arista> adyacencia;

    public Vertice(String nombre) {
        this.nombre = nombre;
        this.adyacencia = new Lista<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarArista(Vertice destino, int peso) {
        // Validación simple para no repetir aristas al mismo destino
        for(int i=0; i < adyacencia.size(); i++){
            if(adyacencia.get(i).getDestino().getNombre().equals(destino.getNombre())){
                return; // Ya existe
            }
        }
        adyacencia.add(new Arista(destino, peso));
    }
    
    public void eliminarAristaConDestino(Vertice destino){
        Arista aEliminar = null;
        for(int i=0; i < adyacencia.size(); i++){
            if(adyacencia.get(i).getDestino().equals(destino)){
                aEliminar = adyacencia.get(i);
                break;
            }
        }
        if(aEliminar != null){
            adyacencia.remove(aEliminar);
        }
    }

    public Lista<Arista> getAdyacencia() {
        return adyacencia;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Vertice){
            return this.nombre.equals(((Vertice)obj).nombre);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}