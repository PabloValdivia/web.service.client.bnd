package co.globalqss.fedian.imp;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;

import org.apache.cxf.jaxws.spi.ProviderImpl;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.felix.service.command.annotations.GogoCommand;
import org.datacontract.schemas._2004._07.dianresponse.DianResponse;
import org.osgi.service.component.annotations.Component;

import co.globalqss.fedian.api.CallService;
import colombia.dian.wcf.IWcfDianCustomerServices;
import colombia.dian.wcf.WcfDianCustomerServices;

@GogoCommand(scope = "CallService", function = "getStatus")
@Component
public class TestCallService implements CallService{

	@Override
	public String getStatus() {
		ClassLoader currentThreadClassLoader = null;
		try {
			
			System.setProperty("javax.xml.soap.SAAJMetaFactory", "com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl");
			System.setProperty("javax.xml.ws.spi.Provider", "org.apache.cxf.jaxws.spi.ProviderImpl");
			System.setProperty("javax.xml.bind.JAXBContextFactory", "com.sun.xml.bind.v2.JAXBContextFactory");
			
			String wsdlLocation = "https://vpfe-hab.dian.gov.co/WcfDianCustomerServices.svc?wsdl";
			String recepcionComprobantesQname = "http://wcf.dian.colombia";
			String recepcionComprobantesService = "WcfDianCustomerServices";
			QName qname = new QName(recepcionComprobantesQname, recepcionComprobantesService);
			URL url = new URL(wsdlLocation);
			System.out.println(ProviderImpl.class.getClassLoader());
			currentThreadClassLoader =  Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			WcfDianCustomerServices service = new WcfDianCustomerServices (url, qname);
			
			IWcfDianCustomerServices port = null;
			
			
			// don't change thread class load https://stackoverflow.com/a/39251733
			/*JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.getClientFactoryBean().getServiceFactory().setWsdlURL(WcfDianCustomerServices.WSDL_LOCATION);
			factory.setServiceName(WcfDianCustomerServices.SERVICE);
			//factory.setEndpointName(WcfDianCustomerServices.WinRmPort);
			// factory.setFeatures(...);  // if required
			WebServiceFeature serviceCxf = factory.create(IWcfDianCustomerServices.class);        
			Client client = ClientProxy.getClient(winrm);*/
	
			WebServiceFeature address = new AddressingFeature(true, true, AddressingFeature.Responses.ANONYMOUS);
			port = service.getWSHttpBindingIWcfDianCustomerServices(address);


			Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
			/************
			 * Timeout
			 * 1. see sun.net.NetworkClient. time out can be setting by system properties "sun.net.client.defaultReadTimeout" and "sun.net.client.defaultConnectTimeout"
			 * 2. for cfx can be override <1> by bellow code: from https://stackoverflow.com/a/38732953
			    Client client = ClientProxy.getClient(port);
				HTTPConduit httpConduit = (HTTPConduit)client.getConduit();
				HTTPClientPolicy policy = httpConduit.getClient();
				policy.setReceiveTimeout(0);
				policy.setConnectionRequestTimeout()
				policy.setConnectionTimeout();
			 * 3. for metro use context properties
			    ctx.put(BindingProviderProperties.CONNECT_TIMEOUT, 10000);
			    ctx.put(BindingProviderProperties.REQUEST_TIMEOUT, 10000);
			 */
			
			/********
			 * Setting Addressing Feature: https://cxf.apache.org/docs/ws-addressing.html
			 * 1. cxf
			    ctx.put("org.apache.cxf.ws.addressing.replyto", "https://vpfe-hab.dian.gov.co/WcfDianCustomerServices.svcx");
			 * more attribution also more flexible way for cxf
				AddressingProperties maps = new AddressingPropertiesImpl();
				EndpointReferenceType ref = new EndpointReferenceType();
				AttributedURIType add = new AttributedURIType();
				add.setValue("http://localhost:9090/decoupled_endpoint");
				ref.setAddress(add);
				maps.setReplyTo(ref);
				maps.setFaultTo(ref);
				((BindingProvider)port).getRequestContext().put("javax.xml.ws.addressing.context", maps);
			 */
			AddressingProperties maps = new AddressingProperties();
			EndpointReferenceType ref = new EndpointReferenceType();
			AttributedURIType action = new AttributedURIType();
			action.setValue("http://wcf.dian.colombia/IWcfDianCustomerServices/GetStatus");
			maps.setAction(action);
			
			// on SoapUI this value is "add default wsa:to"
			AttributedURIType to = new AttributedURIType();
			to.setValue("https://vpfe-hab.dian.gov.co/WcfDianCustomerServices.svc");
			maps.setTo(to);
			
			maps.setRequired(true);

			ctx.put("javax.xml.ws.addressing.context", maps);
			
			// TODO:at moment ever has org.apache.felix.http.api, HTTPTransportActivator don wire to package org.osgi.service.http.HttpService, so it can't load
			// temp disable http.transport for test, fix dependency later
			System.setProperty("org.apache.cxf.osgi.http.transport.disable", "true");
			
			DianResponse status = port.getStatus("bc84a90ee7ae3bcd05961195cb5391dbce7271016978f70648c530ef9b6f87b723fce42167c774b958382d3423d28fc2");
			
			return "ok";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}finally {
			if (currentThreadClassLoader != null && currentThreadClassLoader != Thread.currentThread().getContextClassLoader())
				Thread.currentThread().setContextClassLoader(currentThreadClassLoader);
		}
	}


}
