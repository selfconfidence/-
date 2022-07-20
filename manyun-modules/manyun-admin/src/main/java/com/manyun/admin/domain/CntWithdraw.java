package com.manyun.admin.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;

@ApiModel("提现配置对象")
public class CntWithdraw extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("提现开关 0:开 1:关")
    private String state;

    @ApiModelProperty("手续费")
    private BigDecimal servicefee;

    @ApiModelProperty("提现金额限制")
    private BigDecimal minmoney;

    @ApiModelProperty("月提现次数")
    private Long monthlimit;

    @ApiModelProperty("提现规则")
    private String rule;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setServicefee(BigDecimal servicefee)
    {
        this.servicefee = servicefee;
    }

    public BigDecimal getServicefee()
    {
        return servicefee;
    }
    public void setMinmoney(BigDecimal minmoney)
    {
        this.minmoney = minmoney;
    }

    public BigDecimal getMinmoney()
    {
        return minmoney;
    }
    public void setMonthlimit(Long monthlimit)
    {
        this.monthlimit = monthlimit;
    }

    public Long getMonthlimit()
    {
        return monthlimit;
    }
    public void setRule(String rule)
    {
        this.rule = rule;
    }

    public String getRule()
    {
        return rule;
    }
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }
    public void setUpdatedTime(Date updatedTime)
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime()
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("state", getState())
            .append("servicefee", getServicefee())
            .append("minmoney", getMinmoney())
            .append("monthlimit", getMonthlimit())
            .append("rule", getRule())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
