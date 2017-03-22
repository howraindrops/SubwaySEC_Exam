package huawei.biz.impl;

import huawei.biz.CardManager;
import huawei.biz.Conductor;
import huawei.biz.Passenger;
import huawei.biz.SubwayManager;
import huawei.exam.CardEnum;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;
import huawei.model.Subways;
import huawei.model.Subways.DistanceInfo;

import java.util.List;

import com.google.common.collect.Table;

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
    	//判断地铁站是否合法
    	Subways subways = subwayManager.querySubways();
    	Table<String, String, DistanceInfo> table = subways.getStationDistances();
    	boolean isStationValid = table.containsRow(enterStation)&&table.containsRow(exitStation);
    	if(!isStationValid) throw new SubwayException(ReturnCodeEnum.E07,null);
    	
    	int price = calculateBasicPrice(enterStation, exitStation, table);
    	
    	Card card = conductor.buyCard(enterStation, exitStation);
    	card = recharge(card.getCardId(), price);
        return card;
    }
    
    private int calculateBasicPrice(String enterStation, String exitStation, Table<String, String, DistanceInfo> table)
    	throws SubwayException
    {
    	//TODO Dijkstra算法求最短路径,路径非法时抛exception
    	if(!table.contains(enterStation, exitStation))
    	{
    		throw new SubwayException(ReturnCodeEnum.E01,null);
    	}
    	
    	int path = table.get(enterStation, exitStation).getDistance();
    	int price = 0;
    	if(path<=3000 && path>0)
    	{
    		price = 2;
    	}else if(path<=5000)
    	{
    		price = 3;
    	}else if(path<=10000)
    	{
    		price = 4;
    	}else
    	{
    		price = 5;
    	}
    	return price;
    }

    @Override
    public Card buyCard(CardEnum cardEnum, int money)
        throws SubwayException
    {
    	return conductor.buyCard(cardEnum, money);
    }

    @Override
    public Card recharge(String cardId, int money)
        throws SubwayException
    {
        return conductor.recharge(cardId, money);
    }

    @Override
    public Card queryCard(String cardId) throws SubwayException
    {
        return cardManager.queryCard(cardId);
    }

    @Override
    public Card deleteCard(String cardId)
        throws SubwayException
    {
        return conductor.deleteCard(cardId);
    }

    @Override
    public Card takeSubway(String cardId, String enterStation, String enterTime, String exitStation, String exitTime)
        throws SubwayException
    {
        return subwayManager.takeSubway(cardId, enterStation, enterTime, exitStation, exitTime);
    }

    @Override
    public List<ConsumeRecord> queryConsumeRecord(String cardId)
        throws SubwayException
    {
        return cardManager.queryConsumeRecord(cardId);
    }
}