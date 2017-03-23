package huawei.biz.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Table;

import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.Subways;
import huawei.model.Subways.DistanceInfo;

public class PriceCalculator
{
	private static final double ELDER_DISCOUNT = 0.8;
	private static final String ELDER_DISCOUNT_TIME_FROM = "10:00";
	private static final String ELDER_DISCOUNT_TIME_TO = "15:00";
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
    	long time = getTimePeriod(enterTime,exitTime,card);
    	if(enterStation.equals(exitStation))
    	{
    		//进出站相同时
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
    		switch(card.getCardType())
    		{
    		case A: //单程票
    			price = price>card.getMoney()?price:card.getMoney();
    			break;
    		case B: //老年卡
    			if(isDiscountValid(enterTime))
    			{
    				price = (int)Math.floor(price*ELDER_DISCOUNT);
    			}
    			break;
    		case C: //普通卡
    			break;
    		case D: //学生卡
    			//TODO 计算学生折扣
    			break;
    		default:
    			break;
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
	private static long getTimePeriod(String enterTime, String exitTime, Card card)
		    	throws SubwayException
    {
		SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm" );
		Date enTime, exTime;
		try
		{
			enTime = sdf.parse(enterTime);
			exTime = sdf.parse(exitTime);
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new SubwayException(ReturnCodeEnum.E05, card);
		}
		if(enTime.after(exTime))
		{
			throw new SubwayException(ReturnCodeEnum.E05, card);
		}
		
    	long time = (exTime.getTime()-enTime.getTime())/1000*60;
    	return time;
    }
    
	/**
	 * 判断是否符合老年卡折扣时间段
	 * @param enterTime
	 * @return
	 * @throws SubwayException 
	 */
    private static boolean isDiscountValid(String enterTime) 
    	throws SubwayException
    {
    	SimpleDateFormat sdf =   new SimpleDateFormat( "HH:mm" );
		Date enTime, fromTime, toTime;
		try
		{
			enTime = sdf.parse(enterTime);
			fromTime = sdf.parse(ELDER_DISCOUNT_TIME_FROM);
			toTime = sdf.parse(ELDER_DISCOUNT_TIME_TO);
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new SubwayException(ReturnCodeEnum.E05, new Card());
		}
    	if(!enTime.after(toTime) && !enTime.before(fromTime))
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
    	if(table.contains(enterStation, exitStation))
    	{
    		return table.get(enterStation, exitStation).getDistance();
    	}
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
//    			System.out.println("path="+minPath);
    			return minPath;
    		}
    		//U中没有与enterStation相连的路径了,且还没找到exitStation
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
