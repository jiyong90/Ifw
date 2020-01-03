/**
 * SitemapWSSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface SitemapWSSoap extends java.rmi.Remote {
    public java.lang.String getPlainText(java.lang.String strEncID, java.lang.String strCompanyCode, java.lang.String strEncText) throws java.rmi.RemoteException;
    public java.lang.String getEncText2(java.lang.String strEncID, java.lang.String strCompanyCode, java.lang.String strPlainText) throws java.rmi.RemoteException;
    public java.lang.String getEncText(java.lang.String strEncText) throws java.rmi.RemoteException;
    public java.lang.String autowayEncryption(java.lang.String strEncID, java.lang.String strCompanyCode, java.lang.String strPlainText) throws java.rmi.RemoteException;
    public java.lang.String autowayDecryption(java.lang.String strEncID, java.lang.String strCompanyCode, java.lang.String strEncText, java.lang.String isCheckTime) throws java.rmi.RemoteException;
}
