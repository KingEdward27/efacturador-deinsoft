/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.model.FacturaElectronica;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author EDWARD-PC
 */
public interface FacturaElectronicaRepository extends JpaRepository<FacturaElectronica,Long> {
    @EntityGraph(
		    type = EntityGraph.EntityGraphType.FETCH,
		    attributePaths = {
		      "empresa"
		    }
		  )
    List<FacturaElectronica> findBySerieAndNumero(String serie,String numero);

    List<FacturaElectronica> findByIndSituacion(String situacion);
    
    @Query(value="select p from facturaElectronica p where p.tipo = :#{#facturaElectronica.notaReferenciaTipo} "
            + "and p.serie = :#{#facturaElectronica.notaReferenciaSerie} "
            + "and p.numero = :#{#facturaElectronica.notaReferenciaNumero} "
            + "and p.empresa.idempresa = :#{#facturaElectronica.empresa.idempresa} ")
    List<FacturaElectronica> findByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(@Param("facturaElectronica") FacturaElectronica facturaElectronica);
}
