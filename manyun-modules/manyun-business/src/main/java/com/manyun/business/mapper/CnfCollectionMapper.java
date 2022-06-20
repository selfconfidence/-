package com.manyun.business.mapper;

import com.manyun.business.domain.entity.CnfCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.vo.CollectionVo;

import java.util.List;

/**
 * <p>
 * 藏品表 Mapper 接口
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface CnfCollectionMapper extends BaseMapper<CnfCollection> {

    List<CollectionVo> pageQueryList(CollectionQuery collectionQuery);
}
