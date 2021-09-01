package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xrm.extrim.planner.parser.ExcelParser;
import xrm.extrim.planner.service.UserMigrateService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/export")
@Api(description = "Operations pertaining to import excel files")
public class ImportExportController {
    private final ExcelParser excelParser;
    private UserMigrateService userMigrateService;

    @Autowired
    public ImportExportController(ExcelParser excelParser, UserMigrateService userMigrateService) {
        this.excelParser = excelParser;
        this.userMigrateService = userMigrateService;
    }

    @PreAuthorize("hasRole('importSkills')")
    @PostMapping("skill")
    public void importSkill(@RequestParam("file") MultipartFile file) throws IOException {
        excelParser.updateSkillFromWorkbook(file.getInputStream());
    }

    @GetMapping("skill")
    public void exportSkillXlsx(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (XSSFWorkbook workbook = excelParser.createSkillWorkbook()) {
            response.setContentType("text/xlsx");
            response.setHeader("Content-disposition", "attachment;filename=" + "skills.xlsx");
            try (OutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
            }
        }
    }

    @PostMapping("user")
    public void importUsers() throws JSONException {
        this.userMigrateService.updateAllUsersFromConfluence();
    }

    @GetMapping("user")
    public void exportAllUserXlsx(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (XSSFWorkbook workbook = excelParser.createAllUsersWorkbook()) {
            response.setContentType("text/xlsx");
            response.setHeader("Content-disposition", "attachment;filename=" + "users.xlsx");
            try (OutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
            }
        }
    }

    @GetMapping("user/{id}")
    public void exportUserByIdXlsx(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (XSSFWorkbook workbook = excelParser.createUserWorkbook(id)) {
            response.setContentType("text/xlsx");
            response.setHeader("Content-disposition", "attachment;filename=" + "users.xlsx");
            try (OutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
            }
        }
    }
}
