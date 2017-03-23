package io.github.scottbrand.common.sed;

import java.util.Map;

public interface Domain
{
	Map<String,String> getMap();

	Domain getParent();
}
