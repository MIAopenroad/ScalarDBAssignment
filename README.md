# AraKimura E-Commerce Site

AraKimura E-Commerce Site is the EC Site Demo application with ScalarDB.

## Demo Video

Comming Soon...

## Prerequisists

This application is highly dockernized.

So, prerequisists are only here!

Feel free to get started!

- Docker
- Make

## Get started

You should two docker containers, frontend-related, backend-related.

At first, you should start frontend-related container. **Don't start from backend!!**

```
% docker-compose up -d frontend-dev
```

Second, you should go to the backend directory, also start backend-related container.

About backend, we have many tasks to do, so this container is managed by **make**.

If you are in the first time, you should run **make init**, otherwise, **make** only.

```
% cd backend
# If you are in the first time, run this.
% make init

# otherwise
% make
```

## Overview

## Database Schema
