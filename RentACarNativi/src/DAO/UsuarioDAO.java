/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Models.Usuario;
import java.util.ArrayList;
import java.util.List;
import Database.ConexionDB;

/**
 *
 * @author diego
 */

public class UsuarioDAO {
    private ConexionDB conexionDB;

    public UsuarioDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        String query = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

    try (Connection conn = conexionDB.conectar();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("id_usuario"));
            usuario.setNombre(rs.getString("nombre_usuario"));
            usuario.setRol(rs.getString("rol"));
            usuario.setContraseña(rs.getString("password_hash"));

            usuarios.add(usuario);
        }
        } catch (SQLException e) {
             e.printStackTrace();
        }

       return usuarios;
   }
  

    // Buscar un usuario por su ID
    public Usuario buscarUsuarioPorId(int id) {
        String query = "SELECT * FROM usuarios WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = conexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre_usuario"));
                usuario.setRol(rs.getString("rol"));
                usuario.setContraseña(rs.getString("password_hash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    // Insertar un nuevo usuario
    public boolean insertarUsuario(Usuario usuario) {
        String query = "INSERT INTO usuarios (nombre_usuario, rol, password_hash) VALUES (?, ?, ?)";
        try (Connection conn = conexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getRol());
            stmt.setString(3, usuario.getContraseña());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar un usuario
    public boolean actualizarUsuario(Usuario usuario) {
        String query = "UPDATE usuarios SET nombre_usuario = ?, rol = ?, password_hash = ? WHERE id_usuario = ?";
        try (Connection conn = conexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getRol());
            stmt.setString(3, usuario.getContraseña());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar un usuario
    public boolean eliminarUsuario(int id) {
        String query = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = conexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
