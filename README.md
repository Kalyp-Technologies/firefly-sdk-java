# FireFly Java SDK

[![FireFly Documentation](https://img.shields.io/static/v1?label=FireFly&message=documentation&color=informational)](https://hyperledger.github.io/firefly//)

![Hyperledger FireFly](./images/hyperledger_firefly_logo.png)

This is the client SDK for Java, allowing you to build your own applications on top of Hyperledger FireFly.

## Installation

```xml

<dependency>
    <groupId>com.kalyp</groupId>
    <artifactId>firefly-sdk-java</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

```

## Usage

```java
public class MessagesExample {

    public static void main(String[] args) {
        Firefly firefly = new Firefly("http://localhost:5000");
        try {
            firefly.sendBroadcast("hello world");

            firefly.sendPrivateMessage("this is private message");
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

git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin git@github.com:Kalyp-Technologies/firefly-sdk-java.git
git push -u origin main