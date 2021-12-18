zio
* https://zio.dev/
* https://raw.githubusercontent.com/ghostdogpr/zio-cheatsheet/master/README.md
* https://javadoc.io/doc/dev.zio/zio_2.13/1.0.12/index.html
* https://javadoc.io/doc/dev.zio/zio-test_2.13/1.0.12/index.html
```text
dev.zio:zio_2.13:1.0.12
dev.zio:zio-test_2.13:1.0.12
```

```bash
scala -cp $(ls lib|awk '{printf"lib/"$1"/*:"}END{printf"out/production/zio"}') ZioEnvApp
scala -cp $(ls lib|awk '{printf"lib/"$1"/*:"}END{printf"out/production/zio"}') TestZioEnv
```
