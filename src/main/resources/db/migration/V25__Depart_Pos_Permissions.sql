alter table role
add column if not exists department_operations boolean default false;

alter table role
add column if not exists position_operations boolean default false;

update role
set department_operations = true,
    position_operations = true
where name = 'ADMIN';

update role
set department_operations = false,
    position_operations = false
where name = 'USER';
