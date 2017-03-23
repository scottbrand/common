package io.github.scottbrand.webservice.jaxrs.security.provider;

import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;



@Provider
public class RolesAllowedDynamicFeatureImpl extends RolesAllowedDynamicFeature
{
	// We have to extend the Jersey RolesAllowedDynamicFeature so that the
	// @Provider annotation can be used and registered as an OSGi service.
}
