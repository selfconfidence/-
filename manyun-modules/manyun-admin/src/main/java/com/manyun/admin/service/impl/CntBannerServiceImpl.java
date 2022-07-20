package com.manyun.admin.service.impl;

import java.util.List;

import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntBannerMapper;
import com.manyun.admin.domain.CntBanner;
import com.manyun.admin.service.ICntBannerService;

/**
 * 轮播Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-12
 */
@Service
public class CntBannerServiceImpl implements ICntBannerService
{
    @Autowired
    private CntBannerMapper cntBannerMapper;

    /**
     * 查询轮播
     *
     * @param id 轮播主键
     * @return 轮播
     */
    @Override
    public CntBanner selectCntBannerById(String id)
    {
        return cntBannerMapper.selectCntBannerById(id);
    }

    /**
     * 查询轮播列表
     *
     * @param cntBanner 轮播
     * @return 轮播
     */
    @Override
    public List<CntBanner> selectCntBannerList(CntBanner cntBanner)
    {
        return cntBannerMapper.selectCntBannerList(cntBanner);
    }

    /**
     * 新增轮播
     *
     * @param cntBanner 轮播
     * @return 结果
     */
    @Override
    public int insertCntBanner(CntBanner cntBanner)
    {
        cntBanner.setId(IdUtils.getSnowflakeNextIdStr());
        cntBanner.setCreatedBy(SecurityUtils.getUsername());
        cntBanner.setCreatedTime(DateUtils.getNowDate());
        return cntBannerMapper.insertCntBanner(cntBanner);
    }

    /**
     * 修改轮播
     *
     * @param cntBanner 轮播
     * @return 结果
     */
    @Override
    public int updateCntBanner(CntBanner cntBanner)
    {
        cntBanner.setUpdatedBy(SecurityUtils.getUsername());
        cntBanner.setUpdatedTime(DateUtils.getNowDate());
        return cntBannerMapper.updateCntBanner(cntBanner);
    }

    /**
     * 批量删除轮播
     *
     * @param ids 需要删除的轮播主键
     * @return 结果
     */
    @Override
    public int deleteCntBannerByIds(String[] ids)
    {
        return cntBannerMapper.deleteCntBannerByIds(ids);
    }

    /**
     * 删除轮播信息
     *
     * @param id 轮播主键
     * @return 结果
     */
    @Override
    public int deleteCntBannerById(String id)
    {
        return cntBannerMapper.deleteCntBannerById(id);
    }
}
