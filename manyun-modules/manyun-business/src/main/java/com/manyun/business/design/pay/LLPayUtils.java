package com.manyun.business.design.pay;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.manyun.business.design.pay.bean.OpenacctApplyAccountInfo;
import com.manyun.business.design.pay.bean.OpenacctApplyBasicInfo;
import com.manyun.business.design.pay.bean.OpenacctApplyParams;
import com.manyun.business.design.pay.bean.OpenacctApplyResult;
import com.manyun.business.design.pay.bean.cashier.*;
import com.manyun.business.design.pay.bean.individual.IndividualBindCardApplyParams;
import com.manyun.business.design.pay.bean.individual.IndividualBindCardApplyResult;
import com.manyun.business.design.pay.bean.individual.IndividualBindCardVerifyParams;
import com.manyun.business.design.pay.bean.individual.IndividualBindCardVerifyResult;
import com.manyun.business.design.pay.bean.query.*;
import com.manyun.business.design.pay.bean.random.GetRandomParams;
import com.manyun.business.design.pay.bean.random.GetRandomResult;
import com.manyun.business.design.pay.bean.txn.*;
import com.manyun.business.domain.query.*;
import com.manyun.business.domain.vo.AcctSerIalListVo;
import com.manyun.business.domain.vo.AcctSerIalVo;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.LianLianPayEnum;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.pays.utils.llpay.LLianPayDateUtils;
import com.manyun.common.pays.utils.llpay.client.LLianPayClient;
import com.manyun.common.pays.utils.llpay.config.LLianPayConstant;
import com.manyun.common.pays.utils.llpay.security.LLianPayAccpSignature;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 连连支付 相关api
 */
@Component
public class LLPayUtils {

    //开户
    private final static String openacctApply = "https://accpgw-ste.lianlianpay-inc.com/v1/acctmgr/openacct-apply";
    //绑卡申请
    private final static String individualBindcardApply = "https://accpapi-ste.lianlianpay-inc.com/v1/acctmgr/individual-bindcard-apply";
    //绑卡验证
    private final static String individualBindcardVerify = "https://accpapi-ste.lianlianpay-inc.com/v1/acctmgr/individual-bindcard-verify";
    //提现申请
    private final static String withdrawal = "https://accpapi-ste.lianlianpay-inc.com/v1/txn/withdrawal";
    //交易二次短信验证
    private final static String validationSms = "https://accpapi-ste.lianlianpay-inc.com/v1/txn/validation-sms";
    //充值丶消费
    private final static String paycreate = "https://accpgw-ste.lianlianpay-inc.com/v1/cashier/paycreate";
    //查询余额
    private final static String queryAcctinfo = "https://accpapi-ste.lianlianpay-inc.com/v1/acctmgr/query-acctinfo";
    //查询绑卡列表
    private final static String queryLinkedacct = "https://accpapi-ste.lianlianpay-inc.com/v1/acctmgr/query-linkedacct";
    //查询资金流水列表
    private final static String queryAcctserial = "https://accpapi-ste.lianlianpay-inc.com/v1/acctmgr/query-acctserial";
    //随机因子获取
    private final static String getRandom = "https://accpapi-ste.lianlianpay-inc.com/v1/acctmgr/get-random";

    //使用时,需确认用户实名状况,必须是实名用户
    /**
     * 开户
     * @param innerUserQuery
     * @return 开户的url地址
     */
    public static String innerUser(LLInnerUserQuery innerUserQuery) {
        Assert.isTrue(Objects.nonNull(innerUserQuery),"请求参数有误!");
        Assert.isTrue(
                StringUtils.isNotBlank(innerUserQuery.getUserId()) ||  StringUtils.isNotBlank(innerUserQuery.getRealName()) ||
                            StringUtils.isNotBlank(innerUserQuery.getPhone()) ||  StringUtils.isNotBlank(innerUserQuery.getCartNo()) ||
                            StringUtils.isNotBlank(innerUserQuery.getReturnUrl()),"请求参数有误!"
        );
        OpenacctApplyParams params = new OpenacctApplyParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id(innerUserQuery.getUserId());
        params.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        params.setTxn_time(timestamp);
        /*
        交易发起渠道。
        ANDROID
        IOS
        H5
        PC
         */
        params.setFlag_chnl("H5");
        // 交易完成回跳页面地址，H5/PC渠道必传。
        params.setReturn_url(innerUserQuery.getReturnUrl());
        // 交易结果异步通知接收地址，建议HTTPS协议。
        params.setNotify_url(LianLianPayEnum.INNER_USER.getNotifyUrl());
        /*
        用户类型。
        INNERUSER：个人用户
        INNERCOMPANY：企业用户
         */
        params.setUser_type("INNERUSER");

        // 设置开户账户申请信息
        OpenacctApplyAccountInfo accountInfo = new OpenacctApplyAccountInfo();
        /*
        个人支付账户  PERSONAL_PAYMENT_ACCOUNT
        企业支付账户  ENTERPRISE_PAYMENT_ACCOUNT
         */
        accountInfo.setAccount_type("PERSONAL_PAYMENT_ACCOUNT");
        accountInfo.setAccount_need_level("V3");
        params.setAccountInfo(accountInfo);

        //开户基本信息
        OpenacctApplyBasicInfo basicInfo = new OpenacctApplyBasicInfo();
        basicInfo.setUser_name(innerUserQuery.getRealName());
        basicInfo.setReg_phone(innerUserQuery.getPhone());
        basicInfo.setId_type("ID_CARD");
        basicInfo.setId_no(innerUserQuery.getCartNo());
        params.setBasicInfo(basicInfo);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(openacctApply, JSON.toJSONString(params));
        OpenacctApplyResult openacctApplyResult = JSON.parseObject(resultJsonStr, OpenacctApplyResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(openacctApplyResult.getRet_code()),openacctApplyResult.getRet_msg());
        return openacctApplyResult.getGateway_url();
    }


    /**
     * 绑卡
     * @param llTiedCardQuery
     */
    public static void bindCard(LLTiedCardQuery llTiedCardQuery) {
        LLianPayClient lLianPayClient = new LLianPayClient();
        // 绑卡申请
        IndividualBindCardApplyResult bindCardApplyResult = bindCardApply(lLianPayClient,llTiedCardQuery);
        // 绑卡验证
        if (!"0000".equals(bindCardApplyResult.getRet_code())) {
            Assert.isTrue(Boolean.FALSE,"绑卡申请失败，请检查！");
        }
        bindCardVerify(lLianPayClient, bindCardApplyResult);
    }


    /**
     * 提现
     * @param   userId 用户Id
     * @param   passWord 支付密码
     * @param   amount 提现金额
     */
    public static Map<String,String> withdraw(String userId, String passWord, BigDecimal amount) {
        Map<String,String> map = new HashMap<String, String>();
        WithDrawalParams params = new WithDrawalParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setNotify_url(LianLianPayEnum.WITHDRAW.getNotifyUrl());
        params.setRisk_item("");

        // 设置商户订单信息
        WithDrawalOrderInfo orderInfo = new WithDrawalOrderInfo();
        orderInfo.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        orderInfo.setTxn_time(timestamp);
        orderInfo.setTotal_amount(amount);
        orderInfo.setPostscript("用户提现");
        params.setOrderInfo(orderInfo);

        // 设置付款方信息
        WithDrawalPayerInfo payerInfo = new WithDrawalPayerInfo();
        payerInfo.setPayer_type("USER");
        payerInfo.setPayer_id(userId);
        // 用户：LLianPayTest-In-User-12345 密码：qwerty，本地测试环境测试，没接入密码控件，使用本地加密方法加密密码（仅限测试环境使用）
        payerInfo.setPassword(LLianPayAccpSignature.getInstance().localEncrypt(passWord));
        params.setPayerInfo(payerInfo);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(withdrawal, JSON.toJSONString(params));
        WithDrawalResult drawalResult = JSON.parseObject(resultJsonStr, WithDrawalResult.class);

        // 小额免验，不需要验证码，直接返回0000
        if ("0000".equals(drawalResult.getRet_code())) {
            map.put("code",drawalResult.getRet_code());
            map.put("msg","提现申请成功!");
            return map;
        }else if("8888".equals(drawalResult.getRet_code())){
            map.put("code",drawalResult.getRet_code());
            map.put("msg","提现需要再次信息短信验证码校验!");
            map.put("userId",drawalResult.getUser_id());
            map.put("txn_seqno",drawalResult.getTxn_seqno());
            map.put("total_amount",drawalResult.getTotal_amount().toString());
            map.put("token",drawalResult.getToken());
            return map;
        }
        map.put("code","500");
        map.put("msg","申请提现失败,系统异常!");
        return map;
    }


    /**
     * 交易二次短信验证
     * @param userId 用户id
     * @param txnSeqno 商户系统唯一交易流水号。【注：与创建交易时所传流水号相同】
     * @param amount 订单总金额，单位为元，精确到小数点后两位。
     * @param token  授权令牌，有效期为30分钟。
     * @param verifyCode  短信验证码。交易需要短信验证时发送给用户手机的验证码。
     */
    public static Map<String,String> validationSms(String userId, String txnSeqno,String amount,String token,String verifyCode) {
        Assert.isTrue(
        StringUtils.isNotBlank(userId) || StringUtils.isNotBlank(txnSeqno) ||
                    StringUtils.isNotBlank(amount) || StringUtils.isNotBlank(token) ||
                    StringUtils.isNotBlank(verifyCode)
               ,"请求参数有误!");
        Map<String,String> map = new HashMap<>();
        ValidationSmsParams params=new ValidationSmsParams();
        params.setTimestamp(LLianPayDateUtils.getTimestamp());
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setPayer_type("USER");
        params.setPayer_id(userId);
        params.setTxn_seqno(txnSeqno);
        params.setTotal_amount(amount);
        params.setToken(token);
        params.setVerify_code(verifyCode);
        LLianPayClient lLianPayClient = new LLianPayClient();

        String resultJsonStr = lLianPayClient.sendRequest(validationSms, JSON.toJSONString(params));
        ValidationSmsResult validationSmsResult = JSON.parseObject(resultJsonStr, ValidationSmsResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(validationSmsResult.getRet_code()),validationSmsResult.getRet_msg());
        map.put("code",validationSmsResult.getRet_code());
        map.put("msg",validationSmsResult.getRet_msg());
        return map;
    }


    /**
     * 用户充值
     * @param userId 用户id
     * @param realName 用户真实姓名
     * @param phone 手机号
     * @param ipAddr
     * @param amount 充值金额
     * @param returnUrl ios Android h5 表单提交之后跳转回app的地址
     */
    public static String userTopup(String userId, String realName, String phone, String ipAddr, BigDecimal amount, String returnUrl) {
        CashierPayCreateParams params = new CashierPayCreateParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        // 用户充值
        params.setTxn_type("USER_TOPUP");
        params.setUser_id(userId);
        /*
        用户类型。默认：注册用户。
        注册用户：REGISTERED
        匿名用户：ANONYMOUS
         */
        params.setUser_type("REGISTERED");
        params.setNotify_url(LianLianPayEnum.BIND_CARD_APPLY.getNotifyUrl());
        params.setReturn_url(returnUrl);
        // 交易发起渠道设置
        params.setFlag_chnl("H5");
        // 测试风控参数
        params.setRisk_item(
                "{" +
                        "\"frms_ware_category\":\"4007\"," +
                        "\"goods_name\":\"用户充值\"," +
                        "\"user_info_mercht_userno\":\"" +userId+ "\"," +
                        "\"user_info_dt_register\":\"" +timestamp+ "\"," +
                        "\"user_info_bind_phone\":\"" +phone+ "\"," +
                        "\"user_info_full_name\":\"" +realName+ "\"," +
                        "\"user_info_id_no\":\"\"," +
                        "\"user_info_identify_type\":\"4\"," +
                        "\"user_info_id_type\":\"0\"," +
                        "\"frms_client_chnl\":\" H5\"," +
                        "\"frms_ip_addr\":\"" +ipAddr+ "\"," +
                        "\"user_auth_flag\":\"1\"" +
                        "}"
        );


        // 设置商户订单信息
        CashierPayCreateOrderInfo orderInfo = new CashierPayCreateOrderInfo();
        orderInfo.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        orderInfo.setTxn_time(timestamp);
        orderInfo.setTotal_amount(amount);
        orderInfo.setGoods_name("用户充值");
        params.setOrderInfo(orderInfo);

        // 设置付款方信息
        CashierPayCreatePayerInfo payerInfo = new CashierPayCreatePayerInfo();
        payerInfo.setPayer_id(userId);
        payerInfo.setPayer_type("USER");
        params.setPayerInfo(payerInfo);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(paycreate, JSON.toJSONString(params));
        CashierPayCreateResult cashierPayCreateResult = JSON.parseObject(resultJsonStr, CashierPayCreateResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(cashierPayCreateResult.getRet_code()),cashierPayCreateResult.getRet_msg());
        return cashierPayCreateResult.getGateway_url();
    }


    /**
     * 普通消费
     * @param
     */
    public static String generalConsume(LLGeneralConsumeQuery llGeneralConsumeQuery) {
        CashierPayCreateParams params = new CashierPayCreateParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        // 普通消费
        params.setTxn_type("GENERAL_CONSUME");
        params.setUser_id(llGeneralConsumeQuery.getUserId());
        /*
        用户类型。默认：注册用户。
        注册用户：REGISTERED
        匿名用户：ANONYMOUS
         */
        params.setUser_type("REGISTERED");
        params.setNotify_url(llGeneralConsumeQuery.getNotifyUrl());
        params.setReturn_url(llGeneralConsumeQuery.getReturnUrl());
        // 交易发起渠道设置
        params.setFlag_chnl("H5");
        // 测试风控参数
        params.setRisk_item(
                "{" +
                        "\"frms_ware_category\":\"4007\"," +
                        "\"goods_name\":\"" +llGeneralConsumeQuery.getGoodsName()+ "\"," +
                        "\"user_info_mercht_userno\":\"" +llGeneralConsumeQuery.getUserId()+ "\"," +
                        "\"user_info_dt_register\":\"" +timestamp+ "\"," +
                        "\"user_info_bind_phone\":\"" +llGeneralConsumeQuery.getPhone()+ "\"," +
                        "\"user_info_full_name\":\"" +llGeneralConsumeQuery.getRealName()+ "\"," +
                        "\"user_info_id_no\":\"\"," +
                        "\"user_info_identify_state\":\"0\"," +
                        "\"user_info_identify_type\":\"4\"," +
                        "\"user_info_id_type\":\"0\"," +
                        "\"frms_client_chnl\":\" H5\"," +
                        "\"frms_ip_addr\":\"" +llGeneralConsumeQuery.getIpAddr()+ "\"," +
                        "\"user_auth_flag\":\"1\"" +
                        "}"
        );
        // 设置商户订单信息
        CashierPayCreateOrderInfo orderInfo = new CashierPayCreateOrderInfo();
        orderInfo.setTxn_seqno(llGeneralConsumeQuery.getOrderId());
        orderInfo.setTxn_time(timestamp);
        orderInfo.setTotal_amount(llGeneralConsumeQuery.getAmount());
        orderInfo.setGoods_name(llGeneralConsumeQuery.getGoodsName());
        params.setOrderInfo(orderInfo);

        // 设置收款方信息
        CashierPayCreatePayeeInfo mPayeeInfo = new CashierPayCreatePayeeInfo();
        mPayeeInfo.setPayee_id(LLianPayConstant.OidPartner);
        mPayeeInfo.setPayee_type("MERCHANT");
        mPayeeInfo.setPayee_amount(llGeneralConsumeQuery.getAmount());
        mPayeeInfo.setPayee_memo("用户购买商品");
        params.setPayeeInfo(new CashierPayCreatePayeeInfo[]{mPayeeInfo});

        // 设置付款方信息
        CashierPayCreatePayerInfo payerInfo = new CashierPayCreatePayerInfo();
        payerInfo.setPayer_id(llGeneralConsumeQuery.getUserId());
        payerInfo.setPayer_type("USER");
        params.setPayerInfo(payerInfo);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(paycreate, JSON.toJSONString(params));
        CashierPayCreateResult cashierPayCreateResult = JSON.parseObject(resultJsonStr, CashierPayCreateResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(cashierPayCreateResult.getRet_code()),cashierPayCreateResult.getRet_msg());
        return cashierPayCreateResult.getGateway_url();
    }


    /**
     * 查询余额
     * @param userId 用户id
     */
    public static String queryAcctinfo(String userId) {
        Assert.isTrue(StringUtils.isNotBlank(userId),"请求参数有误,请检查!");
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",LLianPayDateUtils.getTimestamp());
        map.put("oid_partner",LLianPayConstant.OidPartner);
        map.put("user_id",userId);
        map.put("user_type","INNERUSER");

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(queryAcctinfo, JSON.toJSONString(map));
        AcctInfoResult acctInfoResult = JSON.parseObject(resultJsonStr, AcctInfoResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(acctInfoResult.getRet_code()),acctInfoResult.getRet_msg());
        Optional<AcctinfoList> optional = acctInfoResult.getAcctinfo_list().parallelStream().filter(f -> ("USEROWN_PSETTLE".equals(f.getAcct_type()) && "NORMAL".equals(f.getAcct_state()))).findFirst();
        if(!optional.isPresent()){
            Assert.isTrue(Boolean.FALSE,"用户余额查询失败,请重试!");
        }
        return optional.get().getAmt_balcur();
    }



    /**
     * 查询绑卡列表
     * @param userId 用户id
     */
    public static List<LinkedAcctlist> queryLinkedacct(String userId) {
        Assert.isTrue(StringUtils.isNotBlank(userId),"请求参数有误,请检查!");
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",LLianPayDateUtils.getTimestamp());
        map.put("oid_partner",LLianPayConstant.OidPartner);
        map.put("user_id",userId);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(queryLinkedacct, JSON.toJSONString(map));
        LinkeDacctResult linkeDacctResult = JSON.parseObject(resultJsonStr, LinkeDacctResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(linkeDacctResult.getRet_code()),linkeDacctResult.getRet_msg());
        return linkeDacctResult.getLinked_acctlist();
    }


    /**
     * 查询资金流水列表
     * @param userId 用户id
     * @param startDate 账期开始时间。交易账期查询开始时间，必须小于等于当前时间，闭区间。格式：yyyyMMddHHmmss。
     * @param endDate   账期结束时间。交易账期查询结束时间，必须大于等于开始时间且小于等于当前时间，闭区间。格式：yyyyMMddHHmmss。
     * @param pageNo 请求页码。表示当前请求第几页，从1开始计数。
     * @param pageSize 每页记录数。每页最大记录数为10。
     */
    public static AcctSerIalVo queryAcctserial(String userId, String startDate, String endDate, String pageNo, String pageSize) {
        Assert.isTrue(StringUtils.isNotBlank(userId) || StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate),"请求参数有误,请检查!");
        AcctserialParams params = new AcctserialParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id(userId);
        /*
        用户类型。
        INNERMERCHANT:商户
        INNERUSER：个人用户
        INNERCOMPANY：企业用户
         */
        params.setUser_type("INNERUSER");
        /*
        USEROWN_PSETTLE	用户自有待结算账户
        USEROWN_AVAILABLE	用户自有可用账户
         */
        params.setAcct_type("USEROWN_PSETTLE");
        params.setDate_start(StringUtils.isNotBlank(startDate)==true?startDate:"20220913000000");
        params.setDate_end(StringUtils.isNotBlank(endDate)==true?endDate:DateUtils.dateTimeNow());
        params.setPage_no(StringUtils.isNotBlank(pageNo)==true?pageNo:"1");
        params.setPage_size(StringUtils.isNotBlank(pageSize)==true?pageSize:"10");
        params.setSort_type("DESC");

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(queryAcctserial, JSON.toJSONString(params));
        AcctserialResult acctserialResult = JSON.parseObject(resultJsonStr, AcctserialResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(acctserialResult.getRet_code()),acctserialResult.getRet_msg());
        AcctSerIalVo acctSerIalVo=new AcctSerIalVo();
        acctSerIalVo.setUserId(acctserialResult.getUser_id());
        acctSerIalVo.setTotalOutAmt(acctserialResult.getTotal_out_amt());
        acctSerIalVo.setTotalInAmt(acctserialResult.getTotal_in_amt());
        List<AcctSerIalListVo> acctSerIalListVos = new ArrayList<>();
        acctserialResult.getAcctbal_list().parallelStream().forEach(e->{
            acctSerIalListVos.add(
                    Builder.of(AcctSerIalListVo::new)
                            .with(AcctSerIalListVo::setAccpTxnno,e.getAccp_txnno())
                            .with(AcctSerIalListVo::setAmt,e.getAmt())
                            .with(AcctSerIalListVo::setAmtBal,e.getAmt_bal())
                            .with(AcctSerIalListVo::setTxnType,
                                    e.getTxn_type().equals("USER_TOPUP")?"用户充值"
                                            :e.getTxn_type().equals("MCH_TOPUP")?"商户充值"
                                            :e.getTxn_type().equals("GENERAL_CONSUME")?"普通消费"
                                            :e.getTxn_type().equals("SECURED_CONSUME")?"担保消费"
                                            :e.getTxn_type().equals("SERVICE_FEE")?"手续费收取"
                                            :e.getTxn_type().equals("INNER_FUND_EXCHANGE")?"内部代发"
                                            :e.getTxn_type().equals("OUTER_FUND_EXCHANGE")?"外部代发"
                                            :e.getTxn_type().equals("ACCT_CASH_OUT")?"账户提现"
                                            :e.getTxn_type().equals("SECURED_CONFIRM")?"担保确认"
                                            :e.getTxn_type().equals("CAPITAL_CANCEL")?"手续费应收应付核销"
                                            :e.getTxn_type().equals("INNER_DIRECT_EXCHANGE")?"定向内部代发":""
                                    )
                            .with(AcctSerIalListVo::setTxnTime,DateUtils.getDateToDate(DateUtils.getStrToDate(e.getTxn_time(), DateUtils.YYYYMMDDHHMMSS),DateUtils.YYYY_MM_DD_HH_MM_SS))
                    .build()
            );
        });
        acctSerIalVo.setAcctSerIalListVos(acctSerIalListVos);
        return acctSerIalVo;
    }


    /**
     * 个人用户新增绑卡申请
     * @param lLianPayClient
     * @return
     */
    private static IndividualBindCardApplyResult bindCardApply(LLianPayClient lLianPayClient,LLTiedCardQuery llTiedCardQuery) {
        IndividualBindCardApplyParams params = new IndividualBindCardApplyParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id(llTiedCardQuery.getUserId());
        params.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        params.setTxn_time(timestamp);
        params.setNotify_url(LianLianPayEnum.BIND_CARD_APPLY.getNotifyUrl());
        // 设置银行卡号
        params.setLinked_acctno(llTiedCardQuery.getLinkedAcctno());
        // 设置绑卡手机号
        params.setLinked_phone(llTiedCardQuery.getLinkedPhone());
        // 设置钱包密码，正式环境要接密码控件，调试API可以用连连公钥加密密码
        params.setPassword(LLianPayAccpSignature.getInstance().localEncrypt(llTiedCardQuery.getPassword()));

        String resultJsonStr = lLianPayClient.sendRequest(individualBindcardApply, JSON.toJSONString(params));
        IndividualBindCardApplyResult bindCardApplyResult = JSON.parseObject(resultJsonStr, IndividualBindCardApplyResult.class);
        return bindCardApplyResult;
    }


    /**
     * 个人用户新增绑卡验证
     * @param lLianPayClient
     * @param bindCardApplyResult
     */
    private static void bindCardVerify(LLianPayClient lLianPayClient, IndividualBindCardApplyResult bindCardApplyResult) {
        IndividualBindCardVerifyParams params = new IndividualBindCardVerifyParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(bindCardApplyResult.getOid_partner());
        params.setUser_id(bindCardApplyResult.getUser_id());
        params.setTxn_seqno(bindCardApplyResult.getTxn_seqno());
        params.setToken(bindCardApplyResult.getToken());
        // 测试环境首次绑卡，不下发短信验证码，任意6位数字
        params.setVerify_code("123456");

        String resultJsonStr = lLianPayClient.sendRequest(individualBindcardVerify, JSON.toJSONString(params));
        IndividualBindCardVerifyResult bindCardVerifyResult = JSON.parseObject(resultJsonStr, IndividualBindCardVerifyResult.class);
        System.out.println(bindCardVerifyResult);
    }


    /**
     * 随机因子获取
     * @param
     */
    private static GetRandomResult getRandom(String userId) {
        GetRandomParams params = new GetRandomParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id("userId");
        /*
        交易发起渠道。
        ANDROID
        IOS
        H5
        PC
         */
        params.setFlag_chnl("H5");
        params.setEncrypt_algorithm("RSA");

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(getRandom, JSON.toJSONString(params));
        GetRandomResult getRandomResult = JSON.parseObject(resultJsonStr, GetRandomResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(getRandomResult.getRet_code()),getRandomResult.getRet_msg());
        return getRandomResult;
    }

}
