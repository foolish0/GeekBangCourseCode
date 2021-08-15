# GeekBangCourseCode

### Week01
自定义类加载器：/01_jvm/src/MyClassLoader.java

jvm内存和启动参数对应关系：/otherFiles/启动参数对应内存结构.pdf

### Week02

不同GC和对内存总结：

（tips：MacBook Pro、8G双核）

- 串行GC
  概念：
  单线程垃圾收集器，不能进行并行处理，会触发STW，停止所有应用线程。

  启动参数：
  -XX:+UseSerialGC

  过程：
  串行GC对年轻代使用mark-copy算法（标记-复制），对老年代使用mark-sweep-compact（标记-清除-整理）算法.

  特点：
  这种GC算法不能充分利用到多核CPU的优势，在垃圾收集时只能使用单个核心。只适合堆内存不大的JVM，而且是单核CPU场景，比方说客户端应用。

  日志分析：
  启动示例程序：java -Xmx256m -Xms256m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:-UseAdaptiveSizePolicy -XX:+UseSerialGC GCLogAnalysis
  GC日志：(截取部分)

  	2021-08-13T20:41:02.014-0800: [GC (Allocation Failure) 2021-08-13T20:41:02.014-0800: [DefNew: 78651K->8703K(78656K), 0.0265053 secs] 212477K->165635K(253440K), 0.0265543 secs] [Times: user=0.02 sys=0.01, real=0.03 secs]
  	-- 解释
  	2021-08-13T20:41:02.014-0800 是GC事件开始时间；
  	
  	这条日志是一次MinorGC，即新生代GC，触发原因是Allocation Failure（分配空间失败）；
  	
  	DefNew为垃圾收集器名称，表明在年轻代中使用。78651K->8703K(78656K)表明GC前后分别的内存大小，以及年轻代总的内存大小，耗时0.0265053秒；212477K->165635K(253440K)表明GC前后整个堆内存的大小变化及总的堆内存大小，这次GC共耗时0.0265543秒；
  	从新生代减少的空间和整个堆内存占用减少的空间可以间接知道有多少对象从年轻代晋升到了老年代。

  	[Times: user=0.02 sys=0.01, real=0.03 secs]是GC时间的持续时间：
  	user – 在此次垃圾回收过程中, 所有 GC线程所消耗的CPU时间之和。
  	sys – GC过程中中操作系统调用和系统等待事件所消耗的时间。
  	real – 应用程序暂停的时间。因为串行GC只使用单线程, 因此 real time 等于 user 和 system 时间的总和。


  	2021-08-13T20:41:02.052-0800: [GC (Allocation Failure) 2021-08-13T20:41:02.052-0800: [DefNew: 78655K->78655K(78656K), 0.0000215 secs]2021-08-15T20:41:02.052-0800: [Tenured: 156931K->168282K(174784K), 0.0423257 secs] 235587K->168282K(253440K), [Metaspace: 2562K->2562K(1056768K)], 0.0424106 secs] [Times: user=0.04 sys=0.00, real=0.04 secs]
  	-- 解释
  	这条日志是一次MajorGC，回收老年代，但通常伴随出现至少一次MinorGC，触发原因是Allocation Failure（分配空间失败）；
  	DefNew：在年轻代进行了一次MinorGC，清空年轻代到0（！！！这里JVM的GC报告有问题，它误认为年轻代是完全填满的）
  	Tenured：清理老年代空间的垃圾收集器名称, 收集算法为mark-sweep-compact
  	Metaspace：此次Metaspace空间中没有收集到垃圾

  	2021-08-13T20:41:02.154-0800: [Full GC (Allocation Failure) 2021-08-13T20:41:02.154-0800: [Tenured: 174759K->174708K(174784K), 0.0303033 secs] 253412K->196059K(253440K), [Metaspace: 2562K->2562K(1056768K)], 0.0303490 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
  	-- 解释
  	这条日志是一次FullGC，针对年轻代、老年代、元空间（jdk8及以上，jdk8以下为perm gen）的全局范围GC。

  疑问：
  三条日志里后两条是不是都是FullGC？标记FullGC的那条为什么只有Tenured没有DefNew？
  以上这样理解MinorGC和MajorGC、FullGC是否正确？

- 并行GC

  概念：

  启动参数：

  过程：

  其他：



- CMS

  概念：

  启动参数：

  过程：

  其他：



- G1

  概念：

  启动参数：

  过程：

  其他：

