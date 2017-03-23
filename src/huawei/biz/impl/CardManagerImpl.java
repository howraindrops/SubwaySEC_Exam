package huawei.biz.impl;

import huawei.biz.CardManager;
import huawei.exam.CardEnum;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<String, Card> cardMap = new HashMap<String, Card>();
	private Map<String,List<ConsumeRecord>> consumeRecords = new HashMap<String,List<ConsumeRecord>>();
	private final int MAX_CARD_NUMBER = 100;
	
    @Override
    public Card buyCard(String enterStation, String exitStation)
        throws SubwayException
    {
    	Card card = new Card();
    	card.setCardType(CardEnum.A);
    	String cardId = getNextCardId();
    	card.setCardId(cardId);
    	card.setMoney(0);
    	cardMap.put(cardId, card);
        return card;
    }

    @Override
    public Card buyCard(CardEnum cardEnum, int money)
        throws SubwayException
    {	
    	if(cardEnum!=CardEnum.A&&cardEnum!=CardEnum.B&&cardEnum!=CardEnum.C)
    	{
    		throw new SubwayException(ReturnCodeEnum.E04, new Card());
    	}
    	Card card = new Card();
    	card.setCardType(cardEnum);
    	card.setMoney(money);
    	String cardId = getNextCardId();
    	card.setCardId(cardId);
    	cardMap.put(cardId, card);
        return card;
    }

    @Override
    public Card recharge(String cardId, int money)
        throws SubwayException
    {
    	Card card = queryCard(cardId);
    	card.setMoney(money+card.getMoney());
    	return card;
    }

    @Override
    public Card queryCard(String cardId) 
    	throws SubwayException
    {
    	Card card = new Card();
    	if(cardMap.containsKey(cardId))
    	{
    		card = cardMap.get(cardId);
    	}else
    	{
    		card = new Card();
    		card.setCardId(cardId);
    		card.setCardType(CardEnum.E);
    		throw new SubwayException(ReturnCodeEnum.E06, card);
    	}
    	return card;
    }

    @Override
    public Card deleteCard(String cardId)
        throws SubwayException
    {
    	Card card = queryCard(cardId);
    	cardMap.remove(cardId);
    	if(consumeRecords.containsKey(cardId))
    	{
    		consumeRecords.remove(cardId);
    	}
		return card;
    }

    @Override
    public Card consume(String cardId, int billing)
        throws SubwayException
    {
    	Card card = queryCard(cardId);
    	
    	int money = card.getMoney();
    	if(billing>money)
    	{
    		throw new SubwayException(ReturnCodeEnum.E02,card);
    	}
    	card.setMoney(money-billing);
    	if(card.getMoney()<20 && card.getCardType()!=CardEnum.A)
    	{
    		throw new SubwayException(ReturnCodeEnum.E03,card);
    	}
		return card;
    }

    @Override
    public List<ConsumeRecord> queryConsumeRecord(String cardId)
        throws SubwayException
    {
    	queryCard(cardId);
    	List<ConsumeRecord> crList;
    	if(consumeRecords.containsKey(cardId))
    	{
    		crList = consumeRecords.get(cardId);
    	}else
    	{
    		crList = new ArrayList<ConsumeRecord>();
    		consumeRecords.put(cardId, crList);
    	}
        return crList;
    }
    
    private String getNextCardId() 
    	throws SubwayException
    {
    	if(cardMap.size()>=MAX_CARD_NUMBER)
    	{
    		throw new SubwayException(ReturnCodeEnum.E08,new Card());
    	}
    	
    	String cardId = null;
    	for(int i=0; i<MAX_CARD_NUMBER; i++)
    	{
    		String id = String.valueOf(i);
    		if(!cardMap.containsKey(id))
    		{
    			cardId = id;
    			break;
    		}
    	}
    	
    	return cardId;
    }
}