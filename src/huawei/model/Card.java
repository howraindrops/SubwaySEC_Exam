package huawei.model;

import huawei.exam.CardEnum;

/**
 * <p>Title: 卡票</p>
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
public class Card
{
    /**
     * 卡类型
     */
    private CardEnum cardType;

    /**
     * 当前余额
     */
    private int money;

    /**
     * 卡号
     */
    private String cardId;

    public CardEnum getCardType()
    {
        return cardType;
    }

    public void setCardType(CardEnum cardType)
    {
        this.cardType = cardType;
    }

    public int getMoney()
    {
        return money;
    }

    public void setMoney(int money)
    {
        this.money = money;
    }

    public String getCardId()
    {
        return cardId;
    }

    public void setCardId(String cardId)
    {
        this.cardId = cardId;
    }
}
