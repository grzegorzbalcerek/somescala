zio-http
* https://github.com/dream11/zio-http
```text
io.d11:zhttp_2.13:1.0.0.0-RC17
```

```bash
scala -cp $(ls lib|awk '{printf"lib/"$1"/*:"}END{printf"out/production/zio-http"}') ZioHttpApp
curl http://localhost:9990/ok
curl http://localhost:9990/system/pwd
```
