package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.config.XsltCpePath;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deinsoft.efacturador3.repository.ErrorRepository;
import com.deinsoft.efacturador3.repository.ParametroRepository;
import com.deinsoft.efacturador3.service.BandejaDocumentosService;
import com.deinsoft.efacturador3.service.ComunesService;
import com.deinsoft.efacturador3.service.GenerarDocumentosService;
import com.deinsoft.efacturador3.soap.gencdp.ExceptionDetail;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import com.deinsoft.efacturador3.validator.XsdCpeValidator;
import com.deinsoft.efacturador3.validator.XsltCpeValidator;
import javax.annotation.ManagedBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.deinsoft.efacturador3.repository.FacturaElectronicaRepository;
import com.deinsoft.efacturador3.service.EmpresaService;

@ManagedBean
public class BandejaDocumentosServiceImpl implements BandejaDocumentosService {

    private static final Logger log = LoggerFactory.getLogger(BandejaDocumentosServiceImpl.class);

    @Autowired
    private FacturaElectronicaRepository documentoDao;

    @Autowired
    private ParametroRepository parametroDao;

    @Autowired
    private ErrorRepository errorDao;

    @Autowired
    private GenerarDocumentosService generarDocumentosService;

    @Autowired
    private ComunesService comunesService;

    @Autowired
    private XsltCpePath xsltCpePath;
    
    @Autowired
    EmpresaService empresaService;
    
    public String generarComprobantePagoSunat(String rootpath,Empresa empresa, FacturaElectronica documento) throws TransferirArchivoException {
        XsltCpeValidator xsltCpeValidator = new XsltCpeValidator(this.comunesService, this.errorDao,this.xsltCpePath);
        XsdCpeValidator xsdCpeValidator = new XsdCpeValidator(this.comunesService, this.errorDao,this.xsltCpePath);

        try {
            String retorno = "01";
            String tipoComprobante = null;
            String nomFile = "";
            if ("01".equals(documento.getIndSituacion()) || "06"
                    .equals(documento.getIndSituacion()) || "07"
                    .equals(documento.getIndSituacion()) || "10"
                    .equals(documento.getIndSituacion()) || "05"
                    .equals(documento.getIndSituacion())) {
                retorno = "";
                tipoComprobante = documento.getTipo();
                log.debug("BandejaDocumentosServiceImpl.generarComprobantePagoSunat...tipoComprobante: " + tipoComprobante);

                nomFile = documento.getEmpresa().getNumdoc()
                    +"-"+String.format("%02d", Integer.parseInt(documento.getTipo()))
                    +"-"+documento.getSerie()
                    +"-"+String.format("%08d", Integer.parseInt(documento.getNumero()));
                this.generarDocumentosService.formatoPlantillaXml(rootpath,documento,nomFile);
                
                this.generarDocumentosService.firmarXml(rootpath,documento,nomFile);
//                this.generarDocumentosService.validarPlazo(documento.getNomArch());
                xsdCpeValidator.validarSchemaXML(rootpath,documento, rootpath + "/"+documento.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml");

                xsltCpeValidator.validarXMLYComprimir(rootpath,documento, rootpath + "/"+documento.getEmpresa().getNumdoc() + "/PARSE/", nomFile);

            }
            return retorno;
        } catch (Exception e) {

            log.info(e.getMessage());
            e.printStackTrace();
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setMessage(e.getMessage());

            throw new TransferirArchivoException(e.getMessage(), exceptionDetail);
        }
    }

    public HashMap<String, Object> listarCertificados() throws Exception {
        HashMap<String, Object> resultado = new HashMap<>();
        Map<String, String> archivoLista = null;
        List<Map<String, String>> listaArchivos = new ArrayList<>();
        Integer error = Integer.valueOf(0);

        File directorioCertificados = new File(this.comunesService.obtenerRutaTrabajo("CERT"));

        if (!directorioCertificados.exists()) {
            resultado.put("validacion", "No existe el directorio.");
            resultado.put("adjunto", null);
            error = Integer.valueOf(1);
        }

        if (!directorioCertificados.isDirectory() && error.intValue() == 0) {
            resultado.put("validacion", this.comunesService.obtenerRutaTrabajo("CERT") + ", no es directorio.");
            resultado.put("adjunto", null);
            error = Integer.valueOf(1);
        }

        if (error.intValue() == 0) {
            String[] archivos = directorioCertificados.list();

            if (archivos == null) {
                resultado.put("validacion", "No hay ficheros en el directorio especificado");
                resultado.put("adjunto", null);
                error = Integer.valueOf(1);
            } else {
                for (int x = 0; x < archivos.length; x++) {
                    archivoLista = new HashMap<>();
                    archivoLista.put("id", archivos[x]);
                    if (archivos[x] != null) {
                        if (archivos[x].length() > 29) {
                            archivoLista.put("nombre", archivos[x].substring(0, 28));
                        } else {
                            archivoLista.put("nombre", archivos[x]);
                        }
                    } else {
                        archivoLista.put("nombre", archivos[x]);
                    }

                    listaArchivos.add(archivoLista);
                }

                resultado.put("validacion", "");
                resultado.put("adjunto", listaArchivos);
                resultado.put("ruta", this.comunesService.obtenerRutaTrabajo("CERT"));
            }
        }

        return resultado;
    }

    
    private Boolean validarNombreArchivo(String nombreArchivo) {
        Boolean resultado = Boolean.valueOf(true);

        log.debug("Validar que no sea directorio: " + resultado);
        File directorio = new File(nombreArchivo);
        if (directorio.isDirectory()) {
            resultado = Boolean.valueOf(false);
        }

        log.debug("Validar que no exista mas de un punto en el nombre del archivo: " + resultado);
        String[] validacionPunto = nombreArchivo.split("\\.");
        if (validacionPunto.length != 2) {
            resultado = Boolean.valueOf(false);
        }

        log.debug("Validar que sea una extension valida: " + resultado);
        if (resultado.booleanValue()) {
            String tipoExtension = validacionPunto[1];
            log.debug("Validar que sea una extension valida, Extension: " + tipoExtension);
            if (!"CBA".equals(tipoExtension)
                    && !"RDI".equals(tipoExtension)
                    && !"TRD".equals(tipoExtension)
                    && !"RDR".equals(tipoExtension)
                    && !"NOT".equals(tipoExtension)
                    && !"CAB".equals(tipoExtension)
                    && !"RET".equals(tipoExtension)
                    && !"PER".equals(tipoExtension)
                    && !"JSON".equals(tipoExtension)
                    && !"XML".equals(tipoExtension)
                    && !"DET".equals(tipoExtension)
                    && !"REL".equals(tipoExtension)
                    && !"ACA".equals(tipoExtension)
                    && !"ADE".equals(tipoExtension)
                    && !"LEY".equals(tipoExtension)
                    && !"DRE".equals(tipoExtension)
                    && !"DPE".equals(tipoExtension)) {
                resultado = Boolean.valueOf(false);
            }
        }

        log.debug("Validar que el nombre esta bien conformado: " + resultado);
        String[] validacionNombre = nombreArchivo.split("\\-");
        if (resultado.booleanValue()) {
            if (validacionNombre.length != 4) {
                resultado = Boolean.valueOf(false);
            }
        }

        log.debug("Validar RUC es numerico y 11 digitos: " + resultado);
        if (resultado.booleanValue()) {
            String expresion = "^[0-9]{11}$";
            Pattern patron = Pattern.compile(expresion);
            Matcher validador = patron.matcher(validacionNombre[0]);
            resultado = Boolean.valueOf(validador.matches());
        }

        log.debug("Validar extension en el dominio valido: " + resultado);
        if (resultado.booleanValue()
                && !"08".equals(validacionNombre[1])
                && !"07".equals(validacionNombre[1])
                && !"01".equals(validacionNombre[1])
                && !"20".equals(validacionNombre[1])
                && !"40".equals(validacionNombre[1])
                && !"03".equals(validacionNombre[1])
                && !"RA".equals(validacionNombre[1])
                && !"RC".equals(validacionNombre[1])
                && !"RR".equals(validacionNombre[1])
                && !"09".equals(validacionNombre[1])) {
            resultado = Boolean.valueOf(false);
        }

        log.debug("Validar serie sea 4 caracteres: " + resultado);
        if (resultado.booleanValue()) {
            String expresion = "";
            if (!"RA".equals(validacionNombre[1])
                    && !"RC".equals(validacionNombre[1])
                    && !"RR".equals(validacionNombre[1])) {
                expresion = "^[a-zA-Z0-9]{4}$";
            } else {
                expresion = "^[a-zA-Z0-9]{8}$";
            }

            Pattern patron = Pattern.compile(expresion);
            Matcher validador = patron.matcher(validacionNombre[2]);
            resultado = Boolean.valueOf(validador.matches());
        }

        log.debug("Retorno desde Validador de Nombre: " + resultado);

        return resultado;
    }

//    @Override
//    public List<Documento> consultarBandejaComprobantesPorId(Documento paramDocumento) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}
