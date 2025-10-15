package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CreateLeaveRequestDTO;
import app.fichajes.fichajes.models.dtos.response.LeaveRequestResponseDTO;
import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.LeaveRequest;
import app.fichajes.fichajes.repositories.AssignmentRepository;
import app.fichajes.fichajes.repositories.LeaveRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, AssignmentRepository assignmentRepository, ModelMapper modelMapper) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.assignmentRepository = assignmentRepository;
        this.modelMapper = modelMapper;
    }

    public LeaveRequestResponseDTO createLeaveRequest(CreateLeaveRequestDTO dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con id: " + dto.getAssignmentId()));

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setAssignment(assignment);
        leaveRequest.setDateTimeStart(dto.getDateTimeStart());
        leaveRequest.setDateTimeEnd(dto.getDateTimeEnd());
        leaveRequest.setJustifyReason(dto.getJustifyReason());

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        return modelMapper.map(savedRequest, LeaveRequestResponseDTO.class);
    }

    public List<LeaveRequestResponseDTO> getAll() {
        return leaveRequestRepository.findAll().stream()
                .map(leaveRequest -> modelMapper.map(leaveRequest, LeaveRequestResponseDTO.class))
                .collect(Collectors.toList());
    }
}