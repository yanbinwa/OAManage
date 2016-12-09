package com.yanbinwa.OASystem.Service;

public interface ORCodeService
{
    public static final String INTERVAL_PERIOD = "ORCode_IntervalPeriod";
    public static final String ORCODE_KEY_LENGTH = "ORCode_KeyLength";
    
    public static final String ORCODEUPDATE_ROUTEKEY = "ORCode_ORCodeUpdate_RouteKey";
    public static final String ORCODEUPDATE_FUNCTIONKEY = "ORCode_ORCodeUpdate_FunctionKey";
    
    public String getORCodeKey();
}
