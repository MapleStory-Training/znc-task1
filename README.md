# 模拟文件系统
## 功能列表

| 命令                 |   说明        |
| ---                 | ---           |
| format              | 格式化         |
| mkdir [directory]   | 创建目录       |
| touch [file]        | 创建文件       |
| pwd                 | 查看当前路径    |
| ls                  | 查看当前文件夹  |
| cd [directory]      | 打开目录       |
| cd..                | 返回上级目录    |
| write [file] [data] | 写入数据       |
| cat [file]          | 显示文件内容    |
| cat [file] > [file] | 输出重定向     |

## 核心模块
### 1、OSManager 功能操作模块

* __layout__

<img width="329" alt="截屏2021-04-28 11 13 50" src="https://user-images.githubusercontent.com/23651645/116342804-cc484500-a815-11eb-978a-2a48590bc913.png">

* __field__

<img width="266" alt="image" src="https://user-images.githubusercontent.com/23651645/116355068-e856e100-a82b-11eb-8620-b7a937de5546.png">

* __FAT表定义__

<img width="425" alt="截屏2021-04-28 11 35 37" src="https://user-images.githubusercontent.com/23651645/116342898-f568d580-a815-11eb-86de-bcad25686c59.png">

* __数据块定义__

<img width="592" alt="截屏2021-04-28 11 36 06" src="https://user-images.githubusercontent.com/23651645/116342889-f1d54e80-a815-11eb-83a9-ab71082ad666.png">

### 2、IStorage 存储接口

<img width="348" alt="image" src="https://user-images.githubusercontent.com/23651645/116354725-68307b80-a82b-11eb-8c91-a7c2ddafcaf3.png">

* __FileLineStorage 行存储实现类__
    - 读操作
    - <img width="705" alt="截屏2021-04-28 13 44 26" src="https://user-images.githubusercontent.com/23651645/116354591-391a0a00-a82b-11eb-9095-9a2db144a819.png">
    
    - 写操作
        - File
        - FileWriter
        - BufferedWriter
        - Path
        - Files

## 待优化

* __数据存储粒度__
    - 存储方式为行存储，可以改造为按数据块存储，并按照文件大小分配数据块
    - 指定行写入需要读出所有数据
    - 指定行读出需要遍历

* __FAT数据定时持久化__
    - FAT写入文件时机为系统退出，可以采用不同策略进行数据持久化（可以参考InnoDB日志刷盘机制）

* __删除功能__
    - 未实现删除功能，以及后续的数据块复用需要重新设计

* __其他__
    - 绝对路径操作
    - 追加写入
