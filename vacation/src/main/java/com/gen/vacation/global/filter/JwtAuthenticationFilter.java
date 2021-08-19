package com.gen.vacation.global.filter;

import com.gen.vacation.global.contant.ErrorContant;
import com.gen.vacation.global.common.ErrorResult;
import com.gen.vacation.global.config.JwtTokenConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-05-19
 * Time: 오후 8:32
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenConfig jwtTokenConfig;

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            Map<String, Object> result = jwtTokenConfig.getToken((HttpServletRequest) request);
            //server 인지 client인지 비교

            String type = result.get("type") + "";

            String token = result.get("token") + "";
            //server
            if (Jwts.parser().isSigned(token) && jwtTokenConfig.validateToken(token)) {

                    if (type.equals("admin")) {

                        Authentication auth = jwtTokenConfig.getAuthentication(token);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                    //user
                    else {

                        Authentication auth = jwtTokenConfig.getUserAuthentication(token);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }


            }

        } catch (ExpiredJwtException e) {

            ErrorResult errorResult = new ErrorResult();
            errorResult.setCode(ErrorContant.EXF_TOKEN);
            errorResult.setMessage(ErrorContant.getMessage(errorResult.getCode()));
            errorResult.setError("Expired token");
            request.setAttribute("token", errorResult);

        }

        chain.doFilter(request, response);
    }
}
