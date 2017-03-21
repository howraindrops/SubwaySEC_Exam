package huawei.model;

import com.google.common.collect.Table;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: 地铁线路</p>
 *
 * <p>Description: 本类供考生调用，不允许更改</p>
 *
 * <p>Copyright: Copyright 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class Subways
{
    /**
     * 地铁线路基础信息
     * key - subwayName|firstStation|lastStation
     * value - 地铁线路|起始站|终点站
     */
    private List<Map<String, String>> subwayInfo;

    /**
     * Table是GUAVA实现的 ROW COLUMN VALUE 类型数据结构
     *
     * 表示地铁线路详情为  上一站  下一站  (线路名 | 距离)
     */
    private Table<String, String, DistanceInfo> stationDistances;

    public Table<String, String, DistanceInfo> getStationDistances()
    {
        return stationDistances;
    }

    public void setStationDistances(Table<String, String, DistanceInfo> stationDistances)
    {
        this.stationDistances = stationDistances;
    }

    public List<Map<String, String>> getSubwayInfo()
    {
        return subwayInfo;
    }

    public void setSubwayInfo(List<Map<String, String>> subwayInfo)
    {
        this.subwayInfo = subwayInfo;
    }

    public static class DistanceInfo
    {
        private String subwayName;
        private int distance;

        public DistanceInfo(String subway, int distance)
        {
            this.subwayName = subway;
            this.distance = distance;
        }

        public String getSubwayName()
        {
            return subwayName;
        }

        public void setSubwayName(String subwayName)
        {
            this.subwayName = subwayName;
        }

        public int getDistance()
        {
            return distance;
        }

        public void setDistance(int distance)
        {
            this.distance = distance;
        }
    }
}
