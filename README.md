Spring Boot 프로젝트에서 Maven을 사용해 프로필 별로 다양한 리소스 디렉터리와 여러 개의 `application-{profile}.yml` 파일을 설정하는 방법을 설명한다.이 구성은 개발`(dev)` 및 운영`(prod)` 환경에서 각각의 설정 파일을 관리하는 데 유용할 수 있다.

### 단계별 설정

1. 프로필 별로 리소스 디렉터리를 분리하고, 각 디렉터리마다 `application-{profile}.yml` 파일을 생성.
   
2. Maven에서 프로필을 정의하여 빌드시 각 리소스 디렉터리가 올바르게 포함되도록 설정.

3. JAR 또는 WAR 패키징을 위한 Maven 설정을 추가.


### 디렉터리 구조
```
src
├── main
│   ├── java
│   │ 
│   └── resources
│   │   └── application.yml        <-- 공통 설정 파일 (선택사항)
│   │ 
│   └── resources-local
│   │   └── application-local.yml  <-- 개발자 PC
│   │ 
│   └── resources-dev
│   │   └── application-dev.yml    <-- 개발 서버
│   │ 
│   └── resources-prod
       └── application-prod.yml    <-- 운영 서버
```       

### `aplication-{profile}.yml` 파일 예시


```yml
# src/main/resources/application.yml   
# 공통 설정만 입력한다.
spring:
  application:
    name: app5
```


```yml
# src/main/resources-local/application-local.yml 
# 개발자의 로컬 PC의 정보들을 입력한다.
spring:
  config:
    activate:
      on-profile: local

server:
  port: 8081
```

```yml
# src/main/resources-dev/application-dev.yml 
# 개발 서버 정보
spring:
  config:
    activate:
      on-profile: dev

server:
  port: 8082
```

```yml
# src/main/resources-prod/application-prod.yml 
# 운영 서버 정보
spring:
  config:
    activate:
      on-profile: prod

server:
  port: 8083  
```

### Maven `pom.xml` 설정
`pom.xml` 파일에서 각각의 Maven 프로필을 정의하고, 빌드시에 각 리소스 디렉터리가 포함되도록 설정

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
    <packaging>jar</packaging> <!-- WAR로 변경하려면 'war' -->
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


	<!-- Maven 프로필 설정 -->
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

### 빌드 및 실행 방법

`local` 프로필로 실행(`windows환경`)
```bash
mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

`local` 프로필로 빌드(jar)후 실행(`windows환경`)
```bash
mvnw clean package -P local
또는
mvnw clean package -Dspring.profiles.active=local

java -jar ./target/app5-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

### WAR로 패키징
`pom.xml`에서 `<packaging>` 태그를 `war`로 변경하여 WAR 파일을 생성.
```xml
<packaging>war</packaging>
```

그런 후 다음 명령어로 각각의 프로필로 빌드

```bash
mvn clean package -P prod
또는
mvnw clean package -Dspring.profiles.active=prod
```
이 방식은 선택된 Maven 프로필에 따라 각기 다른 `application-{profile}.yml` 파일을 포함하게 되어, 빌드와 실행 시 특정 환경 설정을 손쉽게 적용가능함.


### 참고
* https://chatgpt.com/share/0f6265c1-d0a1-4e32-8b4e-d3da3cd8dd70
* https://royleej9.tistory.com/entry/Spring-properties-%EC%84%A4%EC%A0%95-%EB%B6%84%EB%A6%AC2  
* https://yungenie.tistory.com/14
* https://youtu.be/aJcG2cyeULo