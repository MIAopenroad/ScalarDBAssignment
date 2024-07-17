# AraKimura E-Commerce Site

AraKimura E-Commerce Site is the EC Site Demo application with ScalarDB.

## Demo Video

Ordering

https://github.com/MIAopenroad/ScalarDBAssignment/assets/128337097/79cd2520-f1fd-447d-8430-b2a0b8aa7ea1

Ordering(failure)

https://github.com/MIAopenroad/ScalarDBAssignment/assets/128337097/43844b75-fbd5-4568-a279-f8fdfd09e4c4


## Prerequisists

This application is highly dockernized.

So, prerequisists are only here!

Feel free to get started!

- Docker
- Docker Compose
- Make
- Java runtime

For example, one of the developer used env below...
```
% docker -v      
Docker version 26.1.1, build 4cf5afa
% docker-compose -v
Docker Compose version v2.27.0-desktop.2
% make -v
GNU Make 3.81
% java -version
openjdk version "1.8.0_362"
OpenJDK Runtime Environment (Zulu 8.68.0.19-CA-macos-aarch64) (build 1.8.0_362-b08)
OpenJDK 64-Bit Server VM (Zulu 8.68.0.19-CA-macos-aarch64) (build 25.362-b08, mixed mode)
```

## Get started
```
% git clone git@github.com:MIAopenroad/ScalarDBAssignment.git
% cd ScalarDBAssignment
```

At first, you should start frontend-related container. **Don't start from backend!!**

```
% docker-compose up -d frontend-prd
```

Second, you should go to the backend directory, also start backend-related container.

About backend, we have many tasks to do, so this container is managed by **make**.

If you are in the first time, you should run **make init**, otherwise, **make** only.

```
% cd backend
# If you are in the first time, run this.
% curl -LO https://github.com/scalar-labs/scalardb/releases/download/v3.12.3/scalardb-schema-loader-3.12.3.jar
% make init

# otherwise
% make
```
Then, you can see this app on http://localhost:5173/ !

When you want to finish this app, you should stop the backend process that is triggerd by make, and exec docker-compose down.
```
(press Ctrl-c on your terminal that run the backend process)
...
% docker-compose down [-v (if you want to delete data on databases)]
```
