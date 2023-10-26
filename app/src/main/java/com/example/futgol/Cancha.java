package com.example.futgol;

public class Cancha {
    private int numeroCancha;
    private String estado;

    public Cancha() {
    }

    public Cancha(int numeroCancha, String estado) {
        this.numeroCancha = numeroCancha;
        this.estado = estado;
    }

    public int getNumeroCancha() {
        return numeroCancha;
    }

    public void setNumeroCancha(int numeroCancha) {
        this.numeroCancha = numeroCancha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


}
