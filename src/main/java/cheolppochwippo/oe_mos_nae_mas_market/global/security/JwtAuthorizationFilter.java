package cheolppochwippo.oe_mos_nae_mas_market.global.security;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.global.util.JwtUtil;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String tokenValue = jwtUtil.resolveToken(request);
		if (StringUtils.hasText(tokenValue)) {
			try {
				Date date = new Date();
				if (jwtUtil.getMemberInfoFromExpiredToken(tokenValue).getExpiration()
					.compareTo(date)
					< 0) {
					String token = jwtUtil.validateRefreshToken(
						jwtUtil.getMemberInfoFromExpiredToken(tokenValue).get("userId",
							Long.class));
					response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
					ObjectNode json = new ObjectMapper().createObjectNode();
					json.put("message", "새로운 토큰이 발급되었습니다.                          ");
					String newResponse = new ObjectMapper().writeValueAsString(json);
					response.setContentType("application/json");
					response.setContentLength(newResponse.length());
					response.getOutputStream().write(newResponse.getBytes());
					return;
				}
				Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
				setAuthentication(info);
			} catch (SecurityException | MalformedJwtException | SignatureException e) {
				log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
				return;
			} catch (ExpiredJwtException e) {
				log.error("Expired JWT token, 만료된 JWT token 입니다.");
				return;
			} catch (UnsupportedJwtException e) {
				log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
				return;
			} catch (IllegalArgumentException | NullPointerException e) {
				log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
				return;
			} catch (Exception e) {
				log.error(e.getMessage());
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	// 인증 처리
	public void setAuthentication(Claims info) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(info);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(Claims info) {
		User user = new User(info.get("userId", Long.class), info.get("username", String.class));
		UserDetails userDetails = new UserDetailsImpl(user);
		return new CustomAuthentication(userDetails);
	}
}
