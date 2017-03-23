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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    	if(!isStationValid) 
    	{
    		throw new SubwayException(ReturnCodeEnum.E07,new Card());
    	}
    	//寻找最短路径并判断路径是否合法
    	int price = calculateBasicPrice(enterStation, exitStation, subways);
    	
    	Card card = conductor.buyCard(enterStation, exitStation);
    	card = recharge(card.getCardId(), price);
        return card;
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
    
    private int calculateBasicPrice(String enterStation, String exitStation, Subways subways)
        	throws SubwayException
    {
    	//TODO Dijkstra算法求最短路径,路径非法时抛exception
    	if(enterStation.equals(exitStation))
    	{
    		return 0;
    	}
    	
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
    public static int dijkstraMiniDistance_quick(Subways subways,String enterStation, String exitStation)throws SubwayException{
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

}