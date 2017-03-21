package huawei.biz;

import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.Subways;

/**
 * <p>Title: 车乘中心</p>
 *
 * <p>Description: 考生不得修改，亦无须关注此文件内容</p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public interface SubwayManager
{
    /**
     * 线路初始化
     */
    void manageSubways();

    /**
     * Query subways.
     *
     * @return the list
     */
    Subways querySubways();

    /**
     * 乘车
     *
     * @param cardId the card id
     * @param enterStation the enter station
     * @param enterTime the enter time
     * @param exitStation the exit station
     * @param exitTime the exit time
     * @return 卡当前状态
     * @throws SubwayException the subway exception
     */
    Card takeSubway(String cardId, String enterStation, String enterTime, String exitStation, String exitTime)
        throws SubwayException;
}