## JVM

### 知识点汇总

Java 是面向对象的静态语言，基于字节码和虚拟机（VM）技术实现了二进制跨平台；并基于 JMM（Java 内存模型）和 GC（垃圾回收，动态管理内存）对内存进行管理。

#### 字节码

    一个字节（byte）指令组成。包含栈操作指令、程序流程控制指令、对象操作指令和算术运算以及类型转换指令。

    JVM 每个线程都有一个独立的线程栈，用于存储栈帧。每一次方法调用，自动创建一个栈帧。

    栈帧由操作数栈，局部变量数组以及一个Class 引用（指向运行时常量池对应的 Class）组成。

#### 类加载器
- 加载 Loading，找 Class 文件
- 验证 Verification，验证格式、依赖
- 准备 Preparation，静态字段、方法表
- 解析 Resolution，符号解析为引用
- 初始化 Initialization，构造器、静态变量赋值，静态代码块
- 使用 Using
- 卸载 Unloading

##### 添加引用类的几种方法
- 放到 JDK 的 lib/ext 下，或者-Djava.ext.dirs
- java –cp/classpath 或者 class 文件放到当前路径
- 自定义 ClassLoader 加载
- 拿到当前执行类的 ClassLoader，反射调用 addUrl 方法添加 Jar 或路径(JDK9 无效)。

#### JMM
- 栈 Stack
    - 线程栈（多个栈帧、方法）
        - 返回值
        - 局部变量表
        - 操作数栈
        - Class、Method 指针
    - Nativ 栈
- 堆 Heap
    - 年轻代 Young-gen
        - 新生代 Eden，存放新生对象，Young GC 时释放
        - S0、S1，新生代进阶，复制清理对象，防止内存碎片
    - 老年代 Old-gen
- 非堆 Non-Heap
    - 元数据 Metaspace
    - CSS Compressed Class Space
    - Code Cache
        - 常量池
        - 方法区

#### GC
- 标记清除算法 Mark and Sweep
    - CMS GC，老年代，并行，CPU 核心 1/4，降低暂停时间
- 整理算法 Mark Sweep Compact
    - ParNew GC 老年代，单线程，STW
    - Parallel GC 老年代，多线程
- 复制算法 Mark Copy
    - Serail GC 年轻代，单线程，STW
    - Parallel GC 年轻代，多线程
    - CMS GC，并行标记清除，年轻代，并行
- G1 实时垃圾回收，没有年轻代、老年代，2048 个小区域
- ZGC 通过着色指针和读屏障，实现几乎全部的并发执行，几毫
  秒级别的延迟，线性可扩展

#### JVM 参数
- 系统属性，-Dmaven.test.skip=true
- 运行模式，server、client、Xint（解释模式，降低运行速度）、Xcomp（预编译为本地代码）、Xmixed（混合模式，默认）
- 堆内存
    - -Xmx 最大堆内存
    - -Xms 最小堆内存，服务端默认与 Xms 一样，减少扩容抖动
    - -Xmn 年轻代大小设置，G1 无效，年轻代=Eden + S0 + S1
        - -XX:NewSize 新生代初始值
        - -XX:MaxNewSize 新生代最大值
    - -Xss 线程栈占用内存大小 1024K
    - -XX:NewRatio 设置老年代和年轻代的比例（默认值 2），=8 老年代=Xmx\*8/9，年轻代=Xmx\*1/9
    - -XX:SurvivorRatio=8，新生代和存活区的比例（默认值8），S0:S1:Eden = 1:1:8，S0=S1=Xmn\*1/10，Eden=Xmn\*8/10
    - -XX:MinHeapFreeRatio=40 GC后，如果发现空闲堆内存占到整个预估上限值的40%，则增大上限值
    - -XX:MaxHeapFreeRatio=70 GC后，如果发现空闲堆内存占到整个预估上限值的70%，则收缩预估上限值
- -XX:MetaspaceSize=128m 初始元空间大小，达到该值就会触发垃圾收集进行类型卸载，同时GC会对该值进行调整：如果释放了大量的空间，就适当降低该值；如果释放了很少的空间，那么在不超过MaxMetaspaceSize时，适当提高该值
- -XX:MaxMetaspaceSize=256m 设置元空间的最大值，默认是没有上限的，也就是说你的系统内存上限是多少它就是多少。默认没有上限，在技术上，Metaspace的尺寸可以增长到交换空间
#### JVM 工具
- jps/jinfo 查看 java 进程
- jstat 查看 JVM 内部 gc 相关信息
- jmap 查看 heap 或类占用空间统计
- jstack 查看线程信息
- jcmd 整合命令

### 总结

- JMM 和 GC 是 JVM 管理内存的核心，理解其原理对于问题排查，性能优化起到关键的作用。需要不断的学习、思考和加深理解。
- 字节码和类加载器，以及 CPU 的执行方式决定了 Java 运行时的行为，类加载器的应用需要更加深入一些。
- JVM 参数和工具是依据原理配置、了解运行时的工具，因此需要尝尝应用，积累场景。

## NIO

### BIO

只能单线程阻塞式等待数据由内核态进入用户态，性能低下，CPU 利用率低。

### 非阻塞式 IO

轮询查询数据是否准备好了，没有准备好时，直接返回 error，CPU 可以处理其他事情。但是数据从内核态到用户态仍然需要等待。

### IO 多路复用 事件驱动IO(event driven IO)

单个线程同时监听多个套接字，通过 select、poll 来轮询。

select/poll 的几大缺点：
（1）每次调用 select，都需要把 fd 集合从用户态拷贝到
内核态，这个开销在 fd 很多时会很大
（2）同时每次调用 select 都需要在内核遍历传递进来的
所有 fd，这个开销在 fd 很多时也很大
（3）select 支持的文件描述符数量太小了，默认是1024
epoll（Linux 2.5.44内核中引入,2.6内核正式引入,可被用
于代替 POSIX select 和 poll 系统调用）：
（1）内核与用户空间共享一块内存
（2）通过回调解决遍历问题
（3）fd 没有限制，可以支撑10万连接

### 异步 IO

全流程非阻塞，数据准备和数据拷贝。

### Netty

- Channel 通道，Java NIO 中的基础概念,代表一个打开的连接,可执行读取/写入 IO 操作。
  Netty 对 Channel 的所有 IO 操作都是非阻塞的。

- ChannelFuture
  Java 的 Future 接口，只能查询操作的完成情况, 或者阻塞当前线程等待操作完成。
  Netty 封装一个 ChannelFuture 接口。
  我们可以将回调方法传给 ChannelFuture，在操作完成时自动执行。

- Event & Handler Netty 基于事件驱动，事件和处理器可以关联到入站和出站数据流。
- Encoder &
  Decoder
  处理网络 IO 时，需要进行序列化和反序列化, 转换 Java 对象与字节流。
  对入站数据进行解码, 基类是 ByteToMessageDecoder。
  对出站数据进行编码, 基类是 MessageToByteEncoder。

- ChannelPipeline 数据处理管道就是事件处理器链。
  有顺序、同一 Channel 的出站处理器和入站处理器在同一个列表中。

### 高性能

- 高并发
- 高吞吐
- 低延迟
- 负面
    - 系统复杂度提升
    - 建设维护成本增加
    - 故障破坏半径增加
- 混沌工程
    - 容量预估
    - 爆炸半径
    - 工程积累与改进

### API 网关

- 请求接入
- 业务聚合
- 中介策略，安全、验证、路由、管理
- 统一管理，对 API 和策略进行统一管理


## 并发编程

### Thread
1. Thread.sleep(long millis)，一定是当前线程调用此方法，当前线程进入 TIMED_WAITING 状态，但不释放对象锁，
   millis 后线程自动苏醒进入就绪状态。作用：给其它线程执行机会的最佳方式。
2. Thread.yield()，一定是当前线程调用此方法，当前线程放弃获取的 CPU 时间片，但不释放锁资源，由运行状态变为就
   绪状态，让 OS 再次选择线程。作用：让相同优先级的线程轮流执行，但并不保证一定会轮流执行。实际中无法保证
   yield() 达到让步目的，因为让步的线程还有可能被线程调度程序再次选中。Thread.yield() 不会导致阻塞。该方法与
   sleep() 类似，只是不能由用户指定暂停多长时间。
3. t.join()/t.join(long millis)，当前线程里调用其它线程 t 的 join 方法，当前线程进入WAITING/TIMED_WAITING 状态，
   当前线程不会释放已经持有的对象锁。线程t执行完毕或者 millis 时间到，当前线程进入就绪状态。
4. obj.wait()，当前线程调用对象的 wait() 方法，当前线程释放对象锁，进入等待队列。依靠 notify()/notifyAll() 唤醒或
   者 wait(long timeout) timeout 时间到自动唤醒。
5. obj.notify() 唤醒在此对象监视器上等待的单个线程，选择是任意性的。notifyAll() 唤醒在此对象监视器上等待的所有线
   程。
- 线程与操作系统线程对应
- Object wait、notify
- Thread sleep、join、yield
- 并发的原子性、可见性
- 线程池
    1. Excutor: 执行者 – 顶层接口
    2. ExcutorService: 接口 API
    3. ThreadFactory: 线程工厂
    4. Excutors: 工具类

### Java 并发包

- 锁机制类 Locks : Lock, Condition, ReadWriteLock
- 原子操作类 Atomic : AtomicInteger
- 线程池相关类 Executer : Future, Callable, Executor
- 信号量三组工具类 Tools : CountDownLatch, CyclicBarrier, Semaphore
- 并发集合类 Collections : CopyOnWriteArrayList, ConcurrentMap
- 锁
    1. 粒度
    2. 性能
    3. 重入
    4. 公平
    5. 自旋锁(spinlock)
    6. 场景: 脱离业务场景谈性能都是耍流氓

## Spring 和 ORM

1. Core:Bean/Context/AOP
2. Testing:Mock/TestContext
3. DataAccess:Tx/JDBC/ORM
4. SpringMVC/WebFlux:web
5. Integration:remoting/JMS/WS
6. Languages:Kotlin/Groovy

### 线程安全
可见性：对于可见性，Java 提供了 volatile 关键字来保证可见性。
当一个共享变量被 volatile 修饰时，它会保证修改的值会立即被更新到主存，当有其他
线程需要读取时，它会去内存中读取新值。
另外，通过 synchronized 和 Lock 也能够保证可见性，synchronized 和 Lock 能保证
同一时刻只有一个线程获取锁然后执行同步代码，并且在释放锁之前会将对变量的修改
刷新到主存当中。
volatile 并不能保证原子性。

1. 程序次序规则：一个线程内，按照代码先后顺序
2. 锁定规则：一个 unLock 操作先行发生于后面对同一个锁的 lock 操作
3. Volatile 变量规则：对一个变量的写操作先行发生于后面对这个变量的读操作
4. 传递规则：如果操作 A 先行发生于操作 B，而操作 B 又先行发生于操作 C，则可以得出 A 先于 C
5. 线程启动规则：Thread 对象的 start() 方法先行发生于此线程的每个一个动作
6. 线程中断规则：对线程 interrupt() 方法的调用先行发生于被中断线程的代码检测到中断事件的发生
7. 线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过 Thread.join() 方法结束、
   Thread.isAlive() 的返回值手段检测到线程已经终止执行
8. 对象终结规则：一个对象的初始化完成先行发生于他的 finalize() 方法的开始

### 线程池
BlockingQueue 是双缓冲队列。BlockingQueue 内部使用两条队列，允许两个线程同
时向队列一个存储，一个取出操作。在保证并发安全的同时，提高了队列的存取效率。
1. ArrayBlockingQueue:规定大小的 BlockingQueue，其构造必须指定大小。其所含
   的对象是 FIFO 顺序排序的。
2. LinkedBlockingQueue:大小不固定的 BlockingQueue，若其构造时指定大小，生
   成的 BlockingQueue 有大小限制，不指定大小，其大小有 Integer.MAX_VALUE 来
   决定。其所含的对象是 FIFO 顺序排序的。
3. PriorityBlockingQueue:类似于 LinkedBlockingQueue，但是其所含对象的排序不
   是 FIFO，而是依据对象的自然顺序或者构造函数的 Comparator 决定。
4. SynchronizedQueue:特殊的 BlockingQueue，对其的操作必须是放和取交替完成。

拒绝策略
1. ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出 RejectedExecutionException
   异常。
2. ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。
3. ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提
   交被拒绝的任务
4. ThreadPoolExecutor.CallerRunsPolicy：由调用线程（提交任务的线程）处理该任
   务

### Java.util.concurrency
锁机制类 Locks : Lock, Condition, ReadWriteLock
原子操作类 Atomic : AtomicInteger
线程池相关类 Executer : Future, Callable, Executor
信号量三组工具类 Tools : CountDownLatch, CyclicBarrier, Semaphore
并发集合类 Collections : CopyOnWriteArrayList, ConcurrentMap

synchronized 方式的问题：
1、同步块的阻塞无法中断（不能 Interruptibly）
2、同步块的阻塞无法控制超时（无法自动解锁）
3、同步块无法异步处理锁（即不能立即知道是否可以拿到锁）
4、同步块无法根据条件灵活的加锁解锁（即只能跟同步块范围一致）

Doug Lea《Java 并发编程：设计原则与模式》一书中，
推荐的三个用锁的最佳实践，它们分别是：
1. 永远只在更新对象的成员变量时加锁
2. 永远只在访问可变的成员变量时加锁
3. 永远不在调用其他对象的方法时加锁
   KK总结-最小使用锁：
   1、降低锁范围：锁定代码的范围/作用域
   2、细分锁粒度：讲一个大锁，拆分成多个小锁
   AOP-面向切面编程 Spring早期版本的核心功能，管理对象生命周期与对象装配。
   为了实现管理和装配，一个自然而然的想法就是，加一个中间层代理(字节码增强)来 实现所有对象的托管。

AOP-面向切面编程 Spring早期版本的核心功能，管理对象生命周期与对象装配。
为了实现管理和装配，一个自然而然的想法就是，加一个中间层代理(字节码增强)来 实现所有对象的托管。

IoC-控制反转
也称为DI(Dependency Injection)依赖注入。
对象装配思路的改进。
从对象A直接引用和操作对象B，变成对象A里指需要依赖一个接口IB，系统启动和装配 阶段，把IB接口的实例对象注入到对象A，这样A就不需要依赖一个IB接口的具体实现， 也就是类B。

#### Spring Boot 的出发点
Spring 臃肿以后的必然选择。 一切都是为了简化。
- 让开发变简单:
- 让配置变简单:
- 让运行变简单:

#### Hibernate
ORM(Object-Relational Mapping) 表示对象关 系映射。
Hibernate 是一个开源的对象关系映射框架，它对 JDBC 进行了非常轻量级的对象封装，它将 POJO 与 数据库表建立映射关系，是一个全自动的 orm 框架 ，hibernate 可以自动生成 SQL 语句，自动执行， 使得 Java 程序员可以使用面向对象的思维来操纵数 据库。
Hibernate 里需要定义实体类和 hbm 映射关系文件 (IDE 一般有工具生成)。


#### MyBatis
MyBatis 是一款优秀的持久层框架， 它支持定制化 SQL、存储过程以及高 级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结 果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接 口和 Java 的 POJOs(Plain Old Java Objects,普通的 Java 对象)映射成数 据库中的记录。

#### JPA
JPA 的全称是 Java Persistence API，
即 Java 持久化 API，是一套基于 ORM 的规范，
内部是由一系列的接口和抽象类构成。
JPA 通过 JDK 5.0 注解描述对象-关系表映射关系 ，并将运行期的实体对象持久化到数据库中。
核心 EntityManager

1. Core：Bean/Context/AOP
2. Testing：Mock/TestContext
3. DataAccess: Tx/JDBC/ORM
4. Spring MVC/WebFlux: web
5. Integration: remoting/JMS/WS
6. Languages: Kotlin/Groovy

### Spring Boot 如何做到简化
为什么能做到简化：
1、Spring 本身技术的成熟与完善，各方面第三方组件的成熟集成
2、Spring 团队在去 web 容器化等方面的努力
3、基于 MAVEN 与 POM 的 Java 生态体系，整合 POM 模板成为可能
4、避免大量 maven 导入和各种版本冲突
Spring Boot 是 Spring 的一套快速配置脚手架，关注于自动配置，配置驱动

优势在于，开箱即用：
一、Maven 的目录结构：默认有 resources 文件夹存放配置文件。默认打包方式为 jar。
二、默认的配置文件：application.properties 或 application.yml 文件
三、默认通过 spring.profiles.active 属性来决定运行环境时的配置文件。
四、EnableAutoConfiguration 默认对于依赖的 starter 进行自动装载。
五、spring-boot-start-web 中默认包含 spring-mvc 相关依赖以及内置的 web容器，使得
构建一个 web 应用更加简单。

### 数据库连接池
C3P0
DBCP--Apache CommonPool
Druid
Hikari

Spring 管理事务
Spring 声明式事务配置参考
事务的传播性：
@Transactional(propagation=Propagation.REQUIRED)
事务的隔离级别：
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
读取未提交数据(会出现脏读, 不可重复读) 基本不使用
只读：
@Transactional(readOnly=true)
该属性用于设置当前事务是否为只读事务，设置为 true 表示只读，false 则表示可读写，默认值为 false。
事务的超时性：
@Transactional(timeout=30)
回滚：
指定单一异常类：@Transactional(rollbackFor=RuntimeException.class)
指定多个异常类：@Transactional(rollbackFor={RuntimeException.class, Exception.class})

## MySQL
MySQL 事务
事务可靠性模型 ACID:
•Atomicity: 原子性, 一次事务中的操作要么全部成功, 要么全部失败。
•Consistency: 一致性, 跨表、跨行、跨事务, 数据库始终保持一致状态。
•Isolation: 隔离性, 可见性, 保护事务不会互相干扰, 包含4种隔离级别。
•Durability:, 持久性, 事务提交成功后,不会丢数据。如电源故障, 系统崩溃。

行级锁(InnoDB)
•记录锁(Record): 始终锁定索引记录，注意隐藏的聚簇索引;
•间隙锁(Gap):
•临键锁(Next-Key): 记录锁+间隙锁的组合; 可“锁定”表中不存在记录
•谓词锁(Predicat): 空间索引
死锁:
-阻塞与互相等待
-增删改、锁定读
-死锁检测与自动回滚
-锁粒度与程序设计

《SQL:1992标准》规定了四种事务隔离级别(Isolation):
• 读未提交: READ UNCOMMITTED
• 读已提交: READ COMMITTED
• 可重复读: REPEATABLE READ
• 可串行化: SERIALIZABLE
事务隔离是数据库的基础特征。
MySQL:
• 可以设置全局的默认隔离级别
• 可以单独设置会话的隔离级别
• InnoDB 实现与标准之间的差异

主从复制原理
核心是
1、主库写 binlog
2、从库 relay log

2000年，MySQL 3.23.15版本引入了复制
2002年，MySQL 4.0.2版本分离 IO 和 SQL 线程，引入了 relay log
2010年，MySQL 5.5版本引入半同步复制
2016年，MySQL 在5.7.17中引入 InnoDB Group Replication

MySQL 高可用1：主从手动切换
用 LVS+Keepalived 实现多个节点的探活+请求路由。
配置 VIP 或 DNS 实现配置不变更。

MySQL 高可用2：MHA
MHA（Master High Availability）目前在 MySQL 高可用方面是一个相对成熟的解决方
案，它由日本 DeNA 公司的 youshimaton（现就职于 Facebook 公司）开发，是一套
优秀的作为 MySQL 高可用性环境下故障切换和主从提升的高可用软件。
基于 Perl 语言开发，一般能在30s内实现主从切换。
切换时，直接通过 SSH 复制主节点的日志。

MySQL 高可用3：MGR *
如果主节点挂掉，将自动选择某个从改成主；
无需人工干预，基于组复制，保证数据一致性。
> 问题：
>1. 外部获得状态变更需要读取数据库。
>2. 外部需要使用 LVS/VIP 配置。
## 分库分表

从读写分离到数据库拆分
还有没有其他办法？
主从结构解决了高可用，读扩展，但是单机容量不变，单机写性能无法解决。
提升容量-->分库分表，分布式，多个数据库，作为数据分片的集群提供服务。
降低单个节点的写压力。
提升整个系统的数据容量上限。

扩展立方体
X 轴：通过 clone 整个系统复制，集群
Y 轴：通过解耦不同功能复制，业务拆分
Z 轴：通过拆分不同数据扩展，数据分片

数据库水平拆分
水平拆分（按时间分库分表）：很多时候，我们的数据是有时间属性的，所以自然可以按照时
间维度来拆分。比如当前数据表和历史数据表，甚至按季度，按月，按天来划分不同的表。这
样我们按照时间维度来查询数据时，就可以直接定位到当前的这个子表。更详细的分析参考下
一个小节。
强制按条件指定分库分表：比如配置好某些用户的数据进入单独的库表，其他数据默认处理。
自定义方式分库分表：指定某些条件的数据进入到某些库或表。
### MySQL 存储 独占模式
1. 日志组文件:ib_logfile0和ib_logfile1，默认均为5M 2)、表结构文件:*.frm
3. 独占表空间文件:*.ibd
4. 字符集和排序规则文件:db.opt
5. binlog 二进制日志文件:记录主数据库服务器的 DDL 和 DML 操作 6)、二进制日志索引文件:master-bin.index
   共享模式 innodb_file_per_table=1
6. 数据都在 ibdata1

1)连接请求的变量
1、max_connections
2、back_log
3、wait_timeout和interative_timeout

2)缓冲区变量
4、key_buffer_size
5、query_cache_size(查询缓存简称 QC)
6、max_connect_errors:
7、sort_buffer_size:
8、max_allowed_packet=32M
9、join_buffer_size=2M
10、thread_cache_size=300

3)配置 Innodb 的几个变量
11、innodb_buffer_pool_size
12、innodb_flush_log_at_trx_commit
13、innodb_thread_concurrency=0
14、innodb_log_buffer_size
15、innodb_log_file_size=50M
16、innodb_log_files_in_group=3
17、read_buffer_size=1M
18、read_rnd_buffer_size=16M
19、bulk_insert_buffer_size=64M
20、binary log

### 最佳实践
- 如何恰当选择引擎?
- 库表如何命名?
- 如何合理拆分宽表?
- 如何选择恰当数据类型:明确、尽量小
- char、varchar 的选择
- (text/blob/clob)的使用问题? - 文件、图片是否要存入到数据库? - 时间日期的存储问题?
- 数值的精度问题?
- 是否使用外键、触发器?
- 唯一约束和索引的关系?
- 是否可以冗余字段?
- 是否使用游标、变量、视图、自定义函数、存储过程? - 自增主键的使用问题?
- 能够在线修改表结构(DDL 操作)?
- 逻辑删除还是物理删除?
- 要不要加 create_time,update_time 时间戳?
- 数据库碎片问题?
- 如何快速导入导出、备份数据?

### MySQL 存储 独占模式
1. 日志组文件:ib_logfile0和ib_logfile1，默认均为5M 2)、表结构文件:*.frm
3. 独占表空间文件:*.ibd
4. 字符集和排序规则文件:db.opt
5. binlog 二进制日志文件:记录主数据库服务器的 DDL 和 DML 操作 6)、二进制日志索引文件:master-bin.index
   共享模式 innodb_file_per_table=1
6. 数据都在 ibdata1

1)连接请求的变量
1、max_connections
2、back_log
3、wait_timeout和interative_timeout

2)缓冲区变量
4、key_buffer_size
5、query_cache_size(查询缓存简称 QC)
6、max_connect_errors:
7、sort_buffer_size:
8、max_allowed_packet=32M
9、join_buffer_size=2M
10、thread_cache_size=300

3)配置 Innodb 的几个变量
11、innodb_buffer_pool_size
12、innodb_flush_log_at_trx_commit
13、innodb_thread_concurrency=0
14、innodb_log_buffer_size
15、innodb_log_file_size=50M
16、innodb_log_files_in_group=3
17、read_buffer_size=1M
18、read_rnd_buffer_size=16M
19、bulk_insert_buffer_size=64M
20、binary log

### 最佳实践
- 如何恰当选择引擎?
- 库表如何命名?
- 如何合理拆分宽表?
- 如何选择恰当数据类型:明确、尽量小
- char、varchar 的选择
- (text/blob/clob)的使用问题? - 文件、图片是否要存入到数据库? - 时间日期的存储问题?
- 数值的精度问题?
- 是否使用外键、触发器?
- 唯一约束和索引的关系?
- 是否可以冗余字段?
- 是否使用游标、变量、视图、自定义函数、存储过程? - 自增主键的使用问题?
- 能够在线修改表结构(DDL 操作)?
- 逻辑删除还是物理删除?
- 要不要加 create_time,update_time 时间戳?
- 数据库碎片问题?
- 如何快速导入导出、备份数据?

## 分库分表


## RPC 和微服务

### RPC原理

RPC的简化版原理如下图。
核心是代理机制。

1. 本地代理存根: Stub
2. 本地序列化反序列化
3. 网络通信
4. 远程序列化反序列化
5. 远程服务存根: Skeleton
6. 调用实际业务服务
7. 原路返回服务结果
8. 返回给本地调用方

常见的RPC技术
- Corba/RMI/.NET Remoting
- JSON RPC, XML RPC，WebService(Axis2, CXF)
- Hessian, Thrift, Protocol Buffer, gRPC

具体的分布式业务场景里，除了能够调用远程方法，我们还需要考虑什么？
1、多个相同服务如何管理？
2、服务的注册发现机制？
3、如何负载均衡，路由等集群功能？
4、熔断，限流等治理能力。
5、重试等策略
6、高可用、监控、性能等等。

### Dubbo
整体架构
1. config 配置层：对外配置接口，以 ServiceConfig, ReferenceConfig 为中心，可以直接初始
   化配置类，也可以通过 spring 解析配置生成配置类
   2.proxy 服务代理层：服务接口透明代理，生成服务的客户端 Stub 和服务器端 Skeleton, 以
   ServiceProxy 为中心，扩展接口为 ProxyFactory
   3.registry 注册中心层：封装服务地址的注册与发现，以服务 URL 为中心，扩展接口为
   RegistryFactory, Registry, RegistryService
   4.cluster 路由层：封装多个提供者的路由及负载均衡，并桥接注册中心，以 Invoker 为中心，
   扩展接口为 Cluster, Directory, Router, LoadBalance
   5.monitor 监控层：RPC 调用次数和调用时间监控，以 Statistics 为中心，扩展接口为
   MonitorFactory, Monitor, MonitorService

配置中心（ConfigCenter）：管理系统需要的配置参数信息
注册中心（RegistryCenter）：管理系统的服务注册、提供发现和协调能力
元数据中心（MetadataCenter）：管理各个节点使用的元数据信息

## 分布式缓存

缓存穿透
问题：大量并发查询不存在的KEY，导致都直接将压力透传到数据库。
分析：为什么会多次透传呢？不存在一直为空。
需要注意让缓存能够区分KEY不存在和查询到一个空值。
解决办法：
1、缓存空值的KEY，这样第一次不存在也会被加载会记录，下次拿到有这个KEY。
2、Bloom过滤或RoaringBitmap 判断KEY是否存在。
3、完全以缓存为准，使用 延迟异步加载 的策略2，这样就不会触发更新

缓存击穿
问题：某个KEY失效的时候，正好有大量并发请求访问这个KEY。
分析：跟前面一个其实很像，属于比较偶然的。
解决办法：
1、KEY的更新操作添加全局互斥锁。
2、完全以缓存为准，使用 延迟异步加载 的策略2，这样就不会触发更新

缓存雪崩
问题：当某一时刻发生大规模的缓存失效的情况，会有大量的请求进来直接打到数据库，导致数
据库压力过大升值宕机。
分析：一般来说，由于更新策略、或者数据热点、缓存服务宕机等原因，可能会导致缓存数据同
一个时间点大规模不可用，或者都更新。所以，需要我们的更新策略要在时间上合适，数据要均
匀分散，缓存服务器要多台高可用。
解决办法：
1、更新策略在时间上做到比较均匀。
2、使用的热数据尽量分散到不同的机器上。
3、多台机器做主从复制或者多副本，实现高可用。
4、实现熔断限流机制，对系统进行负载能力控制

Redis 性能优化
- 核心优化点：
  1、内存优化
  https://redis.io/topics/memory-optimization
  hash-max-ziplist-value 64
  zset-max-ziplist-value 64
  2、CPU优化
  不要阻塞
  谨慎使用范围操作
  SLOWLOG get 10 默认10毫秒，默认只保留最后的128条

Redis 使用的一些经验
1、性能：
1) 线程数与连接数；
2) 监控系统读写比和缓存命中率；
   2、容量：
1) 做好容量评估，合理使用缓存资源；
   3、资源管理和分配：
1) 尽量每个业务集群单独使用自己的Redis，不混用；
2) 控制Redis资源的申请与使用，规范环境和Key的管理（以一线互联网为例）；
3) 监控CPU

## 分布式消息队列

MQ的四大作用
对比其他通信模式，MQ的优势在于：
- 异步通信：异步通信，减少线程等待，特别是处理批量等大事务、耗时操作。
- 系统解耦：系统不直接调用，降低依赖，特别是不在线也能保持通信最终完成。
- 削峰平谷：压力大的时候，缓冲部分请求消息，类似于背压处理。
- 可靠通信：提供多种消息模式、服务质量、顺序保障等。

三种QoS（注意：这是消息语义的，不是业务语义的）：
- At most once，至多一次，消息可能丢失但是不会重复发送；
- At least once，至少一次，消息不会丢失，但是可能会重复；
- Exactly once，精确一次，每条消息肯定会被传输一次且仅一次。

关注于应用层的API协议( ~ 类似JDBC)
Message结构与Queue概念
• Body\Header\Property, messages types
• Queue\Topic\TemporaryQueue\TemporaryTopic
• Connection\Session\Producer\Consumer\DurableSubscription
Messaging行为
• PTP&Pub-Sub
• 持久化
• 事务机制
• 确认机制
• 临时队列
