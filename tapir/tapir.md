tapir
* https://tapir.softwaremill.com/en/latest/
```
com.softwaremill.sttp.tapir:tapir-akka-http-server_2.13:0.19.1
com.softwaremill.sttp.tapir:tapir-core_2.13:0.19.1
com.softwaremill.sttp.tapir:tapir-json-circe_2.13:0.19.1
com.softwaremill.sttp.tapir:tapir-swagger-ui-bundle_2.13:0.19.1
com.softwaremill.sttp.tapir:tapir-zio_2.13:0.19.1
com.softwaremill.sttp.tapir:tapir-zio-http-server_2.13:0.19.1
```
```bash
scala -cp $(ls lib|awk '{printf"lib/"$1"/*:"}END{printf"out/production/tapir"}') TapirZioHttpApp
curl http://localhost:9991/ok
curl http://localhost:9991/system/pwd
scala -cp $(ls lib|awk '{printf"lib/"$1"/*:"}END{printf"out/production/tapir"}') TapirAkkaHttpApp
curl http://localhost:9992/ok
```
* http://localhost:9992/docs
