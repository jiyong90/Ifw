package org.tempuri;

public class SitemapWSSoapProxy implements org.tempuri.SitemapWSSoap {
  private String _endpoint = null;
  private org.tempuri.SitemapWSSoap sitemapWSSoap = null;
  
  public SitemapWSSoapProxy() {
    _initSitemapWSSoapProxy();
  }
  
  public SitemapWSSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSitemapWSSoapProxy();
  }
  
  private void _initSitemapWSSoapProxy() {
    try {
      sitemapWSSoap = (new org.tempuri.SitemapWSLocator()).getSitemapWSSoap();
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
  
  public org.tempuri.SitemapWSSoap getSitemapWSSoap() {
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap;
  }
  
  public java.lang.String getPlainText(java.lang.String strEncID, java.lang.String strCompanyCode, java.lang.String strEncText) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.getPlainText(strEncID, strCompanyCode, strEncText);
  }
  
  public java.lang.String getEncText2(java.lang.String strEncID, java.lang.String strCompanyCode, java.lang.String strPlainText) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.getEncText2(strEncID, strCompanyCode, strPlainText);
  }
  
  public java.lang.String getEncText(java.lang.String strEncText) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.getEncText(strEncText);
  }
  
  public java.lang.String autowayEncryption(java.lang.String strEncID, java.lang.String strCompanyCode, java.lang.String strPlainText) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.autowayEncryption(strEncID, strCompanyCode, strPlainText);
  }
  
  public java.lang.String autowayDecryption(java.lang.String strEncID, java.lang.String strCompanyCode, java.lang.String strEncText, java.lang.String isCheckTime) throws java.rmi.RemoteException{
    if (sitemapWSSoap == null)
      _initSitemapWSSoapProxy();
    return sitemapWSSoap.autowayDecryption(strEncID, strCompanyCode, strEncText, isCheckTime);
  }
  
  
}