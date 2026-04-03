package com.portal.studymate.config;

import com.portal.studymate.auth.model.User;
import com.portal.studymate.common.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds MongoDB with test users on application startup (local profile only).
 * Only inserts if users don't already exist.
 */
@Component
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class MongoDataSeeder implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Use school CODE (not numeric ID) — JwtAuthFilter looks up by schoolCode
        String schoolCode = "SPS001";
        seedUser("admin@sps.edu", "Admin User", Role.ADMIN, schoolCode, "9876543200");
        seedUser("priya.sharma@sps.edu", "Priya Sharma", Role.TEACHER, schoolCode, "9876543201");
        seedUser("rajesh.kumar@sps.edu", "Rajesh Kumar", Role.TEACHER, schoolCode, "9876543202");
        seedUser("anita.verma@sps.edu", "Anita Verma", Role.TEACHER, schoolCode, "9876543203");
        seedUser("suresh.patel@sps.edu", "Suresh Patel", Role.TEACHER, schoolCode, "9876543204");
        seedUser("meena.gupta@sps.edu", "Meena Gupta", Role.TEACHER, schoolCode, "9876543205");
        seedUser("student1@sps.edu", "Aarav Patel", Role.STUDENT, schoolCode, "9876500001");
        log.info("MongoDB seed complete. Login: admin@sps.edu / Admin@123");
    }

    private void seedUser(String email, String fullName, Role role, String schoolId, String phone) {
        Query query = new Query(Criteria.where("email").is(email));
        if (mongoTemplate.exists(query, User.class)) {
            // Update schoolId if it's wrong
            var existing = mongoTemplate.findOne(query, User.class);
            if (existing != null && !schoolId.equals(existing.getSchoolId())) {
                existing.setSchoolId(schoolId);
                mongoTemplate.save(existing);
                log.info("Updated schoolId for user: {} to {}", email, schoolId);
            } else {
                log.debug("User {} already exists with correct schoolId", email);
            }
            return;
        }

        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode("Admin@123"))
            .role(role)
            .schoolId(schoolId)
            .enabled(true)
            .forcePasswordChange(false)
            .fullName(fullName)
            .phoneNumber(phone)
            .build();

        mongoTemplate.save(user);
        log.info("Seeded user: {} ({})", email, role);
    }
}
