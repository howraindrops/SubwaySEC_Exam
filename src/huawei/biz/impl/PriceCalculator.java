package huawei.biz.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	 * @return 基本票价（元）
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
	 * @return 扣费票价（元）
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
    		try{
    			price = getBasicPrice(enterStation,exitStation,subways);
    		}catch(SubwayException e)
    		{
    			throw new SubwayException(ReturnCodeEnum.E01,card);
    		}
    		
    		//按卡的类型计算折扣等
    		if(card.getCardType() == CardEnum.A)
        	{
        		price = price>card.getMoney()?price:card.getMoney();
        	}else if(card.getCardType() == CardEnum.B && isDiscountValid(enterTime))
        	{
        		price = (int)Math.floor(price*0.8);
        	}
    	}
		return price;
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
	
	/**
	 * 计算进出站时间差，并判断时间合法性抛出异常
	 * @param enterTime
	 * @param exitTime
	 * @param card
	 * @return 进出站时间差（分钟）
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
    
    /**
     * dijkstra方法计算最短路径
     * @param enterStation
     * @param exitStation
     * @param subways
     * @return 最短路径值（米）
     * @throws SubwayException
     */
    private static int dijkstra(String enterStation, String exitStation, Subways subways)
	    	throws SubwayException
	{	
    	Table<String, String, Subways.DistanceInfo> table = subways.getStationDistances();
    	//初始化
    	Set<String> S = new HashSet<String>();
    	S.add(enterStation);
    	Set<String> U = new HashSet<String>();
    	U.addAll(table.rowKeySet());
    	U.remove(enterStation);
    	Map<String, Integer> shortestPath = new HashMap<String, Integer>();
    	int prePath = 0;
    	String preStation = enterStation;
    	
    	while(!U.isEmpty())
    	{
    		int minPath = -1;
    		String minStation = preStation;
    		//更新剩下的顶点到enterStation的最短路径
    		for(String curStation:U)
    		{
    			int cpath = -1;
    			if(table.contains(preStation, curStation))
    			{
    				cpath = table.get(preStation, curStation).getDistance();
    			}
    			int ppath = -1;
    			if(shortestPath.containsKey(curStation))
    			{
    				ppath = shortestPath.get(curStation);
    			}
    			//更新curStation到enterStation的最短路径
    			if(ppath==-1 && cpath!=-1)
    			{
    				shortestPath.put(curStation, cpath+prePath);
    			}else if(ppath!=-1 && cpath!=-1 && cpath+prePath<ppath)
    			{
    				shortestPath.remove(curStation);
    				shortestPath.put(curStation, cpath+prePath);
    			}
    			//更新U中最短顶点，看curStation是否最短
    			if(shortestPath.containsKey(curStation))
    			{
    				int p = shortestPath.get(curStation);
    				if(p<minPath || minPath==-1)
    				{
    					minPath = p;
    					minStation = curStation;
    				}
    			}
    		}
    		
    		//已找到对应exitStation的最短路径，返回数据
    		if(minStation.equals(exitStation))
    		{
    			System.out.println("path="+minPath);
    			return minPath;
    		}
    		//U中没有与enterStation相连的路径了
    		if(minPath == -1)
    		{
    			throw new SubwayException(ReturnCodeEnum.E01,new Card());
    		}
    		prePath = minPath;
    		preStation = minStation;
    		S.add(minStation);
    		U.remove(minStation);
    	}
    	
	    return -1;
	}

}
