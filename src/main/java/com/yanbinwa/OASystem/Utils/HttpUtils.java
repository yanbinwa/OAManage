package com.yanbinwa.OASystem.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.yanbinwa.OASystem.Message.HttpResult;
import com.yanbinwa.OASystem.Message.Message.MessageHttpMethod;

public class HttpUtils
{
    public static final String STORE_TYPE = "JKS";
    public static final String JKS_PASSWORD = "password";
    public static final String KEY_PASSWORD = "password";
    public static final String KEYSTORE_PATH = "/Users/yanbinwa/.keystore";
    
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_ERROR = 400;
    
    private static final Logger logger = Logger.getLogger(HttpUtils.class);
    
    static 
    {
        HttpsURLConnection.setDefaultHostnameVerifier(
        new HostnameVerifier()
        {
            public boolean verify(String hostname, SSLSession sslSession) 
            {
                if (hostname.equals("localhost")) 
                {
                    return true;
                }
                return false;
            }
        });
        
        
        try
        {
            KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
            InputStream is = new FileInputStream(KEYSTORE_PATH);
            keyStore.load(is, JKS_PASSWORD.toCharArray());
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, KEY_PASSWORD.toCharArray());
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
            final SSLSocketFactory socketFactory = sc.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
        } 
        catch (KeyStoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (CertificateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (UnrecoverableKeyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (KeyManagementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
        
    public static boolean isHttpsUrl(String url)
    {
        if (url == null)
        {
            logger.error("url should not empty");
            return false;
        }
        String urlTag = url.split(":")[0];
        if (urlTag == "https")
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static HttpURLConnection getHttpConnection(String url, String type)
    {
        URL uri = null;
        HttpURLConnection con = null;
        try
        {
            uri = new URL(url);
            con = (HttpURLConnection) uri.openConnection();
            con.setRequestMethod(type);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
            con.setRequestProperty("Content-Type", "application/json");
        }
        catch(Exception e)
        {
            logger.error( "connection i/o failed" );
        }
        return con;
    }
    
    public static HttpsURLConnection getHttpsConnection(String url, String type)
    {
        return null;
    }
    
    public static HttpResult httpRequest(String url, String payLoad, String type)
    {
        HttpURLConnection con = HttpUtils.getHttpConnection(url, type);
        HttpResult ret = new HttpResult();
        
        if (con == null)
        {
            logger.error("the con is null");
            return null;
        }
        
        try
        {
            if (payLoad != null && payLoad != "")
            {
                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
                out.write(payLoad);  
                out.flush();  
                out.close(); 
            }
            
            con.connect();
            
            InputStream inputStream = con.getInputStream();  
            String encoding = con.getContentEncoding();  
            String responseMessage = IOUtils.toString(inputStream, encoding);  
            int stateCode = con.getResponseCode();
            ret.setResponse(responseMessage);
            ret.setStateCode(stateCode);
            
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            ret.setStateCode(RESPONSE_ERROR);
            return ret;
        } 
        finally
        {
            con.disconnect();
        }
        return ret;
    }
    
    public static String getHttpMethodStr(MessageHttpMethod method)
    {
        if (method == null)
        {
            return null;
        }
        String methodStr = null;
        switch(method)
        {
        case GET:
            methodStr = "GET";
            break;
            
        case POST:
            methodStr = "POST";
            break;
            
        case DEL:
            methodStr = "DELETE";
            break;
            
        default:
            break;
        }
        
        return methodStr;
    }
}
