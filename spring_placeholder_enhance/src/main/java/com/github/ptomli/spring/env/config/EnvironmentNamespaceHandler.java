package com.github.ptomli.spring.env.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class EnvironmentNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("property-source", new PropertySourceBeanDefinitionParser());
	}
}
