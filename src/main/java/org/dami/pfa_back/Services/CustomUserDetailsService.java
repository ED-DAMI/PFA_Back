package org.dami.pfa_back.Services;




import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Pour les autorités vides si vous ne gérez pas les rôles

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepo userRepository; // Injectez votre repository User

    public CustomUserDetailsService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Ici, 'identifier' est ce que vous avez extrait du JWT (email ou userId)
        // Adaptez la logique de recherche en fonction de ce qu'est 'identifier'

        User user;
        // Si 'identifier' est l'email:
        // user = userRepository.findByEmail(identifier)
        //         .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + identifier));

        // Si 'identifier' est l'userId (et que c'est une String dans le token, mais un Long dans l'entité):
        try {
            String userId = identifier;
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + identifier));
        } catch (NumberFormatException e) {
            // Si l'identifier n'est pas un Long, essayez par email ou un autre champ unique
            user = userRepository.findByEmail(identifier) // Exemple de fallback si l'ID n'est pas numérique
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier));
        }


        // Créez un objet UserDetails de Spring Security
        // Pour un exemple simple, sans rôles/autorisations complexes :
        return new org.springframework.security.core.userdetails.User(
                // Ce qui sera retourné par principal.getName() dépend de ce que vous passez ici.
                // Souvent, c'est l'email ou un nom d'utilisateur unique.
                // Si vous avez utilisé userId pour charger, vous pourriez vouloir que principal.getName() retourne userId.
                user.getId().toString(), // Ou user.getEmail(), ou un autre champ unique comme nom d'utilisateur pour Spring Security
                user.getPassword(), // Même si vous utilisez JWT, UserDetails le requiert souvent. Peut être une chaîne vide si non utilisé.
                new ArrayList<>() // Liste des autorités (rôles). Vide pour cet exemple.
        );

        // Si vous avez des rôles :
        // List<GrantedAuthority> authorities = user.getRoles().stream()
        //       .map(role -> new SimpleGrantedAuthority(role.getName()))
        //       .collect(Collectors.toList());
        // return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
