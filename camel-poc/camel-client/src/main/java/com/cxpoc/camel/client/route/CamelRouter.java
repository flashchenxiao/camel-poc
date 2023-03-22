package com.cxpoc.camel.client.route;

import java.net.ConnectException;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.messagehistory.MetricsMessageHistoryFactory;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;
import com.cxpoc.camel.client.components.CountRobin;

@Component
public class CamelRouter extends RouteBuilder {
	@Value("${file.upload.path}")
	private String filePath;

	@Value("${file.upload.path.out}")
	private String outFilePath;
	
	@Override
	public void configure() throws Exception {
		restConfiguration();

		MetricRegistry metricRegistry = new MetricRegistry();
		MetricsRoutePolicyFactory fac = new MetricsRoutePolicyFactory();
		fac.setMetricsRegistry(metricRegistry);
		getCamelContext().addRoutePolicyFactory(fac);

		getCamelContext().setMessageHistoryFactory(new MetricsMessageHistoryFactory());

		// 构建异常处理
		onException(HttpOperationFailedException.class).id("HttpOperationFailedException").handled(true).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				exchange.getMessage().setBody("{\"code\":\"400\",\"success\":false,\"message\":\"目标服务内部错误\"}");
				log.error(cause.getMessage());
			}
		});
		
		onException(ConnectException.class).id("ConnectException").handled(true).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 503);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				exchange.getMessage().setBody("{\"code\":\"503\",\"success\":false,\"message\":\"您访问的服务连接失败\"}");
				log.error(cause.getMessage());
			}
		});
		

		onException(Exception.class).id("Exception").handled(true).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				exchange.getMessage()
						.setBody("{\"code\":\"500\",\"success\":false,\"message\":" + cause.getMessage() + "}");
				exchange.getIn().setHeader("exception", cause.getCause());
				log.error(exchange.getMessage().getHeader(Exchange.HTTP_SERVLET_REQUEST) + "  " + cause.getMessage());
			}
		});
		// 文件同步任务
		from("file:" + filePath + "?noop=true").id("file_synchronize").to("file:" + outFilePath + "/personal")
				.log("synchronize file ${file:name} is complete.");

		// 负载均衡

		// 构建负载均衡网关，
		rest().get("/gateway").id("rest_gateway").enableCORS(true).route().loadBalance(new CountRobin()) // 自定义
			.to("http://127.0.0.1:9999/v1/home/user?sev=service1&bridgeEndpoint=true")
			.to("http://127.0.0.1:9999/v1/home/user?sev=service2&bridgeEndpoint=true");

		// 文件上传
//		from("direct:uploadFile").id("direct_uploadFile").;
		rest("/uploadFile").consumes("multipart/form-data").post().id("rest_post_uploadFile").description("uploadFile service")
				.responseMessage().code(200).message("User successfully created").endResponseMessage()
				.route().log("uploadFile filename ${file:name} ")
				.to("http://127.0.0.1:9999/v1/home/uploadFile?bridgeEndpoint=true&method=sendMultiPart");

		// 更新数据，验证
//		from("direct:update_user").id("direct_update_user").log("direct:update_user ${header.id} ").to(
//				"bean-validator://x?group=com.joinfun.camel.client.validator.PutUserValidation&ignoreXmlConfiguration=true")
//				.toD("http://127.0.0.1:9999/v1/home/user/${header.id}?bridgeEndpoint=true&transferException=true");


		// 返回数据-修改编码
		rest().get("/get_gbk").id("rest_get_gbk").route().toD("http://127.0.0.1:9999/v1/home/user?bridgeEndpoint=true")
		.convertBodyTo(String.class, "GBK").log("Response : ${body}");

		// rest api 构建
		rest("/user").description("User REST service").consumes("application/json")
				
				.get().id("rest_user_all")
				.description("Find all Users").responseMessage().code(200).message("All Users successfully returned")
				.endResponseMessage().to("http://127.0.0.1:9999/v1/home/user?bridgeEndpoint=true")

				// 构建返回数据JSON转XML
				.get("/{id}").id("rest_user_get").description("Find User by ID").param().name("id")
				.description("The ID of the User").endParam().responseMessage().code(200)
				.message("User successfully returned").endResponseMessage().route()
				.log("Find User by ID ${header.id} ")
				.toD("http://127.0.0.1:9999/v1/home/user/${header.id}?bridgeEndpoint=true").unmarshal()
				.json(JsonLibrary.Jackson, Map.class).marshal().xstream().endRest()

				.put("/{id}").id("rest_user_put").description("Update a User").param().name("id")
				.description("The ID of the User to update").endParam().param().name("name")
				.description("The User to update").endParam().responseMessage().code(200)
				.message("User successfully updated").endResponseMessage().route()
				.log("Update a User ${header.id} ").to(
				"bean-validator://x?group=com.cxpoc.camel.client.validator.PutUserValidation&ignoreXmlConfiguration=true")
				.toD("http://127.0.0.1:9999/v1/home/user/${header.id}?bridgeEndpoint=true&transferException=true").endRest()

				.post().id("rest_user_post").description("Create a User").param().name("name").description(" User name")
				.endParam().responseMessage().code(200).message("User successfully created").endResponseMessage()
				.to("http://127.0.0.1:9999/v1/home/user?bridgeEndpoint=true")

				.delete("/{id}").id("rest_user_delete").description("Delete by id").param().name("id")
				.description("The ID of the User to delete").endParam().responseMessage().code(200)
				.message("User deleted").endResponseMessage()
				.to("http://127.0.0.1:9999/v1/home/user/${header.id}?bridgeEndpoint=true");

		// socket 构建成http GET 访问
		rest("/socket").get().id("rest_socket").route().log("socket ${header.mes} ").setBody(simple("${header.mes}").append("\r\n"))
		.to("netty:tcp://127.0.0.1:8889?clientMode=true&disconnect=true&reuseChannel=true&tcpNoDelay=true")
		.log("Response : ${body}");

	}

}
