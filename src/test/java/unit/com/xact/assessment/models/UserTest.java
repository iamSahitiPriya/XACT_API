package unit.com.xact.assessment.models;

import com.xact.assessment.models.Profile;
import com.xact.assessment.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void getUserEmail() {
        Profile profile = new Profile();
        profile.setEmail("test@test.com");
        User user = new User("Active", profile);

        assertEquals(user.getUserEmail(), "test@test.com");
    }
}