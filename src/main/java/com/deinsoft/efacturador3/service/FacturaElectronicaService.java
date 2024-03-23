/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ParamBean;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author EDWARD-PC
 */
public interface FacturaElectronicaService {

    public FacturaElectronica getById(long id);

    public FacturaElectronica findById(long id);
            
    public List<FacturaElectronica> getListFacturaElectronica();

    FacturaElectronica save(FacturaElectronica facturaElectronica);

    public List<FacturaElectronica> getBySerieAndNumeroAndEmpresaId(FacturaElectronica facturaElectronica);

    public List<FacturaElectronica> getByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(FacturaElectronica facturaElectronica);

    public FacturaElectronica toFacturaModel(ComprobanteCab documento,Empresa e) throws TransferirArchivoException, ParseException;

    public Map<String, Object> generarComprobantePagoSunat(String rootpath, FacturaElectronica documento) throws TransferirArchivoException;
    public Map<String, Object> generarComprobantePagoSunat(long comprobanteId) throws TransferirArchivoException;
    public String validarComprobante(ComprobanteCab documento,Empresa e);

    public Map<String, Object> sendToSUNAT(long comprobante_id);

    public void sendToSUNAT();
    
    public List<FacturaElectronica> getByFechaEmisionBetweenAndEmpresaIdInAndEstadoIn(LocalDate fecIni, LocalDate fecFin,List<Integer> empresaIds, List<String> estados);
    
    public List<FacturaElectronica> getByIdIn(List<Long> ids);
    
    public List<FacturaElectronica> getByTicketOperacion(long ticketOperacion);
    
    public Map<String, Object> getPDF(long ticketOperacion,int tipo) throws Exception;
    
    public byte[] getPDFInBtyes(long id, int tipo) throws Exception;
    
    public Map<String, Object> generarNotaCredito(long comprobanteId) throws TransferirArchivoException;
    
    public void verifyPending();
    
    public List<FacturaElectronica> getReportActComprobante(ParamBean paramBean);
}
