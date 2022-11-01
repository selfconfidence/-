package com.manyun.business.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.design.pay.SandPayUtil;
import com.manyun.business.design.pay.bean.sandAccount.OpenAccountForm;
import com.manyun.business.design.pay.bean.sandAccount.OpenAccountParams;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.service.IMoneyService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.SandAccountEnum;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sandAccount")
@Api(tags = "杉德云账户相关api")
public class SandAccountController {

    @Autowired
    private IMoneyService moneyService;


    @Value("${open.url}")
    private String url;

    @PostMapping("/openAccount")
    @ApiOperation("云账户开户")
    public R<String> openAccount(@RequestBody OpenAccountForm form) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        OpenAccountParams params = new OpenAccountParams();
        params.setUserId(loginBusinessUser.getUserId());
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,loginBusinessUser.getUserId()));
        params.setUserName(money.getRealName());
        params.setNotifyUrl(url + SandAccountEnum.OPEN_ACCOUNT.getNotifyUrl());
        params.setReturnUrl(form.getReturnUrl());
        return R.ok(SandPayUtil.openAccount(params));
    }
}
