package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RunWith(SpringRunner.class)
@EnableFeignClients("com.example")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=8083", "client.ribbon.listOfServers=http://localhost:${server.port}"})
public class DemoApplication2Tests {

    @Autowired
    Client client;

    @Value("${client.ribbon.listOfServers}")
    private String listOfServers;

    @Test
    public void contextLoads() {
        System.err.println(listOfServers);

        client.test();
    }

    @FeignClient("client")
    public interface Client {

        @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        String test();
    }

    @RestController
    public static class Controller {

        @RequestMapping(value = "/test", method = RequestMethod.GET)
        public String test() {
            return "success";
        }
    }
}
