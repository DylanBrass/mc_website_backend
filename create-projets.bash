#!/usr/bin/env bash

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=customers-service \
--package-name=com.mc_website.customers-service \
--groupId=com.mc_website.customers-service \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
customers-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=orders-service \
--package-name=com.mc_website.orders-service \
--groupId=com.mc_website.orders-service \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
orders-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=api-gateway \
--package-name=com.mc_website.apigateway \
--groupId=com.mc_website.apigateway \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
api-gateway

