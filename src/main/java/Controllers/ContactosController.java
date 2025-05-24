package Controllers;

import Dao.ContactosDao;
import Models.Contactos;

import java.util.List;

public class ContactosController {

    private ContactosDao contactosDao;

    public ContactosController() {
        this.contactosDao = new ContactosDao();
    }

    public void crearContacto(String nombre, String apellido, String telefono, String email){
        Contactos crear = new Contactos(nombre, apellido, telefono, email);
        contactosDao.crearContacto(crear);
        System.out.println("Contacto creado exitosamente :"+crear);
    }

    public List<Contactos> verContactos(){
        return contactosDao.verContacto();
    }

    public void actualizarContactos(int id, String nombre, String apellido, String telefono, String email){
        Contactos actualizar = new Contactos(id, nombre, apellido, telefono, email);
        contactosDao.modificarContacto(actualizar);
        System.out.println("Contacto modificado con exito");
    }

    public void eliminar(int id) {
        Contactos c = new Contactos(id);
        contactosDao.eliminarContacto(c);
    }

}
