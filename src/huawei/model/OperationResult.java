package huawei.model;

import huawei.exam.ReturnCodeEnum;
import java.util.List;

/**
 * <p>Title: 命令执行结果</p>
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
public class OperationResult extends ConsumeRecord
{
    /**
     * 操作结果
     */
    private ReturnCodeEnum returnCodeEnum;

    /**
     * 线路详情
     */
    private Subways subways;

    /**
     * 命令
     */
    private String command;

    /**
     * 卡
     */
    private Card card;

    /**
     * 消费记录
     */
    private List<ConsumeRecord> consumeRecordList;

    public OperationResult()
    {
        this.setReturnCodeEnum(ReturnCodeEnum.S01);
    }

    public List<ConsumeRecord> getConsumeRecordList()
    {
        return consumeRecordList;
    }

    public void setConsumeRecordList(List<ConsumeRecord> consumeRecordList)
    {
        this.consumeRecordList = consumeRecordList;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public Subways getSubways()
    {
        return subways;
    }

    public void setSubways(Subways subways)
    {
        this.subways = subways;
    }

    public ReturnCodeEnum getReturnCodeEnum()
    {
        return returnCodeEnum;
    }

    public void setReturnCodeEnum(ReturnCodeEnum returnCodeEnum)
    {
        this.returnCodeEnum = returnCodeEnum;
    }

    public Card getCard()
    {
        return card;
    }

    public void setCard(Card card)
    {
        this.card = card;
    }

    public void copyFromCommand(Command command)
    {
        if (!(command.getCardId() == null|| "".equals(command.getCardId())))
        {
            Card card = new Card();
            card.setCardId(command.getCardId());
            card.setCardType(command.getCardType());
            card.setMoney(command.getMoney());
            this.setCard(card);
        }
        this.setEnterStation(command.getEnterStation());
        this.setEnterTime(command.getEnterTime());
        this.setExitStation(command.getExitStation());
        this.setExitTime(command.getExitTime());
    }
}