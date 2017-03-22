        package huawei.biz.impl;

import huawei.biz.Administrator;
import huawei.biz.SubwayManager;

/**
 * <p>Title: 系统管理员 - 已实现，考生无需关注</p>
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
public class AdministratorImpl implements Administrator
{
    private SubwayManager subwayManager;

    public AdministratorImpl(SubwayManager subwayManager)
    {
        this.subwayManager = subwayManager;
    }

    @Override
    public void manage()
    {
        subwayManager.manageSubways();
    }
}
