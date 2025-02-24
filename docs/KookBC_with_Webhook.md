# KookBC 与 Webhook

听说你对 WebSocket 模式不满意？没关系，KookBC 也提供对 Webhook 模式的支持！

当然，Webhook 模式的使用是需要配置的！

**请务必正确配置！若您提供的内容有误会导致难以发现的错误！**

**比如您在 Kook 开放平台设置了 encrypt_key ，但是没有将其提供给 KookBC ，这会导致消息内容不能解密并报错。**

**但是报错会被内置的 HTTP 服务器吞掉，导致您不知道异常的发生。**

**本文档默认你已经把 kbc.yml 中的 "mode" 项改为了 "webhook" ，具体请见 [KookBC 配置详解](KookBC_Config.md#mode) 。**

让我们看看 kbc.yml 提供的 Webhook 配置项。

```yaml

## ---- START WEBHOOK CONFIGURATION ----

webhook-port: 8080

webhook-encrypt-key: ""

webhook-verify-token: ""

webhook-route: "kookbc-webhook"

## ---- END WEBHOOK CONFIGURATION ----
```

其中原有的注释已经移除。

## 配置项解读

### webhook-port

此配置项决定 KookBC 的 Webhook 服务将会在哪个端口上启动。

默认为 8080 。

### webhook-encrypt-key

这是您在 Kook 开放平台设置的 Encrypt Key ，请将在开放平台设置的值提供给此配置项。

### webhook-verify-token

这是由 Kook 开放平台提供的 Verify Token ，请将在开放平台设置的值提供给此配置项。

### webhook-route

这是 Webhook 的路由地址。

最终您应该提供给 Kook 开放平台作 Callback 的 URL是： `http(s)://{您的域名}:{webhook-port}/{webhook-route}`

