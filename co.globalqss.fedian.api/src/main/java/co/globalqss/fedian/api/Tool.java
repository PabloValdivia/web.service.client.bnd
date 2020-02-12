package co.globalqss.fedian.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Tool {
	public String encryptPassword (String plainPassword);
	public String decryptPassword (String encryptedPassword);
}
