package co.globalqss.fedian.client.process;

import org.compiere.process.SvrProcess;

import co.globalqss.fedian.client.api.ServiceClientUtil;

public class ServiceClientProcess extends SvrProcess{

	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		ServiceClientUtil.getStatus(false);
		return "OK";
	}

}
