package org.tempuri;

public class SitemapWSSoapProxy implements SitemapWSSoap {
  private String _endpoint = null;
  private SitemapWSSoap sitemapWSSoap = null;
  
  public SitemapWSSoapProxy() {
    _initSitemapWSSoapProxy();
  }
  
  public SitemapWSSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSitemapWSSoapProxy();
  }
  
  private void _initSitemapWSSoapProxy() {
    try {
      sitemapWSSoap = (new SitemapWSLocator()).getSitemapWSSoap();
      if (sitemapWSSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sitemapWSSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sitemapWSSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sitemapWSSoap != null)
      ((javax.xml.rpc.Stub)sitemapWSSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SitemapWSSoap getSitemapWSSoap() {
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap;
  }
  
  public String getPlainText(String strEncID, String strCompanyCode, String strEncText) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.getPlainText(strEncID, strCompanyCode, strEncText);
  }
  
  public String getEncText2(String strEncID, String strCompanyCode, String strPlainText) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.getEncText2(strEncID, strCompanyCode, strPlainText);
  }
  
  public String getEncText(String strEncText) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.getEncText(strEncText);
  }
  
  public String autowayEncryption(String strEncID, String strCompanyCode, String strPlainText) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.autowayEncryption(strEncID, strCompanyCode, strPlainText);
  }
  
  public String autowayDecryption(String strEncID, String strCompanyCode, String strEncText, String isCheckTime) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.autowayDecryption(strEncID, strCompanyCode, strEncText, isCheckTime);
  }
  
  
}