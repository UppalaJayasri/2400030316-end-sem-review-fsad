package com.indianheritage.app.config;

import com.indianheritage.app.entity.Place;
import com.indianheritage.app.entity.User;
import com.indianheritage.app.entity.UserRole;
import com.indianheritage.app.repository.PlaceRepository;
import com.indianheritage.app.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Value("${app.default-admin.username}")
    private String defaultAdminUsername;

    @Value("${app.default-admin.email}")
    private String defaultAdminEmail;

    @Value("${app.default-admin.password}")
    private String defaultAdminPassword;

    @Bean
    CommandLineRunner seedData(
        PlaceRepository placeRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            seedDefaultAdmin(userRepository, passwordEncoder);
            seedPlaces(placeRepository);
        };
    }

    private void seedDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        String normalizedEmail = defaultAdminEmail.trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            return;
        }

        User adminUser = new User(
            defaultAdminUsername.trim(),
            normalizedEmail,
            passwordEncoder.encode(defaultAdminPassword),
            UserRole.ADMIN
        );

        userRepository.save(adminUser);
    }

    private void seedPlaces(PlaceRepository placeRepository) {
        List<Place> places = List.of(
            new Place(
                "Taj Mahal", "Agra", "Uttar Pradesh",
                "UNESCO World Heritage marble mausoleum commissioned by Shah Jahan.",
                "Dharmapuri, Forest Colony, Tajganj, Agra, Uttar Pradesh 282001",
                "6:00 AM - 6:30 PM (Closed on Fridays)",
                "Indian: INR 50 | Foreign: INR 1100",
                "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Red Fort", "Delhi", "Delhi",
                "Historic Mughal fort and ceremonial center of Old Delhi.",
                "Netaji Subhash Marg, Lal Qila, Chandni Chowk, New Delhi, Delhi 110006",
                "9:30 AM - 4:30 PM (Closed on Mondays)",
                "Indian: INR 35 | Foreign: INR 550",
                "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Hawa Mahal", "Jaipur", "Rajasthan",
                "Five-storey pink sandstone palace known as the Palace of Winds.",
                "Hawa Mahal Rd, Badi Choupad, J.D.A. Market, Pink City, Jaipur, Rajasthan 302002",
                "9:00 AM - 5:00 PM",
                "Indian: INR 50 | Foreign: INR 200",
                "https://images.unsplash.com/photo-1477587458883-47145ed94245?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Mysore Palace", "Mysuru", "Karnataka",
                "Royal residence famous for Indo-Saracenic architecture and night illumination.",
                "Sayyaji Rao Rd, Agrahara, Chamrajpura, Mysuru, Karnataka 570001",
                "10:00 AM - 5:30 PM",
                "Indian: INR 100 | Foreign: INR 400",
                "https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Golden Temple", "Amritsar", "Punjab",
                "Holiest Sikh shrine, known for spiritual ambience and gold-plated sanctum.",
                "Golden Temple Rd, Atta Mandi, Katra Ahluwalia, Amritsar, Punjab 143006",
                "Open 24 Hours",
                "Entry Free",
                "https://images.unsplash.com/photo-1605649487212-47bdab064df7?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Gateway of India", "Mumbai", "Maharashtra",
                "Iconic waterfront arch monument overlooking the Arabian Sea.",
                "Apollo Bandar, Colaba, Mumbai, Maharashtra 400001",
                "Open 24 Hours",
                "Entry Free",
                "https://images.unsplash.com/photo-1595658658481-d53d3f999875?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Ajanta Caves", "Aurangabad", "Maharashtra",
                "Ancient Buddhist cave monuments featuring rock-cut caves dating from 2nd century BCE.",
                "Ajanta Caves Rd, Ajanta, Maharashtra 431117",
                "9:00 AM - 5:30 PM (Closed on Mondays)",
                "Indian: INR 40 | Foreign: INR 600",
                "https://images.unsplash.com/photo-1626331915556-c4f817fbcc5d?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Ellora Caves", "Aurangabad", "Maharashtra",
                "Remarkable archaeological site featuring 34 monasteries and temples carved into mountainside.",
                "Ellora Caves Rd, Ellora, Maharashtra 431102",
                "6:00 AM - 6:00 PM (Closed on Tuesdays)",
                "Indian: INR 40 | Foreign: INR 600",
                "https://images.unsplash.com/photo-1709739322415-87135f0d34bd?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Hampi Ruins", "Hampi", "Karnataka",
                "The ruins of Vijayanagara Empire featuring magnificent temples and royal structures.",
                "Hampi, Karnataka 583239",
                "6:00 AM - 6:00 PM",
                "Indian: INR 40 | Foreign: INR 600",
                "https://images.unsplash.com/photo-1631986683754-7d511e03864d?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Konark Sun Temple", "Konark", "Odisha",
                "13th-century temple dedicated to the Sun god, designed as a massive chariot.",
                "Konark, Odisha 752111",
                "6:00 AM - 8:00 PM",
                "Indian: INR 40 | Foreign: INR 600",
                "https://images.unsplash.com/photo-1677211352662-30e7775c7ce8?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Charminar", "Hyderabad", "Telangana",
                "Iconic monument and mosque built in 1591, the global symbol of Hyderabad.",
                "Charminar Rd, Char Kaman, Ghansi Bazaar, Hyderabad, Telangana 500002",
                "9:00 AM - 5:30 PM",
                "Indian: INR 25 | Foreign: INR 300",
                "https://images.unsplash.com/photo-1648644719956-2594a709a512?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Victoria Memorial", "Kolkata", "West Bengal",
                "Majestic marble building dedicated to Queen Victoria, now a colonial history museum.",
                "Victoria Memorial Hall, 1, Queens Way, Maidan, Kolkata, West Bengal 700071",
                "10:00 AM - 5:00 PM (Closed on Mondays)",
                "Indian: INR 30 | Foreign: INR 500",
                "https://images.unsplash.com/photo-1697817665440-f988c6d5080f?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Khajuraho Temples", "Khajuraho", "Madhya Pradesh",
                "Group of Hindu and Jain temples famous for Nagara-style architecture and sculptures.",
                "Khajuraho, Madhya Pradesh 471606",
                "Sunrise to Sunset",
                "Indian: INR 40 | Foreign: INR 600",
                "https://images.unsplash.com/photo-1681181753651-315b07b2b2de?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Amer Fort", "Jaipur", "Rajasthan",
                "Magnificent hilltop fort palace complex known for its artistic Hindu elements.",
                "Devisinghpura, Amer, Jaipur, Rajasthan 302001",
                "8:00 AM - 5:30 PM",
                "Indian: INR 100 | Foreign: INR 500",
                "https://images.unsplash.com/photo-1599661046827-dacff0c0f09a?auto=format&fit=crop&w=1600&q=80"
            ),
            new Place(
                "Qutub Minar", "Delhi", "Delhi",
                "Towering 73-meter tall minaret built of red sandstone and marble.",
                "Seth Sarai, Mehrauli, New Delhi, Delhi 110030",
                "7:00 AM - 5:00 PM",
                "Indian: INR 35 | Foreign: INR 500",
                "https://images.unsplash.com/photo-1667849521021-86eec155bfac?auto=format&fit=crop&w=1600&q=80"
            )
        );

        for (Place place : places) {
            if (!placeRepository.existsByName(place.getName())) {
                placeRepository.save(place);
            }
        }
    }
}
