/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.model.FacturaElectronica;
import java.time.LocalDate;
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
    List<FacturaElectronica> findBySerieAndNumeroAndEmpresaId(String serie,String numero,int empresaId);

    List<FacturaElectronica> findByEmpresaIdAndTipoInAndIndSituacionInAndEstadoOrderByFechaEmisionAsc
        (Integer empresaId, List<String> tipo, List<String> listSituacion, String estado);
    
    List<FacturaElectronica> findByFechaEmisionBetweenAndEmpresaIdInAndEstadoIn(LocalDate fecIni, LocalDate fecFin, List<Integer> empresaIds, List<String> estados);
    
    @Query(value="select p from facturaElectronica p where p.tipo = :#{#facturaElectronica.notaReferenciaTipo} "
            + "and p.serie = :#{#facturaElectronica.notaReferenciaSerie} "
            + "and p.numero = :#{#facturaElectronica.notaReferenciaNumero} "
            + "and p.empresa.id = :#{#facturaElectronica.empresa.id} ")
    List<FacturaElectronica> findByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(@Param("facturaElectronica") FacturaElectronica facturaElectronica);
    
    @Query(value="select p from facturaElectronica p where p.serie = :#{#facturaElectronica.docrefSerie} "
            + "and p.numero = :#{#facturaElectronica.docrefNumero} "
            + "and p.empresa.id = :#{#facturaElectronica.empresa.id} ")
    List<FacturaElectronica> findByDocrefSerieAndDocrefNumero(@Param("facturaElectronica") FacturaElectronica facturaElectronica);
    
    List<FacturaElectronica> findByIdIn(List<Long> ids);
    
    List<FacturaElectronica> findByTicketOperacion(long ticketOperacion);
}
