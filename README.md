# MyHaruna

# 安卓看板娘

## English

### Introduction to MyHaruna

MyHaruna is an Android app that displays a *Yuru-chara* (Chinese: 看板娘, Japanese: ゆるキャラ) on the desktop or above other apps.

### Download

**Note:** Current release is a **pre-release** version, which may be **unstable**.

[Click here to go to Release page](https://github.com/iamsqy/MyHaruna/releases/tag/v1.0-alpha) to download this pre-release version.

### Features and Implementation

The program uses ``webView`` to display a local ``html`` file with ``javascript`` that contains a *Yuru-chara* (who is *Hatsune Miku*, a Japanese virtual singer, in the prerelease version).

If you touch the window, you will receive an *emoticon* (``Toast.makeText``).

If either low battery or network unstable, the *Yuru-chara* will ask user to quit.

There will also be a notification to guide user for quitting.

Class ``MainActivity`` mainly implements initialization and basic GUI.

Class ``NetworkChangeReceiver`` receives a network connectivity change.

Class ``Quit`` terminates the whole program.

Class ``TestService`` mainly displays the *Yuru-chara*.

### Bug Report

We sincerely wish you can report a bug to us, or give us advice. You can create an issue or send me an email (iamsqy@outlook.com).

## 简体中文

### MyHaruna 的简介

MyHaruna 是一个在安卓桌面或其他应用上层展示 *看板娘* (英文: *Yuru-chara*, 日文: ゆるキャラ) 的应用程序。

### 下载

**请注意:** 当前发行为 **pre-release** 版本，可能**不稳定**。

[点击此处去 Release 页面](https://github.com/iamsqy/MyHaruna/releases/tag/v1.0-alpha) 下载此预发行版本。

### 特性与实现

程序使用 ``webView`` 本地 ``html`` 文件，文件中的 ``javascript`` 包含看板娘 (预览版是 *初音未来*, 日本虚拟歌姬)

如果您点击看板娘，会收到一条*颜文字* (``Toast.makeText``)。

如果低电量或网络变化，看板娘会询问是否退出。

通知栏会有引导用户退出的通知。

类 ``MainActivity`` 主要实现初始化和基础图形用户界面。

类 ``NetworkChangeReceiver`` 接收网络连接变化。

类 ``Quit`` 结束整个程序。

类 ``TestService`` 主要用于显示看板娘。

### 报告漏洞

我们真诚地希望您可以报告问题或提供建议。您可以创建一个 Issue，或者给我发送邮件 (iamsqy@outlook.com)。
