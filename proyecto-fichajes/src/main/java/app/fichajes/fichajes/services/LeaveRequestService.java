package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.GenericException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.LeaveRequestDTO;
import app.fichajes.fichajes.models.dtos.response.LeaveRequestResponseDTO;
import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.models.entities.LeaveRequest;
import app.fichajes.fichajes.models.enums.LeaveRequestState;
import app.fichajes.fichajes.repositories.AssignmentRepository;
import app.fichajes.fichajes.repositories.LeaveRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public LeaveRequestResponseDTO createLeaveRequest(LeaveRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // 2. BUSCAR ASIGNACIÓN Y VALIDAR PERTENENCIA
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + dto.getAssignmentId()));

        if (!assignment.getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new AccessDeniedException("You can only create leave requests for your own assignments.");
        }

        // 3. VALIDAR LÓGICA DE FECHAS
        if (dto.getDateTimeStart().isAfter(dto.getDateTimeEnd()) || dto.getDateTimeStart().isEqual(dto.getDateTimeEnd())) {
            throw new GenericException("The start date must be before the end date.");
        }

        // 4. VALIDAR SOLAPAMIENTOS
        List<LeaveRequest> overlapping = leaveRequestRepository.findOverlappingRequests(
                assignment,
                dto.getDateTimeStart(),
                dto.getDateTimeEnd()
        );

        if (!overlapping.isEmpty()) {
            throw new FieldDataAlreadyExistsException("You already have a leave request that overlaps with these dates.");
        }

        // 5. MAPEAR Y GUARDAR
        LeaveRequest leaveRequest = new LeaveRequest();
        modelMapper.map(dto, leaveRequest);
        leaveRequest.setAssignment(assignment);

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        return modelMapper.map(savedRequest, LeaveRequestResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestResponseDTO> getAll() {
        return leaveRequestRepository.findAll().stream()
                .map(leaveRequest -> modelMapper.map(leaveRequest, LeaveRequestResponseDTO.class))
                .toList();
    }

    @Transactional
    public void approveLeaveRequest(Long requestId, Long assignmentId) {
        LeaveRequest leaveRequest = getLeaveRequest(requestId, assignmentId);
        leaveRequest.setLeaveRequestState(LeaveRequestState.APPROVED);
    }

    @Transactional
    public void rejectLeaveRequest(Long requestId, Long assignmentId) {
        LeaveRequest leaveRequest = getLeaveRequest(requestId, assignmentId);
        leaveRequest.setLeaveRequestState(LeaveRequestState.REJECTED);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestResponseDTO> getPendingApproveLeaveRequests(Long assignmentId) {
        Assignment managerAssignment = getAuthUserAssignment(assignmentId);

        Company company = managerAssignment.getCompany();

        List<LeaveRequest> getPendingLeaveRequests = leaveRequestRepository.findByCompanyAndState(
                company,
                LeaveRequestState.PENDING);

        return getPendingLeaveRequests.stream()
                .map(leaveRequest -> modelMapper.map(leaveRequest, LeaveRequestResponseDTO.class))
                .toList();

    }

    private LeaveRequest getLeaveRequest(Long requestId, Long assignmentId) {
        Assignment managerAssignment = getAuthUserAssignment(assignmentId);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + requestId));
        leaveRequest.setManagedByAssignment(managerAssignment);

        if (!leaveRequest.getAssignment().getCompany().equals(managerAssignment.getCompany())) {
            throw new AccessDeniedException("You have not permission to get this leave request.");
        }

        return leaveRequest;
    }

    private Assignment getAuthUserAssignment(Long assignmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Assignment managerAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));

        if (!managerAssignment.getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new AccessDeniedException("You don't have permission to use this assignment.");
        }

        return managerAssignment;
    }
}