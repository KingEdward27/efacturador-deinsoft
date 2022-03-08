/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ComprobanteCuotas;
import com.deinsoft.efacturador3.bean.ComprobanteDet;
import com.deinsoft.efacturador3.config.XsltCpePath;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaCuotas;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.repository.ErrorRepository;
import com.deinsoft.efacturador3.repository.FacturaElectronicaRepository;
import com.deinsoft.efacturador3.service.ComunesService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.GenerarDocumentosService;
import com.deinsoft.efacturador3.soap.gencdp.ExceptionDetail;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.validator.XsdCpeValidator;
import com.deinsoft.efacturador3.validator.XsltCpeValidator;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author EDWARD-PC
 */
@Service
public class FacturaElectronicaServiceImpl implements FacturaElectronicaService{

    private static final Logger log = LoggerFactory.getLogger(BandejaDocumentosServiceImpl.class);
    
    @Autowired
    FacturaElectronicaRepository facturaElectronicaRepository;
    
    @Autowired
    private ComunesService comunesService;
    
    @Autowired
    private GenerarDocumentosService generarDocumentosService;
    
    @Autowired
    private ErrorRepository errorDao;
    
    @Autowired
    private XsltCpePath xsltCpePath;
    
    @Override
    public FacturaElectronica getById(long id) {
        return facturaElectronicaRepository.getById(id);
    }

    @Override
    public List<FacturaElectronica> getListFacturaElectronica() {
        return facturaElectronicaRepository.findAll();
    }

    @Override
    public FacturaElectronica save(FacturaElectronica facturaElectronica) {
        return facturaElectronicaRepository.save(facturaElectronica);
    }
    @Override
    public List<FacturaElectronica> getBySerieAndNumero(FacturaElectronica facturaElectronica){
        return facturaElectronicaRepository.findBySerieAndNumero(facturaElectronica.getSerie(),facturaElectronica.getNumero());
    }
    @Override
    public List<FacturaElectronica> getByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(FacturaElectronica facturaElectronica){
        return facturaElectronicaRepository.findByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(facturaElectronica);
    }
    @Override
    public String generarComprobantePagoSunat(String rootpath,FacturaElectronica documento) throws TransferirArchivoException {
        XsltCpeValidator xsltCpeValidator = new XsltCpeValidator(this.comunesService, this.errorDao,this.xsltCpePath);
        XsdCpeValidator xsdCpeValidator = new XsdCpeValidator(this.comunesService, this.errorDao,this.xsltCpePath);

        try {
            String retorno = "01";
            String tipoComprobante = null;
            String nomFile = "";
            if ("01".equals(documento.getIndSituacion()) || "06"
                    .equals(documento.getIndSituacion()) || "07"
                    .equals(documento.getIndSituacion()) || "10"
                    .equals(documento.getIndSituacion()) || "05"
                    .equals(documento.getIndSituacion())) {
                retorno = "";
                tipoComprobante = documento.getTipo();
                log.debug("BandejaDocumentosServiceImpl.generarComprobantePagoSunat...tipoComprobante: " + tipoComprobante);

                nomFile = documento.getEmpresa().getNumdoc()
                    +"-"+String.format("%02d", Integer.parseInt(documento.getTipo()))
                    +"-"+documento.getSerie()
                    +"-"+String.format("%08d", Integer.parseInt(documento.getNumero()));
                this.generarDocumentosService.formatoPlantillaXml(rootpath,documento,nomFile);
                
                this.generarDocumentosService.firmarXml(rootpath,documento,nomFile);
//                this.generarDocumentosService.validarPlazo(documento.getNomArch());
                xsdCpeValidator.validarSchemaXML(rootpath,documento, rootpath + "/"+documento.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml");

                xsltCpeValidator.validarXMLYComprimir(rootpath,documento, rootpath + "/"+documento.getEmpresa().getNumdoc() + "/PARSE/", nomFile);

            }
            return retorno;
        } catch (Exception e) {

            log.info(e.getMessage());
            e.printStackTrace();
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setMessage(e.getMessage());

            throw new TransferirArchivoException(e.getMessage(), exceptionDetail);
        }
    }
    @Override
    public FacturaElectronica toFacturaModel(ComprobanteCab documento) throws TransferirArchivoException, ParseException{
        BigDecimal totalValorVentasGravadas = BigDecimal.ZERO, totalValorVentasInafectas = BigDecimal.ZERO,
                    totalValorVentasExoneradas = BigDecimal.ZERO,
                    SumatoriaIGV = BigDecimal.ZERO, SumatoriaISC = BigDecimal.ZERO,
                    sumatoriaOtrosTributos = BigDecimal.ZERO, sumatoriaOtrosCargos = BigDecimal.ZERO, totalValorVenta = BigDecimal.ZERO;
        FacturaElectronica comprobante = new FacturaElectronica();
            //constantes o bd
            for (ComprobanteDet detalle : documento.getLista_productos()) {
                if (Integer.valueOf(detalle.getTipo_igv()) >= 10 && Integer.valueOf(detalle.getTipo_igv()) <= 20) {
                    totalValorVentasGravadas = totalValorVentasGravadas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
                } else if (Integer.valueOf(detalle.getTipo_igv()) >= 30 && Integer.valueOf(detalle.getTipo_igv()) <= 36) {
                    totalValorVentasInafectas = totalValorVentasInafectas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
                } else {
                    totalValorVentasExoneradas = totalValorVentasExoneradas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
                }
                SumatoriaIGV = SumatoriaIGV.add(detalle.getAfectacionIGV());
                SumatoriaISC = SumatoriaISC.add(detalle.getAfectacionISC() == null ? BigDecimal.ZERO : detalle.getAfectacionISC());
                totalValorVenta = totalValorVenta.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            }

            documento.setSumatoriaIGV(SumatoriaIGV);
            documento.setSumatoriaISC(SumatoriaISC);
            documento.setSumatoriaOtrosCargos(BigDecimal.ZERO);
            documento.setSumatoriaOtrosTributos(BigDecimal.ZERO);
            documento.setTotalValorVentasGravadas(totalValorVentasGravadas);
            documento.setTotalValorVentasInafectas(totalValorVentasInafectas);
            documento.setTotalValorVentasExoneradas(totalValorVentasExoneradas);

            
            comprobante.setTipo(documento.getTipo());
            comprobante.setTipoOperacion(documento.getTipo_operacion());
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(documento.getFecha_emision());
            //	java.sql.Date dateSql = new java.sql.Date(date1.getTime());
            comprobante.setFechaEmision(date1);
            comprobante.setSerie(documento.getSerie());
            comprobante.setNumero(String.format("%08d", Integer.parseInt(documento.getNumero())));
            comprobante.setFechaVencimiento(documento.getFecha_vencimiento());
            comprobante.setClienteTipo(documento.getCliente_tipo());
            comprobante.setClienteNombre(documento.getCliente_nombre());
            comprobante.setClienteDocumento(documento.getCliente_documento());
            comprobante.setClienteDireccion(documento.getCliente_direccion());
            comprobante.setClienteEmail(documento.getCliente_email());
            comprobante.setClienteTelefono(documento.getCliente_telefono());
            comprobante.setVendedorNombre(documento.getVendedor_nombre());
            comprobante.setObservaciones(documento.getObservaciones());
            comprobante.setPlacaVehiculo(documento.getPlaca_vehiculo());
            comprobante.setOrdenCompra(documento.getOrden_compra());
            comprobante.setGuiaRemision(documento.getGuia_remision());
            comprobante.setDescuentoGlobalPorcentaje(documento.getDescuento_global_porcentaje());
            comprobante.setMoneda(documento.getMoneda());
            comprobante.setNotaTipo(documento.getNota_tipo());
            comprobante.setNotaMotivo(documento.getNota_motivo());
            comprobante.setNotaReferenciaTipo(documento.getNota_referencia_tipo());
            comprobante.setNotaReferenciaSerie(documento.getNota_referencia_serie());
            comprobante.setNotaReferenciaNumero(documento.getNota_referencia_numero());
            comprobante.setIncluirPdf(documento.getIncluir_pdf());
            comprobante.setIncluirXml(documento.getIncluir_xml());
            comprobante.setSumatoriaIGV(SumatoriaIGV);
            comprobante.setSumatoriaISC(SumatoriaISC);
            comprobante.setSumatoriaOtrosCargos(BigDecimal.ZERO);
            comprobante.setSumatoriaOtrosTributos(BigDecimal.ZERO);
            comprobante.setTotalValorVentasGravadas(totalValorVentasGravadas);
            comprobante.setTotalValorVentasInafectas(totalValorVentasInafectas);
            comprobante.setTotalValorVentasExoneradas(totalValorVentasExoneradas);
            comprobante.setTotalValorVenta(totalValorVenta);
            comprobante.setCodLocal("0000");
            comprobante.setFormaPago(documento.getForma_pago());
            comprobante.setPorcentajeIGV(new BigDecimal(Constantes.PORCENTAJE_IGV));
            comprobante.setMontoNetoPendiente(documento.getMonto_neto_pendiente());
            comprobante.setTipoMonedaMontoNetoPendiente(documento.getMoneda_monto_neto_pendiente());

            if(comprobante.getTipo().equals("07") || comprobante.getTipo().equals("08")){
                comprobante.setNotaMotivo(documento.getNota_motivo());
                comprobante.setNotaTipo(documento.getNota_tipo());
                comprobante.setNotaReferenciaTipo(documento.getNota_referencia_tipo());
                comprobante.setNotaReferenciaSerie(documento.getNota_referencia_serie());
                comprobante.setNotaReferenciaNumero(String.format("%08d", Integer.parseInt(documento.getNota_referencia_numero())));
            }
            

            List<FacturaElectronicaDet> list = new ArrayList<>();
            List<FacturaElectronicaTax> listTax = new ArrayList<>();
            List<FacturaElectronicaCuotas> listCuotas = new ArrayList<>();
            BigDecimal baseamt = BigDecimal.ZERO, taxtotal = BigDecimal.ZERO;

            for (ComprobanteDet comprobanteDet : documento.getLista_productos()) {
                FacturaElectronicaDet det = new FacturaElectronicaDet();
                det.setDescripcion(comprobanteDet.getDescripcion());
                det.setUnidadMedida(comprobanteDet.getUnidad_medida());
                det.setCantidad(comprobanteDet.getCantidad());
                det.setPrecioVentaUnitario(comprobanteDet.getPrecio_unitario());
                det.setValorVentaItem(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacionIGV()));
                det.setValorUnitario(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacionIGV()));
                det.setAfectacionIgv(comprobanteDet.getAfectacionIGV());
                det.setAfectacionIGVCode(comprobanteDet.getTipo_igv());
                det.setDescuento(comprobanteDet.getDescuento_porcentaje().
                        divide(new BigDecimal(100)).
                        multiply(comprobanteDet.getCantidad().multiply(comprobanteDet.getPrecio_unitario())));
                det.setRecargo(comprobanteDet.getRecargo());
                list.add(det);

                baseamt = baseamt.add(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacionIGV()));
                taxtotal = taxtotal.add(comprobanteDet.getAfectacionIGV());
                FacturaElectronicaTax facturaElectronicaTax = new FacturaElectronicaTax();
                facturaElectronicaTax.setTaxId(1000);
                facturaElectronicaTax.setBaseamt(baseamt);
                facturaElectronicaTax.setTaxtotal(taxtotal);
                listTax.add(facturaElectronicaTax);
            }

            if (!CollectionUtils.isEmpty(documento.getLista_cuotas())) {

//                BigDecimal sumaCoutas = BigDecimal.ZERO;
                for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {
                    FacturaElectronicaCuotas det = new FacturaElectronicaCuotas();
                    det.setMtoCuotaPago(detalle.getMonto_pago());
                    det.setTipMonedaCuotaPago(detalle.getTipo_moneda_pago());
                    det.setFecCuotaPago(new SimpleDateFormat("dd/MM/yyyy").parse(detalle.getFecha_pago()));
                    listCuotas.add(det);
                }

            }

            comprobante.setListFacturaElectronicaDet(list);
            comprobante.setListFacturaElectronicaTax(listTax);
            comprobante.setListFacturaElectronicaCuotas(listCuotas);

            comprobante.getListFacturaElectronicaDet().stream().forEach(item -> {
                comprobante.addFacturaElectronicaDet(item);
            });
            comprobante.getListFacturaElectronicaTax().stream().forEach(item -> {
                comprobante.addFacturaElectronicaTax(item);
            });
            comprobante.getListFacturaElectronicaCuotas().stream().forEach(item -> {
                comprobante.addFacturaElectronicaCuotas(item);
            });
            //constantes o bd
            comprobante.setIndSituacion("01");
            return comprobante;
    }
}
