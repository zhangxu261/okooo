package io.github.okooo.mapper;

import io.github.okooo.domain.Okooo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface OkoooMapper extends Mapper<Okooo> {

    @Select("select * from okooo where serial = #{serial} and match_time = #{matchTime}")
    Okooo findOneBySerialAndMatchTime(@Param("serial") String serial,
                                      @Param("matchTime") String matchTime);
}
