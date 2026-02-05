/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.logic;

public class Grafo {
    private Lista<Vertice> vertices;

    public Grafo() {
        this.vertices = new Lista<>();
    }

    public Vertice buscarVertice(String nombre) {
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            if (v.getNombre().equalsIgnoreCase(nombre)) {
                return v;
            }
        }
        return null;
    }

    public void insertarVertice(String nombre) {
        if (buscarVertice(nombre) == null) {
            vertices.add(new Vertice(nombre));
        }
    }

    public void insertarArista(String origen, String destino, int peso) {
        Vertice vOrigen = buscarVertice(origen);
        Vertice vDestino = buscarVertice(destino);

        if (vOrigen == null) {
            insertarVertice(origen);
            vOrigen = buscarVertice(origen);
        }
        if (vDestino == null) {
            insertarVertice(destino);
            vDestino = buscarVertice(destino);
        }

        // Grafo no dirigido: Se conecta en ambos sentidos
        vOrigen.agregarArista(vDestino, peso);
        vDestino.agregarArista(vOrigen, peso);
    }
    
    public void eliminarVertice(String nombre){
        Vertice v = buscarVertice(nombre);
        if(v != null){
            // 1. Eliminar referencias en otros nodos
            for(int i=0; i<vertices.size(); i++){
                vertices.get(i).eliminarAristaConDestino(v);
            }
            // 2. Eliminar el nodo
            vertices.remove(v);
        }
    }

    public Lista<Vertice> getVertices() {
        return vertices;
    }
}