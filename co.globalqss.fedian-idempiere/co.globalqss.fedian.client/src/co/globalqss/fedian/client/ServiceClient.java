package co.globalqss.fedian.client;


import org.osgi.service.component.annotations.Component;
import co.globalqss.fedian.client.api.ServiceClientUtil;

@Component(immediate = true, service =  ServiceClient.class, property = {
		"osgi.command.scope=service", "osgi.command.function=getStatus"
})
public class ServiceClient {
	public String getStatus () {
		ServiceClientUtil.getStatus(false);
		return "SUCCESS";
	}
}
