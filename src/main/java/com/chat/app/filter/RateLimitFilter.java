package com.chat.app.filter;


import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class RateLimitFilter implements Filter {

    private final Bucket bucket;

    public RateLimitFilter(
            Bucket bucket
    ){

        this.bucket=bucket;

    }
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain

    ) throws IOException, ServletException {



        if(bucket.tryConsume(1)){


            chain.doFilter(
                    request,
                    response
            );


        }
        else{


            HttpServletResponse res =
                    (HttpServletResponse) response;


            res.setStatus(429);


            res.getWriter()
                    .write(
                            "Too many requests. Try later."
                    );
        }


    }


}