package com.pdx.alipay.util;

/**
 * 雪花算法
 * @author eagle
 */
public class SnowflakeIdUtil {

    private final long twepoch = 1520148401625L;
    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << this.workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << this.datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = this.sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = this.sequenceBits + this.workerIdBits;

    /**
     * 时间戳向左移22位(5+5+12)
     */
    private final long timestampLeftShift = this.sequenceBits + this.workerIdBits + this.datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << this.sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private final long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private final long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowflakeIdUtil(final long workerId, final long datacenterId) {
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
        }
        if (datacenterId > this.maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", this.maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public static long getID() {
        final SnowflakeIdUtil idWorker = new SnowflakeIdUtil(0, 0);
        final long id = idWorker.nextId();
        return id;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected static long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 当前时间戳
     */
    protected static long tilNextMillis(final long lastTimestamp) {
        long timestamp = SnowflakeIdUtil.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = SnowflakeIdUtil.timeGen();
        }
        return timestamp;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = SnowflakeIdUtil.timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & this.sequenceMask;
            //毫秒内序列溢出
            if (this.sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = SnowflakeIdUtil.tilNextMillis(this.lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            this.sequence = 0L;
        }

        //上次生成ID的时间戳
        this.lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - this.twepoch) << this.timestampLeftShift) //
                | (this.datacenterId << this.datacenterIdShift) //
                | (this.workerId << this.workerIdShift) //
                | this.sequence;
    }
    
    /**
     * 测试
     */
    public static void main(final String[] args) {
        System.out.println("开始：" + System.currentTimeMillis());
        final SnowflakeIdUtil idWorker = new SnowflakeIdUtil(0, 0);
        final long id = idWorker.nextId();
        System.out.println(id);
    }
}
