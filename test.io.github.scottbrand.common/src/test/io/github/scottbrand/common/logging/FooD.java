package test.io.github.scottbrand.common.logging;

import java.util.List;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import io.github.scottbrand.common.CollectionUtil;

@Component(property={Constants.SERVICE_RANKING + ":Integer=100","foo=by"})
public class FooD implements IFoo
{

	@Override
	public List<String> getMobileProgrammingLanguages()
	{
		return CollectionUtil.createList(String.class, "Objective-C","Java","JavaScript","CSS");
	}

}
