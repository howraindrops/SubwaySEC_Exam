package testcase;

import huawei.exam.CommandEnum;
import huawei.exam.OpResult;
import huawei.model.Command;
import huawei.model.OperationResult;
import org.junit.Assert;
import org.junit.Test;

import huawei.SubwaySECControlCenter;

/**
 * 自动化测试类，考生可以实现此类，进行测试。
 * @author
 * @version 1.0
 */
public class TestSubwayManager extends SubwaySECControlCenter
{
    /**
     * 线路查询测试用例
     */
    @Test
    public void testQuerySubways()
    {
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_QUERY_SITES);
        OperationResult responseBody = impl.execute(command);
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_QUERY_SITES, responseBody).toString(),
            "0号线:S0<->S1<->S2<->S3<->S4<->S5<->S6<->S7<->S8  " +
                "1号线:S10<->S11<->S12<->S5<->S14<->S15<->S16<->S17<->S18  " +
                "2号线:S20<->S21<->S22<->S23<->S15<->S25<->S26<->S27<->S28  " +
                "3号线:S30<->S31<->S32<->S2<->S22<->S35<->S36<->S37<->S38");
    }
}
