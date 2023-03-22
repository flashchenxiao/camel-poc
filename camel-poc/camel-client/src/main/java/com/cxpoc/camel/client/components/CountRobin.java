package com.cxpoc.camel.client.components;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.processor.loadbalancer.LoadBalancerSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cxpoc.camel.client.IndexController;

/**
 * 自定义负载均衡
 *
 */
public class CountRobin extends LoadBalancerSupport {
	private static Logger logger = LogManager.getLogger(IndexController.class);

	private volatile int count = 0, index = 0;

	public boolean process(Exchange exchange, AsyncCallback callback) {
		int size = getProcessors().size();
		try {
			if (count < 2) {
				count++;
			} else {
				count = 1;
				index = (index < size - 1) ? index + 1 : 0;
			}
			getProcessors().get(index).process(exchange);
			logger.info("CountRobin log ------size:" + size + "----- index=" + index + " count=" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		callback.done(true);
		return true;
	}

}
