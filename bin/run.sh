#!/bin/bash
set -x

java -cp lib/*:../build/libs/github.gateway-0.1.jar  pl.dzalunin.github.gateway.GithubRepositoriesGateway
