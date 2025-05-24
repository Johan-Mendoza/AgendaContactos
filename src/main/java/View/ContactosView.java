package View;

import Controllers.ContactosController;
import Models.Contactos;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class ContactosView {

    private ContactosController contactosController;
    private Scanner entrada;

    public ContactosView() {
        this.contactosController = new ContactosController();
        this.entrada = new Scanner(System.in);
    }

    public void menu(){
        int opcion;

        do{
            System.out.println("========== Menu Contactos ========");
            System.out.println("1. Agregar Contactos");
            System.out.println("2. Ver Contactos");
            System.out.println("3. Modificar Contactos");
            System.out.println("4. Eliminar Contactos");
            System.out.println("5. salir ");
            System.out.println("Seleccione una Opcion");
            opcion = entrada.nextInt();
            entrada.nextLine();

            switch (opcion){
                case 1:
                    agregarContacto();
                    break;

                case 2:
                    verContactos();
                    break;

                case 3:
                    editarContacto();
                    break;

                case 4:
                    eliminar();
                    break;

                case 5:
                    System.out.println("Adios");
                    break;
            }

        }while (opcion != 5);
    }

    public void agregarContacto(){
        System.out.println("=== Agregar Contacto ====");
        System.out.println("Nombre: ");
        String nombre = entrada.nextLine();
        System.out.println("Apellido :");
        String apellido = entrada.nextLine();
        System.out.println("Telefono :");
        String telefono = entrada.nextLine();
        System.out.println("Email :");
        String email = entrada.nextLine();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            System.out.println("Todos los campos son obligatorios.");
        } else {
            contactosController.crearContacto(nombre,apellido,telefono,email);
        }
    }

    public void editarContacto(){
        verContactos();
        List<Contactos> contactos = contactosController.verContactos();

        System.out.println("=== Modificar Contacto ====");
        System.out.println("Ingrese el id del contacto a modificar");
        int id = entrada.nextInt();
        entrada.nextLine();

        Contactos contacto1 = null;
        for (Contactos c : contactos) {
            if (c.getId() == id) {
                contacto1 = c;
                break;
            }
        }

        if (contacto1 != null) {

            System.out.println("Nombre: ");
            String nombre = entrada.nextLine();
            System.out.println("Apellido :");
            String apellido = entrada.nextLine();
            System.out.println("Telefono :");
            String telefono = entrada.nextLine();
            System.out.println("Email :");
            String email = entrada.nextLine();

            if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                System.out.println("Todos los campos son obligatorios.");
            } else {
                contactosController.actualizarContactos(contacto1.getId(), nombre,apellido,telefono,email);
            }

        }else{
            System.out.println("El id ingresado no existe");
        }

    }

    public void verContactos(){
        System.out.println("=== Ver Contactos ===");
        List<Contactos> contactos = contactosController.verContactos();
        if (contactos.isEmpty()){
            System.out.println("No hay contactos registrados");
        }else{
            for(Contactos contacto : contactos){
                System.out.println(contacto);
            }
        }
    }

    public void eliminar(){
        verContactos();
        List<Contactos> contactos = contactosController.verContactos();

        System.out.println("=== Eliminar Contacto ===");
        System.out.println("Ingese el ID a eliminar");
        int id = entrada.nextInt();

        Contactos contacto1 = null;
        for (Contactos c : contactos) {
            if (c.getId() == id) {
                contacto1 = c;
                break;
            }
        }

        if(contacto1 != null){
            contactosController.eliminar(contacto1.getId());
        }

    }
}
