# feign-hateoas
This is a sample project dealing with the usage of a `@FeignClient` along with HATEOAS in a microservice architecture.
In the real project, we want to use a Feign client to access the API of one microservice (here: customer) from another microservice. The responded HAL JSON should then be mapped to a dedicated Customer DTO.

## The Problem
The access to the customer microservice's API via the Feign client works, and we expect a HATEOAS Resource of type Customer (DTO). The mapping of all the simple properties works - however, we also want to access `_embedded` collections, in this case the relations associated with the customer. This mapping does not seem to work, and we tried several solutions so far.

To be on the same page, you might want to take a look at these posts/questions:
* http://stackoverflow.com/questions/30515483/feign-and-hal-resources
* https://github.com/spring-cloud/spring-cloud-netflix/issues/856

**You can find our own question on Stack Overflow here:**
http://stackoverflow.com/questions/42698024/how-to-use-a-feignclient-to-map-a-hal-json-embedded-collection

## The Setup of this sample project
As taken from the first source above, we tried to implement a custom Feign Decoder with a corresponding ObjectMapper, using the often mentioned Jackson2HalModule. However, we still get nothing mapped to the relations property.

We implemented a test to reproduce this problem (see `CustomerClientTests.java`). On using the Feign client, we respond with a static JSON abstracted from our real API to test the mapping. As mentioned above, everything works except for the embedded collection of relations (see FIXME in test).