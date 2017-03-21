package huawei.model;

import huawei.exam.CommandEnum;

/**
 * <p>Title: 命令解析结果</p>
 *
 * <p>Description: 本类供考生调用，不允许更改</p>
 *
 * <p>Copyright: Copyright 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class Command extends Card
{
    /**
     * 命令
     */
    private CommandEnum command;

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
     * Gets enter time.
     *
     * @return the enter time
     */
    public String getEnterTime()
    {
        return enterTime;
    }

    /**
     * Sets enter time.
     *
     * @param enterTime the enter time
     */
    public void setEnterTime(String enterTime)
    {
        this.enterTime = enterTime;
    }

    /**
     * Gets exit time.
     *
     * @return the exit time
     */
    public String getExitTime()
    {
        return exitTime;
    }

    /**
     * Sets exit time.
     *
     * @param exitTime the exit time
     */
    public void setExitTime(String exitTime)
    {
        this.exitTime = exitTime;
    }

    /**
     * Gets command.
     *
     * @return the command
     */
    public CommandEnum getCommand()
    {
        return command;
    }

    /**
     * Sets command.
     *
     * @param command the command
     */
    public void setCommand(CommandEnum command)
    {
        this.command = command;
    }

    /**
     * Gets enter site.
     *
     * @return the enter site
     */
    public String getEnterStation()
    {
        return enterStation;
    }

    /**
     * Sets enter site.
     *
     * @param enterStation the enter site
     */
    public void setEnterStation(String enterStation)
    {
        this.enterStation = enterStation;
    }

    /**
     * Gets exit site.
     *
     * @return the exit site
     */
    public String getExitStation()
    {
        return exitStation;
    }

    /**
     * Sets exit site.
     *
     * @param exitStation the exit site
     */
    public void setExitStation(String exitStation)
    {
        this.exitStation = exitStation;
    }
}