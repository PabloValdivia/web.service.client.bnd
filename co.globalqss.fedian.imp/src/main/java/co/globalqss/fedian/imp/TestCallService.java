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
/**
 * TODO: update cxf to 3.3.2 for 	https://issues.apache.org/jira/browse/CXF-8010 
 * 									http://coheigea.blogspot.com/2019/04/performance-gain-for-web-service.html
 * @author hieplq
 *
 */
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
			
			//String wsdlLocation = "https://vpfe-hab.dian.gov.co/WcfDianCustomerServices.svc?wsdl";
		    URL url = FrameworkUtil.getBundle(this.getClass()).getEntry("WcfDianCustomerServices.xml");		
		    
			currentThreadClassLoader =  Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			WcfDianCustomerServices service = new WcfDianCustomerServices (url);
			
			IWcfDianCustomerServices port = null;
			
			
			// don't change thread class load https://stackoverflow.com/a/39251733
			/*JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.getClientFactoryBean().getServiceFactory().setWsdlURL(WcfDianCustomerServices.WSDL_LOCATION);
			factory.setServiceName(WcfDianCustomerServices.SERVICE);
			//factory.setEndpointName(WcfDianCustomerServices.WinRmPort);
			// factory.setFeatures(...);  // if required
			WebServiceFeature serviceCxf = factory.create(IWcfDianCustomerServices.class);        
			Client client = ClientProxy.getClient(winrm);*/
	
			// secure policy can parse form 
			// WebServiceFeature address = new AddressingFeature(true, true, AddressingFeature.Responses.ANONYMOUS);
			
			// log request https://cxf.apache.org/docs/message-logging.html
			LoggingFeature logRequestFeature = new LoggingFeature();
			logRequestFeature.setPrettyLogging(true);
			port = service.getWSHttpBindingIWcfDianCustomerServices(logRequestFeature);


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
			/*AddressingProperties maps = new AddressingProperties();
			AttributedURIType action = new AttributedURIType();
			action.setValue("http://wcf.dian.colombia/IWcfDianCustomerServices/GetStatus");
			maps.setAction(action);
			
			// on SoapUI this value is "add default wsa:to"
			AttributedURIType to = new AttributedURIType();
			to.setValue("https://vpfe-hab.dian.gov.co/WcfDianCustomerServices.svc");
			maps.setTo(to);
			
			maps.setRequired(true);

			ctx.put("javax.xml.ws.addressing.context", maps);*/
			
			/******** http://cxf.apache.org/docs/ws-securitypolicy.html
			 *        http://cxf.apache.org/docs/security-configuration.html
			 *        http://ws.apache.org/wss4j/topics.html
			 * refe: https://stackoverflow.com/a/48996571
			 *       https://stackoverflow.com/a/41463606
			 * Security policy
			 * 
			 */
		    // Client client = ClientProxy.getClient(port);
		    /*
		     * http://ws.apache.org/wss4j/config.html
		     * http://ws.apache.org/wss4j/topics.html#Crypto_Interface
		     */
		    Properties cerProperties = new Properties() {/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			{
                put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
                put(Merlin.PREFIX + Merlin.KEYSTORE_TYPE, "PKCS12");// JKS is default format on jdk until jdk8, from jdk9 then default is PKCS12 (our case if PKCS12)
                // put(Merlin.PREFIX + Merlin.KEYSTORE_ALIAS, " quality systems  solutions qss ltda"); our keystore hasn't alias
                //TODO: relative path
                put(Merlin.PREFIX + Merlin.KEYSTORE_FILE, "Certificado.pfx");
                
                //encrypted keystore password 
                //This holds a reference to a PasswordEncryptor instance, which is used to encrypt or decrypt passwords in the Merlin Crypto implementation (or any custom Crypto implementations). By default, WSS4J uses the JasyptPasswordEncryptor, which must be instantiated with a master password to use to decrypt keystore passwords in the Merlin Crypto properties file. This master password is obtained via the CallbackHandler defined via PW_CALLBACK_CLASS or PW_CALLBACK_REF. The encrypted passwords must be stored in the format "ENC(encoded encrypted password)".
                put(Merlin.PREFIX + Merlin.KEYSTORE_PASSWORD, Merlin.ENCRYPTED_PASSWORD_PREFIX + CerPasswordCallback.encrytedPassword + Merlin.ENCRYPTED_PASSWORD_SUFFIX);
                 
                // password to get private key for signature.
                // by Merlin we use this method to use PasswordEncryptor method. on our case password same keystore password.
                // by general we can use CallbackHandler with condition WSPasswordCallback.getUsage() == WSPasswordCallback.SIGNATURE to provide this password
                // UPDATE pending to investigate: at moment, this method get issue, because one PasswordEncryptor can't call two time (exception Encryption entity already initialized)
                // put(Merlin.PREFIX + Merlin.KEYSTORE_PRIVATE_PASSWORD, Merlin.ENCRYPTED_PASSWORD_PREFIX + "z82XxI7fV9r4j/rZwUk6NMJcVIgWpNLRi/awQX9RsF0=" + Merlin.ENCRYPTED_PASSWORD_SUFFIX);
            }};
		    // TODO check to see it's same port.getRequestContext();
            ctx.put(SecurityConstants.SIGNATURE_PROPERTIES, cerProperties);
		    //client.getRequestContext().put(SecurityConstants.ENCRYPT_PROPERTIES, cerProperties);
            ctx.put(SecurityConstants.CALLBACK_HANDLER, CerPasswordCallback.class.getName());
		    
		    /*
		     * Setting alias name
		     * Merlin.PREFIX + Merlin.KEYSTORE_ALIAS is trim on org.apache.wss4j.common.crypto.Merlin at "alias = alias.trim();" so name with space at start like our case will wrong afterward
		     * so set SecurityConstants.SIGNATURE_USERNAME also
		     * 
		     * our case has only one cer inside keystore, in case we don't set this value, it's ok, because Merlin engine call crypto.getDefaultX509Identifier() to get it
		     */
            ctx.put(SecurityConstants.SIGNATURE_USERNAME, " quality systems  solutions qss ltda");
		    
		    // TODO:at moment ever has org.apache.felix.http.api, HTTPTransportActivator don wire to package org.osgi.service.http.HttpService, so it can't load
			// temp disable http.transport for test, fix dependency later
			//System.setProperty("org.apache.cxf.osgi.http.transport.disable", "true");
			
			/*#########################
			* all properties for control wss4j https://ws.apache.org/wss4j/config.html
			*/
			
			/*
			 * Key identify type on soapUI mean https://ws.apache.org/wss4j/config.html#keyidentifier_values
			 * soapUI raw request generate below value 
			 * 		<ds:KeyInfo Id="KI-FDCB185CE92CFBBF8E15813901164282"><wsse:SecurityTokenReference wsu:Id="STR-FDCB185CE92CFBBF8E15813901164293"><wsse:Reference URI="#X509-FDCB185CE92CFBBF8E15813901164121"
			 * 		and <wsse:BinarySecurityToken EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary"
			 * map with http://coheigea.blogspot.com/2013/03/signature-and-encryption-key.html
			 * can see Key identify is DirectReference 
			 */
		    /* 
		     * update: security policy passing wsdl and and recognize this value as WSConstants.THUMBPRINT_IDENTIFIER
		     *	
		     *	xml tag <wsp:Policy><wsp:ExactlyOne><wsp:All><sp:EndorsingSupportingTokens><wsp:Policy><sp:X509Token><wsp:Policy><sp:RequireThumbprintReference/>
		     *	code:AbstractBindingBuilder.setKeyIdentifierType
		     *  
		     *  setting ConfigurationConstants.SIG_KEY_ID on request context don't force reset value.
		     *  
		     *  solution:offline wsdl and modify it to parsing can setting value like soupUI
		     *  	download and save to /co.globalqss.fedian.imp/src/main/resources/WcfDianCustomerServices.xml
		     *  	delete tag <wsp:Policy><wsp:ExactlyOne><wsp:All><sp:EndorsingSupportingTokens><wsp:Policy><sp:X509Token><wsp:Policy><sp:RequireThumbprintReference/>
		    */ 
			// client.getRequestContext().put(ConfigurationConstants.SIG_KEY_ID, WSConstants. BST_DIRECT_REFERENCE); 

            //ctx.put(ConfigurationConstants.ACTION, ConfigurationConstants.SIGNATURE + " " + ConfigurationConstants.TIMESTAMP);
            //ctx.put(ConfigurationConstants.TIMESTAMP_PRECISION, true);
            //ctx.put(ConfigurationConstants.TTL_TIMESTAMP, "60000");
			/*
			 * Set signature algorithm
			 * key get from SecurityConstants, value get from SPConstants
			 * 
			 * Algorithm suite setting on wsdl by tab <wsp:Policy><wsp:ExactlyOne><wsp:All><sp:TransportBinding><<wsp:Policy><sp:AlgorithmSuite><wsp:Policy><sp:Basic256Sha256Rsa15/>
			 * the suite Basic256Sha256Rsa15 declare on org.apache.wss4j.policy.model.AlgorithmSuite.ALGORITHM_SUITE_TYPES
			 * on this suite, asymmetric signature algorithm is SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
			 * but actual server use asymmetric signature algorithm is RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"
			 * at the moment no predefine suite on org.apache.wss4j.policy.model.AlgorithmSuite.ALGORITHM_SUITE_TYPES use RSA_SHA256
			 * so can't modify wsdl for this issue.
			 * luckily, we can override asymmetric signature algorithm value by setting request context
			 */
            ctx.put(SecurityConstants.ASYMMETRIC_SIGNATURE_ALGORITHM, SPConstants.RSA_SHA256);
			
			/**
			 * 	ClientImpl.doInvoke
			 * 		prepareConduitSelector ()			parse wsdl
			 * 		modifyChain append customize 		Interceptor
			 * 		PhaseInterceptorChain.doIntercept	build complete request message
			 * 		processResult
			 */
			
			/**
			 * 	First run, parse wsdl to build up configuration (focus security policy)
			 * 	PolicyBuilder.processOperationElement() build up security policy
			 * 		processOperationElement(Object operationElement, PolicyOperator operator) loop recursive to parse security policy from node <wsp:Policy
			 * 			<sp:TransportBinding 	TransportBindingBuilder.build () => TransportBinding
			 * 				<sp:TransportToken
			 * 				<sp:AlgorithmSuite	AlgorithmSuite.ALGORITHM_SUITE_TYPES[SPConstants.ALGO_SUITE_BASIC256_SHA256_RSA15]
			 * 				<sp:Layout
			 * 				<sp:IncludeTimestamp
			 * 				<!-- TransportToken more setup inside TransportBinding.parseNestedPolicy -->
			 * 			<sp:EndorsingSupportingTokens 	SupportingTokensBuilder.build => SupportingTokens
			 * 				<sp:X509Token 		X509TokenBuilder.build () => X509Token
			 * 				<sp:SignedParts		SignedPartsBuilder.build () => SignedParts
			 * 				<!-- X509Token, SignedParts more setup inside SupportingTokens.parseNestedPolicy -->
			 * 			<sp:Wss11				WSS11Builder.build => Wss11
			 * 			<sp:Trust10				Trust10Builder.build => Trust10
			 * 		
			 */
			
			/**
			 * 	PolicyBasedWSS4JOutInterceptorInternal.handleMessageInternal start to handle security policy
			 * 		WSSecHeader.insertSecurityHeader
			 * 			WSSecurityUtil.findWsseSecurityHeaderBlock will find security header, in case not found it add default 
			 * 				<wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
			 * 			XMLUtils.setNamespace(securityHeader, WSConstants.WSU_NS, WSConstants.WSU_PREFIX); append attribute 
			 * 				xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd
			 * 		init WSSConfig with default configuration (properties, object, provider,..) to handle security policy
			 */
			port.getStatus("bc84a90ee7ae3bcd05961195cb5391dbce7271016978f70648c530ef9b6f87b723fce42167c774b958382d3423d28fc2");
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
