/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.client.domain;

import co.unicauca.restaurant.client.access.IClientAccess;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;
import java.util.List;

/**
 *
 * @author braia
 */
public class ClientService {

    private final IClientAccess service;

    /**
     * inyeccion de dependencias
     *
     * @param service un clase concreta que implementa la interfaz de acceso, se
     * instancia con una fabrica
     */
    public ClientService(IClientAccess service) {
        this.service = service;
    }

    /**
     * mascara para guardar un platodeldia, la solicitud se envia al acceso
     *
     * @param plato instancia concreta a enviar su informacion
     * @return
     * @throws Exception
     */
    public String savePlatoEjecutivo(PlatoEjecutivo plato) throws Exception {
        return service.savePlatoEjecutivo(plato); //fklsjdklfjslkdj
    }

    public String saveRestaurante(Restaurant res) throws Exception {
        return service.saveRestaurant(res);
    }

    /**
     * el cliente solicita la modificacion de un parametro en la base de datos
     * para plato dia
     *
     * @param clave identificador del plato
     * @param atributo columna a ser modificada
     * @param valor nuevo valor a almacenar
     * @return true si la operacion es exitosa, false si erra
     * @throws Exception
     */
    public boolean updatePlatoEjecutivo(int clave, String atributo, String valor) throws Exception {
        //validaciones
        return service.updatePlatoEjecutivo(clave, atributo, valor);
    }

    public List<PlatoEjecutivo> listarMenuDia(int resNit) throws Exception {
        return service.listarMenuDia(resNit);
    }

    //aqui los otros servicios, eliminar, modificar listar etc
    public String deletePlatoEjecutivo(int idPlaE) throws Exception {
        return service.deletePlatoEjecutivo(idPlaE);
    }

}
