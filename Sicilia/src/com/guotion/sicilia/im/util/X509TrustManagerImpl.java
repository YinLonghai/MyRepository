package com.guotion.sicilia.im.util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created with IntelliJ IDEA. User: Administrator Date: 14-4-14 Time: 下午10:32
 * To change this template use File | Settings | File Templates.
 */
public class X509TrustManagerImpl implements X509TrustManager {
	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
			throws CertificateException {
		// ignore
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
			throws CertificateException {
		// ignore
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
