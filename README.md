# web.service.client.bnd
user cxf to call service with security policy

# setup environment (run standalone)
1. workspace setup with java-11 Bndtools 5.0 and eclipse 2019-12 (2018-12 is lowest eclipse version for bndtool). so need eclipse with Bndtools
1. clone and checkout this repository
1. open terminal window
1. generate a complicate password and run **export jasyptMasterPassword="master password"**
1. run eclipse from this terminal to has jasyptMasterPassword on environment variable
1. import this workspace (import exists bnd workspace)
1. push your keystore to co.globalqss.fedian.application/Certificado.pfx
1. on eclipse open co.globalqss.fedian.application/co.globalqss.fedian.application.bndrun
1. at tab "run" click "debug osgi" to run program
1. at console type: **Tool:encryptPassword 'your password'**
1. the text generate on console is your "encryted password", remember it.
1. exits eclipse but keep teminal open. type **export keystoreEncrytedPassword="encrypted password"**
1. reopen eclipse (next time you always run eclipse after export keystoreEncrytedPassword and jasyptMasterPassword)
1. run "debug osgi" again
1. at console type **service:getStatus** to test call webservice
1. in case success. can confirm response log on console by scroll up a bit

# setup environment (run on idempiere)
1. clone this reposiotry to folder [web.service.client.bnd]
1. push your keystore  [web.service.client.bnd]/co.globalqss.fedian-idempiere/co.globalqss.fedian.client.api/Certificado.pfx
1. open idempiere (7.1.0) on eclipse
1. import all maven project from [web.service.client.bnd]/co.globalqss.fedian-idempiere
1. include feature co.globalqss.fedian.client-feature into org.adempiere.base-feature
1. open org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target:
    1. change *org.idempiere.webservice.client-* to https://raw.githubusercontent.com/hieplq/idempiere.artifact/cxf-3.3.1-service.client/m2p/org.idempiere.webservice.client
    1. rechoose all bundle on update repository
    1. reload all repository
    1. reload target platform
1. bundle org.idempiere.webservices add more import package "com.sun.xml.messaging.saaj.soap"
1. set trace level to "info" to output request/response log
1. open "org.adempiere.server-feature/server.product" 
    1. on launching tab at "VM agruments" append **-DJasyptPasswordEncryptor.master.password="Your Master Password" -DJasyptPasswordEncryptor.encrytedPassword.keystore="Your Encryped Password"**
    1. on overview tag, click "Launch an eclipse application on debug mode" to update set of new plugins and "VM agruments" to server.product launching
1. run test:
    1. case 1: at console type service:getStatus to test. confirm result at console log
    2. case 2: open idempiere web ui, run process "Translation Doc Sync". confirm result at console log
    
# Note
## What's difference when run at stanalone and inside idempiere
1. bus manage extension
    1. when run by standalone mode, ExtensionManagerBus is used
    1. when run inside idempiere, SpringBus is used
1. class loader inside service.getWSHttpBindingIWcfDianCustomerServices()
    1. when run by standalone mode, class loader don't change
    1. when run inside idempiere, class loader change to "org.idempiere.webservice" by SpringBus at point call JaxWsProxyFactoryBean.create
1. CerPasswordCallback
    1. when run by standalone mode, class loader isn't change, so code inside cxf can instance CerPasswordCallback, so just pass class name to SecurityConstants.CALLBACK_HANDLER
    1. when run inside idempiere, class loader change to "org.idempiere.webservice", so need to instance CerPasswordCallback outside and pass instance reference to SecurityConstants.CALLBACK_HANDLER
    
# project inside web.service.client.bnd/co.globalqss.fedian-idempiere
1. co.client.ws.soap.fe.dian is code gen from wsdl. can re-generate by [web.service.client.bnd]/co.client.ws.soap.fe.dian/pom.xml
2. co.globalqss.fedian.client.api is commond code for call web service
3. co.globalqss.fedian.client is a osgi service function to demo call web service from osgi console
4. co.globalqss.fedian.client.process is a idempiere process to demo call web service from idempiere webui
5. org.opensaml.saml.common.fake give empty package relate opensaml, to cut off requirement of cxf library
