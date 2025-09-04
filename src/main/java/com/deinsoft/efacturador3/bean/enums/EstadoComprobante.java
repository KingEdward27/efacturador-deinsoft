package com.deinsoft.efacturador3.bean.enums;

public enum EstadoComprobante {
    ACTIVO("1", "ACTIVO"),
    ANULADO("0", "ANULADO");
    private final String codigo;
    private final String descripcion;

    EstadoComprobante(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static String getDescripcionByCodigo(String codigo) {
        for (EstadoComprobante estado : values()) {
            if (estado.getCodigo().equals(codigo)) {
                return estado.getDescripcion();
            }
        }
        return "Estado desconocido";
    }
}