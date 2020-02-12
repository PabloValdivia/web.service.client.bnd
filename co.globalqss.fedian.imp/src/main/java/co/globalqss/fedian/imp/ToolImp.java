package co.globalqss.fedian.imp;

import org.apache.felix.service.command.annotations.GogoCommand;
import org.apache.wss4j.common.crypto.JasyptPasswordEncryptor;
import org.osgi.service.component.annotations.Component;

import co.globalqss.fedian.api.Tool;

@GogoCommand(scope = "Tool", function = {"encryptPassword", "decryptPassword"})
@Component
public class ToolImp implements Tool{
	@Override
	public String encryptPassword(String plainPassword) {
		JasyptPasswordEncryptor encryptor = new JasyptPasswordEncryptor(new CerPasswordCallback());
		return encryptor.encrypt(plainPassword);
	}

	@Override
	public String decryptPassword(String encryptedPassword) {
		JasyptPasswordEncryptor encryptor = new JasyptPasswordEncryptor(new CerPasswordCallback());
		return encryptor.decrypt(encryptedPassword);
	}

}
