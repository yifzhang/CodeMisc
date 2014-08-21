package com.github.ptomli.spring.env.config;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

public class EnvironmentNamespaceHandlerTests {

	@Test
	public void testRegistersPropertySource() {
		ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("contextNamespaceHandlerTests-register.xml", getClass());
		ConfigurableEnvironment env = applicationContext.getEnvironment();
		MutablePropertySources ps = env.getPropertySources();
		assertTrue("property source is not registered", ps.contains("ps"));
	}

	@Test
	public void testMultiplePropertySource() {
		ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("contextNamespaceHandlerTests-multiple.xml", getClass());
		ConfigurableEnvironment env = applicationContext.getEnvironment();
		MutablePropertySources ps = env.getPropertySources();
		assertTrue("property source is not registered", ps.contains("ps"));
	}

	@Test
	public void testOrdersPropertySource() {
		ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("contextNamespaceHandlerTests-order.xml", getClass());
		ConfigurableEnvironment env = applicationContext.getEnvironment();
		MutablePropertySources ps = env.getPropertySources();
		assertTrue("property source ps1 is not registered", ps.contains("ps1"));
		assertTrue("property source ps2 is not registered", ps.contains("ps2"));
		assertTrue(ps.precedenceOf(ps.get("ps1")) > ps.precedenceOf(ps.get("ps2")));
	}

	@Test
	public void testIgnoreNotFound() {
		ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("contextNamespaceHandlerTests-ignore.xml", getClass());
		ConfigurableEnvironment env = applicationContext.getEnvironment();
		MutablePropertySources ps = env.getPropertySources();
		assertTrue("property source is not registered", ps.contains("ps"));
	}
}
