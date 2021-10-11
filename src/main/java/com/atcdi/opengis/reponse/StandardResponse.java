package com.atcdi.opengis.reponse;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StandardResponse {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回消息")
    private String msg;

    @ApiModelProperty(value = "返回数据")
    private Object data = new Object();


    public static StandardResponse failure(String msg){
        StandardResponse res = new StandardResponse();
        res.success = false;
        res.msg = msg;
        return res;
    }

}
