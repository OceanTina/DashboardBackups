package dao.impl;

import dao.inf.IEvolNCOChDao;
import dao.mapper.EvolOChResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import source.AbstractDao;
import source.IMapperProxy;

import java.util.List;

public class EvolNCOChDaoImpl extends AbstractDao<EvolOChResultMapper> implements IEvolNCOChDao {

    @Autowired
    @Qualifier("mysql")
    private IMapperProxy mapperProxy;

    @Override
    public List<String> selectStringByID(int ID)
    {
        EvolOChResultMapper evolOChResultMapper = mapperProxy.getMapper(EvolOChResultMapper.class);
        return evolOChResultMapper.selectStringByID(ID);
    }
}
