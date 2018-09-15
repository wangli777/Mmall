## 这是一个基于SSM的网上商城项目
###项目过程中遇到的一些问题
  
- 项目在配置到tomcat中启动时报错：
  MappingJackson2HttpMessageConverter相关的错误。
  使用@ResponseBody注解将对象转化为Json返回给浏览器时，由于我使用的Spring版本是4.3.13
  在使用的Spring版本是4时：
```xml
<!--让@ResponseBody注解自动转换json -->
            <bean  class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="supportedMediaTypes">
                    <list>
                        <value>application/json;charset=UTF-8</value>
                    </list>
                </property>
            </bean>
```
这里要使用MappingJackson2HttpMessageConverter而不是MappingJacksonHttpMessageConverter。
而且pom文件中配置的jackson相关的包也有限制，我这里用的是:
```xml
<!-- MappingJackson2HttpMessageConverter -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.6.5</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.6.5</version>
    </dependency>
```
