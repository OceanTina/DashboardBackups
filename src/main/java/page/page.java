package page;

public class page {

//    @Override
//    @Transactional
//    public Pair<Integer, List<ClientServiceDTO>> getClientService(Map<String, Object> queryParams)
//            throws ServiceException
//    {
//        List<ClientServiceDTO> cDTOs = ((RlbResultMapper)getMapper(RlbResultMapper.class)).selectClientService(queryParams);
//        int totalNum = ((RlbResultMapper)getMapper(RlbResultMapper.class)).getCountInTheSameSession();
//        return Pair.of(totalNum, cDTOs);
//    }
//
//
//
//
//    Pair<Integer, List<OMSWavelengthDetail>> result = null;
//    result = this.iEvolNCOMSDao.getOmsWavelengthBasicInfoList(solutionId, omsUuidList, filterUuidResult, fromIndex, pageSize,
//    topNum, filterParamMap, orderParam, orderBy);
//
//    int totalNumber = result.getLeft();
//
//    List<OMSWavelengthDetail> omsWavelengthDetailList = result.getRight();
//
//							return new PaginationData<OMSWavelengthDetail>(totalNumber, pageSize, pageNo, omsWavelengthDetailListNew);
//
//
//
//    @Override
//    public Pair<Integer, List<OMSWavelengthDetail>> getOmsWavelengthBasicInfoList(String solutionId, List<String> omsUuidList, List<String> filterUuidList,
//                                                                                  int fromIndex, int pageSize, int topNum, Map<String, String> filterParamMap, String orderParam,
//                                                                                  String orderBy) throws ServiceException
//    {
//        EvolOMSResultMapper evolOMSResultMapper = mapperProxy.getMapper(EvolOMSResultMapper.class);
//        List<OMSWavelengthDetail> detailList = evolOMSResultMapper.getOmsWavelengthBasicInfoList(solutionId, omsUuidList, filterUuidList, fromIndex,
//                pageSize, topNum, filterParamMap, orderParam, orderBy);
//        Integer totalNum = evolOMSResultMapper.countOmsWavelengthInfo(solutionId, omsUuidList, filterUuidList, fromIndex,
//                pageSize, topNum, filterParamMap, orderParam, orderBy);
//
//        return Pair.of(totalNum, detailList);
//    }
}
