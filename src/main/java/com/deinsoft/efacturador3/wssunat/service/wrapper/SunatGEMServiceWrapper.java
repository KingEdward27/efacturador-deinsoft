package com.deinsoft.efacturador3.wssunat.service.wrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.deinsoft.efacturador3.wssunat.exception.ConstanciaException;
import com.deinsoft.efacturador3.wssunat.exception.WebserviceConfigurationException;
import com.deinsoft.efacturador3.wssunat.model.StatusResponse;
import com.deinsoft.efacturador3.wssunat.model.UsuarioSol;
import com.deinsoft.efacturador3.wssunat.service.BillService;
import com.deinsoft.efacturador3.wssunat.wss.handler.ClientePasswordCallback;







public class SunatGEMServiceWrapper
{
  private String curentURLService;
  private String usuario;
  /*     */   
  public SunatGEMServiceWrapper(UsuarioSol usuarioSol, String curentURLService) {
    this.curentURLService = curentURLService;
    this.usuario = usuarioSol.rucUsuarioSOL();
    
    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
  }


  
  public Response sendBill(String rutaArchivoCdr, String nombreArchivo, String rutaArchivo) {
    DataSource source = new FileDataSource(new File(rutaArchivo));
    
    System.out.println(">>>nombreArchivo:" + nombreArchivo);
    
    System.out.println(">>>curentURLService:" + this.curentURLService);
    
    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
    
    BillService client = initWebService(1);
    try {
      return sendBill(rutaArchivoCdr, nombreArchivo, source, client);
    } catch (SOAPFaultException e) {
      
      if (e.getCause() instanceof com.ctc.wstx.exc.WstxIOException) {
        
        try {
          client = initWebService(2);
          return sendBill(rutaArchivoCdr, nombreArchivo, source, client);
        } catch (SOAPFaultException e2) {
          
          if (e.getCause() instanceof org.apache.wss4j.common.ext.WSSecurityException) {
            
            if (e.getCause().getCause() != null)
            {
              throw new RuntimeException(e.getCause().getCause().getMessage(), e.getCause().getCause());
            }

            
            throw new RuntimeException(e.getCause().getMessage(), e.getCause());
          } 




          
          Response response = SOAPFaultToResponse(e2.getFault());
          return response;
        } 
      }
      
      return SOAPFaultToResponse(e.getFault());
    } 
  }



  
  private Response sendBill(String rutaArchivoCdr, String nombreArchivo, DataSource source, BillService client) {
    byte[] constancia = client.sendBill(nombreArchivo, new DataHandler(source));
    System.out.println(">>>constancia:" + constancia);
    try {
      FileUtils.writeByteArrayToFile(new File(rutaArchivoCdr + "R" + nombreArchivo), constancia);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return byteToResponse(constancia);
  }

  
  public Response sendSummary(String nombreArchivo, String rutaArchivo) {
    BillService client1 = initWebService(1);
    
    DataSource source = new FileDataSource(new File(rutaArchivo));
    try {
      String constancia = client1.sendSummary(nombreArchivo, new DataHandler(source));
      
      return new Response(0, constancia, new ArrayList<>());
    } catch (SOAPFaultException e) {
      
      if (e.getCause() instanceof com.ctc.wstx.exc.WstxIOException) {
        
        try {
          client1 = initWebService(2);
          
          String constancia = client1.sendSummary(nombreArchivo, new DataHandler(source));
          
          return new Response(0, constancia, new ArrayList<>());
        }
        catch (SOAPFaultException e2) {
          
          if (e.getCause() instanceof org.apache.wss4j.common.ext.WSSecurityException) {
            
            if (e.getCause().getCause() != null)
            {
              throw new RuntimeException(e.getCause().getCause().getMessage(), e.getCause().getCause());
            }

            
            throw new RuntimeException(e.getCause().getMessage(), e.getCause());
          } 




          
          Response response = SOAPFaultToResponse(e2.getFault());
          return response;
        } 
      }
      
      return SOAPFaultToResponse(e.getFault());
    } 
  }



  
  public Response sendPack(String nombreArchivo, String rutaArchivo) {
    System.out.println("SunatGEMServiceWrapper.sendPack()   1.000000000000");
    BillService client1 = initWebService(1);
    System.out.println("SunatGEMServiceWrapper.sendPack()   1.000000000001");
    
    DataSource source = new FileDataSource(new File(rutaArchivo));
    try {
      System.out.println("SunatGEMServiceWrapper.sendPack()   2.000000000000");
      String constancia = client1.sendPack(nombreArchivo, new DataHandler(source));
      System.out.println("SunatGEMServiceWrapper.sendPack()   3.000000000000");
      
      return new Response(0, constancia, new ArrayList<>());
    } catch (SOAPFaultException e) {
      
      if (e.getCause() instanceof com.ctc.wstx.exc.WstxIOException) {
        
        try {
          client1 = initWebService(2);
          
          String constancia = client1.sendPack(nombreArchivo, new DataHandler(source));
          
          return new Response(0, constancia, new ArrayList<>());
        }
        catch (SOAPFaultException e2) {
          
          if (e.getCause() instanceof org.apache.wss4j.common.ext.WSSecurityException) {
            
            if (e.getCause().getCause() != null)
            {
              throw new RuntimeException(e.getCause().getCause().getMessage(), e.getCause().getCause());
            }

            
            throw new RuntimeException(e.getCause().getMessage(), e.getCause());
          } 




          
          Response response = SOAPFaultToResponse(e2.getFault());
          return response;
        } 
      }
      
      return SOAPFaultToResponse(e.getFault());
    } 
  }




  
  public Response getStatus(String rutaArchivo, String ticket) {
    BillService client = initWebService(1);
    
    try {
      return getStatus(rutaArchivo, ticket, client);
    } catch (SOAPFaultException e) {
      
      if (e.getCause() instanceof com.ctc.wstx.exc.WstxIOException) {
        
        try {
          client = initWebService(2);
          return getStatus(rutaArchivo, ticket, client);
        } catch (SOAPFaultException e2) {
          
          if (e.getCause() instanceof org.apache.wss4j.common.ext.WSSecurityException) {
            
            if (e.getCause().getCause() != null)
            {
              throw new RuntimeException(e.getCause().getCause().getMessage(), e.getCause().getCause());
            }

            
            throw new RuntimeException(e.getCause().getMessage(), e.getCause());
          } 




          
          Response response = SOAPFaultToResponse(e2.getFault());
          return response;
        } 
      }
      
      return SOAPFaultToResponse(e.getFault());
    } 
  }



  
  private Response getStatus(String rutaArchivo, String ticket, BillService client) {
    StatusResponse stResponse = client.getStatus(ticket);
    
    Integer inCode = new Integer(stResponse.getStatusCode());
    if (inCode.equals(Integer.valueOf(98))) {
      return new Response(inCode.intValue(), "La transaccion \"" + ticket + "\", aun se esta procesando", null);
    }
    
    if (inCode.equals(Integer.valueOf(127))) {
      return new Response(inCode.intValue(), "El ticket \"" + ticket + "\" no existe", null);
    }
    
    byte[] constancia = stResponse.getContent();
    
    try {
      FileUtils.writeByteArrayToFile(new File(rutaArchivo + ticket + ".zip"), constancia);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return byteToResponse(constancia);
  }
  
  private BillService initWebService(int ind) {
    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
    Map<String, Object> props = new HashMap<>();
    props.put("mtom-enabled", Boolean.FALSE);
    factory.setProperties(props);
    
    factory.setAddress(this.curentURLService);
    factory.getInInterceptors().add(new LoggingInInterceptor());
    factory.getOutInterceptors().add(new LoggingOutInterceptor());
    
    factory.setServiceClass(BillService.class);
    
    BillService client = (BillService)factory.create();
    
    FiltersType filter = new FiltersType();
    filter.getInclude().add(".*_EXPORT_.*");
    filter.getInclude().add(".*_EXPORT1024_.*");
    filter.getInclude().add(".*_WITH_DES_.*");
    filter.getInclude().add(".*_WITH_NULL_.*");
    filter.getExclude().add(".*_DH_anon_.*");

    
    try {
      TLSClientParameters tlsParams = new TLSClientParameters();
      tlsParams.setDisableCNCheck(true);
      if (ind == 1) {
        tlsParams.setCipherSuites(Collections.singletonList("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256"));
      } else {
        tlsParams.setSecureSocketProtocol("TLSv1.2");
      } 
      
      tlsParams.setTrustManagers(getTrustManagers("truststore.jks"));
      tlsParams.setCipherSuitesFilter(filter);
      
      System.out.println(">> Usuario:" + this.usuario);
      configureSSLOnTheClient(client, this.usuario, tlsParams);
    } catch (WebserviceConfigurationException e1) {
      throw new RuntimeException(e1);
    } 
    
    return client;
  }

  
  private ResponseData searchResponse(ZipInputStream srcIs) throws ConstanciaException, IOException {
    ZipEntry entry = null;
    
    byte[] data = null;
    
    try {
      entry = srcIs.getNextEntry();
      
      if (entry.isDirectory()) {
        entry = srcIs.getNextEntry();
      }
      
      if (entry.getName().endsWith(".xml") || entry.getName().endsWith(".XML")) {
        data = getFileResponse(srcIs);
        return new ResponseData(data, TipoArchivo.XML);
      } 
      if (entry.getName().endsWith(".txt") || entry.getName().endsWith(".TXT")) {

        
        data = getFileResponse(srcIs);
        
        return new ResponseData(data, TipoArchivo.TXT);
      } 
      if (entry.getName().endsWith(".zip") || entry.getName().endsWith(".ZIP")) {
        
        do {
          entry = srcIs.getNextEntry();
          
          if (entry == null)
            throw new ConstanciaException("No se encontro el archivo detalle "); 
        } while (!entry.getName().endsWith(".txt") && !entry.getName().endsWith(".TXT"));

        
        data = getFileResponse(srcIs);
        
        return new ResponseData(data, TipoArchivo.TXT);
      } 





      
      throw new ConstanciaException("El archivo enviado no contiene el CDR en formato XML: " + entry.getName());
    }
    catch (IOException e) {
      throw new ConstanciaException("No se pudo leer el CDR: ", e);
    } 
  }

  /*     */ 
  
  class ResponseData
  {
    byte[] data;
    /*     */     
    SunatGEMServiceWrapper.TipoArchivo tipo;
    /*     */     
    ResponseData(byte[] data, SunatGEMServiceWrapper.TipoArchivo tipo) {
      this.data = data;
      this.tipo = tipo;
    }
  }
  
  public enum TipoArchivo {
    XML, TXT;
  }
  
  private static byte[] getFileResponse(ZipInputStream srcIs) throws IOException {
    ByteArrayOutputStream destOs = new ByteArrayOutputStream();
    
    int count = 0;
    byte[] buffer = new byte[2048];
    while ((count = srcIs.read(buffer)) > 0) {
      destOs.write(buffer, 0, count);
    }
    
    destOs.flush();
    
    byte[] data = destOs.toByteArray();
    
    destOs.close();
    
    destOs.close();
    
    srcIs.close();
    
    return data;
  }


  
  private static byte[] unzip(byte[] data) throws ConstanciaException {
    try {
      ByteArrayInputStream bis = new ByteArrayInputStream(data);
      ZipInputStream srcIs = new ZipInputStream(bis);
      
      ByteArrayOutputStream destOs = new ByteArrayOutputStream();
      ZipEntry entry = null;
      while (null != (entry = srcIs.getNextEntry())) {
        if (entry.getName().endsWith(".xml")) {
          
          int count = 0;
          byte[] buffer = new byte[2048];
          while ((count = srcIs.read(buffer)) > 0) {
            destOs.write(buffer, 0, count);
          }
        } 
      } 
      
      destOs.flush();
      byte[] b = destOs.toByteArray();
      destOs.close();
      
      destOs.close();
      srcIs.close();
      bis.close();
      return b;
    } catch (Exception e) {
      throw new ConstanciaException("Error al descomprimir la constancia", e);
    } 
  }






  
  private Document byteToDocument(byte[] data) throws ConstanciaException {
    InputStream ipEntrada = new ByteArrayInputStream(data);
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    try {
      Document doc = dbf.newDocumentBuilder().parse(ipEntrada);
      return doc;
    } catch (SAXException e) {
      throw new ConstanciaException("Error al leer la constancia", e);
    } catch (IOException e) {
      throw new ConstanciaException("Error al leer la constancia", e);
    } catch (ParserConfigurationException e) {
      throw new ConstanciaException("Error al leer la constancia", e);
    } 
  }
  
  private Response byteToResponse(byte[] data) {
    int intCode = -1;


    
    try {
      ByteArrayInputStream bis = new ByteArrayInputStream(data);
      
      ZipInputStream srcIs = new ZipInputStream(bis);
      
      ResponseData responseData = searchResponse(srcIs);
      
      switch (responseData.tipo) {
        case XML:
          return ResponseFromXmlFile(responseData.data);

        
        case TXT:
          return ResponseFromTxtFile(responseData.data);
      } 

      
      return new Response(intCode, "Tipo de archivo  de respuesta no soportado", null);
    
    }
    catch (ConstanciaException e) {
      e.printStackTrace();
      return new Response(intCode, e.getMessage(), null);
    } catch (Exception e) {
      e.printStackTrace();
      return new Response(intCode, "Error al momento de parsear la respuesta del comprobante: " + e
          .getMessage(), null);
    } 
  }
  
  private Response ResponseFromTxtFile(byte[] data) throws ConstanciaException {
    List<String> lstWarnings = new ArrayList<>();
    
    return new Response(0, new String(data), lstWarnings);
  }

  
  private Response ResponseFromXmlFile(byte[] data) throws ConstanciaException {
    Document doc = byteToDocument(data);
    
    XPath xPath = XPathFactory.newInstance().newXPath();
    
    int intCode = -1;
    
    String mensaje = "Error al tratar de leer la respuesta";

    
    try {
      String codigo = (String)xPath.evaluate("/*[local-name()='ApplicationResponse']/*[local-name()='DocumentResponse']/*[local-name()='Response']/*[local-name()='ResponseCode']/text()", doc
          
          .getDocumentElement(), XPathConstants.STRING);
      mensaje = (String)xPath.evaluate("/*[local-name()='ApplicationResponse']/*[local-name()='DocumentResponse']/*[local-name()='Response']/*[local-name()='Description']/text()", doc
          
          .getDocumentElement(), XPathConstants.STRING);

      
      NodeList warningsNode = (NodeList)xPath.evaluate("/*[local-name()='ApplicationResponse']/*[local-name()='Note']", doc
          .getDocumentElement(), XPathConstants.NODESET);

      
      List<String> lstWarnings = new ArrayList<>();
      for (int i = 0; i < warningsNode.getLength(); i++) {
        Node show = warningsNode.item(i);
        lstWarnings.add(show.getTextContent());
      } 
      
      try {
        intCode = (new Integer(codigo)).intValue();
      } catch (NumberFormatException ne) {
        throw new ConstanciaException("El codigo de error informado en la constancia esta vacia o no es un valor valido: " + codigo, ne);
      } 


      
      return new Response(intCode, mensaje, lstWarnings);
    }
    catch (XPathExpressionException e) {
      e.printStackTrace();
      return new Response(intCode, e.getMessage(), null);
    } 
  }


  
  private Response SOAPFaultToResponse(SOAPFault soapFault) {
    String retCode = soapFault.getFaultCode();
    String mensaje = soapFault.getFaultString();
    Detail detalle = soapFault.getDetail();
    
    String[] code = retCode.split("\\.");
    int intCode = -1;

    
    try {
      boolean isErrorFeGEM = StringUtils.isNumeric(code[code.length - 1]);
      
      boolean isErrorGreGEM = StringUtils.isNumeric(mensaje);
      
      if (isErrorFeGEM) {
        intCode = (new Integer(code[code.length - 1])).intValue();
      } else if (isErrorGreGEM) {
        
        intCode = (new Integer(mensaje)).intValue();
        Iterator<DetailEntry> entries = detalle.getDetailEntries();
        mensaje = "";
        while (entries.hasNext()) {
          DetailEntry newEntry = entries.next();
          mensaje = mensaje + "\n" + newEntry.getValue();
          System.out.println("  Detail entry = " + mensaje);
        }
      
      } 
    } catch (Throwable e) {
      e.printStackTrace();
      mensaje = "Ocurrio un error inesperado comuniquese con su administrador de red. " + mensaje;
    } 
    
    return new Response(intCode, mensaje, null);
  }

  
  private void configureSSLOnTheClient(Object c, String usuario, TLSClientParameters tlsParams) throws WebserviceConfigurationException {
    Client client = ClientProxy.getClient(c);








    
    HTTPConduit httpConduit = (HTTPConduit)client.getConduit();
    
    HTTPClientPolicy policy = new HTTPClientPolicy();
    policy.setConnectionTimeout(30000L);
    policy.setReceiveTimeout(180000L);
    policy.setAllowChunking(false);
    httpConduit.setClient(policy);
    
    httpConduit.setTlsClientParameters(tlsParams);

    
    WSS4JOutInterceptor wsOut = UserNamePasswordInterceptor(usuario);
    client.getEndpoint().getOutInterceptors().add(wsOut);
  }





  
  private WSS4JOutInterceptor UserNamePasswordInterceptor(String usuario) {
    Map<String, Object> outProps = new HashMap<>();
    outProps.put("action", "UsernameToken");
    
    outProps.put("user", usuario);
    
    outProps.put("passwordType", "PasswordText");
    
    outProps.put("passwordCallbackClass", ClientePasswordCallback.class.getName());
    
    WSS4JOutInterceptor wsOut = new WSS4JOutInterceptor(outProps);
    return wsOut;
  }












  
  private TrustManager[] getTrustManagers(String truststorepath) throws WebserviceConfigurationException {
    try {
      KeyStore keyStore = KeyStore.getInstance("JKS");
      String trustpass = "changeit";
      
      InputStream truststore = getClass().getClassLoader().getResourceAsStream(truststorepath);




      
      if (truststore == null) {
        throw new WebserviceConfigurationException("no se encontro el archivo " + truststorepath);
      }
      
      keyStore.load(truststore, trustpass.toCharArray());
      
      TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustFactory.init(keyStore);
      TrustManager[] tm = trustFactory.getTrustManagers();
      
      truststore.close();
      
      return tm;
    } catch (KeyStoreException kse) {
      System.out.println("Security configuration failed with the following: " + kse.getCause());
      throw new WebserviceConfigurationException("Security configuration failed", kse);
    } catch (NoSuchAlgorithmException nsa) {
      System.out.println("Security configuration failed with the following: " + nsa.getCause());
      throw new WebserviceConfigurationException("Security configuration failed", nsa);
    } catch (FileNotFoundException fnfe) {
      System.out.println("Security configuration failed with the following: " + fnfe.getCause());
      throw new WebserviceConfigurationException("Security configuration failed", fnfe);
    } catch (CertificateException ce) {
      System.out.println("Security configuration failed with the following: " + ce.getCause());
      throw new WebserviceConfigurationException("Security configuration failed", ce);
    } catch (IOException ioe) {
      System.out.println("Security configuration failed with the following: " + ioe.getCause());
      throw new WebserviceConfigurationException("Security configuration failed", ioe);
    } catch (Exception e) {
      if (e instanceof WebserviceConfigurationException) {
        throw (WebserviceConfigurationException)e;
      }
      System.out.println("Security configuration failed with the following: " + e.getCause());
      throw new WebserviceConfigurationException("Security configuration failed", e);
    } 
  }




  
  public static class TrustAllX509TrustManager
    implements X509TrustManager
  {
    private static final X509Certificate[] acceptedIssuers = new X509Certificate[0];









    
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}









    
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}








    
    public X509Certificate[] getAcceptedIssuers() {
      return acceptedIssuers;
    }
  }




  
  public static class FActuraElectronicaTrustManager
    implements X509TrustManager
  {
    private static final X509Certificate[] acceptedIssuers = new X509Certificate[0];









    
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}









    
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}








    
    public X509Certificate[] getAcceptedIssuers() {
      return acceptedIssuers;
    }
  }
}