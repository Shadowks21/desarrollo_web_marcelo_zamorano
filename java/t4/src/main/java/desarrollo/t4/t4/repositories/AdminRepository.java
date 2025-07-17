package desarrollo.t4.t4.repositories;

import desarrollo.t4.t4.models.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Foto, Long> {

}
