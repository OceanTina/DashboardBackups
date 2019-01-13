package dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActResultMapper {

    List<String> selectNameByID(@Param("ID") int ID);
}
