/**
 * SitemapWSSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface SitemapWSSoap extends java.rmi.Remote {
    public String getPlainText(String strEncID, String strCompanyCode, String strEncText) throws java.rmi.RemoteException;
    public String getEncText2(String strEncID, String strCompanyCode, String strPlainText) throws java.rmi.RemoteException;
    public String getEncText(String strEncText) throws java.rmi.RemoteException;
    public String autowayEncryption(String strEncID, String strCompanyCode, String strPlainText) throws java.rmi.RemoteException;
    public String autowayDecryption(String strEncID, String strCompanyCode, String strEncText, String isCheckTime) throws java.rmi.RemoteException;
}
