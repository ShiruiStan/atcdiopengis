package com.atcdi.opengis.auth;


import lombok.extern.slf4j.Slf4j;
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
import java.util.*;


@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String[] uri = request.getRequestURI().split("/");
        if (uri.length == 0) {
            arrestRequest(request, response);
        } else if (uri[1].equals("api")) {
            apiRequest(request, response);
        } else if (uri[1].equals("partner")) {
            partnerRequest(request, response);
        } else {
            otherRequest(request, response, uri[1]);
        }
        filterChain.doFilter(request, response);
    }

    private void apiRequest(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            if (!token.equals(request.getSession().getAttribute("Authorization"))) {
                apiLogin(request, response);
            } else {
                log.info("验证成功");
            }
        } else {
            response.setStatus(401);
            handlerExceptionResolver.resolveException(
                    request, response, null,
                    new RuntimeException("请求头缺少Authorization", new Throwable("不含token的请求, IP : " + getIpAddress(request))));
        }
    }

    private void apiLogin(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        // TODO 从数据库中查询 database token
        // 如果查询结果不为空，储存对应的合作商名称、项目id到session中
        if (true) {
            request.getSession().setAttribute("partner", "atcdi");
            request.getSession().setAttribute("project", "demo project");
            request.getSession().setAttribute("Authorization", token);
            request.getSession().setAttribute("ip", getIpAddress(request));
        } else {
            handlerExceptionResolver.resolveException(
                    request, response, null,
                    new RuntimeException("请求头Authorization信息错误", new Throwable("错误token的请求, IP : " + getIpAddress(request))));
        }

    }

    private void partnerRequest(HttpServletRequest request, HttpServletResponse response) {

    }

    private void partnerLogin() {


    }

    private void otherRequest(HttpServletRequest request, HttpServletResponse response, String baseUri) {
        Set<String> allowedPath = Set.of("test", "swagger-ui.html", "swagger-resources", "v2", "webjars");
        if (!allowedPath.contains(baseUri)) {
            arrestRequest(request, response);
        }
    }

    private void arrestRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(403);
        handlerExceptionResolver.resolveException(
                request, response, null,
                new RuntimeException("访问地址错误", new Throwable("错误的请求地址, IP : " + getIpAddress(request) + " , uri : " + request.getRequestURI())));
    }


    private String getIpAddress(HttpServletRequest request) {
        String ip = "";
        try {
            ip = tryGetIpAddress(request);
        } catch (UnknownHostException e) {
            ip = e.getMessage();
        }
        return ip;
    }

    private String tryGetIpAddress(HttpServletRequest request) throws UnknownHostException {
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


