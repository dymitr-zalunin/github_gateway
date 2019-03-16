# Github gateway #

## Build and run

Clone the repo:

```
  git clone https://dzalunin@bitbucket.org/dzalunin/github_gateway.git
```
  
Build:

```
  gradle build
```
All artifact will be stored in the ``bin/libs`` directory.

Run as a background process:
  

```
  cd bin
```
```
  nohup ./run.sh &
```

 server listens on port ``8080``. Logs are located in the ``bin/logs`` file
 
## Performance report
 
 Summary of the performace test is located in the [report](report) directory. Also it's available [online](http://54.214.149.134/index.html).
 
 Performance test was run against t2.micro EC2 instance:
 
 * 1 Core Intel(R) Xeon(R) CPU E5-2676 v3 @ 2.40GHz
 * 1 GB RAM 
 
 Tested server is available under [http://54.214.149.134:8080/repositories/protocolbuffers/protobuf](http://54.214.149.134:8080/repositories/protocolbuffers/protobuf)
