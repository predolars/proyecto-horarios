package app.fichajes.fichajes.models.dtos.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AssignmentResponseDTO {
    private Long id;
    private LocalDate assignmentDate;
    private UserResponseDTO user;
    private CompanyResponseDTO company;
    private RoleResponseDTO role;
}