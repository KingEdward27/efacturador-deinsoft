/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.bean.ParamBean;
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

    @Query(value="select p from facturaElectronica p "
            + "where p.fechaEmision >= :fecha "
            + "and p.nroIntentoEnvio < 3"
            + "and p.empresa.id = :empresaId "
            + "and p.tipo in (:listTipo) "
            + "and p.indSituacion in (:listSituacion) "
            + "and p.estado = :estado order by p.fechaEmision asc,p.tipo asc,p.numero asc")
    List<FacturaElectronica> findToSendSunat
        (@Param("empresaId") Integer empresaId,
                @Param("listTipo") List<String> listTipo,
                @Param("listSituacion") List<String> listSituacion,
                @Param("estado") String estado,
                @Param("fecha") LocalDate fecha);
    
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
    
    @Query(value = "select p from facturaElectronica p "
            + "where (p.fechaEmision between :#{#paramBean.fechaDesde} and :#{#paramBean.fechaHasta}) "
            + "order by p.tipo desc, p.serie desc, p.numero desc,p.fechaEmision desc")
    List<FacturaElectronica> getReportActComprobante(@Param("paramBean") ParamBean paramBean);
}
