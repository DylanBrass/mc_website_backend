#!/usr/bin/env bash

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=users-service \
--package-name=com.mc_website.users-service \
--groupId=com.mc_website.users-service \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
users-service

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

