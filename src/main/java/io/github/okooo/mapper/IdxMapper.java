package io.github.okooo.mapper;

import io.github.okooo.domain.Idx;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface IdxMapper extends Mapper<Idx> {

    @Select("select * from idx where serial = #{serial} and match_time = #{matchTime} order by created_time desc limit 1")
    Idx findLastOne(@Param("serial") String serial,
                    @Param("matchTime") String matchTime);
}
