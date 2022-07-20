package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创作者下拉框")
public class CollectionCreationdDictVo {

    @ApiModelProperty("创作者id")
    private String id;

    @ApiModelProperty("创作者名称")
    private String creationName;

}
