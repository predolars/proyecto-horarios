package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.CreateLeaveRequestDTO;
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

    @PostMapping("/create")
    public ResponseEntity<?> createLeaveRequest(@Valid @RequestBody CreateLeaveRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveRequestService.createLeaveRequest(dto));
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(leaveRequestService.getAll());
    }
}