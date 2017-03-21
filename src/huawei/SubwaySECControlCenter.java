package huawei;

import huawei.biz.Administrator;
import huawei.biz.CardManager;
import huawei.biz.Conductor;
import huawei.biz.Passenger;
import huawei.biz.impl.AdministratorImpl;
import huawei.biz.SubwayManager;
import huawei.biz.impl.CardManagerImpl;
import huawei.biz.impl.ConductorImpl;
import huawei.biz.impl.PassengerImpl;
import huawei.biz.impl.SubwayManagerImpl;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.Command;
import huawei.model.ConsumeRecord;
import huawei.model.OperationResult;

import java.util.List;

/**
 * <p>Title: 控制中心，负责将请求命令分发到向对象。 功能已实现，考生无需关注</p>
 *
 * <p>Description: 各方法按要求返回，程序库会组装报文输出</p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class SubwaySECControlCenter
{
    private CardManager cardManager = new CardManagerImpl();
    private Conductor conductor = new ConductorImpl(cardManager);
    private SubwayManager subwayManager = new SubwayManagerImpl(cardManager);
    private Administrator administrator = new AdministratorImpl(subwayManager);
    private Passenger passenger = new PassengerImpl(conductor, cardManager, subwayManager);

    /**
     * 必须提供无参数构造函数，考生可在函数体中根据需要增加初始化代码
     * 程序库中会且只会生成一个SubwaySECImpl实例，并在整个进程生命周期中一直使用这个实例
     */
    public SubwaySECControlCenter()
    {
        opReset();
    }

    /**
     * 业务执行入口
     *
     * @param command 入参
     *                     command - 执行命令
     *                     enterSite - 进站名
     *                     exitSite - 出站 名
     *                     cardType - 卡类型
     *                     money - 余额
     *                     cardId - 卡号
     *                     enterTime - 进站时间
     *                     exitTime - 出站时间
     * @return Result 响应消息
     */
    public OperationResult execute(Command command)
    {
        OperationResult responseBody = new OperationResult();
        try
        {
            switch (command.getCommand())
            {
                case CMD_QUERY_SITES:
                {
                    // 地铁线路查询
                    responseBody.setSubways(subwayManager.querySubways());
                    break;
                }
                case CMD_ONCE:
                {
                    /**
                     * 单程票购买
                     * responseBody必须包含操作结果、卡号和余额
                    */
                    Card card = passenger.buyCard(command.getEnterStation(), command.getExitStation());
                    responseBody.setCard(card);
                    break;
                }
                case CMD_BUY:
                {
                    /**
                     * 办理乘车卡
                     * responseBody必须包含操作结果、卡号、卡类型和余额
                     */
                    Card card = passenger.buyCard(command.getCardType(), command.getMoney());
                    responseBody.setCard(card);
                    break;
                }
                case CMD_CONSUME:
                {
                    /**
                     * 乘车扣费
                     * responseBody必须包含操作结果、卡号、卡类型和余额
                     */
                    Card card = passenger.takeSubway(command.getCardId(),
                        command.getEnterStation(),
                        command.getEnterTime(),
                        command.getExitStation(),
                        command.getExitTime());
                    responseBody.copyFromCommand(command);
                    responseBody.setCard(card);
                    break;
                }
                case CMD_RECHARGE:
                {
                    /**
                     * 充值
                     * responseBody必须包含操作结果、卡号、卡类型和余额
                     */
                    Card card = passenger.recharge(command.getCardId(), command.getMoney());
                    responseBody.setCard(card);
                    break;
                }
                case CMD_DELETE:
                {
                    /**
                     * 注销地铁卡
                     * responseBody必须包含操作结果、卡号和卡类型
                     */
                    Card card = passenger.deleteCard(command.getCardId());
                    responseBody.setCard(card);
                    break;
                }
                case CMD_QUERY_RECORD:
                {
                    /**
                     * 查询历史消费记录
                     * responseBody必须包含操作结果、卡号、卡类型和消费记录
                     */
                    Card card = passenger.queryCard(command.getCardId());
                    List<ConsumeRecord> recordList = passenger.queryConsumeRecord(command.getCardId());
                    responseBody.setCard(card);
                    responseBody.setConsumeRecordList(recordList);
                    break;
                }
                default:
                {
                    responseBody.setReturnCodeEnum(ReturnCodeEnum.E09);
                }
            }
        }
        catch (SubwayException e)
        {
            responseBody.setReturnCodeEnum(e.getReturnCodeEnum());
            responseBody.setCard(e.getCard());
        }

        return  responseBody;
    }

    /**
     * 必须提供无参数构造函数，考生可在函数体中根据需要增加初始化代码
     * 程序库中会且只会生成一个TaxiBookingSECImpl实例，并在整个进程生命周期中一直使用这个实例
     */
    public void opReset()
    {
        administrator.manage();
    }
}