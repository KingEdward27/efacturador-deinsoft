SELECT TOP (1000) [idempresa]
      ,[cert_name]
      ,[cert_pass]
      ,[clavesol]
      ,[estado]
      ,[numdoc]
      ,[razon_social]
      ,[serie]
      ,[tipodoc]
      ,[token]
      ,[usuariosol] from empresa

  update empresa set usuariosol = 'EBOLETAS',clavesol = 'Eboletas123' where idempresa = 14
  FROM [dbo].[empresa]
  update factura_electronica set ind_situacion = '04' where m_id in (24,25)
select * from factura_electronica
update factura_electronica set ind_situacion = '02' where m_id = 39
  delete from empresa where idempresa = 13
  delete from factura_electronica where empresa_id = 13
  delete from factura_electronica where m_id in (39,40,41)
  delete from factura_electronica_tax where factura_electronica_id in (39,40,41)
   delete from factura_electronica_det where m_id in  (39,40,41)
    delete from factura_electronica_cuota where factura_electronica_id in (39,40,41)

ALTER TABLE factura_electronica
drop column ticket_operacion;

ALTER TABLE factura_electronica
add ticket_operacion bigint;

ALTER TABLE factura_electronica_Det
add codigo varchar(100);

ALTER TABLE empresa
add nombre_comercial varchar(300);

ALTER TABLE empresa
add direccion varchar(300);