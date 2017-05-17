## To test projest run

> ./mvnw clean install

The build fails, because ribbon client is not reading properties from correct context

There are two tests, `ADemoApplicationTests` which starts server on a random port and `DemoApplication2Tests` which starts on a 8083 port. 

The `pom.xml` configures `maven-surefire-plugin` to build tests alphabetically

```
 <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <runOrder>alphabetical</runOrder>
    </configuration>
</plugin>
```

## The issue

Thanks to that `ADemoApplicationTests` starts first. This tests only checks if context loads. Also there is specified `client.ribbon.listOfServers=http://localhost:123` property, that theoretically is unused. 
`DemoApplication2Tests` starts second, setting `server.port=8083` and `client.ribbon.listOfServers=http://localhost:${server.port}` properties. In addition, this tests registers `FeignClient` named `client`. 
Tests first prints value of `client.ribbon.listOfServers` property which is equal to *http://localhost:8083*. Unfortunately, when test tries to use feign client, it fails as ribbon initialises server list using properties from context build by first test (`client.ribbon.listOfServers=http://localhost:123`). If the first test doesn't specify this property test fails with error that there are no available servers.    
