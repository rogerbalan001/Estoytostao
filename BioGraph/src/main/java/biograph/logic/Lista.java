/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph.logic;

public class Lista<T> {
    private Nodo<T> pFirst;
    private int size;

    public Lista() {
        this.pFirst = null;
        this.size = 0;
    }

    public void add(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (isEmpty()) {
            pFirst = nuevo;
        } else {
            Nodo<T> aux = pFirst;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) return null;
        Nodo<T> aux = pFirst;
        for (int i = 0; i < index; i++) {
            aux = aux.getSiguiente();
        }
        return aux.getDato();
    }

    public void remove(T dato) {
        if (isEmpty()) return;

        if (pFirst.getDato().equals(dato)) {
            pFirst = pFirst.getSiguiente();
            size--;
            return;
        }

        Nodo<T> aux = pFirst;
        while (aux.getSiguiente() != null) {
            if (aux.getSiguiente().getDato().equals(dato)) {
                aux.setSiguiente(aux.getSiguiente().getSiguiente());
                size--;
                return;
            }
            aux = aux.getSiguiente();
        }
    }

    public boolean isEmpty() {
        return pFirst == null;
    }

    public int size() {
        return size;
    }
}
