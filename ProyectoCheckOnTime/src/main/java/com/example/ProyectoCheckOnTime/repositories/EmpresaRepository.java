package com.example.ProyectoCheckOnTime.repositories;

import com.example.ProyectoCheckOnTime.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCif(String cif);
}