package com.deinsoft.efacturador3.bean.enums;

public enum EstadoEnvioComprobante {
    ANULADO("00", "ANULADO"),
    XML_GENERADO("02", "PENDIENTE"),
    POR_GENERAR_XML("01", "POR GENERAR XML"),
    ACEPTADO("03", "ACEPTADO"),
    CON_PROBLEMAS("06", "CON PROBLEMAS"),
    RECHAZADO04("04", "RECHAZADO04"),
    RECHAZADO("10", "RECHAZADO");
    private final String codigo;
    private final String descripcion;

    EstadoEnvioComprobante(String codigo, String descripcion) {
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
        for (EstadoEnvioComprobante estado : values()) {
            if (estado.getCodigo().equals(codigo)) {
                return estado.getDescripcion();
            }
        }
        return "Estado desconocido";
    }
}