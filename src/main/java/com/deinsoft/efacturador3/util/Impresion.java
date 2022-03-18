/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.util;

//import accesodatos.ConfiguracionADN;
//import entidades.ConsultaVentas;
//import entidades.Empresa;
//import entidades.Formatos;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
//import sistventa.JDVisor;
//import static sistventa.JIVentas.round;
//import util.Constantes;

/**
 *
 * @author EDWARD
 */
public class Impresion {

    public static ByteArrayInputStream Imprimir(int tipo, FacturaElectronica comprobante) throws Exception {
        try {
            JasperReport reporte = null;
            String ubicacion = "";
            if (tipo == 1) {
                ubicacion = "jasper/boleta.jasper";
            }else
            {
                ubicacion = "jasper/ticket.jasper";
            }
//            if (tipo == 1) {
//                ubicacion = "classpath:boleta.jasper";
//            }else
//            {
//                ubicacion = "classpath:ticket.jasper";
//            }
            InputStream fileNode = new ClassPathResource(ubicacion).getInputStream();
//            File fileNode = ResourceUtils.getFile(ubicacion);
            reporte = (JasperReport) JRLoader.loadObject(fileNode);
            Map parametros;
            parametros = new HashMap<String, Object>();

//            float subTotal = round(comprobante.getTotalValorVenta()/ (ConfiguracionADN.Datos().get(0).getValorIGV()/100+ 1), 2);
//            float igvTotal = round(comprobante.getVentatotal() - subTotal, 2);
            parametros.put("tipodoc", Catalogos.tipoDocumento(comprobante.getTipo(), null)[1] );
            parametros.put("razon_social", comprobante.getEmpresa().getRazonSocial());
            parametros.put("direccion", "-AQUI VA LA DIRECCION-");
            parametros.put("ruc", comprobante.getEmpresa().getNumdoc());

            parametros.put("numero", comprobante.getSerie() + "-" + comprobante.getNumero());
            parametros.put("ruc_dniCliente", comprobante.getClienteDocumento());
            parametros.put("nombreCliente", comprobante.getClienteNombre());
            parametros.put("direccionCliente", comprobante.getClienteDireccion());
            parametros.put("pFechaEmision", new SimpleDateFormat("dd/MM/yyyy").format(comprobante.getFechaEmision()));
            parametros.put("fechaVencimiento", "");
            parametros.put("moneda", "SOLES");

            parametros.put("pdescuento2",comprobante.getDescuentosGlobales());
            parametros.put("pgravado",String.valueOf(comprobante.getTotalValorVenta().subtract(comprobante.getSumatoriaIGV())));
            parametros.put("pigv",String.valueOf(comprobante.getSumatoriaIGV()));
            parametros.put("ptotal", String.valueOf(comprobante.getTotalValorVenta()));
            parametros.put("ptotal_letras", "SON "+ NumberToLetterConverter.convertNumberToLetter(comprobante.getTotalValorVenta().doubleValue()));

            parametros.put("pusuario_fecha", "ADMIN el " + comprobante.getFechaEmision());
            parametros.put("presolucion", "Autorizado mediantes resolución N° "+ "N° 034-005-0010431/SUNAT");
            parametros.put("tipoDocFooter", "Representación impresa de la "+  Catalogos.tipoDocumento(comprobante.getTipo(), null)[1]);
            parametros.put("ppagina","Para consultar el comprobante visita www.opendeinsoft.com");
            parametros.put("presumen", comprobante.getXmlHash());
            parametros.put("idTipoDoc", String.valueOf(tipo));
//            if (comprobante.getTipo() != Constantes.ID_TIPO_DOC_PROFORMA) {
                String pathResult = CodigoQR.GenerarQR(comprobante.getEmpresa().getNumdoc()+"|"+
                        comprobante.getTipo()+"|"+
                        comprobante.getSerie()+"-"+comprobante.getNumero()+"|"+
                        String.valueOf(comprobante.getSumatoriaIGV())+"|"+
                        String.valueOf(comprobante.getTotalValorVenta())+"|"+
                        comprobante.getFechaEmision()+"|"+
                        comprobante.getClienteTipo()+"|"+
                        comprobante.getClienteDocumento());
                if (!pathResult.equals("")) {
                    parametros.put("rutaimagen", pathResult);
                }
//            }
            
            List<Map<String, String>> listaBean = new ArrayList();
            Integer count = 0;
            for (FacturaElectronicaDet item : comprobante.getListFacturaElectronicaDet()) {
                ++count;
                Map<String, String> beanMap = new HashMap<>();
//                float subtotalDet = round(item.getPrecioVentaUnitario().divide(comprobante.getPorcentajeIGV().divide(new BigDecimal(100))), 2);
//                float igv = round(item.getPrecio() - subtotalDet, 2);
                beanMap.put("nro", count.toString());
                beanMap.put("cantidad", String.valueOf(item.getCantidad()));
                beanMap.put("um", item.getUnidadMedida());
                beanMap.put("codigo",String.valueOf(item.getId()));
                beanMap.put("descripcion", item.getDescripcion());
                beanMap.put("vu", String.valueOf(item.getValorUnitario()));
                beanMap.put("pu", String.valueOf(item.getPrecioVentaUnitario()));
                beanMap.put("igv", String.valueOf(item.getAfectacionIgv()));
                beanMap.put("descuento", String.valueOf(item.getDescuento()));
                beanMap.put("importe", String.valueOf(item.getValorVentaItem()));

                listaBean.add(beanMap);
            }
            if (count < 20 && tipo == 1) {
                if (count >= 15 && count < 20) {
                    for (int i = 0; i < 2; i++) {
                        Map<String, String> beanMap = new HashMap<>();
                        String empty = "";
                        beanMap.put("nro",empty);
                        beanMap.put("cantidad", empty);
                        beanMap.put("um", empty);
                        beanMap.put("codigo", empty);
                        beanMap.put("descripcion",empty);
                        beanMap.put("vu", empty);
                        beanMap.put("pu", empty);
                        beanMap.put("igv", empty);
                        beanMap.put("descuento", empty);
                        beanMap.put("importe",empty);
                        listaBean.add(beanMap);
                    }
                    
                }else if(count >= 10 && count < 15)
                {
                    for (int i = 0; i < 5; i++) {
                        Map<String, String> beanMap = new HashMap<>();
                        String empty = "";
                        beanMap.put("nro",empty);
                        beanMap.put("cantidad", empty);
                        beanMap.put("um", empty);
                        beanMap.put("codigo", empty);
                        beanMap.put("descripcion",empty);
                        beanMap.put("vu", empty);
                        beanMap.put("pu", empty);
                        beanMap.put("igv", empty);
                        beanMap.put("descuento", empty);
                        beanMap.put("importe",empty);
                        listaBean.add(beanMap);
                    }
                }else if(count >= 5 && count < 10)
                {
                    for (int i = 0; i < 5; i++) {
                        Map<String, String> beanMap = new HashMap<>();
                        String empty = "";
                        beanMap.put("nro",empty);
                        beanMap.put("cantidad", empty);
                        beanMap.put("um", empty);
                        beanMap.put("codigo", empty);
                        beanMap.put("descripcion",empty);
                        beanMap.put("vu", empty);
                        beanMap.put("pu", empty);
                        beanMap.put("igv", empty);
                        beanMap.put("descuento", empty);
                        beanMap.put("importe",empty);
                        listaBean.add(beanMap);
                    }
                }else if(count < 5)
                {
                    for (int i = 0; i < 10; i++) {
                        Map<String, String> beanMap = new HashMap<>();
                        String empty = "";
                        beanMap.put("nro",empty);
                        beanMap.put("cantidad", empty);
                        beanMap.put("um", empty);
                        beanMap.put("codigo", empty);
                        beanMap.put("descripcion",empty);
                        beanMap.put("vu", empty);
                        beanMap.put("pu", empty);
                        beanMap.put("igv", empty);
                        beanMap.put("descuento", empty);
                        beanMap.put("importe",empty);
                        listaBean.add(beanMap);
                    }
                }
            }
            
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, new JRBeanCollectionDataSource(listaBean));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, output);
//            JasperViewer visor = new JasperViewer(print,false);
            
//            JDVisor dialog = new JDVisor(null, true);//the owner
//            dialog.setContentPane(visor.getContentPane());
//            dialog.setSize(visor.getSize());
//            dialog.setTitle("Visor");
//            dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(
//            getClass().getResource("URL IMG")));
//            dialog.setVisible(true);
            
//            visor.setTitle("Impresión de documento");
//            visor.setVisible(true);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            throw e;
        }

    }
}
