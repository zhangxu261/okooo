package io.github.okooo.mapper;

import io.github.okooo.domain.Chayi;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ChayiMapper extends Mapper<Chayi> {

    Chayi findBySerialAndMatchTime(@Param("serial") String serial,
                                   @Param("matchTime") String matchTime);
}
