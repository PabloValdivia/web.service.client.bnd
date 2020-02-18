package co.globalqss.fedian.client.api.tool;

import org.apache.wss4j.common.crypto.JasyptPasswordEncryptor;
import org.osgi.service.component.annotations.Component;

import co.globalqss.fedian.client.api.CerPasswordCallback;

@Component(immediate = true, service =  Password.class, property = {
		"osgi.command.scope=tool", 
		"osgi.command.function=encryptPass", 
		"osgi.command.function=decryptPass", 
		"osgi.command.function=setMasterPass",
		"osgi.command.function=setPlanKeystorePass",
		"osgi.command.function=setEncryptedKeystorePass"
})
public class Password {
	public String encryptPass(String plainPassword) {
		JasyptPasswordEncryptor encryptor = new JasyptPasswordEncryptor(new CerPasswordCallback());
		return encryptor.encrypt(plainPassword);
	}

	public String decryptPass(String encryptedPassword) {
		JasyptPasswordEncryptor encryptor = new JasyptPasswordEncryptor(new CerPasswordCallback());
		return encryptor.decrypt(encryptedPassword);
	}
	
	public void setMasterPass(String masterPassword) {
		System.setProperty(CerPasswordCallback.masterPasswordKey, masterPassword);
	}
	
	public void setPlanKeystorePass(String plainPassword) {
		System.setProperty(CerPasswordCallback.encrytedPasswordKey, decryptPass(plainPassword));
	}
	
	public void setEncryptedKeystorePass(String encryptedPassword) {
		System.setProperty(CerPasswordCallback.encrytedPasswordKey, encryptedPassword);
	}
	
}
