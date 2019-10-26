#!/usr/bin/env bash

java -classpath target/java-sasl-api-1.0-SNAPSHOT.jar \
 -Djava.security.auth.login.config=conf/jaas.conf \
 demo.sasl.DemoClient

