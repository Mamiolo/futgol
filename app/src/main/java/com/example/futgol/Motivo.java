package com.example.futgol;

public class Motivo {
    private int idMotivo;
    private String nombre;
    private String motivo;

    public Motivo() {
    }

    public Motivo(int idMotivo, String nombre, String motivo) {
        this.idMotivo = idMotivo;
        this.nombre = nombre;
        this.motivo = motivo;
    }

    public int getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(int idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "Motivo [ID de Motivo=" + idMotivo + ", Nombre=" + nombre + ", Motivo=" + motivo + "]";
    }
}
