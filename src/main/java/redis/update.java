package redis;

public class update {

    reportMgrDao.updateReportState;


    public void updateReportState(int taskId, int excelType, int value, String localeName)
    {
        Jedis jedis = this.cache.getJedis();
        String strProjId = getStoreKey(String.valueOf(taskId), DATA_TYPE_REPORT, localeName);
        String strExcelType = String.valueOf(excelType);

        if (!jedis.hexists(strProjId, strExcelType))
        {
            jedis.hset(strProjId, strExcelType, String.valueOf(ReportConsts.NOT_EXPORT));
        }
        jedis.hset(strProjId, strExcelType, String.valueOf(value));

        this.cache.returnJedis(jedis);
    }
}
