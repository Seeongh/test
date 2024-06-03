package com._3o3.demo.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

//시큐리티 동작 전 필터
public class myFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("마이 필터");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //로그인 완료시 토큰 만들어서 응답,
        //요청할때마다 Authorization 에 토큰 검증 (RSA, HS256)
        if(req.getMethod().equals("POST")) {
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            if(headerAuth.equals("COS")) {
                chain.doFilter(request, response);

            }
            else {
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");
            }
        }

    }
}
