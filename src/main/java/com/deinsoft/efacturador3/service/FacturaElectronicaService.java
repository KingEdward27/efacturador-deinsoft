/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author EDWARD-PC
 */
public interface FacturaElectronicaService {
    public FacturaElectronica getById(long id);
    public List<FacturaElectronica> getListFacturaElectronica();
    FacturaElectronica save(FacturaElectronica facturaElectronica);
    public List<FacturaElectronica> getBySerieAndNumeroAndEmpresaId(FacturaElectronica facturaElectronica);
    public List<FacturaElectronica> getByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(FacturaElectronica facturaElectronica);
    public FacturaElectronica toFacturaModel(ComprobanteCab documento) throws TransferirArchivoException, ParseException;
    public Map<String, Object> generarComprobantePagoSunat(String rootpath, FacturaElectronica documento) throws TransferirArchivoException;
    public String validarComprobante(ComprobanteCab documento);
    public void sendToSUNAT();
}
