package br.cd.mvo4j.test;

import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.cd.mvo.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.mvo.beans.factory.ioc.ComponentFactoryProvider;
import br.com.cd.mvo.client.controller.ContactTypeControllerListener;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.context.ApplicationConfig;
import br.com.cd.mvo.context.support.LocalPropertyApplicationConfig;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.web.controller.WebCrudController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/controllerTest-context.xml" })
public class ControllerTest {

	@Autowired
	private LocalPropertyApplicationConfig applicationConfig;
	private ComponentFactoryContainer<ApplicationConfig<Properties>> container;

	@Before
	public void setUp() throws Exception {

		ComponentFactoryProvider<ApplicationConfig<Properties>> provider = applicationConfig
				.getProvider();

		container = provider.getContainer(applicationConfig);
		container.start();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		try {
			ContactTypeControllerListener controller = container
					.getBean(ContactTypeControllerListener.class);

			Assert.assertEquals(
					"Instance for ContactTypeControllerListener not is instance of 'WebCrudController'",
					controller.getClass(), WebCrudController.class);

			WebCrudController<ContactType, Long> webCrudController = (WebCrudController) controller;

			webCrudController.toNewMode();
			ContactType entity = new ContactType("TYPE_TEST");
			webCrudController.save(entity);

			Assert.assertNotNull(webCrudController.getService().getRepository()
					.find("type", "TYPE_TEST"));

		} catch (NoSuchBeanDefinitionException e) {
			Assert.fail();
		}
	}
}
