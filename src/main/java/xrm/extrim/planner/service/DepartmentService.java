package xrm.extrim.planner.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.repository.DepartmentRepository;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department getById(Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if(department == null) {
            throw new EntityNotFoundException(String.format(Exception.DEPARTMENT_NOT_FOUND.getDescription(), id));
        }

        return department;
    }

    public Department getByName(String name) {
        Department department = departmentRepository.findByName(name);
        if(department == null) {
            throw new EntityNotFoundException(String.format(Exception.DEPARTMENT_NOT_FOUND.getDescription(), name));
        }

        return department;
    }

    public Department getByDescription(String description) {
        return departmentRepository.findByDescription(description);
    }

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public Department create(Department department) {
        if(departmentRepository.existsByName(department.getName())) {
            throw new PlannerException(String.format(Exception.DEPARTMENT_ALREADY_EXIST.getDescription(),
                    department.getName()));
        }
        return departmentRepository.save(department);
    }

    public Department update(Long id, Department department) {
        Department departmentDb = getById(id);
        if(!departmentDb.getName().equals(department.getName()) && departmentRepository.existsByName(department.getName())) {
            throw new PlannerException(String.format(Exception.DEPARTMENT_ALREADY_EXIST.getDescription(),
                    department.getName()));
        }
        BeanUtils.copyProperties(department, departmentDb, "id", "users");
        return departmentDb;
    }

    public void delete(Long id) {
        Department department = getById(id);
        departmentRepository.delete(department);
    }
}
