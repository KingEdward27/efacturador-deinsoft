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

      eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2NjE0NjQ0NzcsImlzcyI6IkRFSU5TT0ZUIiwianRpIjoiREVGQUNULUpXVCIsInN1YiI6IjEwNDE0MzE2NTk1L1BFUkVaIERFTEdBRE8gQkxBTkNBIE5FUkkiLCJudW1Eb2MiOiIxMDQxNDMxNjU5NSIsInJhem9uU29jaWFsIjoiUEVSRVogREVMR0FETyBCTEFOQ0EgTkVSSSIsInVzdWFyaW9Tb2wiOiJFQk9MRVRBUyIsImV4cCI6MTY5MzQ2NDQ3N30.Dk-LBXyih2218nubjqd58iNPKwZLbhPTuzkRcfmxVn5MoJpkJYtes87mQKiWyQlPIBSExa1KNDWrH9TY8NiYVQ
 update empresa set usuariosol = 'EBOLETAS',clavesol = 'Eboletas123' where idempresa = 14
  FROM [dbo].[empresa]

  --update factura_electronica set ind_situacion = '04' where m_id in (24,25)
select * from factura_electronica where empresa_id = 16
select * from factura_electronica_Det
select CURRENT_TIMEZONE()



update factura_electronica_tax
set taxtotal = (select sum(afectacion_igv) from factura_electronica_det where m_id = factura_electronica_tax.factura_electronica_id)
where taxtotal = 0

update factura_electronica
set sumatoriaigv = (select sum(afectacion_igv) from factura_electronica_det where m_id = factura_electronica.m_id)
where sumatoriaigv = 0


select sumatoriaigv,(select sum(afectacion_igv) from factura_electronica_det where m_id = t.factura_electronica_id)
from factura_electronica t

select * from factura_electronica_tax t

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

ALTER TABLE factura_electronica
add ticket_sunat varchar(100);


ALTER TABLE factura_electronica
drop COLUMN observacion_envio


ALTER TABLE factura_electronica
ALTER COLUMN observacion_envio VARCHAR(1000);

select afectacion_igv,precio_venta_unitario,cantidad,

from factura_electronica_det det
inner join factura_electronica fe on fe.m_id =  det.m_id
where fe.empresa_id = 14

update factura_electronica_det set afectacion_igv = round((precio_venta_unitario* cantidad)-((precio_venta_unitario*cantidad)/1.18),2)
where afectacion_igv = 0

select f.m_id, f.total_valor_venta,f.total_valor_ventas_gravadas, f.sumatoriaigv, 
tax.mto_base_imponible,tax.taxtotal,
d.afectacion_igv,
 d.precio_venta_unitario, d.cantidad ,d.valor_unitario,d.valor_venta_item,d.valor_ref_unitario
from factura_electronica f
inner join factura_electronica_det d on d.m_id = f.m_id
inner join factura_electronica_tax tax on tax.factura_electronica_id = f.m_id
order by sumatoriaigv

where d.valor_unitario < 0;

update factura_electronica_det set valor_unitario = round(precio_venta_unitario/1.18,2),
valor_venta_item = round(precio_venta_unitario/1.18,2)
where valor_unitario < 0;

select * from factura_electronica where m_id = 198
select * from factura_electronica_det where m_id = 198
select * from factura_electronica_tax where factura_electronica_id = 198

update factura_electronica_tax
set taxtotal = (select sum(afectacion_igv) from factura_electronica_det where m_id = factura_electronica_tax.factura_electronica_id)
where taxtotal = 0

update factura_electronica_tax
set mto_base_imponible = (select total_valor_venta/1.18 from factura_electronica 
where m_id = factura_electronica_tax.factura_electronica_id),
baseamt = (select total_valor_venta/1.18 from factura_electronica 
where m_id = factura_electronica_tax.factura_electronica_id)
where mto_base_imponible < 0

where taxtotal = 0

select * from factura_electronica_tax order by mto_base_imponible
update factura_electronica_det set cod_tributo_igv = '1000' where cod_tributo_igv is null
select * from 

select * from empresa
select * from sec_role
insert into sec_user values(4,'yg.estudiocontable1@gmail.com','lavanderiachick','$2a$10$K1zGsVNG6nvnZE3FyDj4/ekgCbW1i7rJX/rykWBwOygwzppdJsCb6','1');
select * from sec_role_user
insert into sec_role_user (sec_role_user_id,empresa_id,sec_role_id,sec_user_id) 
values(3,14,2,4);

CREATE TABLE resumen_diario (
  resumen_diario_id bigint IDENTITY(1,1) NOT NULL,
  fecha_gen_xml datetime DEFAULT NULL,
  customization_id varchar(255) DEFAULT NULL,
  fecha_emision datetime DEFAULT NULL,
  fecha_envio datetime DEFAULT NULL,
  fecha_resumen datetime DEFAULT NULL,
  ind_situacion varchar(255) DEFAULT NULL,
  nombre_archivo varchar(255) DEFAULT NULL,
  observacion_envio varchar(255) DEFAULT NULL,
  ticket_operacion bigint DEFAULT NULL,
  ticket_sunat varchar(255) DEFAULT NULL,
  xml_hash varchar(255) DEFAULT NULL,
  empresa_id int NOT NULL,
  PRIMARY KEY (resumen_diario_id),
  CONSTRAINT fk_resumen_diario_empresa_id FOREIGN KEY (empresa_id) REFERENCES empresa (idempresa)
);


CREATE TABLE resumen_diario_det (
  resumen_diario_det_id bigint IDENTITY(1,1) NOT NULL,
  condicion varchar(255) DEFAULT NULL,
  nom_base_percepcion decimal(19,2) DEFAULT NULL,
  nom_percepcion decimal(19,2) DEFAULT NULL,
  nom_tot_inc_percepcion decimal(19,2) DEFAULT NULL,
  moneda varchar(255) DEFAULT NULL,
  nro_documento varchar(255) DEFAULT NULL,
  num_doc_modifico varchar(255) DEFAULT NULL,
  num_doc_usuario varchar(255) DEFAULT NULL,
  por_percepcion varchar(255) DEFAULT NULL,
  ser_doc_modifico varchar(255) DEFAULT NULL,
  tip_doc_modifico varchar(255) DEFAULT NULL,
  tip_doc_resumen varchar(255) DEFAULT NULL,
  tip_doc_usuario varchar(255) DEFAULT NULL,
  tip_reg_percepcion varchar(255) DEFAULT NULL,
  total_imp_cpe decimal(19,2) DEFAULT 0.00,
  total_otro_cargo decimal(19,2) DEFAULT 0.00,
  total_val_exonerado decimal(19,2) DEFAULT 0.00,
  total_val_exportado decimal(19,2) DEFAULT 0.00,
  total_val_grabado decimal(19,2) DEFAULT 0.00,
  total_val_gratuito decimal(19,2) DEFAULT 0.00,
  total_val_inafecto decimal(19,2) DEFAULT 0.00,
  resumen_diario_id bigint NOT NULL,
  PRIMARY KEY (resumen_diario_det_id),
  CONSTRAINT fk_resumen_diario_det_resumen_diario FOREIGN KEY (resumen_diario_id) REFERENCES resumen_diario (resumen_diario_id)
);

CREATE TABLE resumen_diario_tax (
  resumen_diario_tax_id bigint IDENTITY(1,1) NOT NULL,
  cod_tip_tributo_rd varchar(255) DEFAULT NULL,
  id_lineard int DEFAULT NULL,
  ide_tributo_rd varchar(255) DEFAULT NULL,
  mto_base_imponible_rd decimal(19,2) DEFAULT NULL,
  mto_tributo_rd decimal(19,2) DEFAULT NULL,
  nom_tributo_rd varchar(255) DEFAULT NULL,
  resumen_diario_id bigint NOT NULL,
  PRIMARY KEY (resumen_diario_tax_id),
  CONSTRAINT fk_resumen_diario_tax_resumen_diario FOREIGN KEY (resumen_diario_id) REFERENCES resumen_diario (resumen_diario_id)
) ;

exec sp_columns factura_electronica
exec sp_columns resumen_diario

select * from factura_electronica WHERE M_ID = 381
update factura_electronica set ind_situacion = '03',serie = 'F001',observacion_envio = '- El comprobante de pago consultado ha sido emitido a otro contribuyente.',
 FECHA_EMISION = '2022-04-29' WHERE M_ID = 381

select * from resumen_diario_det
select * from factura_electronica where numero = '00000022'
update factura_electronica set estado = '1' where estado is null
select * from factura_electronica_det where m_id in 380,
alter table factura_electronica
add estado char(1) default '1';

update resumen_diario set ind_situacion = '03',
observacion_envio = 'El Comprobante RC-20220419-10, ha sido aceptado (Consulta validez)'
where resumen_diario_id = 10;

update factura_electronica set ind_situacion = '03',
observacion_envio = 'El Comprobante RC-20220419-10, ha sido aceptado (Consulta validez)'
where exists (select 1 from resumen_diario_det det
where nro_documento = factura_electronica.serie + '-' +factura_electronica.numero
and det.resumen_diario_id = 10);

select 1 from resumen_diario_det det
where nro_documento = 'BB02' + '-' +'00000037'
and det.resumen_diario_id = 2

ALTER TABLE factura_electronica
ADD CONSTRAINT df_estado
DEFAULT '1' FOR estado;

select numero,estado,count(*) from factura_electronica
where tipo = '03'
group by numero,estado
having count(*) > 1

select * from empresa
select * from factura_electronica where tipo = '01'
select * from factura_electronica d where d.m_id =  2072
select * from factura_electronica_Det d where d.m_id = 2072 2072
select * from factura_electronica_tax d where d.factura_electronica_id =  2072
select * from resumen_diario

update factura_electronica set sumatoriaigv = 7.92 where m_id =  2072

update factura_electronica_Det set 
valor_unitario = 5.0 ,
valor_venta_item = 5.0
where m_id =  2072
-- 101.48

update factura_electronica_Det set 
afectacion_igv = precio_venta_unitario*cantidad - (precio_venta_unitario*cantidad/1.18) 
where m_id =  2072

update factura_electronica_tax set taxtotal = 7.92,mto_base_imponible = 44 where factura_electronica_id =  2072

update factura_electronica_Det set 
afectacion_igv = precio_venta_unitario - (precio_venta_unitario/1.18) 
where m_id =  1771

select M_ID,NUMERO,fecha_emision,observacion_envio from factura_electronica F where m_id > 1685
update factura_electronica set estado = '0' where m_id in(
1694,
1696,
1699,
1701,
1703,
1705,
1707,
1709,
1711,
1713,
1715,
1717,
1719,
1721,
1724,
1726,
1728,
1730,
1732,
1734,
1736)

1120 1121
update factura_electronica set estado = '2' where m_id = 1604

UPDATE factura_electronica set ind_situacion = '03', observacion_envio = 'La Boleta numero BB02-00002322, ha sido aceptada'
where numero ='00002322'

UPDATE factura_electronica set ind_situacion = '03', observacion_envio = 'La Boleta numero BB02-00000451, ha sido aceptada'
where numero ='00000451'

select * from factura_electronica where  M_ID = 381
update factura_electronica set FECHA_EMISION = '2022-06-17' WHERE M_ID = 1369
update factura_electronica set FECHA_EMISION = '2022-05-03' WHERE M_ID = 381

select getdate()

insert into sec_user (sec_user_id,email,name,PASSWORD,isactive) 
values(5,'pablojc3005@gmail.com','pablojc3005',
'$2a$10$ySSHW/94asbWwyduiUu6t./z761Sqgo0kJA9Q0/DexTBv9wY267wu','1');

insert into sec_role_user(sec_role_user_id,empresa_id,sec_role_id,sec_user_id) 
values(4,16,2,5);

UPDATE factura_electronica set ind_situacion = '03', observacion_envio = 'La Boleta numero BB02-'+NUMERO+', ha sido aceptada'
where numero IN ('00002707',
'00002982',
'00002972',
'00002971',
'00002794',
'00002791')

SELECT * FROM factura_electronica where numero IN ('00002707',
'00002982',
'00002972',
'00002971',
'00002794',
'00002791')

'00002707',
'00002982',
'00002972',
'00002971',
'00002794',
'00002791'
202310722410859
BB01	00000656

UPDATE factura_electronica set ind_situacion = '03', observacion_envio = 'La Boleta numero BB02-'+NUMERO+', ha sido aceptada'
where serie = 'BB02' and numero IN (4601
)
SELECT M_ID, numero FROM factura_electronica where serie = 'BB01' and numero between 1031 and 1032
order by numero

update factura_electronica set estado = '0' where m_id IN 
(
6105,
6103,
6100,
6101,
6095,
6096,
6097,
6098)

select * from factura_electronica where m_id IN 
(
6105,
6103,
6100,
6101,
6095,
6096,
6097,
6098)
select * from resumen_diario
SELECT * FROM factura_electronica where m_id in
(4380, 4383, 4687)

select * from factura_electronica_det where m_id = 3357

select * from factura_electronica_tax where factura_electronica_id = 3357

update empresa set nombre_comercial = 'LAVANDERIA CHICK' where idempresa = 1;

insert into local (local_id,empresa_id,nombre,serie,direccion) values (null,1,'LAVANDERIA CHICK - LA MOLINA','BB01','AV FLORA TRISTAN 687 - LA MOLINA');
insert into local (local_id,empresa_id,nombre,serie,direccion) values (null,1,'LAVANDERIA CHICK - SURCO','BB02','AV PRIMAVERA 1146 - SURCO');
insert into local (empresa_id,nombre,serie,direccion) values (14,'LAVANDERIA CHICK - LA MOLINA','FF01','AV FLORA TRISTAN 687 - LA MOLINA');
insert into local (empresa_id,nombre,serie,direccion) values (14,'LAVANDERIA CHICK - SURCO','F001','AV PRIMAVERA 1146 - SURCO');

update factura_electronica set estado = '0' where m_id in (4380, 4383, 4687)

6089
6088
6087
6086
6085
6084
6083
6082
6081
6080
6079
6078
6077
6076
6075
6074
6073
6072
6071
6070

select * from [dbo].resumen_diario order by resumen_diario_id desc

select * from [dbo].[resumen_diario_det]
where resumen_diario_id in 
(
8524,8522,8520, 8517, 8515, 8513, 8511, 8509
)

select m_id, numero,ind_situacion,estado from factura_electronica
where serie = 'BB01' and numero IN (
1840,
1839,
1839,
1838,
1838,
1837,
1837,
1836,
1836,
1835,
1834,
1834,
1833,
1833,
1832,
1832

)

UPDATE factura_electronica set estado = '0'
where serie = 'BB01' and m_id IN (
8524,8522,8520, 8517, 8515, 8513, 8511, 8509
)

UPDATE factura_electronica set ind_situacion = '03', observacion_envio = 'La Boleta numero BB01-'+NUMERO+', ha sido aceptada'
where serie = 'BB01' and estado = '1' and numero IN (
1699,
1698,
1697,
1696,
1695,
1694,
1693,
1692,
1691,
1690,
1689
)