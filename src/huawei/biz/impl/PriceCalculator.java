package huawei.biz.impl;

import com.google.common.collect.Table;

import huawei.exam.CardEnum;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.Subways;
import huawei.model.Subways.DistanceInfo;

public class PriceCalculator
{
	/**
	 * 计算基本票价
	 * @param enterStation
	 * @param exitStation
	 * @param subways
	 * @return
	 * @throws SubwayException
	 */
	public static int getBasicPrice(String enterStation, String exitStation, Subways subways)
			throws SubwayException
	{
		checkStationValid(enterStation, exitStation, subways);
		//若进出车站相同，基本票价为0
		if(enterStation.equals(exitStation))
    	{
    		return 0;
    	}
		//Dijkstra算法求最短路径并得到基本票价
    	int path = dijkstra(enterStation, exitStation, subways);
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
	
	/**
	 * 计算扣费票价
	 * @param card
	 * @param enterStation
	 * @param enterTime
	 * @param exitStation
	 * @param exitTime
	 * @param subways
	 * @return
	 * @throws SubwayException
	 */
	public static int getChargePrice(Card card, String enterStation, String enterTime, String exitStation, String exitTime, Subways subways)
			throws SubwayException
	{
		checkStationValid(enterStation, exitStation, subways);
		int price;
    	//计算时间差并判断时间是否合法
    	int time = getTimePeriod(enterTime,exitTime,card);
    	if(enterStation.equals(exitStation))
    	{
    		price = time>30?3:0;
    	}else
    	{
    		//计算基本票价
    		price = getBasicPrice(enterStation,exitStation,subways);
    		//按卡的类型计算折扣等
    		if(card.getCardType() == CardEnum.A)
        	{
        		price = price>card.getMoney()?price:card.getMoney();
        	}else if(card.getCardType() == CardEnum.B && isDiscountValid(enterTime))
        	{
        		price = (int)Math.floor(price*0.8);
        	}
    	}
		
		return 0;
	}
	
	/**
	 * 判断两个地铁站是否合法，若不合法则抛出异常
	 * @param enterStation
	 * @param exitStation
	 * @param subways
	 * @throws SubwayException
	 */
	private static void checkStationValid(String enterStation, String exitStation, Subways subways)
			throws SubwayException
	{
    	Table<String, String, DistanceInfo> table = subways.getStationDistances();
    	boolean isStationValid = table.containsRow(enterStation)&&table.containsRow(exitStation);
    	if(!isStationValid) 
    	{
    		throw new SubwayException(ReturnCodeEnum.E07,null);
    	}
	}
	
	private static int dijkstra(String enterStation, String exitStation, Subways subways)
	    	throws SubwayException
	{	
	    return 0;
	}
	
	/**
	 * 计算进出站时间差，并判断时间合法性抛出异常
	 * @param enterTime
	 * @param exitTime
	 * @param card
	 * @return
	 * @throws SubwayException
	 */
	private static int getTimePeriod(String enterTime, String exitTime, Card card)
		    	throws SubwayException
    {
    	String[] enter = enterTime.split(":");
    	String[] exit = exitTime.split(":");
    	int enHour = Integer.valueOf(enter[0]);
    	int enMin = Integer.valueOf(enter[1]);
    	int exHour = Integer.valueOf(exit[0]);
    	int exMin = Integer.valueOf(exit[0]);
    	boolean isEnterLater = (enHour>exHour)||((enHour==exHour)&&(enMin>exMin));
    	boolean isTimeFormatValid = enHour<24&&exHour<24&&enMin<60&&exMin<60;
    	if(isEnterLater||(!isTimeFormatValid))
    	{
    		throw new SubwayException(ReturnCodeEnum.E05, card);
    	}
    	
    	int time = (exHour*60 + exMin) - (enHour*60 + enMin);
    	return time;
    }
    
	/**
	 * 判断是否符合老年卡折扣时间段
	 * @param enterTime
	 * @return
	 */
    private static boolean isDiscountValid(String enterTime)
    {
    	String[] enter = enterTime.split(":");
    	int enHour = Integer.valueOf(enter[0]);
    	int enMin = Integer.valueOf(enter[1]);
    	if((enHour>=10&&enHour<15)||(enHour==15&&enMin==0))
    	{
    		return true;
    	}
    	return false;
    }
}
