package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;


@ApiModel("轮播图对象")
public class CntBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("轮播标题")
    private String bannerTitle;

    @ApiModelProperty("轮播图片地址")
    private String bannerImage;

    @ApiModelProperty("轮播简介")
    private String bannerInfo;

    @ApiModelProperty("轮播类型;1=首页轮播,2=竞拍轮播")
    private Long bannerType;

    @ApiModelProperty("跳转链接")
    private String jumpLink;

    @ApiModelProperty("创建人")
    private String createdBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setBannerTitle(String bannerTitle)
    {
        this.bannerTitle = bannerTitle;
    }

    public String getBannerTitle()
    {
        return bannerTitle;
    }
    public void setBannerImage(String bannerImage)
    {
        this.bannerImage = bannerImage;
    }

    public String getBannerImage()
    {
        return bannerImage;
    }
    public void setBannerInfo(String bannerInfo)
    {
        this.bannerInfo = bannerInfo;
    }

    public String getBannerInfo()
    {
        return bannerInfo;
    }
    public void setBannerType(Long bannerType)
    {
        this.bannerType = bannerType;
    }

    public Long getBannerType()
    {
        return bannerType;
    }
    public void setJumpLink(String jumpLink)
    {
        this.jumpLink = jumpLink;
    }

    public String getJumpLink()
    {
        return jumpLink;
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
            .append("bannerTitle", getBannerTitle())
            .append("bannerImage", getBannerImage())
            .append("bannerInfo", getBannerInfo())
            .append("bannerType", getBannerType())
            .append("jumpLink", getJumpLink())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
