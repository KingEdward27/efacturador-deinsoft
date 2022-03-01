/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.FacturaElectronica;
import java.util.List;

/**
 *
 * @author EDWARD-PC
 */
public interface FacturaElectronicaService {
    public FacturaElectronica getById(long id);
    public List<FacturaElectronica> getListFacturaElectronica();
    FacturaElectronica save(FacturaElectronica facturaElectronica);
    public List<FacturaElectronica> getBySerieAndNumero(FacturaElectronica facturaElectronica);
}
