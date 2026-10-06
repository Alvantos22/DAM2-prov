package main.java.Dao.repositories;

import main.java.Dao.Model.Usuario;

public interface UsuarioRepository {
    Usuario get(String username);
}
