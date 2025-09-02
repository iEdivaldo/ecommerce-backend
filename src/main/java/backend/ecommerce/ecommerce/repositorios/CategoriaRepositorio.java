package backend.ecommerce.ecommerce.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.ecommerce.ecommerce.entidades.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {}