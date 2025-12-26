package com.portal.studymate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled("Disabled until DB setup is ready")
class SchoolApplicationPortalApplicationTests {

	@Test
	void contextLoads() {
	}

}
