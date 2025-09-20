package model;

import java.time.LocalDate;

public class Reporte {
    private int id;
    private String tipo;
    private LocalDate fechaGeneracion;
    private int administradorId;

    public Reporte(int id, String tipo, LocalDate fechaGeneracion, int administradorId) {
        this.id = id;
        this.tipo = tipo;
        this.fechaGeneracion = fechaGeneracion;
        this.administradorId = administradorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public int getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(int administradorId) {
        this.administradorId = administradorId;
    }

    @Override
    public String toString() {
        return "Reporte{" + "id=" + id + ", tipo=" + tipo + ", fechaGeneracion=" + fechaGeneracion + ", administradorId=" + administradorId + '}';
    }


}

