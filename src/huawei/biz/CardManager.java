package huawei.biz;

import huawei.exam.CardEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;

import java.util.List;

/**
 * <p>Title: 卡票中心</p>
 *
 * <p>Description: 考生不得修改，亦无须关注此文件内容</p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public interface CardManager
{
    /**
     * 购买临时卡
     *
     * @param enterStation the enter station
     * @param exitStation the exit station
     * @return the card
     * @throws SubwayException the subway exception
     */
    Card buyCard(String enterStation, String exitStation)  throws SubwayException;

    /**
     * 办卡
     *
     * @param cardEnum the card enum
     * @param money the money
     * @return the card
     * @throws SubwayException the subway exception
     */
    Card buyCard(CardEnum cardEnum, int money)  throws SubwayException;

    /**
     * 充值
     *
     * @param cardId the card
     * @param money the money
     * @return the card
     * @throws SubwayException the subway exception
     */
    Card recharge(String cardId, int money) throws SubwayException;

    /**
     * Query card.
     *
     * @param cardId the card id
     * @return the card
     * @throws SubwayException the subway exception
     */
    Card queryCard(String cardId) throws SubwayException;

    /**
     * Delete card.
     *
     * @param cardId the card id
     * @return the card
     * @throws SubwayException the subway exception
     */
    Card deleteCard(String cardId) throws SubwayException;

    /**
     * 乘车扣费
     *
     * @param cardId the card id
     * @param billing the billing
     * @return the card
     * @throws SubwayException the subway exception
     */
    Card consume(String cardId, int billing) throws SubwayException;

    /**
     * 查询消费记录
     *
     * @param cardId the card id
     * @return the list
     * @throws SubwayException the subway exception
     */
    List<ConsumeRecord> queryConsumeRecord(String cardId) throws SubwayException;
}