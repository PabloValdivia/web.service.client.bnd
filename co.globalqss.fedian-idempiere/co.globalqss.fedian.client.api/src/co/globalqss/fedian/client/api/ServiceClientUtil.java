package co.globalqss.fedian.client.api;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.wss4j.common.crypto.Merlin;
import org.apache.wss4j.policy.SPConstants;
import org.osgi.framework.FrameworkUtil;

import colombia.dian.wcf.IWcfDianCustomerServices;
import colombia.dian.wcf.WcfDianCustomerServices;

public class ServiceClientUtil {
	public static IWcfDianCustomerServices getClientProxy (boolean isFineClassload) {
		ClassLoader currentThreadClassLoader = null;
		try {
			System.setProperty("javax.xml.soap.SAAJMetaFactory", "com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl");
			// use cxf stack
			System.setProperty("javax.xml.ws.spi.Provider", "org.apache.cxf.jaxws.spi.ProviderImpl");
			//System.setProperty("javax.xml.bind.JAXBContextFactory", "com.sun.xml.bind.v2.JAXBContextFactory");
		    
			if (!isFineClassload) {
				currentThreadClassLoader =  Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(ServiceClientUtil.class.getClassLoader());
			}
			
			URL url = FrameworkUtil.getBundle(ServiceClientUtil.class).getEntry("WcfDianCustomerServices.xml");

			WcfDianCustomerServices service = new WcfDianCustomerServices (url);
			
			LoggingFeature logRequestFeature = new LoggingFeature();
			logRequestFeature.setPrettyLogging(true);
			IWcfDianCustomerServices port = service.getWSHttpBindingIWcfDianCustomerServices(logRequestFeature);
			
			// secutity setting. it can be on extenal file
			Properties cerProperties = new Properties();
			cerProperties.put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
			cerProperties.put(Merlin.PREFIX + Merlin.KEYSTORE_TYPE, "PKCS12");
			
			// Certificado.pfx on bundle of getBus().getExtension(ClassLoader.class)
			// on idempiere Bus manage is SpringBus, so cxf class will load by this ClassLoad getBus().getExtension(ClassLoader.class)
			// it's org.idempiere.service class load
			String cerPath = "bundleentry://" + FrameworkUtil.getBundle(ServiceClientUtil.class).getBundleId() + "/Certificado.pfx";
			cerProperties.put(Merlin.PREFIX + Merlin.KEYSTORE_FILE, cerPath);
			cerProperties.put(Merlin.PREFIX + Merlin.KEYSTORE_PASSWORD, Merlin.ENCRYPTED_PASSWORD_PREFIX + CerPasswordCallback.getEncrytedPassword() + Merlin.ENCRYPTED_PASSWORD_SUFFIX);
			
			// on idempiere Bus manage is SpringBus, so cxf class will load by this ClassLoad getBus().getExtension(ClassLoader.class)
			// it's org.idempiere.service class load
			Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
			
			ctx.put(SecurityConstants.SIGNATURE_PROPERTIES, cerProperties);
			//ctx.put(SecurityConstants.CALLBACK_HANDLER, CerPasswordCallback.class.getName());
			ctx.put(SecurityConstants.CALLBACK_HANDLER, new CerPasswordCallback());
			ctx.put(SecurityConstants.SIGNATURE_USERNAME, " quality systems  solutions qss ltda");
			ctx.put(SecurityConstants.ASYMMETRIC_SIGNATURE_ALGORITHM, SPConstants.RSA_SHA256);
			
			return port;
		}finally {
			if (currentThreadClassLoader != null && currentThreadClassLoader != Thread.currentThread().getContextClassLoader())
				Thread.currentThread().setContextClassLoader(currentThreadClassLoader);
		}
	}
	
	public static void getStatus (boolean isFineClassload) {
		ClassLoader currentThreadClassLoader = null;
		try {
			if (!isFineClassload) {
				currentThreadClassLoader =  Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(ServiceClientUtil.class.getClassLoader());
			}
			
			IWcfDianCustomerServices  port = getClientProxy (isFineClassload);
			port.getStatus("bc84a90ee7ae3bcd05961195cb5391dbce7271016978f70648c530ef9b6f87b723fce42167c774b958382d3423d28fc2");
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if (currentThreadClassLoader != null && currentThreadClassLoader != Thread.currentThread().getContextClassLoader())
				Thread.currentThread().setContextClassLoader(currentThreadClassLoader);
		}
	}
}
