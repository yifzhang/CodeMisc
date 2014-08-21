package com.github.ptomli.spring.env.support;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.util.Assert;

public class PropertySourceConfigurer extends PropertiesLoaderSupport implements EnvironmentAware, BeanNameAware, PriorityOrdered, BeanFactoryPostProcessor {

	private ConfigurableEnvironment env;
	private String name;
	private int order;

	@Override
	public void setBeanName(String name) {
		this.name = name;
	}

	@Override
	public void setEnvironment(Environment env) {
		Assert.isInstanceOf(ConfigurableEnvironment.class, env);
		this.env = (ConfigurableEnvironment) env;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			this.env.getPropertySources().addFirst(new PropertiesPropertySource(this.name, this.mergeProperties()));
		}
		catch (IOException ex) {
			throw new BeanInitializationException("Could not load properties", ex);
		}
	}

}
