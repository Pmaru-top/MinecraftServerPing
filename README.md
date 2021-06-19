# MinecraftServerPing
[**English**](README-EN.md) | [**简体中文**](README.md)

MinecraftServerPing 是一个 Minecraft 服务器信息获取工具
`MX233/MinecraftServerPing` 参考了 [`jamietech/MinecraftServerPing`](https://github.com/jamietech/MinecraftServerPing) 的部分代码

您可以通过此工具获取以下信息
----
- `版本`
- `玩家数量`
- `图标`
- `mod信息(服务器类型,Mod列表,Mod数量)`
- `描述`
----
`MinecraftServerPing` 需要 [FastJSON](https://github.com/alibaba/fastjson) 依赖

使用示例请见 [Main](https://github.com/MX233/MinecraftServerPing/blob/main/tax/cute/Main.java)

PS:如果服务器不返回某些JSON对象 获取它时你将得到`默认值` 如"-1" "null"

:grin:
