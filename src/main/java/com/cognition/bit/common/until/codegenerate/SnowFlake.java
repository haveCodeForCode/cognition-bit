package com.cognition.bit.common.until.codegenerate;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 *
 * @author Worry
 * @version 2019/4/14
 */
public class SnowFlake {

    // 初始时间截 (2017-01-01)
    private static final long INITIAL_TIME_STAMP = 1483200000000L;
    // 机器id所占的位数
    private static final long WORKER_ID_BITS = 5L;
    // 数据标识id所占的位数
    private static final long DATACENTER_ID_BITS = 5L;
    // 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 支持的最大数据标识id，结果是31
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    // 序列在id中占的位数
    private final long SEQUENCE_BITS = 12L;
    // 机器ID的偏移量(12)
    private final long WORKERID_OFFSET = SEQUENCE_BITS;
    // 数据中心ID的偏移量(12+5)
    private final long DATACENTERID_OFFSET = SEQUENCE_BITS + WORKER_ID_BITS ;
    // 时间截的偏移量(5+5+12)
    private final long TIMESTAMP_OFFSET = WORKER_ID_BITS + DATACENTER_ID_BITS + SEQUENCE_BITS;
    // 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
    private final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    // 工作节点ID(0~31)
    private long workerId;
    // 数据中心ID(0~31)
    private long datacenterId;
    // 毫秒内序列(0~4095)
    private long sequence = 0L;
    // 上次生成ID的时间截
    private long lastTimestamp = -1L;



    private static SnowFlake snowFlakes = new SnowFlake(0,0);

    public static long createSFid(){
        return snowFlakes.nextId();
    }

    /**
     * 构造函数
     * @param workerId 工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowFlake(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("WorkerID 不能大于 %d 或小于 0", MAX_WORKER_ID));
        }

        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("DataCenterID 不能大于 %d 或小于 0", MAX_DATACENTER_ID));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获得下一个ID (用同步锁保证线程安全)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("当前时间小于上一次记录的时间戳！");
        }
        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // sequence等于0说明毫秒内序列已经增长到最大值
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {// 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - INITIAL_TIME_STAMP) << TIMESTAMP_OFFSET)  | (datacenterId << DATACENTERID_OFFSET) | (workerId << WORKERID_OFFSET) | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}

