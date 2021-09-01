package xrm.extrim.planner.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xrm.extrim.planner.controller.dto.SkillDto;
import xrm.extrim.planner.domain.Group;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.UserSkill;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.RateDescriptionMapper;
import xrm.extrim.planner.service.GroupService;
import xrm.extrim.planner.service.SkillService;
import xrm.extrim.planner.service.UserService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "PMD.AvoidCatchingNPE"})
public class ExcelParser {
    private final static String HEADER_CHECK_CELL_TEXT = "Группа";
    private final GroupService groupService;
    private final SkillService skillService;
    private final UserService userService;
    private final RateDescriptionMapper rateDescriptionMapper;
    @Autowired
    public ExcelParser(GroupService groupService, SkillService skillService, UserService userService,
                       RateDescriptionMapper rateDescriptionMapper) {
        this.groupService = groupService;
        this.skillService = skillService;
        this.userService = userService;
        this.rateDescriptionMapper = rateDescriptionMapper;
    }

    public void updateSkillFromWorkbook(InputStream inputStream) throws PlannerException {
        try {
            Sheet sheet;
            try (Workbook wb = WorkbookFactory.create(inputStream)) {
                sheet = wb.getSheetAt(0);
            }
            Iterator<Row> rowIter = sheet.rowIterator();

            if(sheet.getRow(0).getCell(0).getRichStringCellValue().getString().equals(HEADER_CHECK_CELL_TEXT)) {
                rowIter.next();
            }

            Group group = null;
            while (rowIter.hasNext()) {
                Row row = rowIter.next();
                Cell cell = row.getCell(0);
                if(cell != null) {
                    group = parseGroup(cell);
                }
                cell = row.getCell(1);
                if(cell != null) {
                    Skill skill = skillService.getSkillByName(cell.getRichStringCellValue().getString());
                    SkillDto skillDto = parseSkill(row);
                    skillDto.setGroupId(group.getId());
                    if(skill == null) {
                        skillService.createSkill(skillDto);

                    } else {
                        skillService.updateSkill(skill.getId(), skillDto);
                    }
                }
            }
        }
        catch (IOException e) {
            throw new PlannerException(e, e.getMessage());
        }
    }

    private Group parseGroup(Cell cell) {
        Group group = groupService.getByName(cell.getRichStringCellValue().getString());
        if(group == null) {
            group = new Group();
            group.setName(cell.getRichStringCellValue().getString());
            group = groupService.createGroup(group);
        }
        return group;
    }

    private SkillDto parseSkill(Row row) {
        Cell cell = row.getCell(1);
        if(cell != null) {
            SkillDto skillDescriptions = new SkillDto();
            skillDescriptions.setName(cell.getRichStringCellValue().getString());
            cell = row.getCell(2);
            if (cell != null) {
                skillDescriptions.setDescription(cell.getRichStringCellValue().getString());
            }
            skillDescriptions.setRateDescriptions(
                    rateDescriptionMapper.listRateDescriptionsToListRateDescriptionDtos(parseRateDescription(row)));
            return skillDescriptions;
        } else {
            return null;
        }
    }

    private List<RateDescription> parseRateDescription(Row row) {
        List<RateDescription> descriptions = new ArrayList<>();
        for(int i = 3;i < 8;i++) {
            Cell cell = row.getCell(i);
            RateDescription description = new RateDescription();
            description.setDescription(cell.getRichStringCellValue().getString());
            description.setRateNumber(i-2);
            descriptions.add(description);
        }
        return descriptions;
    }

    public XSSFWorkbook createSkillWorkbook() {
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("Группа");
        row.createCell(1).setCellValue("Навык");
        row.createCell(2).setCellValue("Описание");
        row.createCell(3).setCellValue("*");
        row.createCell(4).setCellValue("**");
        row.createCell(5).setCellValue("***");
        row.createCell(6).setCellValue("****");
        row.createCell(7).setCellValue("******");

        for(Group group: groupService.getAll()) {
            rowNum++;
            row = sheet.createRow(rowNum);

            for(Skill skill: group.getSkills()) {
                Cell cell = row.createCell(0);
                cell.setCellValue(group.getName());
                cell = row.createCell(1);
                cell.setCellValue(skill.getName());
                cell = row.createCell(2);
                cell.setCellValue(skill.getDescription());

                for(int i = 3; i < 8; i++) {
                    cell = row.createCell(i);
                    final int j = i-2;
                    RateDescription rateDescription = skill.getRateDescriptions().stream().filter(rate -> rate.getRateNumber() == j).findFirst().orElse(new RateDescription());
                    cell.setCellValue(rateDescription.getDescription());
                }
                row = sheet.createRow(++rowNum);
            }
        }
        return wb;
    }


    private XSSFWorkbook initUserWorkbook() {
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Сотрудник");
        row.createCell(1).setCellValue("Навык");
        row.createCell(2).setCellValue("Оценка");
        row.createCell(3).setCellValue("Подтвержденно");
        return wb;
    }

    public XSSFWorkbook createAllUsersWorkbook() {
        XSSFWorkbook wb = initUserWorkbook();
        Sheet sheet = wb.getSheetAt(0);
        int rowNum = 0;

        for(User user : userService.getUsers()) {
            rowNum++;
            Row row = sheet.createRow(rowNum);
            Cell cell = row.createCell(0);
            if(user.getUserSkills().isEmpty()) {
                cell.setCellValue(user.getName() + " " + user.getSurname());
            }
            for(UserSkill userSkill : user.getUserSkills()) {
                cell = row.createCell(0);
                cell.setCellValue(user.getName() + " " + user.getSurname());
                cell = row.createCell(1);
                cell.setCellValue(userSkill.getSkill().getName());
                cell = row.createCell(2);
                cell.setCellValue(userSkill.getRate());
                cell = row.createCell(3);
                if(userSkill.isConfirmed()) {
                    cell.setCellValue("+");
                }
                row = sheet.createRow(++rowNum);
            }
        }
        return wb;
    }

    public XSSFWorkbook createUserWorkbook(Long userId) {
        XSSFWorkbook wb = initUserWorkbook();
        Sheet sheet = wb.getSheetAt(0);
        int rowNum = 1;
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0);
        User user = userService.getUser(userId);
        if(user.getUserSkills().isEmpty()) {
            cell.setCellValue(user.getName() + " " + user.getSurname());
        }
        for(UserSkill userSkill : user.getUserSkills()) {
            cell = row.createCell(0);
            cell.setCellValue(user.getName() + " " + user.getSurname());
            cell = row.createCell(1);
            cell.setCellValue(userSkill.getSkill().getName());
            cell = row.createCell(2);
            cell.setCellValue(userSkill.getRate());
            cell = row.createCell(3);
            if(userSkill.isConfirmed()) {
                cell.setCellValue("+");
            }

            row = sheet.createRow(++rowNum);
        }
        return wb;
    }
}
