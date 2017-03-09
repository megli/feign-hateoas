package org.mycompany.feign.service;

import java.util.Arrays;

import org.mycompany.feign.domain.Customer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Logger;
import feign.codec.Decoder;

@FeignClient(name = "customer", url = "http://localhost:8085/customer", configuration = CustomerClient.CustomerClientConfiguration.class)
public interface CustomerClient {

	@RequestMapping(method = RequestMethod.GET, value = "/api/customers/{customerId}")
	Resource<Customer> findOne(@PathVariable("customerId") Long customerId);
	
	@Configuration
	@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
	public static class CustomerClientConfiguration {
		
		@Bean
	    Logger.Level feignLoggerLevel() {
	        return Logger.Level.FULL;
	    }
		
		@Bean
		public Decoder feignDecoder() {
			ObjectMapper objectMapper = new ObjectMapper()
					 .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					 .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
					 .registerModule(new Jackson2HalModule());
			
			MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
			jacksonConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
			jacksonConverter.setObjectMapper(objectMapper);
		    
		    ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
	        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
		}
	}
}
