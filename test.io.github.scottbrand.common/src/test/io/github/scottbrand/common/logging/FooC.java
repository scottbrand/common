package test.io.github.scottbrand.common.logging;

import java.util.List;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import io.github.scottbrand.common.CollectionUtil;

@Component(property={Constants.SERVICE_RANKING + ":Integer=99","foo=bar","dataSupplier=1000"})
public class FooC implements IFoo
{

	@Override
	public List<String> getMobileProgrammingLanguages()
	{
		return CollectionUtil.createList(String.class, "Objective-C");
	}

}
