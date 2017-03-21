package huawei.biz.impl;

import huawei.biz.CardManager;
import huawei.exam.CardEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;
import java.util.List;

/**
 * <p>Title: 待考生实现类</p>
 *
 * <p>Description: 卡票中心</p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class CardManagerImpl implements CardManager
{
    @Override
    public Card buyCard(String enterStation, String exitStation)
        throws SubwayException
    {
        //TODO 待考生实现
        return null;
    }

    @Override
    public Card buyCard(CardEnum cardEnum, int money)
        throws SubwayException
    {
        //TODO 待考生实现
        return null;
    }

    @Override
    public Card recharge(String cardId, int money)
        throws SubwayException
    {
        //TODO 待考生实现
        return null;
    }

    @Override
    public Card queryCard(String cardId) throws SubwayException
    {
        //TODO 待考生实现
        return null;
    }

    @Override
    public Card deleteCard(String cardId)
        throws SubwayException
    {
        //TODO 待考生实现
        return null;
    }

    @Override
    public Card consume(String cardId, int billing)
        throws SubwayException
    {
        //TODO 待考生实现
        return null;
    }

    @Override
    public List<ConsumeRecord> queryConsumeRecord(String cardId)
        throws SubwayException
    {
        //TODO 待考生实现
        return null;
    }
}