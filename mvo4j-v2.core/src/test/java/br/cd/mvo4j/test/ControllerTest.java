package br.cd.mvo4j.test;

import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.cd.mvo.client.controller.ContactTypeControllerListener;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.mvo.ioc.ContainerProvider;
import br.com.cd.mvo.ioc.LocalPropertyContainerConfig;
import br.com.cd.mvo.web.WebCrudController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/applicationContext.xml" })
public class ControllerTest {

	@Autowired
	private LocalPropertyContainerConfig applicationConfig;
	private Container container;

	@Before
	public void setUp() throws Exception {

		ContainerProvider<ContainerConfig<Properties>> provider = applicationConfig
				.getContainerProvider();

		container = provider.getContainer(applicationConfig);
		container.start();
	}

	@After
	public void tearDown() throws Exception {
		container.stop();
	}

	@Test
	@Transactional
	@Rollback
	public void test() {

		try {
			ContactTypeControllerListener controller = container
					.getBean(ContactTypeControllerListener.class);

			Assert.assertEquals(
					"Instance for ContactTypeControllerListener not is instance of 'WebCrudController'",
					controller.getClass(), WebCrudController.class);

			WebCrudController<ContactType> webCrudController = (WebCrudController<ContactType>) controller;

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
