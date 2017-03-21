package huawei.biz.impl;

import huawei.biz.CardManager;
import huawei.biz.Conductor;
import huawei.biz.Passenger;
import huawei.biz.SubwayManager;
import huawei.exam.CardEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;
import java.util.List;

/**
 * <p>Title: 待考生实现类</p>
 *
 * <p>Description: 乘客</p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class PassengerImpl implements Passenger
{
    private Conductor conductor;
    private CardManager cardManager;
    private SubwayManager subwayManager;

    public PassengerImpl(Conductor conductor, CardManager cardManager, SubwayManager subwayManager)
    {
        this.conductor = conductor;
        this.cardManager = cardManager;
        this.subwayManager = subwayManager;
    }

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
    public Card takeSubway(String cardId, String enterStation, String enterTime, String exitStation, String exitTime)
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