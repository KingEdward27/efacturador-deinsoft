SELECT * FROM dbpuntoventa.cnf_moneda;
select * from empresa
select * from locales
select * from resumen_diario
select * from resumen_diario_tax;
update factura_electronica_tax set nom_tributo = 'IGV';
update factura_electronica set cliente_telefono = null where cliente_telefono = 'NULL';
update factura_electronica set cod_bien_detraccion = null where cod_bien_detraccion = 'NULL';
update factura_electronica set cta_banco_nacion_detraccion = null where cta_banco_nacion_detraccion = 'NULL';
update factura_electronica set customization_id = null where customization_id = 'NULL';
update factura_electronica set docref_Serie = null where docref_Serie = 'NULL';
update factura_electronica set docref_numero = null where docref_numero = 'NULL';
update factura_electronica set fecha_vencimiento = null where fecha_vencimiento = 'NULL';
update factura_electronica set incluir_pdf = null where incluir_pdf = 'NULL';
update factura_electronica set incluir_xml = null where incluir_xml = 'NULL';
update factura_electronica set leyenda1 = null where leyenda1 = 'NULL';
update factura_electronica set leyenda2 = null where leyenda2 = 'NULL';
update factura_electronica set leyenda3 = null where leyenda3 = 'NULL';
update factura_electronica set nota_motivo = null where nota_motivo = 'NULL';
update factura_electronica set nota_referencia_numero = null where nota_referencia_numero = 'NULL';
update factura_electronica set nota_referencia_serie = null where nota_referencia_serie = 'NULL';
update factura_electronica set nota_tipo = null where nota_tipo = 'NULL';

select * from factura_electronica d left join factura_electronica f on d.m_id = f.m_id


where f.m_id is null;

INSERT local (local_id, direccion, nombre, serie, empresa_id) VALUES (null, N'AV FLORA TRISTAN 687 - LA MOLINA', N'LAVANDERIA CHICK - LA MOLINA', N'BB01', 14);
INSERT local (local_id, direccion, nombre, serie, empresa_id) VALUES (null, N'AV PRIMAVERA 1146 - SURCO', N'LAVANDERIA CHICK - SURCO', N'BB02', 14);
INSERT local (local_id, direccion, nombre, serie, empresa_id) VALUES (null, N'AV FLORA TRISTAN 687 - LA MOLINA', N'LAVANDERIA CHICK - LA MOLINA', N'FF01', 14);
INSERT local (local_id, direccion, nombre, serie, empresa_id) VALUES (null, N'AV PRIMAVERA 1146 - SURCO', N'LAVANDERIA CHICK - SURCO', N'F001', 14);
INSERT local (local_id, direccion, nombre, serie, empresa_id) VALUES (null, N'AV FLORA TRISTAN 687 - LA MOLINA', N'LAVANDERIA CHICK - LA MOLINA', N'FN01', 14);
INSERT local (local_id, direccion, nombre, serie, empresa_id) VALUES (null, N'AV PRIMAVERA 1146 - SURCO', N'LAVANDERIA CHICK - SURCO', N'FN01', 14);
INSERT local (local_id, direccion, nombre, serie, empresa_id) VALUES (null, N'Av Panamericana Sur 1232 - A', N'DEINSOFT - LIMA', N'B001', 16);
INSERT local (local_id, direccion, nombre, serie, empresa_id) VALUES (null, N'Av Panamericana Sur 1232 - A', N'DEINSOFT - LIMA', N'F001', 16);

select * from factura_electronica d where numero = 12776 not exists (select 1 from factura_electronica_det dd where dd.m_id = d.m_id)
select * from factura_electronica_Det where m_id = 16349
select * from factura

delete from factura_electronica
delete from factura_electronica_cuota
delete from factura_electronica_det
delete from factura_electronica_tax
delete from factura_electronica_leyenda
delete from resumen_diario
delete from resumen_diario_Det
delete from resumen_diario_tax;
truncate table factura_electronica

select * from resumen_diario 
select * from resumen_diario_Det
select * from resumen_diario_TAX
select * from factura_electronica where fecha_emision like '%2024-01-08%'
select * from factura_electronica_det where m_id = 18888

select * from factura_electronica_tax where factura_electronica_id = 18888
IGV

select * from factura_electronica where fecha_emision like '%2023-12-04%'

eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2NjE0NjkzNzksImlzcyI6IkRFSU5TT0ZUIiwianRpIjoiREVGQUNULUpXVCIsInN1YiI6IjIwNTM0OTk5NjE2L0RFSU5TT0ZUIiwibnVtRG9jIjoiMjA1MzQ5OTk2MTYiLCJyYXpvblNvY2lhbCI6IkRFSU5TT0ZUIiwidXN1YXJpb1NvbCI6ImFkbWluIiwiZXhwIjoxNjkzNDY5Mzc5fQ.0BG9imMoIM-EunXuuCABnSzMfqMBFzaZr184AnXaWo3kkF3gb2zH29cfddu1ZNoJljcAIdQ4yi76jggOjB76Gg
eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MDUwNzQ4NDUsImlzcyI6IkRFSU5TT0ZUIiwianRpIjoiREVGQUNULUpXVCIsInN1YiI6IjIwNTM0OTk5NjE2L0RFSU5TT0ZUIiwibnVtRG9jIjoiMjA1MzQ5OTk2MTYiLCJyYXpvblNvY2lhbCI6IkRFSU5TT0ZUIiwidXN1YXJpb1NvbCI6ImFkbWluIiwiZXhwIjoxNzM3MDc0ODQ1fQ.UAGHn79pxQIjTldoPGqnVnlkINZOFGTuZQk1OcAwVZsI0zPy5RSGn6ssZdVaQaVDdMVhrUsalhs_o8JsTdSGug
select * from factura_electronica f where empresa_id not in (select empresa_id from empresa) 
right join factura_electronica_Det d on f.m_id = d.m_id
where f.m_id is null
select * from factura_electronica where importe_total is null
select NULL
SET GLOBAL local_infile = @TRUE;

INSERT empresa (idempresa, cert_name, cert_pass, clavesol, estado, numdoc, razon_social, serie, tipodoc, token, usuariosol, nombre_comercial, direccion) VALUES (14, N'10414316595.pfx', N'Lk3O6rZtXsQ/6NDGu8zXNQ==', N'Eboletas123', N'1', N'10414316595', N'PEREZ DELGADO BLANCA NERI', N'', 6, N'eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2OTM2MDc0NjgsImlzcyI6IkRFSU5TT0ZUIiwianRpIjoiREVGQUNULUpXVCIsInN1YiI6IjEwNDE0MzE2NTk1L1BFUkVaIERFTEdBRE8gQkxBTkNBIE5FUkkiLCJudW1Eb2MiOiIxMDQxNDMxNjU5NSIsInJhem9uU29jaWFsIjoiUEVSRVogREVMR0FETyBCTEFOQ0EgTkVSSSIsInVzdWFyaW9Tb2wiOiJFQk9MRVRBUyIsImV4cCI6MTcyNTYwNzQ2OH0.el-BxlCmW0gLwymfSJPDSo0vnztJqdlbomDIXGJ8ZSB7OZUlXXJaJA_HHChn9pSLOX0M0MAE-cIkJhK9wuB3lA', N'EBOLETAS', N'LAVANDERIA CHICK', NULL);
INSERT empresa (idempresa, cert_name, cert_pass, clavesol, estado, numdoc, razon_social, serie, tipodoc, token, usuariosol, nombre_comercial, direccion) VALUES (15, N'LLAMA-PE-CERTIFICADO-DEMO-20518964756.pfx', N'KW3BL19V9nc=', N'MODDATOS', N'1', N'20518964756', N'HEROES DEL PACIFICO', N'', 6, N'eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2NDkzNjExODQsImlzcyI6IkRFSU5TT0ZUIiwianRpIjoiREVGQUNULUpXVCIsInN1YiI6IjIwNTE4OTY0NzU2L0hFUk9FUyBERUwgUEFDSUZJQ08iLCJudW1Eb2MiOiIyMDUxODk2NDc1NiIsInJhem9uU29jaWFsIjoiSEVST0VTIERFTCBQQUNJRklDTyIsInVzdWFyaW9Tb2wiOiJNT0REQVRPUyIsImV4cCI6MTY1MTE2MTE4NH0.jqX1NeB4MZ3O_wpiZIK_B2MgUsRacAjWG-2rPzXJQd1p1D-jXt4ihznBKC4ymp53O7K3wXCEikZiO2IIOT-AzQ', N'MODDATOS', NULL, NULL);
INSERT empresa (idempresa, cert_name, cert_pass, clavesol, estado, numdoc, razon_social, serie, tipodoc, token, usuariosol, nombre_comercial, direccion) VALUES (16, N'LLAMA-PE-CERTIFICADO-DEMO-20534999616.pfx', N'KW3BL19V9nc=', N'admin', NULL, N'20534999616', N'DEINSOFT', NULL, 6, N'eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2NjE0NjkzNzksImlzcyI6IkRFSU5TT0ZUIiwianRpIjoiREVGQUNULUpXVCIsInN1YiI6IjIwNTM0OTk5NjE2L0RFSU5TT0ZUIiwibnVtRG9jIjoiMjA1MzQ5OTk2MTYiLCJyYXpvblNvY2lhbCI6IkRFSU5TT0ZUIiwidXN1YXJpb1NvbCI6ImFkbWluIiwiZXhwIjoxNjkzNDY5Mzc5fQ.0BG9imMoIM-EunXuuCABnSzMfqMBFzaZr184AnXaWo3kkF3gb2zH29cfddu1ZNoJljcAIdQ4yi76jggOjB76Gg', N'admin', N'DEINSOFT', N'Av Panamericana Sur 1232 - A');

-- bajar una fila cada csv
LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/factura_electronica.csv' INTO TABLE factura_electronica FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' ignore 1 lines  (@m_id,@fecha_gen_xml,@cliente_direccion,@cliente_documento,@cliente_email,@cliente_nombre,@cliente_telefono,@cliente_tipo,@cod_bien_detraccion,@cod_local,@cta_banco_nacion_detraccion,@customization_id,@descuento_global_porcentaje,@descuentos_globales,@docref_fecha,@docref_monto,@docref_numero,@docref_serie,@estado,@fecha_emision,@fecha_envio,@fecha_vencimiento,@forma_pago,@guia_remision,@importe_total,@incluir_pdf,@incluir_xml,@ind_situacion,@leyenda1,@leyenda2,@leyenda3,@moneda,@monto_neto_pendiente,@mto_detraccion,@nota_motivo,@nota_referencia_numero,@nota_referencia_serie,@nota_referencia_tipo,@nota_tipo,@nro_intento_envio,@numero,@observacion_envio,@observaciones,@orden_compra,@placa_vehiculo,@por_detraccion,@porcentaje_igv,@serie,@sumatoriaigv,@sumatoriaisc,@sumatoria_otros_cargos,@sumatoria_otros_tributos,@ticket_operacion,@ticket_sunat,@tipo,@tipo_moneda_monto_neto_pendiente,@tipo_operacion,@total_valor_venta,@total_valor_ventas_exoneradas,@total_valor_ventas_gravadas,@total_valor_ventas_inafectas,@vendedor_nombre,@xml_hash,@empresa_id) set m_id=trim(@m_id),fecha_gen_xml = substr(@fecha_gen_xml,1,19),cliente_direccion = @cliente_direccion,cliente_documento = @cliente_documento,cliente_email = @cliente_email,cliente_nombre = @cliente_nombre,cliente_telefono = @cliente_telefono,cliente_tipo = @cliente_tipo,cod_bien_detraccion = @cod_bien_detraccion,cod_local = @cod_local,cta_banco_nacion_detraccion = @cta_banco_nacion_detraccion, customization_id = @customization_id,descuento_global_porcentaje = @descuento_global_porcentaje,descuentos_globales = @descuentos_globales,docref_fecha = case when @docref_fecha = 'NULL' then null else @docref_fecha end,docref_monto = @docref_monto,docref_numero = @docref_numero,docref_serie = @docref_serie,estado = @estado,fecha_emision = substr(@fecha_emision,1,19),fecha_envio = case when @fecha_envio = 'NULL' then null else substr(@fecha_envio,1,19) end,fecha_vencimiento = substr(@fecha_vencimiento,1,19),forma_pago = @forma_pago,guia_remision = @guia_remision,importe_total = @importe_total,incluir_pdf = @incluir_pdf,incluir_xml = @incluir_xml,ind_situacion = @ind_situacion,leyenda1 = @leyenda1,leyenda2 = @leyenda2,leyenda3 = @leyenda3,moneda = @moneda,monto_neto_pendiente = @monto_neto_pendiente,mto_detraccion = @mto_detraccion,nota_motivo = @nota_motivo,nota_referencia_numero = @nota_referencia_numero,nota_referencia_serie = @nota_referencia_serie,nota_referencia_tipo = @nota_referencia_tipo, nota_tipo = @nota_tipo,nro_intento_envio = @nro_intento_envio,numero = @numero,observacion_envio = substr(@observacion_envio,1,255),observaciones = @observaciones,orden_compra = @orden_compra,placa_vehiculo = @placa_vehiculo,por_detraccion = @por_detraccion, porcentaje_igv = @porcentaje_igv,serie = @serie,sumatoriaigv = @sumatoriaigv,sumatoriaisc = @sumatoriaisc,sumatoria_otros_cargos =sumatoria_otros_cargos,sumatoria_otros_tributos = @sumatoria_otros_tributos,ticket_operacion = @ticket_operacion,ticket_sunat = @ticket_sunat, tipo = @tipo,tipo_moneda_monto_neto_pendiente = @tipo_moneda_monto_neto_pendiente,tipo_operacion = @tipo_operacion,total_valor_venta = @total_valor_venta,total_valor_ventas_exoneradas = @total_valor_ventas_exoneradas,total_valor_ventas_gravadas = @total_valor_ventas_gravadas, total_valor_ventas_inafectas = @total_valor_ventas_inafectas, vendedor_nombre = @vendedor_nombre,xml_hash = @xml_hash,empresa_id=trim(@empresa_id);

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/factura_electronica_cuota.csv' INTO TABLE factura_electronica_cuota FIELDS TERMINATED BY ';'LINES TERMINATED BY '\n' ignore 1 lines  (@factura_electronica_cuota_id,@fecha_cuota_pago,@monto_cuota_pago,@tipo_moneda_cuota_pago,@factura_electronica_id) SET factura_electronica_cuota_id=trim(factura_electronica_cuota_id),fecha_cuota_pago = factura_electronica_cuota_id,monto_cuota_pago = @monto_cuota_pago,tipo_moneda_cuota_pago = @tipo_moneda_cuota_pago,factura_electronica_id = @factura_electronica_id;

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/factura_electronica_det.csv' INTO TABLE factura_electronica_det FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' ignore 1 lines  (@factura_electronica_det_id ,@afectacion_igvcode,@afectacion_igv,@cantidad,@cod_tributo_igv,@descripcion,@descuento,@precio_code,@precio_venta_unitario,@recargo,@unidad_medida,@valor_unitario,@valor_venta_item,@m_id,@codigo,@valor_ref_unitario) SET factura_electronica_det_id=trim(@factura_electronica_det_id),afectacion_igvcode = @afectacion_igvcode,afectacion_igv = @afectacion_igv,cantidad = @cantidad,cod_tributo_igv = @cod_tributo_igv,descripcion = @descripcion,descuento = @descuento,precio_code = @precio_code,precio_venta_unitario = @precio_venta_unitario,recargo = @recargo,unidad_medida = @unidad_medida,valor_unitario = @valor_unitario,valor_venta_item = @valor_venta_item,m_id=trim(@m_id),codigo = @codigo,valor_ref_unitario = @valor_ref_unitario;

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/factura_electronica_tax.csv' INTO TABLE factura_electronica_tax FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' ignore 1 lines  (@factura_electronica_tax_id,@baseamt,@tax_id,@taxtotal,@factura_electronica_id,@cod_tip_tributo,@mto_base_imponible,@nom_tributo)  set factura_electronica_tax_id=trim(@factura_electronica_tax_id),baseamt = @baseamt,tax_id = @tax_id,taxtotal = @taxtotal,factura_electronica_id=trim(@factura_electronica_id),cod_tip_tributo = @cod_tip_tributo,mto_base_imponible = @mto_base_imponible,nom_tributo  = @nom_tributo;

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/resumen_diario.csv' INTO TABLE resumen_diario FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' ignore 1 lines  (@resumen_diario_id,@fecha_gen_xml,@customization_id,@fecha_emision,@fecha_envio,@fecha_resumen,@ind_situacion,@nombre_archivo,@observacion_envio,@ticket_operacion,@ticket_sunat,@xml_hash,@empresa_id)set resumen_diario_id = @resumen_diario_id,fecha_gen_xml = @fecha_gen_xml,customization_id = @customization_id,fecha_emision = @fecha_emision,fecha_envio = case when @fecha_envio = 'NULL' then null else @fecha_envio end,fecha_resumen = @fecha_resumen,ind_situacion = @ind_situacion,nombre_archivo = @nombre_archivo,observacion_envio = @observacion_envio,ticket_operacion = @ticket_operacion,ticket_sunat = @ticket_sunat,xml_hash = @xml_hash,empresa_id=trim(@empresa_id);

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/resumen_diario_det.csv' INTO TABLE resumen_diario_det FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' ignore 1 lines  (@resumen_diario_det_id,@condicion,@nom_base_percepcion,@nom_percepcion,@nom_tot_inc_percepcion,@moneda,@nro_documento,@num_doc_modifico,@num_doc_usuario,@por_percepcion,@ser_doc_modifico,@tip_doc_modifico,@tip_doc_resumen,@tip_doc_usuario,@tip_reg_percepcion,@total_imp_cpe,@total_otro_cargo,@total_val_exonerado,@total_val_exportado,@total_val_grabado,@total_val_gratuito,@total_val_inafecto,@resumen_diario_id) set resumen_diario_det_id=trim(@resumen_diario_det_id),condicion = @condicion,nom_base_percepcion = @nom_base_percepcion,nom_percepcion = @nom_percepcion,nom_tot_inc_percepcion = @nom_tot_inc_percepcion,moneda = @moneda,nro_documento = @nro_documento,num_doc_modifico = @num_doc_modifico,num_doc_usuario = @num_doc_usuario,por_percepcion = @por_percepcion,ser_doc_modifico = @ser_doc_modifico,tip_doc_modifico = @tip_doc_modifico,tip_doc_resumen = @tip_doc_resumen,tip_doc_usuario = @tip_doc_usuario,tip_reg_percepcion = @tip_reg_percepcion,total_imp_cpe = @total_imp_cpe,total_otro_cargo = @total_otro_cargo,total_val_exonerado = @total_val_exonerado,total_val_exportado = @total_val_exportado,total_val_grabado = @total_val_grabado,total_val_gratuito = @total_val_gratuito,total_val_inafecto = @total_val_inafecto,resumen_diario_id = @resumen_diario_id;

LOAD DATA LOCAL INFILE 'C:/Users/user/Downloads/resumen_diario_tax.csv' INTO TABLE resumen_diario_tax FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' ignore 1 lines  (@resumen_diario_tax_id,@cod_tip_tributo_rd,@id_lineard,@ide_tributo_rd,@mto_base_imponible_rd,@mto_tributo_rd,@nom_tributo_rd,@resumen_diario_id) set resumen_diario_tax_id=trim(@resumen_diario_tax_id),cod_tip_tributo_rd = @cod_tip_tributo_rd,id_lineard = @id_lineard,ide_tributo_rd = @ide_tributo_rd,mto_base_imponible_rd = @mto_base_imponible_rd,mto_tributo_rd = @mto_tributo_rd,nom_tributo_rd = @nom_tributo_rd,resumen_diario_id =trim(@resumen_diario_id);