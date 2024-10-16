/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import DAO.UsuarioDAO;
import Models.Usuario;
import java.util.List;

/**
 *
 * @author diego
 */

public class UsuarioService {
    
    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Método para listar todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    // Método para obtener un usuario por ID
    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioDAO.buscarUsuarioPorId(id); // Asumiendo que el UsuarioDAO tiene este método
    }

    // Método para crear un nuevo usuario
    public boolean crearUsuario(Usuario usuario) {
        if (usuario != null && usuario.getNombre() != null && !usuario.getNombre().isEmpty()) {
            return usuarioDAO.insertarUsuario(usuario);
        } else {
            System.out.println("Datos inválidos para crear un usuario.");
            return false;
        }
    }

    // Método para actualizar un usuario existente
    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario != null && usuario.getId() > 0) {
            return usuarioDAO.actualizarUsuario(usuario); // Asumiendo que el UsuarioDAO tiene este método
        } else {
            System.out.println("Datos inválidos para actualizar el usuario.");
            return false;
        }
    }

    // Método para eliminar un usuario por ID
    public boolean eliminarUsuario(int id) {
        if (id > 0) {
            return usuarioDAO.eliminarUsuario(id); // Asumiendo que el UsuarioDAO tiene este método
        } else {
            System.out.println("ID inválido para eliminar el usuario.");
            return false;
        }
    }
}
