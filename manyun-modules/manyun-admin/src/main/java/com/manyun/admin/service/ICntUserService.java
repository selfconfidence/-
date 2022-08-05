package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.dto.MyBoxDto;
import com.manyun.admin.domain.dto.MyCollectionDto;
import com.manyun.admin.domain.dto.MyOrderDto;
import com.manyun.admin.domain.dto.UpdateBalanceDto;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.UserBoxVo;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 用户Service接口
 *
 * @author yanwei
 * @date 2022-07-12
 */
public interface ICntUserService extends IService<CntUser>
{

    /**
     * 用户和钱包信息
     *
     * @param userMoneyQuery 用户和钱包信息
     * @return 结果
     */
    TableDataInfo<UserMoneyVo> selectUserMoneyList(UserMoneyQuery userMoneyQuery);

    /**
     * 修改用户
     *
     * @param cntUser 用户
     * @return 结果
     */
    public int updateCntUser(CntUser cntUser);

    /**
     * 我的订单
     */
    List<CntOrderVo> myOrderList(MyOrderDto orderDto);

    /**
     * 我的藏品
     */
    List<UserCollectionVo> myCollectionList(MyCollectionDto collectionDto);

    /**
     * 修改余额
     */
    int updateBalance(UpdateBalanceDto balanceDto);

    /**
     * 我的盲盒
     */
    List<UserBoxVo> myBoxList(MyBoxDto boxDto);
}
