#!/bin/sh
set -e

#git clone git@github.com:idempiere/idempiere.git
#git clone git@github.com:hieplq/web.service.client.bnd.git

cd idempiere

#sed -i -r "s|https://raw.githubusercontent.com/idempiere/binary.file/master/p2.maven/org.idempiere.webservice.client.*|https://raw.githubusercontent.com/hieplq/idempiere.artifact/cxf-3.3.1-service.client/m2p/org.idempiere.webservice.client\"/>|g" org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target
sed -i 's|</target>||g' org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target
sed -i 's|<targetJRE path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-11"/>||g' org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target
sed -i 's|</locations>||g' org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target

cat <<'EOT' >>org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target
	<location includeAllPlatforms="true" includeConfigurePhase="true" includeMode="slicer" includeSource="true" type="InstallableUnit">
		<repository location="file:/mnt/data/1Dev/project/vn.hsv.git.build/idempiere/org.idempiere.maven.to.p2/org.idempiere.webservice.client/target/repository/"/>
		<unit id="com.sun.istack.commons-runtime" version="3.0.8"/>
		<unit id="com.sun.istack.commons-runtime.source" version="3.0.8"/>
		<unit id="com.sun.org.apache.xml.internal.resolver" version="20050927.0.0"/>
		<unit id="com.sun.org.apache.xml.internal.resolver.source" version="20050927.0.0"/>
		<unit id="com.sun.xml.bind.external.rngom" version="2.3.2"/>
		<unit id="com.sun.xml.bind.external.rngom.source" version="2.3.2"/>
		<unit id="com.sun.xml.bind.jaxb-impl" version="2.3.2"/>
		<unit id="com.sun.xml.bind.jaxb-impl.source" version="2.3.2"/>
		<unit id="com.sun.xml.bind.jaxb-jxc" version="2.3.2"/>
		<unit id="com.sun.xml.bind.jaxb-jxc.source" version="2.3.2"/>
		<unit id="com.sun.xml.bind.jaxb-osgi" version="2.3.2"/>
		<unit id="com.sun.xml.bind.jaxb-osgi.source" version="2.3.2"/>
		<unit id="com.sun.xml.bind.jaxb-xjc" version="2.3.2"/>
		<unit id="com.sun.xml.bind.jaxb-xjc.source" version="2.3.2"/>
		<unit id="com.sun.xml.fastinfoset.FastInfoset" version="1.2.16"/>
		<unit id="com.sun.xml.fastinfoset.FastInfoset.source" version="1.2.16"/>
		<unit id="com.sun.xml.messaging.saaj.impl" version="1.5.2.b01"/>
		<unit id="com.sun.xml.messaging.saaj.impl.source" version="1.5.2.b01"/>
		<unit id="com.sun.xml.stream.buffer.streambuffer" version="1.5.7"/>
		<unit id="com.sun.xml.stream.buffer.streambuffer.source" version="1.5.7"/>
		<unit id="com.sun.xml.ws.jaxws-rt" version="2.3.2"/>
		<unit id="com.sun.xml.ws.jaxws-rt.source" version="2.3.2"/>
		<unit id="com.sun.xml.ws.policy" version="2.7.6"/>
		<unit id="com.sun.xml.ws.policy.source" version="2.7.6"/>
		<unit id="jakarta.jws-api" version="1.1.1"/>
		<unit id="jakarta.jws-api.source" version="1.1.1"/>
		<unit id="jakarta.xml.bind-api" version="2.3.2"/>
		<unit id="jakarta.xml.bind-api.source" version="2.3.2"/>
		<unit id="jakarta.xml.soap-api" version="1.4.1"/>
		<unit id="jakarta.xml.soap-api.source" version="1.4.1"/>
		<unit id="jakarta.xml.ws-api" version="2.3.2"/>
		<unit id="jakarta.xml.ws-api.source" version="2.3.2"/>
		<unit id="javax.interceptor-api" version="1.2.2"/>
		<unit id="javax.interceptor-api.source" version="1.2.2"/>
		<unit id="net.sf.ehcache" version="2.10.6"/>
		<unit id="net.sf.ehcache.source" version="2.10.6"/>
		<unit id="org.apache.commons.commons-codec" version="1.14.0"/>
		<unit id="org.apache.commons.commons-codec.source" version="1.14.0"/>
		<unit id="org.apache.cxf.cxf-rt-security-saml" version="3.3.1"/>
		<unit id="org.apache.cxf.cxf-rt-security-saml.source" version="3.3.1"/>
		<unit id="org.apache.cxf.cxf-rt-ws-mex" version="3.3.1"/>
		<unit id="org.apache.cxf.cxf-rt-ws-mex.source" version="3.3.1"/>
		<unit id="org.apache.cxf.cxf-rt-ws-security" version="3.3.1"/>
		<unit id="org.apache.cxf.cxf-rt-ws-security.source" version="3.3.1"/>
		<unit id="org.apache.santuario.xmlsec" version="2.1.4"/>
		<unit id="org.apache.santuario.xmlsec.source" version="2.1.4"/>
		<unit id="org.apache.wss4j.wss4j-bindings" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-bindings.source" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-policy" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-policy.source" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-ws-security-common" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-ws-security-common.source" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-ws-security-policy-stax" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-ws-security-policy-stax.source" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-ws-security-stax" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-ws-security-stax.source" version="2.2.4"/>
		<unit id="org.glassfish.external.management-api" version="3.2.1"/>
		<unit id="org.glassfish.external.management-api.source" version="3.2.1"/>
		<unit id="org.glassfish.gmbal.gmbal" version="4.0.0"/>
		<unit id="org.glassfish.gmbal.gmbal.source" version="4.0.0"/>
		<unit id="org.glassfish.ha.ha-api" version="3.1.12"/>
		<unit id="org.glassfish.ha.ha-api.source" version="3.1.12"/>
		<unit id="org.glassfish.jaxb.txw2" version="2.3.2"/>
		<unit id="org.glassfish.jaxb.txw2.source" version="2.3.2"/>
		<unit id="org.glassfish.pfl.pfl-asm" version="4.0.1"/>
		<unit id="org.glassfish.pfl.pfl-asm.source" version="4.0.1"/>
		<unit id="org.glassfish.pfl.pfl-basic" version="4.0.1"/>
		<unit id="org.glassfish.pfl.pfl-basic.source" version="4.0.1"/>
		<unit id="org.glassfish.pfl.pfl-dynamic" version="4.0.1"/>
		<unit id="org.glassfish.pfl.pfl-dynamic.source" version="4.0.1"/>
		<unit id="org.glassfish.pfl.pfl-tf" version="4.0.1"/>
		<unit id="org.glassfish.pfl.pfl-tf.source" version="4.0.1"/>
		<unit id="org.jasypt" version="1.9.3"/>
		<unit id="org.jasypt.source" version="1.9.3"/>
		<unit id="org.jvnet.mimepull" version="1.9.11"/>
		<unit id="org.jvnet.mimepull.source" version="1.9.11"/>
		<unit id="org.jvnet.staxex.stax-ex" version="1.8.1"/>
		<unit id="org.jvnet.staxex.stax-ex.source" version="1.8.1"/>
		<unit id="org.apache.wss4j.wss4j-ws-security-dom" version="2.2.4"/>
		<unit id="org.apache.wss4j.wss4j-ws-security-dom.source" version="2.2.4"/>
	</location>
	</locations>
<targetJRE path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-11"/>
</target>
EOT


sed -i 's|</license>|</license> <includes id=\"co.globalqss.fedian.client.feature\" version=\"0.0.0\"/>|g' org.adempiere.server-feature/feature.xml
sed -i 's|</modules>|<module>../web.service.client.bnd/co.globalqss.fedian-idempiere</module></modules>|g' pom.xml
#mvn validate

