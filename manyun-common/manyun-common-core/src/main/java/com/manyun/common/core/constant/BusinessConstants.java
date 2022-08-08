package com.manyun.common.core.constant;

import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.NumberUtil;

/**
 * 业务常量类
 */
public interface BusinessConstants {


    /**
     * 全局modelType 常量类型
     */
    interface ModelTypeConstant{
        // 盲盒业务模块类型
        String BOX_MODEL_TYPE= "box_model_type";
        // 藏品业务模块类型
        String COLLECTION_MODEL_TYPE= "collection_model_type";


        // 钱包业务模块类型
        String MONEY_TYPE= "money_model_type";

        // 盲盒业务 标识码
        Integer BOX_TAYPE = Integer.valueOf(1);
        // 藏品业务 标识码
        Integer COLLECTION_TAYPE = Integer.valueOf(0);
    }

    /**
     * 系统编号常量类
     */
    interface SystemTypeConstant{

        /**
         *  订单到期时间
         */
        String ORDER_END_TIME= "ORDER_PAY_END";

        /**
         * 是否开启转增
         */
        String TRAN_ACC= "TRAN_ACC";

        /**
         * 转赠说明
         */
        String TRAN_INFO= "TRAN_INFO";

        /**
         * 寄售手续费
         */
        String  CONSIGNMENT_SERVER_CHARGE = "consignment_server_chage";

        /**
         * 寄售订单到期时间
         */
        String  CONSIGNMENT_ORDER_TIME =  "consignment_order_time";


        /**
         *  寄售订单超时时间 小时为单位
         */
        String  CONSIGNMENT_DE_TIME = "CONSIGNMENT_DE_TIME";

        /**
         * 是否开启拍卖（1开启，0关闭）
         */
        String AUCTION_ACC = "auction_acc";

        /**
         * 拍卖出售须知
         */
        String AUCTION_SELL_INFO = "auction_sell_info";

        /**
         * 拍卖订单到期时间
         */
        String AUCTION_ORDER_TIME = "auction_order_time";

        /**
         * 拍卖预售时间
         */
        String AUCTION_PRE_TIME = "auction_pre_time";

        /**
         * 拍卖时间
         */
        String AUCTION_BID_TIME = "auction_bid_time";

        /**
         * 延拍时间
         */
        String AUCTION_DELAY_TIME = "auction_delay_time";

        /**
         * 拍卖加价幅度
         */
        String AUCTION_PRICE_RANGE = "auction_price_range";

        /**
         * 保证金比例
         */
        String MARGIN_SCALE = "margin_scale";

        /**
         * 佣金比例
         */
        String COMMISSION_SCALE = "commission_scale";

        /**
         * 购买须知
         */
        String  SELL_INFO =  "SELL_INFO";

        /**
         * 关于藏品
         */
        String  COLLECTION_INFO =  "COLLECTION_INFO";

        /**
         *  用户默认头像地址
         */
        String  USER_DEFAULT_LINK = "USER_DEFAULT_LINK";


        /**
         * 藏品编号生成位数
         */
        String COLLECTION_POT = "COLLECTION_POT";


        /**
         * 邀请海报链接
         */
        String INVITE_URL = "invite_url";

        /**
         * 注册链接
         */
        String REG_URL = "reg_url";

        /**
         * 网关地址
         */
        String GATEWAY_URL = "gateway_url";

        /**
         * 上传图片本地保存地址
         */
        String LOCAL_PATH = "local_path";

    }


    /**
     * 通用日志相关
     * See Also: ModelTypeConstant
     */
    interface LogsTypeConstant{

        /**
         * (钱包/支出)
         * (盲盒/转赠)
         * (藏品/转赠)
         */
        Integer POLL_SOURCE = Integer.valueOf(1);


        /**
         * (钱包/收入)
         * (盲盒/购入)
         * (藏品/购入)
         */
        Integer PULL_SOURCE = Integer.valueOf(0);
    }



    interface RedisDict{
        String PHONE_CODE = "phone:code:";
        // 验证码过期时间 60 秒
        Integer EXP_TIME = Integer.valueOf(60);
        // 持续递增编号key
        String COLLECTION_AUTO_NUM = "collection:auto:numbers:";
    }


}
