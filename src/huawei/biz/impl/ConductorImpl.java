package huawei.biz.impl;

import huawei.biz.CardManager;
import huawei.biz.Conductor;
import huawei.exam.CardEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;

/**
 * <p>Title: 待考生实现类</p>
 *
 * <p>Description: 售票员</p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author 王雨芬
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class ConductorImpl implements Conductor
{
    private CardManager cardManager;

    public ConductorImpl(CardManager cardManager)
    {
        this.cardManager = cardManager;
    }

    @Override
    public Card buyCard(String enterStation, String exitStation)
        throws SubwayException
    {
        return cardManager.buyCard(enterStation, exitStation);
    }

    @Override
    public Card buyCard(CardEnum cardEnum, int money)
        throws SubwayException
    {
        return cardManager.buyCard(cardEnum, money);
    }

    @Override
    public Card recharge(String cardId, int money)
        throws SubwayException
    {
        return cardManager.recharge(cardId, money);
    }

    @Override
    public Card deleteCard(String cardId)
        throws SubwayException
    {
        return cardManager.deleteCard(cardId);
    }
}