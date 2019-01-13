package dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EvolOChResultMapper {

    List<String> selectStringByID(@Param("ID") int ID);
}
