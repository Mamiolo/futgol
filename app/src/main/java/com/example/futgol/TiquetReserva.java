package com.example.futgol;

public class TiquetReserva {
    private long idTiquet; // Cambiado a long
    private String fecha;
    private String hora;
    private String nombre;
    private String telefono;
    private String numeroDocumento;
    private Cancha cancha;

    public TiquetReserva(long idTiquet, String fecha, String hora, String nombre, String telefono, String numeroDocumento, Cancha cancha) {
        this.idTiquet = idTiquet;
        this.fecha = fecha;
        this.hora = hora;
        this.nombre = nombre;
        this.telefono = telefono;
        this.numeroDocumento = numeroDocumento;
        this.cancha = cancha;
    }


    public long getIdTiquet() {
        return idTiquet;
    }


    public void setIdTiquet(long idTiquet) {
        this.idTiquet = idTiquet;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public String toString() {
        return "TiquetReserva{" +
                "idTiquet=" + idTiquet +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", cancha=" + cancha +
                '}';
    }
}
