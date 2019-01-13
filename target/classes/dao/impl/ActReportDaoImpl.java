package dao.impl;

import dao.inf.ActResultDao;
import dao.mapper.ActResultMapper;
import source.AbstractDao;

import java.util.List;

public class ActReportDaoImpl extends AbstractDao<ActResultMapper> implements ActResultDao {
    public ActResultMapper getMapper()
    {
        ActResultMapper mapper = getMapper(ActResultMapper.class);
        return mapper;
    }

    @Override
    public List<String> selectNameByID(int ID)
    {
        return getMapper().selectNameByID(ID);
    }

}
