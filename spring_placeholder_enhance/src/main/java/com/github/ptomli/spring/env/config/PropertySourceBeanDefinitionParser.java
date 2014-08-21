package com.github.ptomli.spring.env.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.github.ptomli.spring.env.support.PropertySourceConfigurer;

class PropertySourceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return PropertySourceConfigurer.class;
	}

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String location = element.getAttribute("location");
		if (StringUtils.hasLength(location)) {
			String[] locations = StringUtils.commaDelimitedListToStringArray(location);
			builder.addPropertyValue("locations", locations);
		}

		String order = element.getAttribute("order");
		if (StringUtils.hasLength(order)) {
			builder.addPropertyValue("order", Integer.valueOf(order));
		}

		builder.addPropertyValue("ignoreResourceNotFound", Boolean.valueOf(element.getAttribute("ignore-resource-not-found")));

		builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
	}
	
}
