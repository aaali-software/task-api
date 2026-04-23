package com.aziz.taskapi.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> loginBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> apiBuckets = new ConcurrentHashMap<>();

    private Bucket newLoginBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket newApiBucket() {
        Bandwidth limit = Bandwidth.classic(60, Refill.intervally(60, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Clears in-memory buckets so integration tests can start from a clean state.
     */
    public void clearBuckets() {
        loginBuckets.clear();
        apiBuckets.clear();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String ip = request.getRemoteAddr();

        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        Bucket bucket;

        if ("/api/auth/login".equals(path)) {
            bucket = loginBuckets.computeIfAbsent(ip, key -> newLoginBucket());
        } else {
            bucket = apiBuckets.computeIfAbsent(ip, key -> newApiBucket());
        }

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }

        log.warn("Rate limit exceeded for ip={} path={}", ip, path);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
                {
                  "status": 429,
                  "error": "Too Many Requests",
                  "message": "Rate limit exceeded. Please try again later."
                }
                """);
    }
}
