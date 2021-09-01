UPDATE "user" Set position_id = 1;
UPDATE "user" Set department_id = 4;
DELETE FROM "position" WHERE id > 1;
DELETE FROM department WHERE id != 4;

alter table "user"
    add column if not exists team_lead boolean default false;
alter table "user"
    add column if not exists birthday DATE;

insert into position(id, name, description)
values
    (2, 'Junior_Analyst', 'Стажер аналитик'),
    (3, 'Middle_Analyst', 'Аналитик отдела системного и бизнес-анализа'),
    (4, 'Senior_Analyst', 'Ведущий аналитик отдела системного и бизнес-анализа'),
    (5, 'Middle_Analyst_IS', 'Аналитик отдела информационной безопасности'),
    (6, 'Accountant', 'Бухгалтер'),
    (7, 'Junior_.NET', 'Стажёр программист .Net'),
    (8, 'Middle_.NET', 'Программист .Net'),
    (9, 'Senior_.NET', 'Ведущий программист .Net'),
    (10, 'Junior_Java', 'Стажёр программист Java'),
    (11, 'Middle_Java', 'Программист Java'),
    (12, 'Senior_Java', 'Ведущий программист Java'),
    (13, 'Junior_Frontend', 'Стажёр программист Frontend'),
    (14, 'Middle_Frontend', 'Программист Frontend'),
    (15, 'Senior_Frontend', 'Ведущий программист Frontend'),
    (16, 'Escort_Group_Specialist', 'Ведущий специалист группы сопровождения прода'),
    (18, 'Junior_Tester', 'Стажер тестировщик'),
    (19, 'Middle_Tester', 'Специалист отдела тестирования и документирования'),
    (20, 'Senior_Tester', 'Ведущий специалист отдела тестирования и документирования'),
    (21, 'Lead_Tester', 'Руководитель отдела тестирования и документирования'),
    (22, 'Middle_Technical_Support', 'Специалист технической поддержки'),
    (23, 'Senior_Technical_Support', 'Ведущий специалист технической поддержки'),
    (24, 'Engineer_Technical_Support', 'Инженера группы технической поддержки и администрирования СЗИ'),
    (25, 'CEO', 'Генеральный директор'),
    (26, 'CEO_Assistant', 'Помощник генерального директора'),
    (27, 'Specialist_IS', 'Специалист отдела информационной безопасности'),
    (28, 'Engineer_IS', 'Инженер по информационной безопасности'),
    (29, 'Lead_IS', 'Главный инженер по информационной безопасности'),
    (30, 'Engineer_NS', 'Инженер группы сетевой безопасности'),
    (31, 'HR_Manager', 'Менеджер по персоналу'),
    (32, 'Sales_Manager', 'Менеджер по продажам'),
    (33, 'Account_Manager', 'Менеджер по работе с клиентами'),
    (34, 'Lead_Analytical_Team_IS', 'Руководитель аналитической группы по ИБ'),
    (35, 'Lead_group_Implementation_And_Maintenance_IS_Tools', 'Руководитель группы внедрения и сопровождения средств защиты информации'),
    (36, 'Lead_Corporate_Sales_Team', 'Руководитель группы корпоративных продаж'),
    (37, 'Lead_Network_Security_Team', 'Руководитель группы сетевой безопасности'),
    (38, 'Lead_Support_Team_IS', 'Руководитель группы сопровождения продаж отдела ИБ'),
    (39, 'Lead_Technical_Support_Team', 'Руководитель группы технической поддержки и администрирования СЗИ'),
    (40, 'Lead_Testing_Group', 'Руководитель отдела тестирования и документирования'),
    (41, 'Lead_IS_Department', 'Руководитель направления ИБ'),
    (42, 'Lead_HR_Department', 'Руководитель отдела персонала'),
    (43, 'Lead_Development_Team', 'Руководитель разработки'),
    (44, 'Lead_IS_Support_Service_Team', 'Руководитель службы сопровождения ИС'),
    (45, 'Lead_Technical_Support', 'Руководитель службы технической поддержки'),
    (46, 'Lead_System_Business_Analysis', 'Руководитель отдела системного и бизнес-анализа'),
    (47, 'Project_manager', 'Руководитель проектов'),
    (48, 'System_Administrator', 'Системный администратор'),
    (49, 'Tender_Specialist', 'Специалист по тендерам'),
    (50, 'Technical_Director', 'Технический директор');

insert into department(id, name, description)
VALUES
    (1, 'Personnel_Department', 'Отдел персонала'),
    (2, 'Development', 'Отдел разработки'),
    (3, 'IS', 'Информационная безопасность'),
    (5, 'Sales_Department', 'Отдел продаж'),
    (6, 'System_Business_Analysis', 'Отдел системного и бизнес-анализа'),
    (7, 'Testing_Department', 'Отдел тестирования и документирования'),
    (8, 'IS_Support_Service', 'Служба сопровождения ИС'),
    (9, 'Technical_Support', 'Техническая поддержка'),
    (10, 'Spitsyn_Team', 'Отдел разработки (Команда Александра Спицына)'),
    (11, 'Ryagin_Team', 'Отдел разработки (Команда Михаила Рягина)'),
    (12, 'Administrator', 'Администраторы');
