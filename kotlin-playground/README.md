# Kotlin Playground

独立于 Android 的纯 Kotlin JVM 模块，用于：

- 学习、练习 Kotlin 语法
- 写可直接运行的 Kotlin 代码（不依赖 Android 环境）

## 如何运行

在项目根目录执行：

```bash
./gradlew :kotlin-playground:run
```

或在 IDE 中右键 `Main.kt` → Run 'MainKt'。

## 使用方式

- 在 `src/main/kotlin/` 下写任意 `.kt` 文件。
- 入口是 `Main.kt` 里的 `main()`，在 `main()` 里调用你写的函数即可运行。
- 本模块未被 app 依赖，不会打进 Android 应用。
