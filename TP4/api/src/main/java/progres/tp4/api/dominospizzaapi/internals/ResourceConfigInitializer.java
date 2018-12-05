package progres.tp4.api.dominospizzaapi.internals;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.autoconfigure.jersey.ResourceConfigCustomizer;
import org.springframework.stereotype.Component;
import progres.tp4.api.dominospizzaapi.internals.providers.CORSFilter;
import progres.tp4.api.dominospizzaapi.internals.providers.ServiceExceptionMapper;

@Component
public class ResourceConfigInitializer implements ResourceConfigCustomizer {
	@Override
	public void customize(ResourceConfig config) {
		config.register(CORSFilter.class)
		      .register(ServiceExceptionMapper.class);
	}
}
