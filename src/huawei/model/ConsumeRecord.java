package huawei.model;

/**
 * <p>Title: 消费记录</p>
 *
 * <p>Description: 本类供考生调用，不允许更改</p>
 *
 * <p>Copyright: Copyright 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 */
public class ConsumeRecord
{
    /**
     * 进站名
     */
    private String enterStation;

    /**
     * 出站名
     */
    private String exitStation;

    /**
     * 进站时间
     */
    private String enterTime;

    /**
     * 出站时间
     */
    private String exitTime;

    /**
     * 金额
     */
    private int consumeMoney;

    public String getEnterStation()
    {
        return enterStation;
    }

    public void setEnterStation(String enterStation)
    {
        this.enterStation = enterStation;
    }

    public String getExitStation()
    {
        return exitStation;
    }

    public void setExitStation(String exitStation)
    {
        this.exitStation = exitStation;
    }

    public String getEnterTime()
    {
        return enterTime;
    }

    public void setEnterTime(String enterTime)
    {
        this.enterTime = enterTime;
    }

    public String getExitTime()
    {
        return exitTime;
    }

    public void setExitTime(String exitTime)
    {
        this.exitTime = exitTime;
    }

    public int getConsumeMoney()
    {
        return consumeMoney;
    }

    public void setConsumeMoney(int consumeMoney)
    {
        this.consumeMoney = consumeMoney;
    }
}
