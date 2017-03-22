package huawei.biz.impl;

import huawei.biz.CardManager;
import huawei.biz.SubwayManager;
import huawei.exam.CardEnum;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;

import java.util.ArrayList;
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
	private Card[] cardArray = new Card[100];
	private List<ConsumeRecord>[] consumeRecords = new ArrayList[100];
	
    @Override
    public Card buyCard(String enterStation, String exitStation)
        throws SubwayException
    {
    	Card card = new Card();
    	card.setCardType(CardEnum.A);
    	String cardId = getNextCardId();
    	card.setCardId(cardId);
    	card.setMoney(0);
    	cardArray[Integer.valueOf(cardId)] = card;
        return card;
    }

    @Override
    public Card buyCard(CardEnum cardEnum, int money)
        throws SubwayException
    {
    	Card card = new Card();
    	card.setCardType(cardEnum);
    	card.setMoney(money);
    	String cardId = getNextCardId();
    	card.setCardId(cardId);
    	cardArray[Integer.valueOf(cardId)] = card;
    	
        return card;
    }

    @Override
    public Card recharge(String cardId, int money)
        throws SubwayException
    {
    	int id = Integer.valueOf(cardId);
    	isCardIdValid(id);
    	Card card = cardArray[id];
    	card.setMoney(money+card.getMoney());
        return card;
    }

    @Override
    public Card queryCard(String cardId) throws SubwayException
    {
    	int id = Integer.valueOf(cardId);
    	isCardIdValid(id);
    	Card card = cardArray[id];
        return card;
    }

    @Override
    public Card deleteCard(String cardId)
        throws SubwayException
    {
    	int id = Integer.valueOf(cardId);
    	isCardIdValid(id);
    	Card card = cardArray[id];
    	cardArray[id] = null;
        return card;
    }

    @Override
    public Card consume(String cardId, int billing)
        throws SubwayException
    {
    	int id = Integer.valueOf(cardId);
    	isCardIdValid(id);
    	Card card = cardArray[id];
    	int money = card.getMoney();
    	if(billing>money)
    	{
    		throw new SubwayException(ReturnCodeEnum.E02,card);
    	}
    	card.setMoney(money-billing);
    	if(card.getMoney()<20)
    	{
    		throw new SubwayException(ReturnCodeEnum.E03,card);
    	}
    	
        return card;
    }

    @Override
    public List<ConsumeRecord> queryConsumeRecord(String cardId)
        throws SubwayException
    {
    	int id = Integer.valueOf(cardId);
    	isCardIdValid(id);
        return consumeRecords[id];
    }
    
    private String getNextCardId() 
    	throws SubwayException
    {
    	boolean findValidId = false;
    	String cardId = null;
    	for(int i=0; i<100; i++)
    	{
    		if(cardArray[i]==null)
    		{
    			cardId = String.valueOf(i);
    			findValidId = true;
    			break;
    		}
    	}
    	
    	if(!findValidId)
    	{
    		throw new SubwayException(ReturnCodeEnum.E08,null);
    	}
    	return cardId;
    }
    
    private boolean isCardIdValid(int id)
    	throws SubwayException
	{
    	if(cardArray[id] == null)
    	{
    		throw new SubwayException(ReturnCodeEnum.E06,null);
    	}
    	return true;
	}
}