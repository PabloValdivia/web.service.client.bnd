# web.service.client.bnd
user cxf to call service with security policy

# setup environment
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
