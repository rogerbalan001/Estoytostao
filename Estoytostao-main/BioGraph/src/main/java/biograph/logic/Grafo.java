/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    // ==========================================
    // MÉTODOS BASE DEL TDA GRAFO
    // ==========================================
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
            for(int i = 0; i < vertices.size(); i++){
                vertices.get(i).eliminarAristaConDestino(v);
            }
            // 2. Eliminar el nodo
            vertices.remove(v);
        }
    }

    // Método necesario para poder dibujar los nodos y guardar el archivo
    public Lista<Vertice> getVertices() {
        return vertices;
    }

    // ==========================================
    // 1. CENTRALIDAD DE GRADO (Buscar el Hub)
    // ==========================================
    public Vertice obtenerHub() {
        if (vertices.size() == 0) return null;
        
        Vertice hub = vertices.get(0);
        int maxConexiones = hub.getAdyacencia().size();
        
        for (int i = 1; i < vertices.size(); i++) {
            Vertice actual = vertices.get(i);
            if (actual.getAdyacencia().size() > maxConexiones) {
                hub = actual;
                maxConexiones = actual.getAdyacencia().size();
            }
        }
        return hub;
    }

    // ==========================================
    // 2. DETECCIÓN DE COMPLEJOS (Usando DFS)
    // ==========================================
    public Lista<Lista<Vertice>> obtenerComplejos() {
        Lista<Lista<Vertice>> complejos = new Lista<>();
        Lista<Vertice> visitados = new Lista<>();

        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            if (!contieneVertice(visitados, v)) {
                Lista<Vertice> complejoActual = new Lista<>();
                dfs(v, visitados, complejoActual);
                complejos.add(complejoActual);
            }
        }
        return complejos;
    }

    private void dfs(Vertice v, Lista<Vertice> visitados, Lista<Vertice> complejoActual) {
        visitados.add(v);
        complejoActual.add(v);
        Lista<Arista> ady = v.getAdyacencia();
        for (int i = 0; i < ady.size(); i++) {
            Vertice vecino = ady.get(i).getDestino();
            if (!contieneVertice(visitados, vecino)) {
                dfs(vecino, visitados, complejoActual);
            }
        }
    }

    private boolean contieneVertice(Lista<Vertice> lista, Vertice v) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombre().equals(v.getNombre())) return true;
        }
        return false;
    }

    // ==========================================
    // 3. ALGORITMO DE DIJKSTRA (Ruta más corta)
    // ==========================================
    public Lista<Vertice> dijkstra(String origen, String destino) {
        Vertice vOrigen = buscarVertice(origen);
        Vertice vDestino = buscarVertice(destino);
        if (vOrigen == null || vDestino == null) return null;

        int n = vertices.size();
        int[] distancias = new int[n];
        Vertice[] previos = new Vertice[n];
        boolean[] visitados = new boolean[n];

        // Inicializar
        for (int i = 0; i < n; i++) {
            distancias[i] = Integer.MAX_VALUE;
            previos[i] = null;
            visitados[i] = false;
        }

        int indiceOrigen = obtenerIndice(vOrigen);
        distancias[indiceOrigen] = 0;

        for (int i = 0; i < n; i++) {
            // Buscar vértice con distancia mínima no visitado
            int u = -1;
            int minPath = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!visitados[j] && distancias[j] < minPath) {
                    minPath = distancias[j];
                    u = j;
                }
            }

            if (u == -1) break; // Inalcanzables
            if (vertices.get(u).getNombre().equals(vDestino.getNombre())) break; // Llegamos al destino

            visitados[u] = true;
            Vertice verticeU = vertices.get(u);
            Lista<Arista> ady = verticeU.getAdyacencia();

            for (int j = 0; j < ady.size(); j++) {
                Vertice v = ady.get(j).getDestino();
                int peso = ady.get(j).getPeso();
                int indiceV = obtenerIndice(v);

                if (!visitados[indiceV] && distancias[u] != Integer.MAX_VALUE && distancias[u] + peso < distancias[indiceV]) {
                    distancias[indiceV] = distancias[u] + peso;
                    previos[indiceV] = verticeU;
                }
            }
        }

        // Reconstruir la ruta (se guarda del destino al origen)
        Lista<Vertice> ruta = new Lista<>();
        int indiceActual = obtenerIndice(vDestino);
        if (distancias[indiceActual] == Integer.MAX_VALUE) return ruta; // Retorna vacía si no hay ruta

        Vertice actual = vDestino;
        while (actual != null) {
            ruta.add(actual);
            actual = previos[obtenerIndice(actual)];
        }
        return ruta;
    }

    private int obtenerIndice(Vertice v) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getNombre().equals(v.getNombre())) return i;
        }
        return -1;
    }
}