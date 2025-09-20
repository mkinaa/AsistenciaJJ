package app;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginGUI() {
        setTitle("Iniciar Sesión");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblCorreo = new JLabel("Correo:");
        JTextField tfCorreo = new JTextField();

        JLabel lblContrasena = new JLabel("Contraseña:");
        JPasswordField pfContrasena = new JPasswordField();

        JButton btnLogin = new JButton("Iniciar Sesión");

        add(lblCorreo);
        add(tfCorreo);
        add(lblContrasena);
        add(pfContrasena);
        add(new JLabel()); // espacio vacío
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String correo = tfCorreo.getText();
            String contrasena = new String(pfContrasena.getPassword());

            // Buscar usuario por correo
            Usuario usuario = usuarioDAO.obtenerPorCorreo(correo);
            if (usuario != null && usuario.getContrasena().equals(contrasena)) {
                // Login exitoso, abrir MainGUI
                if (usuario.getRolId() == 2) {
                    new MainGUI(usuario); // administrador
                } else {
                    new UsuarioGUI(usuario); // empleado
                }
                this.dispose(); // cerrar ventana de login
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }

        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
