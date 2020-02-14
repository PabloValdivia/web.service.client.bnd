package co.globalqss.fedian.imp;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.felix.service.command.annotations.GogoCommand;
import org.apache.wss4j.common.crypto.Merlin;
import org.apache.wss4j.policy.SPConstants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;

import co.globalqss.fedian.api.CallService;
import colombia.dian.wcf.IWcfDianCustomerServices;
import colombia.dian.wcf.WcfDianCustomerServices;

@GogoCommand(scope = "service", function = "getStatus")
@Component
public class ServiceClient implements CallService {

	
	
	@Override
	public String getStatus() {
		ClassLoader currentThreadClassLoader = null;
		try {
			System.setProperty("javax.xml.soap.SAAJMetaFactory", "com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl");
			// use cxf stack
			System.setProperty("javax.xml.ws.spi.Provider", "org.apache.cxf.jaxws.spi.ProviderImpl");
			System.setProperty("javax.xml.bind.JAXBContextFactory", "com.sun.xml.bind.v2.JAXBContextFactory");
		    
			//String recepcionComprobantesQname = "http://wcf.dian.colombia";
			//String recepcionComprobantesService = "WcfDianCustomerServices";
			//QName qname = new QName(recepcionComprobantesQname, recepcionComprobantesService);
			
			currentThreadClassLoader =  Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			
			URL url = FrameworkUtil.getBundle(this.getClass()).getEntry("WcfDianCustomerServices.xml");
			
			WcfDianCustomerServices service = new WcfDianCustomerServices (url);
			
			LoggingFeature logRequestFeature = new LoggingFeature();
			logRequestFeature.setPrettyLogging(true);
			IWcfDianCustomerServices port = service.getWSHttpBindingIWcfDianCustomerServices(logRequestFeature);
			
			// secutity setting. it can be on extenal file
			Properties cerProperties = new Properties();
			cerProperties.put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
			cerProperties.put(Merlin.PREFIX + Merlin.KEYSTORE_TYPE, "PKCS12");
			cerProperties.put(Merlin.PREFIX + Merlin.KEYSTORE_FILE, "Certificado.pfx");
			cerProperties.put(Merlin.PREFIX + Merlin.KEYSTORE_PASSWORD, Merlin.ENCRYPTED_PASSWORD_PREFIX + CerPasswordCallback.encrytedPassword + Merlin.ENCRYPTED_PASSWORD_SUFFIX);
			
			Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
			
			ctx.put(SecurityConstants.SIGNATURE_PROPERTIES, cerProperties);
			ctx.put(SecurityConstants.CALLBACK_HANDLER, CerPasswordCallback.class.getName());
			ctx.put(SecurityConstants.SIGNATURE_USERNAME, " quality systems  solutions qss ltda");
			ctx.put(SecurityConstants.ASYMMETRIC_SIGNATURE_ALGORITHM, SPConstants.RSA_SHA256);
			
			port.getStatus("bc84a90ee7ae3bcd05961195cb5391dbce7271016978f70648c530ef9b6f87b723fce42167c774b958382d3423d28fc2");
			
			return "success";
		}catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}finally {
			if (currentThreadClassLoader != null && currentThreadClassLoader != Thread.currentThread().getContextClassLoader())
				Thread.currentThread().setContextClassLoader(currentThreadClassLoader);
		}
	}

}
