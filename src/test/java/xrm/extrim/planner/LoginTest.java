package xrm.extrim.planner;

import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.markers.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Category(IntegrationTest.class)
public class LoginTest extends PlannerTestBase {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accessDeniedTest() {
        try {
            this.mockMvc.perform(get("/"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            throw new PlannerException(e, e.getMessage());
        }
    }

    @Test
    public void loginForbiddenTest() {
        try {
            this.mockMvc.perform(post("/api/login").param("username","Anosov").param( "password", "neJoker"))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            throw new PlannerException(e, e.getMessage());
        }
    }
}