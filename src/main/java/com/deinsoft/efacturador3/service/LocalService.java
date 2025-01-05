/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.Local;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author EDWARD-PC
 */
public interface LocalService {

    public List<Local> getLocales();

    public Local save(Local local);

    public List<Local> getByEmpresaIdAndSerieRelacion(long empresaId, String serie);
    
}
