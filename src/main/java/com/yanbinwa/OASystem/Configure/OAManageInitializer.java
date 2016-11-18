package com.yanbinwa.OASystem.Configure;

import javax.servlet.Filter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.yanbinwa.OASystem.Filters.CORSFilter;

public class OAManageInitializer extends AbstractAnnotationConfigDispatcherServletInitializer 
{

    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        // TODO Auto-generated method stub
        return new Class[] { OAManageConfiguration.class };
    }
    
    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected String[] getServletMappings()
    {
        // TODO Auto-generated method stub
        return new String[] { "/servlet/*", "/websocket/*" }; 
    }
    
    @Override
    protected Filter[] getServletFilters() {
        Filter [] singleton = { new CORSFilter()};
        return singleton;
    }

}
