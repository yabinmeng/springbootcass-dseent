## Overview

This repository is **the third scenario of** a seriers of code templates(examples) to demonstrate how to use java Spring framework for data access (insert/update/delete/read) with Cassandra NoSQL database. This series contains the following scenarios:

**[Scenario 1]**:  Use ***Spring Boot*** and ***Spring Data for Cassandra*** for basic CRUD operations (single-column primary key) on simple data types (text, int, etc.) through Rest APIs: https://github.com/yabinmeng/springbootcass-basic

   For this scenario, there is NO special configuration for Cassandra such as user authentication or client-to-server SSL/TLS encyption, etc.

**[Scnenario 2]**: Use ***Spring Boot*** and ***Spring Data for Cassandra*** for more advanced CRUD operations (composite primary key) on more complex data types (UDTs, collections, blobs, etc.) through command line interface: https://github.com/yabinmeng/springbootcass-advanced 

   For this scenario, user autentication is enabled for Cassandra.

**[Scenario 3]**: Use ***Spring Boot*** and ***DataStax Enterprise Java Driver*** to take full potentials of all advanced features as provided by the DataStax driver APIs that are not avaible through Spring Data for Cassandra (or at least in simple and straightforward way), such as flexible load balancing policy, retry policy, connection pooling, more robust object mapping, and a lot more: https://github.com/yabinmeng/springbootcass-dseent 

   For this scenario, user authentication and client-to-server SSL/TLS encryption are both enabled for Cassandra.

---

Please **NOTE**:
* For all scenarios, DataStax Enterprise (DSE) version 5.1.11 is used as the Cassandra database.

* Among these scenarios, **the third one of using Spring Boot and DataStastx Enterprise Java Driver is the recommended approach**. The first 2 approaches, due to its usage with Spring Data for Cassandra, have some limititations that can't be easily addressed and may cause some issues (e.g. unnecessary tombstones) when interacting with Cassandra.

---
