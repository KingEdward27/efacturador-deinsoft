--factura electronica
select distinct empresa_id from factura_electronica where importe_total is null

select * from factura_electronica d where not exists (select 1 from factura_electronica_det where m_id = d.m_id)
select * from factura_electronica_Det where m_id in (16348,16349)

UPDATE FACTURA_ELECTRONICA_DET set descripcion = ' Alfombras Pequeñas De Colores ' 
WHERE FACTURA_ELECTRONICA_DET_id = 28785

select m_id,fecha_gen_xml,cliente_direccion,cliente_documento,cliente_email,cliente_nombre,cliente_telefono,
cliente_tipo,cod_bien_detraccion,cod_local,cta_banco_nacion_detraccion,customization_id,descuento_global_porcentaje
,descuentos_globales,docref_fecha,docref_monto,docref_numero,docref_serie,estado,fecha_emision,
fecha_envio,fecha_vencimiento,forma_pago,guia_remision,importe_total,incluir_pdf,incluir_xml,ind_situacion,
leyenda1,leyenda2,leyenda3,moneda,monto_neto_pendiente,mto_detraccion,nota_motivo,nota_referencia_numero,
nota_referencia_serie,nota_referencia_tipo,nota_tipo,nro_intento_envio,numero,observacion_envio,observaciones,
orden_compra,placa_vehiculo,por_detraccion,porcentaje_igv,serie,sumatoriaigv,sumatoriaisc,sumatoria_otros_cargos,
sumatoria_otros_tributos,ticket_operacion,ticket_sunat,tipo,tipo_moneda_monto_neto_pendiente,tipo_operacion,
total_valor_venta,total_valor_ventas_exoneradas,total_valor_ventas_gravadas,total_valor_ventas_inafectas,
vendedor_nombre,xml_hash,empresa_id
from factura_electronica

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/factura_electronica3.csv' INTO TABLE factura_electronica FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\n' (@m_id,@fecha_gen_xml,@cliente_direccion,@cliente_documento,@cliente_email,@cliente_nombre,@cliente_telefono,@cliente_tipo,@cod_bien_detraccion,@cod_local,@cta_banco_nacion_detraccion,@customization_id,@descuento_global_porcentaje,@descuentos_globales,@docref_fecha,@docref_monto,@docref_numero,@docref_serie,@estado,@fecha_emision,@fecha_envio,@fecha_vencimiento,@forma_pago,@guia_remision,@importe_total,@incluir_pdf,@incluir_xml,@ind_situacion,@leyenda1,@leyenda2,@leyenda3,@moneda,@monto_neto_pendiente,@mto_detraccion,@nota_motivo,@nota_referencia_numero,@nota_referencia_serie,@nota_referencia_tipo,@nota_tipo,@nro_intento_envio,@numero,@observacion_envio,@observaciones,@orden_compra,@placa_vehiculo,@por_detraccion,@porcentaje_igv,@serie,@sumatoriaigv,@sumatoriaisc,@sumatoria_otros_cargos,@sumatoria_otros_tributos,@ticket_operacion,@ticket_sunat,@tipo,@tipo_moneda_monto_neto_pendiente,@tipo_operacion,@total_valor_venta,@total_valor_ventas_exoneradas,@total_valor_ventas_gravadas,@total_valor_ventas_inafectas,@vendedor_nombre,@xml_hash,@empresa_id) set m_id=trim(@m_id),fecha_gen_xml = substr(@fecha_gen_xml,1,19),cliente_direccion = @cliente_direccion,cliente_documento = @cliente_documento,cliente_email = @cliente_email,cliente_nombre = @cliente_nombre,cliente_telefono = @cliente_telefono,cliente_tipo = @cliente_tipo,cod_bien_detraccion = @cod_bien_detraccion,cod_local = @cod_local,cta_banco_nacion_detraccion = @cta_banco_nacion_detraccion, customization_id = @customization_id,descuento_global_porcentaje = @descuento_global_porcentaje,descuentos_globales = @descuentos_globales,docref_fecha = @docref_fecha,docref_monto = @docref_monto,docref_numero = @docref_numero,docref_serie = @docref_serie,estado = @estado,fecha_emision = substr(@fecha_emision,1,19),fecha_envio = substr(@fecha_envio,1,19),fecha_vencimiento = substr(@fecha_vencimiento,1,19),forma_pago = @forma_pago,guia_remision = @guia_remision,importe_total = @importe_total,incluir_pdf = @incluir_pdf,incluir_xml = @incluir_xml,ind_situacion = @ind_situacion,leyenda1 = @leyenda1,leyenda2 = @leyenda2,leyenda3 = @leyenda3,moneda = @moneda,monto_neto_pendiente = @monto_neto_pendiente,mto_detraccion = @mto_detraccion,nota_motivo = @nota_motivo,nota_referencia_numero = @nota_referencia_numero,nota_referencia_serie = @nota_referencia_serie,nota_referencia_tipo = @nota_referencia_tipo, nota_tipo = @nota_tipo,nro_intento_envio = @nro_intento_envio,numero = @numero,observacion_envio = substr(@observacion_envio,1,255),observaciones = @observaciones,orden_compra = @orden_compra,placa_vehiculo = @placa_vehiculo,por_detraccion = @por_detraccion, porcentaje_igv = @porcentaje_igv,serie = @serie,sumatoriaigv = @sumatoriaigv,sumatoriaisc = @sumatoriaisc,sumatoria_otros_cargos =sumatoria_otros_cargos,sumatoria_otros_tributos = @sumatoria_otros_tributos,ticket_operacion = @ticket_operacion,ticket_sunat = @ticket_sunat, tipo = @tipo,tipo_moneda_monto_neto_pendiente = @tipo_moneda_monto_neto_pendiente,tipo_operacion = @tipo_operacion,total_valor_venta = @total_valor_venta,total_valor_ventas_exoneradas = @total_valor_ventas_exoneradas,total_valor_ventas_gravadas = @total_valor_ventas_gravadas, total_valor_ventas_inafectas = @total_valor_ventas_inafectas, vendedor_nombre = @vendedor_nombre,xml_hash = @xml_hash,empresa_id = @empresa_id;

--factura electronica cuota
select * from parametro
select factura_electronica_cuota_id,fecha_cuota_pago,monto_cuota_pago,tipo_moneda_cuota_pago,
factura_electronica_id
from factura_electronica_cuota

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/factura_electronica3.csv' INTO TABLE factura_electronica_cuota FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\n' (@factura_electronica_cuota_id,@fecha_cuota_pago,@monto_cuota_pago,@tipo_moneda_cuota_pago,@factura_electronica_id) SET factura_electronica_cuota_id = factura_electronica_cuota_id,fecha_cuota_pago = factura_electronica_cuota_id,monto_cuota_pago = @monto_cuota_pago,tipo_moneda_cuota_pago = @tipo_moneda_cuota_pago,factura_electronica_id = @factura_electronica_id;

--factura electronica det
SELECT factura_electronica_det_id
      ,afectacion_igvcode
      ,afectacion_igv
      ,cantidad
      ,cod_tributo_igv
      ,descripcion
      ,descuento
      ,precio_code
      ,precio_venta_unitario
      ,recargo
      ,unidad_medida
      ,valor_unitario
      ,valor_venta_item
      ,m_id
      ,codigo
      ,valor_ref_unitario
  FROM [efacturador].[dbo].[factura_electronica_det]
  
  select * from factura_electronica_Det d 
left join factura_electronica f on f.m_id = d.m_id
where f.m_id is null

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/factura_electronica3.csv' INTO TABLE factura_electronica_det FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\n' (@factura_electronica_det_id ,@afectacion_igvcode,@afectacion_igv,@cantidad,@cod_tributo_igv,@descripcion,@descuento,@precio_code,@precio_venta_unitario,@recargo,@unidad_medida,@valor_unitario,@valor_venta_item,@m_id,@codigo,@valor_ref_unitario) SET factura_electronica_det_id = @factura_electronica_det_id,afectacion_igvcode = @afectacion_igvcode,afectacion_igv = @afectacion_igv,cantidad = @cantidad,cod_tributo_igv = @cod_tributo_igv,descripcion = @descripcion,descuento = @descuento,precio_code = @precio_code,precio_venta_unitario = @precio_venta_unitario,recargo = @recargo,unidad_medida = @unidad_medida,valor_unitario = @valor_unitario,valor_venta_item = @valor_venta_item,m_id = @m_id,codigo = @codigo,valor_ref_unitario = @valor_ref_unitario

--factura electronica tax
SELECT factura_electronica_tax_id
      ,baseamt
      ,tax_id
      ,taxtotal
      ,factura_electronica_id
      ,cod_tip_tributo
      ,mto_base_imponible
      ,nom_tributo
  FROM [efacturador].[dbo].[factura_electronica_tax]
  
LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/factura_electronica3.csv' INTO TABLE factura_electronica_tax FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\n'(@factura_electronica_tax_id,@baseamt,@tax_id,@taxtotal,@factura_electronica_id,@cod_tip_tributo,@mto_base_imponible,@nom_tributo)  set factura_electronica_tax_id = @factura_electronica_tax_id,baseamt = @baseamt,tax_id = @tax_id,taxtotal = @taxtotal,factura_electronica_id = @factura_electronica_id,cod_tip_tributo = @cod_tip_tributo,mto_base_imponible = @mto_base_imponible,nom_tributo  = @nom_tributo;

-- [resumen_diario]
SELECT resumen_diario_id
      ,fecha_gen_xml
      ,customization_id
      ,fecha_emision
      ,fecha_envio
      ,fecha_resumen
      ,ind_situacion
      ,nombre_archivo
      ,observacion_envio
      ,ticket_operacion
      ,ticket_sunat
      ,xml_hash
      ,empresa_id
  FROM [efacturador].[dbo].[resumen_diario]
  
LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/resumen_diario.csv' INTO TABLE resumen_diario FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\n' (@resumen_diario_id,@fecha_gen_xml,@customization_id,@fecha_emision,@fecha_envio,@fecha_resumen,@ind_situacion,@nombre_archivo,@observacion_envio,@ticket_operacion,@ticket_sunat,@xml_hash,@empresa_id)set resumen_diario_id = @resumen_diario_id,fecha_gen_xml = @fecha_gen_xml,customization_id = @customization_id,fecha_emision = @fecha_emision,fecha_envio = @fecha_envio,fecha_resumen = @fecha_resumen,ind_situacion = @ind_situacion,nombre_archivo = @nombre_archivo,observacion_envio = @observacion_envio,ticket_operacion = @ticket_operacion,ticket_sunat = @ticket_sunat,xml_hash = @xml_hash,empresa_id   = @empresa_id  


-- [resumen_diario_Det]
SELECT resumen_diario_det_id
      ,condicion
      ,nom_base_percepcion
      ,nom_percepcion
      ,nom_tot_inc_percepcion
      ,moneda
      ,nro_documento
      ,num_doc_modifico
      ,num_doc_usuario
      ,por_percepcion
      ,ser_doc_modifico
      ,tip_doc_modifico
      ,tip_doc_resumen
      ,tip_doc_usuario
      ,tip_reg_percepcion
      ,total_imp_cpe
      ,total_otro_cargo
      ,total_val_exonerado
      ,total_val_exportado
      ,total_val_grabado
      ,total_val_gratuito
      ,total_val_inafecto
      ,resumen_diario_id
  FROM [efacturador].[dbo].[resumen_diario_det]
  
LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/resumen_diario_det.csv' INTO TABLE resumen_diario_det FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\n' (@resumen_diario_det_id,@condicion,@nom_base_percepcion,@nom_percepcion,@nom_tot_inc_percepcion,@moneda,@nro_documento,@num_doc_modifico,@num_doc_usuario,@por_percepcion,@ser_doc_modifico,@tip_doc_modifico,@tip_doc_resumen,@tip_doc_usuario,@tip_reg_percepcion,@total_imp_cpe,@total_otro_cargo,@total_val_exonerado,@total_val_exportado,@total_val_grabado,@total_val_gratuito,@total_val_inafecto,@resumen_diario_id) set resumen_diario_det_id = @resumen_diario_det_id,condicion = @condicion,nom_base_percepcion = @nom_base_percepcion,nom_percepcion = @nom_percepcion,nom_tot_inc_percepcion = @nom_tot_inc_percepcion,moneda = @moneda,nro_documento = @nro_documento,num_doc_modifico = @num_doc_modifico,num_doc_usuario = @num_doc_usuario,por_percepcion = @por_percepcion,ser_doc_modifico = @ser_doc_modifico,tip_doc_modifico = @tip_doc_modifico,tip_doc_resumen = @tip_doc_resumen,tip_doc_usuario = @tip_doc_usuario,tip_reg_percepcion = @tip_reg_percepcion,total_imp_cpe = @total_imp_cpe,total_otro_cargo = @total_otro_cargo,total_val_exonerado = @total_val_exonerado,total_val_exportado = @total_val_exportado,total_val_grabado = @total_val_grabado,total_val_gratuito = @total_val_gratuito,total_val_inafecto = @total_val_inafecto,resumen_diario_id = @resumen_diario_id

-- [resumen_diario_tax]
SELECT [resumen_diario_tax_id]
      ,[cod_tip_tributo_rd]
      ,[id_lineard]
      ,[ide_tributo_rd]
      ,[mto_base_imponible_rd]
      ,[mto_tributo_rd]
      ,[nom_tributo_rd]
      ,[resumen_diario_id]
  FROM [efacturador].[dbo].[resumen_diario_tax]
  
LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/resumen_diario_tax.csv' INTO TABLE resumen_diario_tax FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\n' (@resumen_diario_tax_id,@cod_tip_tributo_rd,@id_lineard,@ide_tributo_rd,@mto_base_imponible_rd,@mto_tributo_rd,@nom_tributo_rd,@resumen_diario_id)set  resumen_diario_tax_id = @resumen_diario_tax_id,cod_tip_tributo_rd = @resumen_diario_tax_id,id_lineard = @resumen_diario_tax_id,ide_tributo_rd = @resumen_diario_tax_id,mto_base_imponible_rd = @resumen_diario_tax_id,mto_tributo_rd = @resumen_diario_tax_id,nom_tributo_rd = @resumen_diario_tax_id,resumen_diario_id = @resumen_diario_tax_id

select * from rh_controldiario where month(fecha) = 12 and persona_id = '80688076'

select * from rh_rrhh_param_org

select * from rh_controldiario

insert into rh_rrhh_param_org (tipo_trabajador,fec_proceso,fec_inicio,fec_final,company)
values ('EMP','2023-11-30','2023-10-26','2023-11-26','DMT')

update rh_rrhh_param_org set fec_final = '2023-11-25' where fec_proceso = '2023-11-30'

