package xrm.extrim.planner.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.domain.Messengers;
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.domain.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import xrm.extrim.planner.configuration.ConfluenceConfig;
import xrm.extrim.planner.parser.HTMLParser;
import xrm.extrim.planner.repository.UserRepository;

@Service
@Slf4j
@SuppressWarnings({"PMD.GuardLogStatement", "PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
public class UserMigrateService {
    private final static String POSITION = "position";
    private JSONObject confluenceData;
    private final ConfluenceConfig confluenceConfig;
    private final UserRepository userRepository;
    private final PositionService positionService;
    private final DepartmentService departmentService;
    private final HTMLParser htmlParser;

    @Autowired
    public UserMigrateService(ConfluenceConfig config, UserRepository userRepository, PositionService positionService, DepartmentService departmentService, HTMLParser htmlParser) {
        this.userRepository = userRepository;
        this.positionService = positionService;
        this.departmentService = departmentService;
        this.htmlParser = htmlParser;
        this.confluenceConfig = config;
    }

    public User updateUserFromConfluence(User user) throws JSONException {
        if (confluenceData == null) {
            confluenceData = getConfluenceData();
        }
        if (user.getContact().getMessengers() == null) {
            user.getContact().setMessengers(new Messengers());
        }
        try {
            JSONObject userJson = this.confluenceData.getJSONObject(user.getContact().getEmail());
            if (user.getContact().getPhoneNumber() == null) {
                user.getContact().setPhoneNumber(userJson.getString("mobileNumber"));
            }
            if (user.getContact().getOffice() == null) {
                user.getContact().setOffice(userJson.getString("office"));
            }
            if (user.getContact().getMessengers().getSkype() == null) {
                user.getContact().getMessengers().setSkype(userJson.getString("skype"));
            }
            if (!user.isTeamLead()) {
                String position = userJson.getString(POSITION);
                if (position.contains("Team lead")) {
                    user.setTeamLead(true);
                    userJson.remove(POSITION);
                    userJson.put(POSITION, position.substring(0, position.length() - 10));
                }
            }
            if (user.getBirthday() == null) {
                user.setBirthday(parseDate(userJson.getString("birthday")));
            }
            if (user.getDepartment().getName().equals("UNDEFINED")) {
                Department department = departmentService.getByDescription(userJson.getString("department"));
                if (userJson.getString("department").contains("Команда Александра Спицына")) {
                    department = departmentService.getByName("Spitsyn_Team");
                }
                if (userJson.getString("department").contains("Команда Михаила Рягина")) {
                    department = departmentService.getByName("Ryagin_Team");
                }
                if (department != null) {
                    user.setDepartment(department);
                }
            }
            if (user.getPosition().getName().equals("UNDEFINED")) {
                Position position = positionService.getByDescription(userJson.getString(POSITION));
                if (position != null) {
                    user.setPosition(position);
                }
            }
            return this.userRepository.save(user);
        } catch (JSONException e) {
            log.info("user with login " + user.getContact().getEmail() + " not found in confluence");
        }
        return user;
    }

    public void updateAllUsersFromConfluence() throws JSONException {
        List<User> allUser = this.userRepository.findAll();
        for (User user : allUser) {
            updateUserFromConfluence(user);
        }
    }

    private JSONObject getConfluenceData() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(confluenceConfig.getConfluenceUrl(),
                HttpMethod.GET, new HttpEntity<String>(createHeaders()), String.class);
        return htmlParser.tableToJson(new JSONObject(response.getBody()).getJSONObject("body").getJSONObject("export_view").getString("value"));
    }

    private HttpHeaders createHeaders() {
        String auth = confluenceConfig.getConfluenceLogin() + ":" + confluenceConfig.getConfluencePassword();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + new String(encodedAuth));
        return headers;
    }

    private LocalDate parseDate(String dateString) {
        try {
            String month = dateString.split(" ")[0];
            int day = Integer.parseInt(dateString.split(" ")[1]);
            return LocalDate.of(2020, confluenceConfig.getMonthNumbers().get(month), day);
        } catch (Exception e) {
            return null;
        }
    }
}
