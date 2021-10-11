package com.atcdi.opengis.filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.getSession();
        filterChain.doFilter(request, response);
    }

    private void partnerLogin(){

    }

    private void apiLogin(HttpServletRequest request, HttpServletResponse response){
        String header = request.getHeader("Authorization");
        if (header != null) {
            //SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(null, null, null));
            System.out.println("添加到session");
        }
        else {
            response.setStatus(401);
            String ip = "";
            try{
                ip = getIpAddress(request);
            } catch (UnknownHostException e) {
                ip = e.getMessage();
            }
            handlerExceptionResolver.resolveException(
                    request, response, null,
                    new RuntimeException("需要验证", new Throwable("不含token的请求, IP : " + ip)));
        }

    }


    private static String getIpAddress(HttpServletRequest request) throws UnknownHostException {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            String localIp = "127.0.0.1";
            String localIpv6 = "0:0:0:0:0:0:0:1";
            if (ipAddress.equals(localIp) || ipAddress.equals(localIpv6)) {
                InetAddress iNet = null;
                iNet = InetAddress.getLocalHost();
                ipAddress = iNet.getHostAddress();
            }
        }
        String ipSeparate = ",";
        int ipLength = 15;
        if (ipAddress != null && ipAddress.length() > ipLength) {
            if (ipAddress.indexOf(ipSeparate) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(ipSeparate));
            }
        }
        return ipAddress;
    }
}


