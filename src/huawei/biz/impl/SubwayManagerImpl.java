package huawei.biz.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import huawei.biz.CardManager;
import huawei.biz.SubwayManager;
import huawei.exam.CardEnum;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;
import huawei.model.Subways;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title: 待考生实现类takeSubway乘车函数，其它已实现功能函数不用关注</p>
 *
 * <p>Description: 车乘中心</p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class SubwayManagerImpl implements SubwayManager
{
    private static final String SUBWAY_NAME = "subwayName";
    private static final String FIRST_STATION = "firstStation";
    private static final String LAST_STATION = "lastStation";

    private Subways subways = new Subways();
    private CardManager cardManager;
    
    public SubwayManagerImpl(CardManager cardManager)
    {
        this.cardManager = cardManager;
    }

    /**
     * 乘车  -- 待考生实现
     *
     * @param cardId the card id
     * @param enterStation the enter station
     * @param enterTime the enter time
     * @param exitStation the exit station
     * @param exitTime the exit time
     * @return 卡当前状态
     * @throws SubwayException the subway exception
     */
    @Override
    public Card takeSubway(String cardId, String enterStation, String enterTime, String exitStation, String exitTime)
        throws SubwayException
    {
    	Card card = cardManager.queryCard(cardId);
//    	Table<String, String, DistanceInfo> table = subways.getStationDistances();
//    	boolean isStationValid = table.containsRow(enterStation)&&table.containsRow(exitStation);
//    	if(!isStationValid) 
//    	{
//    		throw new SubwayException(ReturnCodeEnum.E07,null);
//    	}
    	
    	//计算票价
    	int price;
    	int time = getTimePeriod(enterTime,exitTime,card);
    	if(enterStation.equals(exitStation)&&time>30)
    	{
    		price = 3;
    	}else
    	{
    		//计算基本票价
    		price = calculateBasicPrice(enterStation,exitStation,subways);
    	}
    
    	if(card.getCardType() == CardEnum.A)
    	{
    		price = price>card.getMoney()?price:card.getMoney();
    	}else if(card.getCardType() == CardEnum.B && isDiscountValid(enterTime))
    	{
    		price = (int)Math.floor(price*0.8);
    	}
    	//扣费并保存消费记录，回收单程卡
    	cardManager.consume(cardId, price);
    	if(card.getCardType() == CardEnum.A)
    	{
    		cardManager.deleteCard(cardId);
    	}else
    	{
    		ConsumeRecord cr = new ConsumeRecord();
	    	cr.setEnterStation(enterStation);
	    	cr.setEnterTime(enterTime);
	    	cr.setExitStation(exitStation);
	    	cr.setExitTime(exitTime);
	    	cr.setConsumeMoney(price);
	    	List<ConsumeRecord> crList = cardManager.queryConsumeRecord(cardId);
	    	crList.add(cr);
    	}
        return card;
    }
    
    @Override
    public void manageSubways()
    {
        List<Map<String, String>> subwayMap = new ArrayList<Map<String, String>>();
        Map<String, String> oneSubway = Maps.newHashMap();
        oneSubway.put(FIRST_STATION, "S0");
        oneSubway.put(LAST_STATION, "S8");
        oneSubway.put(SUBWAY_NAME, "0");
        subwayMap.add(oneSubway);

        Map<String, String> secondSubway = Maps.newHashMap();
        secondSubway.put(FIRST_STATION, "S10");
        secondSubway.put(LAST_STATION, "S18");
        secondSubway.put(SUBWAY_NAME, "1");
        subwayMap.add(secondSubway);

        Map<String, String> thirdSubway = Maps.newHashMap();
        thirdSubway.put(FIRST_STATION, "S20");
        thirdSubway.put(LAST_STATION, "S28");
        thirdSubway.put(SUBWAY_NAME, "2");
        subwayMap.add(thirdSubway);

        Map<String, String> forthSubway = Maps.newHashMap();
        forthSubway.put(FIRST_STATION, "S30");
        forthSubway.put(LAST_STATION, "S38");
        forthSubway.put(SUBWAY_NAME, "3");
        subwayMap.add(forthSubway);

        subways.setSubwayInfo(subwayMap);
        subways.setStationDistances(initStationsTable());
    }

    @Override
    public Subways querySubways()
    {
        return subways;
    }

    private Table<String, String, Subways.DistanceInfo> initStationsTable()
    {
        Table<String, String, Subways.DistanceInfo> distanceTable = HashBasedTable.create();
        // 0号线
        distanceTable.put("S0", "S1", new Subways.DistanceInfo("0", 800));
        distanceTable.put("S1", "S2", new Subways.DistanceInfo("0", 2168));
        distanceTable.put("S2", "S3", new Subways.DistanceInfo("0", 2098));
        distanceTable.put("S3", "S4", new Subways.DistanceInfo("0", 2165));
        distanceTable.put("S4", "S5", new Subways.DistanceInfo("0", 1500));
        distanceTable.put("S5", "S6", new Subways.DistanceInfo("0", 1345));
        distanceTable.put("S6", "S7", new Subways.DistanceInfo("0", 1567));
        distanceTable.put("S7", "S8", new Subways.DistanceInfo("0", 1897));

        distanceTable.put("S1", "S0", new Subways.DistanceInfo("0", 800));
        distanceTable.put("S2", "S1", new Subways.DistanceInfo("0", 2168));
        distanceTable.put("S3", "S2", new Subways.DistanceInfo("0", 2098));
        distanceTable.put("S4", "S3", new Subways.DistanceInfo("0", 2165));
        distanceTable.put("S5", "S4", new Subways.DistanceInfo("0", 1500));
        distanceTable.put("S6", "S5", new Subways.DistanceInfo("0", 1345));
        distanceTable.put("S7", "S6", new Subways.DistanceInfo("0", 1567));
        distanceTable.put("S8", "S7", new Subways.DistanceInfo("0", 1897));

        // 1号线
        distanceTable.put("S10", "S11", new Subways.DistanceInfo("1", 900));
        distanceTable.put("S11", "S12", new Subways.DistanceInfo("1", 1168));
        distanceTable.put("S12", "S5", new Subways.DistanceInfo("1", 2198));
        distanceTable.put("S5", "S14", new Subways.DistanceInfo("1", 2000));
        distanceTable.put("S14", "S15", new Subways.DistanceInfo("1", 1600));
        distanceTable.put("S15", "S16", new Subways.DistanceInfo("1", 1485));
        distanceTable.put("S16", "S17", new Subways.DistanceInfo("1", 1600));
        distanceTable.put("S17", "S18", new Subways.DistanceInfo("1", 1888));

        distanceTable.put("S11", "S10", new Subways.DistanceInfo("1", 900));
        distanceTable.put("S12", "S11", new Subways.DistanceInfo("1", 1168));
        distanceTable.put("S5", "S12", new Subways.DistanceInfo("1", 2198));
        distanceTable.put("S14", "S5", new Subways.DistanceInfo("1", 2000));
        distanceTable.put("S15", "S14", new Subways.DistanceInfo("1", 1600));
        distanceTable.put("S16", "S15", new Subways.DistanceInfo("1", 1485));
        distanceTable.put("S17", "S16", new Subways.DistanceInfo("1", 1600));
        distanceTable.put("S18", "S17", new Subways.DistanceInfo("1", 1888));

        // 2号线
        distanceTable.put("S20", "S21", new Subways.DistanceInfo("2", 1100));
        distanceTable.put("S21", "S22", new Subways.DistanceInfo("2", 1238));
        distanceTable.put("S22", "S23", new Subways.DistanceInfo("2", 1998));
        distanceTable.put("S23", "S15", new Subways.DistanceInfo("2", 1800));
        distanceTable.put("S15", "S25", new Subways.DistanceInfo("2", 1700));
        distanceTable.put("S25", "S26", new Subways.DistanceInfo("2", 1585));
        distanceTable.put("S26", "S27", new Subways.DistanceInfo("2", 1405));
        distanceTable.put("S27", "S28", new Subways.DistanceInfo("2", 1822));

        distanceTable.put("S21", "S20", new Subways.DistanceInfo("2", 1100));
        distanceTable.put("S22", "S21", new Subways.DistanceInfo("2", 1238));
        distanceTable.put("S23", "S22", new Subways.DistanceInfo("2", 1998));
        distanceTable.put("S15", "S23", new Subways.DistanceInfo("2", 1800));
        distanceTable.put("S25", "S15", new Subways.DistanceInfo("2", 1700));
        distanceTable.put("S26", "S25", new Subways.DistanceInfo("2", 1585));
        distanceTable.put("S27", "S26", new Subways.DistanceInfo("2", 1405));
        distanceTable.put("S28", "S27", new Subways.DistanceInfo("2", 1822));

        // 3号线
        distanceTable.put("S30", "S31", new Subways.DistanceInfo("3", 1110));
        distanceTable.put("S31", "S32", new Subways.DistanceInfo("3", 1338));
        distanceTable.put("S32", "S2", new Subways.DistanceInfo("3", 1568));
        distanceTable.put("S2", "S22", new Subways.DistanceInfo("3", 1450));
        distanceTable.put("S22", "S35", new Subways.DistanceInfo("3", 1680));
        distanceTable.put("S35", "S36", new Subways.DistanceInfo("3", 1345));
        distanceTable.put("S36", "S37", new Subways.DistanceInfo("3", 1555));
        distanceTable.put("S37", "S38", new Subways.DistanceInfo("3", 1682));

        distanceTable.put("S31", "S30", new Subways.DistanceInfo("3", 1110));
        distanceTable.put("S32", "S31", new Subways.DistanceInfo("3", 1338));
        distanceTable.put("S2", "S32", new Subways.DistanceInfo("3", 1568));
        distanceTable.put("S22", "S2", new Subways.DistanceInfo("3", 1450));
        distanceTable.put("S35", "S22", new Subways.DistanceInfo("3", 1680));
        distanceTable.put("S36", "S35", new Subways.DistanceInfo("3", 1345));
        distanceTable.put("S37", "S36", new Subways.DistanceInfo("3", 1555));
        distanceTable.put("S38", "S37", new Subways.DistanceInfo("3", 1682));
        return distanceTable;
    }
    
    private int getTimePeriod(String enterTime, String exitTime, Card card)
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
    
    private boolean isDiscountValid(String enterTime)
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
    
    
    public static int calculateBasicPrice(String enterStation, String exitStation, Subways subways)
        	throws SubwayException
    {
    	if(enterStation.equals(exitStation))
    	{
    		return 0;
    	}

    	//Dijkstra算法求最短路径并计算出价钱
    	int path = dijkstraMiniDistance_quick(subways, enterStation, exitStation);
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
     * Dijkstra算法求取两站点之间的最短距离
     * @param subways
     * @param enterStation 起始站点
     * @param exitStation 终止站点
     * @return 最短距离；如果输入enterStation和exitStation有误，则会报出异常
     *
     * @author lj95801
     */
    public static int dijkstraMiniDistance_quick(Subways subways,String enterStation, String exitStation)
    	throws SubwayException
    {
        class Node {
           Node(String name, int dist){
                this.name = name;
                this.dist = dist;
            }
            private String name;
            private int dist;

            @Override
            public String toString() {
                return "Node{" +
                        "name='" + name + '\'' +
                        ", dist=" + dist +
                        '}';
            }
        }

        //依据命令中站点是否不要区分大小写
        String enterSta = enterStation.toUpperCase();
        String exitSta = exitStation.toUpperCase();

        if(subways == null || subways.getStationDistances().size() <= 0){
            //无效的线路
            throw new SubwayException(ReturnCodeEnum.E01, null);
        }

        //所有地铁站点名称集合
        Set<String> V = subways.getStationDistances().columnKeySet();
        if(!V.contains(enterSta) || !V.contains(exitSta)){
            //无效的地铁站
            throw new SubwayException(ReturnCodeEnum.E07, null);
        }

        Table<String, String, Subways.DistanceInfo> table = subways.getStationDistances();
        LinkedList<Node> list = new LinkedList<>();
        for (String s : V) {
            Node nd = new Node(s, Integer.MAX_VALUE);
            if(nd.name.equals(enterSta)){
                nd.dist = 0;
            }
            list.add(nd);
        }

        //逻辑上能够保证函数出口时不为null，此处赋一个无效值是为了通过findbugs测试
        Node exitNode = new Node("#", -1);
        int total = V.size();
        for (int i = 0; i < total; i++) {
            //lambda表达式jdk8
//            list.sort((o1, o2) -> o1.dist - o2.dist);
            list.sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.dist - o2.dist;
                }
            });
            Node uu = list.poll();
            if(uu.name.equals(exitSta)){
                exitNode = uu;
                //找到出站点，可以提前结束查找
                break;
            }

            Set<String> u = table.column(uu.name).keySet();
            for (Node uNode : list) {
                if(!u.contains(uNode.name)){
                    continue;
                }
                if(uu.dist + table.get(uu.name, uNode.name).getDistance() < uNode.dist){
                    uNode.dist = uu.dist + table.get(uu.name, uNode.name).getDistance();
                }
            }
        }

        return exitNode.dist;
    }
    
    public static int dijkstra(Subways subways,String enterStation, String exitStation)
    	throws SubwayException
    {
    	
    	return 0;
    }
}