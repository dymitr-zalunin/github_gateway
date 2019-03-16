#!/bin/bash
set -x

java -cp libs/*:.  pl.dzalunin.github.gateway.GithubRepositoriesGateway > logs
