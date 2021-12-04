sttp
* https://sttp.softwaremill.com/en/latest/
* https://javadoc.io/doc/com.softwaremill.sttp.client3/core_2.13/latest/index.html
* https://mvnrepository.com/artifact/com.softwaremill.sttp.client3/core
* https://mvnrepository.com/artifact/com.softwaremill.sttp.client3/circe
* https://mvnrepository.com/artifact/com.softwaremill.sttp.client3/scribe-backend
```text
com.softwaremill.sttp.client3:core_2.13:3.3.18
com.softwaremill.sttp.client3:circe_2.13:3.3.18
com.softwaremill.sttp.client3:scribe-backend_2.13:3.3.18
io.circe:circe-core_2.13:0.14.1
io.circe:circe-generic_2.13:0.14.1
io.circe:circe-parser_2.13:0.14.1
```
```bash
scala -cp $(ls lib|awk '{printf"lib/"$1"/*:"}END{printf"out/production/sttp.client3"}') SttpClientApp
scala -cp $(ls lib|awk '{printf"lib/"$1"/*:"}END{printf"out/production/sttp.client3"}') TestSttpClient
```
