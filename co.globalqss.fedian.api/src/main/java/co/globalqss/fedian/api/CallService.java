package co.globalqss.fedian.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface CallService {
	public String getStatus ();
}
