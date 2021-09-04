package me.zhengjie.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:41
 */
@Getter
@Setter
public class FullUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    //errMsg
    private String errMsg;
    //rawData
    private String rawData;
    //userInfo
    private UserInfo userInfo;
    //encryptedData
    private String encryptedData;
    //iv
    private String iv;
    //signature
    private String signature;


}
