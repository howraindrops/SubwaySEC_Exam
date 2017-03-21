package huawei;

import huawei.exam.ExamCommand;

import com.huawei.exam.Command;
import com.huawei.exam.ExamSocketServer;

/**
 * <p>Title: 主执行类</p>
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
public class SubwaySECMain
{
    public static void main(String[] args)
    {
        /*
         * 启动Socket服务侦听5555端口，从Socket获取命令，会丢给Command类的command函数执行
         * Command类的command函数已经实现了从Socket接收到字符串后的解析与分发
         * 考生只需要实现TaxiBookingSECImpl类的各命令接口即可。
         */
        Command cmd = new ExamCommand();
        ExamSocketServer as = new ExamSocketServer(cmd);
        as.start();
    }

    /**
     * 私有构造函数,此类无需实例化
     */
    private SubwaySECMain()
    {
        
    }
}