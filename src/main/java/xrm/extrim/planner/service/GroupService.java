package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.domain.Group;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.repository.GroupRepository;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group getById(Long id) {
        return groupRepository.getById(id);
    }

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public Group getByName(String name) {
        return groupRepository.findByName(name);
    }

    public Group createGroup(Group group) {
        if (groupRepository.existsByName(group.getName())) {
            throw new PlannerException(String.format(Exception.GROUP_ALREADY_EXISTS.getDescription(), group.getName()));
        }
        return groupRepository.save(group);
    }

    public Group update(long id, String name) {
        Group group = groupRepository.getById(id);
        group.setName(name);
        return groupRepository.save(group);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }
}
