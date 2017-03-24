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
	private Map<String,List<ConsumeRecord>> cardRecords = new HashMap<String,List<ConsumeRecord>>();
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
    	checkMoneyValid(money);
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
    	checkMoneyValid(money);
    	int result = money+card.getMoney();
    	//计算学生卡充值优惠
    	if(card.getCardType() == CardEnum.D && money>50)
    	{
    		money -= 50;
	    	while(money > 0)
	    	{
	    		result += 10;
	    		money -= 50;
	    	}
    	}
    	checkMoneyValid(result);
    	card.setMoney(result);
    	return card;
    }

    @Override
    public Card queryCard(String cardId) 
    	throws SubwayException
    {
    	if(cardMap.containsKey(cardId))
    	{
    		return cardMap.get(cardId);
    	}else
    	{
    		Card card = new Card();
    		card.setCardId(cardId);
    		card.setCardType(CardEnum.E);
    		throw new SubwayException(ReturnCodeEnum.E06, card);
    	}
    }

    @Override
    public Card deleteCard(String cardId)
        throws SubwayException
    {
    	Card card = queryCard(cardId);
    	cardMap.remove(cardId);
    	if(cardRecords.containsKey(cardId))
    	{
    		cardRecords.remove(cardId);
    	}
		return card;
    }

    @Override
    public Card consume(String cardId, int billing)
        throws SubwayException
    {
    	Card card = queryCard(cardId);
    	int money = card.getMoney();
    	//销毁单程卡
    	if(card.getCardType() == CardEnum.A)
		{
			deleteCard(cardId);
		}
    	if(billing>money)
    	{
    		throw new SubwayException(ReturnCodeEnum.E02,card);
    	}else
    	{
    		card.setMoney(money-billing);
    	}
		return card;
    }

    @Override
    public List<ConsumeRecord> queryConsumeRecord(String cardId)
        throws SubwayException
    {
    	Card card = queryCard(cardId);
    	//单程卡不能查询消费记录
    	if(card.getCardType() == CardEnum.A)
    	{
    		throw new SubwayException(ReturnCodeEnum.E00, card);
    	}
    	List<ConsumeRecord> crList;
    	if(cardRecords.containsKey(cardId))
    	{
    		crList = cardRecords.get(cardId);
    	}else
    	{
    		crList = new ArrayList<ConsumeRecord>();
    		cardRecords.put(cardId, crList);
    	}
        return crList;
    }
    
    /**
     * 获取一个合法cardId，若card数量超限会抛异常
     * @return
     * @throws SubwayException
     */
    private String getNextCardId() 
    	throws SubwayException
    {
    	if(cardMap.size()>=MAX_CARD_NUMBER)
    	{
    		throw new SubwayException(ReturnCodeEnum.E08,new Card());
    	}
    	
    	for(int i=0; i<MAX_CARD_NUMBER; i++)
    	{
    		String id = String.valueOf(i);
    		if(!cardMap.containsKey(id))
    		{
    			return id;
    		}
    	}
    	return null;
    }
   
    private void checkMoneyValid(int money)
    	throws SubwayException
    {
    	if(money<0 || money>999)
    	{
    		throw new SubwayException(ReturnCodeEnum.E00,new Card());
    	}
    }
}