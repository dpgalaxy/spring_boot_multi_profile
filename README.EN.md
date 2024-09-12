
This document explains how to configure different resource directories and multiple `application-{profile}.yml` files based on profiles using Maven in a Spring Boot project. This setup can be useful for managing configuration files separately for development `(dev)` and production `(prod)` environments.

### Step-by-step Configuration

1. Separate resource directories by profile, and create an `application-{profile}.yml` file in each directory.
   
2. Define profiles in Maven so that the appropriate resource directories are included during the build.

3. Add Maven settings for JAR or WAR packaging.

### Directory Structure

```
src
├── main
│   ├── java
│   │ 
│   └── resources
│   │   └── application.yml        <-- Common configuration file (optional)
│   │ 
│   └── resources-local
│   │   └── application-local.yml  <-- Developer's local machine
│   │ 
│   └── resources-dev
│   │   └── application-dev.yml    <-- Development server
│   │ 
│   └── resources-prod
       └── application-prod.yml    <-- Production server
```       

### Example of `application-{profile}.yml` Files

```yml
# src/main/resources/application.yml   
# Only common configurations are written here.
spring:
  application:
    name: app5
```

```yml
# src/main/resources-local/application-local.yml 
# Information for the developer's local machine.
spring:
  config:
    activate:
      on-profile: local

server:
  port: 8081
```

```yml
# src/main/resources-dev/application-dev.yml 
# Development server information.
spring:
  config:
    activate:
      on-profile: dev

server:
  port: 8082
```

```yml
# src/main/resources-prod/application-prod.yml 
# Production server information.
spring:
  config:
    activate:
      on-profile: prod

server:
  port: 8083  
```

### Maven `pom.xml` Configuration

In the `pom.xml` file, define Maven profiles so that the correct resource directories are included during the build.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.3.3</version>
		<relativePath/>
	</parent>
	<groupId>com.isb</groupId>
	<artifactId>app5</artifactId>
	<version>0.0.1-SNAPSHOT</version>
    <name>app5</name>
    <packaging>jar</packaging> 
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>


	<!-- Maven profiles configuration -->
	<profiles>
		<profile>
			<id>local</id>
			 <properties>
			 	<env>local</env>
			 </properties>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
		</profile>
		
	    <profile>
	        <id>dev</id>
	        <properties>
	            <env>dev</env>
	        </properties>	        
	    </profile>
	    
	    <profile>
	        <id>prod</id>
	        <properties>
	            <env>prod</env>
	        </properties>
	    </profile>	    
	</profiles>
	
	<build>
		<!-- Set Resources -->
		<resources>
			<!-- Common -->
			<resource>
				<directory>src/main/resources</directory>
			</resource>
			
			<!-- local/dev/prod -->
			<resource>
            	<directory>src/main/resources-${env}</directory>
        	</resource>
		</resources>
		
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

### Build and Run

To run with the `local` profile (on Windows):

```bash
mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

To build and run the `local` profile (JAR):

```bash
mvnw clean package -P local
or
mvnw clean package -Dspring.profiles.active=local

java -jar ./target/app5-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

### WAR Packaging

To package as WAR, modify the `<packaging>` tag in the `pom.xml` to `war` and generate the WAR file.

```xml
<packaging>war</packaging>
```

Then, build for each profile using:

```bash
mvn clean package -P prod
or
mvnw clean package -Dspring.profiles.active=prod
```

This approach includes different `application-{profile}.yml` files depending on the selected Maven profile, making it easier to apply specific environment configurations during build and execution.

### References
* https://chatgpt.com/share/0f6265c1-d0a1-4e32-8b4e-d3da3cd8dd70
* https://royleej9.tistory.com/entry/Spring-properties-%EC%84%A4%EC%A0%95-%EB%B6%84%EB%A6%AC2  
* https://yungenie.tistory.com/14
