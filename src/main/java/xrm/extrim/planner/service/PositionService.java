package xrm.extrim.planner.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.repository.PositionRepository;

import java.util.List;

@Service
public class PositionService {
    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Position getById(Long id) {
        Position position = positionRepository.findById(id).orElse(null);
        if(position == null) {
            throw new EntityNotFoundException(String.format(Exception.POSITION_NOT_FOUND.getDescription(), id));
        }

        return position;
    }

    public Position getByName(String name) {
        Position position = positionRepository.findByName(name);
        if(position == null) {
            throw new EntityNotFoundException(String.format(Exception.POSITION_NOT_FOUND.getDescription(), name));
        }

        return position;
    }

    public Position getByDescription(String description) {
        return positionRepository.findByDescription(description);
    }

    public List<Position> getAll() {
        return positionRepository.findAll();
    }

    public Position create(Position position) {
        if(positionRepository.existsByName(position.getName())) {
            throw new PlannerException(String.format(Exception.POSITION_ALREADY_EXIST.getDescription(),
                    position.getName()));
        }
        return positionRepository.save(position);
    }

    public Position update(Long id, Position position) {
        Position positionDb = getById(id);
        if(!positionDb.getName().equals(position.getName()) && positionRepository.existsByName(position.getName())) {
            throw new PlannerException(String.format(Exception.POSITION_ALREADY_EXIST.getDescription(),
                    position.getName()));
        }

        BeanUtils.copyProperties(position, positionDb, "id", "users");
        return positionRepository.save(positionDb);
    }

    public void delete(Long id) {
        Position position = getById(id);
        positionRepository.delete(position);
    }
}
