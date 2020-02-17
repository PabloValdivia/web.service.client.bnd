package co.globalqss.fedian.client.process;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.osgi.service.component.annotations.Component;

@Component(
		 property= {"service.ranking:Integer=2"},
		 service = org.adempiere.base.IProcessFactory.class
		 )
public class FedianClientProcessFactory implements IProcessFactory{

	@Override
	public ProcessCall newProcessInstance(String className) {
		ProcessCall process = null;
		if ("org.compiere.process.TranslationDocSync".equals(className)) {
			process =  new ServiceClientProcess();
		}
		
		return process;
	}

}
