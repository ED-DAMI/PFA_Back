package org.dami.pfa_back.config; // Ou org.dami.pfa_back.security, adaptez selon votre structure

import org.dami.pfa_back.Security.JwtUtil; // Importez votre JwtUtil
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Component // Important pour que Spring le gère comme un bean
public class JwtAuthChannelInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthChannelInterceptor.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Utilisez @Lazy pour UserDetailsService si vous avez une dépendance circulaire
    // avec la configuration de sécurité principale.
    @Autowired
    public JwtAuthChannelInterceptor(JwtUtil jwtUtil, @Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        logger.trace("INTERCEPTOR: preSend invoked for STOMP command: {}", accessor != null ? accessor.getCommand() : "UNKNOWN");

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            logger.info("INTERCEPTOR: STOMP CONNECT command detected.");

            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            String jwtToken = null;
            logger.debug("INTERCEPTOR: Native Authorization Headers: {}", authorizationHeaders);

            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                String bearerToken = authorizationHeaders.get(0); // Prendre le premier header Auth
                if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                    jwtToken = bearerToken.substring(7);
                    logger.info("INTERCEPTOR: Extracted JWT: {}", jwtToken);
                } else {
                    logger.warn("INTERCEPTOR: Authorization header found but not a Bearer token: {}", bearerToken);
                }
            } else {
                logger.warn("INTERCEPTOR: No Authorization header found in STOMP CONNECT.");
            }

            if (jwtToken != null && jwtUtil.validateToken(jwtToken)) {
                logger.info("INTERCEPTOR: JWT token is valid.");
                // Décidez quel identifiant utiliser: email ou userId
                // String identifier = jwtUtil.extractEmail(jwtToken);
                String identifier = jwtUtil.extractUserId(jwtToken); // Supposons que vous utilisez userId
                logger.info("INTERCEPTOR: Identifier (userId) from JWT: {}", identifier);

                try {
                    // Votre UserDetailsService doit pouvoir charger par cet 'identifier'
                    // Si loadUserByUsername s'attend à un email, et que identifier est un userId, cela échouera.
                    // Adaptez cette ligne ou votre UserDetailsService.
                    UserDetails userDetails = userDetailsService.loadUserByUsername(identifier);
                    logger.info("INTERCEPTOR: UserDetails loaded for identifier '{}': {}", identifier, userDetails.getUsername());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, // Le Principal
                            null,        // Credentials
                            userDetails.getAuthorities() // Authorities
                    );

                    logger.info("INTERCEPTOR: Attempting to set user on accessor. Authentication Principal: {}", authentication.getPrincipal());
                    accessor.setUser(authentication); // C'est L'ÉTAPE CRUCIALE
                    Principal userSet = accessor.getUser();
                    logger.info("INTERCEPTOR: User set on accessor: {}. Is User null? {}",
                            userSet != null ? userSet.getName() : "NULL",
                            userSet == null);

                } catch (UsernameNotFoundException e) {
                    logger.warn("INTERCEPTOR: User not found for identifier '{}' from token. Authentication failed. Error: {}", identifier, e.getMessage());
                } catch (Exception e) {
                    logger.error("INTERCEPTOR: Error during UserDetails loading or Authentication creation for identifier '{}': {}", identifier, e.getMessage(), e);
                }
            } else {
                if (jwtToken == null) {
                    logger.warn("INTERCEPTOR: Authentication failed: No JWT token provided or extracted from headers.");
                } else {
                    logger.warn("INTERCEPTOR: Authentication failed: Invalid JWT token provided.");
                }
                // Si le token n'est pas valide, accessor.getUser() restera null.
                // Les destinations sécurisées pourraient alors rejeter l'accès.
            }
        } else if (accessor != null && accessor.getUser() != null) {
            // Pour les autres messages d'un utilisateur déjà authentifié
            logger.trace("INTERCEPTOR: Authenticated user '{}' performing STOMP command {} to destination {}",
                    Objects.requireNonNull(accessor.getUser()).getName(), accessor.getCommand(), accessor.getDestination());
        } else if (accessor != null) {
            // Pour les autres messages d'un utilisateur non encore authentifié (ou dont l'auth a échoué)
            logger.trace("INTERCEPTOR: Unauthenticated session performing STOMP command {} to destination {}",
                    accessor.getCommand(), accessor.getDestination());
        }
        return message;
    }
}
