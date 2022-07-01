/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.service.*;
import com.deinsoft.efacturador3.bean.ResumenDiarioBean;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.ResumenDiario;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author EDWARD-PC
 */
public interface ResumenDiarioService {
    
    public ResumenDiario getResumenDiarioById(long id) ;
    public List<ResumenDiario> getResumenDiarios();
    public ResumenDiario save(ResumenDiario e);
    public Map<String, Object> generarComprobantePagoSunat(String rootpath,ResumenDiario resumenDiario) throws TransferirArchivoException;
    ResumenDiario toResumenDiarioModel(ResumenDiarioBean resumenDiarioBean,Empresa empresa) throws ParseException;
    public List<ResumenDiario> saveAll(List<ResumenDiario> e);
    public Map<String, Object> generarComprobantePagoSunatFromFacturas(List<Long> listIds) throws TransferirArchivoException;
    public Map<String, Object> sendSUNAT(Long id) ;
    void sendSUNAT() throws TransferirArchivoException;
}
