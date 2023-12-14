# FireFly Java SDK

[![FireFly Documentation](https://img.shields.io/static/v1?label=FireFly&message=documentation&color=informational)](https://hyperledger.github.io/firefly//)

![Hyperledger FireFly](./images/hyperledger_firefly_logo.png)

This is the client SDK for Java, allowing you to build your own applications on top of Hyperledger FireFly.

## Installation

For now the library is only on Github Packages, see: See: https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-with-a-personal-access-token.
To be able to use it in your project please update your settings.xml.

_GitHub Packages supports SNAPSHOT versions of Apache Maven. To use the GitHub Packages repository for downloading 
SNAPSHOT artifacts, enable SNAPSHOTS in the POM of the consuming project or your ~/.m2/settings.xml file._


```xml

    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
    http://maven.apache.org/xsd/settings-1.0.0.xsd">
    
      <activeProfiles>
        <activeProfile>github</activeProfile>
      </activeProfiles>
    
      <profiles>
        <profile>
          <id>github</id>
          <repositories>
            <repository>
              <id>central</id>
              <url>https://repo1.maven.org/maven2</url>
            </repository>
            <repository>
              <id>github</id>
              <url>https://maven.pkg.github.com/OWNER/REPOSITORY</url>
              <snapshots>
                <enabled>true</enabled>
              </snapshots>
            </repository>
          </repositories>
        </profile>
      </profiles>
        
        
      <servers>
        <server>
          <id>github</id>
          <username>USERNAME</username>
          <password>TOKEN</password>
        </server>
      </servers>
    </settings>
```

In your project pom.xml: 

```xml

<dependency>
    <groupId>com.kalyp</groupId>
    <artifactId>firefly-sdk-java</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>

```


## Usage

```java
public class MessagesExample {

    public static void main(String[] args) {
        Firefly firefly = new Firefly("http://localhost:5000/api/v1");
        try {
            firefly.sendBroadcast("hello world");

            List<Identity> orgs = firefly.listOrganizations();
            System.out.println("List of orgs:" + orgs);

            firefly.sendPrivateMessage("this is private message", orgs.get(0).getDid());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
```

(This example was taken from the [examples](src/main/java/com/kalyp/examples) folder where you will find some other basic samples)

## Generated schemas

The types for FireFly requests and responses are generated from the [OpenAPI schema for FireFly](https://hyperledger.github.io/firefly/swagger/swagger.html)

## Git repositories

There are multiple Git repos making up the Hyperledger FireFly project. Some others
that may be helpful to reference:

- [Core](https://github.com/hyperledger/firefly)
- [Command Line Interface (CLI)](https://github.com/hyperledger/firefly-cli)
- [FireFly Sandbox](https://github.com/hyperledger/firefly-sandbox)
- [Firefly Node SDK](https://github.com/hyperledger/firefly-sdk-nodejs/tree/main/lib)