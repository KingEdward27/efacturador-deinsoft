package com.deinsoft.efacturador3.signer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SignXmlDocument implements SignDocument
{
  public ByteArrayOutputStream signDocumento(InputStream inDocument) throws KeyStoreException, SignDocumentException {
    KeystoreSunat ks = new KeystoreSunat();
    PrivateKey keyEntry = ks.getPrivateKey();
    
    X509Certificate cert = ks.getCertificate();
    ByteArrayOutputStream signatureFile = new ByteArrayOutputStream();





    
    try {
      Document doc = buildDocument(inDocument);
      Node parentNode = addExtensionContent(doc);
      
      String idReference = "SignSUNAT";
      
      XMLSignatureFactory fac = XMLSignatureFactory.getInstance();
      
      Reference ref = fac.newReference("", fac.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", null), 
          Collections.singletonList(fac.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", (TransformParameterSpec)null)), null, null);
      
      SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null), fac
          .newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null), Collections.singletonList(ref));



      
      KeyInfoFactory kif = fac.getKeyInfoFactory();
      List<X509Certificate> x509Content = new ArrayList<>();
      x509Content.add(cert);
      X509Data xd = kif.newX509Data(x509Content);
      KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));
      
      DOMSignContext dsc = new DOMSignContext(keyEntry, doc.getDocumentElement());
      XMLSignature signature = fac.newXMLSignature(si, ki);
      if (parentNode != null)
        dsc.setParent(parentNode); 
      dsc.setDefaultNamespacePrefix("ds");
      signature.sign(dsc);
      Element elementParent = (Element)dsc.getParent();
      if (idReference != null && elementParent.getElementsByTagName("ds:Signature") != null) {
        Element elementSignature = (Element)elementParent.getElementsByTagName("ds:Signature").item(0);
        elementSignature.setAttribute("Id", idReference);
      } 




      
      DOMUtils.outputDocToOutputStream(doc, signatureFile);
      signatureFile.close();
      return signatureFile;
    } catch (TransformerException e) {
      throw new SignDocumentException(e);
    } catch (NoSuchAlgorithmException e) {
      throw new SignDocumentException(e);
    } catch (InvalidAlgorithmParameterException e) {
      throw new SignDocumentException(e);
    } catch (MarshalException e) {
      throw new SignDocumentException(e);
    } catch (XMLSignatureException e) {
      throw new SignDocumentException(e);
    } catch (IOException e) {
      throw new SignDocumentException(e);
    } 
  }







  
  private Document buildDocument(InputStream inDocument) throws SignDocumentException {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(inDocument);
      return doc;
    } catch (ParserConfigurationException e) {
      throw new SignDocumentException(e);
    } catch (SAXException e) {
      throw new SignDocumentException(e);
    } catch (IOException e) {
      throw new SignDocumentException(e);
    } 
  }
  
  protected Node addExtensionContent(Document doc) {
    NodeList nodeList = doc.getDocumentElement().getElementsByTagNameNS("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2", "UBLExtensions");
    Node extensions = nodeList.item(0);
    
    Node extension = doc.createElement(extensions.getPrefix() + ":UBLExtension");
    Node content = doc.createElement(extensions.getPrefix() + ":ExtensionContent");
    extension.appendChild(content);
    extensions.appendChild(extension);
    return content;
  }
}
