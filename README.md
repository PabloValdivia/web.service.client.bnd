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
    1. add new software site with url https://raw.githubusercontent.com/hieplq/idempiere.artifact/cxf-3.3.1-service.client/m2p/org.idempiere.webservice.client
    1. reload target platform
1. set trace level to "info" to output request/response log
1. open "org.adempiere.server-feature/server.product" 
    1. on overview tag, click "Launch an eclipse application on debug mode" to update set of new plugins to server.product launching
1. run test:
    1. at console type tool:setMasterPass 'your master password'
    1. at console type tool:setEncryptedKeystorePass 'your encrypt'
    1. at console type instead of tool:setEncryptedKeystorePass you can use tool:setPlanKeystorePass 'your plain keystore password'
    1. case 1: at console type service:getStatus to test. confirm result at console log
    1. case 2: open idempiere web ui, run process "Translation Doc Sync". confirm result at console log

# for build with command line. do follow step
1. download file https://raw.githubusercontent.com/hieplq/web.service.client.bnd/master/setupEnv.sh to a folder [root]
1. clone idempiere to [root]/idempiere by run `git clone git@github.com:idempiere/idempiere.git [root]/idempiere`
1. clone web.service.client.bnd to [root]/web.service.client.bnd git by run `clone git@github.com:hieplq/web.service.client.bnd.git`
1. run `sh setupEnv.sh` to
    1. append new p2 repository to org.idempiere.p2.targetplatform.target
    1. include co.globalqss.fedian.client-feature on binary
1. place your file keystore to [root]/web.service.client.bnd/co.globalqss.fedian-idempiere/co.globalqss.fedian.client.api/Certificado.pfx
1. get rid of depend parent pom from extra plugin
    1. cd [root]/idempiere/org.idempiere.parent
    1. mvn install
1. build product by run `mvn validate`
1. wait for build after that you can run server by 
    1. cd idempiere/org.idempiere.p2/target/products/org.adempiere.server.product/linux/gtk/x86_64
    1. ./console-setup-alt.sh
    1. ./idempiere-server.sh
1. open other terminal
    1. telnet localhost 12612
    1. tool:setMasterPass 'your master password'
    1. tool:setEncryptedKeystorePass 'your encrypt' or tool:setPlanKeystorePass 'your plain keystore password'
    1. service:getStatus


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
