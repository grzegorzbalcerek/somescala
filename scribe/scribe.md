
scribe
* https://github.com/outr/scribe
* https://javadoc.io/doc/com.outr/scribe_2.13/latest/index.html
* https://javadoc.io/doc/com.outr/scribe-file_2.13/latest/index.html
* https://www.matthicks.com/2018/02/scribe-2-fastest-jvm-logger-in-world.html
* https://www.matthicks.com/2017/01/logging-performance.html
* https://mvnrepository.com/artifact/com.outr/scribe
* https://mvnrepository.com/artifact/com.outr/scribe-file
```text
com.outr:scribe_2.13:3.6.3
com.outr:scribe-file_2.13:3.6.3
```
```bash
scala -cp $(ls lib|awk '{printf"lib/"$1"/*:"}END{printf"out/production/scribe"}') ScribeApp
```
