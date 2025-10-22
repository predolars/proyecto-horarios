package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.LeaveRequestDTO;
import app.fichajes.fichajes.services.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> createLeaveRequest(@Valid @RequestBody LeaveRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveRequestService.createLeaveRequest(dto));
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(leaveRequestService.getAll());
    }
}