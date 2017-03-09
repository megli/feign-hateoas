package org.mycompany.feign.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mycompany.feign.domain.Customer;
import org.mycompany.feign.domain.Customer.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import feign.Logger;

/**
 * Tests for the {@link CustomerClient} Feign client.
 * Provides a minimal test-specific configuration including a controller used to access a static JSON of a sample customer.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CustomerClientTests.TestConfiguration.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class CustomerClientTests {
	
	@Autowired
	private CustomerClient customerClient;
	
	@Test
	public void findOne_exists_customerMappedAndReturned() throws Exception {
		
		// arrange
		Long customerId = new Long(400);
		
		// act
		Customer customer = customerClient.findOne(customerId).getContent();
		
		// assert
		assertThat(customer).isNotNull();
		assertThat(customer.getDisplayName().equals("John Doe"));
		assertThat(customer.getRating().equals(6));
		assertThat(customer.getType().equals(Type.PERSON));
		
		// TODO: Get the _embedded relations mapped to customer's Relation objects
		assertThat(customer.getRelations()).isNotNull();
		assertThat(customer.getRelations()).hasSize(1);
		// TODO: assertions that the relation has been properly mapped (i.e. use its properties)
	}

	/**
	 * Test Controller to mock API response using static JSON file for the given URL.
	 */
	@Controller
	static class TestController {
		
		@RequestMapping(value = "/customer/api/customers/{customerId}")
	    public ResponseEntity<String> getCustomer(@PathVariable("customerId") Long customerId) throws IOException {
			String data = IOUtils.toString(this.getClass().getResourceAsStream("/customerResponse.json"), "UTF-8");
			return ResponseEntity.ok().contentType(MediaTypes.HAL_JSON).body(data);
	    }
	}
	
	@Configuration
	@EnableAutoConfiguration
	@EnableFeignClients(basePackages = {"org.mycompany.feign.service"})
	public static class TestConfiguration {

		@Bean
		public TestController testController() {
			return new TestController();
		};
		
		@Bean
	    Logger.Level feignLoggerLevel() {
	        return Logger.Level.FULL;
	    }
		
	}
}
