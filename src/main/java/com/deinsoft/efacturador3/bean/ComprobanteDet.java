package com.deinsoft.efacturador3.bean;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author EDWARD
 */
public class ComprobanteDet {

    private String codigo;
    @NotBlank
    private String descripcion;
    private String detalle_adicional;
    @NotBlank
    private String unidad_medida;
    
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal cantidad;
   
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal precio_unitario;
    
    private BigDecimal descuento_porcentaje;
    
    @NotBlank
    private String tipo_igv;

    @NotBlank
    private String cod_tributo_igv;
    
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal afectacion_igv;
    
    private BigDecimal afectacion_isc;
    
    private BigDecimal recargo;
    
    private BigDecimal monto_referencial_unitario;
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalle_adicional() {
        return detalle_adicional;
    }

    public void setDetalle_adicional(String detalle_adicional) {
        this.detalle_adicional = detalle_adicional;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    


    public String getTipo_igv() {
        return tipo_igv;
    }

    public void setTipo_igv(String tipo_igv) {
        this.tipo_igv = tipo_igv;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public BigDecimal getDescuento_porcentaje() {
        return descuento_porcentaje;
    }

    public void setDescuento_porcentaje(BigDecimal descuento_porcentaje) {
        this.descuento_porcentaje = descuento_porcentaje;
    }

    public BigDecimal getAfectacion_igv() {
        return afectacion_igv;
    }

    public void setAfectacion_igv(BigDecimal afectacion_igv) {
        this.afectacion_igv = afectacion_igv;
    }

    public BigDecimal getAfectacion_isc() {
        return afectacion_isc;
    }

    public void setAfectacion_isc(BigDecimal afectacion_isc) {
        this.afectacion_isc = afectacion_isc;
    }


    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getMonto_referencial_unitario() {
        return monto_referencial_unitario;
    }

    public void setMonto_referencial_unitario(BigDecimal monto_referencial_unitario) {
        this.monto_referencial_unitario = monto_referencial_unitario;
    }

    public String getCod_tributo_igv() {
        return cod_tributo_igv;
    }

    public void setCod_tributo_igv(String cod_tributo_igv) {
        this.cod_tributo_igv = cod_tributo_igv;
    }

    

    public ComprobanteDet() {
    }

//        public JsonObject toJson(ComprobanteDet detail) {
//            JsonObject object = null;
//            try {
//                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
//            for(Field f : detail.getClass().getDeclaredFields()) {
//                if (f.get(detail) == null) {
//                    continue;
//                }
//                System.out.println(f.getGenericType() +" "+f.getName() + " = " + f.get(detail));
//                objectBuilder.add(f.getName(),f.get(detail).toString());
//            }
//            
//            object = objectBuilder.build();
//            
//            } catch (Exception e) {
//                System.out.println("error inesperado: "+e.toString());
//            }
//            return object;
//        }

    @Override
    public String toString() {
        return "ComprobanteDet{" + "codigo=" + codigo + ", descripcion=" + descripcion + ", detalle_adicional=" + detalle_adicional + ", unidad_medida=" + unidad_medida + ", cantidad=" + cantidad + ", precio_unitario=" + precio_unitario + ", descuento_porcentaje=" + descuento_porcentaje + ", tipo_igv=" + tipo_igv + ", afectacion_igv=" + afectacion_igv + ", afectacion_isc=" + afectacion_isc + ", recargo=" + recargo + ", monto_referencial_unitario=" + monto_referencial_unitario + '}';
    }
}
