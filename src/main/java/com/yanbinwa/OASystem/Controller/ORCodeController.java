package com.yanbinwa.OASystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yanbinwa.OASystem.Service.ORCodeService;

@RestController("/ORCode")
public class ORCodeController
{
    public static final String ORCODE_ROOT_URL = "/ORCode";
    
    @Autowired
    ORCodeService oRCodeService;
    
    //最终应该发送过来门店信息，通过门店信息来生成对应的key
    @RequestMapping(value = ORCODE_ROOT_URL + "/getORCodeKey", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getORCodeKey() {
        return oRCodeService.getORCodeKey();
    }
}
