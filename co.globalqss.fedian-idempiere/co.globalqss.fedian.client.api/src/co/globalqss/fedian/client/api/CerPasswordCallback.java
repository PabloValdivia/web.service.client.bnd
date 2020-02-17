package co.globalqss.fedian.client.api;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.crypto.JasyptPasswordEncryptor;
import org.apache.wss4j.common.crypto.PasswordEncryptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;

public class CerPasswordCallback implements CallbackHandler{
	public static String encrytedPassword;
	
	static {
		encrytedPassword = System.getProperty("JasyptPasswordEncryptor.encrytedPassword.keystore");
	}
	
	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (!(callbacks[i] instanceof WSPasswordCallback))
				continue;
			
			WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];

			if (pc.getUsage() == WSPasswordCallback.PASSWORD_ENCRYPTOR_PASSWORD) {
				/*
				 * set master password for JasyptPasswordEncryptor to decrypt keystore password set on Merlin.PREFIX + Merlin.KEYSTORE_PASSWORD 
				 */
				pc.setPassword(System.getProperty("JasyptPasswordEncryptor.master.password"));
			}else if (pc.getUsage() == WSPasswordCallback.SIGNATURE) {// WSPasswordCallback.SIGNATURE - SIGNATURE usage is used on the outbound side only, to get a password to get the private key of this identifier (alias) from a keystore. The CallbackHandler must set the password via the setPassword(String) method.
				
				// we use Merlin, so set null to use encrypted password set on properties with key Merlin.PREFIX + Merlin.KEYSTORE_PRIVATE_PASSWORD"
				// UPDATE pending to investigate: at moment, this method get issue, because one PasswordEncryptor can't call two time (exception Encryption entity already initialized)
				// pc.setPassword(null);
				
				// call JasyptPasswordEncryptor manual
				PasswordEncryptor passwordEncryptor = new JasyptPasswordEncryptor(this);
				pc.setPassword(passwordEncryptor.decrypt(CerPasswordCallback.encrytedPassword));
			}else {
				PasswordEncryptor passwordEncryptor = new JasyptPasswordEncryptor(this);
				pc.setPassword(passwordEncryptor.decrypt(CerPasswordCallback.encrytedPassword));
			}
		}
		
	}

}
