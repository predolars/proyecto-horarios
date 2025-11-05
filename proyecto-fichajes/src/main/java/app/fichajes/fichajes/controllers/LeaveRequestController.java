package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.LeaveRequestDTO;
import app.fichajes.fichajes.services.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave-requests")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @Autowired
    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> createLeaveRequest(@Valid @RequestBody LeaveRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveRequestService.createLeaveRequest(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(leaveRequestService.getAll());
    }

    @GetMapping("/pending-approve/{assignmentId}")
    @PreAuthorize("hasAnyRole('JEFE', 'ADMIN')")
    public ResponseEntity<Object> getPendingApproveLeaveRequests(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(leaveRequestService.getPendingApproveLeaveRequests(assignmentId));

    }


    @PutMapping("/{assignmentId}/approve/{idLeaveRequest}")
    @PreAuthorize("hasAnyRole('JEFE', 'ADMIN')")
    public ResponseEntity<Object> approveLeaveRequest(@PathVariable Long idLeaveRequest, @PathVariable Long assignmentId) {
        leaveRequestService.approveLeaveRequest(idLeaveRequest, assignmentId);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/{assignmentId}/reject/{idLeaveRequest}")
    @PreAuthorize("hasAnyRole('JEFE', 'ADMIN')")
    public ResponseEntity<Object> rejectLeaveRequest(@PathVariable Long idLeaveRequest, @PathVariable Long assignmentId) {
        leaveRequestService.rejectLeaveRequest(idLeaveRequest, assignmentId);
        return ResponseEntity.ok().build();

    }

}