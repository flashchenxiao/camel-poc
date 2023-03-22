package com.cxpoc.camel.client.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class CamelExceptionRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		onException(BeanValidationException.class).handled(true).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				exchange.getMessage().setBody("{error:" + cause.getMessage() + "}");
			}
		});
		
		onException(IllegalArgumentException.class).handled(true).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				exchange.getMessage().setBody("{error:" + cause.getMessage() + "}");
			}
		});

		onException(HttpOperationFailedException.class).handled(true).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				exchange.getMessage().setBody("{error:" + cause.getMessage() + "}");
			}
		});


		onException(Exception.class).handled(true).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				exchange.getMessage().setBody("{error:" + cause.getMessage() + "}");
				exchange.getIn().setHeader("exception", cause.getCause());
			}
		});
		
		rest("/rest_https").get().route().id("rest_https").toD("https://autumnfish.cn/api/joke/list?num=1&bridgeEndpoint=true");
		
		
	}
}
